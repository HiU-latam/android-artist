package com.hiulatam.hiu.hiuartist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.an.customfontview.CustomButton;
import com.hiulatam.hiu.hiuartist.R;
import com.hiulatam.hiu.hiuartist.common.Config;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/15/17.
 */

public class VideoSaveConfirmationDialogFragment extends DialogFragment {

    private static final String TAG = "VideoSaveConfirmationDialogFragment";

    private CustomButton buttonPositive, buttonNegative;


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
        return inflater.inflate(R.layout.video_save_confirmation_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        buttonPositive = (CustomButton) view.findViewById(R.id.customButtonOK);
        buttonNegative = (CustomButton) view.findViewById(R.id.customButtonCancel);


        buttonNegative.setOnClickListener(onClickListener);
        buttonPositive.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.customButtonCancel:
                    getDialog().dismiss();
                    getActivity().finish();
                    break;
                case R.id.customButtonOK:
                    getDialog().dismiss();
                    getActivity().finish();
                    break;
            }
        }
    };
}
