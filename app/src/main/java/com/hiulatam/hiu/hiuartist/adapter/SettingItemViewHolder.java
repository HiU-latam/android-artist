package com.hiulatam.hiu.hiuartist.adapter;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.an.customfontview.CustomEditText;
import com.an.customfontview.CustomTextView;
import com.hiulatam.hiu.hiuartist.R;
import com.hiulatam.hiu.hiuartist.common.Config;
import com.hiulatam.hiu.hiuartist.modal.SettingItemModal;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/23/17.
 */

public class SettingItemViewHolder extends GroupViewHolder {

    private static final String TAG = "SettingItemViewHolder - ";

    private ImageView imageViewNonAct, imageViewMenuIcon, imageViewMenuArrow, imageViewMenuNotification;
    private CustomTextView customTextViewMenuName;
    private RelativeLayout relativeLayoutMenu;
    private CheckBox checkboxMenuNotification;
    private CustomEditText textMenuNotification;

    public SettingItemViewHolder(View itemView) {
        super(itemView);
        Config.LogInfo(TAG + "SettingItemViewHolder");
        customTextViewMenuName = (CustomTextView) itemView.findViewById(R.id.customTextViewMenuName);
        imageViewMenuArrow = (ImageView) itemView.findViewById(R.id.imageViewMenuArrow);
        imageViewMenuIcon = (ImageView) itemView.findViewById(R.id.imageViewMenuIcon);
        relativeLayoutMenu = (RelativeLayout) itemView.findViewById(R.id.relativeLayoutMenu);
        textMenuNotification = (CustomEditText) itemView.findViewById(R.id.textMenuNotification);
        imageViewMenuNotification = (ImageView) itemView.findViewById(R.id.imageViewMenuNotification);
        checkboxMenuNotification = (CheckBox) itemView.findViewById(R.id.checkboxMenuNotification);
    }

    public void setMenuTitle(SettingItemModal settingItemModal){
        Config.LogInfo(TAG + "setMenuTitle");
        Config.LogInfo(TAG + "setMenuTitle - getName: " + settingItemModal.getName());
        customTextViewMenuName.setText(settingItemModal.getName());
        imageViewMenuIcon.setImageResource(settingItemModal.getImageDrawable());
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        imageViewMenuArrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(90, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        imageViewMenuArrow.setAnimation(rotate);
    }

    public RelativeLayout getRelativeLayoutMenu() {
        return relativeLayoutMenu;
    }

    public CustomEditText getTextMenuNotification() {
        return textMenuNotification;
    }

    public CustomTextView getCustomTextViewMenuName() {
        return customTextViewMenuName;
    }

    public ImageView getImageViewMenuNotification() {
        return imageViewMenuNotification;
    }

    public ImageView getImageViewMenuIcon() {
        return imageViewMenuIcon;
    }

    public CheckBox getCheckboxMenuNotification() {
        return checkboxMenuNotification;
    }
}
