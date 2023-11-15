package com.example.petshopuser.model;

import java.util.ArrayList;

public class DetailInvoice {
    private String idInvoice;
    private String idDetailInvoice;
    String day;
    String time;
    String uidUser;
    boolean check;
    ArrayList<Order> orderArrayList;
    public DetailInvoice(){

    }

    public DetailInvoice(String idInvoice, String idDetailInvoice, String day, String time, String uidUser, boolean check, ArrayList<Order> orderArrayList) {
        this.idInvoice = idInvoice;
        this.idDetailInvoice = idDetailInvoice;
        this.day = day;
        this.time = time;
        this.uidUser = uidUser;
        this.check = check;
        this.orderArrayList = orderArrayList;
    }

    public String getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(String idInvoice) {
        this.idInvoice = idInvoice;
    }

    public String getIdDetailInvoice() {
        return idDetailInvoice;
    }

    public void setIdDetailInvoice(String idDetailInvoice) {
        this.idDetailInvoice = idDetailInvoice;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public ArrayList<Order> getOrderArrayList() {
        return orderArrayList;
    }

    public void setOrderArrayList(ArrayList<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
    }
}
