package com.example.pepperapp28aprile;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import com.airbnb.lottie.LottieAnimationView;


public class AnswerDialogFragment extends Dialog {
    private static final long DURATION_ANIMATION = 4000;
    private static final long DURATION_START = 4500;
    private final typeDialog type;
    private String text = "";

    public enum typeDialog {
        CORRECT, WRONG, START,
    }

    private Context c;

    public AnswerDialogFragment(@NonNull Context context, typeDialog type) {
        super(context);
        this.c = context;
        this.type = type;

    }

    public AnswerDialogFragment(@NonNull Context context, typeDialog type, String text) {
        super(context);
        this.c = context;
        this.type = type;
        this.text = text;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.answer_dialog_fragment);
        LottieAnimationView animationView = findViewById(R.id.anViewDialog);
        TextView textView = findViewById(R.id.txtmsgdialog);

        @RawRes
        int animationFiles = R.raw.correnct;

        switch (type) {
            case CORRECT:
                animationFiles = R.raw.correnct;
                if (text.equals("")) {
                    text = getContext().getString(R.string.text_dialog_correct);
                }
                break;
            case WRONG:
                animationFiles = R.raw.wrong;
                if (text.equals("")) {
                    text = getContext().getString(R.string.text_dialog_wrong);
                }
                break;
            case START:
                animationFiles = R.raw.countdown;
                text = "";
                textView.setVisibility(View.GONE);
                break;

        }

        animationView.setAnimation(animationFiles);
        textView.setText(text);

    }

    @Override
    public void show() {

        super.show();

        if (type == typeDialog.CORRECT || type == typeDialog.WRONG) {
            new CountDownTimer(DURATION_ANIMATION, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    dismiss();

                }
            }.start();
        }

        if (type == typeDialog.START) {
            new CountDownTimer(DURATION_START, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    dismiss();

                }
            }.start();
        }

    }

}