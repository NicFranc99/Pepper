package com.example.pepperapp28aprile.request.core;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ArrayRequest extends JsonRequest<JSONArray> {
    private String authToken;

    public ArrayRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
    }

    public ArrayRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener, String authToken) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
        this.authToken = authToken;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<>();

        map.put("Expect", "application/json");
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
        if (authToken != null) {
            map.put("Authorization", authToken);
        }

        return map;
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
