package com.example.fooddeliveryapplication.Model;

public class BillInfo {
    public int amount;
    public String billInfoId;
    public String productId;
    public String productImage;
    public String productName;
    public int productPrice;

    public BillInfo(int amount, String billInfoId, String productId, String productImage, String productName, int productPrice) {
        this.amount = amount;
        this.billInfoId = billInfoId;
        this.productId = productId;
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public BillInfo() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBillInfoId() {
        return billInfoId;
    }

    public void setBillInfoId(String billInfoId) {
        this.billInfoId = billInfoId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
}
