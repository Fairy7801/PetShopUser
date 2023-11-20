package com.example.petshopuser.callback;

import com.example.petshopuser.model.HDCT;

import java.util.ArrayList;

public interface HDCTCallBack {
    void onSuccess(ArrayList<HDCT> lists);
    void onError(String message);
}
