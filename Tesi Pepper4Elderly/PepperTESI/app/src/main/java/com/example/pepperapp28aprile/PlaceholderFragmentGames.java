package com.example.pepperapp28aprile;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.QiException;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.example.pepperapp28aprile.map.RobotHelper;
import com.example.pepperapp28aprile.models.Categoria;
import com.example.pepperapp28aprile.presenter.CardPresenter;
import com.example.pepperapp28aprile.utilities.DataManager;
import com.example.pepperapp28aprile.utilities.PazienteService;
import com.example.pepperapp28aprile.utilities.Phrases;
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
    private ArrayList<Persona.Game> gameList;
    private GridView gridView;
    private RecyclerView recyclerView;
    public static String id;
    public static final String FRAGMENT_TAG =  Globals.PlaceHolderFragmentGamesTag;
    private static RobotHelper robotHelper;
    private static QiContext  qiContext;
    private Future<Void> requestSay;
    private Future<String> listenFuture;
    private PazienteService pazienteService;
    public static Fragment newInstance(int index,String idPaziente) {
        id = idPaziente;
        PlaceholderFragmentGames fragment = new PlaceholderFragmentGames();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PlaceholderFragmentGames(){

    }

    public static Fragment newInstance(String idPaziente) {
        id = idPaziente;
        PlaceholderFragmentGames fragment = new PlaceholderFragmentGames();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

    }




    private void getPazienteById(String idPaziente){
        new DataManager(this.getContext(),"pazienti",idPaziente,new DataManager.onDownloadDataListener() {
            @Override
            public void onDataSuccess(Persona paziente) {
                GameProfileActivity.gameArrayList = paziente.getEsercizi();
                GameListAdapter adapter = new GameListAdapter(getActivity(), R.layout.category_card, paziente,(GameProfileActivity) getActivity());
                gridView.setAdapter(adapter);
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
        pazienteService = new PazienteService(getContext());
        View fragmentLayout = null;

        fragmentLayout = inflater.inflate(R.layout.game_list_grid, container, false);
            gameList = new ArrayList<>();
            gridView = (GridView) fragmentLayout.findViewById(R.id.grid);

            Persona paziente = pazienteService.getPazienteById(id);
            paziente.setEserciziList(pazienteService.getGameListByEldId(id,true));


        GameProfileActivity.gameArrayList = paziente.getEsercizi();
        GameListAdapter adapter = new GameListAdapter(getActivity(), R.layout.category_card, paziente,(GameProfileActivity) getActivity());
        gridView.setAdapter(adapter);

            //getPazienteById(id);

        return fragmentLayout;
    }


    //TODO: Vedere qua, questo viene eseguito quando fragmentLayout e' bello pronto e visualizzato
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        robotHelper = ((GameProfileActivity)getActivity()).getRobotHelper();
        qiContext = ((GameProfileActivity)getActivity()).qiContext;

        // Operazione da eseguire dopo che il layout del fragment è stato restituito
        // Puoi inserire qui il tuo codice per l'operazione desiderata
            // E se lo siamo, verifico se tutte le componenti sono state visualizzate
            requestSay = robotHelper.say(Phrases.menuGame).andThenCompose(result -> {
                listenFuture = robotHelper.setListener(((GameProfileActivity)getActivity()).getGameTitleList(), qiContext);
                return listenFuture.andThenCompose(heardPhrase -> {
                    // Usa la frase ascoltata come necessario
                    Log.d("Pepper4RSA", "Pepper heard: " + heardPhrase);

                    ((GameProfileActivity)getActivity()).viewGameListByVoice(null,heardPhrase);

                    // Ritorna un Future completato per continuare la catena
                    listenFuture.cancel(true);
                    listenFuture.requestCancellation();
                    return Future.of(null);
                }).thenConsume(future -> { //Verifico se c'e' un problema con l'esecuzione del future per l'ascolto
                    if (future.hasError()) {
                        Log.e("Pepper4RSA", "Error while speaking or listening: ", future.getError());
                    }
                });
            });
    }
/*
    @Override
    public void onResume() {
        super.onResume();
        requestSay.cancel(true);
        requestSay.requestCancellation();
        requestSay = robotHelper.say(Phrases.menuGame,getActivity()).thenConsume(future -> { //Verifico se c'e' un problema con l'esecuzione del future per l'ascolto
            if (future.hasError()) {
                Log.e("Pepper4RSA", "Error while speaking or listening: ", future.getError());
            }
        });
        // Verifica se il fragment è visibile e se è il fragment attuale
        if (isVisible() && getActivity() instanceof GameProfileActivity) {


                // Codice per far parlare Pepper
                Future<Void> requestSay = robotHelper.say(Phrases.menuGame).andThenCompose(result -> {
                    Future<String> listenFuture = robotHelper.setListener(((GameProfileActivity) getActivity()).getGameTitleList(), qiContext);
                    return listenFuture.andThenCompose(heardPhrase -> {
                        // Usa la frase ascoltata come necessario
                        Log.d("Pepper4RSA", "Pepper heard: " + heardPhrase);

                        ((GameProfileActivity) getActivity()).viewGameListByVoice(null, heardPhrase);

                        // Ritorna un Future completato per continuare la catena
                        listenFuture.cancel(true);
                        listenFuture.requestCancellation();
                        return Future.of(null);
                    });
                }).thenConsume(future -> { //Verifico se c'e' un problema con l'esecuzione del future per l'ascolto
                    if (future.hasError()) {
                        Log.e("Pepper4RSA", "Error while speaking or listening: ", future.getError());
                    }
                });
        }
    }*/

}

