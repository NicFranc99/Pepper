package com.example.pepperapp28aprile;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;

import java.util.List;

class MyQiChatExecutor extends BaseQiChatExecutor {
    private final QiContext qiContext;

    MyQiChatExecutor(QiContext context) {
        super(context);
        this.qiContext = context;
    }

    @Override
    public void runWith(List<String> params) {
        // This is called when execute is reached in the topic
        //animate(qiContext);
        switch (params.get(0)){
            case "chiamata" : {
                ProfileActivity.startWeb = params.get(1);
                System.out.println("chiamata" + params.get(1));
                break;
            }
        }
    }

    @Override
    public void stop() {
        // This is called when chat is canceled or stopped
        Log.i("MyQiChatExecutor", "QiChatExecutor stopped");
    }


}