package com.example.petshopuser.View.User;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petshopuser.DangNhapActivity;
import com.example.petshopuser.EditProfile;
import com.example.petshopuser.MainActivity;
import com.example.petshopuser.R;
import com.example.petshopuser.ThanhToanActivity;
import com.example.petshopuser.View.CartActivity;
import com.example.petshopuser.View.FavoriteActivity;
import com.example.petshopuser.View.HistoryActivity;
import com.example.petshopuser.View.ThayDoiMatKhauActivity;
import com.example.petshopuser.callback.UserCallBack;
import com.example.petshopuser.dao.DaoUser;
import com.example.petshopuser.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrangCaNhan extends AppCompatActivity {
    public static ImageView back;
    private Switch darkModeSwitch;
    RelativeLayout edtEditProfile;
    ImageView profileCircleImageView;
    TextView usernameTextView, email, txtlogout,history,txteditprofile,txtchangepassword,txtGioHang, txtmap;
    TextView txtYeuThich,map,txtVersion,txtMesenger,txtDonHang;
    DaoUser databaseUser;
    FirebaseUser firebaseUser;

    FirebaseAuth fAuth;
    FirebaseDatabase fData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_ca_nhan);

        back = findViewById(R.id.back);
        txtVersion = findViewById(R.id.txtVersion);
        edtEditProfile = findViewById(R.id.edtEditProfile);
        txtmap = findViewById(R.id.map);
        txtMesenger = findViewById(R.id.txtMesenger);
        txtDonHang = findViewById(R.id.txtDonHang);
        txtYeuThich = findViewById(R.id.txtYeuThich);
        profileCircleImageView = findViewById(R.id.profileCircleImageView);
        usernameTextView = findViewById(R.id.usernameTextView);
        email= findViewById(R.id.email);
        txtlogout = findViewById(R.id.txtlogout);
        txtGioHang = findViewById(R.id.txtGioHang);
        txtchangepassword = findViewById(R.id.txtchangepassword);
        map = findViewById(R.id.map);
        txteditprofile = findViewById(R.id.txteditprofile);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        history = findViewById(R.id.history);
        fAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();
        databaseUser = new DaoUser(getApplicationContext());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseUser.getAll(new UserCallBack() {
            @Override
            public void onSuccess(ArrayList<User> lists) {
                for (int i =0 ; i<lists.size();i++){
                    if (lists.get(i).getToken()!=null && lists.get(i).getToken().equalsIgnoreCase(firebaseUser.getUid())){
                        email.setText(lists.get(i).getEmail());
                        usernameTextView.setText(lists.get(i).getName());
                        Picasso.get()
                                .load(lists.get(i).getImage()).into(profileCircleImageView);
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });
//        map.setVisibility(View.GONE);

        darkModeSwitch.setVisibility(View.GONE);

        edtEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iProfile = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(iProfile);
            }
        });

        txteditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iProfile = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(iProfile);
            }
        });

        txtchangepassword.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ThayDoiMatKhauActivity.class));
            finish();
        });

        txteditprofile.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), EditProfile.class)));

        txtMesenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent iMess = new Intent(getApplicationContext(), ChatActivity.class);
//                startActivity(iMess);
            }
        });
        txtDonHang.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CartActivity.class)));
        txtGioHang.setOnClickListener(v -> {
            Intent iGioHang = new Intent(getApplicationContext(), ThanhToanActivity.class);
            startActivity(iGioHang);
        });
        txtYeuThich.setOnClickListener(v -> {
            Intent iYeuThich = new Intent(getApplicationContext(), FavoriteActivity.class);
            startActivity(iYeuThich);
        });

        history.setOnClickListener(v -> {
            Intent iLichSu = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(iLichSu);
        });

        txtlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
//                LoginManager.getInstance().logOut();
                startActivity(new Intent(getApplicationContext(), DangNhapActivity.class));
                finish();
            }
        });

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        txtVersion.setText( getString(R.string.phienban) + " " +packageInfo.versionName);
    }

}