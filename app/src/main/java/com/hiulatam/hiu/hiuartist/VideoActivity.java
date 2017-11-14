package com.hiulatam.hiu.hiuartist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.hiulatam.hiu.hiuartist.dialog.VideoSaveConfirmationDialogFragment;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/10/17.
 */

public class VideoActivity extends AppCompatActivity {

    private static final String TAG = "VideoActivity - ";

    private Toolbar toolbar;
    private ImageView imageViewVideoCapture;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        bindComponents();
        init();
        addListeners();
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/10/17.
     */
    private void bindComponents(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        imageViewVideoCapture = (ImageView) findViewById(R.id.imageViewCaptureVideo);
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/10/17.
     */
    private void init(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/10/17.
     */
    private void addListeners(){
        imageViewVideoCapture.setOnClickListener(onClickListener);
    }

    private void openVideoConfirmationDialog(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
            ft.remove(prev);
        ft.addToBackStack(null);

        VideoSaveConfirmationDialogFragment videoSaveConfirmationDialogFragment = VideoSaveConfirmationDialogFragment.newInstance();
        videoSaveConfirmationDialogFragment.show(ft, "dialog");
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imageViewCaptureVideo:
                    openVideoConfirmationDialog();
                    break;
            }
        }
    };
}
