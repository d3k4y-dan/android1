package com.d3k4y.project2.Models;

public class PostModel {

    private String post_title;
    private String post_description;
    private String image;
    private String email;
    private String date;

    public PostModel(String post_title, String post_description, String image, String email, String date) {
        this.post_title = post_title;
        this.post_description = post_description;
        this.image = image;
        this.email = email;
        this.date = date;
    }

    public PostModel() {

    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
