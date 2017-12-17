package com.hiulatam.hiu.hiuartist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import com.hiulatam.hiu.hiuartist.R;
import com.hiulatam.hiu.hiuartist.common.Config;
import com.hiulatam.hiu.hiuartist.modal.SettingsChildItemModal;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/28/17.
 */

public class SettingsChildItemViewHolder extends ChildViewHolder {

    private static final String TAG = "SettingsChildItemViewHolder - ";

    public RadioButton radioButtonOptionOne;

    public SettingsChildItemViewHolder(View itemView) {
        super(itemView);
        radioButtonOptionOne = (RadioButton) itemView.findViewById(R.id.radioButtonOptionOne);
        radioButtonOptionOne.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    public void onBind(SettingsChildItemModal settingsChildItemModal, int i){
        Config.LogInfo(TAG + "onBind - ");
        Config.LogInfo(TAG + "onBind - i: " + i);
        radioButtonOptionOne.setText(settingsChildItemModal.getOptionItem());
        radioButtonOptionOne.setTag(i);
        if (i == 1){
            radioButtonOptionOne.setChecked(true);
        }else
            radioButtonOptionOne.setChecked(false);



    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b){
                if (compoundButton.getParent().getParent() instanceof RecyclerView){
                    RecyclerView recyclerView = (RecyclerView) compoundButton.getParent().getParent();
                    for (int i = 0; i < recyclerView.getChildCount(); i++){
                        if (recyclerView.getChildAt(i) instanceof RelativeLayout){
                            RelativeLayout relativeLayout = (RelativeLayout) recyclerView.getChildAt(i);
                            for (int j = 0; j < relativeLayout.getChildCount(); j++){
                                if (relativeLayout.getChildAt(j) instanceof RadioButton){
                                    RadioButton radioButton = (RadioButton) relativeLayout.getChildAt(j);
                                    if (compoundButton.getTag() != radioButton.getTag()){
                                        radioButton.setChecked(false);
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }
    };


}
