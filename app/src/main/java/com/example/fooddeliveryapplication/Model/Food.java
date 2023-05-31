package com.example.fooddeliveryapplication.Model;

public class Food {

    int imgSrc;
    String name;
    Double Price;

    public Food() {
    }

    public Food(int imgSrc, String name, Double price) {
        this.imgSrc = imgSrc;
        this.name = name;
        Price = price;
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
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }
}
