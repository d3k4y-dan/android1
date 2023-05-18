package com.d3k4y.project2.Models;

public class MessageModel {

    private String email;
    private String chat;
    private String date;

    public MessageModel(String email, String chat, String date) {
        this.email = email;
        this.chat = chat;
        this.date = date;
    }

    public MessageModel() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
