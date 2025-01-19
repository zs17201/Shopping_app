package com.example.shop_management.models;

public class CurrentUser {

    private static CurrentUser  curr_user;
    private String email;

    private String name;

    private CurrentUser(String email, String name){
        this.email = email;
        this.name = name;
    }

    public static CurrentUser getInstance(String email, String name) {
        if (curr_user == null) {
            curr_user = new CurrentUser(email, name);
        }
        return curr_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void clearInstance() {
        curr_user = null;
    }

}
