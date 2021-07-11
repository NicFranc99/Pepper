package com.example.pepperapp28aprile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.Chatbot;
import com.aldebaran.qi.sdk.object.conversation.QiChatExecutor;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.example.pepperapp28aprile.models.Emergency;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.pepperapp28aprile.Globals.NowIsRunning;

public class ProfileActivity extends RobotActivity implements RobotLifecycleCallbacks {

    public static String name;
    private static boolean doButtonOperation;
    public static boolean tornaNav;
    private QiContext qiContext;
    public static String startWeb;
    //private static boolean doButtonOperationImpegnato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QiSDK.register(this, this);

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        Globals.arrivato = true;

        System.out.println("profile activity");

        doButtonOperation = false;
        //doButtonOperationImpegnato = false;
        setContentView(R.layout.fragment_main_persone);
        //setContentView(R.layout.activity_profile);
        doButtonOp();


        PeopleListAdapter.tornaNav = tornaNav;
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());


            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            System.out.println("una volta");
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        if(Globals.chiamataInArrivo == true){
            annunciaChiamata(name);
            setFragment(Globals.avviso);
            System.out.println("avviso");
        }





        //FloatingActionButton fab = findViewById(R.id.fab);
        //ImageView btn = findViewById(R.id.imageButton);

     /*  fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

      /*  btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSettings(view);
            }
        });*/

    }

   /* public void startSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/

    public void annunciaChiamata(String nomeDest){

       /* Say ciaoSonoPepper = SayBuilder.with(qiContext) // Create the builder with the context.
                .withText("Hey, è in arrivo una chiamata per " + nomeDest) // Set the text to say.
                .build(); // Build the say action.

        ciaoSonoPepper.async().run();

        Animation animazioneArrivoChiamata = AnimationBuilder.with(qiContext)
                .withResources(R.raw.show_tablet_a004)
                .build();

        // Create the second action.
        Animate animate = AnimateBuilder.with(qiContext)
                .withAnimation(animazioneArrivoChiamata)
                .build();

        // Run the second action asynchronously.
        animate.async().run();*/


    }

   public void setFragment(Fragment fragment) {

        /*if (!(fragment instanceof LoadingFragment || fragment instanceof ProductSelectionFragment || fragment instanceof SplashScreenFragment)) {
            this.currentChatData.goToBookmarkNewTopic("init", topicName);
        }
        if (this.currentChatData != null) {
            if (fragment instanceof LoadingFragment || fragment instanceof SplashScreenFragment) {
                this.currentChatData.enableListeningAnimation(false);
            } else {
                this.currentChatData.enableListeningAnimation(true);
            }
        }*/

       try{
           System.out.println("starting fragment Transaction for fragment : " + fragment.getClass().getSimpleName());
           FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
           transaction.setCustomAnimations(R.anim.enter_fade_in_right, R.anim.exit_fade_out_left, R.anim.enter_fade_in_left, R.anim.exit_fade_out_right);
           transaction.replace(R.id.profile, fragment, "currentFragment");
           transaction.addToBackStack(null);
           transaction.commit();
       }catch (Exception e){
           e.printStackTrace();
       }
   }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Globals.NowIsRunning = Globals.ProfileActivity;
        this.qiContext = qiContext;
        // Create a new say action.
        if(Globals.chiamataInArrivo){
            Say ciaoSonoPepper = SayBuilder.with(qiContext) // Create the builder with the context.
                    .withText("Hey "+ name + ",è in arrivo una chiamata per te!") // Set the text to say.
                    .build(); // Build the say action.
            ciaoSonoPepper.async().run();

        }
        else{

            Say ciaoSonoPepper = SayBuilder.with(qiContext) // Create the builder with the context
                .withText("Hey "+ name + ", Chi vuoi chiamare? Clicca sulla sua foto oppure dimmelo a voce!") // Set the text to say.
                .build(); // Build the say action.

            ciaoSonoPepper.run();
            startTopic(R.raw.file, endReason -> {
                callByVoice(null, startWeb);
            });

        }
        Animation animazioneSaulto = AnimationBuilder.with(qiContext)
                .withResources(R.raw.salute_left_b001)
                .build();

        // Create the second action.
        Animate animate = AnimateBuilder.with(qiContext)
                .withAnimation(animazioneSaulto)
                .build();

        // Run the second action asynchronously.
        animate.async().run();





        /*****+TEST**


        for(int i=0; i<100; i++){
            if(i==50 || i >80)
                System.out.println(i);
        }
        if(tornaNav){
            System.out.println("TORNA NAV IS TRUE");
            //NavigationFragment.bottone = false;
            CheckMattutino.doButtonOperation(null,true);
        }***/

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

    public void startWeb(View view, int id) {
        WebActivity.tornaNav = tornaNav;
        Intent intent = new Intent(this, WebActivity.class);
        Bundle b = new Bundle();
        b.putInt("id", id); //Your id
        b.putInt("type", 1);
        intent.putExtras(b);
        this.startActivity(intent);
        finish();
    }

    public void callByVoice(View view, String pepperString) {

        System.out.println(getPeopleList());
        for(Persona p: getPeopleList())
            if(pepperString.toLowerCase().contains(p.getName().toLowerCase()) )
                startWeb(view, p.getId());
    }

    public List<Persona> getPeopleList() {
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
    }

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

    private void doButtonOp() {
        new Thread(() -> {
            while(true){
                while(!doButtonOperation){ }
                System.out.println("PROFILE: do botton operation");
                doButtonOperation = false;
                CheckMattutino.bottone = true;
                CheckMattutino.buttonEmergency = buttonEmergency;
                buttonEmergency = null;
                Intent intent = new Intent(this, CheckMattutino.class);
                startActivity(intent);
            }
        }).start();
    }

    private static Emergency buttonEmergency;

    public static void doButtonOperation(Emergency e){
        buttonEmergency = e;
        //if(impegnato)
            doButtonOperation = true;
        //else doButtonOperationImpegnato = true;
    }


    @Override
    public void onBackPressed() {
        int close = 0;
        if(tornaNav){
            tornaNav=false;
            Intent intentMattutino = new Intent(this, CheckMattutino.class);
            NowIsRunning = Globals.CheckMattutino;
            CheckMattutino.tornaACasa = true;
            startActivity(intentMattutino);
            finish();
        }
        else
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

}