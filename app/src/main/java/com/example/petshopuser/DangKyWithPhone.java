package com.example.petshopuser;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petshopuser.Helper.ValidationHelper;
import com.example.petshopuser.databinding.ActivityDangKyWithPhoneBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class DangKyWithPhone extends AppCompatActivity {
    private ActivityDangKyWithPhoneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDangKyWithPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.gotoDangNhapDangKyWithPhone.setOnClickListener(v -> startActivity(new Intent(DangKyWithPhone.this, DangNhapActivity.class)));
        binding.btnOTPDangKyWithPhone.setOnClickListener(v -> {
            final String phone = binding.edtPhone.getText().toString().trim();
            if (!validatePhone(phone)) {
                binding.progressBarDangKyWithPhone.setVisibility(View.VISIBLE);
                binding.btnOTPDangKyWithPhone.setVisibility(View.INVISIBLE);
                sendVerificationCode(phone);
            }
        });
    }

    private boolean validatePhone(String phone) {
        if (phone.isEmpty()) {
            binding.edtPhone.setError("Phone number is required");
            binding.edtPhone.requestFocus();
            return true;
        }
        if (phone.length() > 10 || phone.length() < 9) {
            binding.edtPhone.setError("please enter a valid phone");
            binding.edtPhone.requestFocus();
            return true;
        }
        return false;
    }

    public void sendVerificationCode(String phone) {
        binding.progressBarDangKyWithPhone.setVisibility(View.GONE);
        binding.btnOTPDangKyWithPhone.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + phone,
                60L,
                TimeUnit.SECONDS,
                DangKyWithPhone.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(DangKyWithPhone.this, "Đăng ký thành công với số điện thoại: " + "+84" + phone, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(DangKyWithPhone.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationID, forceResendingToken);
                        Toast.makeText(DangKyWithPhone.this, "Mã code đã được gửi đến số điện thoại: " + "0" + phone, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), verifyPhone.class);
                        intent.putExtra("mobile", binding.edtPhone.getText().toString());
                        intent.putExtra("verificationID", verificationID);
                        startActivity(intent);
                    }
                });
    }
}