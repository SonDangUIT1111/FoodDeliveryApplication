package com.example.fooddeliveryapplication.Model;

public class Food {
    private int imgSrc;
    private String name;
    private Double price;

    public Food() {
    }

    public Food(int imgSrc, String name, Double price) {
        this.imgSrc = imgSrc;
        this.name = name;
        price = price;
    }

    public int getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(int imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
