package com.d3k4y.project2.Models;

public class User {

    private String name;
    private String nickname;
    private String email;
    private String password;
    private String mobile;
    private String address;
    private String birthday;
    private String gender;
    private String image;
    private String uid;
    private int status;

    public User(String name, String nickname, String email, String password, String mobile, String address, String birthday, String gender, String image, String uid, int status) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.address = address;
        this.birthday = birthday;
        this.gender = gender;
        this.image = image;
        this.uid = uid;
        this.status = status;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
