package com.example.pepperapp28aprile.request.core;

import com.android.volley.Request;

import org.json.JSONException;

import java.util.Collection;

import com.example.pepperapp28aprile.Constants;
import com.example.pepperapp28aprile.adapter.Adapter;
import com.example.pepperapp28aprile.models.*;
import com.example.pepperapp28aprile.provider.AdapterProvider;
import com.example.pepperapp28aprile.provider.Providers;


public abstract class CRUDRequest<T extends Model> {

    /*protected void create(T model, String url, RequestListener<T> requestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);
        try {
            JSONObject jsonObject = adapter.toJSON(model);

            ObjectRequest request = new ObjectRequest(Request.Method.POST, String.format("%s/api/%s", Constants.SERVER_HOST, url), jsonObject,
                response -> {
                    T data = adapter.fromJSON(response, modelType);
                    requestListener.successResponse(data);
                    if (model instanceof User) {
                        ((User) model).setPassword(null);
                    }
                    else if (model instanceof Restaurateur) {
                        ((Restaurateur) model).setPassword(null);
                    }
                },
                error -> {
                    requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                }, ((needToken) ? Providers.getAuthProvider().getAuthToken() : null));

            Providers.getRequestProvider().addToQueue(request);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    protected void create(T model, String url, RequestListener<T> requestListener, Class<T> modelType, boolean needToken) {
        throw new UnsupportedOperationException();
    }


    protected void read(long id, String url, RequestListener<T> requestListener, Class<T> modelType) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);

        ObjectRequest request = new ObjectRequest(Request.Method.GET, String.format("%s/api/%s/%d", Constants.SERVER_HOST, url, id), null,
            response -> {
                T data = adapter.fromJSON(response, modelType);
                requestListener.successResponse(data);
            },
            error -> {
                requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
            });

        Providers.getRequestProvider().addToQueue(request);
    }

    protected void readAll(String url, RequestListener<Collection<T>> requestListener, Class<T> modelType) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);

        ArrayRequest request = new ArrayRequest(Request.Method.GET, String.format("%s/%s", Constants.SERVER_HOST, url), null,
            response -> {
                try {
                    Collection<T> collection = adapter.fromJSONArray(response, modelType);
                    requestListener.successResponse(collection);
                }
                catch (JSONException e) {
                    requestListener.errorResponse(new RequestException(null, e.getMessage()));
                }
            },
            error -> requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error)));

        Providers.getRequestProvider().addToQueue(request);
    }

    /*protected void update(T model, String url, RequestListener<T> requestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);

        try {
            JSONObject jsonObject = adapter.toJSON(model);

            ObjectRequest request = new ObjectRequest(Request.Method.PUT, String.format("%s/api/%s", Constants.SERVER_HOST, url), jsonObject,
                response -> {
                    T data = adapter.fromJSON(response, modelType);
                    requestListener.successResponse(data);
                    if (model instanceof User) {
                        ((User) model).setPassword(null);
                    }
                    else if (model instanceof Restaurateur) {
                        ((Restaurateur) model).setPassword(null);
                    }
                },
                error ->requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error)),
                           ((needToken) ? Providers.getAuthProvider().getAuthToken() : null));

            Providers.getRequestProvider().addToQueue(request);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    protected void update(T model, String url, RequestListener<T> requestListener, Class<T> modelType, boolean needToken) {
        throw new UnsupportedOperationException();
    }

    /*protected void delete(long id, String url, RequestListener<Boolean> RequestListener, boolean needToken) {
        ObjectRequest request = new ObjectRequest(Request.Method.DELETE, String.format("%s/api/%s", Constants.SERVER_HOST, url), null,
            response -> RequestListener.successResponse(true),
            error -> RequestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error)),
                ((needToken) ? Providers.getAuthProvider().getAuthToken() : null));

        Providers.getRequestProvider().addToQueue(request);
    }*/

    protected void delete(long id, String url, RequestListener<Boolean> RequestListener, boolean needToken) {
        throw new UnsupportedOperationException();
    }
}
