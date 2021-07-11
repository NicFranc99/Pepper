package com.example.pepperapp28aprile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class AvvisoFragment extends Fragment {

    private static final String TAG = "MSI_MainMenuFragment";
    private MainActivity ma;

    private ArrayList<Persona> peopleList;
    public static ListView listView;
    private String nameMittente;
    private String surnameMittente;
    private String nameDestinatario;
    private String surnameDestinatario;
    private String imgDest;
    private String imgMit;

    public void setImageDest(String imgDest){
        this.imgDest = imgDest;
    }

    public void setImageMitt(String imgMit){
        this.imgMit = imgMit;
    }

    public void setSurnameMittente(String surnameMittente){
        this.surnameMittente = surnameMittente;
    }

    public void setNameMittente(String nameMittente){
        this.nameMittente = nameMittente;
    }

    public void setNameDestinatario(String nameDestinatario){
        this.nameDestinatario = nameDestinatario;
    }

    public void setSurnameDestinatario(String surnameDestinatario){
        this.surnameDestinatario = surnameDestinatario;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        this.ma = (MainActivity) getActivity();
        //ma.status.reset();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        View view = inflater.inflate(R.layout.fragment_avviso, container, false);
        TextView chiamataInArrivo = view.findViewById(R.id.textInArrivo);
        chiamataInArrivo.setText("Chiamata in arrivo per " + nameDestinatario);

        ImageView imgDest = view.findViewById(R.id.imgDestinatario);
        Bitmap bitmapDest = null;
        try {
            //adding values to the list item
            bitmapDest = BitmapFactory.decodeStream((InputStream) new URL(this.imgDest).getContent());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgDest.setImageBitmap(bitmapDest);

        ImageView imgMit = view.findViewById(R.id.imgMittente);

        Bitmap bitmapMit = null;
        try {
            //adding values to the list item
            bitmapMit = BitmapFactory.decodeStream((InputStream) new URL(this.imgMit).getContent());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgMit.setImageBitmap(bitmapMit);

        Button accetta = view.findViewById(R.id.accetta);

        accetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ma, AcceptActivity.class);
                ma.startActivity(intent);
                System.out.println("ACCETTA" );
                ma.setChiamataGestita();
                ma.setFragment(new MainMenuFragment());
            }
        });

        Button rifiuta = view.findViewById(R.id.rifiuta );

        rifiuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ma, RejectActivity.class);
                ma.startActivity(intent);
                ma.setFragment(new MainMenuFragment());
                System.out.println("RIFIUTA" );
                ma.setChiamataGestita();
                ma.setFragment(new MainMenuFragment());
            }
        });

        return view;
    }



}