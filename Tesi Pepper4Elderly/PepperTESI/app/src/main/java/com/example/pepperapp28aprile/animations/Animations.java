package com.example.pepperapp28aprile.animations;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.example.pepperapp28aprile.R;


public class Animations {
    public static float SCALING_IMAGE = 1.4f; // aumento la dimensione
    public static float SCALING_BUTTON = 1.1f; // aumento la dimensione
    public static float SCALING_BUTTON_DIALOG = 1.25f; // aumento la dimensione

    public static void focusView(View view, float scalingFactor) {
        view.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                v.setScaleX(scalingFactor);
                v.setScaleY(scalingFactor);
            } else {
                float scalingFactor1 = 1f; // diminuisco la dimensione
                v.setScaleX(scalingFactor1);
                v.setScaleY(scalingFactor1);
            }
        });
    }

    public static void focusViewBackGround(Context c, View view, float scalingFactor) {
        view.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                v.setBackgroundColor(c.getColor(R.color.category_focused));
                v.setScaleX(scalingFactor);
                v.setScaleY(scalingFactor);
            } else {
                v.setBackgroundColor(c.getColor(R.color.category_no_focused));

                float scalingFactor1 = 1f; // diminuisco la dimensione
                v.setScaleX(scalingFactor1);
                v.setScaleY(scalingFactor1);
            }
        });
    }

    public static void blink(View card, Animation.AnimationListener l) {
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500); // You can manage the blinking time with this parameter
        animation.setStartOffset(100);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(3);
        animation.setAnimationListener(l);
        card.startAnimation(animation);

    }
}
