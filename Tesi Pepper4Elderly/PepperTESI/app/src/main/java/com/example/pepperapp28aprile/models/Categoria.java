package com.example.pepperapp28aprile.models;

import com.example.pepperapp28aprile.Persona;

public class Categoria {
    private final int i;

    public Persona.Game getGame() {
        return game;
    }

    private final Persona.Game game;

    public int getPosition() {
        return i;
    }

    public Categoria(Persona.Game game, int position) {
        this.game = game;
        this.i = position;
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

