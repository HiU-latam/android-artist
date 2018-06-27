package com.hiulatam.hiu.hiuartist.common;

import android.util.Log;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/28/17.
 */

public class Config {

    public static final String TAG = "com.hiulatam.hiu";

    public static final String KEY_CHARITY_ITEM_MODAL = "CHARITY_ITEM_MODAL";
    public static final String EXTRA_SEARCH_QUERY = "EXTRA_SEARCH_QUERY";

    public static final String ACTION_SEARCH_QUERY = "ACTION_SEARCH_QUERY";

    public static final String kAll = "All";


    public static void LogInfo(String message){
        Log.i(TAG, message);
    }


}
