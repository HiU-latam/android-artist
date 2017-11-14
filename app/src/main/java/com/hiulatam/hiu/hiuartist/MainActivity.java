package com.hiulatam.hiu.hiuartist;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hiulatam.hiu.hiuartist.adapter.CharityItemAdapter;
import com.hiulatam.hiu.hiuartist.customclass.DividerItemDecoration;
import com.hiulatam.hiu.hiuartist.modal.CharityItemModal;
import com.hiulatam.hiu.hiuartist.utils.circleTransform;
import com.hiulatam.hiu.hiuartist.utils.profileUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity - ";

    private profileUser pefil;

    private NavigationView navigationView;
    private Spinner spinnerMonths;
    private RecyclerView recyclerViewRequests;
    private ImageView imageViewPlus;
    private ImageButton imageButtonNavigationView;
    private DrawerLayout drawerLayout;;

    private ArrayAdapter monthAdapter;
    private CharityItemAdapter charityItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        pefil=MyApplication.profile;

        bindComponents();
        init();
        addListeners();

        if(profileUser.isLoggedInFAacebook(this)){//loged by facebook
            facebookprofilefill();
        }else if(profileUser.isLoggedInInstagram(this)){
            Instagramprofilefill();
        }
        else if(profileUser.isLoggedInTwitter(this)){
            Twitterprofilefill();
        }

    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  10/28/17
     */
    private void bindComponents(){
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        spinnerMonths = (Spinner) findViewById(R.id.spinnerMonth);

        recyclerViewRequests = (RecyclerView) findViewById(R.id.recyclerViewRequests);

        imageViewPlus = (ImageView) findViewById(R.id.imageViewPlus);

        imageButtonNavigationView = (ImageButton) findViewById(R.id.image_button_navigation_view);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  10/28/17
     */
    private void init(){

        setSpinnerMonths();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRequests.setLayoutManager(layoutManager);
        recyclerViewRequests.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRequests.addItemDecoration(new DividerItemDecoration(this));

        setCharityItemAdapter();

    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  10/28/17
     */
    private void addListeners(){
        imageButtonNavigationView.setOnClickListener(onClickListener);
        imageViewPlus.setOnClickListener(onClickListener);
    }

    private void facebookprofilefill() {
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.username);
        nav_user.setText(pefil.profilefacebok.getName());
        ImageView img_user = (ImageView) hView.findViewById(R.id.imageView);
        Picasso.with(this)
                .load(pefil.profilefacebok.getProfilePictureUri(140,140))
                .placeholder(R.drawable.com_facebook_button_login_logo)
                .error(android.R.drawable.sym_def_app_icon)
                .transform(new circleTransform())
                .into(img_user);
        TextView mail_user = (TextView)hView.findViewById(R.id.textView);
        mail_user.setText("");
    }

    private void Instagramprofilefill() {
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.username);
        nav_user.setText(pefil.profileInstagram.getData().getFullName());
        ImageView img_user = (ImageView) hView.findViewById(R.id.imageView);
        Picasso.with(this)
                .load(pefil.profileInstagram.getData().getProfilePicture())
                .placeholder(R.drawable.instagram_button)
                .error(android.R.drawable.sym_def_app_icon)
                .transform(new circleTransform())
                .into(img_user);
        TextView mail_user = (TextView)hView.findViewById(R.id.textView);
        mail_user.setText("");
    }

    private void Twitterprofilefill() {
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.username);
        nav_user.setText(pefil.profileTwitter.name);
        ImageView img_user = (ImageView) hView.findViewById(R.id.imageView);
        //String  a= pefil.profileTwitter.profileBackgroundImageUrl;
        String  b= pefil.profileTwitter.profileImageUrl;
        Picasso.with(this)
                .load(b.replace("_normal",""))
                .placeholder(R.drawable.tw__composer_logo_white)
                .error(android.R.drawable.sym_def_app_icon)
                .transform(new circleTransform())
                .into(img_user);
        TextView mail_user = (TextView)hView.findViewById(R.id.textView);
        mail_user.setText("");
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/08/17
     */
    private void setSpinnerMonths(){
        if (monthAdapter == null)
            monthAdapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonths.setAdapter(monthAdapter);
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/08/17
     * @return
     */
    private List<CharityItemModal> setCharityItemModalList(){
        List<CharityItemModal> charityItemModalList = new ArrayList<CharityItemModal>();

        CharityItemModal charityItemModal = new CharityItemModal();
        charityItemModal.setName("Mary Snow");
        charityItemModal.setTime("06:10 am");
        charityItemModal.setDate("December 24, 2019");
        charityItemModalList.add(charityItemModal);

        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Mary Snow");
        charityItemModal.setTime("06:10 am");
        charityItemModal.setDate("December 24, 2019");
        charityItemModalList.add(charityItemModal);

        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Mary Snow");
        charityItemModal.setTime("06:10 am");
        charityItemModal.setDate("December 24, 2019");
        charityItemModalList.add(charityItemModal);

        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Mary Snow");
        charityItemModal.setTime("06:10 am");
        charityItemModal.setDate("December 24, 2019");
        charityItemModalList.add(charityItemModal);

        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Mary Snow");
        charityItemModal.setTime("06:10 am");
        charityItemModal.setDate("December 24, 2019");
        charityItemModalList.add(charityItemModal);

        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Mary Snow");
        charityItemModal.setTime("06:10 am");
        charityItemModal.setDate("December 24, 2019");
        charityItemModalList.add(charityItemModal);

        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Mary Snow");
        charityItemModal.setTime("06:10 am");
        charityItemModal.setDate("December 24, 2019");
        charityItemModalList.add(charityItemModal);

        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Mary Snow");
        charityItemModal.setTime("06:10 am");
        charityItemModal.setDate("December 24, 2019");
        charityItemModalList.add(charityItemModal);

        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Mary Snow");
        charityItemModal.setTime("06:10 am");
        charityItemModal.setDate("December 24, 2019");
        charityItemModalList.add(charityItemModal);

        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Mary Snow");
        charityItemModal.setTime("06:10 am");
        charityItemModal.setDate("December 24, 2019");
        charityItemModalList.add(charityItemModal);

        return charityItemModalList;
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/08/17
     */
    private void setCharityItemAdapter(){
        if (charityItemAdapter == null){
            charityItemAdapter = new CharityItemAdapter(setCharityItemModalList());
        }
        recyclerViewRequests.setAdapter(charityItemAdapter);
    }

    private void getVideoActivity(){
        Intent intent = new Intent();
        intent.setClass(this, VideoActivity.class);
        startActivity(intent);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imageViewPlus:
                    getVideoActivity();
                    break;
                case R.id.image_button_navigation_view:
                    drawerLayout.openDrawer(Gravity.LEFT);
                    break;
            }
        }
    };
}
