package com.example.fooddeliveryapplication.Model;

import java.io.Serializable;

public class Bill implements Serializable {
    private String addressId;
    private String billId;
    private String orderDate;
    private String orderStatus;

    private boolean checkAllComment;
    private String recipientId;
    private String senderId;
    private long totalPrice;
    private String imageUrl;

    public Bill(String addressId, String billId, String orderDate, String orderStatus, boolean checkAllComment, String recipientId, String senderId, long totalPrice, String imageUrl) {
        this.addressId = addressId;
        this.billId = billId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.checkAllComment = checkAllComment;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.totalPrice = totalPrice;
        this.imageUrl = imageUrl;
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

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isCheckAllComment() {
        return checkAllComment;
    }

    public void setCheckAllComment(boolean checkAllComment) {
        this.checkAllComment = checkAllComment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
