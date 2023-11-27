package com.example.petshopuser;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petshopuser.dao.DaoUser;
import com.example.petshopuser.databinding.ActivityVerifyPhoneBinding;
import com.example.petshopuser.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class verifyPhone extends AppCompatActivity {
    private ActivityVerifyPhoneBinding binding;
    private EditText[] inputCodes = new EditText[6];
    private FirebaseAuth firebaseAuth;
    private DaoUser databaseUser;
    private String verificationID;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        phone = getIntent().getStringExtra("mobile");
        verificationID = getIntent().getStringExtra("verificationID");

        binding.txtMobile.setText(String.format("+84-%s", phone));

        initInputCodes();
        setupOTPInputs();

        binding.btnXacNhan.setOnClickListener(v -> {
            if (isAnyCodeEmpty()) {
                Toast.makeText(verifyPhone.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder codeBuilder = new StringBuilder();
            for (EditText editText : inputCodes) {
                codeBuilder.append(editText.getText().toString().trim());
            }

            String code = codeBuilder.toString();
            if (verificationID != null) {
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID, code);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                databaseUser = new DaoUser(getApplicationContext());
                                User user = new User(null, null, null, "0" + phone, null, null, null, null, firebaseAuth.getUid());
                                databaseUser.insert(user);

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(verifyPhone.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(verifyPhone.this, "Verification code " + verificationID, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initInputCodes() {
        inputCodes[0] = binding.otpNumberOne;
        inputCodes[1] = binding.optNumberTwo;
        inputCodes[2] = binding.otpNumberThree;
        inputCodes[3] = binding.otpNumberFour;
        inputCodes[4] = binding.otpNumberFive;
        inputCodes[5] = binding.optNumberSix;
    }

    private void setupOTPInputs() {
        for (int i = 0; i < inputCodes.length - 1; i++) {
            final int currentIndex = i;
            inputCodes[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().isEmpty()) {
                        inputCodes[currentIndex + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    private boolean isAnyCodeEmpty() {
        for (EditText editText : inputCodes) {
            if (editText.getText().toString().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
