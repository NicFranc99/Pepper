package com.example.pepperapp28aprile;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SelectionGameModeFragment extends Fragment {

    private static String idPaziente;
    private GridView gridView;
    public static String FRAGMENT_TAG = "SELECTION_GAME_MODE_FRAGMENT";

    public SelectionGameModeFragment() {
        // Required empty public constructor
    }

    public SelectionGameModeFragment(String idPaziente) {
        // Required empty public constructor
        this.idPaziente = idPaziente;
    }

    public static SelectionGameModeFragment newInstance(String id) {
        SelectionGameModeFragment fragment = new SelectionGameModeFragment();
        idPaziente = id;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gonfia il layout principale
        View fragmentLayout = inflater.inflate(R.layout.fragment_selection_game_mode, container, false);

        // Recupera il GridView dal layout
        gridView = fragmentLayout.findViewById(R.id.grid);

        // Recupera i bottoni dal layout
        Button gameMode1Button = fragmentLayout.findViewById(R.id.game_mode_button_single);
        Button gameMode2Button = fragmentLayout.findViewById(R.id.game_mode_button_multy);

        // Imposta il listener per il click del bottone per il Single Mode
        gameMode1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Single Mode Button Clicked", Toast.LENGTH_SHORT).show();
                GameProfileActivity gameProfileActivity = (GameProfileActivity)getActivity();
                FragmentManager fragmentManager = gameProfileActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment currentFragment = fragmentManager.findFragmentByTag(SelectionGameModeFragment.FRAGMENT_TAG);

// Nascondi il fragment corrente se è presente
                if (currentFragment != null) {
                    fragmentTransaction.hide(currentFragment);
                }

                fragmentTransaction.add(R.id.profile,  PlaceholderFragmentGames.newInstance(idPaziente), PlaceholderFragmentGames.FRAGMENT_TAG);

                fragmentTransaction.addToBackStack(PlaceholderFragmentGames.FRAGMENT_TAG);
                fragmentTransaction.commit();
            }
        });

        // Imposta il listener per il click del bottone per il Multiplayer Mode
        gameMode2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Multiplayer Mode Button Clicked", Toast.LENGTH_SHORT).show();
                gameMode1Button.setBackgroundColor(getResources().getColor(R.color.red));
            }
        });

        // Restituisci il layout principale
        return fragmentLayout;
    }


}
