package com.example.fooddeliveryapplication.Model;

public class User {
    private String accountName;
    private String userId;
    private String email;
    private String avatarURL;
    private String userName;
    private String birthDate;
    private String phoneNumber;
    private String registerDate;
    private String saleRegisterDate;

    public User() {
    }

    public User(String accountName, String userId, String email, String avatarURL, String userName, String birthDate, String phoneNumber, String registerDate, String saleRegisterDate) {
        this.accountName = accountName;
        this.userId = userId;
        this.email = email;
        this.avatarURL = avatarURL;
        this.userName = userName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.registerDate = registerDate;
        this.saleRegisterDate = saleRegisterDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
