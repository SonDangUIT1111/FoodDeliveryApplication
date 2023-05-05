package com.example.fooddeliveryapplication.Model;

public class BillInfo {
    int amount;
    String billInfoId;
    String productId;

    public BillInfo(int amount, String billInfoId, String productId) {
        this.amount = amount;
        this.billInfoId = billInfoId;
        this.productId = productId;
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
}
