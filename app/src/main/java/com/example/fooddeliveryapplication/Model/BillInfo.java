package com.example.fooddeliveryapplication.Model;

import java.io.Serializable;

public class BillInfo implements Serializable {
    private int amount;
    private String billInfoId;
    private String productId;
    private boolean check;

    public BillInfo() {
    }

    public BillInfo(int amount, String billInfoId, String productId, boolean check) {
        this.amount = amount;
        this.billInfoId = billInfoId;
        this.productId = productId;
        this.check = check;
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

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
