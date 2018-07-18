package com.hiulatam.hiu.hiuartist.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Shiny Solutions on 7/3/18.
 */

public class InstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "InstanceIDService - ";

    public InstanceIDService(){
        super();
    }

    @Override
    public void onTokenRefresh(){
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();


    }
}
