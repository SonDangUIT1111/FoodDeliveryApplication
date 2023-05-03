package com.example.fooddeliveryapplication.Model;

import java.util.List;

public class Bill {
    public String addressId;
    public String billId;
    public List<BillInfo> billInfos;
    public String detailAddress;
    public String orderDate;
    public String orderStatus;
    public String recipientId;
    public String recipientName;
    public String senderId;
    public String senderName;
    public int totalPrice;

    public Bill(String addressId, String billId, List<BillInfo> billInfos, String detailAddress, String orderDate, String orderStatus, String recipientId, String recipientName, String senderId, String senderName, int totalPrice) {
        this.addressId = addressId;
        this.billId = billId;
        this.billInfos = billInfos;
        this.detailAddress = detailAddress;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.senderId = senderId;
        this.senderName = senderName;
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

    public List<BillInfo> getBillInfos() {
        return billInfos;
    }

    public void setBillInfos(List<BillInfo> billInfos) {
        this.billInfos = billInfos;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
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

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
