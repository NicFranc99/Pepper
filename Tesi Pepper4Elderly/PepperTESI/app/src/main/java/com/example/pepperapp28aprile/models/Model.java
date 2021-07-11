package com.example.pepperapp28aprile.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public abstract class Model implements Parcelable {

    private long id;

    public Model() {}

    public Model(long id) {
        this.id = id;
    }

    public Model(Parcel in) {
        this.id = in.readLong();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        boolean result = false;

        if (obj instanceof Model) {
            Model model = (Model) obj;
            result = (this.id == model.getId());
        }

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
    }
}
