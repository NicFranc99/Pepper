package com.example.parenteapp.ui.main;

public class Persona {

    private String image;
    private int id;
    private String name;
    private String status;

    public Persona(String image, int id, String name, String status) {
        this.image = image;
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}