package com.example.parenteapp;

import android.app.Activity;
import android.os.Bundle;

import com.example.parenteapp.ui.main.Persona;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static com.example.parenteapp.Globals.myAppID;
import static com.example.parenteapp.Globals.receiveCallID;

public class RejectActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String sURL = "https://bettercallpepper.altervista.org/api/updateCallStatus.php?parid="+ myAppID +"&eldid="+receiveCallID+"&status=0";

        // Connect to the URL using java's native library
        URL url = null;
        try {
            url = new URL(sURL);
            URLConnection request = url.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(request.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println("Url opened");
            MainActivity.deleteNotification();
            //request.connect();
        } catch (Exception e) {
            System.out.println("Erroreeeee");
            e.printStackTrace();
        }
        finish();
    }
}
