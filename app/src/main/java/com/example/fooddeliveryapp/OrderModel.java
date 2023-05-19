package com.example.fooddeliveryapp;

public class OrderModel {

    private String productImage, productName, productPrice, deliveryStatus, username, address, phone;

    public OrderModel(String productImage, String productName, String productPrice, String deliveryStatus, String username, String address, String phone) {
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.deliveryStatus = deliveryStatus;
        this.username = username;
        this.address = address;
        this.phone = phone;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
