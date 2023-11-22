package com.example.petshopuser.common;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.petshopuser.Helper.ConnectionHelper;
import com.example.petshopuser.R;
import com.example.petshopuser.databinding.ActivityBeginBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


import java.util.List;
import java.util.Locale;

public class BeginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int SPLASH_TIME = 5000;
    public static final int REQUEST_PERMISSION_LOCATION = 1;
    private ActivityBeginBinding binding;
    private GoogleApiClient googleApiClient;
    private SharedPreferences sharedPreferences;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBeginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        binding.backgroudImageBeginActivity.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_anim));
        binding.taoBoiBeginActivity.setAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_anim));
        binding.versionBeginActivity.setAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_anim));

        requestLocationPermission();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(view);
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Quyền truy cập vị trí đã được cấp
            checkLocationService();
        } else {
            // Yêu cầu quyền truy cập vị trí
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
        }
    }

    private void checkLocationService() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGpsEnabled || isNetworkEnabled) {
            // Dịch vụ vị trí đã được bật
            // Tiếp tục thực hiện các hành động cần thiết
            connectGoogleApiClient();

        } else {
            // Hiển thị thông báo yêu cầu bật dịch vụ vị trí
            showLocationServiceAlertDialog();
        }
    }

    private void showLocationServiceAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Enable Location Service")
                .setMessage("We need your location to provide accurate results.")
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Mở cài đặt để bật dịch vụ vị trí
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void connectGoogleApiClient() {
        googleApiClient.connect();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);  // 10 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    onLocationChanged(location);
                }
            }
        };
        // Kiểm tra xem quyền vị trí đã được cấp chưa
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            // Nếu chưa được cấp quyền, yêu cầu quyền từ người dùng
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                connectGoogleApiClient();
            }
        }
    }

    // Lắng nghe sự kiện khi kết nối thành công với dịch vụ vị trí
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
            binding.versionBeginActivity.setText( getString(R.string.phienban) + " " +packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(() -> {
            startActivity(new Intent(BeginActivity.this, IntroActivity.class));
            finish();
        }, SPLASH_TIME);
    }

    // Lắng nghe sự kiện thay đổi vị trí của thiết bị
    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            saveLocationToSharedPreferences(addresses.get(0).getAddressLine(0));
        } catch (Exception e) {
            Log.e("thien", "onLocationChanged: Error getting address", e);
        }
    }

    // Lưu vị trí đã lấy được vào SharedPreferences
    private void saveLocationToSharedPreferences(String locality) {
        sharedPreferences = getSharedPreferences("toado", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("diaChi", locality);
        editor.apply();
    }

    // Xử lý khi kết nối bị gián đoạn
    @Override
    public void onConnectionSuspended(int cause) {
        ConnectionHelper.handleConnectionSuspended(cause, this);
    }

    // Xử lý khi kết nối thất bại
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ConnectionHelper.handleConnectionFailed(connectionResult, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        googleApiClient.disconnect();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }

        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}