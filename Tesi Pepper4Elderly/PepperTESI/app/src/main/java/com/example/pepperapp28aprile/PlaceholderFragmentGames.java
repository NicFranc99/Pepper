package com.example.pepperapp28aprile;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.leanback.app.BrowseSupportFragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.lifecycle.ViewModelProvider;
import com.example.pepperapp28aprile.models.Categoria;
import com.example.pepperapp28aprile.presenter.CardPresenter;
import com.example.pepperapp28aprile.utilities.DataManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentGames extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private int section;
    private ArrayList<Persona.Game> gameList;
    private ArrayList<Call> callList;
    private GridView gridView;
    public static String id;

    public static Fragment newInstance(int index,String idPaziente) {
        id = idPaziente;
        PlaceholderFragmentGames fragment = new PlaceholderFragmentGames();
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




    private void getPazienteById(String idPaziente){
        new DataManager(this.getContext(),"pazienti",idPaziente,new DataManager.onDownloadDataListener() {
            @Override
            public void onDataSuccess(Persona paziente) {
                //System.out.println("Ho trovato il paziente! " + paziente.getName());

                GameListAdapter adapter = new GameListAdapter(getActivity(), R.layout.custom_list_singoloprofilo, paziente);

                gridView.setAdapter(adapter);

                //System.out.println("settato1");

                //attaching adapter to the listview
                //listView.setAdapter(adapter);
            }

            @Override
            public void onDataFailed() {
            }

            @Override
            public void notFoundUser() {

            }
        });
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

/*        this.ma = (MainActivity) getActivity();
        ma.status.reset();*/

        //View fragmentLayout = inflater.inflate(R.layout., container, false);
        View fragmentLayout = null;
        //initializing objects
        if(section == 1) {
            fragmentLayout = inflater.inflate(R.layout.activity_profile_grid, container, false);
            //fragmentLayout = inflater.inflate(R.layout.fragment_main_persone, container, false);
            gameList = new ArrayList<>();
            gridView = (GridView) fragmentLayout.findViewById(R.id.mygridview);
            getPazienteById(id);
        }

        if(section == 2)
        {
            getCategoriesToGames(id);
            fragmentLayout = inflater.inflate(R.layout.activity_profile, container, false);
            callList = new ArrayList<>();
            ListView listView = (ListView) fragmentLayout.findViewById(R.id.mylistview);

           String sURL = "https://bettercallpepper.altervista.org/api/getCallsElder.php?appid="+ Globals.myAppID;

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

            //attaching adapter to the listview
            listView.setAdapter(adapter);
            System.out.println("settato2");
        }

    /*
        ListView listView1 = (ListView) fragmentLayout.findViewById(R.id.mylistview);
        String [] array = {"Antonio","Giovanni","Michele","Giuseppe", "Leonardo", "Alessandro"};
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, array);
        listView1.setAdapter(adapter);*/

        return fragmentLayout;
    }

    private void getCategoriesToGames(String idPaziente){
        new DataManager(this.getContext(),"pazienti",idPaziente,new DataManager.onDownloadDataListener() {
            @Override
            public void onDataSuccess(Persona paziente) {
              ArrayList<Categoria> categories = new ArrayList<>();
                for (Persona.Game game : paziente.getEsercizi()) {
                    categories.add(new Categoria(game.getTitleCategory(),game.getNameIcon()));
                }
                System.out.println("FINITO!");
            }

            @Override
            public void onDataFailed() {
            }

            @Override
            public void notFoundUser() {

            }
        });
    }


}

