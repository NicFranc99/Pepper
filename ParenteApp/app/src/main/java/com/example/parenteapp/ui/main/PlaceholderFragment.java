package com.example.parenteapp.ui.main;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.parenteapp.Globals;
import com.example.parenteapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private int section;
    private ArrayList<Persona> peopleList;
    private ArrayList<Call> callList;
    private ListView listView;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        section = index;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        /*
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;*/
        View fragmentLayout = inflater.inflate(R.layout.fragment_main_persone, container, false);
        //Thread thread = new Thread(new Runnable() {
         //   @Override
           // public void run() {
             //   try  {

                    //initializing objects
                    if(section == 1) {
                        System.out.println("Set1");
                        peopleList = new ArrayList<>();
                        listView = (ListView) fragmentLayout.findViewById(R.id.mylistview);

                        String sURL = "https://bettercallpepper.altervista.org/api/getElderlies.php?appid=" + Globals.myAppID;

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
                                int id = rootelem.get("id").getAsInt();
                                peopleList.add(new Persona(img, id, name, surname));
                            }
                        } catch (Exception e) {
                            System.out.println("Erroreeeee");
                            e.printStackTrace();
                        }

                            //creating the adapter
                            PeopleListAdapter adapter = new PeopleListAdapter(getActivity(), R.layout.custom_list, peopleList);
                            listView.setAdapter(adapter);
                            //attaching adapter to the listview
                    }

                    if(section == 2)
                    {
                        System.out.println("Set2");
                        callList = new ArrayList<>();
                        listView = (ListView) fragmentLayout.findViewById(R.id.mylistview);

                        String sURL = "https://bettercallpepper.altervista.org/api/getCalls.php?appid=" + Globals.myAppID;

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
                                String callmsg = rootelem.get("callmsg").getAsString();
                                String img = rootelem.get("img").getAsString();
                                int type = rootelem.get("type").getAsInt();
                                int calltime = rootelem.get("calltime").getAsInt();
                                callList.add(new Call(type, callmsg, img, calltime));
                            }
                        } catch (Exception e) {
                            System.out.println("Erroreeeee");
                            e.printStackTrace();
                        }

                        CallListAdapter adapter = new CallListAdapter(getActivity(), R.layout.cronologia, callList);
                        listView.setAdapter(adapter);
                    }
        // } catch (Exception e) {
        //            System.out.println("Erroreeeee2");
        //           e.printStackTrace();
        //       }
        //    }
        //});
        //adding some values to our list

        //thread.start();

    /*
        ListView listView1 = (ListView) fragmentLayout.findViewById(R.id.mylistview);
        String [] array = {"Antonio","Giovanni","Michele","Giuseppe", "Leonardo", "Alessandro"};
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, array);
        listView1.setAdapter(adapter);*/
        return fragmentLayout;
    }
}

