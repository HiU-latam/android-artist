package com.hiulatam.hiu.hiuartist;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.an.customfontview.CustomTextView;
import com.hiulatam.hiu.hiuartist.adapter.CharityItemAdapter;
import com.hiulatam.hiu.hiuartist.common.Config;
import com.hiulatam.hiu.hiuartist.customclass.DividerItemDecoration;
import com.hiulatam.hiu.hiuartist.customclass.FilterResultsCallback;
import com.hiulatam.hiu.hiuartist.modal.CharityItemModal;
import com.hiulatam.hiu.hiuartist.utils.circleTransform;
import com.hiulatam.hiu.hiuartist.utils.profileUser;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Search;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity - ";

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private profileUser pefil;

    private NavigationView navigationView;
    private Spinner spinnerMonths;
    private RecyclerView recyclerViewRequests;
    private ImageView imageViewPlus;
    private ImageButton imageButtonNavigationView, imageButtonSettings;
    private DrawerLayout drawerLayout;
    private CustomTextView customTextViewByName, customTextViewCount;
    private TabLayout tab_layout_celebrity;
    private SearchView search_view_celebrity;
    private TextView textViewEmptyResult;

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
        imageButtonSettings = (ImageButton) findViewById(R.id.image_button_settings);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        customTextViewByName = (CustomTextView) findViewById(R.id.customTextViewByName);
        customTextViewCount = (CustomTextView) findViewById(R.id.customTextViewCount);

        tab_layout_celebrity = (TabLayout) findViewById(R.id.tab_layout_celebrity);

        search_view_celebrity = (SearchView) findViewById(R.id.search_view_celebrity);

        textViewEmptyResult = (TextView)  findViewById(R.id.textViewEmptyResult);
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

        customTextViewByName.setText(String.format("By %s", tab_layout_celebrity.getTabAt(tab_layout_celebrity.getSelectedTabPosition()).getText()));

        if (charityItemAdapter != null)
            customTextViewCount.setText(String.valueOf(charityItemAdapter.getItemCount()));

    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  10/28/17
     */
    private void addListeners(){
        imageButtonNavigationView.setOnClickListener(onClickListener);
        imageViewPlus.setOnClickListener(onClickListener);
        imageButtonSettings.setOnClickListener(onClickListener);
        tab_layout_celebrity.addOnTabSelectedListener(onTabSelectedListener);
        search_view_celebrity.setOnQueryTextListener(onQueryTextListener);
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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm a");
        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        Calendar cTime = Calendar.getInstance();
        Date currentTime = cTime.getTime();

        CharityItemModal charityItemModal = new CharityItemModal();
        charityItemModal.setName("Mary Snow");
        charityItemModal.setTime(simpleTimeFormat.format(currentTime));
        charityItemModal.setDate(simpleDateFormat.format(currentDate));
        charityItemModal.setProfilePicture("mary_snow");
        charityItemModal.setCountry("Virginia");
        charityItemModalList.add(charityItemModal);

        c.set(Calendar.DAY_OF_MONTH, -1);
        currentDate = c.getTime();
        cTime.set(Calendar.HOUR_OF_DAY, -1);
        currentTime = cTime.getTime();
        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Amy Adams");
        charityItemModal.setTime(simpleTimeFormat.format(currentTime));
        charityItemModal.setDate(simpleDateFormat.format(currentDate));
        charityItemModal.setProfilePicture("amy_adams");
        charityItemModal.setCountry("California");
        charityItemModalList.add(charityItemModal);

        c.set(Calendar.DAY_OF_MONTH, -3);
        currentDate = c.getTime();
        cTime.set(Calendar.HOUR_OF_DAY, -3);
        currentTime = cTime.getTime();
        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Angelina Jolie");
        charityItemModal.setTime(simpleTimeFormat.format(currentTime));
        charityItemModal.setDate(simpleDateFormat.format(currentDate));
        charityItemModal.setProfilePicture("angelina_jolie");
        charityItemModal.setCountry("Cambodia");
        charityItemModalList.add(charityItemModal);

        c.set(Calendar.DAY_OF_MONTH, -5);
        currentDate = c.getTime();
        cTime.set(Calendar.HOUR_OF_DAY, -5);
        currentTime = cTime.getTime();
        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Charlize Theron");
        charityItemModal.setTime(simpleTimeFormat.format(currentTime));
        charityItemModal.setDate(simpleDateFormat.format(currentDate));
        charityItemModal.setProfilePicture("charlize_theron");
        charityItemModal.setCountry("California");
        charityItemModalList.add(charityItemModal);

        c.set(Calendar.DAY_OF_MONTH, -7);
        currentDate = c.getTime();
        cTime.set(Calendar.HOUR_OF_DAY, -7);
        currentTime = cTime.getTime();
        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Emma Stone");
        charityItemModal.setTime(simpleTimeFormat.format(currentTime));
        charityItemModal.setDate(simpleDateFormat.format(currentDate));
        charityItemModal.setProfilePicture("emma_stone");
        charityItemModal.setCountry("Arizona");
        charityItemModalList.add(charityItemModal);

        c.set(Calendar.DAY_OF_MONTH, -11);
        currentDate = c.getTime();
        cTime.set(Calendar.HOUR_OF_DAY, -13);
        currentTime = cTime.getTime();
        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Jennifer Lawrence");
        charityItemModal.setTime(simpleTimeFormat.format(currentTime));
        charityItemModal.setDate(simpleDateFormat.format(currentDate));
        charityItemModal.setProfilePicture("jennifer_lawrence");
        charityItemModal.setCountry("California");
        charityItemModalList.add(charityItemModal);

        c.set(Calendar.DAY_OF_MONTH, -13);
        currentDate = c.getTime();
        cTime.set(Calendar.HOUR_OF_DAY, -15);
        currentTime = cTime.getTime();
        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Mary Snow");
        charityItemModal.setTime(simpleTimeFormat.format(currentTime));
        charityItemModal.setDate(simpleDateFormat.format(currentDate));
        charityItemModal.setProfilePicture("mary_snow");
        charityItemModal.setCountry("Virginia");
        charityItemModalList.add(charityItemModal);

        c.set(Calendar.DAY_OF_MONTH, -17);
        currentDate = c.getTime();
        cTime.set(Calendar.HOUR_OF_DAY, -17);
        currentTime = cTime.getTime();
        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Natalie Portman");
        charityItemModal.setTime(simpleTimeFormat.format(currentTime));
        charityItemModal.setDate(simpleDateFormat.format(currentDate));
        charityItemModal.setProfilePicture("natalie_portman");
        charityItemModal.setCountry("Danish");
        charityItemModalList.add(charityItemModal);

        c.set(Calendar.DAY_OF_MONTH, -21);
        currentDate = c.getTime();
        cTime.set(Calendar.HOUR_OF_DAY, -21);
        currentTime = cTime.getTime();
        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Scarlett Johansson");
        charityItemModal.setTime(simpleTimeFormat.format(currentTime));
        charityItemModal.setDate(simpleDateFormat.format(currentDate));
        charityItemModal.setProfilePicture("scarlett_johansson");
        charityItemModal.setCountry("Danish");
        charityItemModalList.add(charityItemModal);

        c.set(Calendar.DAY_OF_MONTH, -25);
        currentDate = c.getTime();
        cTime.set(Calendar.HOUR_OF_DAY, -23);
        currentTime = cTime.getTime();
        charityItemModal = new CharityItemModal();
        charityItemModal.setName("Mary Snow");
        charityItemModal.setTime(simpleTimeFormat.format(currentTime));
        charityItemModal.setDate(simpleDateFormat.format(currentDate));
        charityItemModal.setProfilePicture("mary_snow");
        charityItemModal.setCountry("Virginia");
        charityItemModalList.add(charityItemModal);

        return charityItemModalList;
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/08/17
     */
    private void setCharityItemAdapter(){
        if (charityItemAdapter == null){
            charityItemAdapter = new CharityItemAdapter(setCharityItemModalList(), this);
            charityItemAdapter.setOnClickListener(onClickListener);
            charityItemAdapter.setFilterResultsCallback(filterResultsCallback);
        }
        recyclerViewRequests.setAdapter(charityItemAdapter);
    }

    private void getVideoActivity(CharityItemModal charityItemModal){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
        /*
        Intent intent = new Intent();
        intent.setClass(this, VideoCapture.class);
        intent.putExtra(Config.KEY_CHARITY_ITEM_MODAL, charityItemModal);
        startActivity(intent);
        Intent intent = new Intent();
        intent.setClass(this, Video.class);
        intent.putExtra(Config.KEY_CHARITY_ITEM_MODAL, charityItemModal);
        startActivity(intent);
        */
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/21/17
     */
    private void openSettingsActivity(){
        Intent intent = new Intent();
        intent.setClass(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/21/17
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imageViewPlus:
                    getVideoActivity(null);
                    break;
                case R.id.customTextViewReply:
                    CharityItemModal charityItemModal = (CharityItemModal) view.getTag();
                    getVideoActivity(charityItemModal);
                    break;
                case R.id.image_button_navigation_view:
                    drawerLayout.openDrawer(Gravity.LEFT);
                    break;
                case R.id.image_button_settings:
                    openSettingsActivity();
                    break;
            }
        }
    };

    /**
     * Created By:   Shiny Solutions
     * Created On:  05/14/2018
     */
    TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            customTextViewByName.setText(String.format("By %s", tab.getText()));

            switch (tab.getPosition()){
                case 1:
                    Collections.sort(charityItemAdapter.getCharityItemModalList(), new Comparator<CharityItemModal>() {
                        @Override
                        public int compare(CharityItemModal charityItemModal, CharityItemModal t1) {
                            return charityItemModal.getCountry().compareTo(t1.getCountry());
                        }
                    });
                    charityItemAdapter.setCharityItemModalList(charityItemAdapter.getCharityItemModalList());
                    break;
                case 2:
                    Collections.sort(charityItemAdapter.getCharityItemModalList(), new Comparator<CharityItemModal>() {
                        @Override
                        public int compare(CharityItemModal charityItemModal, CharityItemModal t1) {
                            return charityItemModal.getName().compareTo(t1.getName());
                        }
                    });
                    charityItemAdapter.setCharityItemModalList(charityItemAdapter.getCharityItemModalList());
                    break;
                case 0:
                    Collections.sort(charityItemAdapter.getCharityItemModalList(), new Comparator<CharityItemModal>() {
                        @Override
                        public int compare(CharityItemModal charityItemModal, CharityItemModal t1) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm a");
                            Date dateSource = null;
                            Date dateDestination = null;
                            try {
                                dateSource = simpleDateFormat.parse(charityItemModal.getDate() + " " + charityItemModal.getTime());
                                dateDestination = simpleDateFormat.parse(t1.getDate() + " " + t1.getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return dateSource.compareTo(dateDestination);
                        }
                    });
                    charityItemAdapter.setCharityItemModalList(charityItemAdapter.getCharityItemModalList());
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Config.LogInfo(TAG + "onQueryTextChange");
            charityItemAdapter.getFilter().filter(newText);
            return false;
        }
    };

    /**
     * Created By:  Shiny Solutions
     * Created On:  05/31/2018
     */
    FilterResultsCallback filterResultsCallback = new FilterResultsCallback() {
        @Override
        public void getFilterResultCount(int filterResultCount) {
            customTextViewCount.setText(String.valueOf(filterResultCount));
            if (filterResultCount > 0){
                textViewEmptyResult.setVisibility(View.GONE);
                recyclerViewRequests.setVisibility(View.VISIBLE);
            }else{
                textViewEmptyResult.setText(getString(R.string.empty_result));
                textViewEmptyResult.setVisibility(View.VISIBLE);
                recyclerViewRequests.setVisibility(View.GONE);
            }
        }
    };
}
