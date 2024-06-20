package com.example.pepperapp28aprile.utilities;

import android.content.Context;

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

    public Persona.FluenzeSemantiche buildFluenzeSemanticheGame(JsonObject rootelem){
        Persona.FluenzeSemantiche game = new Persona.FluenzeSemantiche(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.setCategory(esercizio.parola);
            game.setDomandaGioco(esercizio.domanda);
        }

        return game;
    }

    public Persona.EsistenzaParole buildEsistenzaParoleGame(JsonObject rootelem, Context context){
        Persona.EsistenzaParole game = new Persona.EsistenzaParole(rootelem.get("titleGame").getAsString());
        ArrayList<String> listaRisposte = null;

        String risposteString = rootelem.get("risposte").getAsString();

        listaRisposte = gson.fromJson(risposteString, ArrayList.class);

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        game.setListaRisposte(listaRisposte);
        for (Esercizio esercizio: esercizi) {
            game.setParole(context,esercizio.parola,esercizio.domanda);
        }

        return game;
    }

    public Persona.Categorizzazione buildCategorizzazioneGame(JsonObject rootelem) throws Exception {
        Persona.Categorizzazione game = new Persona.Categorizzazione(rootelem.get("titleGame").getAsString());
        ArrayList<String> listaRisposte;

        String risposteString = rootelem.get("risposte").getAsString();

        listaRisposte = gson.fromJson(risposteString, ArrayList.class);
        game.setRisposte(listaRisposte);

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.setDomanda(esercizio.rispostaCorretta, esercizio.parola, esercizio.domanda);
        }

        return game;
    }

    public Persona.FinaliParole buildFinaliParoleGame(JsonObject rootelem){
        Persona.FinaliParole game = new Persona.FinaliParole(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.setParole(esercizio.parola,esercizio.domanda);
        }

        return game;
    }

    public Persona.FluenzeVerbali buildFluenzeVerbaliGame(JsonObject rootelem){
        Persona.FluenzeVerbali game = new Persona.FluenzeVerbali(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.setParole(esercizio.parola, esercizio.domanda);
        }

        return game;
    }

    public Persona.LettereMancanti buildLettereMancantiGame(JsonObject rootelem){
        Persona.LettereMancanti game = new Persona.LettereMancanti(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.setParole(esercizio.parola, esercizio.domanda);
        }

        return game;
    }

    public Persona.Musica buildMusicaGame(JsonObject rootelem){
        Persona.Musica game = new Persona.Musica(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            Persona.Musica.Domanda domanda = new Persona.Musica.Domanda();
            domanda.setUrlMedia(esercizio.urlMedia);
            domanda.setRispostaCorretta(esercizio.rispostaCorretta);
            for (String risposta: esercizio.risposte) {
                domanda.setRispostaSbagliata(risposta);
            }
            game.setListaDomande(domanda,esercizio.domanda);
        }
        return game;
    }

    public Persona.Volti buildVoltiGame(JsonObject rootelem){
        Persona.Volti game = new Persona.Volti(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.urlMedia(esercizio.urlMedia);
            Persona.Volti.Domanda domanda = new Persona.Volti.Domanda();
            domanda.setTestoDomanda(esercizio.domanda);
            domanda.setRispostaCorretta(esercizio.rispostaCorretta);

            for (String risposta: esercizio.risposte) {
                domanda.setRispostaSbagliata(risposta);
            }

            game.setListaDomande(domanda);
        }
        return game;
    }
/*
    public Persona.Racconti buildRaccontiGame(JsonObject rootelem){
        Persona.Racconti game = new Persona.Racconti(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.urlMedia(esercizio.urlMedia);
            Persona.Volti.Domanda domanda = new Persona.Volti.Domanda();
            domanda.setTestoDomanda(esercizio.domanda);
            domanda.setRispostaCorretta(esercizio.rispostaCorretta);

            for (String risposta: esercizio.risposte) {
                domanda.setRispostaSbagliata(risposta);
            }

            game.setListaDomande(domanda);
        }
        return game;
    }
*/
    public Persona.Mesi buildMesiGame(JsonObject rootelem){
        Persona.Mesi game = new Persona.Mesi(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            Persona.Mesi.Domanda domanda = new Persona.Mesi.Domanda();
            domanda.setTestoDomanda(esercizio.domanda);
            domanda.setRispostaCorretta(esercizio.rispostaCorretta);
            for (String risposta: esercizio.risposte) {
                domanda.setRispostaSbagliata(risposta);
            }
            game.setListaDomande(domanda);
        }

        return game;
    }

    public Persona.FluenzeFonologiche buildFluenzeFonologiche(JsonObject rootelem){
        Persona.FluenzeFonologiche game = new Persona.FluenzeFonologiche(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.setDomandaGioco(esercizio.domanda, esercizio.parola);
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
