package com.example.petshopuser.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.petshopuser.R;
import com.example.petshopuser.ThanhToanActivity;
import com.example.petshopuser.View.User.TrangCaNhan;
import com.example.petshopuser.adapter.CartAdapter;
import com.example.petshopuser.databinding.ActivityCartBinding;
import com.example.petshopuser.local.LocalStorage;
import com.example.petshopuser.model.HDCT;
import com.example.petshopuser.model.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private CartAdapter cartAdapter =null;
    private ArrayList<HDCT> hdctArrayList;
    private LocalStorage localStorage;
    private Gson gson;
    private ArrayList<Order> orderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        localStorage = new LocalStorage(getApplicationContext());

        gson = new Gson();
        hdctArrayList = new ArrayList<>();
        orderArrayList = new ArrayList<>();
        orderArrayList = getCartList();
        if (orderArrayList.size() != 0) {
            cartAdapter = new CartAdapter(orderArrayList, getApplicationContext(), true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            binding.rcvcartCartActivity.setLayoutManager(linearLayoutManager);
            binding.rcvcartCartActivity.setAdapter(cartAdapter);

        }else {
            binding.linearbackgroundCartActivity.setBackgroundResource(R.drawable.empty_cart);
        }

        binding.backCartActivity.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), TrangCaNhan.class));
        });
        binding.btnDatHangCartActivity.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, ThanhToanActivity.class)));

    }
    public ArrayList<Order> getCartList () {
        if (localStorage.getCart() != null) {
            String jsonCart = localStorage.getCart();
            Log.d("CART : ", jsonCart);
            Type type = new TypeToken<List<Order>>() {
            }.getType();
            orderArrayList = gson.fromJson(jsonCart, type);

            return orderArrayList;
        }
        return orderArrayList;
    }
}