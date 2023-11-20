package com.example.petshopuser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopuser.View.User.TrangCaNhan;
import com.example.petshopuser.callback.UserCallBack;
import com.example.petshopuser.dao.DaoUser;
import com.example.petshopuser.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    public static ImageView back;
    DaoUser databaseUser;
    EditText edtname, edtphone, edtmail, edtaddress,edtNgaySinh;
    TextView txtGioiTinh;
    RadioButton rdbNam, rdbNu, rdbKhac;
    RadioGroup RGroup;
    Button btnupdateprofile;
    CircleImageView imgprofile;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    String pass;

    public int Year;
    public int Month;
    public int dayOfMonth;
    public int yearCurrent;
    public ImageButton buttonDate;

    FirebaseUser firebaseUser;
    String mail, name, phone, diachi, anh, ngaysinh, gioitinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        back = findViewById(R.id.back);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        edtname = findViewById(R.id.profilename);
        edtphone = findViewById(R.id.profilephone);
        edtmail = findViewById(R.id.profilemail);
        edtaddress = findViewById(R.id.profileaddress);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        buttonDate = findViewById(R.id.button_date);
        RGroup = findViewById(R.id.RGroup);
        rdbNam = findViewById(R.id.rdbNam);
        rdbNu = findViewById(R.id.rdbNu);
        rdbKhac = findViewById(R.id.rdbKhac);
        txtGioiTinh = findViewById(R.id.txtGioiTinh);
        imgprofile = findViewById(R.id.imgProfile);
        btnupdateprofile = findViewById(R.id.updateprofile);

        final Calendar c = Calendar.getInstance();
        this.Year = c.get(Calendar.YEAR);
        this.yearCurrent = c.get(Calendar.YEAR);
        this.Month = c.get(Calendar.MONTH);
        this.dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        rdbNam.isChecked();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseUser = new DaoUser(getApplicationContext());
        databaseUser.getAll(new UserCallBack() {
            @Override
            public void onSuccess(ArrayList<User> lists) {
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).getToken()!=null && lists.get(i).getToken().equalsIgnoreCase(firebaseUser.getUid())) {
                        name = lists.get(i).getName();
                        diachi = lists.get(i).getAddress();
                        mail = lists.get(i).getEmail();
                        phone = lists.get(i).getPhone();
                        pass = lists.get(i).getPass();
                        anh = lists.get(i).getImage();
                        ngaysinh = lists.get(i).getBirthday();
                        gioitinh = lists.get(i).getSex();
                    }
                }
                edtaddress.setText(diachi);
                edtmail.setText(mail);
                edtname.setText(name);
                edtphone.setText(phone);
                if (anh ==null){
                    Picasso.get().load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").into(imgprofile);
                }else if (anh !=null){
                    Picasso.get().load(anh).into(imgprofile);
                }
                edtNgaySinh.setText(ngaysinh);
                txtGioiTinh.setText(gioitinh);
                if (gioitinh == "Nam"){
                    rdbNam.isChecked();
                } else if (gioitinh == "Nữ") {
                    rdbNu.isChecked();
                } else {
                    rdbKhac.isChecked();
                }

            }

            @Override
            public void onError(String message) {

            }
        });

        /*Hàm thêm ảnh*/

        imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        /*End Hàm thêm ảnh*/

        //Hàm Date
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSelectDate();
            }
        });
        RGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rdbNam.isChecked()) {
                    txtGioiTinh.setText("Nam");
                }else if (rdbNu.isChecked()) {
                    txtGioiTinh.setText("Nữ");
                } else if (rdbKhac.isChecked()) {
                    txtGioiTinh.setText("Khác");
                }
            }
        });

        btnupdateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath != null) {
                    String email = edtmail.getText().toString().trim(); // buiducthien
                    String phone = edtphone.getText().toString().trim();
                    String ten = edtname.getText().toString().trim();
                    String diachi = edtaddress.getText().toString().trim();
                    String ngaysinh = edtNgaySinh.getText().toString().trim();
                    String gioitinh = txtGioiTinh.getText().toString().trim();
                    if (email.isEmpty() || phone.isEmpty() || ten.isEmpty() || diachi.isEmpty() || ngaysinh.isEmpty() || gioitinh.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
                    } else if (!email.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
                        Toast.makeText(getApplicationContext(), "Email Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                    } else if (phone.length() < 10 || phone.length() > 12) {
                        Toast.makeText(getApplicationContext(), "Vui lòng nhập đúng số điện thoại!", Toast.LENGTH_SHORT).show();
                    } else {
                        change();
                    }
                } else  {
                    String email = edtmail.getText().toString().trim();
                    String phone = edtphone.getText().toString().trim();
                    String ten = edtname.getText().toString().trim();
                    String diachi = edtaddress.getText().toString().trim();
                    String ngaysinh = edtNgaySinh.getText().toString().trim();
                    String gioitinh = txtGioiTinh.getText().toString().trim();
                    if (email.isEmpty() || phone.isEmpty() || ten.isEmpty() || diachi.isEmpty() || ngaysinh.isEmpty() || gioitinh.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
                    } else if (!email.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
                        Toast.makeText(getApplicationContext(), "Email Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                    } else if (phone.length() < 10 || phone.length() > 12) {
                        Toast.makeText(getApplicationContext(), "Vui lòng nhập đúng số điện thoại!", Toast.LENGTH_SHORT).show();
                    } else {
                        User store = new User();
                        store.setEmail(edtmail.getText().toString());
                        store.setName(edtname.getText().toString());
                        store.setPhone(edtphone.getText().toString());
                        store.setAddress(edtaddress.getText().toString());
                        store.setPass(pass);
                        store.setAddress(edtaddress.getText().toString());
                        store.setBirthday(edtNgaySinh.getText().toString());
                        store.setSex(txtGioiTinh.getText().toString());
                        store.setImage(anh);
                        store.setToken(firebaseUser.getUid());
                        databaseUser = new DaoUser(getApplicationContext());
                        databaseUser.update(store);
                        Intent intent = new Intent(getApplicationContext(), TrangCaNhan.class);
                        startActivity(intent);
                        finish();
                    }
                }


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrangCaNhan.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void buttonSelectDate() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                edtNgaySinh.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                Year = year;
                Month = monthOfYear;
                dayOfMonth = dayOfMonth;
                if (Year > yearCurrent){
                    Toast.makeText(getApplicationContext(), "Sai định dạng ngày/tháng/năm", Toast.LENGTH_SHORT).show();
                }
            }
        };
        DatePickerDialog datePickerDialog = null;
        datePickerDialog = new DatePickerDialog(this, dateSetListener, Year, Month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void change() {

        final StorageReference imageFolder = storageReference.child("Users/" + UUID.randomUUID().toString());
        imageFolder.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        User store = new User();
                        store.setEmail(edtmail.getText().toString());
                        store.setName(edtname.getText().toString());
                        store.setPhone(edtphone.getText().toString());
                        store.setAddress(edtaddress.getText().toString());
                        store.setBirthday(edtNgaySinh.getText().toString());
                        store.setSex(txtGioiTinh.getText().toString());
                        store.setImage(uri.toString());
                        store.setPass(pass);
                        store.setToken(firebaseUser.getUid());
                        databaseUser = new DaoUser(getApplicationContext());
                        databaseUser.update(store);
                        Intent intent = new Intent(getApplicationContext(), TrangCaNhan.class);
                        startActivity(intent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getApplicationContext().getContentResolver(),
                                filePath);
                imgprofile.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}