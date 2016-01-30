package com.hoya.vt.timecheck;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class RiderActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Button mWaitButton;
    private int timeToBus1;
    private int timeToBus2;
    private Location mBusLoc1;
    private Location mBusLoc2;
    private Marker mMarker1;
    private Marker mMarker2;
    private Marker currMarker;

    private static final double BUS_LAT_1 = 38.906291;
    private static final double BUS_LONG_1 = -77.074834;
    private static final double BUS_LAT_2 = 38.906726;
    private static final double BUS_LONG_2 = -77.073733;
    private static final double WALK_SPEED = 1.4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        final twitter4j.Twitter twitter = new TwitterFactory().getInstance();
//
//        AccessToken token = new AccessToken("4862469189-nTQrHeWycUUmNzHcKypYlVUigMGevWzoHbGQEKp",
//                "nMrqffb03wCcigJmPolqeilWsNutCOuBeMy7xx3m8IS5N", Long.parseLong("4862469189"));
//


        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mWaitButton = (Button) findViewById(R.id.button_wait);
        mWaitButton.setVisibility(View.VISIBLE);

        mWaitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: inform the bus driver that the user is on their way


//                try {
//                    Log.d("AWESOME", "IM AWESOME");
//                    Status tweet = twitter.updateStatus("#RIDEALERT411");
//                    Log.d("SUCCESS", "Successfully updated the status to [" + tweet.getText() + "].");
//
//                } catch (twitter4j.TwitterException e) {
//
//                    Log.d("FAIL", "DID NOT PRINT STATUS");
//
//                    e.printStackTrace();
//                }

            }
        });
        mBusLoc1 = new Location("");
        mBusLoc1.setLatitude(BUS_LAT_1);
        mBusLoc1.setLongitude(BUS_LONG_1);
        mBusLoc2 = new Location("");
        mBusLoc2.setLatitude(BUS_LAT_2);
        mBusLoc2.setLongitude(BUS_LONG_2);
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
        }
        // Hard-code the bus markers
        mMarker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(BUS_LAT_1, BUS_LONG_1))
                .title("Last ride home"));
        mMarker2 = mMap.addMarker(new MarkerOptions().position(new LatLng(BUS_LAT_2, BUS_LONG_2))
                .title("The hype train"));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                currMarker = marker;
                mWaitButton.setVisibility(View.VISIBLE);
                return false;
            }
        });

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, LocationRequest
                .create().setInterval(5000),
                new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                timeToBus1 = calculateWalkTime(location, mBusLoc1);
                timeToBus2 = calculateWalkTime(location, mBusLoc2);
                mMarker1.setSnippet(timeToBus1 + " seconds away");
                mMarker2.setSnippet(timeToBus2 + " seconds away");
                if (currMarker != null) {
                    currMarker.hideInfoWindow();
                    currMarker.showInfoWindow();
                }
                if ((currMarker == mMarker1 && timeToBus1 > 60) || (currMarker == mMarker2
                        && timeToBus2 > 60)) {
                    mWaitButton.setEnabled(false);
                    mWaitButton.setText(R.string.too_far);
                }
                else {
                    mWaitButton.setEnabled(true);
                    mWaitButton.setText(R.string.wait);
                }
            }
        });
    }

    private int calculateWalkTime(Location a, Location b) {
        return (int) (a.distanceTo(b) / WALK_SPEED);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
