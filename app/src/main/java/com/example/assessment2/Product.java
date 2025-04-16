package com.example.assessment2;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Product implements Parcelable{

    private String id;
    private String name;
    private String price;
    private ArrayList<String> details;
    private String dimensions;
    private String type;
    private String quantity;
    private String ageRestriction;
    private String gameID;
    private String categoryID;
    private LocalDate dateAdded;
    private ArrayList<String> images;
    private int onCart;

    public Product(){}

    public Product(String id, String name, String price, ArrayList<String> details, String dimensions, String type, String quantity, String ageRestriction, String gameID, String categoryID) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.details = details;
        this.dimensions = dimensions;
        this.type = type;
        this.quantity = quantity;
        this.ageRestriction = ageRestriction;
        this.gameID = gameID;
        this.categoryID = categoryID;
    }

    public Product(String name, String price, ArrayList<String> details, String dimensions, String type, String quantity, String ageRestriction, String gameID, String categoryID) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.dimensions = dimensions;
        this.type = type;
        this.quantity = quantity;
        this.ageRestriction = ageRestriction;
        this.gameID = gameID;
        this.categoryID = categoryID;
    }

    // for product display
    public Product(String id, String name, String price, ArrayList<String> details, String dimensions, String type, String quantity, String ageRestriction, String gameID, String categoryID, ArrayList<String> images) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.details = details;
        this.dimensions = dimensions;
        this.type = type;
        this.quantity = quantity;
        this.ageRestriction = ageRestriction;
        this.gameID = gameID;
        this.categoryID = categoryID;
        this.images = images;
    }

    // for basket
    public Product(String id, String name, String price, ArrayList<String> details, String dimensions, String type, String quantity,
                   String ageRestriction, String gameID, String categoryID, ArrayList<String> images, int onCart) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.details = details;
        this.dimensions = dimensions;
        this.type = type;
        this.quantity = quantity;
        this.ageRestriction = ageRestriction;
        this.gameID = gameID;
        this.categoryID = categoryID;
        this.images = images;
        this.onCart = onCart;
    }

    public Product(String id, String name, String price, ArrayList<String> details, String dimensions, String type, String quantity,
                   String ageRestriction, String gameID, String categoryID, ArrayList<String> images, LocalDate dateAdded) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.details = details;
        this.dimensions = dimensions;
        this.type = type;
        this.quantity = quantity;
        this.ageRestriction = ageRestriction;
        this.gameID = gameID;
        this.categoryID = categoryID;
        this.images = images;
        this.dateAdded = dateAdded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<String> details) {
        this.details = details;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(String ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public int getOnCart() {
        return onCart;
    }

    public void setOnCart(int onCart) {
        this.onCart = onCart;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeStringList(details);
        parcel.writeString(dimensions);
        parcel.writeString(type);
        parcel.writeString(quantity);
        parcel.writeString(ageRestriction);
        parcel.writeString(gameID);
        parcel.writeString(categoryID);
        parcel.writeStringList(images);
    }

    public Product(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.price = in.readString();
        this.details = in.createStringArrayList();
        this.dimensions = in.readString();
        this.type = in.readString();
        this.quantity = in.readString();
        this.ageRestriction = in.readString();
        this.gameID = in.readString();
        this.categoryID = in.readString();
        this.images = in.createStringArrayList();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
