package com.example.pepperapp28aprile.request.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.VolleyError;

public class RequestException extends Exception {
    private VolleyError volleyError;

    public RequestException(@Nullable VolleyError volleyError, String message) {
        super(message);
        this.volleyError = volleyError;
    }

    public VolleyError getVolleyError() {
        return volleyError;
    }

    public int getResponseCode() {
        return (volleyError != null && volleyError.networkResponse != null) ? volleyError.networkResponse.statusCode : 0;
    }

    @NonNull
    @Override
    public String toString() {
        return (super.toString() + " " + this.volleyError.toString());
    }
}
