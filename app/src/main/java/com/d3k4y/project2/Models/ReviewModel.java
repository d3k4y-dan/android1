package com.d3k4y.project2.Models;

public class ReviewModel {

    private String email;
    private String review;
    private String product_id;

    public ReviewModel(String email, String review, String product_id) {
        this.email = email;
        this.review = review;
        this.product_id = product_id;
    }

    public ReviewModel() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
