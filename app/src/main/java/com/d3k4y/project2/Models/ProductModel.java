package com.d3k4y.project2.Models;

public class ProductModel {

    private String name;
    private String brand;
    private String description;
    private String gender;
    private String category;
    private String price;
    private String qty;
    private String image;
    private String email;

    public ProductModel(String name, String brand, String description, String gender, String category, String price, String qty, String image, String email) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.gender = gender;
        this.category = category;
        this.price = price;
        this.qty = qty;
        this.image = image;
        this.setEmail(email);
    }

    public ProductModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
