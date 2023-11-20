package com.example.petshopuser.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.petshopuser.Helper.ValidationHelper;
import com.example.petshopuser.MainActivity;
import com.example.petshopuser.adapter.FavoriteAdapter;
import com.example.petshopuser.callback.ProductsCallBack;
import com.example.petshopuser.dao.DaoProducts;
import com.example.petshopuser.databinding.ActivitySearchBinding;
import com.example.petshopuser.model.Products;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private ArrayList<Products> productsArrayList;
    private DaoProducts daoProducts;
    private FavoriteAdapter favoriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productsArrayList = new ArrayList<>();

        daoProducts = new DaoProducts(SearchActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rcvSearchSearchActivity.setHasFixedSize(true);
        binding.rcvSearchSearchActivity.setLayoutManager(layoutManager);

        binding.btnSearchSearchActivity.setOnClickListener(v -> {
            String keyword = binding.edtKeyworkSearchActivity.getText().toString().toLowerCase(Locale.getDefault());
            if (!ValidationHelper.isNotEmpty(keyword)) {
                productsArrayList.clear();
                updateAdapter();
            } else {
                searchInDatabase(keyword);
            }
        });

        binding.btnBackSearchActivity.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }

    private void searchInDatabase(String keyword) {
        daoProducts.getAll(new ProductsCallBack() {
            @Override
            public void onSuccess(ArrayList<Products> lists) {
                productsArrayList.clear();
                for (Products product : lists) {
                    if (product.getNameP().toLowerCase(Locale.getDefault()).contains(keyword)) {
                        productsArrayList.add(product);
                    }
                }
                updateAdapter();
            }

            @Override
            public void onError(String message) {
            }
        });
    }

    private void updateAdapter() {
        if (favoriteAdapter == null) {
            favoriteAdapter = new FavoriteAdapter(productsArrayList, SearchActivity.this);
            binding.rcvSearchSearchActivity.setAdapter(favoriteAdapter);
        } else {
            favoriteAdapter.notifyDataSetChanged();
        }
    }
}