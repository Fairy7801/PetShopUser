package com.example.petshopuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopuser.dao.DaoUser;
import com.example.petshopuser.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class verifyPhone extends AppCompatActivity {
    private EditText inputCode1,inputCode2,inputCode3,inputCode4,inputCode5,inputCode6;
    Button btnXacNhan;
    TextView txtResenOTP;
    //Boolean otpValid = true;
    FirebaseAuth firebaseAuth;
    private DaoUser DatabaseUser;
    PhoneAuthCredential phoneAuthCredential;
    PhoneAuthProvider.ForceResendingToken token;
    String verificationID;
    String  phone;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        firebaseAuth = FirebaseAuth.getInstance();
        phone = getIntent().getStringExtra("mobile");
        verificationID =getIntent().getStringExtra("verificationID");
        TextView txtMobile = findViewById(R.id.txtMobile);
        txtMobile.setText(String.format(
                "+84-%s",phone));

        inputCode1 = findViewById(R.id.otpNumberOne);
        inputCode2 = findViewById(R.id.optNumberTwo);
        inputCode3 = findViewById(R.id.otpNumberThree);
        inputCode4 = findViewById(R.id.otpNumberFour);
        inputCode5 = findViewById(R.id.otpNumberFive);
        inputCode6 = findViewById(R.id.optNumberSix);

        btnXacNhan = findViewById(R.id.btnXacNhan);
        txtResenOTP = findViewById(R.id.txtResenOTP);

        setupOTPInputs();
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputCode1.getText().toString().trim().isEmpty()
                        || inputCode2.getText().toString().trim().isEmpty()
                        || inputCode3.getText().toString().trim().isEmpty()
                        || inputCode4.getText().toString().trim().isEmpty()
                        || inputCode5.getText().toString().trim().isEmpty()
                        || inputCode6.getText().toString().trim().isEmpty()){
                    Toast.makeText(verifyPhone.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code =
                        inputCode1.getText().toString()
                                + inputCode2.getText().toString()
                                + inputCode3.getText().toString()
                                + inputCode4.getText().toString()
                                + inputCode5.getText().toString()
                                + inputCode6.getText().toString();

                if (verificationID != null) {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID, code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    progressBar.setVisibility(View.GONE);
//                                    buttonVerify.setVisibility(View.VISIBLE);
                                    if(task.isSuccessful()){

                                        DatabaseUser = new DaoUser(getApplicationContext());
                                        User user = new User(null,null,null,"0"+phone,null,null,null,null,firebaseAuth.getUid());
                                        DatabaseUser.insert(user);

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(verifyPhone.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(verifyPhone.this, "verificationcode " + verificationID, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setupOTPInputs(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}