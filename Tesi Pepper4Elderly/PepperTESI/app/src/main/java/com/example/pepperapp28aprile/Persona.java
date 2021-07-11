package com.example.pepperapp28aprile;

import java.util.ArrayList;

public class Persona {

    private String image;
    private int id;
    private String name;
    private String surname; //surname
    private ArrayList<String> training;


    public Persona(String image, int id, String name, String status, ArrayList<String> training) {
        this.image = image;
        this.id = id;
        this.name = name;
        surname = status;
        this.training = training;
    }

    public Persona(String image, int id, String name, String status) {
        this.image = image;
        this.id = id;
        this.name = name;
        surname = status;
        training = new ArrayList<>();
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

    public String getSurname() {
        return surname;
    }

    public String toString() {
        return name+"_"+surname;
    }

    public String urlFullName() {
        String complete = name + "_" + surname.replace(" ", "_");
        complete.replace(" ", "_");
        return complete;
    }
}