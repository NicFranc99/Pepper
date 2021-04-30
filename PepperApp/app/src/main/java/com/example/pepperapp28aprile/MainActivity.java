package com.example.pepperapp28aprile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aldebaran.qi.sdk.QiSDK;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;
import static com.example.pepperapp28aprile.Globals.myAppID;
import static com.example.pepperapp28aprile.Globals.receiveCallID;

public class MainActivity extends AppCompatActivity {

    private String currentFragment;
    private FragmentManager fragmentManager;
    static NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fragmentManager = getSupportFragmentManager();

        setFragment(new LoadingFragment());
        System.out.println("ciao");
        //setFragment(new MainMenuFragment());

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
                .setSmallIcon(R.mipmap.ic_launcher)
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
                String sURL = "https://bettercallpepper.altervista.org/api/getElderCall.php?eldid=" + myAppID;
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
                    //JsonArray rootarr = root.getAsJsonArray(); //May be an array, may be an object.
                    JsonObject rootelem = root.getAsJsonArray().get(0).getAsJsonObject();
                    name = rootelem.get("name").getAsString();
                    surname = rootelem.get("surname").getAsString();
                    receiveCallID = rootelem.get("id").getAsInt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(name != "") {
                    notificationBuilder.setContentText("Stai ricevendo una chiamata da " + name + " " + surname);
                    notificationManager.notify(1, notificationBuilder.build());
                    System.out.println("Clock");
                }
            }
        }, 1000, 5000);

    }

    public static void deleteNotification()
    {
        notificationManager.cancelAll();
    }

    @Override
    public void onUserInteraction() {
        if (getFragment() instanceof LoadingFragment) {
            setFragment(new MainMenuFragment());
            //countDownNoInteraction.start();
        } /*else {
            countDownNoInteraction.reset();
        }*/
    }

    public Fragment getFragment() {
        return fragmentManager.findFragmentByTag("currentFragment");
    }

    public void setFragment(Fragment fragment) {
        currentFragment = fragment.getClass().getSimpleName();
        String topicName = currentFragment.toLowerCase().replace("fragment", "");
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
        System.out.println("starting fragment Transaction for fragment : " + fragment.getClass().getSimpleName());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_fade_in_right, R.anim.exit_fade_out_left, R.anim.enter_fade_in_left, R.anim.exit_fade_out_right);
        transaction.replace(R.id.placeholder, fragment, "currentFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public Integer getThemeId() {
       try {
            return getPackageManager().getActivityInfo(getComponentName(), 0).getThemeResource();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}