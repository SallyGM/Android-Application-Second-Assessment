package com.example.assessment2;

public class Game {

    private String id;
    private String Name;

    public Game(String id, String name) {
        this.id = id;
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
