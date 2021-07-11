package com.example.pepperapp28aprile.provider;

import android.content.Context;

public final class Providers {
    private static Context applicationContext;
    private static RequestProvider requestProvider;

    public static void init(Context applicationContext) {
        Providers.applicationContext = applicationContext;
        requestProvider = new RequestProvider(applicationContext);
    }

    public static RequestProvider getRequestProvider() {
        return requestProvider;
    }

    public static String getStringFromApplicationContext(int stringId) {
        return applicationContext.getString(stringId);
    }
}