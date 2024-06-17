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

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.example.pepperapp28aprile.map.RobotHelper;
import com.example.pepperapp28aprile.models.Categoria;
import com.example.pepperapp28aprile.utilities.DataManager;
import com.example.pepperapp28aprile.utilities.PazienteService;
import com.example.pepperapp28aprile.utilities.Phrases;

public class SelectionGameModeFragment extends Fragment {

    private static String idPaziente;
    private GridView gridView;
    public static String FRAGMENT_TAG = "SELECTION_GAME_MODE_FRAGMENT";
    private QiContext qiContext;
    private RobotHelper robotHelper;
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

        GameProfileActivity activity = (GameProfileActivity)getActivity();

        robotHelper = activity.getRobotHelper();
        qiContext = activity.getQiContext();

        // Gonfia il layout principale
        View fragmentLayout = inflater.inflate(R.layout.fragment_selection_game_mode, container, false);

        // Recupera il GridView dal layout
        gridView = fragmentLayout.findViewById(R.id.grid);

        // Recupera i bottoni dal layout
        Button gameMode1Button = fragmentLayout.findViewById(R.id.game_mode_button_single);
        Button gameMode2Button = fragmentLayout.findViewById(R.id.game_mode_button_multy);

        Future<Void> sayFuture = robotHelper.say(Phrases.selectGameMode);

        // Imposta il listener per il click del bottone per il Single Mode
        gameMode1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sayFuture.cancel(true);
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
                sayFuture.cancel(true);
               // Toast.makeText(getActivity(), "Multiplayer Mode Button Clicked", Toast.LENGTH_SHORT).show();
                PazienteService pazienteService = new PazienteService(getContext());
                Persona paziente = pazienteService.getPazienteById(idPaziente);

                paziente.setEserciziList(pazienteService.getGameListByEldId(idPaziente,false));

                Intent game = new Intent(getContext(), GameActivity.class);
                GameActivity gameActivity = new GameActivity();
                gameActivity.startGameActivity(paziente,0,getContext(),true);

               // getPazienteById(idPaziente);
            }
        });

        // Restituisci il layout principale
        return fragmentLayout;
    }

    private void getPazienteById(String idPaziente){
        new DataManager(this.getContext(),"pazienti",idPaziente,new DataManager.onDownloadDataListener() {
            @Override
            public void onDataSuccess(Persona paziente) {
                Intent game = new Intent(getContext(), GameActivity.class);
                GameActivity gameActivity = new GameActivity();
                gameActivity.startGameActivity(paziente,0,getContext(),true);
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
