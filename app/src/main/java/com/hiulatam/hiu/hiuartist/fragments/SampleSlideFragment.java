package com.hiulatam.hiu.hiuartist.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SampleSlideFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";


    private int  layouRestId;


       public SampleSlideFragment() {
        // Required empty public constructor
    }


    public static SampleSlideFragment newInstance(int layouResID) {
        SampleSlideFragment fragment = new SampleSlideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, layouResID);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            layouRestId = getArguments().getInt(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(layouRestId, container, false);
    }



}
