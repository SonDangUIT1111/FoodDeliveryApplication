package com.example.fooddeliveryapplication.Model;

public class CartInfo {
    int amount;
    String cartInfoId;
    String productId;

    public CartInfo(int amount, String cartInfoId, String productId) {
        this.amount = amount;
        this.cartInfoId = cartInfoId;
        this.productId = productId;
    }

    public CartInfo() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCartInfoId() {
        return cartInfoId;
    }

    public void setCartInfoId(String cartInfoId) {
        this.cartInfoId = cartInfoId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
