package com.hiulatam.hiu.hiuartist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.an.customfontview.CustomTextView;
import com.hiulatam.hiu.hiuartist.adapter.SettingsItemAdapter;
import com.hiulatam.hiu.hiuartist.common.Config;
import com.hiulatam.hiu.hiuartist.modal.SettingItemModal;
import com.hiulatam.hiu.hiuartist.modal.SettingsChildItemModal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/22/17.
 */

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity - ";

    CustomTextView customTextViewProfileName;
    RecyclerView recyclerViewSettings;

    SettingsItemAdapter settingsItemAdapter;

    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bindComponents();
        init();
        addListeners();
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/22/17.
     */
    private void bindComponents(){
        Config.LogInfo(TAG + "bindComponents");
        customTextViewProfileName = (CustomTextView) findViewById(R.id.customTextViewProfileName);

        recyclerViewSettings = (RecyclerView) findViewById(R.id.recyclerViewSettings);
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/22/17.
     */
    private void init(){
        Config.LogInfo(TAG + "init");
        layoutManager = new LinearLayoutManager(this);


        RecyclerView.ItemAnimator animator = recyclerViewSettings.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        setSettingsItemAdapter();
    }


    /**
     * Created by:  Shiny Solutions
     * Created on:  11/22/17.
     */
    private void addListeners(){
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/28/17
     * @return
     */
    private List<SettingItemModal> getSettingItems(){
        Config.LogInfo(TAG + "getSettingItems");
        List<SettingItemModal> settingItemModalList = new ArrayList<SettingItemModal>();

        List<SettingsChildItemModal> settingsChildItemModalList = new ArrayList<SettingsChildItemModal>();
        SettingsChildItemModal settingsChildItemModal = new SettingsChildItemModal(getString(R.string.number_requests_allowed_option_one));
        settingsChildItemModalList.add(settingsChildItemModal);
        settingsChildItemModal = new SettingsChildItemModal(getString(R.string.number_requests_allowed_option_two));
        settingsChildItemModalList.add(settingsChildItemModal);
        settingsChildItemModal = new SettingsChildItemModal(getString(R.string.number_requests_allowed_option_three));
        settingsChildItemModalList.add(settingsChildItemModal);
        SettingItemModal settingItemModal = new SettingItemModal(getString(R.string.number_requests_allowed), R.drawable.ic_number_request_allowed, settingsChildItemModalList);
        settingItemModalList.add(settingItemModal);

        settingsChildItemModalList = new ArrayList<SettingsChildItemModal>();
        settingItemModal = new SettingItemModal(getString(R.string.my_charity_preferences), R.drawable.ic_my_chairty_preferences, settingsChildItemModalList);
        settingItemModalList.add(settingItemModal);

        settingsChildItemModalList = new ArrayList<SettingsChildItemModal>();
        settingItemModal = new SettingItemModal(getString(R.string.notification), R.drawable.ic_notification, settingsChildItemModalList);
        settingItemModalList.add(settingItemModal);

        settingsChildItemModalList = new ArrayList<SettingsChildItemModal>();
        settingItemModal = new SettingItemModal(getString(R.string.value_min_request), R.drawable.ic_value_min_request, settingsChildItemModalList);
        settingItemModalList.add(settingItemModal);

        settingsChildItemModalList = new ArrayList<SettingsChildItemModal>();
        settingItemModal = new SettingItemModal(getString(R.string.balance), R.drawable.ic_number_request_allowed, settingsChildItemModalList);
        settingItemModalList.add(settingItemModal);

        settingsChildItemModalList = new ArrayList<SettingsChildItemModal>();
        settingItemModal = new SettingItemModal(getString(R.string.more_options), R.drawable.ic_more_options, settingsChildItemModalList);
        settingItemModalList.add(settingItemModal);

        Config.LogInfo(TAG + "getSettingItems - Group Size: " + settingItemModalList.size());

        return settingItemModalList;
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/28/17
     */
    private void setSettingsItemAdapter(){
        Config.LogInfo(TAG + "setSettingsItemAdapter");
        if (settingsItemAdapter == null)
            settingsItemAdapter = new SettingsItemAdapter(this, getSettingItems());

        recyclerViewSettings.setLayoutManager(layoutManager);
        recyclerViewSettings.setAdapter(settingsItemAdapter);
    }

}
