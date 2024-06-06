package com.example.pepperapp28aprile;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.aldebaran.qi.Consumer;
import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.Qi;
import com.aldebaran.qi.sdk.QiContext;
import com.example.pepperapp28aprile.map.RobotHelper;
import com.example.pepperapp28aprile.models.RecyclerViewAnswersAdapter;
import com.example.pepperapp28aprile.utilities.Phrases;
import com.example.pepperapp28aprile.utilities.RisultatiManager;
import com.example.pepperapp28aprile.utilities.Util;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameFragment extends Fragment{
    private View v;
    private RecyclerView recyclerView;
    private ArrayList<RecyclerAnswers> recyclerAnswersArrayList;
    private List<Persona.Game.Domanda> lisaDomande;
    private MediaManagerFragment fragment;
    private ViewGroup containerFragment;
    private RisultatiManager risultatiManager;
    private Persona.Game game;
    public static int positiongame = 0;
    private RobotHelper robotHelper;
    private QiContext qiContext;
    public static PepperLissenerActivity lissenerActivity;
    public static String rispostaUtente;
    public GameActivity gameActivity;
    private  String requestText = new String();
    private String requestTextForPepper = new String();

    private LottieAnimationView lottieAnimationView;
    private RelativeLayout containerAnimations;
    private ShapeableImageView imageMic;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    public static Persona.Game.Domanda domanda;
    private Future<Void> requestSay;

    /*public GameFragment(int positionGame, Persona.Game.TypeInputGame chose) {
        this.positionGame = positionGame;
        this.choseTypeInputGame = chose;
        risultatiManager = new RisultatiManager();
    } */

    public GameFragment(Persona.Game game,int positionGame) {
        this.game = game;
        //this.positiongame = positionGame; Se non serve vedere di togliere positionGame come parametro del construttore
        risultatiManager = new RisultatiManager();
    }

    private List<Integer> positioslistclic= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.containerFragment = container;
        v = inflater.inflate(R.layout.question_game_fragment, container, false);
        lisaDomande = game.getListaDomandeGioco();
         gameActivity = (GameActivity) getActivity();
         qiContext = gameActivity.getQiContext();
         robotHelper = gameActivity.getRobotHelper();
        // mischio le domande
        Collections.shuffle(lisaDomande);
        recyclerView = v.findViewById(R.id.idCourseRV);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        positiongame = 0;
        iniziaGioco();
        Globals.gameFragmentRun = true;
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    }

    /**
     * Per il momento per i giochi che richiedono l'iterazione con l'utente non ascoltano l'input vocale. Bisogna utilizzare il microfono del tablet di pepper
     */
    private void iniziaGioco() {
        risultatiManager.startDomanda();
        domanda = lisaDomande.get(positiongame);
        FrameLayout listaMedia = v.findViewById(R.id.linearimage);
        TextView testoDomanda = v.findViewById(R.id.testoDomanda);
        TextView testoparola = v.findViewById(R.id.testoparola);
        containerAnimations = v.findViewById(R.id.viewanim);

//================Scelta modalita Input===========================

      /*  if (choseTypeInputGame == Persona.Game.TypeInputGame.SELEZIONE) {
            containerAnimations.setVisibility(View.GONE);
        } else if (choseTypeInputGame == Persona.Game.TypeInputGame.VOCALE) */

        //if(game instanceof Persona.FinaliParole || game instanceof Persona.CombinazioniLettere){
            //processaRisposta(PepperLissenerActivity.risposta,domanda,positiongame);
            //robotHelper = lissenerActivity.getRobotHelper();
            //qiContext = lissenerActivity.getQiContext();

        //Inizio processo parola gioco lissener
        requestTextForPepper = domanda.getTestoDomanda()+ " " + domanda.getTestoParola();
        if(game.getInputType().get(0) == Persona.Game.TypeInputGame.VOCALE)
        {
            containerAnimations.setVisibility(View.VISIBLE);
            lottieAnimationView = containerAnimations.findViewById(R.id.wave);
            imageMic = containerAnimations.findViewById(R.id.btnvocalinput);

            containerAnimations.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    imageMic.setBackgroundColor(getContext().getColor(R.color.category_focused));
                } else {
                    imageMic.setBackgroundColor(getContext().getColor(R.color.category_no_focused));
                }
            });
            containerAnimations.setOnClickListener(v -> {
               // VoiceManager.getIstance(getContext()).stop();
                if (imageMic.getVisibility() == View.VISIBLE) {
                    requestSay.cancel(true);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    lottieAnimationView.playAnimation();
                    imageMic.setVisibility(View.INVISIBLE);
                } else {
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    lottieAnimationView.cancelAnimation();
                    imageMic.setVisibility(View.VISIBLE);
                }
            });
            containerAnimations.requestFocus();

           lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    speechRecognizer.startListening(speechRecognizerIntent);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    speechRecognizer.stopListening();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });

            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                }

                @Override
                public void onBeginningOfSpeech() {
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                }

                @Override
                public void onEndOfSpeech() {
                    lottieAnimationView.cancelAnimation();
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    lottieAnimationView.cancelAnimation();
                    imageMic.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(int error) {
                    Log.e("RISPOSTA_VOCALE", "errore " + error);
                    if (SpeechRecognizer.ERROR_AUDIO == error || SpeechRecognizer.ERROR_CLIENT == error
                            || SpeechRecognizer.ERROR_NO_MATCH == error
                            || SpeechRecognizer.ERROR_SPEECH_TIMEOUT == error) {
                      // VoiceManager.getIstance(getContext()).play("Non ho capito. Ripeti ", VoiceManager.QUEUE_ADD);
                        requestSay = robotHelper.say("Non ho capito. Ripeti ");
                    }
                }

                //Recupero parola vocale dal microfono
                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    Toast.makeText(getContext(), data.toString(), Toast.LENGTH_SHORT).show();
                    String risposta = data.toString().replace("[", "").replace("]", "").toLowerCase();

                    Log.e("RISPOSTA_VOCALE", "risposta " + risposta);

                    if (game instanceof Persona.CombinazioniLettere ||
                        game instanceof Persona.FluenzeFonologiche ||
                        game instanceof Persona.FluenzeVerbali ||
                        game instanceof Persona.FluenzeSemantiche ||
                        game instanceof Persona.FinaliParole){

                        if (!(game instanceof  Persona.FinaliParole) &&!(game instanceof  Persona.FluenzeVerbali) && Util.isParolaPhraseToStopGame(risposta)) {
                            risultatiManager.stopDomanda();
                            risultatiManager.fineGioco();
                            getActivity().getSupportFragmentManager().beginTransaction().remove(GameFragment.this)
                                    .add(containerFragment.getId(), new FinishGameFragment(game, risultatiManager))
                                    .commit();
                        }
                        else {
                            if( game instanceof Persona.FluenzeVerbali || game instanceof Persona.FluenzeSemantiche ){
                                domanda.checkDomandaSimiliarita(getContext(),domanda.getTestoParola() , risposta, new Util.SimilaritaParoleListener() {
                                    @Override
                                    public void valida(float value) {
                                        AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.CORRECT,"Ottimo lavoro. Vai avanti così ");
                                        requestSay = correctAnswer();
                                        answerDialogFragment.show();
                                        answerDialogFragment.setOnDismissListener(dialog -> {
                                            if(game instanceof Persona.FluenzeVerbali){
                                                if  (lisaDomande.size() - 1 == positiongame) {
                                                    risultatiManager.stopDomanda();

                                                    risultatiManager.fineGioco();
                                                    getActivity().getSupportFragmentManager().beginTransaction().remove(GameFragment.this)
                                                            .add(containerFragment.getId(), new FinishGameFragment(game, risultatiManager))
                                                            .commit();
                                                } else {
                                                    if (fragment != null) {
                                                        getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                                                    }
                                                    risultatiManager.stopDomanda();

                                                    positiongame++;
                                                    iniziaGioco();
                                                }
                                            }
                                        });
                                        risultatiManager.setParoleList(new RisultatiManager.Parole(risposta, RisultatiManager.Risposta.CORRELATA));
                                    }

                                    @Override
                                    public void nonvalida(float value) {
                                        Log.e("RISPOSTA_VOCALE", "NON ESISTE ");
                                        String testoDialog="";
                                        if(game instanceof Persona.FluenzeVerbali){
                                            testoDialog="Pensaci meglio. E dimmi un'altra parola";
                                            wrongAnswer().andThenCompose(action -> robotHelper.say(requestTextForPepper));
                                        }
                                        if(game instanceof Persona.FluenzeSemantiche){
                                            testoDialog="Non é molto attinente, pensaci meglio ";
                                            wrongAnswer().andThenCompose(action -> robotHelper.say(requestTextForPepper));
                                        }

                                        risultatiManager.setParoleList(new RisultatiManager.Parole(risposta, RisultatiManager.Risposta.NONCORRELATA));
                                        AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.WRONG, testoDialog);
                                        answerDialogFragment.show();
                                    }

                                    @Override
                                    public void errore() {
                                        Toast.makeText(getContext(),"C'é qualcosa che non va, prova ad accendere internet o ad riavviare il gioco ",Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                            else if (controllaInBaseAlGioco(risposta.trim().replaceAll("\\s+",""), domanda.getTestoParola().toLowerCase())) { //Fluenze fonologiche
                                domanda.checkDomandaOnline(getContext(), risposta, new Util.RicercaParoleListener() {
                                    @Override
                                    public void esiste() {
                                        if(game instanceof Persona.FinaliParole||game instanceof Persona.FluenzeVerbali){
                                            AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.CORRECT);
                                            answerDialogFragment.show();
                                            answerDialogFragment.setOnDismissListener(dialog -> {
                                                if (lisaDomande.size() - 1 == positiongame) {
                                                    risultatiManager.stopDomanda();

                                                    risultatiManager.fineGioco();
                                                    getActivity().getSupportFragmentManager().beginTransaction().remove(GameFragment.this)
                                                            .add(containerFragment.getId(), new FinishGameFragment(game, risultatiManager))
                                                            .commit();
                                                } else {
                                                    if (fragment != null) {
                                                        getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                                                    }
                                                    requestSay = correctAnswer();
                                                    risultatiManager.stopDomanda();

                                                    positiongame++;
                                                    iniziaGioco();
                                                }
                                            });
                                            risultatiManager.setParoleList(new RisultatiManager.Parole(risposta, RisultatiManager.Risposta.ESISTE));
                                        }else {
                                            Log.e("RISPOSTA_VOCALE", "ESISTE");
                                            risultatiManager.setParoleList(new RisultatiManager.Parole(risposta, RisultatiManager.Risposta.ESISTE));
                                            AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.CORRECT, "La parola ESISTE");
                                            requestSay = correctAnswer();
                                            answerDialogFragment.show();
                                        }
                                    }

                                    @Override
                                    public void nonesiste() { //Fluenze fonologiche se la parola non esiste
                                        Log.e("RISPOSTA_VOCALE", "NON ESISTE ");
                                        risultatiManager.setParoleList(new RisultatiManager.Parole(risposta, RisultatiManager.Risposta.NONESISTE));
                                        AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.WRONG, "La parola NON ESISTE");
                                        requestSay = wrongAnswer()
                                                .andThenCompose(response -> robotHelper.say(requestTextForPepper));
                                        answerDialogFragment.show();
                                    }
                                });
                            } else {
                                Log.e("RISPOSTA_VOCALE", "NON VALIDA ");
                                risultatiManager.setParoleList( new RisultatiManager.Parole(risposta, RisultatiManager.Risposta.NONVALIDA));
                                AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.WRONG, "Attenzione! Parola non valida");
                                answerDialogFragment.show();
                                requestSay = wrongAnswer()
                                        .andThenCompose(response -> robotHelper.say(requestTextForPepper));
                            }
                        }
                    }
                    else {

                        if (domanda.chekResponse(risposta)) {
                            AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(),
                                    AnswerDialogFragment.typeDialog.CORRECT);
                            answerDialogFragment.show();
                            answerDialogFragment.setOnDismissListener(dialog -> {
                                if (lisaDomande.size() - 1 == positiongame) {
                                    risultatiManager.stopDomanda();

                                    risultatiManager.fineGioco();
                                    getActivity().getSupportFragmentManager().beginTransaction().remove(GameFragment.this)
                                            .add(containerFragment.getId(), new FinishGameFragment(game, risultatiManager))
                                            .commit();
                                } else {
                                    if (fragment != null) {
                                        getActivity().getSupportFragmentManager().beginTransaction().remove(fragment)
                                                .commit();
                                    }
                                    risultatiManager.stopDomanda();
                                    positiongame++;
                                    iniziaGioco();
                                }
                            });
                        } else {
                            AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(),
                                    AnswerDialogFragment.typeDialog.WRONG);
                            answerDialogFragment.show();
                            risultatiManager.setError();
                            requestSay = robotHelper.say(requestTextForPepper);
                        }
                    }
                }

                private boolean controllaInBaseAlGioco(String risposta, String testoParola) {
                    String parola = testoParola.replace(" ","");
                    if(game instanceof Persona.CombinazioniLettere)
                        //return risposta.indexOf(testoParola) != -1;
                        return risposta.contains(parola);
                    if(game instanceof Persona.FluenzeFonologiche||game instanceof Persona.FinaliParole)
                        return risposta.toUpperCase().startsWith(testoParola.toUpperCase());
                    else
                        return true;
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                }
            });

            }
                //Fine processo parola gioco lissener

        if (domanda.getListaRispose().size() < 1) {
             requestText = domanda.getTestoDomanda();
            //testoDomanda.setText(domanda.getTestoDomanda());

        } else {
            requestText = "Domanda " + (positiongame + 1) + " :\n" + domanda.getTestoDomanda();

            //testoDomanda.setText("Domanda " + (positiongame + 1) + " :\n" + domanda.getTestoDomanda());
        }

        //TODO:RIMETTERE SE SI VUOLE RIPETERE LA PAROLA DELLA DOMANDA
      //  requestText = requestText + " " + domanda.getTestoParola();
        testoDomanda.setText(requestText);

        requestSay = robotHelper.say(requestTextForPepper);

        if (!(domanda.getTestoParola().trim().equalsIgnoreCase("")) || domanda.getTestoParola() != null) {
            testoparola.setText(domanda.getTestoParola());
            testoparola.setVisibility(View.VISIBLE);
           // VoiceManager.getIstance(getContext()).play(testoDomanda.getText().toString(), VoiceManager.QUEUE_ADD);
           // VoiceManager.getIstance(getContext()).play(testoparola.getText().toString(), VoiceManager.QUEUE_ADD);
        } else {
            testoparola.setVisibility(View.GONE);
        }

        if (domanda.getPhatMedia() == null) {
            listaMedia.setVisibility(View.GONE);
        } else {
            fragment = new MediaManagerFragment(domanda.getPhatMedia().get(0), domanda.getTypeMedia());
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.linearimage, fragment).commit();
        }

        testoparola.setText(domanda.getTestoParola());

        recyclerAnswersArrayList = new ArrayList<>();
        if (//domanda.getTypeMedia() != Persona.Game.Domanda.typeMedia.AUDIO
               // &&
                !(game instanceof Persona.CombinazioniLettere)
                && !(game instanceof Persona.FluenzeFonologiche)
                && !(game instanceof Persona.FluenzeSemantiche)
                && !(game instanceof Persona.FluenzeVerbali)
                && !(game instanceof Persona.FinaliParole)){
            //VoiceManager.getIstance(getContext()).play("Le varie risposte sono: ", VoiceManager.QUEUE_ADD);
            String risposte = Util.toString(domanda.getListaRispose());

            //Quando pepper termina di dire la domanda, gli faccio dire le risposte disponibili
            if (!(game instanceof Persona.FluenzeVerbali || game instanceof Persona.FluenzeSemantiche || game instanceof Persona.FluenzeFonologiche)) {
                requestSay.andThenConsume(Qi.onUiThread((Consumer<Void>) ignore -> {
                    requestSay = robotHelper.say(getContext().getString(R.string.answers_introduction) + "   " + risposte).andThenConsume(action -> {
                        if(game instanceof  Persona.Musica){ // Se sta runnando il gioco musicale dopo che spiega le risposte disponibili faccio partire il player
                            fragment.start();
                        }
                    });
                }));
            }
        }

        for (String risposte : domanda.getListaRispose()) {
            recyclerAnswersArrayList.add(new RecyclerAnswers(risposte));
            //if (domanda.getTypeMedia() != Persona.Game.Domanda.typeMedia.AUDIO) {
                //VoiceManager.getIstance(getContext()).play(risposte, VoiceManager.QUEUE_ADD);
            //}
        }

        RecyclerViewAnswersAdapter adapter = new RecyclerViewAnswersAdapter(recyclerAnswersArrayList, getContext());
        adapter.setMode(Persona.Game.TypeInputGame.SELEZIONE);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        adapter.setListener((v, item, position) -> {
            requestSay.cancel(true);
            if (domanda.chekResponse(item)) {
                AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.CORRECT);
                answerDialogFragment.show();

                requestSay = correctAnswer();

                positioslistclic.clear();
                answerDialogFragment.setOnDismissListener(dialog -> {
                    if (lisaDomande.size() - 1 == positiongame) {
                        risultatiManager.stopDomanda();
                        risultatiManager.fineGioco();
                        getActivity().getSupportFragmentManager().beginTransaction().remove(GameFragment.this)
                                .commit();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(containerFragment.getId(), new FinishGameFragment(game, risultatiManager))
                                .commit();
                    } else {
                        if (fragment != null) {
                            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        }
                        risultatiManager.stopDomanda();
                        positiongame++;
                        iniziaGioco();
                    }
                });
            } else {
                requestSay = wrongAnswer()
                        .andThenCompose(response -> robotHelper.say(requestTextForPepper));
                AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.WRONG);

                answerDialogFragment.show();
                risultatiManager.setError();
                answerDialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Button button = (Button) v;
                        v.setBackgroundResource(0);
                        v.setBackgroundResource(R.drawable.stylebuttonerrorresponce);
                    }
                });
            }
        });

        positioslistclic.add(0);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private boolean controllaInBaseAlGioco(String risposta, String testoParola) {
        if(game instanceof Persona.CombinazioniLettere)
            return risposta.indexOf(testoParola) != -1;
        if(game instanceof Persona.FluenzeFonologiche||game instanceof Persona.FinaliParole)
            return risposta.toUpperCase().startsWith(testoParola.toUpperCase());
        else
            return true;
    }


 // public void processaRisposta(String responce, Persona.Game.Domanda domanda,int i) {
  public void processaRisposta(String responce) {
        String risposta = responce.replace(" ","");

        Log.e("RISPOSTA_VOCALE", "risposta " + risposta);

        if (game instanceof Persona.CombinazioniLettere ||
                game instanceof Persona.FluenzeFonologiche ||
                game instanceof Persona.FluenzeVerbali ||
                game instanceof Persona.FluenzeSemantiche ||
                game instanceof Persona.FinaliParole){

            if (!(game instanceof  Persona.FinaliParole) &&!(game instanceof  Persona.FluenzeVerbali)&& Util.isParolaPhraseToStopGame(risposta)) {
                risultatiManager.stopDomanda();
                risultatiManager.fineGioco();
                getActivity().getSupportFragmentManager().beginTransaction().remove(GameFragment.this)
                        .add(containerFragment.getId(), new FinishGameFragment(game, risultatiManager))
                        .commit();
            }
            else {
                if( game instanceof Persona.FluenzeVerbali || game instanceof Persona.FluenzeSemantiche ){
                    String categoria = "";

                    domanda.checkDomandaSimiliarita(getContext(),domanda.getTestoParola() , risposta, new Util.SimilaritaParoleListener() {
                        @Override
                        public void valida(float value) {
                            AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.CORRECT,"Ottimo lavoro. Vai avanti così ");

                            requestSay = correctAnswer();

                            answerDialogFragment.show();
                            answerDialogFragment.setOnDismissListener(dialog -> {
                                if(game instanceof Persona.FluenzeVerbali){
                                    if  (lisaDomande.size() - 1 == positiongame) {
                                        risultatiManager.stopDomanda();

                                        risultatiManager.fineGioco();
                                        getActivity().getSupportFragmentManager().beginTransaction().remove(GameFragment.this)
                                                .add(containerFragment.getId(), new FinishGameFragment(game, risultatiManager))
                                                .commit();
                                    } else {
                                        if (fragment != null) {
                                            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                                        }
                                        risultatiManager.stopDomanda();

                                        positiongame++;
                                        iniziaGioco();
                                    }
                                }
                            });
                            risultatiManager.setParoleList(new RisultatiManager.Parole(risposta, RisultatiManager.Risposta.CORRELATA));
                        }

                        //Parole non valide giochi vocali
                        @Override
                        public void nonvalida(float value) {
                            Log.e("RISPOSTA_VOCALE", "NON ESISTE ");
                            String testoDialog="";
                            if(game instanceof Persona.FluenzeVerbali){
                                testoDialog="Pensaci meglio. E dimmi un'altra parola  ";
                                requestSay = wrongAnswer()
                                        .andThenCompose(response -> robotHelper.say(requestTextForPepper));
                            }
                            if(game instanceof Persona.FluenzeSemantiche){
                                testoDialog="Non é molto attinente, pensaci meglio ";
                                requestSay = wrongAnswer()
                                        .andThenCompose(response -> robotHelper.say(requestTextForPepper));
                            }

                            risultatiManager.setParoleList(new RisultatiManager.Parole(risposta, RisultatiManager.Risposta.NONCORRELATA));
                            AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.WRONG, testoDialog);
                            requestSay = wrongAnswer()
                                    .andThenCompose(response -> robotHelper.say(requestTextForPepper));

                            answerDialogFragment.show();
                        }

                        @Override
                        public void errore() {
                            Toast.makeText(getContext(),"C'é qualcosa che non va, prova ad accendere internet o ad riavviare il gioco ",Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else if (controllaInBaseAlGioco(risposta, domanda.getTestoParola().toLowerCase())) {
                    domanda.checkDomandaOnline(getContext(), risposta, new Util.RicercaParoleListener() {
                        @Override
                        public void esiste() {
                            if(game instanceof Persona.FinaliParole||game instanceof Persona.FluenzeVerbali){
                                AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.CORRECT);

                                requestSay = correctAnswer();

                                answerDialogFragment.show();
                                answerDialogFragment.setOnDismissListener(dialog -> {
                                    if (lisaDomande.size() - 1 == positiongame) {
                                        risultatiManager.stopDomanda();

                                        risultatiManager.fineGioco();
                                        getActivity().getSupportFragmentManager().beginTransaction().remove(GameFragment.this)
                                                .add(containerFragment.getId(), new FinishGameFragment(game, risultatiManager))
                                                .commit();
                                    } else {
                                        if (fragment != null) {
                                            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                                        }
                                        risultatiManager.stopDomanda();

                                        positiongame++;
                                        iniziaGioco();
                                    }
                                });
                                risultatiManager.setParoleList(new RisultatiManager.Parole(risposta, RisultatiManager.Risposta.ESISTE));
                            }else {
                                Log.e("RISPOSTA_VOCALE", "ESISTE");
                                risultatiManager.setParoleList(new RisultatiManager.Parole(risposta, RisultatiManager.Risposta.ESISTE));
                                requestSay = correctAnswer();
                                AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.CORRECT, "La parola ESISTE");
                                answerDialogFragment.show();
                            }
                        }

                        @Override
                        public void nonesiste() {
                            Log.e("RISPOSTA_VOCALE", "NON ESISTE ");
                            risultatiManager.setParoleList(new RisultatiManager.Parole(risposta, RisultatiManager.Risposta.NONESISTE));
                            AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.WRONG, "La parola NON ESISTE");
                            answerDialogFragment.show();
                            requestSay = robotHelper.say("La parola NON ESISTE")
                                    .andThenCompose(response -> robotHelper.say(requestTextForPepper));
                        }
                    });
                } else {
                    Log.e("RISPOSTA_VOCALE", "NON VALIDA ");
                    risultatiManager.setParoleList( new RisultatiManager.Parole(risposta, RisultatiManager.Risposta.NONVALIDA));
                    AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(), AnswerDialogFragment.typeDialog.WRONG, "Attenzione! Parola non valida");
                    answerDialogFragment.show();
                    requestSay = robotHelper.say("Attenzione! Parola non valida")
                            .andThenCompose(response -> robotHelper.say(requestTextForPepper));
                }
            }
        }
        else {

            if (domanda.chekResponse(risposta)) {
                AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(),
                        AnswerDialogFragment.typeDialog.CORRECT);
                answerDialogFragment.show();
                answerDialogFragment.setOnDismissListener(dialog -> {
                    if (lisaDomande.size() - 1 == positiongame) {
                        risultatiManager.stopDomanda();

                        risultatiManager.fineGioco();
                        getActivity().getSupportFragmentManager().beginTransaction().remove(GameFragment.this)
                                .add(containerFragment.getId(), new FinishGameFragment(game, risultatiManager))
                                .commit();
                    } else {
                        if (fragment != null) {
                            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment)
                                    .commit();
                        }
                        risultatiManager.stopDomanda();
                        positiongame++;
                        iniziaGioco();
                    }
                });
            } else {
                AnswerDialogFragment answerDialogFragment = new AnswerDialogFragment(getActivity(),
                        AnswerDialogFragment.typeDialog.WRONG);
                answerDialogFragment.show();
                risultatiManager.setError();
            }
        }
    }

    private Future<Void> correctAnswer(){
        int[] animationsArray = {
                R.raw.correct_answer_animation2,
                R.raw.correct_answer_animation
        };

        int randomNumberForAnimation = new Random().nextInt(animationsArray.length);
        int randomNumberForText = new Random().nextInt(Phrases.phrasePepperCorrectAnwswer.length);

        return robotHelper.sayAndMove(animationsArray[randomNumberForAnimation],Phrases.phrasePepperCorrectAnwswer[randomNumberForText]);
    }

    private Future<Void> wrongAnswer(){

        int randomNumberForText = new Random().nextInt(Phrases.phrasePepperWrongAnwswer.length);

        return robotHelper.say(Phrases.phrasePepperWrongAnwswer[randomNumberForText]);
    }
}