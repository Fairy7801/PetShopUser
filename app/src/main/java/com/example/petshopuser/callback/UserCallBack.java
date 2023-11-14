package com.example.petshopuser.callback;

import com.example.petshopuser.model.User;

import java.util.ArrayList;

public interface UserCallBack {
    void onSuccess(ArrayList<User> lists);
    void onError(String message);
}
