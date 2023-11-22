package com.example.petshopuser.dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.petshopuser.callback.UserCallBack;
import com.example.petshopuser.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DaoUser {
    Context context;
    DatabaseReference mRef;
    String key;

    public DaoUser(Context context) {
        this.context = context;
        this.mRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    public void getAll(final UserCallBack callback) {
        final ArrayList<User> dataloai = new ArrayList<>();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dataloai.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        User user = data.getValue(User.class);
                        dataloai.add(user);
                    }
                    callback.onSuccess(dataloai);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.toString());
            }
        });
    }

    public void insert(User item) {
        key = mRef.push().getKey();
        mRef.child(key).setValue(item).addOnSuccessListener(aVoid -> {
        }).addOnFailureListener(e -> {
        });
    }

    public boolean update(final User item) {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("email").getValue(String.class) != null && dataSnapshot.child("email").getValue(String.class).equalsIgnoreCase(item.getEmail())) {
                        key = dataSnapshot.getKey();
                        mRef.child(key).setValue(item);
                        Toast.makeText(context, "Update Thành Công", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return true;
    }

    public void delete(final String matheloai) {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("email").getValue(String.class).equalsIgnoreCase(matheloai)) {
                        key = dataSnapshot.getKey();
                        mRef.child(key).removeValue()
                                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Delete Thành Công", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> {});
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void updateField(String userToken, String fieldName, String updatedValue) {
        Query query = mRef.orderByChild("token").equalTo(userToken);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    key = dataSnapshot.getKey();
                    mRef.child(key).child(fieldName).setValue(updatedValue);
                    Toast.makeText(context, "Update Thành Công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
