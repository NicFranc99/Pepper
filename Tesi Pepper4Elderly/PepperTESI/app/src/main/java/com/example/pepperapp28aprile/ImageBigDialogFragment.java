package com.example.pepperapp28aprile;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ImageBigDialogFragment extends Dialog {
    private final String urlImage;
    private final Context context;

    public ImageBigDialogFragment(@NonNull Context context, String urlImage) {
        super(context);
        this.urlImage = urlImage;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_viewbig_fragment);
        ConstraintLayout cont = findViewById(R.id.contentetImage);
        ImageView imageView = findViewById(R.id.imagedom);
        Glide.with(context).load(urlImage).apply(new RequestOptions().override(1200, 1200))
                .placeholder(R.drawable.ic_loop).centerCrop().into(imageView);

    }
}
