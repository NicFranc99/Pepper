package com.example.pepperapp28aprile.utilities;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.Locale;

public class VoiceManager {

    public interface StatusListener {
        void onStart();
        void onDone();
        void onError();
        void onRangeStart(String utteranceId, int start, int end, int frame);
    }

    private static final String IDVOICE = "ID_VOICE_TTS";

    public static final int QUEUE_ADD = 1;
    public static final int QUEUE_FLUSH = 0;

    private String totalChars = "";
    private int totalCall = 0;

    private TextToSpeech tts;
    private static VoiceManager voiceManager = null;

    public static VoiceManager getIstance(Context c) {
        if (voiceManager == null) {
            voiceManager = new VoiceManager(c);
        }
        return voiceManager;
    }

    private VoiceManager(Context c) {
        tts = new TextToSpeech(c, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int risultato = tts.setLanguage(Locale.ITALY);
                tts.setSpeechRate(1);
                tts.setPitch(1);
                if (risultato == TextToSpeech.LANG_MISSING_DATA || risultato == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Linguaggio non supportato");
                }
            } else {
                Log.e("TTS", "Inizializzazione fallita");
            }
        });
    }

    public void play(String text, final int queueMode) {
        totalChars += text;
        totalCall++;

        Log.i(IDVOICE, " Total Call: " + totalCall + " CharTotal: " + totalChars.length());
        tts.speak(text, queueMode, null, IDVOICE);
    }

    public void stop() {
        tts.stop();
    }

    public void setListener(StatusListener listener) {
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                listener.onStart();
            }

            @Override
            public void onDone(String utteranceId) {
                listener.onDone();
            }

            @Override
            public void onError(String utteranceId) {
                listener.onError();
            }

            @Override
            public void onRangeStart(String utteranceId, int start, int end, int frame) {
                listener.onRangeStart(utteranceId, start, end, frame);
                super.onRangeStart(utteranceId, start, end, frame);
            }

        });
    }
}
