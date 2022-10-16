package com.example.pepperapp28aprile;


import android.util.Log;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;

import java.util.List;

class MyQiChatExecutorChiamaVocal extends BaseQiChatExecutor {
    private final QiContext qiContext;

    MyQiChatExecutorChiamaVocal(QiContext context) {
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
            case "gioca" : {
                GameProfileActivity.viewGameList = params.get(1);
                break;
            }
            case "termina" :{
                PepperLissenerActivity.risposta = params.get(1);
            }
        }
        }

    @Override
    public void stop() {
        // This is called when chat is canceled or stopped
        Log.i("MyQiChatExecutor", "QiChatExecutor stopped");
    }


}