package com.example.parenteapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.parenteapp.ui.main.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.parenteapp.ui.main.SectionsPagerAdapter;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    static NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        ImageView btn = findViewById(R.id.imageButton);

        //Globals.myAppID = 100;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSettings(view);
            }
        });


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "CHANNEL_ID");

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                1, activityIntent, 0);

        Intent reject = new Intent(this, RejectActivity.class);
        PendingIntent rejectintent = PendingIntent.getActivity(this,
                1, reject, 0);

        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_icon_large)
                .setContentTitle("Chiamata in arrivo")
                .setContentText("Stai ricevendo una chiamata da Tizio")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Accetta", contentIntent)
                .addAction(R.mipmap.ic_launcher, "Rifiuta", rejectintent)
                .build();

        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run () {
                String sURL = "https://bettercallpepper.altervista.org/api/getElderCall.php?eldid=1";
                String name = "";
                String surname = "";
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
                        name = rootelem.get("name").getAsString();
                        surname = rootelem.get("surname").getAsString();
                    }
                } catch (Exception e) {
                }
                if(name != "") {
                    notificationBuilder.setContentText("Stai ricevendo una chiamata da " + name + " " + surname);
                    notificationManager.notify(1, notificationBuilder.build());
                    System.out.println("Clock");
                }
            }
        }, 1000, 5000);
    }

    public void startSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public static void deleteNotification()
    {
        notificationManager.cancelAll();
    }
}