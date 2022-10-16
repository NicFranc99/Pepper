package com.example.pepperapp28aprile;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.example.pepperapp28aprile.animations.Animations;
import com.example.pepperapp28aprile.interfacedir.onCLickListener;
import com.example.pepperapp28aprile.utilities.DataManager;

import java.util.ArrayList;

public class GameActivity extends FragmentActivity{

    private RecyclerView recyclerView;
    private ArrayList<RecyclerAnswers> recyclerAnswersArrayList;
    private TextToSpeech t1;
    private final Bundle extras = new Bundle();
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_game);
        setTItleUi();

        if (savedInstanceState == null) {
            Fragment fragment = new GameExplanationFragment();
            fragment.setArguments(getIntent().getExtras()); // delego al fragment la ricezzione dei dati
            getSupportFragmentManager().beginTransaction().replace(R.id.container_game_com, fragment).commit();
        }
    }

    private void setTItleUi() {
        ImageView iconCategori = findViewById(R.id.img_icon_cat);
        TextView txtCat = findViewById(R.id.txt_cat);
        TextView txtTitleGame = findViewById(R.id.txt_title);

        ImageView exit=findViewById(R.id.exit2);
        Animations.focusView(exit, Animations.SCALING_BUTTON_DIALOG);
        exit.setVisibility(View.VISIBLE);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esci();
            }
        });
        int position = getIntent().getExtras().getInt("item");
        Persona paziente = (Persona)getIntent().getSerializableExtra("paziente");

        Persona.Game g = paziente.getEsercizi().get(position);
        iconCategori.setBackground(getDrawable(g.getNameIcon()));
        txtCat.setText(g.getTitleCategory());
        txtTitleGame.setText(g.getTitleGame());
    }

    @Override
    public void onBackPressed() {

        esci();
    }

    private void esci() {
        ExitDialogFragment exit = new ExitDialogFragment(GameActivity.this);
        exit.setText(getResources().getString(R.string.msg_exit_session));
        exit.setIcon(getDrawable(R.drawable.answer));
        exit.setListener(new onCLickListener() {
            @Override
            public void onClickExit() {
                exit.dismiss();
                finish();
            }

            @Override
            public void onClickContinue() {
                exit.dismiss();
            }
        });
        exit.show();
    }
    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}