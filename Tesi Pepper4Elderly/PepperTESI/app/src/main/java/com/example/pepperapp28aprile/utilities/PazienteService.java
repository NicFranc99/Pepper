package com.example.pepperapp28aprile.utilities;

import android.content.Context;

import com.example.pepperapp28aprile.Persona;
import com.example.pepperapp28aprile.models.Esercizio;
import com.example.pepperapp28aprile.models.GameResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class PazienteService {

    private GameBuilder gameBuilder;
    public PazienteService(Context context){
        gameBuilder = new GameBuilder(context);
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

    public ArrayList<Persona.Game> getGameListByEldId(String id,boolean orderByGamesWithoutResults){
        String sURL = "http://bettercallpepper.altervista.org/api/getGamesByElderId.php?eldid=" + id + "&orderByGamesWithoutResults=" + orderByGamesWithoutResults;
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
                    Persona.Appartenenza game = gameBuilder.buildAppartenenzaGame(rootelem);
                    addGameToList(rootelem, game, gameList);
                }

                 if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.Categorizzazione.class.getSimpleName())){
                     Persona.Categorizzazione game = gameBuilder.buildCategorizzazioneGame(rootelem);
                     addGameToList(rootelem, game, gameList);
                 }

                 if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.CombinazioniLettere.class.getSimpleName())){
                     Persona.CombinazioniLettere game = gameBuilder.buildCombinazioniLettereGame(rootelem);
                     addGameToList(rootelem, game, gameList);
                 }

                 if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.EsistenzaParole.class.getSimpleName())){
                     Persona.EsistenzaParole game = gameBuilder.buildEsistenzaParoleGame(rootelem);
                     addGameToList(rootelem, game, gameList);
                 }

                 if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.FinaliParole.class.getSimpleName())){
                     Persona.FinaliParole game = gameBuilder.buildFinaliParoleGame(rootelem);
                     addGameToList(rootelem, game, gameList);
                 }

                 if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.FluenzeFonologiche.class.getSimpleName())){
                     Persona.FluenzeFonologiche game = gameBuilder.buildFluenzeFonologiche(rootelem);
                     addGameToList(rootelem, game, gameList);
                 }

                 if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.FluenzeSemantiche.class.getSimpleName())){
                     Persona.FluenzeSemantiche finaliParole = gameBuilder.buildFluenzeSemanticheGame(rootelem);
                     addGameToList(rootelem, finaliParole, gameList);
                 }

                 if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.FluenzeVerbali.class.getSimpleName())){
                     Persona.FluenzeVerbali game = gameBuilder.buildFluenzeVerbaliGame(rootelem);
                     addGameToList(rootelem, game, gameList);
                 }

                 if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.LettereMancanti.class.getSimpleName())){
                     Persona.LettereMancanti game = gameBuilder.buildLettereMancantiGame(rootelem);
                     addGameToList(rootelem, game, gameList);
                 }

                 if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.Mesi.class.getSimpleName())){
                     Persona.Mesi game = gameBuilder.buildMesiGame(rootelem);
                     addGameToList(rootelem, game, gameList);
                 }

                 if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.Musica.class.getSimpleName())){
                     Persona.Musica game = gameBuilder.buildMusicaGame(rootelem);
                     addGameToList(rootelem, game, gameList);
                 }

                 if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.Volti.class.getSimpleName())){
                     Persona.Volti game = gameBuilder.buildVoltiGame(rootelem);
                     addGameToList(rootelem, game, gameList);
                 }

                /* if(rootelem.get("nameCategory").getAsString().equalsIgnoreCase(Persona.Volti.class.getSimpleName())){
                     Persona.Racconti game = gameBuilder.buildRaccontiGame(rootelem);
                     addGameToList(rootelem, game, gameList);
                 }

                 */
             }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return gameList;
    }

    private void addGameToList(JsonObject rootelem, Persona.Game game, ArrayList<Persona.Game> gameList){
        boolean hasVocalInput =  (rootelem.get("hasVocalInput").getAsInt() != 0);
        String gameDescription = rootelem.get("explaitationText").getAsString();
        String namePaziente =  rootelem.get("nameElderly").getAsString();
        String creationDateResult = rootelem.has("creationDateResult") && !rootelem.get("creationDateResult").isJsonNull() ? rootelem.get("creationDateResult").getAsString() : null;
        Integer idResult = rootelem.has("idResult") && !rootelem.get("idResult").isJsonNull() ? rootelem.get("idResult").getAsInt() : null;
        Integer score = rootelem.has("score") && !rootelem.get("score").isJsonNull() ? rootelem.get("score").getAsInt() : null;

        game.setDescrizioneGioco(gameDescription,namePaziente);
        game.addInputType(getInputType(hasVocalInput));
        game.setLivello(1);

        if(idResult != null){
            game.gameResult = new GameResult(score,creationDateResult,idResult);
        }

        gameList.add(game);
    }


    private Persona.Game.TypeInputGame getInputType(boolean hasVocalType){
        if(hasVocalType)
            return Persona.Game.TypeInputGame.VOCALE;

        return Persona.Game.TypeInputGame.SELEZIONE;
    }
}
