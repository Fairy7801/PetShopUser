package com.example.petshopuser.model;

public class Products {
    private String idProducts;
    private String name;
    private double price;
    private int quatity;
    private String image;
    private String address;
    private String description;
    private String status;
    private String idCategories;
    private String idStore;
    private String tokenStore;
    public Products(){

    }

    public Products(String idProducts, String name, double price, int quatity, String image, String address, String description, String status, String idCategories, String idStore, String tokenStore) {
        this.idProducts = idProducts;
        this.name = name;
        this.price = price;
        this.quatity = quatity;
        this.image = image;
        this.address = address;
        this.description = description;
        this.status = status;
        this.idCategories = idCategories;
        this.idStore = idStore;
        this.tokenStore = tokenStore;
    }

    public String getIdProducts() {
        return idProducts;
    }

    public void setIdProducts(String idProducts) {
        this.idProducts = idProducts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuatity() {
        return quatity;
    }

    public void setQuatity(int quatity) {
        this.quatity = quatity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdCategories() {
        return idCategories;
    }

    public void setIdCategories(String idCategories) {
        this.idCategories = idCategories;
    }

    public String getIdStore() {
        return idStore;
    }

    public void setIdStore(String idStore) {
        this.idStore = idStore;
    }

    public String getTokenStore() {
        return tokenStore;
    }

    public void setTokenStore(String tokenStore) {
        this.tokenStore = tokenStore;
    }
}
