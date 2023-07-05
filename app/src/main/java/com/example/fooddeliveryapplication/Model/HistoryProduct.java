package com.example.fooddeliveryapplication.Model;

public class HistoryProduct {
    private int resourceId;
    private String NameHistoryProduct;
    private String State;
    private String PriceHistoryProduct;

    public HistoryProduct(int resourceId, String nameHistoryProduct, String state, String priceHistoryProduct) {
        this.resourceId = resourceId;
        NameHistoryProduct = nameHistoryProduct;
        State = state;
        PriceHistoryProduct = priceHistoryProduct;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getNameHistoryProduct() {
        return NameHistoryProduct;
    }

    public void setNameHistoryProduct(String nameHistoryProduct) {
        NameHistoryProduct = nameHistoryProduct;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPriceHistoryProduct() {
        return PriceHistoryProduct;
    }

    public void setPriceHistoryProduct(String priceHistoryProduct) {
        PriceHistoryProduct = priceHistoryProduct;
    }


}
