package com.example.pepperapp28aprile.models;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Esercizio {
    public String urlMedia;
    public String parola;
    public String rispostaCorretta;
    public String domanda;
    public ArrayList<String> risposte;

    public Esercizio(String parola, String rispostaCorretta, String domanda, ArrayList<String> risposte, String urlMedia){
        this.domanda = domanda;
        this.parola = parola;
        this.rispostaCorretta = rispostaCorretta;
        this.risposte = risposte;
        this.urlMedia = urlMedia;
        }
    }
