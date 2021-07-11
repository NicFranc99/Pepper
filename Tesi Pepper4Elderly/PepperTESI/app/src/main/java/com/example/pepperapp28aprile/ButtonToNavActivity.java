package com.example.pepperapp28aprile;

import android.content.Intent;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;


public class ButtonToNavActivity extends RobotActivity implements RobotLifecycleCallbacks {

    public static NavigationFragment navFr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QiSDK.register(this, this);

        System.out.println("ButtonToNavActivity");
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        //Providers.init(getApplicationContext());

    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        CheckMattutino.ennesimo = true;
        Intent intent = new Intent(this, CheckMattutino.class);
        this.startActivity(intent);
    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }


}
