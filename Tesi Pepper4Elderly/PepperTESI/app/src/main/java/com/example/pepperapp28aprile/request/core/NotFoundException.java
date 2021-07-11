package com.example.pepperapp28aprile.request.core;

import com.android.volley.VolleyError;

public class NotFoundException extends RequestException {
    public NotFoundException(VolleyError volleyError, String message) {
        super(volleyError, message);
    }
}
