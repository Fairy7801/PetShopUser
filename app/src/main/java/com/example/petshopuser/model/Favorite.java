package com.example.petshopuser.model;

public class Favorite {
    private String idFavorite;
    private String uidUser;
    Products idProducts;
    boolean checkFavorite;
    public Favorite(){

    }

    public Favorite(String idFavorite, String uidUser, Products idProducts, boolean checkFavorite) {
        this.idFavorite = idFavorite;
        this.uidUser = uidUser;
        this.idProducts = idProducts;
        this.checkFavorite = checkFavorite;
    }

    public String getIdFavorite() {
        return idFavorite;
    }

    public void setIdFavorite(String idFavorite) {
        this.idFavorite = idFavorite;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public Products getIdProducts() {
        return idProducts;
    }

    public void setIdProducts(Products idProducts) {
        this.idProducts = idProducts;
    }

    public boolean isCheckFavorite() {
        return checkFavorite;
    }

    public void setCheckFavorite(boolean checkFavorite) {
        this.checkFavorite = checkFavorite;
    }
}
