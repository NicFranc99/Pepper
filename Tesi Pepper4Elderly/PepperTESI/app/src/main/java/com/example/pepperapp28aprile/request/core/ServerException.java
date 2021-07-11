package com.example.pepperapp28aprile.request.core;

import com.android.volley.VolleyError;

public class ServerException extends RequestException {
    public ServerException(VolleyError volleyError, String message) {
        super(volleyError, message);
    }
}
