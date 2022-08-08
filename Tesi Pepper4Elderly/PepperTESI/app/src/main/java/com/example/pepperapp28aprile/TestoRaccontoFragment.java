package com.example.pepperapp28aprile;

import android.graphics.Color;
import android.graphics.text.LineBreaker;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pepperapp28aprile.utilities.VoiceManager;


public class TestoRaccontoFragment extends Fragment {
    private static final String ID_LOG_TESTO_RACCONTO_FRAGMENT = "TESTO_RACCONTO_FRAGMENT";
    private final String testoRacconto, titolo;
    private final int positionGame;
    private TextView txtTestoRacconto, txttitoloracconto;
    private Persona.Game game;
    public TestoRaccontoFragment(Persona.Game game,int positionGame) {
        Persona.Racconti g = (Persona.Racconti) game;
        this.game = game;
        this.positionGame = positionGame;
        testoRacconto = g.getTestoRacconto();
        titolo = g.getTitleGame();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.testo_racconto_view, container, false);

        VoiceManager tts = VoiceManager.getIstance(getContext());
        tts.play(testoRacconto, TextToSpeech.QUEUE_FLUSH);
        tts.setListener(new VoiceManager.StatusListener() {
            @Override
            public void onStart() {
                Log.i(ID_LOG_TESTO_RACCONTO_FRAGMENT, " START");
            }

            @Override
            public void onDone() {
                Log.i(ID_LOG_TESTO_RACCONTO_FRAGMENT, " DONE");

                getActivity().getSupportFragmentManager().beginTransaction().remove(TestoRaccontoFragment.this)
                        .commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(container.getId(), new GameFragment(game,positionGame)).commit();
            }

            @Override
            public void onError() {
                Log.i(ID_LOG_TESTO_RACCONTO_FRAGMENT, " ERROR");

            }

            @Override
            public void onRangeStart(String utteranceId, int start, int end, int frame) {
                Log.i(ID_LOG_TESTO_RACCONTO_FRAGMENT, "onRangeStart() ... utteranceId: " + utteranceId + ", start: "
                        + start + ", end: " + end + ", frame: " + frame);

                // onRangeStart non viene eseguito sul thread principale
                // mentre manipoliamo le visualizzazioni sul thread principale:
                getActivity().runOnUiThread(() -> {

                    Spannable textWithHighlights = new SpannableString(testoRacconto);
                    textWithHighlights.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    textWithHighlights.setSpan(
                            new BackgroundColorSpan(getContext().getColor(R.color.category_no_focused)), start, end,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    txtTestoRacconto.setText(textWithHighlights);
                });
            }
        });

        txtTestoRacconto = v.findViewById(R.id.txtracconto);
        txtTestoRacconto.setText(testoRacconto);
        //txtTestoRacconto.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);

        txttitoloracconto = v.findViewById(R.id.txttitoloracconto);
        txttitoloracconto.setText(titolo);

        ((Button) v.findViewById(R.id.btnvaidomande)).setOnClickListener(view -> {
            tts.stop();
            getActivity().getSupportFragmentManager().beginTransaction().remove(TestoRaccontoFragment.this).commit();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(container.getId(), new GameFragment(game,positionGame)).commit();

        });
        return v;
    }

}
