package com.example.pepperapp28aprile.utilities;

import com.example.pepperapp28aprile.Persona;
import com.example.pepperapp28aprile.models.Esercizio;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class GameBuilder {
    private Gson gson;
    public GameBuilder(){
        gson = new Gson();
    }

    public Persona.Appartenenza buildAppartenenzaGame(JsonObject rootelem){
        Persona.Appartenenza game = new Persona.Appartenenza(rootelem.get("titleGame").getAsString());
        ArrayList<String> listaRisposte = null;

        String risposteString = rootelem.get("risposte").getAsString();

        listaRisposte = gson.fromJson(risposteString, ArrayList.class);

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.setDomanda(esercizio.parola,listaRisposte,esercizio.rispostaCorretta);
        }

        return game;
    }

    public Persona.Categorizzazione buildCategorizzazioneGame(JsonObject rootelem){
        Persona.Categorizzazione game = new Persona.Categorizzazione(rootelem.get("titleGame").getAsString());
        ArrayList<String> listaRisposte;

        String risposteString = rootelem.get("risposte").getAsString();

        listaRisposte = gson.fromJson(risposteString, ArrayList.class);
        game.setRisposte(listaRisposte);

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.setDomanda(esercizio.rispostaCorretta, esercizio.parola);
        }

        return game;
    }

    public Persona.CombinazioniLettere buildCombinazioniLettereGame(JsonObject rootelem){
        Persona.CombinazioniLettere game = new Persona.CombinazioniLettere(rootelem.get("titleGame").getAsString());
        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.setLettera(esercizio.parola);
        }
        
        game.setDomandaGioco();
        return game;
    }
}
