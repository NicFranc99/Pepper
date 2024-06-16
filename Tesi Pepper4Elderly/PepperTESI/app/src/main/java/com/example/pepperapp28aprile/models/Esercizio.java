package com.example.pepperapp28aprile.models;

public class Esercizio {

    public String parola;
    public String rispostaCorretta;
    public String domanda;

    public Esercizio(String parola, String rispostaCorretta, String domanda){
        this.domanda = domanda;
        this.parola = parola;
        this.rispostaCorretta = rispostaCorretta;
    }
}
