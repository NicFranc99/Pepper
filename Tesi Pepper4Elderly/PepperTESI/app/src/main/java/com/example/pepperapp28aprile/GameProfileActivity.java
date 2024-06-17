package com.example.pepperapp28aprile;

import android.content.Context;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.widget.TitleView;
import androidx.viewpager.widget.ViewPager;

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
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.QiChatExecutor;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.aldebaran.qi.sdk.object.locale.Language;
import com.aldebaran.qi.sdk.object.locale.Locale;
import com.aldebaran.qi.sdk.object.locale.Region;
import com.example.pepperapp28aprile.CheckMattutino;
import com.example.pepperapp28aprile.Globals;
import com.example.pepperapp28aprile.MainActivity;
import com.example.pepperapp28aprile.PeopleListAdapter;
import com.example.pepperapp28aprile.Persona;
import com.example.pepperapp28aprile.R;
import com.example.pepperapp28aprile.SectionsPagerAdapter;
import com.example.pepperapp28aprile.WebActivity;
import com.example.pepperapp28aprile.animations.Animations;
import com.example.pepperapp28aprile.interfacedir.onCLickListener;
import com.example.pepperapp28aprile.map.RobotHelper;
import com.example.pepperapp28aprile.models.Categoria;
import com.example.pepperapp28aprile.models.Emergency;
import com.example.pepperapp28aprile.utilities.DataManager;
import com.example.pepperapp28aprile.utilities.Phrases;
import com.example.pepperapp28aprile.utilities.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieStore;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.pepperapp28aprile.Globals.NowIsRunning;

/**
 * Activity che gestice la schermata di visualizzazione dei giochi
 */
public class GameProfileActivity extends RobotActivity implements RobotLifecycleCallbacks {
    public static String name;
    public static String idPaziente;
    public static ArrayList<Persona.Game> gameArrayList;
    public static String sesso;
    public static boolean tornaNav;
    public static QiContext qiContext;
    private RobotHelper robotHelper;
    private static FragmentManager fragmentManager;
    public Future<Void> requestSay;
    public Future<String> listenFuture;
    private static final String TAG = "MSI_MainMenuFragment";
    private boolean createSelectedFragment = true;
    private Intent intentFromGameActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QiSDK.register(this, this);

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        this.fragmentManager = getSupportFragmentManager();
        this.robotHelper = new RobotHelper();
        System.out.println("GameProfile activity");

        setContentView(R.layout.fragment_main_persone);
        TextView tv1 = (TextView) findViewById(R.id.txtname);

        tv1.setText(Globals.Greeting + " " + name);

        TitleView sectionInfoElder = (TitleView) findViewById(R.id.imgsenior);
        try{
            setElderlyImageByGender(sectionInfoElder);
        }catch(Exception ex){

        }

        PeopleListAdapter.tornaNav = tornaNav;
    }

    /**
     * Tale metodo prende un TitleView e gli imposta il drawable con icona da donna se il gender del paziente considerato sia "F" altrimenti gli assegna un drawable
     * che corrisponde all'icona di un signore.
     *
     * @param sectionInfoElder TitleView di cui impostare l'icona drawable inbase al gender del paziente considerato.
     */
    private void setElderlyImageByGender(TitleView sectionInfoElder) {
        if (sesso.equals("M"))
            sectionInfoElder.setBackground(ContextCompat.getDrawable(this, R.drawable.grandfather));
        else
            sectionInfoElder.setBackground(ContextCompat.getDrawable(this, R.drawable.grandmother));
    }

