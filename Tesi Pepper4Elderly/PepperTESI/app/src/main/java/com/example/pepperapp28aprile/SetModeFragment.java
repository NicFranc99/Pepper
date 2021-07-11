package com.example.pepperapp28aprile;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SetModeFragment extends Fragment {

    private MainActivity ma;

    private ArrayList<Persona> peopleList;
    public static ListView listView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        this.ma = (MainActivity) getActivity();
        //ma.status.reset();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

      //  ma.setContentView(R.layout.fragment_set_mode);
        View fragmentLayout = inflater.inflate(R.layout.fragment_set_mode, container, false);



        Button btn1 = fragmentLayout.findViewById(R.id.button_setup_mode);
        //System.out.println(btn.toString());

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCheck();
            }
        });

        Button btn2 = fragmentLayout.findViewById(R.id.button_production_mode);
        //System.out.println(btn.toString());

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTele();
            }
        });

        return fragmentLayout;
    }

    public void startCheck() {

        CheckMattutino.ma = ma;
        CheckMattutino.bottone = false;
        Intent intent = new Intent(getActivity(),CheckMattutino.class);
        startActivity(intent);
    }

    public void startTele() {
        ma.setFragment(new MainMenuFragment());
    }

}
