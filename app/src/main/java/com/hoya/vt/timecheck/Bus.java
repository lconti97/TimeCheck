package com.hoya.vt.timecheck;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Lucas on 1/30/2016.
 */
public class Bus {
    private Marker mMarker;
    private Location mLocation;
    private double mLat;
    private double mLng;
    private String mTitle;

    // In m/s
    private static final double WALK_SPEED = 1.4;

    public Bus(String title, double lat, double lon) {
        mTitle = title;
        mLat = lat;
        mLng = lon;
        mLocation = new Location("");
        mLocation.setLatitude(lat);
        mLocation.setLongitude(lon);
    }

    public int calcWalkTime(Location start) {
        int walkTime = (int) (getLocation().distanceTo(start) / WALK_SPEED);
        if (mMarker!= null) {
            mMarker.setSnippet(walkTime + " seconds away");
        }
        return walkTime;
    }

    public Marker placeMarker(GoogleMap map) {
        mMarker = map.addMarker(new MarkerOptions().position(getLatLng()).title(mTitle));
        return mMarker;
    }

    public String getTitle() {
        return mTitle;
    }

    public LatLng getLatLng() {
        return new LatLng(mLat, mLng);
    }

    public double getLat() {
        return mLat;
    }

    public double getLon() {
        return mLng;
    }

    public Location getLocation()  {
        return mLocation;
    }

    public Marker getMarker() {
        return mMarker;
    }
}
