package com.example.pepperapp28aprile.utilities;

import android.content.Context;
import android.sax.RootElement;

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



            game.setDomandaGioco(getDomandaGioco(rootelem,esercizio));
        }

        return game;
    }

    private String getDomandaGioco(JsonObject rootelem, Esercizio esercizio){
        String domanda = rootelem.has("domanda") && !rootelem.get("domanda").isJsonNull() ? rootelem.get("domanda").getAsString() : null;

        if(domanda == null){
            return esercizio.domanda;
        }

        return domanda;
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
            game.setParole(context,esercizio.parola,getDomandaGioco(rootelem,esercizio));
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
            game.setDomanda(esercizio.rispostaCorretta, esercizio.parola, getDomandaGioco(rootelem,esercizio));
        }

        return game;
    }

    public Persona.FinaliParole buildFinaliParoleGame(JsonObject rootelem){
        Persona.FinaliParole game = new Persona.FinaliParole(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.setParole(esercizio.parola,getDomandaGioco(rootelem,esercizio));
        }

        return game;
    }

    public Persona.FluenzeVerbali buildFluenzeVerbaliGame(JsonObject rootelem){
        Persona.FluenzeVerbali game = new Persona.FluenzeVerbali(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.setParole(esercizio.parola, getDomandaGioco(rootelem,esercizio));
        }

        return game;
    }

    public Persona.LettereMancanti buildLettereMancantiGame(JsonObject rootelem){
        Persona.LettereMancanti game = new Persona.LettereMancanti(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.setParole(esercizio.parola, getDomandaGioco(rootelem,esercizio));
        }

        return game;
    }

    public Persona.Musica buildMusicaGame(JsonObject rootelem){
        Persona.Musica game = new Persona.Musica(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);
        setMediaurl(rootelem,esercizi);


        for (Esercizio esercizio: esercizi) {
            Persona.Musica.Domanda domanda = new Persona.Musica.Domanda();
            domanda.setUrlMedia(esercizio.urlMedia);
            domanda.setRispostaCorretta(esercizio.rispostaCorretta);
            for (String risposta: esercizio.risposte) {
                domanda.setRispostaSbagliata(risposta);
            }
            game.setListaDomande(domanda,getDomandaGioco(rootelem, esercizio));
        }
        return game;
    }

    public Persona.Volti buildVoltiGame(JsonObject rootelem){
        Persona.Volti game = new Persona.Volti(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);
        String mediaUrlString = rootelem.has("mediaUrl") && !rootelem.get("mediaUrl").isJsonNull() ? rootelem.get("mediaUrl").getAsString() : null;

        setMediaUrlForAllEsecizi(esercizi,mediaUrlString);

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

    public Persona.Racconti buildRaccontiGame(JsonObject rootelem){
        Persona.Racconti game = new Persona.Racconti(rootelem.get("titleGame").getAsString());

        String eserciziString = rootelem.get("esercizi").getAsString();
        String testoRacconto = rootelem.get("freeText").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        setMediaurl(rootelem,esercizi);
        game.setTestoRacconto(testoRacconto);
        for (Esercizio esercizio: esercizi) {
            game.setUrlsMedia(esercizio.urlMedia);
            Persona.Racconti.Domanda domanda = new Persona.Racconti.Domanda();
            domanda.setTestoDomanda(esercizio.domanda);
            domanda.setRispostaCorretta(esercizio.rispostaCorretta);

            for (String risposta: esercizio.risposte) {
                domanda.setRispostaSbagliata(risposta);
            }

            game.setListaDomande(domanda);
        }
        return game;
    }

    //se nella colonna MediaUrl del database è stato definito almeno un valore:
    // se è stato definito un solo valore viene utilizzato quel singolo media per tutte le domande
    //Se ce ne sono più di uno allora si utilizza 1:1 e lo si assegna alla rispettiva domanda
    //Se non contiene nessun elemento (null) allora si utilizzano i mediaUrl definiti nel json "esercizi[i].mediaUrl" del database
    private void setMediaurl(JsonObject rootelem, Esercizio[] esercizi){
        String mediaUrlString = rootelem.has("mediaUrl") && !rootelem.get("mediaUrl").isJsonNull() ? rootelem.get("mediaUrl").getAsString() : null;

        if(mediaUrlString!=null){

            String[] mediaUrlList = gson.fromJson(mediaUrlString, String[].class);
            if(mediaUrlList.length == 1){
                setMediaUrlForAllEsecizi(esercizi, mediaUrlList[0]);
            }
            setEserciziMediaUrlList(esercizi, mediaUrlList);
        }
    }

    private void setEserciziMediaUrlList(Esercizio[] esercizi, String[] mediaUrlList){

            for (String mediaUrl:mediaUrlList) {
                for (Esercizio esercizio : esercizi) {
                    esercizio.urlMedia = mediaUrl;
                }
            }
    }

    //Assegna ad ogni esercizio lo stesso mediaUrl, ad sesmepio viene utilizzato nel gioco dei volti che contiene la stessa immagine
    private void setMediaUrlForAllEsecizi(Esercizio[] esercizi, String mediaUrlJsonString){

        if(mediaUrlJsonString != null){
            String[] mediaUrlList = gson.fromJson(mediaUrlJsonString, String[].class);
            String firstMerdiaUrl = mediaUrlList[0];

            for (Esercizio esercizio:esercizi)
            {
                esercizio.urlMedia = firstMerdiaUrl;
            }
        }
    }

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
        String domanda = rootelem.get("domanda").getAsString();

        Esercizio[] esercizi = gson.fromJson(eserciziString, Esercizio[].class);

        for (Esercizio esercizio: esercizi) {
            game.setLettera(esercizio.parola);
        }

        game.setDomandaGioco(domanda);
        return game;
    }
}
