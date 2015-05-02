package com.oilMap.client.auth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Francis on 2015-05-02.
 */
public class Auth implements Parcelable{

    private String id;

    private String name;

    private String gender;

    private String locale;

    public Auth(String id, String name, String gender, String locale) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.locale = locale;
    }

    public Auth(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
        this.gender = in.readString();
        this.locale = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.gender);
        parcel.writeString(this.locale);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Auth createFromParcel(Parcel paracel) {
            return new Auth(paracel);
        }

        public Auth[] newArray(int size) {
            return new Auth[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }
}
