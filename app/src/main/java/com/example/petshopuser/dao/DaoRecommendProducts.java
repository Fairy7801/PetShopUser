package com.example.petshopuser.dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.petshopuser.callback.CategoriesCallBack;
import com.example.petshopuser.callback.ProductsCallBack;
import com.example.petshopuser.model.Categories;
import com.example.petshopuser.model.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoRecommendProducts {
    private DatabaseReference databaseReference;
    Context context;

    public DaoRecommendProducts(Context context) {
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SearchHistory");
    }

    public void saveSearchHistory(String userToken, String idCategory) {
        DatabaseReference userRef = databaseReference.child(userToken).child("categories").child(idCategory);
        // Tăng giá trị số lần tìm kiếm lên 1
        userRef.setValue(ServerValue.increment(1));
    }

    public DatabaseReference getSearchHistoryRef(String userToken) {
        // Trả về tham chiếu đến nút "categories" cho người dùng hiện tại
        return databaseReference.child(userToken).child("categories");
    }
}