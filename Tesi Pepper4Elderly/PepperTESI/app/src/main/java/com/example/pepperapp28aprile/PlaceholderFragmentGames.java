package com.example.pepperapp28aprile;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
    private ArrayList<Persona.Game> gameList;
    private GridView gridView;
    private RecyclerView recyclerView;
    public static String id;
    public static final String FRAGMENT_TAG =  Globals.PlaceHolderFragmentGamesTag;

    public static Fragment newInstance(int index,String idPaziente) {
        id = idPaziente;
        PlaceholderFragmentGames fragment = new PlaceholderFragmentGames();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
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
                GameListAdapter adapter = new GameListAdapter(getActivity(), R.layout.category_card, paziente);
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

        View fragmentLayout = null;

        fragmentLayout = inflater.inflate(R.layout.game_list_grid, container, false);
            gameList = new ArrayList<>();
            gridView = (GridView) fragmentLayout.findViewById(R.id.grid);

        getPazienteById(id);



        return fragmentLayout;
    }

}

