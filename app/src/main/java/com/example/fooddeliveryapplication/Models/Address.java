package com.example.fooddeliveryapplication.Models;

public class Address {
    String addressId;
    String detailAddress;

    public Address() {
    }

    public Address(String addressId, String detailAddress) {
        this.addressId = addressId;
        this.detailAddress = detailAddress;
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
}
