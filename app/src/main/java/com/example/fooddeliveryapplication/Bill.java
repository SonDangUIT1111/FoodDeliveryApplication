package com.example.fooddeliveryapplication;

import java.io.Serializable;

public class Bill implements Serializable {
    String addressId;
    String billId;
    String orderDate;
    String orderStatus;

    boolean checkAllComment;
    String recipientId;
    String senderId;
    Double totalPrice;

    public Bill(String addressId, String billId, String orderDate, String orderStatus, boolean checkAllComment, String recipientId, String senderId, Double totalPrice) {
        this.addressId = addressId;
        this.billId = billId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.checkAllComment = checkAllComment;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.totalPrice = totalPrice;
    }

    public Bill() {
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isCheckAllComment() {
        return checkAllComment;
    }

    public void setCheckAllComment(boolean checkAllComment) {
        this.checkAllComment = checkAllComment;
    }
}
