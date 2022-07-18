package com.example.pepperapp28aprile.utilities;

import android.os.SystemClock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimerManager {

    private long startGame = 0L;
    private long startGameTimeClock = 0L;

    private long startQuestion = 0L;
    private long stopGameTimeClock = 0L;

    private List<Long> listTimeQuestions;

    private boolean isStartGame;
    private boolean isStartQuestion;

    public TimerManager() {
        isStartGame = false;
        isStartQuestion = false;
        listTimeQuestions = new ArrayList<>();
    }

    public void startGame() throws ErrorGameNotStarted {

        if (!isStartGame) {
            isStartGame = true;
            startGame = 0L;
            this.startGame = SystemClock.uptimeMillis();
            this.startGameTimeClock = System.currentTimeMillis();

        } else {
            throw new ErrorGameNotStarted("Il timer é stato giá avviato");
        }
    }

    /**
     * Ritorna il tempo totale di gioco
     *
     * @return il tempo totale del gioco
     */
    public long stopGame() throws ErrorGameNotStarted {
        if (isStartGame) {
            if (isStartQuestion) {
                throw new ErrorGameNotStarted(
                        "Il Gioco non puó terminar, il timer della domanda non si é ancora stoppato");
            } else {
                isStartGame = false;
                long stopGame = SystemClock.uptimeMillis();
                long totalGame = stopGame - startGame;
                startGame = 0L;
                this.stopGameTimeClock = System.currentTimeMillis();
                return totalGame;
            }

        } else {
            throw new ErrorGameNotStarted("Il timer del gioco non é stato ancora avviato");
        }
    }

    public static String convert(long totalGame) {
        int secs = (int) (totalGame / 1000);
        int min = secs / 60;
        secs %= 60;
        int milliseconds = (int) (totalGame % 1000);
        if (milliseconds > 500) {
            secs = secs + 1;
        }
        // return ""+min+" min "+String.format("%2d",secs)+" sec
        // "+String.format("%3d",milliseconds)+" ms";
        return "" + min + " min " + String.format("%2d", secs) + " sec ";

    }

    /**
     * Ritorna il tempo totale della Domanda
     *
     * @return il tempo totale dell Domanda
     */
    public String stopQuestion() throws ErrorGameNotStarted {
        if (isStartQuestion) {
            long stopQuestion = SystemClock.uptimeMillis();

            long totalQuestion = stopQuestion - startQuestion;
            isStartQuestion = false;

            startQuestion = 0L;

            listTimeQuestions.add(totalQuestion);
            return convert(totalQuestion);
        } else {
            throw new ErrorGameNotStarted("Il timer della domanda non é stato ancora avviato");

        }
    }

    public void startQuestion() throws ErrorGameNotStarted {
        if (isStartGame) {
            isStartQuestion = true;
            startQuestion = 0L;
            this.startQuestion = SystemClock.uptimeMillis();
        } else {
            throw new ErrorGameNotStarted("Il gioco  é stato ancora avviato");
        }

    }

    public long getAverageQuestions() {
        long tootal = 0L;
        if (!listTimeQuestions.isEmpty()) {
            for (Long time : listTimeQuestions) {
                tootal += time;
            }
            return (tootal / listTimeQuestions.size());
        }

        return 0;
    }

    public List<Long> getListTimeQuestions() {
        return listTimeQuestions;
    }

    public String getStartGameTimeClock() {
        return covertToString(startGameTimeClock);
    }

    public String getStopGameTimeClock() {
        return covertToString(stopGameTimeClock);
    }

    private String covertToString(long gameTimeClock) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date(gameTimeClock);
        return format.format(date);

    }
}
