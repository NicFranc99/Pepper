package com.example.pepperapp28aprile.request.core;

import com.android.volley.VolleyError;

public class UnauthorizedException extends RequestException {
    public UnauthorizedException(VolleyError volleyError, String message) {
        super(volleyError, message);
    }
}
