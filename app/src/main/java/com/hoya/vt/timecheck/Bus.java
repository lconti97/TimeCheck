package com.hoya.vt.timecheck;

import android.location.Location;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Lucas on 1/30/2016.
 */
public class Bus {
    private Marker mMarker;
    private Location mLocation;
    private double mLat;
    private double mLong;
    private String mTitle;

    // In m/s
    private static final double WALK_SPEED = 1.4;

    public Bus(String title, double lat, double lon) {
        mTitle = title;
        mLat = lat;
        mLong = lon;
        mLocation = new Location("");
        mLocation.setLatitude(lat);
        mLocation.setLongitude(lon);
    }
}
