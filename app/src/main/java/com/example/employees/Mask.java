package com.example.employees;

import android.os.Parcel;
import android.os.Parcelable;

public class Mask implements Parcelable {

    private int Id;
    private String Product;
    private int Quantity;
    private double Cost;
    private String Image;

    protected Mask(Parcel in) {
        Id = in.readInt();
        Product = in.readString();
        Quantity = in.readInt();
        Cost = in.readDouble();
        Image = in.readString();
    }

    public static final Creator<Mask> CREATOR = new Creator<Mask>() {
        @Override
        public Mask createFromParcel(Parcel in) {
            return new Mask(in);
        }

        @Override
        public Mask[] newArray(int size) {
            return new Mask[size];
        }
    };

    public int getId() {
        return Id;
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

    public void setCost(double cost) {
        Cost = cost;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getProduct() {
        return Product;
    }

    public int getQuantity() {
        return Quantity;
    }

    public double getCost() {
        return Cost;
    }

    public String getImage() {
        return Image;
    }

    public Mask(int Id, String product, int quantity, double cost, String image) {
        this.Id = Id;
        Product = product;
        Quantity = quantity;
        Cost = cost;
        Image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Product);
        dest.writeInt(Quantity);
        dest.writeDouble(Cost);
        dest.writeString(Image);
    }
}