package com.example.fooddeliveryapplication.Class;

public class Product {
    private int resourceId;
    private String Name;
    private String Price;

    public Product(int resourceId, String name, String price) {
        this.resourceId = resourceId;
        Name = name;
        Price = price;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
