package com.example.employees;

import android.os.Parcel;

public class DataModal {

    private int Id;
    private String Product;
    private int Quantity;
    private int Cost;
    private String Image;

    public DataModal(String product, int quantity, int cost, String image) {
        //Id = in.readInt();
        Product = product;
        Quantity = quantity;
        Cost = cost;
        Image = image;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public void setCost(int cost) {
        Cost = cost;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getId() {
        return Id;
    }

    public String getProduct() {
        return Product;
    }

    public int getQuantity() {
        return Quantity;
    }

    public int getCost() {
        return Cost;
    }

    public String getImage() {
        return Image;
    }
}