package com.burkow.sykler;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Sykler extends Activity {

    private static final String TAG = "Sykler";
    private ArrayList<Stations> stations;
    private ColorDrawable colorDrawable;
    private ListView mDrawerList;
    private StationAdapter stationAdapter;
    private DrawerLayout mDrawerLayout;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private LocationManager locationManager;
    private String locationProvider;
    private Location currentBestLocation;
    private LocationListener ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sykler);

        colorDrawable = new ColorDrawable(Color.parseColor("#34495e"));
        getActionBar().setBackgroundDrawable(colorDrawable);

        stations = new ArrayList<Stations>();
        stationAdapter = new StationAdapter(stations, this);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(stationAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.NETWORK_PROVIDER;
        currentBestLocation = locationManager.getLastKnownLocation(locationProvider);
        ll = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "Got new location\nNew location is"+location.toString()+"\nwhile old location is"+currentBestLocation.toString());
                currentBestLocation = location;
            }
        };

        // How often does this happen?
        locationManager.requestLocationUpdates(locationProvider, 0, 0, ll);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        StationFragment sf = StationFragment.newInstance(new Stations(0, "name", 0, 0, false, false, 0, 0));
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, sf)
                .commit();

        new FetchStationsTask(this, stations).execute();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectItem(i);
        }
    }

    private void selectItem(int position) {
        StationFragment sf = StationFragment.newInstance(stations.get(position));
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, sf)
                .commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(stations.get(position).getName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sykler, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStationsLoaded() {
        for (Stations s: stations) {
            Log.d(TAG, s.toString());
        }
        stationAdapter.notifyDataSetChanged();
    }
}
