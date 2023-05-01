package com.example.fooddeliveryapplication.Model;

public class CartInfo {
    public int amount;
    public String cartInfoId;
    public String productId;
    public String productName;
    public String productImage;
    public int productPrice;

    public CartInfo() {
    }

    public CartInfo(int amount, String cartInfoId, String productId, String productName, String productImage, int productPrice) {
        this.amount = amount;
        this.cartInfoId = cartInfoId;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
}
