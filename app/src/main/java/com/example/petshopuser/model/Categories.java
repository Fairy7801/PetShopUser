package com.example.petshopuser.model;

public class Categories {
    private String idCategories;
    private String name;
    private String description;
    private String image;
    public Categories(){

    }

    public Categories(String idCategories, String name, String description, String image) {
        this.idCategories = idCategories;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getIdCategories() {
        return idCategories;
    }

    public void setIdCategories(String idCategories) {
        this.idCategories = idCategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
