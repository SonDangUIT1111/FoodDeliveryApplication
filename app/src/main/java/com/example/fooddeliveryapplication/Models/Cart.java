package com.example.fooddeliveryapplication.Models;

public class Cart {
    String cartId;
    int totalAmount;
    int totalPrice;
    String userId;

    public Cart() {
    }

    public Cart(String cartId, int totalAmount, int totalPrice, String userId) {
        this.cartId = cartId;
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
}
