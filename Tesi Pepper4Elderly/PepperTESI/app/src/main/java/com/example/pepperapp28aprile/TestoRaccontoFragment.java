package com.example.pepperapp28aprile;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.graphics.text.LineBreaker;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.ImageLoader;
import com.example.pepperapp28aprile.utilities.VoiceManager;

import java.io.InputStream;
import java.net.URL;
import java.util.List;


public class TestoRaccontoFragment extends Fragment {
    private static final String ID_LOG_TESTO_RACCONTO_FRAGMENT = "TESTO_RACCONTO_FRAGMENT";
    private final String testoRacconto, titolo;
    private final int positionGame;
    private TextView txtTestoRacconto, txttitoloracconto;
    private Persona.Game game;
    public  static Button domandaButton;
    public Persona.Racconti racconto;
    public  List<String> urlMedia;
    public  VerticalScrollingTextView tvContent;
    public TestoRaccontoFragment(Persona.Game game,int positionGame) {
        Persona.Racconti g = (Persona.Racconti) game;
        this.game = game;
        this.positionGame = positionGame;
        testoRacconto = g.getTestoRacconto();
        titolo = g.getTitleGame();
        racconto = ( Persona.Racconti )game;
         urlMedia = racconto.getListUrlsMedia();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.testo_racconto_view, container, false);

        tvContent = (VerticalScrollingTextView) v.findViewById(R.id.tvContent);
        tvContent.setMovementMethod(new ScrollingMovementMethod());
        tvContent.scroll();
        tvContent.setText(testoRacconto);

        VoiceManager tts = VoiceManager.getIstance(getContext());
        tts.play(testoRacconto, TextToSpeech.QUEUE_FLUSH);


        ImageView imageRacconto = (ImageView) v.findViewById(R.id.img_racconto);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i=1;
            public void run() {
                try {
                    Bitmap img = BitmapFactory.decodeStream((InputStream) new URL(urlMedia.get(i)).getContent());
                    imageRacconto.setImageBitmap(img);
                    i++;
                }catch (Exception e) {
                    System.out.println("Exc="+e);
                }
                if(i>urlMedia.size())
                {
                    i=0;
                }
                tvContent.setVisibility(View.VISIBLE); //Lor endo visible perche' lo renderizza prima di farlo scrollare automaticamente..
                handler.postDelayed(this, 7000);  //for interval...

            }
        };
        handler.postDelayed(runnable, 0); //for initial delay..

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

        domandaButton = (Button) v.findViewById(R.id.btnvaidomande);

        (domandaButton).setOnClickListener(view -> {
            tts.stop();
            getActivity().getSupportFragmentManager().beginTransaction().remove(TestoRaccontoFragment.this).commit();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(container.getId(), new GameFragment(game,positionGame)).commit();

        });
        return v;
    }

    public static void setImageDrawableWithAnimation(ImageView imageView,
                                                     Drawable drawable,
                                                     int duration) {
        Drawable currentDrawable = imageView.getDrawable();
        if (currentDrawable == null) {
            imageView.setImageDrawable(drawable);
            return;
        }

        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                currentDrawable,
                drawable
        });
        imageView.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(duration);
    }

}
