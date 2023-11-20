package com.example.petshopuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class DangKyWithPhone extends AppCompatActivity {
    EditText edtPhone;
    Button btnOTP;
    TextView gotoDangNhap;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky_with_phone);

        edtPhone = findViewById(R.id.edtPhone);
        btnOTP = findViewById(R.id.btnOTP);
        gotoDangNhap = findViewById(R.id.gotoDangNhap);
        progressBar = findViewById(R.id.progressBar);

        gotoDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangKyWithPhone.this,DangNhapActivity.class);
                startActivity(intent);
            }
        });


        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = edtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)){
                    edtPhone.setError("Bắt buộc");
                    return;
                }
                sendVerificationCode(phone);
                progressBar.setVisibility(View.VISIBLE);
                btnOTP.setVisibility(View.INVISIBLE);
//                Intent intent = new Intent(getApplicationContext(),verifyPhone.class);
//                intent.putExtra("phone",edtPhone.getText().toString());
//                startActivity(intent);
            }
        });
    }

    public void sendVerificationCode(String phone) {

        if (phone.isEmpty()) {
            edtPhone.setError("Phone number is required");
            edtPhone.requestFocus();
        }
        if (phone.length() > 10 || phone.length() < 9) {
            edtPhone.setError("please enter a valid phone");
            edtPhone.requestFocus();
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + phone,
                60L,
                TimeUnit.SECONDS,
                DangKyWithPhone.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE);
                        btnOTP.setVisibility(View.VISIBLE);
                        Toast.makeText(DangKyWithPhone.this, "Happy new year-COMPLETED", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        btnOTP.setVisibility(View.VISIBLE);
                        Toast.makeText(DangKyWithPhone.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationID, forceResendingToken);
                        progressBar.setVisibility(View.GONE);
                        btnOTP.setVisibility(View.VISIBLE);
                        Toast.makeText(DangKyWithPhone.this, "Happy new year-CODESENT", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), verifyPhone.class);
                        intent.putExtra("mobile", edtPhone.getText().toString());
                        intent.putExtra("verificationID", verificationID);
                        startActivity(intent);
                    }
                });
    }
}