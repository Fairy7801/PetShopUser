package com.example.petshopuser.model;

public class User {
    private String email;
    private String pass;
    private String name;
    private String phone;
    private String image;
    private String address;
    private String birthday;
    private String sex;
    String token;
    public User(){

    }

    public User(String email, String pass, String name, String phone, String image, String address, String birthday, String sex, String token) {
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.address = address;
        this.birthday = birthday;
        this.sex = sex;
        this.token = token;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
