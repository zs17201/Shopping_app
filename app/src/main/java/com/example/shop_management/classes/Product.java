package com.example.shop_management.classes;

public class Product {
    private String name;
    private String price;
    private int imageResource;

    private String id;
    private int quantity;

    public Product(String name, String price, int imageResource,String id) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.id = id;
        this.quantity = 0;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
