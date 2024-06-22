package com.example.pepperapp28aprile;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aldebaran.qi.Consumer;
import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.Qi;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.object.conversation.BodyLanguageOption;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.conversation.SpeechEngine;
import com.aldebaran.qi.sdk.object.locale.Locale;
import com.example.pepperapp28aprile.map.RobotHelper;
import com.google.android.material.card.MaterialCardView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;


public class TestoRaccontoFragment extends Fragment {
    private static final String ID_LOG_TESTO_RACCONTO_FRAGMENT = "TESTO_RACCONTO_FRAGMENT";
    private final String testoRacconto, titolo;
    private TextView txtTestoRacconto, txttitoloracconto;
    private Persona.Game game;
    public  static Button domandaButton;
    public RobotHelper robotHelper;
    public QiContext qiContext;
    public Persona.Racconti racconto;
    public  List<String> urlMedia;
    public  VerticalScrollingTextView tvContent;
    public GameActivity gameActivity;
    private Persona paziente;
    public TestoRaccontoFragment(Persona.Game game,Persona paziente) {
        Persona.Racconti g = (Persona.Racconti) game;
        this.game = game;
        testoRacconto = g.getTestoRacconto();
        titolo = g.getTitleGame();
        racconto = (Persona.Racconti)game;
        urlMedia = racconto.getListUrlsMedia();
        this.paziente = paziente;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.testo_racconto_view, container, false);

        //Istanzio lo scroll automatico e gli imposto il testo del racconto.
        tvContent = (VerticalScrollingTextView) v.findViewById(R.id.tvContent);
        tvContent.setMovementMethod(new ScrollingMovementMethod());
        tvContent.scroll();
        tvContent.setText(testoRacconto);
        gameActivity = (GameActivity) getActivity();
        domandaButton = (Button) v.findViewById(R.id.btnvaidomande);
        Future<Void> futureSpeekPepper = gameActivity.getRobotHelper().say(testoRacconto,true);
        // Chain a lambda to the future.

        //TODO: Quando pepper termina il racconto viene mostrato il pulsante per accedere alle domande!
        futureSpeekPepper.andThenConsume(Qi.onUiThread((Consumer<Void>) ignore -> {
            domandaButton.setVisibility(View.VISIBLE);
        }));

      //  VoiceManager tts = VoiceManager.getIstance(getContext());
    //    tts.play(testoRacconto, TextToSpeech.QUEUE_FLUSH);


        if(!urlMedia.isEmpty()) { //Il Racconto ha dei media associati da visualizzare durante il racconto
            ImageView imageRacconto = (ImageView) v.findViewById(R.id.img_racconto);
            imageRacconto.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                int i = 1;

                public void run() {
                    try {
                        Bitmap img = BitmapFactory.decodeStream((InputStream) new URL(urlMedia.get(i)).getContent());
                        imageRacconto.setImageBitmap(img);
                        i++;
                    } catch (Exception e) {
                        System.out.println("Exc=" + e);
                    }
                    if (i > urlMedia.size()) {
                        i = 0;
                    }
                    //tvContent.setVisibility(View.VISIBLE); //Lo rendo visible perche' lo renderizza prima di farlo scrollare automaticamente..
                    handler.postDelayed(this, 10000);  //for interval...

                }
            };
            handler.postDelayed(runnable, 0); //for initial delay..
        }
        else {
            tvContent.setHeight(50);
            tvContent.setPadding(100,0,0,0);
            MaterialCardView cardView = v.findViewById(R.id.card);
        }

       /* tts.setListener(new VoiceManager.StatusListener() {
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
        }); */


        (domandaButton).setOnClickListener(view -> {
           // tts.stop();
            getActivity().getSupportFragmentManager().beginTransaction().remove(TestoRaccontoFragment.this).commit();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(container.getId(), new GameFragment(game,paziente)).commit();

        });



        return v;
    }
}
