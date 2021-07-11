package com.example.pepperapp28aprile.models;

import android.os.Parcel;

public class Bed extends Model{

    private Room room;
    private Inmate inmate;

    public Bed (){

    }

    public static final Creator<Bed> CREATOR = new Creator<Bed>() {
        @Override
        public Bed createFromParcel(Parcel in) {
            return new Bed(in);
        }

        @Override
        public Bed[] newArray(int size) {
            return new Bed[size];
        }
    };

    public Bed(Parcel in) {
        super(in);
        this.room = in.readParcelable(Room.class.getClassLoader());
        this.inmate = in.readParcelable(Inmate.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.room, flags);
        dest.writeParcelable(this.inmate, flags);
    }

    public Room getRoom() {
        return room;
    }

    public void setRoomId(Room room) {
        this.room = room;
    }

    public Inmate getInmate() {
        return inmate;
    }

    public void setInmateId(Inmate inmate) {
        this.inmate = inmate;
    }
}
