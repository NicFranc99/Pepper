package com.example.pepperapp28aprile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.github.wihoho.Trainer;
import com.github.wihoho.jama.Matrix;
import com.github.wihoho.training.FileManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.example.pepperapp28aprile.Globals.myAppID;
import static com.example.pepperapp28aprile.MainMenuFragment.downloadFile;

//we need to extend the ArrayAdapter class as we are building an adapter
public class MyListAdapter extends ArrayAdapter<Persona>  {

    //ArrayAdapter<Persona> arrayAdapter;


    //the list values in the List of type hero
    List<Persona> peopleList;
    public static Trainer trainer;

    //activity context 
    Context context;

    //the layout resource file for the list items
    int resource;

    //constructor initializing the values 
    public MyListAdapter(Context context, int resource, List<Persona> peopleList, Trainer trainer) {
        //arrayAdapter = new ArrayAdapter<Persona>(context, resource, peopleList);
        super(context, resource, peopleList);
        this.context = context;
        this.resource = resource;
        this.peopleList = peopleList;
        this.trainer = trainer;
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, parent, false);

        //getting the view elements of the list from the view
        //ImageView imageView = view.findViewById(R.id.buttonDelete);
        TextView textViewName = view.findViewById(R.id.textViewName);
        //TextView textViewTeam = view.findViewById(R.id.textViewTeam);
        ImageView imageButton = view.findViewById(R.id.buttonDelete);
       // Button buttonDelete = view.findViewById(R.id.buttonDelete);

        //getting the hero of the specified position
        Persona p = peopleList.get(position);

        //adding values to the list item 
        Bitmap img;
        try {
            //adding values to the list item
            img = BitmapFactory.decodeStream((InputStream) new URL(p.getImage()).getContent());
            imageButton.setImageBitmap(img);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        textViewName.setText(p.getName());
        //textViewTeam.setText(hero.getTeam());

        //ImageButton buttonDelete = view.findViewById(R.id.buttonDelete);

        //adding a click listener to the button to remove item from the list
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we will call this method to remove the selected value from the list
                //we are passing the position which is to be removed in the method
                myAppID = p.getId();
                System.out.println("MYAPP" + myAppID);

                startWeb(view,p);



            }
        });

        //finally returning the view
        return view;
    }







    private void startWeb(View view, Persona p) {
        ProfileActivity.name = p.getName();
        Intent intent = new Intent ( this.getContext(), ProfileActivity.class );
        this.getContext().startActivity(intent);

        /*

        Intent intent = new Intent(this.getContext(), WebActivityRecognition.class);
        Bundle b = new Bundle();
        b.putString("name",p.getName());
        b.putString("surname",p.getSurname());
        //b.putInt("id", id); //Your id
        //b.putInt("type", 1);
        intent.putExtras(b);
        this.getContext().startActivity(intent);*/

    }
    //this method will remove the item from the list 
 /*   private void removeHero(final int position) {
        //Creating an alert dialog to confirm the deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete this?");

        //if the response is positive in the alert 
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //removing the item 
                heroList.remove(position);

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
    } */
}