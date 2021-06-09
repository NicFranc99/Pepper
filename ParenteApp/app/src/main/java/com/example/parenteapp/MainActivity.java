package com.example.parenteapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;
import static com.example.parenteapp.Globals.myAppID;

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

        //Globals.myAppID = 100;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "1");

        Intent accept = new Intent(this, AcceptActivity.class);
        PendingIntent acceptIntent = PendingIntent.getActivity(this,
                1, accept, 0);

        Intent reject = new Intent(this, RejectActivity.class);
        PendingIntent rejectintent = PendingIntent.getActivity(this,
                1, reject, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = getString(R.string.app_name);
            String description = getString(R.string.CHANNEL_DESCRIPTION);
            int importance = NotificationManager.IMPORTANCE_HIGH; //Important for heads-up notification
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_icon_large)
                .setContentTitle("Chiamata in arrivo")
                .setContentText("Stai ricevendo una chiamata da Tizio")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(acceptIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Accetta", acceptIntent)
                .addAction(R.mipmap.ic_launcher, "Rifiuta", rejectintent)
                .build();

        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run () {
                String sURL = "https://bettercallpepper.altervista.org/api/getParentCall.php?parid=" + myAppID;
                String name = "";
                String surname = "";
                int recv = 0;
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
                        Globals.receiveCallID = rootelem.get("id").getAsInt();
                    }
                } catch (Exception e) {
                }
                if(name != "") {
                    notificationBuilder.setContentText("Stai ricevendo una chiamata da " + name + " " + surname);
                    notificationManager.notify(001, notificationBuilder.build());
                    System.out.println("Clock");
                }
            }
        }, 1000, 5000);
    }

    public static void deleteNotification()
    {
        notificationManager.cancelAll();
    }
}
