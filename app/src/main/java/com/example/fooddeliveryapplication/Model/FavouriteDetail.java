package com.example.fooddeliveryapplication.Model;

public class FavouriteDetail {
    public String productId;
    public String productImage;
    public String productName;
    public float ratingStar;

    public FavouriteDetail() {
    }

    public FavouriteDetail(String productId, String productImage, String productName, float ratingStar) {
        this.productId = productId;
        this.productImage = productImage;
        this.productName = productName;
        this.ratingStar = ratingStar;
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

    public float getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(float ratingStar) {
        this.ratingStar = ratingStar;
    }
}
