package com.example.pepperapp28aprile.request;

import java.util.Collection;

import com.example.pepperapp28aprile.models.*;
import com.example.pepperapp28aprile.request.core.CRUD;
import com.example.pepperapp28aprile.request.core.CRUDRequest;
import com.example.pepperapp28aprile.request.core.RequestListener;

public class RoomRequest extends CRUDRequest<Room> implements CRUD<Room> {

    @Override
    public void create(Room model, RequestListener<Room> requestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void read(long id, RequestListener<Room> requestListener) {

    }

    @Override
    public void readAll(RequestListener<Collection<Room>> requestListener) {
        super.readAll("room/list", requestListener, Room.class);
    }

    @Override
    public void update(Room model, RequestListener<Room> requestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> requestListener) {
        throw new UnsupportedOperationException();
    }
}
