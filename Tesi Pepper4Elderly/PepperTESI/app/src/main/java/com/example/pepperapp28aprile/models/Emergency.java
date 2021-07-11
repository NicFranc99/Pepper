package com.example.pepperapp28aprile.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class Emergency extends Model{

    private int type;
    private boolean done;
    private int level;
    @SerializedName("env_data")
    private EnvData envData;
    @SerializedName("vital_signs")
    private VitalData vitalSigns;
    private String bedLabel;
    private String tags;
    private int pulsante;
    private String valBed;

    private String name;
    private String surname;
    private String id;
    public boolean isButtonEmergency;

    public Emergency(){

    }

    public String getValBed(){
        return valBed;
    }

    public String getName(){
        return name;
    }

    public String getElderId(){
        return  id;
    }

    public String getSurname(){
        return surname;
    }

    public void setDone(boolean done){
        this.done = done;
    }

    public boolean getDone(){
        return done;
    }

    public int getPulsante(){
        return pulsante;
    }

    public static final Creator<Emergency> CREATOR = new Creator<Emergency>() {
        @Override
        public Emergency createFromParcel(Parcel in) {
            return new Emergency(in);
        }

        @Override
        public Emergency[] newArray(int size) {
            return new Emergency[size];
        }
    };

    public Emergency(String bedLabel, String name, String surname, int pulsante, String valBed){
        this.name = name;
        this.surname = surname;
        this.bedLabel = bedLabel;
        this.pulsante = pulsante;
        this.valBed = valBed; //solo il numero del letto

        done = false;
        level = 0;
        type = 3;
        tags = "";
        envData = null;
        vitalSigns = null;

    }

    public Emergency(String bedLabel, String name, String surname, String valBed){
        this.name = name;
        this.surname = surname;
        this.bedLabel = bedLabel;
        this.valBed = valBed;

        done = false;
        level = 0;
        type = 3;
        tags = "";
        envData = null;
        vitalSigns = null;
        pulsante = 0;


    }

    public Emergency(String bedLabel, String name, String surname){
        this.name = name;
        this.surname = surname;
        this.bedLabel = bedLabel;

        done = false;
        level = 0;
        type = 3;
        tags = "";
        envData = null;
        vitalSigns = null;
        pulsante = 0;

        valBed = "costruttore che non lo setta";
    }

    public Emergency(String bedLabel, String name, String surname, int pulsante){
        this.name = name;
        this.surname = surname;
        this.bedLabel = bedLabel;
        this.pulsante = pulsante;

        done = false;
        level = 0;
        type = 3;
        tags = "";
        envData = null;
        vitalSigns = null;

        valBed = "costruttore che non lo setta";
    }

    public Emergency(Parcel in) {
        super(in);
        this.type = in.readInt();
        this.level = in.readInt();
        this.envData = in.readParcelable(EnvData.class.getClassLoader());
        this.vitalSigns = in.readParcelable(VitalData.class.getClassLoader());
        this.bedLabel = in.readString();
        this.tags = in.readString();


        valBed = "costruttore che non lo setta";
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.type);
        dest.writeInt(this.level);
        dest.writeParcelable(this.envData, flags);
        dest.writeParcelable(this.vitalSigns, flags);
        dest.writeString(this.bedLabel);
        dest.writeString(this.tags);
    }

    public void setId(String id){
        //System.out.println("IL CCCCCCCC " + id);
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public EnvData getEnvData() {
        return envData;
    }

    public void setEnvData(EnvData envData) {
        this.envData = envData;
    }

    public VitalData getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(VitalData vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public String getBedLabel() {
        return bedLabel;
    }

    public void setBedLabel(String bedLabel) {
        this.bedLabel = bedLabel;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String toString() {
        return name+" "+surname+" "+bedLabel+"done = " + done;
    }
}
