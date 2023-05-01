package com.example.fooddeliveryapplication.Model;

import java.util.List;

public class Cart {
    public String cartId;
    public List<CartInfo> cartInfos;
    public int totalAmount;
    public int totalPrice;
    public String userId;
    public String userName;

    public Cart() {
    }

    public Cart(String cartId, List<CartInfo> cartInfos, int totalAmount, int totalPrice, String userId, String userName) {
        this.cartId = cartId;
        this.cartInfos = cartInfos;
        this.totalAmount = totalAmount;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.userName = userName;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public List<CartInfo> getCartInfos() {
        return cartInfos;
    }

    public void setCartInfos(List<CartInfo> cartInfos) {
        this.cartInfos = cartInfos;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
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
