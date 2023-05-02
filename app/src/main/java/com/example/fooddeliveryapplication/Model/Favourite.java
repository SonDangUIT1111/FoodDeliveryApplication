package com.example.fooddeliveryapplication.Model;

import java.util.List;

public class Favourite {
    public String userId;
    public String userName;
    public List<FavouriteDetail> favouriteList;

    public Favourite(String userId, String userName, List<FavouriteDetail> favouriteList) {
        this.userId = userId;
        this.userName = userName;
        this.favouriteList = favouriteList;
    }

    public Favourite() {
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

    public List<FavouriteDetail> getFavouriteList() {
        return favouriteList;
    }

    public void setFavouriteList(List<FavouriteDetail> favouriteList) {
        this.favouriteList = favouriteList;
    }
}
