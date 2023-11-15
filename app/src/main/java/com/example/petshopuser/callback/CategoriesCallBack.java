package com.example.petshopuser.callback;

import com.example.petshopuser.model.Categories;

import java.util.ArrayList;

public interface CategoriesCallBack {
    void onSuccess(ArrayList<Categories> lists);
    void onError(String message);
}
