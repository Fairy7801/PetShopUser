package com.example.petshopuser;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petshopuser.Helper.ValidationHelper;
import com.example.petshopuser.dao.DaoUser;
import com.example.petshopuser.databinding.ActivityDangKyBinding;
import com.example.petshopuser.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

public class DangKyActivity extends AppCompatActivity {
    private ActivityDangKyBinding binding;
    private FirebaseAuth mAuth;
    private int dayOfMonth, month, year, yearCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDangKyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.buttonDateDangKyActivity.setOnClickListener(v -> buttonSelectDate());
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        yearCurrent = year;
        month = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        binding.btnOtpDangKyActivity.setOnClickListener(v -> performRegistration());

        binding.btnDangNhapDangKyActivity.setOnClickListener(v ->
                startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class)));
    }

    // Phương thức để validate dữ liệu
    private boolean validateData(String email, String password, String confpassword, String hoten, String phone) {
        if (!ValidationHelper.isNotEmpty(hoten)) {
            binding.fullNameDangKyActivity.setError("Bắt buộc");
            return false;
        }

        if (!ValidationHelper.isValidEmail(email)) {
            binding.emailDangKyActivity.setError("Email không hợp lệ.");
            return false;
        }

        if (!ValidationHelper.isPasswordValid(password)) {
            binding.passwordDangKyActivity.setError("Mật khẩu phải lớn hơn 6 ký tự");
            return false;
        }

        if (!ValidationHelper.isNotEmpty(confpassword)) {
            binding.confPasswordDangKyActivity.setError("Bắt buộc");
            return false;
        }

        if (!password.equals(confpassword)) {
            binding.confPasswordDangKyActivity.setError("Mật khẩu không khớp");
            return false;
        }

        if (!ValidationHelper.isNotEmpty(phone)) {
            binding.edtPhoneDangKyActivity.setError("Bắt buộc");
            return false;
        }

        // Nếu không có lỗi validate, trả về true
        return true;
    }

    // Phương thức để thực hiện đăng ký
    private void performRegistration() {
        final String email = binding.emailDangKyActivity.getText().toString().trim();
        final String password = binding.passwordDangKyActivity.getText().toString();
        final String confpass = binding.confPasswordDangKyActivity.getText().toString();
        final String hoten = binding.fullNameDangKyActivity.getText().toString();
        final String phone = binding.edtPhoneDangKyActivity.getText().toString().trim();
        final String ngaysinh = binding.editTextDateDangKyActivity.getText().toString().trim();
        if (validateData(email, password, confpass, hoten, phone)) {
            final String gioitinh;
            if (binding.rdbNamDangKyActivity.isChecked()) {
                gioitinh = "Nam";
            } else if (binding.rdbNuDangKyActivity.isChecked()) {
                gioitinh = "Nữ";
            } else if (binding.rdbKhacDangKyActivity.isChecked()) {
                gioitinh = "Khác";
            } else {
                gioitinh = "";
            }

            binding.progressBarDangKyActivity.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        binding.progressBarDangKyActivity.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            DaoUser databaseUser = new DaoUser(DangKyActivity.this);
                            User user = new User(email, password, hoten, phone, null, null, ngaysinh, gioitinh, mAuth.getUid());
                            databaseUser.insert(user);
                            Toast.makeText(DangKyActivity.this, "Đăng Ký Thành Công.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
                            finish();
                        } else {
                            Toast.makeText(DangKyActivity.this, "Đăng ký Thất Bại.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Phương thức chọn ngày sinh
    private void buttonSelectDate() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            binding.editTextDateDangKyActivity.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            if (year > yearCurrent) {
                Toast.makeText(DangKyActivity.this, "Sai định dạng ngày/tháng/năm", Toast.LENGTH_SHORT).show();
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener,
                year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }
}