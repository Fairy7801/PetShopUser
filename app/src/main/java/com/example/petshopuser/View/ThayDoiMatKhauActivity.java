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
import com.example.petshopuser.databinding.ActivityThayDoiMatKhauBinding;
import com.example.petshopuser.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ThayDoiMatKhauActivity extends AppCompatActivity {
    private ActivityThayDoiMatKhauBinding binding;
    DaoUser databaseUser;
    FirebaseUser firebaseUser;
    ArrayList<User> dataUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThayDoiMatKhauBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dataUser = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        binding.back.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), TrangCaNhan.class));
            finish();
        });

        binding.btnchangepassword.setOnClickListener(v -> handleChangePassword());
    }

    private void handleChangePassword() {
        if (isInputValid()) {
            for (User user : dataUser) {
                if (user.getToken().equalsIgnoreCase(firebaseUser.getUid())) {
                    if (user.getPass().equals(binding.edtoldpass.getText().toString().trim())) {
                        updatePassword();
                    } else {
                        Toast.makeText(getApplicationContext(), "Password cũ bạn nhập không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private boolean isInputValid() {
        if (binding.edtoldpass.getText().toString().trim().isEmpty() ||
                binding.edtpassnew.getText().toString().trim().isEmpty() ||
                binding.edtxacnhanpass.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Vui Lòng Nhập Đầy Đủ 3 Trường", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.edtoldpass.getText().toString().trim().length() < 6 ||
                binding.edtpassnew.getText().toString().trim().length() < 6 ||
                binding.edtxacnhanpass.getText().toString().trim().length() < 6) {
            Toast.makeText(getApplicationContext(), "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updatePassword() {
        if (binding.edtpassnew.getText().toString().trim().equalsIgnoreCase(binding.edtoldpass.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Password cũ với password mới không được trùng", Toast.LENGTH_SHORT).show();
        } else {
            if (binding.edtxacnhanpass.getText().toString().equalsIgnoreCase(binding.edtpassnew.getText().toString())) {
                databaseUser = new DaoUser(this);
                firebaseUser.updatePassword(binding.edtpassnew.getText().toString().trim())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                databaseUser.updateField(firebaseUser.getUid(), "pass", binding.edtpassnew.getText().toString().trim());
                                Toast.makeText(getApplicationContext(), "Mật khẩu đã được cập nhật.", Toast.LENGTH_SHORT).show();
                            }
                        });
                startActivity(new Intent(getApplicationContext(), TrangCaNhan.class));
            } else {
                Toast.makeText(getApplicationContext(), "Password Xác nhận phải trùng Với Password mới", Toast.LENGTH_SHORT).show();
            }
        }
    }
}