package com.hiulatam.hiu.hiuartist.fragments;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hiulatam.hiu.hiuartist.R;
import com.hiulatam.hiu.hiuartist.common.Config;
import com.wesley.camera2.fragment.Camera2Fragment;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Shiny Solutions
 * Created on 7/22/18.
 */

public class RecordVideoFragment extends Camera2Fragment {
    private static final String TAG = "RecordVideoFragment - ";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.record_video_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        ImageView capture = view.findViewById(R.id.camera_control);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCameraControlClick((ImageView) v);
            }
        });
    }

    @Override
    public Cam2Listener getCam2Listener() {
        return new Cam2Listener() {
            @Override
            public void onCameraException(CameraAccessException cae) {
                Config.LogInfo(TAG + "onCameraException - Error Message: " + cae.getMessage());
            }

            @Override
            public void onNullPointerException(NullPointerException npe) {
                Config.LogInfo(TAG + "onNullPointerException - Error Message: " + npe.getMessage());
            }

            @Override
            public void onInterruptedException(InterruptedException ie) {
                Config.LogInfo(TAG + "onInterruptedException - Error Message: " + ie.getMessage());
            }

            @Override
            public void onIOException(IOException ioe) {
                Config.LogInfo(TAG + "onIOException - Error Message: " + ioe.getMessage());
            }

            @Override
            public void onConfigurationFailed() {
                Config.LogInfo(TAG + "onConfigurationFailed");
            }
        };
    }

    @Override
    public int getTextureResource() {
        return R.id.camera_preview;
    }

    @Override
    public File getVideoFile(Context context) {
        File file;
        try {
            File location = context.getExternalFilesDir("video");
            file = File.createTempFile(String.valueOf(new Date().getTime()), ".mp4", location);
        } catch (IOException e) {
            file = new File(context.getExternalFilesDir("video"),String.valueOf(new Date().getTime()) + ".mp4");
        }
        return file;
    }

    public void onCameraControlClick(ImageView view) {
        if (isRecording()) {
            Config.LogInfo(TAG + "File saved: " + getCurrentFile().getName());
            view.setImageResource(R.drawable.capture_shade);
            stopRecordingVideo();
        } else {
            view.setImageResource(R.drawable.record_capture_shade);
            startRecordingVideo();
        }
    }
}
