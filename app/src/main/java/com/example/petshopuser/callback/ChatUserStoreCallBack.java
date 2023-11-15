package com.example.petshopuser.callback;

import com.example.petshopuser.model.ChatUserStore;

import java.util.ArrayList;

public interface ChatUserStoreCallBack {
    void onSuccess(ArrayList<ChatUserStore> lists);
    void onError(String message);
}
