package com.example.pepperapp28aprile;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;


public class CustomScrollView extends ScrollView {
    private long lastScrollUpdate = -1;

    public CustomScrollView(Context context) { super(context);}
    public CustomScrollView(Context context, AttributeSet attrs) { super(context,attrs);}
    public CustomScrollView (Context context, AttributeSet attrs, int default_style){super(context, attrs, default_style);}

    private class ScrollStateHandler implements Runnable {

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastScrollUpdate) > 100) {
                lastScrollUpdate = -1;
                onScrollEnd();
            } else {
                postDelayed(this, 100);
            }
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (lastScrollUpdate == -1) {
            onScrollStart();
            postDelayed(new ScrollStateHandler(), 100);
        }

        lastScrollUpdate = System.currentTimeMillis();
    }

    private void onScrollStart() {
        // Feel Free to add something here
    }

    private void onScrollEnd() {
        this.fullScroll(View.FOCUS_UP); // Each time user finish with scrolling it will scroll to top
    }}