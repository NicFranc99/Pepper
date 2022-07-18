package com.example.pepperapp28aprile.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.leanback.widget.BaseCardView;

import com.example.pepperapp28aprile.R;

/**
 * Questa classe serve solo per creare la grafica dei vari giochi
 */
public class CategoryCardView extends BaseCardView {

    public CategoryCardView(Context context) {
        super(context, null, R.style.CharacterCardStyle);
        LayoutInflater.from(getContext()).inflate(R.layout.category_card, this);
        setOnFocusChangeListener((v, hasFocus) -> {
            ImageView mainImage = findViewById(R.id.img_category);
            View container = findViewById(R.id.container);
            if (hasFocus) {
                container.setBackgroundResource(R.drawable.category_focused);
            } else {
                container.setBackgroundResource(R.drawable.category_not_focused);
            }
        });

        setFocusable(true);
    }

}
