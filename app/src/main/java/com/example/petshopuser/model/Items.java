package com.example.petshopuser.model;

public class Items {
    private String description;
    private String imageURL;
    public Items(){

    }

    public Items(String description, String imageURL) {
        this.description = description;
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
