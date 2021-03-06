package com.hiulatam.hiu.hiuartist.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/8/17.
 */

public class CharityItemModal implements Parcelable {

    private String name;
    private String time;
    private String date;
    private String profilePicture;
    private String country;

    public CharityItemModal(){

    }

    protected CharityItemModal(Parcel in) {
        name = in.readString();
        time = in.readString();
        date = in.readString();
        profilePicture = in.readString();
        country = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(profilePicture);
        dest.writeString(country);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CharityItemModal> CREATOR = new Creator<CharityItemModal>() {
        @Override
        public CharityItemModal createFromParcel(Parcel in) {
            return new CharityItemModal(in);
        }

        @Override
        public CharityItemModal[] newArray(int size) {
            return new CharityItemModal[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
