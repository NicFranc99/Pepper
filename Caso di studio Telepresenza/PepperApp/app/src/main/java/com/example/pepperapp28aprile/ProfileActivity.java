package com.example.pepperapp28aprile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
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

import static com.example.pepperapp28aprile.MainActivity.qiContext;

public class ProfileActivity extends RobotActivity implements RobotLifecycleCallbacks {

    public static String name;

    public static QiContext qiContext;

    public static String startWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QiSDK.register(this, this);
        WebActivity.name = name;
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);

        setContentView(R.layout.fragment_main_persone);
        //setContentView(R.layout.activity_profile);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            System.out.println("una volta");
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

        this.qiContext = qiContext;
        // Create a new say action.
        Say ciaoSonoPepper = SayBuilder.with(qiContext) // Create the builder with the context.
                .withText("Ciao "+ name + ", Chi vuoi chiamare? Clicca sulla sua foto oppure dimmelo a voce!") // Set the text to say.
                .build(); // Build the say action.

        ciaoSonoPepper.run();


        Animation animazioneSaulto = AnimationBuilder.with(qiContext)
                .withResources(R.raw.salute_left_b001)
                .build();

        // Create the second action.
        Animate animate = AnimateBuilder.with(qiContext)
                .withAnimation(animazioneSaulto)
                .build();

        // Run the second action asynchronously.
        animate.async().run();

        startTopic(R.raw.file, endReason -> {
            callByVoice(null, startWeb);
        });
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
/*
    public void callByVoice(View view, String pepperString) {
        for(Persona p: peopleList)
            if(pepperString.toLowerCase().contains(p.getName()) && pepperString.toLowerCase().contains("chiama"))
                startWeb(view, p.getId());
    }*/

    public void startWeb(View view, int id) {
        Intent intent = new Intent(this, WebActivity.class);
        Bundle b = new Bundle();
        b.putInt("id", id); //Your id
        b.putInt("type", 1);
        intent.putExtras(b);
        startActivity(intent);
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
            executors.put("myExecutor", new MyQiChatExecutor(qiContext));

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
}
