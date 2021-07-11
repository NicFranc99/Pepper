package com.example.pepperapp28aprile.models;

import android.os.Parcel;

public class EnvData extends Model{

    private int lux;
    private float voc;
    private float degree;
    private int humidity;
    private Room room;

    public EnvData (){

    }

    public static final class Thresholds {
        public static final int MIN_LUX = 401;
        public static final int MAX_LUX = 1000;
        public static final float MAX_VOC = 0.7f;
        public static final int MIN_DEGREE = 15;
        public static final int MAX_DEGREE = 30;
        public static final int MAX_HUMIDITY = 60;
    }

    public static final Creator<EnvData> CREATOR = new Creator<EnvData>() {
        @Override
        public EnvData createFromParcel(Parcel in) {
            return new EnvData(in);
        }

        @Override
        public EnvData[] newArray(int size) {
            return new EnvData[size];
        }
    };

    public EnvData(Parcel in) {
        super(in);
        this.lux = in.readInt();
        this.voc = in.readFloat();
        this.degree = in.readFloat();
        this.humidity = in.readInt();
        this.room = in.readParcelable(Room.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.lux);
        dest.writeFloat(this.voc);
        dest.writeFloat(this.degree);
        dest.writeInt(this.humidity);
        dest.writeParcelable(this.room, flags);
    }

    public int getLux() {
        return lux;
    }

    public void setLux(int lux) {
        this.lux = lux;
    }

    public float getVoc() {
        return voc;
    }

    public void setVoc(float voc) {
        this.voc = voc;
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
