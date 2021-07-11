package com.example.pepperapp28aprile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.actuation.AttachedFrame;
import com.aldebaran.qi.sdk.object.actuation.Frame;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.geometry.Transform;
import com.aldebaran.qi.sdk.object.streamablebuffer.StreamableBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executor;

import com.example.pepperapp28aprile.map.*;
import com.example.pepperapp28aprile.models.Emergency;
import com.example.pepperapp28aprile.provider.Providers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class CheckMattutino extends RobotActivity implements RobotLifecycleCallbacks {

    public static final String CONSOLE_TAG = "Pepper4RSA";
    private static final int PERMISSION_STORAGE = 1;
    public static MainActivity ma;

    private QiContext qiContext;
    private SaveFileHelper saveFileHelper;
    private RobotHelper robotHelper;

    private TreeMap<String, AttachedFrame> savedLocations;
    private ArrayList<Emergency> anzianiToCheck;
    private static boolean localized;

    private Chat chat;

    public static boolean ennesimo;
    public CheckDecision checkDecisionFragment;
    public static boolean bottone;
    private static boolean doButtonOperation;
    private static boolean doButtonOperationImpegnato;
    public static Emergency buttonEmergency;
    private static FragmentManager fragmentManager;
    public static boolean tornaACasa;

    public static final String FRAGMENT_TAG = "main_fragment";


    public ArrayList<Emergency>  getAnzianiList(){
        return anzianiToCheck;
    }

    public MainActivity getMainActivity(){
        return ma;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO NON SO SE QUESTO VA MESSO?
        QiSDK.register(this, this);

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        Providers.init(getApplicationContext());

       // setContentView(R.layout.activity_check_mattutino);
         setContentView(R.layout.fragment_main);
        this.fragmentManager = getSupportFragmentManager();


        checkDecisionFragment = new CheckDecision();
        //setFragment(checkDecisionFragment);


         doButtonOperation = false;
         doButtonOperationImpegnato = false;
         doButtonOp();


        //robotHelper.say("prova");

      //  MainCheckFragment mainFragment = new MainCheckFragment();
       // this.getSupportFragmentManager().beginTransaction().add(R.id.frame_fragment, mainFragment).addToBackStack(null).commit();

        getAnziani();
        System.out.println(anzianiToCheck);

        // Connect to the URL using java's native library

        //Check android permission
        if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            this.init();
            System.out.println("on create init?");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
        }


        System.out.println("end on create1");

        /***TEST**
        qiContext = this.getQiContext();
        //this.qiContext = qiContext;
        System.out.println("context:" + qiContext);
        this.robotHelper.onRobotFocusGained(qiContext);
        System.out.println("on robot focus gained: else strano");
        NavigationFragment.bottone = false;
        setFragment(checkDecisionFragment);
        /***FINETEST***/


    }


    private void getAnziani(){
        anzianiToCheck = new ArrayList<>();
        String sURL = "https://bettercallpepper.altervista.org/api/getElderliesList.php";
        System.out.println("prova");
        URL url = null;
        try {
            url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            //JsonArray rootarr = root.getAsJsonArray(); //May be an array, may be an object.
            int size = root.getAsJsonArray().size();
            System.out.println("size" + size);
            for(int i = 0; i<size; i++){
                JsonObject rootelem = root.getAsJsonArray().get(i).getAsJsonObject();
                String name = rootelem.get("name").getAsString();
                String surname = rootelem.get("surname").getAsString();
                String bedNumber = rootelem.get("room").getAsString();
                String bedLabel = "Letto " + bedNumber;
                anzianiToCheck.add(new Emergency(bedLabel,name,surname,bedNumber));
                System.out.println("aggiunto " + bedLabel+name+surname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void doButtonOperation(Emergency e, boolean impegnato){
        buttonEmergency = e;
        if(impegnato)
            doButtonOperationImpegnato = true;
        else doButtonOperation = true;
    }

    private void doButtonOp() {
        new Thread(() -> {
            while(true){
                while(!doButtonOperation && !doButtonOperationImpegnato & !ennesimo){ }

                if(doButtonOperation){

                    System.out.println("doButtonOperationFree");
                    doButtonOperation = false;
                    try {
                        loadLocations(); //si spera che siano state già scaricate dal server durante questa esecuzione, possibile bug
                        if(localized){
                            //MainCheckFragment mainFragment = new MainCheckFragment();
                            //this.getSupportFragmentManager().beginTransaction().add(R.id.frame_fragment, mainFragment).addToBackStack(null).commit();
                            NavigationFragment.bottone = true;
                            NavigationFragment.buttonEmergency.add(buttonEmergency);
                            //setContentView(R.layout.fragment_navigation);
                            //checkDecisionFragment.setNavigationFragment();
                            System.out.println("lo sto per settare");
                            setFragment(new NavigationFragment());

                            //checkDecisionFragment.setNavigationFragment();

                           // ButtonToNavActivity.navFr = new NavigationFragment();
                           // Intent intent = new Intent(this, ButtonToNavActivity.class);
                           // this.startActivity(intent);

                            //setFragment(new NavigationFragment());
                        }
                        else{
                            setPepperFailedToGo(buttonEmergency);
                            System.out.println("robot non localizzato, impossibile effettuare movimento");
                        }
                   }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if(doButtonOperationImpegnato){
                    System.out.println("doButtonOperationImpeganto");
                    doButtonOperationImpegnato = false;
                    try {
                       loadLocations(); //si spera che siano state già scaricate dal server durante questa esecuzione, possibile bug
                        if(localized){
                            //MainCheckFragment mainFragment = new MainCheckFragment();
                            //this.getSupportFragmentManager().beginTransaction().add(R.id.frame_fragment, mainFragment).addToBackStack(null).commit();

                            /***
                             * if stai assistendo un anziano ; aspetta finché occupato
                             *
                             * appena si libera inserisci in testa alla lista delle emergenze la Emergency e
                             */




                            System.out.println("navigation bottone???" + NavigationFragment.bottone);
                            if(NavigationFragment.bottone == true){
                                NavigationFragment.buttonEmergency.add(buttonEmergency);
                                NavigationFragment.buttonEmergency.add(buttonEmergency);
                                setFragment(new NavigationFragment());
                            }
                            else{
                                System.out.println("navigation fragment free???" + NavigationFragment.free);
                                while(NavigationFragment.free == false);
                                buttonEmergency.isButtonEmergency = true;
                                NavigationFragment.allTheChecks.add(0,buttonEmergency);
                                NavigationFragment.buttonEmergency.add(buttonEmergency);
                                setFragment(new NavigationFragment());

                            /*NavigationFragment.bottone = true;
                            NavigationFragment.buttonEmergency = buttonEmergency;
                            setFragment(new NavigationFragment());*/
                            }

                        }
                        else{
                            setPepperFailedToGo(buttonEmergency);
                            System.out.println("errore");
                        }
                   }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                /*if(localized){
                    //MainCheckFragment mainFragment = new MainCheckFragment();
                    //this.getSupportFragmentManager().beginTransaction().add(R.id.frame_fragment, mainFragment).addToBackStack(null).commit();
                    NavigationFragment.bottone = true;
                    NavigationFragment.buttonEmergency = buttonEmergency;
                    setFragment(new NavigationFragment());
                }
                else{
                    System.out.println("robot non localizzato, impossibile effettuare movimento");
                }*/
            }
        }).start();
    }

    public void setFragment(Fragment fragment) {


        try{
            //fragmentManager = getSupportFragmentManager();

            System.out.println("starting fragment Transaction for fragment : " + fragment.getClass().getSimpleName());
            //FragmentTransaction transaction =  this.getSupportFragmentManager().beginTransaction();
            FragmentTransaction transaction =  fragmentManager.beginTransaction();
            //transaction.setCustomAnimations(R.anim.enter_fade_in_right, R.anim.exit_fade_out_left, R.anim.enter_fade_in_left, R.anim.exit_fade_out_right);
            transaction.replace(R.id.check_main, fragment,"currentFragment");
            transaction.addToBackStack(null);
            Integer test = transaction.commit();
            System.out.println("SENZA BOOL " + test);
        }catch(Exception e){
            System.out.println("ECCEZIONE SET FRAGMENT CHECK MATTUTINO");
            e.printStackTrace();
        }


    }


    private void init() {
        QiSDK.register(this, this);

        this.saveFileHelper = new SaveFileHelper();
        this.robotHelper = new RobotHelper();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
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

    private void setPepperFailedToGo(Emergency emergency) {
        try{
            URL urlSetFalse = new URL("https://bettercallpepper.altervista.org/api/setPepperFailed.php"+
                    "/?room="+emergency.getValBed()+"&failed=1");
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) urlSetFalse.openConnection();
            conn.setRequestMethod("GET");
            conn.getInputStream();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        this.qiContext = qiContext;
        this.robotHelper.onRobotFocusGained(qiContext);
        System.out.println("start on robot focus gained");

        //onCreateView();

        Globals.NowIsRunning = Globals.CheckMattutino;

        try {
           loadLocations();

            System.out.println("CHECK MATTUTINO ON ROBOT FOCUS GAINED" + bottone +" " + localized);

            if(tornaACasa){
                Globals.arrivato = false;
                tornaACasa = false;
                NavigationFragment.bottone = false;
                NavigationFragment.tornaACasa = true;
                setFragment(new NavigationFragment());
            }

            if(bottone && localized){
                System.out.println("localizzato e bottone premuto, vado!");

                    NavigationFragment.bottone = true;
                    NavigationFragment.buttonEmergency.add(buttonEmergency);
                    setFragment(new NavigationFragment());
                    /*runOnUiThread( ()->{
                        setContentView(R.layout.fragment_navigation);
                    });*/
                    //setContentView(R.layout.fragment_navigation);
            }
            else if(bottone && !localized){
                //TODO qui avvisare il server
                setPepperFailedToGo(buttonEmergency);

                System.out.println("on robot focus gained: robot non localizzato, impossibile effettuare movimento");
                NavigationFragment.bottone = false;
                setFragment(checkDecisionFragment);
            }
            else if(!bottone && localized){
                System.out.println("servizio mattutino");
                NavigationFragment.bottone = false;
                setFragment(checkDecisionFragment);
            }
            else{
                //qui non dovrebbe mai enatrare, ma meglio che ci sia questo else
                System.out.println("on robot focus gained: else strano");
                NavigationFragment.bottone = false;
                setFragment(checkDecisionFragment);
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bottone = false;

    }


    private void loadLocations() throws FileNotFoundException {
        // Read file into a temporary hashmap.
        File file = new File("/sdcard/Maps", "points.json");
        if (file.exists()) {
            Map<String, Vector2theta> vectors = saveFileHelper.getLocationsFromFile("/sdcard/Maps", "points.json");

            // Clear current savedLocations.
            savedLocations = new TreeMap<>();
            Frame mapFrame = robotHelper.getMapFrame();

            // Build frames from the vectors.
            for (Map.Entry<String, Vector2theta> entry : vectors.entrySet()) {
                // Create a transform from the vector2theta.
                Transform t = entry.getValue().createTransform();
                Log.d(CONSOLE_TAG, "loadLocations: " + entry.getKey());

                // Create an AttachedFrame representing the current robot frame relatively to the MapFrame.
                AttachedFrame attachedFrame = mapFrame.async().makeAttachedFrame(t).getValue();

                // Store the FreeFrame.
                savedLocations.put(entry.getKey(), attachedFrame);
            }
            Log.d(CONSOLE_TAG, "loadLocations: Done");
        }
        else {
            throw new FileNotFoundException();
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
    protected void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    public QiContext getQiContext() {
        return qiContext;
    }

    public RobotHelper getRobotHelper() {
        return robotHelper;
    }

    public TreeMap<String, AttachedFrame> getSavedLocations() {
        return savedLocations;
    }

    public SaveFileHelper getSaveFileHelper() {
        return saveFileHelper;
    }

    public void startLocalizing() {
        robotHelper.say(getString(R.string.start_localizing));
        if (robotHelper.localizeAndMapHelper.getStreamableMap() == null) {
            StreamableBuffer mapData = getSaveFileHelper().readStreamableBufferFromFile("/sdcard/Maps", "mapData.txt");;
            if (mapData == null) {
                Log.d(CONSOLE_TAG, "startLocalizing: No Map Available");
                robotHelper.localizeAndMapHelper.raiseFinishedLocalizing(LocalizeAndMapHelper.LocalizationStatus.MAP_MISSING);
            } else {
                Log.d(CONSOLE_TAG, "startLocalizing: get and set map");

                robotHelper.localizeAndMapHelper.setStreamableMap(mapData);

                robotHelper.holdAbilities(true).andThenConsume((useless) ->
                        robotHelper.localizeAndMapHelper.animationToLookInFront().andThenConsume(aVoid ->
                                robotHelper.localizeAndMapHelper.localize()));
            }
        } else {
            robotHelper.holdAbilities(true).andThenConsume((useless) ->
                    robotHelper.localizeAndMapHelper.animationToLookInFront().andThenConsume(aVoid ->
                            robotHelper.localizeAndMapHelper.localize()));
        }
    }

    public boolean isLocalized(){

        return localized;
    }

    public void setLocalized(boolean localized) { this.localized = localized;}

}