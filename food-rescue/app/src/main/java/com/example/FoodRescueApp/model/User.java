package com.example.FoodRescueApp.model;

public class User
{
    // Variables
    private int user_id;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String password;

    // Constructor
    public User(String username, String email, String phone, String address, String password) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    public User(int user_id, String username, String email, String phone, String address, String password) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    public User() {
    }

    // Getter and Setter

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
