package com.example.pepperapp28aprile.request.core;

public interface RequestListener<T> {
    void successResponse(T response);
    void errorResponse(RequestException error);
}
