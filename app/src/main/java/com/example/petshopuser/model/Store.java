package com.example.petshopuser.model;

public class Store {
    private String email;
    private String pass;
    private String name;
    private String phone;
    private String image;
    private String address;
    private String status;
    private String tokenStore;
    public Store(){

    }

    public Store(String email, String pass, String name, String phone, String image, String address, String status, String tokenStore) {
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.address = address;
        this.status = status;
        this.tokenStore = tokenStore;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTokenStore() {
        return tokenStore;
    }

    public void setTokenStore(String tokenStore) {
        this.tokenStore = tokenStore;
    }
}
