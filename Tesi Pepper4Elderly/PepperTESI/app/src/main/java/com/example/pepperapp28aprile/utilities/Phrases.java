package com.example.pepperapp28aprile.utilities;

import com.aldebaran.qi.sdk.QiContext;

import java.util.ArrayList;
import java.util.Arrays;

public class Phrases {
    public static final String beforeMenuParent = "Da questo menù puoi cliccare sulla tua foto per chiamare i tuoi parenti!";
    public static final String menuGame = "vuoi giocare con me? Clicca sulla foto del gioco per cominciare!, oppure dimmelo a voce!";
    public static final String beforeMenuGame = "Da questo menù puoi cliccare sulla tua foto per giocare ai tuoi giochi preferiti!";
    public static final String phrasePepperLissenForPlay = "giochiamo a ";

    public static final String[] phrasePepperCorrectAnwswer = {
            "Risposta Giusta! Ottimo lavoro",
            "Continua cosi'!",
            "Stai andando alla grande!",
            "Bravissimo!"
    };

    public static final String fluenzeFonologicheWhenWordIsAlreadyPresent = "Hai già detto questa parola!, riproviamo!";

    public static final String[] phrasePepperWrongAnwswer = {
            "Non ci siamo, riprova!",
            "Pensaci meglio e riprova!",
            "Sbagliato, forza riproviamo!"
    };

    public static ArrayList<String> phraseStopGame = new ArrayList<String>(Arrays.asList("stop", "ferma gioco","fine gioco"));
}
