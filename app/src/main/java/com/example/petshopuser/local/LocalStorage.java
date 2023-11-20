package com.example.petshopuser.local;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
    SharedPreferences sharedPreferences;

    public LocalStorage(Context context) {
        sharedPreferences = context.getSharedPreferences("Preferences", 0);
    }
    public String getCart() {
        if (sharedPreferences.contains("GioHang"))
            return sharedPreferences.getString("GioHang", null);
        else return null;
    }


    public void setCart(String cart) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("GioHang", cart);
        editor.commit();
    }
    public void deleteCart() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("GioHang");
        editor.commit();
    }
}

