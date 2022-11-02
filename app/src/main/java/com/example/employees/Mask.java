package com.example.employees;

public class Mask {

    private int Id;
    private String Product;
    private int Quantity;
    private int Cost;
    private String Image;

    public Mask(int Id, String product, int quantity, int cost, String image) {
        this.Id = Id;
        Product = product;
        Quantity = quantity;
        Cost = cost;
        Image = image;
    }

    public Mask(String product, int quantity, int cost, String image) {
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