package com.burkow.sykler;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by aleksanb on 5/31/13.
 */
public class StationFragment extends Fragment {

    public static final String ARG_STATION = "ARG_STATION";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View returnable = inflater.inflate(R.layout.fragment_station, container, false);
        TextView tv = (TextView) returnable.findViewById(R.id.tvName);
        Stations s = (Stations) getArguments().getSerializable(StationFragment.ARG_STATION);
        tv.setText((s != null)? s.toString():"Holo Yolo");

        return returnable;
    }

    public static StationFragment newInstance(Stations s) {
        StationFragment sf = new StationFragment();
        Bundle args = new Bundle();
        args.putSerializable(StationFragment.ARG_STATION, s);
        sf.setArguments(args);

        return sf;
    }
}
