package com.example.pepperapp28aprile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.object.conversation.BodyLanguageOption;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.humanawareness.HumanAwareness;
import com.aldebaran.qi.sdk.object.locale.Language;
import com.aldebaran.qi.sdk.object.locale.Locale;
import com.aldebaran.qi.sdk.object.locale.Region;
import com.github.wihoho.Trainer;
import com.github.wihoho.constant.FeatureType;
import com.github.wihoho.jama.Matrix;
import com.github.wihoho.training.CosineDissimilarity;
import com.github.wihoho.training.FileManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.example.pepperapp28aprile.Globals.myAppID;

public class MainMenuFragment extends Fragment {

    private static final String TAG = "MSI_MainMenuFragment";
    private MainActivity ma;
    private Trainer trainer;

    private ArrayList<Persona> peopleList;
    public static ListView listView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        this.ma = (MainActivity) getActivity();
        //ma.status.reset();

        //ma.menu();
/*
                 SayBuilder.with(MainActivity.qiContext)
                .withText("Da questo menù puoi cliccare sulla tua foto per chiamare i tuoi parenti!")
                .withLocale(new Locale(Language.ITALIAN, Region.ITALY))
                .withBodyLanguageOption(BodyLanguageOption.DISABLED)
                .buildAsync().andThenCompose(say -> {
                    Log.d(TAG, "Say started : " + "text");
                    return say.async().run();
                });*/

       /* Say ciaoSonoPepper = SayBuilder.with(MainActivity.qiContext) // Create the builder with the context.
                .withText("Da questo menù puoi cliccare sulla tua foto per chiamare i tuoi parenti!") // Set the text to say.
                .build(); // Build the say action.

        ciaoSonoPepper.async().run();*/

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
        gridview.setAdapter(new MyListAdapter(getActivity(),R.layout.custom_list, peopleList,trainer));

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


    static void downloadFile(URL url, String fileName) {

        System.out.println("Downloading from "+url.toString()+" to "+fileName);
        try (InputStream in = url.openStream();
             BufferedInputStream bis = new BufferedInputStream(in);
             FileOutputStream fos = new FileOutputStream(fileName)) {

            byte[] data = new byte[1024];
            int count;
            while ((count = bis.read(data, 0, 1024)) != -1) {
                fos.write(data, 0, count);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addestra(ArrayList<Persona> peopleList){
        try{

            // Build a trainer

            trainer = Trainer.builder()
                    .metric(new CosineDissimilarity())
                    .featureType(FeatureType.PCA)
                    .numberOfComponents(3)
                    .k(1)
                    .build();

            URL url;
            File initialFile = null;
            System.out.println(peopleList);
            for(int i = 0; i<peopleList.size(); i++)
            {
                //File initialFile = new File("sdcard/faces/simone1.pgm");
                for(int k = 1; k<= 11 ; k++){
                    String fileName = peopleList.get(i).urlFullName()+"_"+k+".pgm";
                    url = new URL("https://bettercallpepper.altervista.org/img/training/"+(
                            peopleList.get(i).urlFullName()+"/"+ fileName).toLowerCase());

                    System.out.println(url.toString());


                    /*FileUtils.copyURLToFile(
                            url,
                            new File("/sdcard/faces/"+fileName.toLowerCase()),
                            60000,
                            60000);*/

                    downloadFile(url,/*Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+*/"/sdcard/faces/"+fileName.toLowerCase());
                    initialFile = new File(/*Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+*/"/sdcard/faces/"+fileName.toLowerCase());


                  /*  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                       // initialFile = Paths.get(url.toURI().getPath()).toFile();
                        initialFile = Paths.get(url.toURI()).toFile();
                        System.out.println("initial file: " + initialFile.getPath());

                    }
                    else throw new Exception();*/
                    //trainer.add(vectorize(FileManager.convertPGMtoMatrix(initialFile.getPath())), peopleList.get(i).urlFullName());

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



   /*         initialFile = new File("sdcard/faces/simone4.pgm");
            System.out.println("voglio simone, riconosco" + trainer.recognize(vectorize(FileManager.convertPGMtoMatrix(initialFile.getPath()))));
            //assertEquals("simone", trainer.recognize(vectorize(FileManager.convertPGMtoMatrix(initialFile.getPath()))));

            //

            System.out.println("END");


                 /*
        filePath = "/home/ubuntu/Scrivania/faces/simone4.pgm";
        inputStream = Resources.class.getResourceAsStream(filePath);
        tempFile = File.createTempFile("pic", ",pgm");
        tempFile.deleteOnExit();
        ByteStreams.copy(inputStream, new FileOutputStream(tempFile));*/

        }catch(Exception e) {
            e.printStackTrace();

        }
    }

}
