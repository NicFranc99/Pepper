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

import com.example.pepperapp28aprile.animations.Animations;
import com.example.pepperapp28aprile.utilities.VoiceManager;
import com.google.android.material.imageview.ShapeableImageView;

public class GameExplanationFragment extends Fragment {
    private View v;
    //private ShapeableImageView input1;
    private Button start;
    //private TextView metodoInput;
    private int position;
    //public Persona.Game.TypeInputGame chose;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.command_view, container, false);

        position = getActivity().getIntent().getExtras().getInt("item");
        Persona paziente = (Persona)getActivity().getIntent().getSerializableExtra("paziente");
        Persona.Game g = paziente.getEsercizi().get(position);

        TextView txtSpiegazione = v.findViewById(R.id.txt_spiegazione);
        txtSpiegazione.setText(g.getDescrizioneGioco());

        if (getContext().checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.RECORD_AUDIO }, 1);
        }

        //input1 = v.findViewById(R.id.input1);

       /* if (g.getInputType().size() == 2) {
            input1.setImageDrawable(getImage(g.getInputType().get(0)));
        }

        if (g.getInputType().size() == 1) {
            input1.setVisibility(View.GONE);
        }
*/
        start = v.findViewById(R.id.start);
        start.requestFocus();
        //metodoInput = v.findViewById(R.id.txtmetodoinput);

        //nimations.focusViewBackGround(getContext(), input1, Animations.SCALING_IMAGE);

        /*VoiceManager.getIstance(getContext()).play(g.getDescrizioneGioco() + "Scegli il metodo di immissione",
                VoiceManager.QUEUE_ADD);*/

        /*input1.setOnClickListener(v -> {

            iniziaGioco("di SELEZIONARE");
            chose = Persona.Game.TypeInputGame.SELEZIONE;

            VoiceManager.getIstance(getContext()).play("Hai sceldo di Selezionare con il telecomando la risposta",
                    VoiceManager.QUEUE_ADD);
            VoiceManager.getIstance(getContext()).play("Premi per iniziare a Giocare", VoiceManager.QUEUE_ADD);

        }); */

        start.setOnClickListener(v -> {
            AnswerDialogFragment start = new AnswerDialogFragment(getContext(), AnswerDialogFragment.typeDialog.START);
            start.show();
            start.setOnDismissListener(dialog -> {
                if (g instanceof Persona.Racconti) {
                    // Paziente.Racconti racconti =(Paziente.Racconti)g;
                    // Toast.makeText(getContext(), racconti.getTestoRacconto() ,
                    // Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager().beginTransaction().remove(GameExplanationFragment.this)
                            .commit();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(container.getId(), new TestoRaccontoFragment(g,position)).commit();

                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(GameExplanationFragment.this)
                            .commit();
                    //TODO: Se devo giocare a un gioco con il talk to speack, allora devo chiamare l'activity PepperLissenerActivity
                    if(g instanceof Persona.FinaliParole || g instanceof Persona.CombinazioniLettere){
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
