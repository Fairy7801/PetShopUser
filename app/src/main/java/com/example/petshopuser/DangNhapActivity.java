package com.example.petshopuser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petshopuser.Helper.ConnectionHelper;
import com.example.petshopuser.Helper.ValidationHelper;
import com.example.petshopuser.View.User.QuenMatKhau;
import com.example.petshopuser.dao.DaoUser;
import com.example.petshopuser.databinding.ActivityDangNhapBinding;
import com.example.petshopuser.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class DangNhapActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, FirebaseAuth.AuthStateListener {
    private DaoUser daoUser;
    private ActivityDangNhapBinding binding;
    private FirebaseAuth firebaseAuth;
    public static int KEY_LOGIN_GOOGLE = 99;
    private GoogleApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDangNhapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        createClientSigninGoogle();

        binding.loginBtnDangNhapActivity.setOnClickListener(v -> login());
        binding.btnLoginGoogleDangNhapActivity.setOnClickListener(v -> loginGoogle(apiClient));
        binding.btnDangKyDangNhapActivity.setOnClickListener(v -> startActivity(new Intent(DangNhapActivity.this, DangKyActivity.class)));
        binding.btnLoginPhoneDangNhapActivity.setOnClickListener(v -> startActivity(new Intent(DangNhapActivity.this, DangKyWithPhone.class)));
        binding.btnQuenMatKhauDangNhapActivity.setOnClickListener(v -> startActivity(new Intent(DangNhapActivity.this, QuenMatKhau.class)));
    }

    private void createClientSigninGoogle() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
                .build();
    }

    private void loginGoogle(GoogleApiClient apiClient) {
        startActivityForResult(new Intent(Auth.GoogleSignInApi.getSignInIntent(apiClient)), KEY_LOGIN_GOOGLE);
    }

    private void authenticateFirebase(String tokenID) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(tokenID, null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Firebase authentication success
                        Toast.makeText(DangNhapActivity.this, "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DangNhapActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(DangNhapActivity.this, "Login Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEY_LOGIN_GOOGLE && resultCode == RESULT_OK) {
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (signInResult != null && signInResult.isSuccess()) {
                GoogleSignInAccount account = signInResult.getSignInAccount();
                if (account != null) {
                    daoUser = new DaoUser(DangNhapActivity.this);
                    User user = new User(account.getEmail(), null, account.getDisplayName(), null, null, null, null, null, firebaseAuth.getUid());
                    daoUser.insert(user);

                    String tokenID = account.getIdToken();
                    authenticateFirebase(tokenID);
                }
            }
        }
    }

    private void login() {
        final String email = binding.emailDangNhapActivity.getText().toString().trim();
        final String password = binding.passwordDangNhapActivity.getText().toString();

        if (validateLogin(email, password)) {
            binding.progressBarDangNhapActivity.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        binding.progressBarDangNhapActivity.setVisibility(View.GONE);
                        if (task.isSuccessful() && task.getResult().getUser() != null) {
                            // Firebase authentication success
                        Toast.makeText(DangNhapActivity.this, "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DangNhapActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(DangNhapActivity.this, "Login Thất Bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean validateLogin(String email, String password) {
        // Validate email
        if (!ValidationHelper.isValidEmail(email)) {
            binding.emailDangNhapActivity.setError("Email không hợp lệ");
            return false;
        }

        if (!ValidationHelper.isNotEmpty(email)) {
            binding.emailDangNhapActivity.setError("Bắt buộc");
            return false;
        }

        // Validate password
        if (!ValidationHelper.isPasswordValid(password)) {
            binding.passwordDangNhapActivity.setError("Mật khẩu phải lớn hơn 6 ký tự");
            return false;
        }

        if (!ValidationHelper.isNotEmpty(password)) {
            binding.passwordDangNhapActivity.setError("Bắt buộc");
            return false;
        }

        // Nếu không có lỗi validate, trả về true
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user !=null){
            Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
            startActivity(intent);
        };
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}