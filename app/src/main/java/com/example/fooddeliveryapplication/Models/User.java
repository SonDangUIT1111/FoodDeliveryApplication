package com.example.fooddeliveryapplication.Models;

public class User {
    String accountName;
    String avatarURL;
    String birthDate;
    String email;
    String phoneNumber;
    String registerDate;
    String saleRegisterDate;
    String userId;
    String userName;

    public User() {
    }

    public User(String accountName, String avatarURL, String birthDate, String email, String phoneNumber, String registerDate, String saleRegisterDate, String userId, String userName) {
        this.accountName = accountName;
        this.avatarURL = avatarURL;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registerDate = registerDate;
        this.saleRegisterDate = saleRegisterDate;
        this.userId = userId;
        this.userName = userName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getSaleRegisterDate() {
        return saleRegisterDate;
    }

    public void setSaleRegisterDate(String saleRegisterDate) {
        this.saleRegisterDate = saleRegisterDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
