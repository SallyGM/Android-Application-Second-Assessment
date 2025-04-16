package com.example.assessment2;

import java.util.ArrayList;
import java.util.HashMap;

public class Order {

    private String id;
    private Address address;
    private String date;
    private HashMap<String, Integer> items;
    private String lastUpdate;
    private String notes;
    private String price;
    private String status;
    private String userID;

    private HashMap<String, String> ProductNames;

    public Order(Address address, String date,  HashMap<String, Integer> items, String lastUpdate, String notes, String price, String status, String userID) {
        this.address = address;
        this.date = date;
        this.items = items;
        this.lastUpdate = lastUpdate;
        this.notes = notes;
        this.price = price;
        this.status = status;
        this.userID = userID;
    }

    public Order(String id, Address address, String date,  HashMap<String, Integer> items, String lastUpdate, String notes, String price, String status, String userID) {
        this.id = id;
        this.address = address;
        this.date = date;
        this.items = items;
        this.lastUpdate = lastUpdate;
        this.notes = notes;
        this.price = price;
        this.status = status;
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, Integer> getItems() {
        return items;
    }

    public void setItems(HashMap<String, Integer> items) {
        this.items = items;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public HashMap<String, String> getProductNames() {
        return ProductNames;
    }

    public void setProductNames(HashMap<String, String> productNames) {
        ProductNames = productNames;
    }
}
