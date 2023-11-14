package com.example.petshopuser.callback;

import com.example.petshopuser.model.Store;

import java.util.ArrayList;

public interface StoreCallBack {
    void onSuccess(ArrayList<Store> lists);
    void onError(String message);
}
