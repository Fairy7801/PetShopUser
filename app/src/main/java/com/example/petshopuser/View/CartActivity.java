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
import com.example.petshopuser.View.User.TrangCaNhan;
import com.example.petshopuser.adapter.CartAdapter;
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

    RecyclerView rcvcart;
    CartAdapter cartAdapter =null;
    ArrayList<HDCT> hdctArrayList;
    LocalStorage localStorage;
    Gson gson;
    ImageView back;
    RelativeLayout linearbackground;
    ArrayList<Order> orderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        back = findViewById(R.id.back);
        rcvcart = findViewById(R.id.rcvcart);
        localStorage = new LocalStorage(getApplicationContext());

        linearbackground = findViewById(R.id.linearbackground);

        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        gson = new Gson();
        hdctArrayList = new ArrayList<>();
        orderArrayList = new ArrayList<>();
        orderArrayList = getCartList();
        if (orderArrayList.size() != 0) {
            cartAdapter = new CartAdapter(orderArrayList, getApplicationContext());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            rcvcart.setLayoutManager(linearLayoutManager);
            rcvcart.setAdapter(cartAdapter);

        }else {
            linearbackground.setBackgroundResource(R.drawable.empty_cart);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TrangCaNhan.class);
                startActivity(i);
            }
        });

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