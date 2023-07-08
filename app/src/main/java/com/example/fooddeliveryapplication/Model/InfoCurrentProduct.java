package com.example.fooddeliveryapplication.Model;

public class InfoCurrentProduct {
    private int resourceId;
    private String NameCurrentProduct;
    private String State;
    private String PriceCurrentProduct;

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getNameCurrentProduct() {
        return NameCurrentProduct;
    }

    public void setNameCurrentProduct(String nameCurrentProduct) {
        NameCurrentProduct = nameCurrentProduct;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPriceCurrentProduct() {
        return PriceCurrentProduct;
    }

    public void setPriceCurrentProduct(String priceCurrentProduct) {
        PriceCurrentProduct = priceCurrentProduct;
    }

    public InfoCurrentProduct(int resourceId, String nameCurrentProduct, String state, String priceCurrentProduct) {
        this.resourceId = resourceId;
        NameCurrentProduct = nameCurrentProduct;
        State = state;
        PriceCurrentProduct = priceCurrentProduct;
    }
}
