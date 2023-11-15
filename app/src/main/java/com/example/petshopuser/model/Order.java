package com.example.petshopuser.model;

public class Order {
    private String idOrder;
    Products products;
    private int quatity;
    Store store;
    User user;
    public Order(){

    }

    public Order(String idOrder, Products products, int quatity, Store store, User user) {
        this.idOrder = idOrder;
        this.products = products;
        this.quatity = quatity;
        this.store = store;
        this.user = user;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public int getQuatity() {
        return quatity;
    }

    public void setQuatity(int quatity) {
        this.quatity = quatity;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
