package com.example.pepperapp28aprile;

public class Persona {

    private String image;
    private String name;
    private String status;

    public Persona(String image, String name, String status) {
        this.image = image;
        this.name = name;
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}