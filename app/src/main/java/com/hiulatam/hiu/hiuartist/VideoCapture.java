package com.hiulatam.hiu.hiuartist;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraDevice;
import android.media.CamcorderProfile;
import android.media.CameraProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.hiulatam.hiu.hiuartist.common.Config;
import com.hiulatam.hiu.hiuartist.fragments.RecordVideoFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shiny Solutions
 * Created on 7/21/18.
 */

public class VideoCapture extends Activity {
    private static final String TAG = "VideoCapture - ";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_capture_activity);

        getFragmentManager().beginTransaction().add(R.id.content, new RecordVideoFragment()).commit();

    }

    private boolean checkCameraHardware(){
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return  true;
        }else{
            return false;
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
