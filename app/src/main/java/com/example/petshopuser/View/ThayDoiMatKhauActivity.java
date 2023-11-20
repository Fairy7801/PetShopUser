package com.example.petshopuser.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.petshopuser.R;
import com.example.petshopuser.View.User.TrangCaNhan;
import com.example.petshopuser.callback.UserCallBack;
import com.example.petshopuser.dao.DaoUser;
import com.example.petshopuser.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ThayDoiMatKhauActivity extends AppCompatActivity {
    ImageView back;
    EditText edtoldpass, edtpassnew, edtxacnhanpass;
    DaoUser databaseUser;
    String mail, name, phone, diachi, anh, pass, gioitinh, ngaysinh;
    FirebaseUser firebaseUser;
    Button btnchangepassword;
    ArrayList<User> dataUser;
    ImageView anhdaidien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thay_doi_mat_khau);

        back = findViewById(R.id.back);
        edtoldpass = findViewById(R.id.edtoldpass);
        edtpassnew = findViewById(R.id.edtpassnew);
        edtxacnhanpass = findViewById(R.id.edtxacnhanpass);
        anhdaidien = findViewById(R.id.anhdaidien);
        btnchangepassword = findViewById(R.id.btnchangepassword);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dataUser = new ArrayList<>();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TrangCaNhan.class);
                startActivity(i);
                finish();
            }
        });

        btnchangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseUser = new DaoUser(getApplicationContext());
                databaseUser.getAll(new UserCallBack() {
                    @Override
                    public void onSuccess(ArrayList<User> lists) {
                        dataUser.clear();
                        dataUser.addAll(lists);
                    }

                    @Override
                    public void onError(String message) {

                    }
                });

                if (edtoldpass.getText().toString().trim().isEmpty() || edtpassnew.getText().toString().trim().isEmpty() || edtxacnhanpass.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui Lòng Nhập Đầy Đủ 3 Trường", Toast.LENGTH_SHORT).show();
                } else if (edtoldpass.getText().toString().trim().length() < 6 ||
                        edtpassnew.getText().toString().trim().length() < 6 || edtxacnhanpass.getText().toString().trim().length() < 6) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu phải có ít nhất 6 ký tự!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < dataUser.size(); i++) {
                        if (dataUser.get(i).getToken().equalsIgnoreCase(firebaseUser.getUid())) {
                            if (!(edtoldpass.getText().toString().trim().equalsIgnoreCase(dataUser.get(i).getPass()))) {
                                Toast.makeText(getApplicationContext(), "Password cũ bạn nhập không đúng", Toast.LENGTH_SHORT).show();
                            } else {
                                pass = dataUser.get(i).getPass();
                                name = dataUser.get(i).getName();
                                phone = dataUser.get(i).getPhone();
                                diachi = dataUser.get(i).getAddress();
                                mail = dataUser.get(i).getEmail();
                                anh = dataUser.get(i).getImage();
                                gioitinh = dataUser.get(i).getSex();
                                ngaysinh = dataUser.get(i).getBirthday();


                                if (edtpassnew.getText().toString().trim().equalsIgnoreCase(edtoldpass.getText().toString().trim())) {
                                    Toast.makeText(getApplicationContext(), "Password cũ với password mới không được trùng", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (!(edtxacnhanpass.getText().toString().equalsIgnoreCase(edtpassnew.getText().toString()))) {
                                        Toast.makeText(getApplicationContext(), "Password Xác nhận phải trùng Với Password mới", Toast.LENGTH_SHORT).show();
                                    } else {

                                        User user = new User(mail, edtpassnew.getText().toString().trim(), name, phone, anh, diachi, ngaysinh, gioitinh, firebaseUser.getUid());
                                        databaseUser.update(user);
                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        String newPassword = edtpassnew.getText().toString().trim();

                                        firebaseUser.updatePassword(newPassword)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "User password updated.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        Intent intent = new Intent(getApplicationContext(), TrangCaNhan.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        });

    }
}