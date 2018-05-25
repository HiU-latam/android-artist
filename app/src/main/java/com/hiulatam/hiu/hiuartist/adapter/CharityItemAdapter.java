package com.hiulatam.hiu.hiuartist.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.an.customfontview.CustomTextView;
import com.hiulatam.hiu.hiuartist.R;
import com.hiulatam.hiu.hiuartist.common.Config;
import com.hiulatam.hiu.hiuartist.modal.CharityItemModal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/9/17.
 */

public class CharityItemAdapter extends RecyclerView.Adapter<CharityItemAdapter.CharityItemHolder> {

    private static final String TAG = "CharityItemAdapter - ";

    private List<CharityItemModal> charityItemModalList;
    private View.OnClickListener onClickListener;

    private Context context;

    public CharityItemAdapter(List<CharityItemModal> charityItemModalList, Context context){
        this.charityItemModalList = charityItemModalList;
        this.context = context;
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public List<CharityItemModal> getCharityItemModalList(){
        return charityItemModalList;
    }

    public void setCharityItemModalList(List<CharityItemModal> charityItemModalList){
        this.charityItemModalList = charityItemModalList;
        this.notifyDataSetChanged();
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
        Config.LogInfo(TAG + "onBindViewHolder - profile picture:" + charityItemModal.getProfilePicture());
        holder.imageViewCharity.setImageResource(this.context.getResources().getIdentifier(charityItemModal.getProfilePicture(), "drawable", this.context.getPackageName()));
        holder.customTextViewReply.setTag(charityItemModal);
        holder.customTextViewReply.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return charityItemModalList.size();
    }

    class CharityItemHolder extends RecyclerView.ViewHolder{
        public CustomTextView customTextViewName, customTextViewTimeAndDate, customTextViewReply;
        public ImageView imageViewCharity;

        public CharityItemHolder(View itemView) {
            super(itemView);

            customTextViewName = (CustomTextView) itemView.findViewById(R.id.customTextViewName);
            customTextViewTimeAndDate = (CustomTextView) itemView.findViewById(R.id.customTextViewTimeAndDate);
            customTextViewReply = (CustomTextView) itemView.findViewById(R.id.customTextViewReply);
            imageViewCharity = (ImageView) itemView.findViewById(R.id.imageViewCharity);
        }
    }
}
