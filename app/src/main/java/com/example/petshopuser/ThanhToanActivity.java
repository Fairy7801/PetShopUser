package com.example.petshopuser;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.petshopuser.Helper.NumberFormatHelper;
import com.example.petshopuser.Notification.DataHoaDon;
import com.example.petshopuser.Notification.SenderHoaDon;
import com.example.petshopuser.View.User.TrangCaNhan;
import com.example.petshopuser.adapter.CartAdapter;
import com.example.petshopuser.callback.UserCallBack;
import com.example.petshopuser.dao.DaoHDCT;
import com.example.petshopuser.dao.DaoUser;
import com.example.petshopuser.databinding.ActivityThanhToanBinding;
import com.example.petshopuser.local.LocalStorage;
import com.example.petshopuser.model.HDCT;
import com.example.petshopuser.model.Order;
import com.example.petshopuser.model.Token;
import com.example.petshopuser.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ThanhToanActivity extends AppCompatActivity {
    private ActivityThanhToanBinding binding;
    CartAdapter cartAdapter =null;
    ArrayList<HDCT> hdctArrayList;
    LocalStorage localstorage;
    Gson gson;
    DaoHDCT daoHDCT;
    DaoUser daoUser;
    double tongtien=0;
    FirebaseUser user;
    ArrayList<Order> orderArrayList;
    private RequestQueue requestQueue;
    String nameuser;
    String namestore;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityThanhToanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        localstorage = new LocalStorage(this);
        getWindow().setStatusBarColor(ContextCompat.getColor(ThanhToanActivity.this, R.color.colorPrimary));

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        setSupportActionBar(binding.toolbarThanhToanActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarTitleThanhToanActivity.setText("Đơn Hàng");
        binding.toolbarTitleThanhToanActivity.setTextSize(30);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        gson = new Gson();
        hdctArrayList = new ArrayList<>();
        orderArrayList = new ArrayList<>();
        orderArrayList = getCartList();
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("HDCT");
        String keyhdct = databaseReference.push().getKey();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        SimpleDateFormat tg = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String thoigian = tg.format(new Date());
        hdctArrayList.clear();

        for (Order order: getCartList()){
            namestore = "Ppq6eE9AraU2LchCsOwk92JrRQM2";
            nameuser =  order.getUser().getEmail();
        }
        cartAdapter = new CartAdapter(orderArrayList,ThanhToanActivity.this, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ThanhToanActivity.this,LinearLayoutManager.VERTICAL,false);
        binding.rcvcartThanhToanActivity.setLayoutManager(linearLayoutManager);
        binding.rcvcartThanhToanActivity.setAdapter(cartAdapter);

        if (getCartList().size() != 0){
            cartAdapter.notifyDataSetChanged();
            for (Order order: getCartList()){
                tongtien += order.getSoLuong() * order.getProducts().getPrice();
            }

            binding.txtTienTongThanhToanActivity.setText(NumberFormatHelper.formatNumber(tongtien)+" VNĐ");
            binding.btnInsertcartThanhToanActivity.setOnClickListener(v -> {
                HDCT hoadonchitiet = new HDCT(currentDateandTime,thoigian, keyhdct, keyhdct, false,user.getUid(),getCartList());
                hdctArrayList.add(hoadonchitiet);
                daoHDCT = new DaoHDCT(ThanhToanActivity.this);
                for (HDCT hdct: hdctArrayList){
                    daoHDCT.insert(hdct);
                }
                localstorage.deleteCart();
                updateUIForEmptyCart();
            });
        }else {
            updateUIForEmptyCart();
        }

        daoUser = new DaoUser( ThanhToanActivity.this);
        daoUser.getAll(new UserCallBack() {
            @Override
            public void onSuccess(ArrayList<User> lists) {
                for (int i = 0; i < lists.size(); i++) {
                    String userToken = lists.get(i).getToken();
                    if (userToken != null && userToken.matches(user.getUid())) {
                        binding.txtaddressThanhToanActivity.setText(lists.get(i).getAddress());
                        break;
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });
        binding.toolbarThanhToanActivity.setNavigationOnClickListener(v -> {
            startActivity(new Intent(ThanhToanActivity.this, TrangCaNhan.class));
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
            finish();
        });
    }
    public ArrayList<Order> getCartList() {
        if (localstorage.getCart() != null) {
            String jsonCart = localstorage.getCart();
            Type type = new TypeToken<List<Order>>() {}.getType();
            orderArrayList = gson.fromJson(jsonCart, type);
            return orderArrayList;
        }
        return orderArrayList;
    }

    // Phương thức để cập nhật giao diện khi giỏ hàng trống
    private void updateUIForEmptyCart() {
        binding.rcvcartThanhToanActivity.setVisibility(View.GONE);
        binding.linearbackgroundThanhToanActivity.setBackgroundResource(R.drawable.empty_cart);
        binding.scrollViewThanhToanActivity.setVisibility(View.GONE);
        binding.linear1ThanhToanActivity.setVisibility(View.GONE);
    }
}