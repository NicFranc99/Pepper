package com.example.pepperapp28aprile;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
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
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.Chatbot;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.QiChatExecutor;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.example.pepperapp28aprile.CheckMattutino;
import com.example.pepperapp28aprile.Globals;
import com.example.pepperapp28aprile.MainActivity;
import com.example.pepperapp28aprile.PeopleListAdapter;
import com.example.pepperapp28aprile.Persona;
import com.example.pepperapp28aprile.R;
import com.example.pepperapp28aprile.SectionsPagerAdapter;
import com.example.pepperapp28aprile.WebActivity;
import com.example.pepperapp28aprile.models.Categoria;
import com.example.pepperapp28aprile.models.Emergency;
import com.example.pepperapp28aprile.utilities.DataManager;
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
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.pepperapp28aprile.Globals.NowIsRunning;

public class GameProfileActivity extends RobotActivity implements RobotLifecycleCallbacks {
    public static String name;
    public static String idPaziente;
    public static String sesso;
    public static boolean tornaNav;
    private QiContext qiContext;
    public static String viewGameList; //Nome del gioco detto a pepper dall'utente
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QiSDK.register(this, this);

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        this.fragmentManager = getSupportFragmentManager();
        System.out.println("GameProfile activity");

        setContentView(R.layout.fragment_main_persone);
        TextView tv1 = (TextView)findViewById(R.id.txtname);

        tv1.setText(Globals.Greeting + " " + name);

        TitleView sectionInfoElder = (TitleView)findViewById(R.id.imgsenior);
        setElderlyImageByGender(sectionInfoElder);
        PeopleListAdapter.tornaNav = tornaNav;

        setFragment(PlaceholderFragmentGames.newInstance(idPaziente),PlaceholderFragmentGames.FRAGMENT_TAG);

    }

    /**
     * Tale metodo prende un TitleView e gli imposta il drawable con icona da donna se il gender del paziente considerato sia "F" altrimenti gli assegna un drawable
     * che corrisponde all'icona di un signore.
     * @param sectionInfoElder TitleView di cui impostare l'icona drawable inbase al gender del paziente considerato.
     */
    private void setElderlyImageByGender(TitleView sectionInfoElder){
        if(sesso.equals("M"))
            sectionInfoElder.setBackground(ContextCompat.getDrawable(this, R.drawable.grandfather));
        else
            sectionInfoElder.setBackground(ContextCompat.getDrawable(this, R.drawable.grandmother));
    }

    public void setFragment(Fragment fragment,String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            fragment = PlaceholderFragmentGames.newInstance(idPaziente);
            ft.add(android.R.id.content,fragment,fragmentTag);
            ft.commit();
        }
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Globals.NowIsRunning = Globals.GameProfile;
        this.qiContext = qiContext;
        // Create a new say action.

            Say ciaoSonoPepper = SayBuilder.with(qiContext) // Create the builder with the context
                    .withText("Hey "+ name + ", vuoi giocare con me? Clicca sulla foto del gioco per cominciare!, oppure dimmelo a voce!") // Set the text to say.
                    .build(); // Build the say action.
        ciaoSonoPepper.run();

        //Carico il file per ascoltare ciò che dice il paziente con pepper.
        startTopic(R.raw.gameslistener, endReason -> {
            viewGameListByVoice(null, viewGameList);
        });

        Animation animazioneSaluto = AnimationBuilder.with(qiContext)
                .withResources(R.raw.salute_left_b001)
                .build();

        // Create the second action.
        Animate animate = AnimateBuilder.with(qiContext)
                .withAnimation(animazioneSaluto)
                .build();

        // Run the second action asynchronously.
        animate.async().run();
    }

    private void startTopic(Integer topicResource, QiChatbot.OnEndedListener chatEndedListener){
        try{

            System.out.println("sono nel topic");

            final Topic topic = TopicBuilder.with(qiContext)
                    .withResource(topicResource).build();

            // Create a qiChatbot
            QiChatbot qiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).build();

            Map<String, QiChatExecutor> executors = new HashMap<>();

            // Map the executor name from the topic to our qiChatExecutor
            executors.put("myExecutor", new MyQiChatExecutorChiamaVocal(qiContext));

            // Set the executors to the qiChatbot
            qiChatbot.setExecutors(executors);
            List<Chatbot> chatbots = new ArrayList<>();
            chatbots.add(qiChatbot);

            // Build chat with the chatbotBuilder
            Chat chat = ChatBuilder.with(qiContext).withChatbot(qiChatbot).build();

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


    public void startWeb(View view, int position, Persona paziente) {
        WebActivity.tornaNav = tornaNav;
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("item", position);
        intent.putExtra("paziente",paziente);
        this.startActivity(intent);
        finish();
    }

    public void viewGameListByVoice(View view, String pepperString) {
        getPazienteById(this.idPaziente,pepperString);
    }


    private void getPazienteById(String idPaziente,String pepperString){
        new DataManager(this,"pazienti",idPaziente,new DataManager.onDownloadDataListener() {
            @Override
            public void onDataSuccess(Persona paziente) {
                int count = paziente.getEsercizi().size();
                if(!paziente.getEsercizi().isEmpty())
                    for(int i=0;i<paziente.getEsercizi().size();i++){
                    if(paziente.getEsercizi().get(i).getTitleGame().equalsIgnoreCase(pepperString)){
                       startWeb(null,i,paziente);
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

}