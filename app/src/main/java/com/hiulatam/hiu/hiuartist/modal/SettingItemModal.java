package com.hiulatam.hiu.hiuartist.modal;

import android.os.Parcel;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import android.annotation.SuppressLint;
import java.util.List;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/23/17.
 */

public class SettingItemModal extends ExpandableGroup<SettingsChildItemModal> {
    private static final String TAG = "SettingItemModal - ";

    private String name;
    private int imageDrawable;
    private List<Object> items;

    public SettingItemModal(String name, int imageDrawable, List<SettingsChildItemModal> items) {
        super(name, items);
        this.name = name;
        this.imageDrawable = imageDrawable;
    }

    public String getName() {
        return name;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }
}
