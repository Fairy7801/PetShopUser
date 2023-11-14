package com.example.petshopuser.callback;

import com.example.petshopuser.model.DetailInvoice;

import java.util.ArrayList;

public interface HDCTCallBack {
    void onSuccess(ArrayList<DetailInvoice> lists);
    void onError(String message);
}
