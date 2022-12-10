package com.example.pepperapp28aprile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.example.pepperapp28aprile.animations.Animations;
import com.example.pepperapp28aprile.map.RobotHelper;
import com.example.pepperapp28aprile.utilities.VoiceManager;
import com.google.android.material.imageview.ShapeableImageView;

public class GameExplanationFragment extends Fragment {
    private View v;
    private Button start;
    private int position;
    private GameActivity gameActivity;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.command_view, container, false);
        gameActivity = (GameActivity)getActivity();
        position = getActivity().getIntent().getExtras().getInt("item");
        Persona paziente = (Persona)getActivity().getIntent().getSerializableExtra("paziente");
        Persona.Game g = paziente.getEsercizi().get(position);
        TextView txtSpiegazione = v.findViewById(R.id.txt_spiegazione);
        txtSpiegazione.setText(g.getDescrizioneGioco());
        Future<Void> sayDescription = gameActivity.getRobotHelper().say(g.getDescrizioneGioco());
        if (getContext().checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.RECORD_AUDIO }, 1);
        }

        start = v.findViewById(R.id.start);
        start.requestFocus();
        start.setOnClickListener(v -> {
            sayDescription.cancel(true); //TODO: Bisogna stoppare il say di pepper per andare avanti col tread!
            AnswerDialogFragment start = new AnswerDialogFragment(getContext(), AnswerDialogFragment.typeDialog.START);
            start.show();
            start.setOnDismissListener(dialog -> {
                if (g instanceof Persona.Racconti) {

                    getActivity().getSupportFragmentManager().beginTransaction().remove(GameExplanationFragment.this)
                            .commit();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(container.getId(), new TestoRaccontoFragment(g,position)).commit();

                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(GameExplanationFragment.this)
                            .commit();

                    if(g instanceof Persona.CombinazioniLettere
                            || g instanceof Persona.FinaliParole
                            || g instanceof Persona.FluenzeVerbali
                            || g instanceof Persona.FluenzeFonologiche
                            || g instanceof Persona.FluenzeSemantiche){
                        Intent intent = new Intent(this.getActivity(),PepperLissenerActivity.class);
                        intent.putExtra("gamePosition", position);
                        intent.putExtra("game",g);
                        intent.putExtra("idContainer",container.getId());
                         //TODO: Vedere perche' ci mette tempo a terminare la gameActivity
                        this.startActivity(intent);
                   }
                    else
                   getActivity().getSupportFragmentManager().beginTransaction()
                           .add(container.getId(), new GameFragment(g,position)).commit();
                }
            });
        });
        //input1.requestFocus();
        return v;
    }

    private Drawable getImage(Persona.Game.TypeInputGame type) {
        Drawable img = getActivity().getResources().getDrawable(R.drawable.pad);
        if (type == Persona.Game.TypeInputGame.SCRITTURA) {
            img = getActivity().getResources().getDrawable(R.drawable.typing);
        }
        if (type == Persona.Game.TypeInputGame.VOCALE) {
            img = getActivity().getResources().getDrawable(R.drawable.microphone);
        }
        if (type == Persona.Game.TypeInputGame.SELEZIONE) {
            img = getActivity().getResources().getDrawable(R.drawable.pad);
        }
        return img;
    }

    private void iniziaGioco() {
        //input1.setVisibility(View.GONE);
        start.setVisibility(View.VISIBLE);
        //metodoInput.append(": \nhai scelto " + msg + " la risposta");
        start.requestFocus();
    }

  /*  @Override
    public void onPause() {
        super.onPause();
        VoiceManager.getIstance(getContext()).stop();
    }*/
}
