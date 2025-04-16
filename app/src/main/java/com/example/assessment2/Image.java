package com.example.assessment2;

import java.util.ArrayList;

public class Image {

    private String id;
    private ArrayList<String> url;

    public Image(String id, ArrayList<String> url){
        this.id = id;
        this.url = url;
    }

    public Image(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getUrl() {
        return url;
    }

    public void setUrl(ArrayList<String> url) {
        this.url = url;
    }
}
