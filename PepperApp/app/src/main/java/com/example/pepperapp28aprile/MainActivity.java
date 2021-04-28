package com.example.pepperapp28aprile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aldebaran.qi.sdk.QiSDK;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private String currentFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fragmentManager = getSupportFragmentManager();

        setFragment(new LoadingFragment());
        System.out.println("ciao");
        //setFragment(new MainMenuFragment());

    }

    @Override
    public void onUserInteraction() {
        if (getFragment() instanceof LoadingFragment) {
            setFragment(new MainMenuFragment());
            //countDownNoInteraction.start();
        } /*else {
            countDownNoInteraction.reset();
        }*/
    }

    public Fragment getFragment() {
        return fragmentManager.findFragmentByTag("currentFragment");
    }

    public void setFragment(Fragment fragment) {
        currentFragment = fragment.getClass().getSimpleName();
        String topicName = currentFragment.toLowerCase().replace("fragment", "");
        /*if (!(fragment instanceof LoadingFragment || fragment instanceof ProductSelectionFragment || fragment instanceof SplashScreenFragment)) {
            this.currentChatData.goToBookmarkNewTopic("init", topicName);
        }
        if (this.currentChatData != null) {
            if (fragment instanceof LoadingFragment || fragment instanceof SplashScreenFragment) {
                this.currentChatData.enableListeningAnimation(false);
            } else {
                this.currentChatData.enableListeningAnimation(true);
            }
        }*/
        System.out.println("starting fragment Transaction for fragment : " + fragment.getClass().getSimpleName());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_fade_in_right, R.anim.exit_fade_out_left, R.anim.enter_fade_in_left, R.anim.exit_fade_out_right);
        transaction.replace(R.id.placeholder, fragment, "currentFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public Integer getThemeId() {
       try {
            return getPackageManager().getActivityInfo(getComponentName(), 0).getThemeResource();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}