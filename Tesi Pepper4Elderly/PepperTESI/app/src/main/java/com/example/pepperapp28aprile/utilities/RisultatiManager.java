package com.example.pepperapp28aprile.utilities;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RisultatiManager {
    private static String LOG_ID = "Log_Risulatati";
    private List<Parole> paroleList = null;

    public List<Parole> getParoleList() {
        return paroleList;
    }

    public void setParoleList(Parole parola) {
        if (paroleList == null) {
            paroleList = new ArrayList<>();
        }
        this.paroleList.add(parola);
    }

    private TimerManager t;
    private long tempototale;
    public int numeroErrori = 0;
    private List<Integer> erroriDomande;

    public RisultatiManager() {
        t = new TimerManager();
        erroriDomande = new ArrayList<>();
        try {
            t.startGame();
        } catch (ErrorGameNotStarted errorGameNotStarted) {
            Log.e(LOG_ID, errorGameNotStarted.toString());
        }
    }

    public void fineGioco() {
        try {
            tempototale = t.stopGame();
        } catch (ErrorGameNotStarted errorGameNotStarted) {
            Log.e(LOG_ID, errorGameNotStarted.toString());
        }
    }

    public String getTempoTotale() {
        Log.i(LOG_ID, "Tempo Totale: " + tempototale);

        return TimerManager.convert(tempototale);
    }

    public String getTempoMedio() {
        Log.i(LOG_ID, "Tempo Medio: " + t.getAverageQuestions());

        return TimerManager.convert(t.getAverageQuestions());

    }

    public void startDomanda() {
        try {
            t.startQuestion();
        } catch (ErrorGameNotStarted errorGameNotStarted) {
            Log.e(LOG_ID, errorGameNotStarted.toString());
        }

    }

    public void stopDomanda() {
        try {
            t.stopQuestion();
            erroriDomande.add(numeroErrori);
            // Log.e(LOG_ID, numeroErrori+"");

            numeroErrori = 0;
        } catch (ErrorGameNotStarted errorGameNotStarted) {
            Log.e(LOG_ID, errorGameNotStarted.toString());
        }

    }

    public void setError() {
        numeroErrori++;
    }

    public List<Integer> getListError() {
        return erroriDomande;
    }

    public List<Long> getListTimeQuestions() {
        return t.getListTimeQuestions();
    }

    public String getStopGameTimeClock() {
        return t.getStopGameTimeClock();
    }

    public String getStartGameTimeClock() {
        return t.getStartGameTimeClock();
    }

    public enum Risposta {
        ESISTE, NONESISTE, NONVALIDA, CORRELATA,NONCORRELATA
    }

    public static class Parole {
        Risposta risposta;

        public String getParola() {
            return parola;
        }

        String parola = "";

        public Risposta getRisposta() {
            return risposta;
        }

        public Parole(String parola, Risposta risposta) {
            this.parola = parola;
            this.risposta = risposta;

        }

    }
}
