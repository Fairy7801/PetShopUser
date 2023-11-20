package com.example.petshopuser.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.petshopuser.R;
import com.example.petshopuser.View.User.TrangCaNhan;
import com.example.petshopuser.adapter.FavoriteAdapter;
import com.example.petshopuser.model.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    RecyclerView rcvcart;
    ArrayList<Products> foodArrayList;
    FirebaseUser firebaseUser;
    ImageView backyeuthich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        rcvcart= findViewById(R.id.rcvcart);
        backyeuthich = findViewById(R.id.backyeuthich);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        rcvcart.setLayoutManager(linearLayoutManager);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        foodArrayList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("favoriteList").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Products food = dataSnapshot.child("idP").getValue(Products.class);
                    foodArrayList.add(food);
                    FavoriteAdapter foodAdapter = new FavoriteAdapter(foodArrayList,getApplicationContext());
                    rcvcart.setAdapter(foodAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrangCaNhan.class);
                startActivity(intent);
            }
        });
    }
}