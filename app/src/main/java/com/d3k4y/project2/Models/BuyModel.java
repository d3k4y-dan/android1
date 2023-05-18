package com.d3k4y.project2.Models;

public class BuyModel {

    private String buyer_mail;
    private String seller_mail;
    private String product_id;
    private String billing_name;
    private String buy_qty;
    private String total_price;
    private String billing_address;
    private String date;
    private String cardnumber;
    private String cardexpmonth;
    private String cardexpyear;
    private String cardccv;

    public BuyModel(String buyer_mail, String product_id, String billing_name, String buy_qty, String total_price, String billing_address, String date, String cardnumber, String cardexpmonth, String cardexpyear, String cardccv) {
        this.buyer_mail = buyer_mail;
        this.product_id = product_id;
        this.billing_name = billing_name;
        this.buy_qty = buy_qty;
        this.total_price = total_price;
        this.billing_address = billing_address;
        this.date = date;
        this.cardnumber = cardnumber;
        this.cardexpmonth = cardexpmonth;
        this.cardexpyear = cardexpyear;
        this.cardccv = cardccv;
    }

    public BuyModel(String buyer_mail, String seller_mail, String product_id, String billing_name, String buy_qty, String total_price, String billing_address, String date) {
        this.buyer_mail = buyer_mail;
        this.setSeller_mail(seller_mail);
        this.product_id = product_id;
        this.billing_name = billing_name;
        this.buy_qty = buy_qty;
        this.total_price = total_price;
        this.billing_address = billing_address;
        this.date = date;
    }

    public BuyModel() {

    }

    public String getBuyer_mail() {
        return buyer_mail;
    }

    public void setBuyer_mail(String buyer_mail) {
        this.buyer_mail = buyer_mail;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getBilling_name() {
        return billing_name;
    }

    public void setBilling_name(String billing_name) {
        this.billing_name = billing_name;
    }

    public String getBuy_qty() {
        return buy_qty;
    }

    public void setBuy_qty(String buy_qty) {
        this.buy_qty = buy_qty;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(String billing_address) {
        this.billing_address = billing_address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getCardexpmonth() {
        return cardexpmonth;
    }

    public void setCardexpmonth(String cardexpmonth) {
        this.cardexpmonth = cardexpmonth;
    }

    public String getCardexpyear() {
        return cardexpyear;
    }

    public void setCardexpyear(String cardexpyear) {
        this.cardexpyear = cardexpyear;
    }

    public String getCardccv() {
        return cardccv;
    }

    public void setCardccv(String cardccv) {
        this.cardccv = cardccv;
    }

    public String getSeller_mail() {
        return seller_mail;
    }

    public void setSeller_mail(String seller_mail) {
        this.seller_mail = seller_mail;
    }
}
