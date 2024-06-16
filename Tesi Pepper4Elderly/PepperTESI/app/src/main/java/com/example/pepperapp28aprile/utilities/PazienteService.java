package com.example.pepperapp28aprile.utilities;

import com.example.pepperapp28aprile.Persona;
import com.example.pepperapp28aprile.models.Esercizio;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class PazienteService {

    private GameBuilder gameBuilder;
    public PazienteService(){
        gameBuilder = new GameBuilder();
    }

    public Persona getPazienteById(String id) {
        String sURL = "http://bettercallpepper.altervista.org/api/getElderById.php?eldid=" + id;
        Persona paziente = null;
        try {
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonObject rootelem = root.getAsJsonObject();

            paziente = new Persona(
                    rootelem.get("propic").getAsString(),
                    rootelem.get("idEderly").getAsInt(),
                    rootelem.get("name").getAsString(),
                    rootelem.get("surname").getAsString(),
                    rootelem.get("city").getAsString(),
                    rootelem.get("birthDate").getAsString(),
                    rootelem.get("gender").getAsString(),
                    rootelem.get("room").getAsInt());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return paziente;
    }

    public ArrayList<Persona.Game> getGameListByEldId(String id){
        String sURL = "http://bettercallpepper.altervista.org/api/getGamesByElderId.php?eldid=" + id;
        ArrayList<Persona.Game> gameList = new ArrayList<>();
        try {
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element

            JsonArray rootarr = root.getAsJsonArray(); //May be an array, may be an object.

             for (int i = 0; i < rootarr.size(); i++) {
                 JsonObject rootelem = rootarr.get(i).getAsJsonObject();

                if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.Appartenenza.class.getSimpleName())){
                    Persona.Appartenenza appartenenzaGame = gameBuilder.buildAppartenenzaGame(rootelem);
                    addGameToList(rootelem, appartenenzaGame, gameList);
                }

                 if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.Categorizzazione.class.getSimpleName())){
                     Persona.Categorizzazione categorizzazioneGame = gameBuilder.buildCategorizzazioneGame(rootelem);
                     addGameToList(rootelem, categorizzazioneGame, gameList);
                 }

                 if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.CombinazioniLettere.class.getSimpleName())){
                     Persona.CombinazioniLettere combinazioniLettere = gameBuilder.buildCombinazioniLettereGame(rootelem);
                     addGameToList(rootelem, combinazioniLettere, gameList);
                 }
             }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return gameList;
    }

    private void addGameToList(JsonObject rootelem, Persona.Game game, ArrayList<Persona.Game> gameList){
        boolean hasVocalInput =  (rootelem.get("hasVocalInput").getAsInt() != 0);
        String gameDescription = rootelem.get("explaitationText").getAsString();
        String namePaziente =  rootelem.get("nameEderly").getAsString();
        game.setDescrizioneGioco(gameDescription,namePaziente);
        game.addInputType(getInputType(hasVocalInput));
        gameList.add(game);
    }


    private Persona.Game.TypeInputGame getInputType(boolean hasVocalType){
        if(hasVocalType)
            return Persona.Game.TypeInputGame.VOCALE;

        return Persona.Game.TypeInputGame.SELEZIONE;
    }
}
