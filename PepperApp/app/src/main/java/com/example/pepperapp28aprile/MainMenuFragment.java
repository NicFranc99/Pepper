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
import com.github.wihoho.Trainer;
import com.github.wihoho.constant.FeatureType;
import com.github.wihoho.jama.Matrix;
import com.github.wihoho.training.CosineDissimilarity;
import com.github.wihoho.training.FileManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.example.pepperapp28aprile.Globals.myAppID;

public class MainMenuFragment extends Fragment {

    private static final String TAG = "MSI_MainMenuFragment";
    private MainActivity ma;

    private ArrayList<Persona> peopleList;
    public static ListView listView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        this.ma = (MainActivity) getActivity();
        //ma.status.reset();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        View fragmentLayout = inflater.inflate(R.layout.fragment_main_menu, container, false);

        peopleList = new ArrayList<>();
        listView = (ListView) fragmentLayout.findViewById(R.id.mylistview);


        String sURL = "https://bettercallpepper.altervista.org/api/getElderliesList.php";

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
                ArrayList<String> training = null;
                peopleList.add(new Persona(img, id, name, surname, training));
            }
        } catch (Exception e) {
            System.out.println("Erroreeeee");
            e.printStackTrace();
        }

        addestra(peopleList);


        //View customList = inflater.inflate(R.layout.custom_list, container, false);

        GridView gridview = (GridView) fragmentLayout.findViewById(R.id.gridview);
        gridview.setAdapter(new MyListAdapter(getActivity(),R.layout.custom_list, peopleList));

        return fragmentLayout;
    }


    Matrix vectorize(Matrix input) {
        int m = input.getRowDimension();
        int n = input.getColumnDimension();

        Matrix result = new Matrix(m * n, 1);
        for (int p = 0; p < n; p++) {
            for (int q = 0; q < m; q++) {
                result.set(p * m + q, 0, input.get(q, p));
            }
        }
        return result;
    }

    private void addestra(ArrayList<Persona> peopleList){
        try{

            // Build a trainer

            Trainer trainer = Trainer.builder()
                    .metric(new CosineDissimilarity())
                    .featureType(FeatureType.PCA)
                    .numberOfComponents(3)
                    .k(1)
                    .build();

            URL url;
            File initialFile = null;
            for(int i = 0; i<peopleList.size(); i++){
                //File initialFile = new File("sdcard/faces/simone1.pgm");
                for(int k = 1; k<= 4 ; k++){
                    url = new URL("https://bettercallpepper.altervista.org/img/training/"+
                            peopleList.get(i).urlFullName()+"/"+peopleList.get(i).urlFullName()+"_"+k+".pgm");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        initialFile = Paths.get(url.toURI()).toFile();
                    }
                    else throw new Exception();
                    trainer.add(vectorize(FileManager.convertPGMtoMatrix(initialFile.getPath())), peopleList.get(i).urlFullName());
                }
            }

            System.out.println("before train");
            // train

            trainer.train();

            System.out.println("after train");

            // recognize

/*
            initialFile = new File("sdcard/faces/irene4.pgm");
            assertEquals("irene", trainer.recognize(vectorize(FileManager.convertPGMtoMatrix(initialFile.getPath()))));
*/

        /*
        filePath = "/home/ubuntu/Scrivania/faces/simone4.pgm";
        inputStream = Resources.class.getResourceAsStream(filePath);
        tempFile = File.createTempFile("pic", ",pgm");
        tempFile.deleteOnExit();
        ByteStreams.copy(inputStream, new FileOutputStream(tempFile));*/

   /*         initialFile = new File("sdcard/faces/simone4.pgm");
            System.out.println("voglio simone, riconosco" + trainer.recognize(vectorize(FileManager.convertPGMtoMatrix(initialFile.getPath()))));
            //assertEquals("simone", trainer.recognize(vectorize(FileManager.convertPGMtoMatrix(initialFile.getPath()))));

            //    initialFile = new File("sdcard/faces/prova.pgm");
            //   System.out.println("voglio simone, riconosco" + trainer.recognize(vectorize(FileManager.convertPGMtoMatrix(initialFile.getPath()))));

            System.out.println("END");
*/
        }catch(Exception e) {
            e.printStackTrace();

        }
    }
}
