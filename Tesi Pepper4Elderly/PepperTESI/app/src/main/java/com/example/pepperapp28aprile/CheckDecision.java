package com.example.pepperapp28aprile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pepperapp28aprile.map.LocalizeAndMapHelper;
import com.example.pepperapp28aprile.map.RobotHelper;

public class CheckDecision extends Fragment {



    private Button buttonStartLocalize;
    private Button buttonStartService;
    private RobotHelper robotHelper;
    public static CheckMattutino cm;
    public static NavigationFragment navigationFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        this.cm = (CheckMattutino) getActivity();
        robotHelper = cm.getRobotHelper();

        System.out.println("onCreateView");
        // checkMattutino.getQiContext();

        View view = inflater.inflate(R.layout.fragment_check_decision, container, false);

        System.out.println("onCreateView");
        // checkMattutino.getQiContext();

        robotHelper.say(getString(R.string.starting_phrase));
        buttonStartLocalize = view.findViewById(R.id.buttonStartLocalize);
        buttonStartService = view.findViewById(R.id.buttonStartService);


        robotHelper.localizeAndMapHelper.addOnFinishedLocalizingListener(result -> {
            robotHelper.releaseAbilities();
            cm.runOnUiThread(() -> {
                if (result == LocalizeAndMapHelper.LocalizationStatus.LOCALIZED) {
                    robotHelper.localizeAndMapHelper.removeOnFinishedLocalizingListeners();
                    robotHelper.say("Localizzazione Terminata");
                    Log.d(CheckMattutino.CONSOLE_TAG, "Localized");
                    cm.setLocalized(true);
                    buttonStartService.setEnabled(true);
                    buttonStartService.setAlpha(1);
                    buttonStartService.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_goto_frame, 0, 0);
                    buttonStartLocalize.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icn_localize_robot_burgermenu_oklocation, 0, 0);
                } else if (result == LocalizeAndMapHelper.LocalizationStatus.MAP_MISSING) {
                    robotHelper.localizeAndMapHelper.removeOnFinishedLocalizingListeners();
                    robotHelper.say("Mappa mancante");
                    Log.d(CheckMattutino.CONSOLE_TAG, "Map_Missing");
                } else if (result == LocalizeAndMapHelper.LocalizationStatus.FAILED) {
                    robotHelper.say("Localizzazione Fallita, riproviamoci");
                    Log.d(CheckMattutino.CONSOLE_TAG, "Failed");
                } else {
                    Log.d(CheckMattutino.CONSOLE_TAG, "onViewCreated: Unable to localize in Map");
                }
            });
        });

        buttonStartLocalize.setOnClickListener(v -> {
            if(robotHelper.askToCloseIfFlapIsOpened(cm)){
                Log.d(CheckMattutino.CONSOLE_TAG, "Flat opened");
            }
            else {
                Log.d(CheckMattutino.CONSOLE_TAG, "Localize Button");
                cm.startLocalizing();
            }
        });


        /***ATTENZIONE 2 LINEE DI TEST **
        cm.setLocalized(true);
        buttonStartService.setEnabled(true);
        /*** ***/

        buttonStartService.setEnabled(true);

        buttonStartService.setOnClickListener(v -> {
            System.out.println("schiacciato");
            if(robotHelper.askToCloseIfFlapIsOpened(cm)){
                Log.d(CheckMattutino.CONSOLE_TAG, "Flat opened");
            }
            else{
                if(cm.isLocalized() ) {
                    Log.d(CheckMattutino.CONSOLE_TAG, "Start Service Button");


                    System.out.println("PRIMA SET FRAGMENT NAVIGATION");
                    //this.fragmentManager = getSupportFragmentManager();

                    NavigationFragment navigationFragment = new NavigationFragment();
                    this.navigationFragment = navigationFragment;
                    System.out.println("localized");

                    cm.setFragment(navigationFragment);
                    //this.getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, navigationFragment).addToBackStack(NavigationFragment.FRAGMENT_TAG).commit();
                }
                else{
                    robotHelper.say("localìzzami prima di iniziare il servizio");
                }
            }
        });
        return view;
    }

}
