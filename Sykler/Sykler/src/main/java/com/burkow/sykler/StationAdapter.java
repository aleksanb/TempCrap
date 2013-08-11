package com.burkow.sykler;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aleksanb on 5/31/13.
 */
public class StationAdapter extends BaseAdapter {

    private final Activity activity;
    private final LayoutInflater inflater;
    private ArrayList<Stations> stations;

    public StationAdapter(ArrayList<Stations> stations, Activity activity) {
        this.stations = stations;
        this.activity = activity;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public Object getItem(int i) {
        return stations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_station, null);
        }

        TextView id = (TextView) view.findViewById(R.id.tvId);
        TextView label = (TextView) view.findViewById(R.id.tvName);
        TextView available = (TextView) view.findViewById(R.id.tvAvailable);

        Stations s = stations.get(i);

        String sId = String.valueOf(s.getId());
        if (sId.length()==1) sId = "0"+sId;

        id.setText(sId);
        label.setText(s.getName());
        available.setText(s.getAvailableBikes() + " / " + s.getSize());

        return view;
    }
}
