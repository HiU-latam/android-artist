package com.hiulatam.hiu.hiuartist.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/28/17.
 */

public class SettingsChildItemModal implements Parcelable {

    private static final String TAG = "SettingsChildItemModal - ";

    private String optionItem;

    public SettingsChildItemModal(String itemName) {
        optionItem = itemName;
    }

    protected SettingsChildItemModal(Parcel in) {
        optionItem = in.readString();
    }

    public String getOptionItem(){
        return optionItem;
    }

    @Override
    public boolean equals(Object o) {
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(optionItem);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SettingsChildItemModal> CREATOR = new Creator<SettingsChildItemModal>() {
        @Override
        public SettingsChildItemModal createFromParcel(Parcel in) {
            return new SettingsChildItemModal(in);
        }

        @Override
        public SettingsChildItemModal[] newArray(int size) {
            return new SettingsChildItemModal[size];
        }
    };
}
