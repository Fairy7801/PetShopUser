package com.example.petshopuser.callback;

import com.example.petshopuser.model.Products;

import java.util.ArrayList;

public interface ProductsCallBack {
    void onSuccess(ArrayList<Products> lists);
    void onError(String message);
}
