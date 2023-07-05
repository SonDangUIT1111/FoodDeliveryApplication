package com.example.fooddeliveryapplication.Model;

public class Address {
    private String addressId;
    private String detailAddress;
    private String state;
    private String receiverName;
    private String receiverPhoneNumber;

    public Address() {
    }

    public Address(String addressId, String detailAddress, String state, String receiverName, String receiverPhoneNumber) {
        this.addressId = addressId;
        this.detailAddress = detailAddress;
        this.state = state;
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }
}
