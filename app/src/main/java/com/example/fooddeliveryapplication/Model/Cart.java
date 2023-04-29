package com.example.fooddeliveryapplication.Model;

import java.util.List;

public class Cart {
    public String cartId;
    public List<CartInfo> cartInfos;
    public int totalAmount;
    public String totalPrice;
    public String userId;

    public Cart() {
    }

    public Cart(String cartId, List<CartInfo> cartInfos, int totalAmount, String totalPrice, String userId) {
        this.cartId = cartId;
        this.cartInfos = cartInfos;
        this.totalAmount = totalAmount;
        this.totalPrice = totalPrice;
        this.userId = userId;
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
