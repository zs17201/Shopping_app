package com.example.shop_management.classes;

public class CartItem {

    private String productId;
    private String productName;

    private int imageResource;
    private double price;
    private int quantity;

    public CartItem(String productId, String productName, double price, int quantity,int imageResource ) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageResource = imageResource;
    }

    public CartItem() {
    }

    public String getProductId() {
        return productId;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getProductName() {
        return productName;
    }


    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}


