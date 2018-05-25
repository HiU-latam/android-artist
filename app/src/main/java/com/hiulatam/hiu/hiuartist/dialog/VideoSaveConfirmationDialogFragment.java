package com.hiulatam.hiu.hiuartist.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hiulatam.hiu.hiuartist.R;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/15/17.
 */

public class VideoSaveConfirmationDialogFragment extends DialogFragment {

    public static VideoSaveConfirmationDialogFragment newInstance(){
        VideoSaveConfirmationDialogFragment videoSaveConfirmationDialogFragment = new VideoSaveConfirmationDialogFragment();
        return videoSaveConfirmationDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.video_save_confirmation_dialog, container, false);
        return v;
    }
}
