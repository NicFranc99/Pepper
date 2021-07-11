package com.example.pepperapp28aprile.models;

import android.os.Parcel;

public class Inmate extends Model{

    private String name;
    private String surname;
    private String cf;
    private String birthDate;

    public Inmate (){

    }

    public static final Creator<Inmate> CREATOR = new Creator<Inmate>() {
        @Override
        public Inmate createFromParcel(Parcel in) {
            return new Inmate(in);
        }

        @Override
        public Inmate[] newArray(int size) {
            return new Inmate[size];
        }
    };

    public Inmate(Parcel in) {
        super(in);
        this.name = in.readString();
        this.surname = in.readString();
        this.cf = in.readString();
        this.birthDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeString(this.surname);
        dest.writeString(this.cf);
        dest.writeString(this.birthDate);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
