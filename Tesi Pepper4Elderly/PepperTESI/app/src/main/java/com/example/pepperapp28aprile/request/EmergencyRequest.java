package com.example.pepperapp28aprile.request;

import android.util.Log;

import com.android.volley.Request;

import com.example.pepperapp28aprile.CheckMattutino;
import com.example.pepperapp28aprile.provider.AdapterProvider;
import com.example.pepperapp28aprile.provider.Providers;
import com.example.pepperapp28aprile.request.core.*;
import com.example.pepperapp28aprile.models.*;
import com.example.pepperapp28aprile.Constants;
import com.example.pepperapp28aprile.adapter.*;


import java.util.Collection;

/*
import it.uniba.di.sysag.pepper4rsa.MainActivity;
import it.uniba.di.sysag.pepper4rsa.utils.Constants;
import it.uniba.di.sysag.pepper4rsa.utils.adapter.Adapter;
import it.uniba.di.sysag.pepper4rsa.utils.models.Emergency;
import it.uniba.di.sysag.pepper4rsa.utils.provider.AdapterProvider;
import it.uniba.di.sysag.pepper4rsa.utils.provider.Providers;
import it.uniba.di.sysag.pepper4rsa.utils.request.core.CRUD;
import it.uniba.di.sysag.pepper4rsa.utils.request.core.CRUDRequest;
import it.uniba.di.sysag.pepper4rsa.utils.request.core.ObjectRequest;
import it.uniba.di.sysag.pepper4rsa.utils.request.core.RequestExceptionFactory;
import it.uniba.di.sysag.pepper4rsa.utils.request.core.RequestListener;*/

public class EmergencyRequest extends CRUDRequest<Emergency> implements CRUD<Emergency> {
    @Override
    public void create(Emergency model, RequestListener<Emergency> requestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void read(long id, RequestListener<Emergency> requestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readAll(RequestListener<Collection<Emergency>> requestListener) {
        super.readAll("emergency/", requestListener, Emergency.class);
    }

    @Override
    public void update(Emergency model, RequestListener<Emergency> requestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> requestListener) {
        throw new UnsupportedOperationException();
    }

    public void setAsDone(long id, RequestListener<Boolean> requestListener){

        String url = String.format("emergency/done?id=%d", id);
        ObjectRequest request = new ObjectRequest(Request.Method.POST, String.format("%s/%s", Constants.SERVER_HOST, url), null,
                response -> {
                    Log.d(CheckMattutino.CONSOLE_TAG, response.toString());
                    requestListener.successResponse(true);
                },
                error -> {
                    Log.d(CheckMattutino.CONSOLE_TAG, error.toString());
                    requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                });

        Providers.getRequestProvider().addToQueue(request);
    }

    public void getNext(RequestListener<Emergency> requestListener){
        Adapter<Emergency> adapter = AdapterProvider.getAdapterFor(Emergency.class);
        try {
            ObjectRequest request = new ObjectRequest(Request.Method.GET, String.format("%s/emergency/next", Constants.SERVER_HOST), null,
                    response -> {
                        if(response.length() == 0){
                            requestListener.successResponse(null);
                        }
                        else {
                            Log.d(CheckMattutino.CONSOLE_TAG, response.toString());
                            Emergency emergency = adapter.fromJSON(response, Emergency.class);
                            requestListener.successResponse(emergency);
                        }
                    },
                    error -> {
                        requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                    });

            Providers.getRequestProvider().addToQueue(request);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
