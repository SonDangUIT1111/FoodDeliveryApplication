package com.example.fooddeliveryapplication.Model;

import java.io.Serializable;

public class Product implements Serializable {
    String productId;
    String productName;
    String productImage1;
    String productImage2;
    String productImage3;
    String productImage4;
    int productPrice;
    String productType;
    int remainAmount;
    int sold;
    String description;
    Double ratingStar;

    String publisherId;
    String avatarURL;

    public Product() {
    }

    public Product(String productId, String productName, String productImage1, String productImage2, String productImage3, String productImage4, int productPrice, String productType, int remainAmount, int sold, String description, Double ratingStar, String publisherId, String avatarURL) {
        this.productId = productId;
        this.productName = productName;
        this.productImage1 = productImage1;
        this.productImage2 = productImage2;
        this.productImage3 = productImage3;
        this.productImage4 = productImage4;
        this.productPrice = productPrice;
        this.productType = productType;
        this.remainAmount = remainAmount;
        this.sold = sold;
        this.description = description;
        this.ratingStar = ratingStar;
        this.publisherId = publisherId;
        this.avatarURL = avatarURL;
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

    public String getProductImage1() {
        return productImage1;
    }

    public void setProductImage1(String productImage1) {
        this.productImage1 = productImage1;
    }

    public String getProductImage2() {
        return productImage2;
    }

    public void setProductImage2(String productImage2) {
        this.productImage2 = productImage2;
    }

    public String getProductImage3() {
        return productImage3;
    }

    public void setProductImage3(String productImage3) {
        this.productImage3 = productImage3;
    }

    public String getProductImage4() {
        return productImage4;
    }

    public void setProductImage4(String productImage4) {
        this.productImage4 = productImage4;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(int remainAmount) {
        this.remainAmount = remainAmount;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(Double ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    @Override
    public String toString() {
        return "Products{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productImage1='" + productImage1 + '\'' +
                ", productImage2='" + productImage2 + '\'' +
                ", productImage3='" + productImage3 + '\'' +
                ", productImage4='" + productImage4 + '\'' +
                ", productPrice=" + productPrice +
                ", productType='" + productType + '\'' +
                ", remainAmount=" + remainAmount +
                ", description='" + description + '\'' +
                ", ratingStar=" + ratingStar +
                ", publisherId='" + publisherId + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                '}';
    }
}
