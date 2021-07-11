package com.example.pepperapp28aprile.adapter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.example.pepperapp28aprile.models.*;

public class Adapter<T extends Model> {
    static final String JSON_DATETIME_FORMAT =  "yyyy-MM-dd HH:mm";
    static final String JSON_TIME_FORMAT =  "HH:mm:ss";

    private static final Gson jsonConverter = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat(JSON_DATETIME_FORMAT)
            .registerTypeAdapter(Emergency.class, new EmergencyAdapter())
            .create();

    public JSONObject toJSON(T object) throws JSONException {
            return new JSONObject(jsonConverter.toJson(object));
    }

    public T fromJSON(JSONObject json, Type classType) {
        return jsonConverter.fromJson(json.toString(), classType);
    }

    public Collection<T> fromJSONArray(JSONArray json, Type classType) throws JSONException {
        Collection<T> collection = new ArrayList<>();

        for(int i = 0; i < json.length(); i++) {
            JSONObject object = json.getJSONObject(i);
            collection.add(jsonConverter.fromJson(object.toString(), classType));
        }

        return collection;
    }
}
