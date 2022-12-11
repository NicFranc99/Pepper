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
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.pepperapp28aprile.interfacedir.onCLickListener;
import com.example.pepperapp28aprile.utilities.DataManager;
import com.example.pepperapp28aprile.utilities.RisultatiManager;

import java.util.List;

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
        LottieAnimationView finish = v.findViewById(R.id.finishanimations);
        TextView testo = v.findViewById(R.id.testofinish);
        testo.setVisibility(View.INVISIBLE);

//        String s="Inizio Giochi: "+risultatiManager.getStartGameTimeClock()+"\nFine gioco: "+risultatiManager.getStopGameTimeClock();
//        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
//        Log.i("TEMPOGIOCO",s);

        showRestartGame();

        v.findViewById(R.id.btnStatistiche).setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().remove(FinishGameFragment.this)
                .add(container.getId(), new StatisticheFragment(risultatiManager)).commit());

        v.findViewById(R.id.btnMenuPrincipale).setOnClickListener(v -> getActivity().finish());

        finish.addAnimatorListener(new Animator.AnimatorListener() {
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
        });

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

    /**
     * Se il numero di risposte errate e' maggiore o uguale al numero di risposte possibili - 1 mostro dialogView per ripetere il gioco
     *  domanda con 2 risposte =
     *  1 errore non chiedo
     *  2 errore chiedo
     *  3 errore chiedo
     *  domanda con 4 risposte =
     *  1 errore non chiedo
     *  2 errore non chiedo
     *  3 errore non chiedo
     *  4 errore chiedo
     */
    private void showRestartGame(){
        for(int i=0;i<=game.getListaDomandeGioco().size()-1;i++){
            if( game.getListaDomandeGioco().get(i).getListaRispose().size() <= risultatiManager.getListError().get(i)){
                showRestartDialog();
                break;
            }
        }

    }

    private void showRestartDialog() {
        ExitDialogFragment exit = new ExitDialogFragment(getContext(),"Ripeti","Continua");
        exit.setText(getResources().getString(R.string.msg_repeat_session));
        exit.setIcon(getContext().getDrawable(R.drawable.answer));
        exit.setListener(new onCLickListener() {
            @Override
            public void onClickExit() { //Clicco su ripeti
                exit.dismiss();
                GameActivity activity = (GameActivity)getActivity();

                Fragment fragment = new GameExplanationFragment();
                fragment.setArguments(activity.getIntent().getExtras()); // delego al fragment la ricezzione dei dati
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_game_com, fragment).commit();
            }

            @Override
            public void onClickContinue() { //Clicco su non voglio ripetere
                exit.dismiss();
            }
        });
        exit.show();
    }
}
