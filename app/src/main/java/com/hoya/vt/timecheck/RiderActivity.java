package com.hoya.vt.timecheck;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class RiderActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Button mWaitButton;
    private int mTimeToBus;
    private Location mBusLoc;
    private Marker mMarker;

    private static final double BUS_LAT = 38.906291;
    private static final double BUS_LONG = -77.074834;
    private static final double WALK_SPEED = 1.4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mWaitButton = (Button) findViewById(R.id.button_wait);
        mWaitButton.setVisibility(View.INVISIBLE);
        mWaitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: inform the bus driver that the user is on their way
            }
        });
        mBusLoc = new Location("");
        mBusLoc.setLatitude(BUS_LAT);
        mBusLoc.setLongitude(BUS_LONG);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        // Verify permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Verify permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (lastLocation != null) {
            LatLng currLoc = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLoc, (float) 18));
            calculateWalkTime(lastLocation, mBusLoc);
        }
        // Hard-code the bus marker
        mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(BUS_LAT, BUS_LONG))
                .title("Last ride home").snippet(mTimeToBus + " seconds away"));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mWaitButton.setVisibility(View.VISIBLE);
                return false;
            }
        });

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, LocationRequest
                .create().setInterval(5000),
                new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Tag", "Location changed");
                calculateWalkTime(mBusLoc, location);
            }
        });
    }

    private void calculateWalkTime(Location a, Location b) {
        mTimeToBus = (int) (a.distanceTo(b) / WALK_SPEED);
        Log.i("Tag", "time to bus " + mTimeToBus);
        if (mMarker != null) {
            mMarker.setSnippet(mTimeToBus + " seconds away");
            //Refresh the snippet
            mMarker.hideInfoWindow();
            mMarker.showInfoWindow();
            Log.i("Tag", "setSnippet " + mTimeToBus);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
