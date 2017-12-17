package com.hiulatam.hiu.hiuartist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hiulatam.hiu.hiuartist.R;
import com.hiulatam.hiu.hiuartist.common.Config;
import com.hiulatam.hiu.hiuartist.modal.SettingItemModal;
import com.hiulatam.hiu.hiuartist.modal.SettingsChildItemModal;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.util.List;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/28/17.
 */

public class SettingsItemAdapter extends ExpandableRecyclerViewAdapter<SettingItemViewHolder, ChildViewHolder> {
    private static final String TAG = "SettingsItemAdapter - ";

    public static final int FIRST_VIEW_TYPE = 1;
    public static final int ARTIST_VIEW_TYPE = 4;

    List<SettingItemModal> parentObjectList;
    Context context;

    public SettingsItemAdapter(Context context, List<? extends ExpandableGroup> parentItemList) {
        super(parentItemList);
        this.context = context;
        Config.LogInfo(TAG + "SettingsItemAdapter");
    }

    @Override
    public SettingItemViewHolder onCreateGroupViewHolder(ViewGroup viewGroup, int viewType) {
        Config.LogInfo(TAG + "onCreateParentViewHolder");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_settings, null, false);
        return new SettingItemViewHolder(view);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup, int viewType) {
        Config.LogInfo(TAG + "onCreateChildViewHolder");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_expanded_settings, viewGroup, false);
        return new SettingsChildItemViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(SettingItemViewHolder settingItemViewHolder, int i, ExpandableGroup o) {
        Config.LogInfo(TAG + "onBindParentViewHolder");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(R.dimen.settings_group_item_height));
        settingItemViewHolder.getRelativeLayoutMenu().setLayoutParams(layoutParams);
        settingItemViewHolder.getRelativeLayoutMenu().setGravity(Gravity.CENTER_VERTICAL);
        if (i < (getItemCount() - 1 )){
            settingItemViewHolder.getRelativeLayoutMenu().setBackgroundResource(R.drawable.menu_group_bg);
        }
        SettingItemModal settingItemModal = (SettingItemModal) o;
        settingItemViewHolder.getCustomTextViewMenuNotification().setVisibility(View.GONE);
        settingItemViewHolder.getImageViewMenuNotification().setVisibility(View.GONE);
        settingItemViewHolder.getImageViewMenuIcon().setVisibility(View.VISIBLE);
        if (settingItemModal.getName().equalsIgnoreCase(context.getString(R.string.notification))){
            settingItemViewHolder.getImageViewMenuNotification().setVisibility(View.VISIBLE);
        }
        if (settingItemModal.getName().equalsIgnoreCase(context.getString(R.string.value_min_request))){
            settingItemViewHolder.getCustomTextViewMenuNotification().setBackgroundResource(R.drawable.ic_menu_rectangle_1);
            settingItemViewHolder.getCustomTextViewMenuNotification().setVisibility(View.VISIBLE);
        }
        if (settingItemModal.getName().equalsIgnoreCase(context.getString(R.string.balance))){
            settingItemViewHolder.getImageViewMenuIcon().setVisibility(View.GONE);
            settingItemViewHolder.getCustomTextViewMenuNotification().setVisibility(View.VISIBLE);
            settingItemViewHolder.getCustomTextViewMenuNotification().setText("$850");
            settingItemViewHolder.getCustomTextViewMenuNotification().setTextColor(Color.BLACK);
            settingItemViewHolder.getCustomTextViewMenuName().setTextColor(Color.BLACK);
            settingItemViewHolder.getCustomTextViewMenuName().setTextSize(context.getResources().getDimension(R.dimen.large_font));
        }
        if (settingItemModal.getName().equalsIgnoreCase(context.getString(R.string.more_options))){
            settingItemViewHolder.getCustomTextViewMenuName().setTextColor(Color.BLACK);
        }

        settingItemViewHolder.setMenuTitle(settingItemModal);
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int i, ExpandableGroup group, int childIndex) {
        Config.LogInfo(TAG + "onBindChildViewHolder");
        if (childViewHolder instanceof SettingsChildItemViewHolder){
            SettingsChildItemModal settingsChildItemModal = ((SettingsChildItemModal) group.getItems().get(childIndex));
            SettingsChildItemViewHolder settingsChildItemViewHolder = (SettingsChildItemViewHolder) childViewHolder;
            settingsChildItemViewHolder.onBind(settingsChildItemModal, i);
        }
    }
}
