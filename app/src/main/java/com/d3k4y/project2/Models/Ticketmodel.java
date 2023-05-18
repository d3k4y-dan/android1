package com.d3k4y.project2.Models;

public class Ticketmodel {

    private String user_email;
    private String ticket_title;
    private String ticket_description;
    private String date;

    public Ticketmodel(String user_email, String ticket_title, String ticket_description, String date) {
        this.user_email = user_email;
        this.ticket_title = ticket_title;
        this.ticket_description = ticket_description;
        this.date = date;
    }

    public Ticketmodel() {

    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getTicket_title() {
        return ticket_title;
    }

    public void setTicket_title(String ticket_title) {
        this.ticket_title = ticket_title;
    }

    public String getTicket_description() {
        return ticket_description;
    }

    public void setTicket_description(String ticket_description) {
        this.ticket_description = ticket_description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