public QiContext getQiContext(){
        return qiContext;
}
    public void setPlaceHolderFragment(Fragment fragment, String fragmentTag) {
            FragmentManager fm = getSupportFragmentManager();
            fragment = fm.findFragmentByTag(fragmentTag);
            if (fragment == null) {
                FragmentTransaction ft = fm.beginTransaction();
                fragment = PlaceholderFragmentGames.newInstance(idPaziente);


                ft.add(android.R.id.content, fragment, fragmentTag);
                ft.commit();
            }
    }

    public void setSelectedGameModeFragment(Fragment fragment, String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
          fragment = SelectionGameModeFragment.newInstance(idPaziente);

            ft.add(android.R.id.content, fragment, fragmentTag);
            ft.commit();
        }
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Globals.NowIsRunning = Globals.GameProfile;
        this.qiContext = qiContext;
        this.robotHelper.onRobotFocusGained(qiContext);

        intentFromGameActivity = getIntent();
        createSelectedFragment = intentFromGameActivity.getBooleanExtra("load_fragment", true);
        String idPaziente = intentFromGameActivity.getStringExtra("idPaziente");

        if(createSelectedFragment)
            setSelectedGameModeFragment(SelectionGameModeFragment.newInstance(idPaziente), SelectionGameModeFragment.FRAGMENT_TAG);
        else
            setPlaceHolderFragment(PlaceholderFragmentGames.newInstance(idPaziente),PlaceholderFragmentGames.FRAGMENT_TAG);



        // Create a new say action.

        //     Say ciaoSonoPepper = SayBuilder.with(qiContext) // Create the builder with the context
        //.withText("Hey "+ name + ", vuoi giocare con me? Clicca sulla foto del gioco per cominciare!, oppure dimmelo a voce!") // Set the text to say.
        //               .withText("Ciao " + name + " " + Phrases.selectGameMode)
        //               .build(); // Build the say action.
        //    ciaoSonoPepper.run();


        //   ArrayList<String> titleGames = getGameTitleList();

        //  List<Phrase> phraseList = getPhraseSetListByStringList(titleGames);

        //  ListenResult listenResult  = setTitleGameToLissen(phraseList);

        //  String pepperString = listenResult.getHeardPhrase().getText();

        // robotHelper.say(getString(R.string.start_game) + pepperString);
        // viewGameListByVoice(null,pepperString);

        //  Animation animazioneSaluto = AnimationBuilder.with(qiContext)
        //           .withResources(R.raw.salute_left_b001)
        //           .build();

        // Create the second action.
        //  Animate animate = AnimateBuilder.with(qiContext)
        //          .withAnimation(animazioneSaluto)
        //          .build();

        // Run the second action asynchronously.
        //   animate.async().run();
    }

    //TODO: Vedere qua per passare valori a una activity
    public void startWeb(View view, int position, Persona paziente) {
        WebActivity.tornaNav = tornaNav;
        Intent intent = new Intent(this, GameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("item", position);
        intent.putExtra("paziente", paziente);
        this.startActivity(intent);
        finish();
    }

    public void viewGameListByVoice(View view, String pepperString) {
        getPazienteById(this.idPaziente, pepperString);
    }

    public RobotHelper getRobotHelper() {
        return robotHelper;
    }


    private void getPazienteById(String idPaziente, String pepperString) {
        new DataManager(this, "pazienti", idPaziente, new DataManager.onDownloadDataListener() {
            @Override
            public void onDataSuccess(Persona paziente) {
                int count = paziente.getEsercizi().size();
                if (!paziente.getEsercizi().isEmpty())
                    for (int i = 0; i < paziente.getEsercizi().size(); i++) {
                        if (paziente.getEsercizi().get(i).getTitleGame().equalsIgnoreCase(pepperString)) {
                            startWeb(null, i, paziente);
                            return;
                        }
                    }
                else return;
            }

            @Override
            public void onDataFailed() {
            }

            @Override
            public void notFoundUser() {

            }
        });
    }

    /*public List<Persona> getPeopleList() {
        List<Persona> peopleList = new ArrayList<>();
        String sURL = "https://bettercallpepper.altervista.org/api/getParents.php?appid="+ Globals.myAppID;
        // Connect to the URL using java's native library
        URL url = null;
        try {
            url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();
            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonArray rootarr = root.getAsJsonArray(); //May be an array, may be an object.
            for (int i = 0; i < rootarr.size(); i++) {
                JsonObject rootelem = rootarr.get(i).getAsJsonObject();
                String img = rootelem.get("propic").getAsString();
                String name = rootelem.get("name").getAsString();
                String surname = rootelem.get("surname").getAsString();
                int id = rootelem.get("id").getAsInt();
                peopleList.add(new Persona(img, id, name, surname));
            }
        } catch (Exception e) {
            System.out.println("Erroreeeee");
            e.printStackTrace();
        }
        return peopleList;
    }*/

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Prende la lista dei giochi assegnati al paziente corrente (Passata dakka MyListAdapter) e restituisce
     * una lista di tutti i titoli dei giochi
     *
     * @return List dei titoli dei giochi assegnati al paziente
     */
    public ArrayList<String> getGameTitleList() {
        ArrayList<String> result = new ArrayList<>();
        for (Persona.Game game : gameArrayList) {
            result.add(game.getTitleGame());
        }
        return result;
    }

    private List<Phrase> getPhraseSetListByStringList(ArrayList<String> gameTitleList) {
        List<Phrase> phraseList = new ArrayList<Phrase>();
        for (String title : gameTitleList) {
            phraseList.add(new Phrase(title));
        }

        return phraseList;
    }

    /**
     * Prende una lista di frasi che pepper deve rimanere in ascolto, le imposta,
     * e restituisce un oggetto dell'Sdk che permette di recuperare il testo ascoltato da pepper.
     *
     * @param phraseList
     * @return Restituisce un oggetto che permette di recuperare cio' che pepper ha ascoltato durante l'interazione
     */
    private ListenResult setTitleGameToLissen(List<Phrase> phraseList) {
        PhraseSet phraseSet = PhraseSetBuilder.with(qiContext)
                .withPhrases(phraseList)
                .build();

        return ListenBuilder.with(qiContext)
                .withPhraseSet(phraseSet)
                .build()
                .run();
    }

   /* private void setTItleUi() {
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
        int position = getIntent().getExtras().getInt("item");
        Persona paziente = (Persona)getIntent().getSerializableExtra("paziente");

        Persona.Game g = paziente.getEsercizi().get(position);

        iconCategori.setBackground(getDrawable(g.getNameIcon()));
        txtCat.setText(g.getTitleCategory());
        txtTitleGame.setText(g.getTitleGame());
    }*/

    /*private void esci() {
        ExitDialogFragment exit = new ExitDialogFragment(GameProfileActivity.this);
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
    }*/
}
