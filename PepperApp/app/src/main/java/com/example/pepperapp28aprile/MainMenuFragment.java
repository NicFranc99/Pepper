package com.example.pepperapp28aprile;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aldebaran.qi.sdk.object.humanawareness.HumanAwareness;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainMenuFragment extends Fragment {

    private static final String TAG = "MSI_MainMenuFragment";
    private MainActivity ma;

    private ArrayList<Persona> peopleList;
    private ListView listView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        this.ma = (MainActivity) getActivity();
        //ma.status.reset();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        View fragmentLayout = inflater.inflate(R.layout.fragment_main_menu, container, false);

        peopleList = new ArrayList<>();
        listView = (ListView) fragmentLayout.findViewById(R.id.mylistview);


        String sURL = "https://bettercallpepper.altervista.org/api/getElderlies.php?appid=1";

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
                peopleList.add(new Persona(img, name, surname));
            }
        } catch (Exception e) {
            System.out.println("Erroreeeee");
            e.printStackTrace();
        }


        //View customList = inflater.inflate(R.layout.custom_list, container, false);

        GridView gridview = (GridView) fragmentLayout.findViewById(R.id.gridview);
        gridview.setAdapter(new MyListAdapter(getActivity(),R.layout.custom_list, peopleList));

        return fragmentLayout;
    }

}
