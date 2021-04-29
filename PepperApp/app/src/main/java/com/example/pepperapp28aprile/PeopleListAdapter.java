package com.example.pepperapp28aprile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

//we need to extend the ArrayAdapter class as we are building an adapter
public class PeopleListAdapter extends ArrayAdapter<Persona> {

    //the list values in the List of type hero
    List<Persona> peopleList;

    //activity context 
    Context context;

    //the layout resource file for the list items
    int resource;

    //constructor initializing the values 
    public PeopleListAdapter(Context context, int resource, List<Persona> peopleList) {
        super(context, resource, peopleList);
        this.context = context;
        this.resource = resource;
        this.peopleList = peopleList;
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);      //era parent, ora null

        //getting the view elements of the list from the view
       // ImageView imageView = view.findViewById(R.id.imageView);
        TextView textViewName = view.findViewById(R.id.textViewName);
        RelativeLayout rl = view.findViewById(R.id.profiloSingolo);
        //TextView textViewTeam = view.findViewById(R.id.textViewTeam);
        ImageView callbutton = view.findViewById(R.id.callbutton);

        //getting the hero of the specified position
        Persona p = peopleList.get(position);

        //adding values to the list item
        Bitmap img;
        try {
            //adding values to the list item
            img = BitmapFactory.decodeStream((InputStream) new URL(p.getImage()).getContent());
            callbutton.setImageBitmap(img);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        textViewName.setText(p.getName());
        //textViewTeam.setText(p.getStatus());

        //adding a click listener to the button to remove item from the list
        callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("chiama");
                //we will call this method to remove the selected value from the list
                //we are passing the position which is to be removed in the method 
                removeHero(position,view); //PARTE LA CHIAMATA: DA FARE
            }
        });

        //finally returning the view
        return view;
    }

    //this method will remove the item from the list 
    private void removeHero(final int position,View view) {
        //Creating an alert dialog to confirm the deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sei sicuro di voler chiamare "+ peopleList.get(position).getName() +"?");

        //if the response is positive in the alert 
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //removing the item
                startWeb(view, peopleList.get(position).getId()); //
                //reloading the list
                notifyDataSetChanged();
            }
        });

        //if response is negative nothing is being done 
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //creating and displaying the alert dialog 
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void startWeb(View view, int id) {
        Intent intent = new Intent(this.getContext(), WebActivity.class);
        Bundle b = new Bundle();
        b.putInt("id", id); //Your id
        intent.putExtras(b);
        this.getContext().startActivity(intent);
    }
}