package com.example.pepperapp28aprile;


import android.content.Intent;
import android.util.Log;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;

import java.util.List;

class TelepresenzaExecutor extends BaseQiChatExecutor {

    private static final String TAG = "MyQiChatExecutor";
    public static NavigationFragment navigationFr;


    TelepresenzaExecutor(QiContext context) {
        super(context);
    }

    @Override
    public void runWith(List<String> params) {
        // This is called when execute is reached in the topic
        Log.i(TAG, "Arm raised = " + params.get(0));
        NavigationFragment.startTele = true;

    }

    @Override
    public void stop() {
        // This is called when chat is canceled or stopped.
        Log.i(TAG, "execute stopped");
    }
}

