package com.hiulatam.hiu.hiuartist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class SplashScreen extends AppCompatActivity {
    private static final String LOG_TAG = SplashScreen.class.getSimpleName();

    private Context context;
    public GifImageView gifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context=this;
        gifView = (GifImageView) findViewById(R.id.gif);
        try {
            GifDrawable gifFromResource = new GifDrawable( getResources(), R.drawable.gif_splash );
            gifView.setImageDrawable(gifFromResource);
            gifFromResource.setSpeed(2.0f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initActivity();




    }

    private void initActivity() {

        GifDrawable gifDrawable=(GifDrawable) gifView.getDrawable();
           if(gifView.getDrawable()!=null){
            gifDrawable.addAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationCompleted(int loopNumber) {
                    Intent intent= new Intent(context,LoginActivity.class);
                    context.startActivity(intent);


                }
            });
        }

    }



}
