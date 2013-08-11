package com.burkow.sykler;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aleksanb on 6/1/13.
 */
public class MainFragment extends Fragment {

    public static final String ARG_STATIONS = "ARG_STATIONS";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View returnable = inflater.inflate(R.layout.fragment_main, container, false);



        return returnable;
    }



}
