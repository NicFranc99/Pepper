package com.example.pepperapp28aprile.provider;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class RequestProvider {
    private RequestQueue requestQueue;

    RequestProvider(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void addToQueue(Request request) {
        requestQueue.add(request);
    }
}
