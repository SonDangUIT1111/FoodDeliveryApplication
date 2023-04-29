package com.example.fooddeliveryapplication.Model;

public class CartInfo {
    public int amount;
    public String cartInfoId;
    public String price;
    public String productId;

    public CartInfo() {
    }

    public CartInfo(int amount, String cartInfoId, String price, String productId) {
        this.amount = amount;
        this.cartInfoId = cartInfoId;
        this.price = price;
        this.productId = productId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
