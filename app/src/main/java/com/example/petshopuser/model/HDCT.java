package com.example.petshopuser.model;

import java.util.ArrayList;

public class HDCT {
    private String ngay;
    private String thoigian;
    private String idHD;
    private String idHDCT;
    boolean check;
    String uiduser;
    ArrayList<Order> orderArrayList;

    public HDCT() {

    }

    public HDCT(String ngay, String thoigian, String idHD, String idHDCT, boolean check, String uiduser, ArrayList<Order> orderArrayList) {
        this.ngay = ngay;
        this.thoigian = thoigian;
        this.idHD = idHD;
        this.idHDCT = idHDCT;
        this.check = check;
        this.uiduser = uiduser;
        this.orderArrayList = orderArrayList;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

    public String getIdHD() {
        return idHD;
    }

    public void setIdHD(String idHD) {
        this.idHD = idHD;
    }

    public String getIdHDCT() {
        return idHDCT;
    }

    public void setIdHDCT(String idHDCT) {
        this.idHDCT = idHDCT;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getUiduser() {
        return uiduser;
    }

    public void setUiduser(String uiduser) {
        this.uiduser = uiduser;
    }

    public ArrayList<Order> getOrderArrayList() {
        return orderArrayList;
    }

    public void setOrderArrayList(ArrayList<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
    }
}