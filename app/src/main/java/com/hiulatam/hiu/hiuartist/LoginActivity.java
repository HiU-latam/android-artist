package com.hiulatam.hiu.hiuartist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.an.customfontview.CustomButton;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.hiulatam.hiu.hiuartist.utils.profileUser;
import com.steelkiwi.instagramhelper.InstagramHelper;
import com.steelkiwi.instagramhelper.model.InstagramUser;
import com.steelkiwi.instagramhelper.utils.SharedPrefUtils;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements FacebookCallback {

    private static final String TAG ="facebook login:   " ;
    private Button btn_fb_login,btn_ins_login,btn_twi_login;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private Context mContext;
    private ImageView info;
    private InstagramHelper instagramHelper;
    CustomButton buttonSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         //facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,this);
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                //profile=new profileUser();
                MyApplication.profile.profilefacebok=currentProfile;
                // App code
            }

        };
        profileTracker.startTracking();
        if(profileUser.isLoggedInFAacebook(this)){
            facebookSuccess();
        }else if(profileUser.isLoggedInInstagram(this)){
            InstagramSuccess();
        }else if(profileUser.isLoggedInTwitter(this)){
            TwitterSuccess();

        }
        bindComponents();
        init();
        addListeners();






    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public void onSuccess(Object o) {

        LoginResult loginresult=(LoginResult)o;
        Log.i(TAG, "LoginButton FacebookCallback onSuccess");
        if(loginresult.getAccessToken() != null){
            Log.i(TAG, "Access Token:: "+loginresult.getAccessToken());

            facebookSuccess();
        }
       //String UserId= loginresult.getAccessToken().getUserId();

    }

    private void facebookSuccess() {
        Profile curreProfile = Profile.getCurrentProfile();

        MyApplication.profile.profilefacebok=curreProfile;
        Intent intent= new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("profile", MyApplication.profile);
        startActivity(intent);
    }

    private void InstagramSuccess() {
        InstagramUser instagramUser = SharedPrefUtils.getInstagramUser(this);
        String token = SharedPrefUtils.getToken(this);
        MyApplication.profile.profileInstagram=instagramUser;
        Intent intent= new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("profile", MyApplication.profile);
        startActivity(intent);
    }

    public void TwitterSuccess() {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
       // MyApplication.profile.profileInstagram=instagramUser;
        TwitterActivity.getUserProfile(this);
    }

    @Override
    public void onCancel() {
        Toast.makeText(this,"cancel",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(FacebookException error) {
        Toast.makeText(this,error.toString(),
                Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }





    /**
     * Created by:  Shiny Solutions
     * Created on:  09/30/2017
     */
    private void bindComponents(){
        info= (ImageView) findViewById(R.id.imageInfo);
        buttonSignIn = (CustomButton) findViewById(R.id.sign_in);
        btn_fb_login = (Button)findViewById(R.id.btn_fb_login);
        btn_ins_login = (Button)findViewById(R.id.btn_ins_login);
        btn_twi_login = (Button) findViewById(R.id.btn_twi_login);
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  09/30/2017
     */
    private void init(){

    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  09/30/2017
     */
    private void addListeners(){
        buttonSignIn.setOnClickListener(clickListener);
        btn_fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(
                        LoginActivity.this, Arrays.asList("public_profile", "user_friends"));

            }
        });
        btn_ins_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(LoginActivity.this,InstagramActivity.class);
                startActivity(intent);

            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,Walkthrough.class);
                startActivity(intent);
            }
        });
        btn_twi_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,TwitterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  09/30/2017
     */
    private void showDashboard(){
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  09/30/2017
     */
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDashboard();
        }
    };


}

