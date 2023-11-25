package com.example.petshopuser.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.petshopuser.model.Products;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Products>> productsLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> recommendedProductsLiveData = new MutableLiveData<>();

    // Các phương thức getter và setter cho LiveData
    public LiveData<ArrayList<Products>> getProductsLiveData() {
        return productsLiveData;
    }

    public LiveData<ArrayList<String>> getRecommendedProductsLiveData() {
        return recommendedProductsLiveData;
    }

    // Các phương thức để cập nhật dữ liệu
    public void setProducts(ArrayList<Products> products) {
        productsLiveData.setValue(products);
    }

    public void setRecommendedProducts(ArrayList<String> recommendedProducts) {
        recommendedProductsLiveData.setValue(recommendedProducts);
    }
}

