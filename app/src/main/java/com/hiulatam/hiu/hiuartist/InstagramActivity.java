package com.hiulatam.hiu.hiuartist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.steelkiwi.instagramhelper.InstagramHelper;
import com.steelkiwi.instagramhelper.InstagramHelperConstants;
import com.steelkiwi.instagramhelper.model.InstagramUser;

public class InstagramActivity extends FragmentActivity {
    private InstagramHelper instagramHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                String scope = "likes+comments";
        instagramHelper = new InstagramHelper.Builder()
                .withClientId(getString(R.string.instagram_app_id))
                .withRedirectUrl("http://localhost")
                .withScope(scope)
                .build();

        instagramHelper.loginFromActivity(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == InstagramHelperConstants.INSTA_LOGIN && resultCode == RESULT_OK) {

            InstagramUser user = instagramHelper.getInstagramUser(this);
            MyApplication.profile.profileInstagram=user;
            Intent intent= new Intent(this,MainActivity.class);
            intent.putExtra("profile", MyApplication.profile);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
            Intent inten= new Intent(this,LoginActivity.class);
            startActivity(inten);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
