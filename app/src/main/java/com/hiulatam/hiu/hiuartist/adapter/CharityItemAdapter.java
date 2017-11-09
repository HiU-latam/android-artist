package com.hiulatam.hiu.hiuartist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.an.customfontview.CustomTextView;
import com.hiulatam.hiu.hiuartist.R;
import com.hiulatam.hiu.hiuartist.modal.CharityItemModal;

import java.util.List;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/9/17.
 */

public class CharityItemAdapter extends RecyclerView.Adapter<CharityItemAdapter.CharityItemHolder> {

    private List<CharityItemModal> charityItemModalList;

    public CharityItemAdapter(List<CharityItemModal> charityItemModalList){
        this.charityItemModalList = charityItemModalList;
    }

    @Override
    public CharityItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_charity, parent, false);
        return new CharityItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CharityItemHolder holder, int position) {
        CharityItemModal charityItemModal = charityItemModalList.get(position);

        holder.customTextViewName.setText(String.valueOf(charityItemModal.getName()));
        holder.customTextViewTimeAndDate.setText(String.format("%s - %s", charityItemModal.getTime(), charityItemModal.getDate()));
    }

    @Override
    public int getItemCount() {
        return charityItemModalList.size();
    }

    class CharityItemHolder extends RecyclerView.ViewHolder{
        public CustomTextView customTextViewName, customTextViewTimeAndDate;

        public CharityItemHolder(View itemView) {
            super(itemView);

            customTextViewName = (CustomTextView) itemView.findViewById(R.id.customTextViewName);
            customTextViewTimeAndDate = (CustomTextView) itemView.findViewById(R.id.customTextViewTimeAndDate);
        }
    }
}
