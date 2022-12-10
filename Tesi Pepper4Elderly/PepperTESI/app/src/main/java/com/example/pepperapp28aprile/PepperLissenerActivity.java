package com.example.pepperapp28aprile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
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
import com.aldebaran.qi.sdk.object.conversation.BodyLanguageOption;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.Chatbot;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.QiChatExecutor;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.aldebaran.qi.sdk.object.locale.Language;
import com.aldebaran.qi.sdk.object.locale.Locale;
import com.aldebaran.qi.sdk.object.locale.Region;
import com.example.pepperapp28aprile.QiExecutor.MyQiChatExcecutorGame;
import com.example.pepperapp28aprile.animations.Animations;
import com.example.pepperapp28aprile.interfacedir.onCLickListener;
import com.example.pepperapp28aprile.map.RobotHelper;
import com.example.pepperapp28aprile.map.SaveFileHelper;
import com.example.pepperapp28aprile.models.Categoria;
import com.example.pepperapp28aprile.utilities.RisultatiManager;
import com.example.pepperapp28aprile.utilities.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PepperLissenerActivity extends RobotActivity implements RobotLifecycleCallbacks { //TODO: Rimuovere questo activity perche' inutilizzato

    public static final String CONSOLE_TAG = "Pepper4RSA";
    private static final int PERMISSION_STORAGE = 1;
    public static MainActivity ma;
    private String currentFragment;
    private Persona.Game game;
    private int position;
    public static QiContext qiRobotContext;
    private static FragmentManager fragmentManager;
    private RisultatiManager risultatiManager;
    public static String risposta;
    private int idContainer;
    private SaveFileHelper saveFileHelper;
    private RobotHelper robotHelper;
    public static boolean isGameEnd = false;

    private void init() {
        QiSDK.register(this, this);

        this.saveFileHelper = new SaveFileHelper();
        this.robotHelper = new RobotHelper();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_STORAGE) {
            this.init();
            System.out.println("init?");
        }
        else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QiSDK.register(this, this);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        risultatiManager = new RisultatiManager();
         setContentView(R.layout.activity_game);
        this.fragmentManager = getSupportFragmentManager();
        if(isGameEnd){
            isGameEnd = false;
        }
        setGameFragmentVariable();
        setTItleUi();

        if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            this.init();
            System.out.println("on create init?");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /*private void startTopic(Integer topicResource, QiChatbot.OnEndedListener chatEndedListener){
        try{

            System.out.println("sono nel topic");

            final Topic topic = TopicBuilder.with(qiRobotContext)
                    .withResource(topicResource).build();

            // Create a qiChatbot
            QiChatbot qiChatbot = QiChatbotBuilder.with(qiRobotContext).withTopic(topic).build();

            Map<String, QiChatExecutor> executors = new HashMap<>();

            // Map the executor name from the topic to our qiChatExecutor
            executors.put("myExecutor", new MyQiChatExecutorChiamaVocal(qiRobotContext));

            // Set the executors to the qiChatbot
            qiChatbot.setExecutors(executors);
            List<Chatbot> chatbots = new ArrayList<>();
            chatbots.add(qiChatbot);

            // Build chat with the chatbotBuilder
            Chat chat = ChatBuilder.with(qiRobotContext).withChatbot(qiChatbot).build();

            // Run an action asynchronously.
//chat.async().run();

            Future<Void> chatFuture = chat.async().run();

            qiChatbot.addOnEndedListener(endReason -> {
                chatFuture.requestCancellation();
            });
            qiChatbot.addOnEndedListener(chatEndedListener);

            chatFuture.thenConsume(value -> {
                if (value.hasError()) {
                    System.out.println("Discussion finished with error.");
                }
            });

        }catch(Exception e){ System.out.println("eccezione topic");
            e.printStackTrace();
        }

    }*/

    @Override
    public void onBackPressed() {
        esci();
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

    private void esci() {
        ExitDialogFragment exit = new ExitDialogFragment(PepperLissenerActivity.this);
        exit.setText(getResources().getString(R.string.msg_exit_session));
        exit.setIcon(getDrawable(R.drawable.answer));
        exit.setListener(new onCLickListener() {
            @Override
            public void onClickExit() {
                exit.dismiss();
                finish();
            }

            @Override
            public void onClickContinue() {
                exit.dismiss();
            }
        });
        exit.show();
    }

    /**
     * Metodo utilizzato per settare le variabili game,positionGame e idContainer che servono
     * per istanziare il GameFragment (Vedere nel onRobotFocusGanied). Tali parametri vengono passati
     * dal GameExplanationFragment con intent.putExtra()..
     */
    private void setGameFragmentVariable(){
        idContainer = Util.getIntegerByIntent(getIntent(),"idContainer");
        position = Util.getIntegerByIntent(getIntent(),"gamePosition");
        game = (Persona.Game)Util.getObjectByItentKey(getIntent(),"game");
    }

    public static QiContext getQiContext() {
        return qiRobotContext;
    }

    public RobotHelper getRobotHelper() {
        return robotHelper;
    }
    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Globals.NowIsRunning = Globals.GameProfile;
        this.qiRobotContext = qiContext;
        this.robotHelper.onRobotFocusGained(qiContext);

        this.getSupportFragmentManager().beginTransaction()
                .add(idContainer, new GameFragment(game,position)).commit();

    while (true) {
        if(isGameEnd)
            break;
            startTopic(R.raw.finaliparole, endReason -> {
                analizzaRisposta(risposta);
            });
    }
    }

    private void analizzaRisposta(String risposta){
            GameFragment fragment = (GameFragment) getSupportFragmentManager().getFragments().get(0);
            fragment.processaRisposta(risposta);
    }

    private void startTopic(Integer topicResource, QiChatbot.OnEndedListener chatEndedListener){
        try{
            System.out.println("sono nel topic");

            final Topic topic = TopicBuilder.with(qiRobotContext)
                    .withResource(topicResource).build();

            // Create a qiChatbot
            QiChatbot qiChatbot = QiChatbotBuilder.with(qiRobotContext).withTopic(topic).build();

            Map<String, QiChatExecutor> executors = new HashMap<>();

            // Map the executor name from the topic to our qiChatExecutor
            executors.put("myExecutor", new MyQiChatExcecutorGame(qiRobotContext));

            // Set the executors to the qiChatbot
            qiChatbot.setExecutors(executors);
            List<Chatbot> chatbots = new ArrayList<>();
            chatbots.add(qiChatbot);

            // Build chat with the chatbotBuilder
            Chat chat = ChatBuilder.with(qiRobotContext).withChatbot(qiChatbot).build();

            // Run an action asynchronously.
//chat.async().run();

            Future<Void> chatFuture = chat.async().run();

            qiChatbot.addOnEndedListener(endReason -> {
                chatFuture.requestCancellation();
            });
            qiChatbot.addOnEndedListener(chatEndedListener);

            chatFuture.thenConsume(value -> {
                if (value.hasError()) {
                    System.out.println("Discussion finished with error.");
                }
            });

        }catch(Exception e){ System.out.println("eccezione topic");
            e.printStackTrace();
        }

    }
    @Override
    public void onRobotFocusLost() {

    }


    @Override
    public void onRobotFocusRefused(String reason) {
        System.out.println("REASON: " + reason);
    }

    @Override
    public void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }
}