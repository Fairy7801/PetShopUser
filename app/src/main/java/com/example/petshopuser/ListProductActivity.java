package com.example.petshopuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.petshopuser.adapter.ProductAdapter;
import com.example.petshopuser.callback.ProductsCallBack;
import com.example.petshopuser.dao.DaoProducts;
import com.example.petshopuser.model.Products;

import java.util.ArrayList;

public class ListProductActivity extends AppCompatActivity {
    ArrayList<Products> foodArrayList;
    DaoProducts databaseFood;
    ProductAdapter foodAdapter;
    RecyclerView rcvfood;
    ImageView backDSMA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        rcvfood = findViewById(R.id.rcvfood);
        backDSMA = findViewById(R.id.backDSMA);
        Intent getdata = getIntent();
        String matl = getdata.getStringExtra("matl");
        databaseFood = new DaoProducts(this);
        foodArrayList = new ArrayList<>();
        foodAdapter = new ProductAdapter(foodArrayList,ListProductActivity.this);
        GridLayoutManager idLayoutManager = new GridLayoutManager(ListProductActivity.this,2);
        rcvfood.setLayoutManager(idLayoutManager);
        rcvfood.setHasFixedSize(true);
        rcvfood.setAdapter(foodAdapter);

        databaseFood.getAll(new ProductsCallBack() {
            @Override
            public void onSuccess(ArrayList<Products> lists) {
                foodArrayList.clear();
                for (int i =0;i<lists.size();i++){
                    if (lists.get(i).getId().equalsIgnoreCase(matl)){
                        foodArrayList.add(lists.get(i));
                        foodAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(String message) {
            }
        });

        backDSMA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
    }
}