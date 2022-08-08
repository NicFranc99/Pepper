package com.example.pepperapp28aprile.models;

import com.example.pepperapp28aprile.Persona;

import java.io.Serializable;
import java.util.ArrayList;

public class Categoria implements Serializable {
    private  int i;
    private String categoryTitle;
    private int imageDrawable;


    public Persona.Game getGame() {
        return game;
    }

    private  Persona.Game game;

    public int getPosition() {
        return i;
    }

    public Categoria(Persona.Game game, int position) {
        this.game = game;
        this.i = position;
    }

    public Categoria(String categoryTitle,int imageDrawable){
        this.categoryTitle = categoryTitle;
        this.imageDrawable = imageDrawable;
    }

    public int getImageDrawable(){
        return imageDrawable;
    }

    public String getCategoryTitle(){
        return categoryTitle;
    }

    public String getNomeCategoia() {
        return game.getTitleCategory();
    }

    public int getImg() {
        return game.getNameIcon();
    }

    public String getNomeGioco() {
        return game.getTitleGame();
    }
}

