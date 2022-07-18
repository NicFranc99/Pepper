package com.example.pepperapp28aprile;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.leanback.widget.TitleView;

import com.example.pepperapp28aprile.animations.Animations;
import com.example.pepperapp28aprile.interfacedir.onCLickListener;


public class ExitDialogFragment extends Dialog {

    private final Context c;
    private onCLickListener l;
    private String textMessage;
    private Drawable image;

    public ExitDialogFragment(@NonNull Context context) {
        super(context);
        this.c = context;
        textMessage = context.getString(R.string.msg_exit_game);
        image = context.getDrawable(R.drawable.exit);
    }

    public void setListener(onCLickListener listener) {
        this.l = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_dialog_fragment);
        TextView txtmsgdialog = findViewById(R.id.txtmsgdialog);
        TitleView seticon = findViewById(R.id.iconexit);
        seticon.setBackground(image);
        txtmsgdialog.setText(textMessage);

        Button exit = findViewById(R.id.btn_exit);
        Button continua = findViewById(R.id.btn_continue);
        Animations.focusView(exit, Animations.SCALING_BUTTON_DIALOG);
        Animations.focusView(continua, Animations.SCALING_BUTTON_DIALOG);

        exit.setOnClickListener(v -> {
            if (l != null) {
                l.onClickExit();

            }
        });

        continua.setOnClickListener(v -> {
            if (l != null) {
                l.onClickContinue();
            }
        });

    }

    public void setText(String textMessage) {
        this.textMessage = textMessage;
    }

    public void setIcon(Drawable icon) {
        this.image = icon;
    }

}