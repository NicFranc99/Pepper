package com.example.pepperapp28aprile.provider;

import java.util.HashMap;
import java.util.Map;

import com.example.pepperapp28aprile.adapter.Adapter;
import com.example.pepperapp28aprile.models.*;


public final class AdapterProvider {
    private static final Map<Class<? extends Model>, Adapter<? extends Model>> adapters = new HashMap<>();

    static {
        adapters.put(Room.class, new Adapter<Room>());
        adapters.put(Emergency.class, new Adapter<Emergency>());
    }

    public static <T extends Model> Adapter<T> getAdapterFor(Class<T> modelClass) {
        return (Adapter<T>) adapters.get(modelClass);
    }
}
