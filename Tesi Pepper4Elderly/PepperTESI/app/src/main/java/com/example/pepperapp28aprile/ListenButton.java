package com.example.pepperapp28aprile;

import android.content.Intent;
import android.util.Log;

import com.aldebaran.qi.sdk.object.actuation.AttachedFrame;
import com.aldebaran.qi.sdk.object.actuation.OrientationPolicy;
import com.example.pepperapp28aprile.map.RobotHelper;
import com.example.pepperapp28aprile.models.Emergency;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ListenButton extends Thread {

    private ScheduledExecutorService scheduledExecutorService;

    public ListenButton(){ }

    public void start(){
        run();
    }

    public void run(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            String sURL = "https://bettercallpepper.altervista.org/api/getElderliesList.php";
            //System.out.println("ascolto bottone");
            URL url = null;
            try {
                url = new URL(sURL);
                URLConnection request = url.openConnection();
                request.connect();

                // Convert to a JSON object to print data
                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                //JsonArray rootarr = root.getAsJsonArray(); //May be an array, may be an object.
                //JsonObject rootelem = root.getAsJsonArray().get(0).getAsJsonObject();
                int size = root.getAsJsonArray().size();
                //System.out.println("size" + size);


                for(int i = 0; i<size; i++){
                    JsonObject rootelem = root.getAsJsonArray().get(i).getAsJsonObject();
                    boolean pulsante = false;
                    //System.out.println("prova : " + rootelem.get("name").getAsString());

                    pulsante = rootelem.get("pulsante").getAsBoolean();
                    //System.out.println("pulsante = " + pulsante);


                    /*//pulsante = rootelem.get("pulsante").getAsInt(); AL MOMENTO NON C'È QUESTO CAMPO NEL JSON
                    String provvisorio = rootelem.get("name").getAsString();
                    Integer k2 = k++;
                    System.out.println("k++:" + k2);*/
                    //System.out.println("indice di ascolto bottone = " + i + "provvisorio : " + provvisorio);
                    /***adesso risetto a false il pulsante del database**/
                    if(pulsante){
                        System.out.println("PREMUTO");
                        String id = rootelem.get("id").getAsString();
                        String name = rootelem.get("name").getAsString();
                        String surname = rootelem.get("surname").getAsString();
                        String valBed = rootelem.get("room").getAsString();
                        String bedLabel = "Letto " + valBed;

                        int grado = 0;


                        URL urlSetFalse = new URL("https://bettercallpepper.altervista.org/api/callPepper.php"+
                                                        "/?room="+valBed+"&button=false");
                        HttpURLConnection conn = (HttpURLConnection) urlSetFalse.openConnection();
                        conn.setRequestMethod("GET");
                        conn.getInputStream();


                      /*  URL urlSetFalse = new URL( "https://bettercallpepper.altervista.org/api/callPepper.php");
                        HttpURLConnection connection = (HttpURLConnection) urlSetFalse.openConnection();
                        // BufferedReader read = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String data = "room="+bedLabel+"&button="+false+"\r\n";//corpo della richiesta
                        connection.setDoOutput(true);//abilita la scrittura
                        connection.setRequestMethod("POST");//settaggio del metodo
                        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                        wr.write(data);//scrittura del content
                        wr.flush();*/

                        Emergency e = new Emergency(bedLabel, name,surname,0, valBed);
                        e.setId(id);
                        gestire(e);

                        //QUANDO IL ROBOT SARÀ ANDATO A DESTINAZIONE FARE RICHIESTA POST PER SETTARE PULSANTE A ZERO

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(NavigationFragment.end);
        }, 0, 2, TimeUnit.SECONDS);
    }

    private void gestire(Emergency emergency){
        int impegnato = isFree();
        System.out.println("pepper è impegnato?" + impegnato);
        if(impegnato == 0){
            //pepper non è impegnato in nessuna chiamata e in nessun check mattutino

            System.out.println("now is running " + Globals.NowIsRunning);
            switch (Globals.NowIsRunning){
                case ("MainActivity"): {
                    System.out.println("case main");
                    MainActivity.doButtonOperation(emergency);
                    break;
                }
                case ("ProfileActivity"): {
                    System.out.println("case profile");
                    ProfileActivity.doButtonOperation(emergency);
                    break;
                }
                case ("CheckMattutino"): {
                    System.out.println("case check");
                    CheckMattutino.doButtonOperation(emergency,false);
                    break;
                }
                default:{
                    System.out.println("Which is the current activity?");
                }
            }
        }
        else{
            System.out.println("pepper impegnato");

            /***
             * 1)setPepperImpegnato
             * 2)avvia metodo con thread
             */

            try{
                URL urlSetFalse = new URL("https://bettercallpepper.altervista.org/api/setPepperImpegnato.php"+
                        "/?room="+emergency.getValBed()+"&motivation="+impegnato);
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) urlSetFalse.openConnection();
                conn.setRequestMethod("GET");
                conn.getInputStream();
            }  catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("prima di go thread");
            goThread(emergency); //mi metto in ascolto della risposta dell'operatore
            //TODO è un thread perché se l'operaotre dovesse tardare, potrei impostare un tempo massimo di attesa per poi eliminare la richiesta

        }
    }

    private void goThread(Emergency emergency) {
        new Thread(() -> {
            ScheduledExecutorService scheduledExecutorService2 = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService2.scheduleAtFixedRate(() -> {
                int go = checkGoFromServer(emergency);
                System.out.println("check go from server" + go);
                if(go == 1){ //l'operatore del server ha approvato la richiesta di pepper
                    vai(emergency);
                    setPepperGoTo0(emergency);
                    scheduledExecutorService2.shutdown();
                }
                if(go == 2){ //l'operatore del server ha negato la richiesta di pepper
                    setPepperGoTo0(emergency);
                    scheduledExecutorService2.shutdown();
                }

            }, 0, 10, TimeUnit.SECONDS);
        }).start();
    }

    private void vai(Emergency goEmergency){

        //TODO
        /***ATTENZIONE, DA CAMBIARE IL METODO STATICO doBottonOperation,
         * avvisare pepper "è vero che puoi andare ma sei impegnato, dunque devi prima sospendere qullo che stai facendo"
         *
         *
         *
         * ATTUALMENTE POSSIBILI BUG DI ESECUZIONE
         */
        switch (Globals.NowIsRunning){
            case ("MainActivity"): {
                System.out.println("VAI: case main");
                MainActivity.doButtonOperation(goEmergency);
                break;
            }
            case ("WebActivity"): {
                System.out.println("VAI: case WEB");
                WebActivity.doButtonOperation(goEmergency);
                WebActivity.mWebView.loadUrl("");
                break;
            }
            case ("ProfileActivity"): {
                System.out.println("VAI: case profile");
                ProfileActivity.doButtonOperation(goEmergency);
                break;
            }
            case ("CheckMattutino"): {
                System.out.println("VAI: case check");
                CheckMattutino.doButtonOperation(goEmergency,true);
                break;
            }
            default:{
                System.out.println("VAI: Which is the current activity?");
            }
        }
    }

    private Integer checkGoFromServer(Emergency emergency){
        String sURL = "https://bettercallpepper.altervista.org/api/getElderliesList.php";
        URL url = null;

        Integer go = -1;
        try {
            url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element

            int size = root.getAsJsonArray().size();

            for(int i = 0; i<size; i++){
                JsonObject rootelem = root.getAsJsonArray().get(i).getAsJsonObject();

                if(rootelem.get("room").getAsString().equals(emergency.getValBed())){
                    go = rootelem.get("pepper_go").getAsInt();
                    break;
                }
                else if(i == size-1){
                    throw new Exception();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return go; //returns -1 or throw Exception in case of error
    }


    private void setPepperGoTo0(Emergency emergency) {
        try{
                URL urlSetFalse = new URL("https://bettercallpepper.altervista.org/api/setPepperGo.php"+
                        "/?room="+emergency.getValBed()+"&go=0");
                HttpURLConnection conn = null;
                    conn = (HttpURLConnection) urlSetFalse.openConnection();
                    conn.setRequestMethod("GET");
                    conn.getInputStream();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }



    private Integer isFree(){
        boolean freeWeb, freeCheck;
        /***controllare anche che sia free rispetto alle emergenze richieste dal pulsante***/
        freeWeb = checkFreeWebActivity();
        freeCheck = checkFreeCheckActivity();
        System.out.println("free Web: " + freeWeb + "free Check" + freeCheck);

        Integer impegnato = 0;

            if(!freeWeb)
                impegnato = 1;

            if(!freeCheck)
                impegnato = impegnato +2;

            if(freeCheck && freeWeb)
                impegnato = 0;

        return impegnato;
    }

    private boolean checkFreeWebActivity(){
        return WebActivity.free;
    }

    private boolean checkFreeCheckActivity(){
        return NavigationFragment.end;
    }
}
