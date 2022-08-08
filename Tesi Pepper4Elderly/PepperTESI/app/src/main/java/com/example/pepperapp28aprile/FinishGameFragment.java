package com.example.pepperapp28aprile;

import android.animation.Animator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.pepperapp28aprile.utilities.DataManager;
import com.example.pepperapp28aprile.utilities.RisultatiManager;

public class FinishGameFragment extends Fragment {
    private final RisultatiManager risultatiManager;
    private final Persona.Game game;
    private int positions;
    private View v;

    public FinishGameFragment(Persona.Game game, RisultatiManager risultatiManager){
        this.risultatiManager = risultatiManager;
        // this.positions=positions;
        this.game = game;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.finish_fragment, container, false);
        //LottieAnimationView finish = v.findViewById(R.id.finishanimations);
        //TextView testo = v.findViewById(R.id.testofinish);
        //testo.setVisibility(View.INVISIBLE);

//        String s="Inizio Giochi: "+risultatiManager.getStartGameTimeClock()+"\nFine gioco: "+risultatiManager.getStopGameTimeClock();
//        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
//        Log.i("TEMPOGIOCO",s);

        v.findViewById(R.id.btnStatistiche).setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().remove(FinishGameFragment.this)
                .add(container.getId(), new StatisticheFragment(risultatiManager)).commit());

        v.findViewById(R.id.btnMenuPrincipale).setOnClickListener(v -> getActivity().finish());

        /*finish.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Animation aniFade = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
                testo.startAnimation(aniFade);
                testo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });*/

        new DataManager(getContext(), game, risultatiManager, new DataManager.onUploadDataListener() {
            @Override
            public void onDataSuccess(String s) {
                Log.i("RISULTATI", s);
            }

            @Override
            public void onDataFailed() {

            }
        });
        return v;
    }
}
