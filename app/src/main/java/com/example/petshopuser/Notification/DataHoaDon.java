package com.example.petshopuser.Notification;

public class DataHoaDon {
    private String user;
    private int icon;
    private String body;
    private String title;
    private String sented;
    String detailInvoice;
    public DataHoaDon(){

    }

    public DataHoaDon(String user, int icon, String body, String title, String sented, String detailInvoice) {
        this.user = user;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sented = sented;
        this.detailInvoice = detailInvoice;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    public String getDetailInvoice() {
        return detailInvoice;
    }

    public void setDetailInvoice(String detailInvoice) {
        this.detailInvoice = detailInvoice;
    }
}
