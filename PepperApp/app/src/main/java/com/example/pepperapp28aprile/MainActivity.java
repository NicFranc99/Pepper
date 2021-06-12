package com.example.pepperapp28aprile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Say;
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
import static com.example.pepperapp28aprile.Globals.senderCallID;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private String currentFragment;
    private FragmentManager fragmentManager;
    static NotificationManager notificationManager;
    private boolean chiamataGestita;
    public static QiContext qiContext;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QiSDK.register(this, this);

        setContentView(R.layout.activity_main);
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);

        /*if (checkPermission()) {
            //main logic or main code

            // . write your main code to execute, It will execute if the permission is already given.

        } else {
            requestPermission();
        }

        String[] permissions =
                {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA
                };

        ActivityCompat.requestPermissions(
                this,
                permissions,
                1);*/

        this.fragmentManager = getSupportFragmentManager();
        chiamataGestita = false;

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
                String sURL = "https://bettercallpepper.altervista.org/api/getElderCall.php?eldid=all";
                String nameMittente = "";
                String surnameMittente  = "";
                String nameDestinatario = "";
                String surnameDestinatario = "";
                String imageMittente = "";
                String imageDestinatario = "";

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
                    nameMittente = rootelem.get("name").getAsString();
                    surnameMittente = rootelem.get("surname").getAsString();
                    imageMittente = rootelem.get("propic").getAsString();
                    nameDestinatario = rootelem.get("ename").getAsString();
                    surnameDestinatario = rootelem.get("esurname").getAsString();
                    imageDestinatario = rootelem.get("epropic").getAsString();

                    senderCallID = rootelem.get("eid").getAsInt();
                    receiveCallID = rootelem.get("id").getAsInt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(nameMittente != "") {
                    chiamataInArrivo(nameMittente,surnameMittente,nameDestinatario,surnameDestinatario, imageDestinatario, imageMittente);
                /*    notificationBuilder.setContentText("Stai ricevendo una chiamata da " + nameMittente + " " + surnameMittente);
                    notificationManager.notify(1, notificationBuilder.build());*/
                    System.out.println("Clock");
                }
            }
        }, 1000, 5000);

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    public void setChiamataGestita(){
        chiamataGestita = false;
        System.out.println("gestita");
    }


    private void chiamataInArrivo(String nameMittente, String surnameMittente, String nameDestinatario, String surnameDestinatario, String imgDest, String imgMit) {
        if(!chiamataGestita){
            chiamataGestita = true;

            annunciaChiamata(nameDestinatario);

            AvvisoFragment avviso = new AvvisoFragment();
            avviso.setNameMittente(nameMittente);
            avviso.setSurnameMittente(surnameMittente);
            avviso.setNameDestinatario(nameDestinatario);
            avviso.setSurnameDestinatario(surnameDestinatario);
            avviso.setImageDest(imgDest);
            avviso.setImageMitt(imgMit);
            setFragment(avviso);
        }
    }

    public static void deleteNotification()
    {
        notificationManager.cancelAll();
    }

    @Override
    public void onUserInteraction() {
        if (getFragment() instanceof LoadingFragment) {
            //menu();
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
        transaction.commitAllowingStateLoss();
        //transaction.commit();
    }

    public Integer getThemeId() {
       try {
            return getPackageManager().getActivityInfo(getComponentName(), 0).getThemeResource();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void annunciaChiamata(String nomeDest){
        try{
        Say ciaoSonoPepper = SayBuilder.with(qiContext) // Create the builder with the context.
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
        animate.async().run();
        }catch(Exception e){
            System.out.println("ex");
        }


    }

    public void salutoIniziale(){
        Say ciaoSonoPepper = SayBuilder.with(qiContext) // Create the builder with the context.
                .withText("Ciao, io sono Pepper!") // Set the text to say.
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
        animate.async().run();

    }

    public void menu(){
        // Create a new say action.
        Say ciaoSonoPepper = SayBuilder.with(qiContext) // Create the builder with the context.
                .withText("Da questo menù puoi cliccare sulla tua foto per chiamare i tuoi parenti!") // Set the text to say.
                .build(); // Build the say action.

        ciaoSonoPepper.async().run();


        Animation animazioneSaulto = AnimationBuilder.with(qiContext)
                .withResources(R.raw.show_tablet_a004)
                .build();

        // Create the second action.
        Animate animate = AnimateBuilder.with(qiContext)
                .withAnimation(animazioneSaulto)
                .build();

        // Run the second action asynchronously.
        animate.async().run();

    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

        this.qiContext = qiContext;
        System.out.println("focusgained");

        if(getFragment() instanceof LoadingFragment){
            salutoIniziale();
        }
        if(getFragment() instanceof MainMenuFragment){
            menu();
        }

    }

    @Override
    public void onBackPressed() {
        int close = 0;
        if(getFragment() instanceof AvvisoFragment){
            Intent intent = new Intent(this, RejectActivity.class);
            this.startActivity(intent);
            System.out.println("RIFIUTA con indietro" );
            setFragment(new MainMenuFragment());
        }
        else if(getFragment() instanceof MainMenuFragment || getFragment() instanceof LoadingFragment){
            android.os.Process.killProcess (android.os.Process.myPid ());
        }

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

}