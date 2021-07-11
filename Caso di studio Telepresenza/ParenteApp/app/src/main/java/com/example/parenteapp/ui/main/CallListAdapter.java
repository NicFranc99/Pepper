package com.example.parenteapp.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.parenteapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

//we need to extend the ArrayAdapter class as we are building an adapter
public class CallListAdapter extends ArrayAdapter<Call> {

    //the list values in the List of type hero
    List<Call> callList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    //constructor initializing the values
    public CallListAdapter(Context context, int resource, List<Call> callList) {
        super(context, resource, callList);
        this.context = context;
        this.resource = resource;
        this.callList = callList;
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textViewName = view.findViewById(R.id.textViewName);

        //getting the call of the specified position
        Call call = callList.get(position);

        //adding values to the list item
        textViewName.setText(call.getCallmsg() + "\nDurata: " + call.getFancyCallTime());
        Bitmap img;
        try {
            //adding values to the list item
            img = BitmapFactory.decodeStream((InputStream) new URL(call.getImage()).getContent());
            imageView.setImageBitmap(img);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //adding a click listener to the button to remove item from the list
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we will call this method to remove the selected value from the list
                //we are passing the position which is to be removed in the method 
                removeCall(position);
            }
        });

        //finally returning the view
        return view;
    }

    //this method will remove the item from the list 
    private void removeCall(final int position) {
        //Creating an alert dialog to confirm the deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sei sicuro di volerla rimuovere?");

        //if the response is positive in the alert 
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //removing the item 
                callList.remove(position);

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
}