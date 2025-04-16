package com.example.assessment2;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

public class Basket implements Parcelable {

    private String id;
    private HashMap<String, Integer> products;

    public Basket(String id, HashMap<String, Integer> products) {
        this.id = id;
        this.products = products;
    }

    public Basket() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<String, Integer> products) {
        this.products = products;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeSerializable(products);
    }

    public Basket(Parcel in) {
        this.id = in.readString();
        this.products = (HashMap<String, Integer>) in.readSerializable();
    }

    public static final Creator<Basket> CREATOR = new Creator<Basket>() {
        @Override
        public Basket createFromParcel(Parcel in) {
            return new Basket(in);
        }

        @Override
        public Basket[] newArray(int size) {
            return new Basket[size];
        }
    };

    public boolean contains(String productID) {
        if(products == null){
            return false;
        }
        else{
            return products.containsKey(productID);
        }
    }
}
