package com.example.pepperapp28aprile.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class VitalData extends Model {

    private int bpm;
    @SerializedName("body_temperature")
    private float bodyTemperature;
    @SerializedName("min_body_temperature")
    private int minBodyPressure;
    @SerializedName("max_body_pressure")
    private int maxBodyPressure;
    @SerializedName("blood_oxygenation")
    private int bloodOxygenation;
    private Inmate inmate;


    public void VitalData(){

    }

    public static final Creator<VitalData> CREATOR = new Creator<VitalData>() {
        @Override
        public VitalData createFromParcel(Parcel in) {
            return new VitalData(in);
        }

        @Override
        public VitalData[] newArray(int size) {
            return new VitalData[size];
        }
    };

    public VitalData(Parcel in) {
        super(in);
        this.bpm = in.readInt();
        this.bodyTemperature = in.readFloat();
        this.minBodyPressure = in.readInt();
        this.maxBodyPressure = in.readInt();
        this.bloodOxygenation = in.readInt();
        this.inmate = in.readParcelable(Inmate.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.bpm);
        dest.writeFloat(this.bodyTemperature);
        dest.writeInt(this.minBodyPressure);
        dest.writeInt(this.maxBodyPressure);
        dest.writeInt(this.bloodOxygenation);
        dest.writeParcelable(this.inmate, flags);
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public float getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(float bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public int getMinBodyPressure() {
        return minBodyPressure;
    }

    public void setMinBodyPressure(int minBodyPressure) {
        this.minBodyPressure = minBodyPressure;
    }

    public int getMaxBodyPressure() {
        return maxBodyPressure;
    }

    public void setMaxBodyPressure(int maxBodyPressure) {
        this.maxBodyPressure = maxBodyPressure;
    }

    public int getBloodOxygenation() {
        return bloodOxygenation;
    }

    public void setBloodOxygenation(int bloodOxygenation) {
        this.bloodOxygenation = bloodOxygenation;
    }

    public Inmate getInmate() {
        return inmate;
    }

    public void setInmate(Inmate inmate) {
        this.inmate = inmate;
    }
}
