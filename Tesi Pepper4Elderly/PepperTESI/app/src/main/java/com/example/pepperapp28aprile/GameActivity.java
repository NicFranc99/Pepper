package com.example.pepperapp28aprile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiRobot;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.Chatbot;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.QiChatExecutor;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.example.pepperapp28aprile.QiExecutor.MyQiChatExcecutorGame;
import com.example.pepperapp28aprile.animations.Animations;
import com.example.pepperapp28aprile.interfacedir.onCLickListener;
import com.example.pepperapp28aprile.map.RobotHelper;
import com.example.pepperapp28aprile.map.SaveFileHelper;
import com.example.pepperapp28aprile.models.Categoria;
import com.example.pepperapp28aprile.utilities.DataManager;
import com.example.pepperapp28aprile.utilities.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GameActivity extends RobotActivity implements RobotLifecycleCallbacks{

    private RecyclerView recyclerView;
    private ArrayList<RecyclerAnswers> recyclerAnswersArrayList;
    private TextToSpeech t1;
    private final Bundle extras = new Bundle();
    private static FragmentManager fragmentManager;
    public static QiContext qiContext;
    private static final int PERMISSION_STORAGE = 1;
    public static RobotHelper robotHelper;
    private int position;
    private Persona.Game game;
    private Persona paziente;
    public static String rispostaUtente;
    private String currentFragment;
    private Bundle eliminami;
    public Persona.Game.Domanda domanda;
    private Future<Void> say;
    public  Future<Void> sayDescription;
    public boolean isMultyExecution;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QiSDK.register(this, this);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        this.fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_game);
         eliminami = savedInstanceState;
       position = Util.getIntegerByIntent(getIntent(),"item");
       paziente = (Persona) Util.getObjectByItentKey(getIntent(),"paziente");
        isMultyExecution = (boolean) Util.getObjectByItentKey(getIntent(),"isMultyExecution");
       game = paziente.getEsercizi().get(position);

        setTItleUi();

        if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            this.init();
            System.out.println("on create init?");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
        }
    }



    private void init() {
        QiSDK.register(this, this);
        this.robotHelper = new RobotHelper();
    }

    private void setTItleUi() {
        ImageView iconCategori = findViewById(R.id.img_icon_cat);
        TextView txtCat = findViewById(R.id.txt_cat);
        TextView txtTitleGame = findViewById(R.id.txt_title);

        ImageView exit=findViewById(R.id.exit2);
        Animations.focusView(exit, Animations.SCALING_BUTTON_DIALOG);
        exit.setVisibility(View.VISIBLE);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esci();
            }
        });
        iconCategori.setBackground(getDrawable(game.getNameIcon()));
        txtCat.setText(game.getTitleCategory());
        txtTitleGame.setText(game.getTitleGame());
    }

    @Override
    public void onBackPressed() {

        esci();
    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }

    //Metodo da utilizizzare fuori per creare questa activity (viene usato dalla listAdapter)
    public void startGameActivity(Persona paziente,int position, Context context, boolean isMultyExecution) {
        Persona.Game game = paziente.getEsercizi().get(position);
        Intent gameActivity = new Intent(context, GameActivity.class);
        gameActivity.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Categoria category = new Categoria(game,position);
        gameActivity.putExtra("item", category.getPosition());
        gameActivity.putExtra("paziente",paziente);
        gameActivity.putExtra("isMultyExecution",isMultyExecution);
        context.startActivity(gameActivity);
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // Create a new say action.
        this.qiContext = qiContext;
        this.robotHelper.onRobotFocusGained(qiContext);
        if (eliminami == null) { //TODO: Se non dovesse piu' funzionare portare l'instance del GameExploitationFragment nel OnCreate del GameActivity
            Fragment fragment = new GameExplanationFragment();
            fragment.setArguments(getIntent().getExtras()); // delego al fragment la ricezzione dei dati
            getSupportFragmentManager().beginTransaction().replace(R.id.container_game_com, fragment).commit();
        }
    }

    public QiContext getQiContext() {
        return qiContext;
    }

    public RobotHelper getRobotHelper() {
        return robotHelper;
    }

    private void esci() {
        ExitDialogFragment exit = new ExitDialogFragment(GameActivity.this);
        exit.setText(getResources().getString(R.string.msg_exit_session));
        exit.setIcon(getDrawable(R.drawable.answer));
        if(sayDescription != null){
            sayDescription.cancel(true);
        }
        say = robotHelper.say(getResources().getString(R.string.msg_exit_session) ).thenConsume(future -> { //Verifico se c'e' un problema con l'esecuzione del future per l'ascolto
            if (future.hasError()) {
                Log.e("Pepper4RSA", "Error while speaking or listening: ", future.getError());
            }
        });
        exit.setListener(new onCLickListener() {
            @Override
            public void onClickExit() {
                say.cancel(true);
                    Intent intent = new Intent(GameActivity.this, GameProfileActivity.class);
                    intent.putExtra("load_fragment", isMultyExecution);
                    intent.putExtra("idPaziente", String.valueOf(paziente.getId()));
                    startActivity(intent);

                exit.dismiss();
                finish();
            }

            @Override
            public void onClickContinue() {
                say.cancel(true);
                say = robotHelper.say("Continuamo a giocare!");
                exit.dismiss();
            }
        });
        exit.show();
    }
    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}