package com.hoya.vt.timecheck;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class RiderActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Button mWaitButton;
    private Marker mCurrMarker;
    private Bus mBus1;
    private Bus mBus2;
    private Toolbar toolbar;

    private static final double BUS_LAT_1 = 38.906291;
    private static final double BUS_LNG_1 = -77.074834;
    private static final double BUS_LAT_2 = 38.906728;
    private static final double BUS_LNG_2 = -77.073733;

    private static final String ACCESS_TOKEN = "4862469189-nTQrHeWycUUmNzHcKypYlVUigMGevWzoHbGQEKp";
    private static final String ACCESS_TOKEN_SECRET = "nMrqffb03wCcigJmPolqeilWsNutCOuBeMy7xx3m8IS5N";
    private static final String CONSUMER_KEY = "WKxzxQpFZNtbU4ti0N0Sxigbk";
    private static final String CONSUMER_SECRET = "P8ySJD1tpC6Mh8KfXvhppz2ioj8uuBs8Mrow2KDIaNr6ATdgu9";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onOptionsItemSelected(item);
                return true;
            }
        });

        toolbar.inflateMenu(R.menu.menu_settings);
        toolbar.setTitle("Find a bus!");
        toolbar.setLogo(R.drawable.rider_icon);

        if (android.os.Build.VERSION.SDK_INT > 11) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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
                // TODO: check if the user is close enough
                tweet();
            }
        });
        mBus1 = new Bus("Last ride home", BUS_LAT_1, BUS_LNG_1);
        mBus2 = new Bus("The hype train", BUS_LAT_2, BUS_LNG_2);
    }

    public void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
        //test change
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settingsMenu) {
            launchSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tweet() {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Status status;
        try {
            status = twitter.updateStatus(currentDateandTime + " #PASSENGERALERT411 Passenger Arriving");
            Log.d("Updated the status to" + status.getText(), "TRUE");
        } catch (TwitterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        mBus1.placeMarker(mMap);
        mBus2.placeMarker(mMap);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mCurrMarker = marker;
                Log.i("Tag", "New marker id: " + mCurrMarker);
                mWaitButton.setVisibility(View.VISIBLE);
                return false;
            }
        });

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, LocationRequest
                .create().setInterval(1000),
                new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (mCurrMarker == null) {
                    return;
                }
                int time1 = mBus1.calcWalkTime(location);
                int time2 = mBus2.calcWalkTime(location);
                if (mCurrMarker!=null) {
                    Log.i("Tag", "Still " + mCurrMarker);
                    Log.i("Tag", "Bus1 " + mBus1.getMarker());
                }
                if (mCurrMarker.equals(mBus1.getMarker())) {
                    Log.i("Tag", "Time1 = " + time1);
                    if (time1 > 60) {
                        mWaitButton.setEnabled(false);
                        mWaitButton.setText("Too far away");
                    }
                    else {
                        mWaitButton.setEnabled(true);
                        mWaitButton.setText("Ask " + mBus1.getTitle() + " to wait");
                    }
                }
                else if (mCurrMarker.equals(mBus2.getMarker())) {
                    if (time2 > 60) {
                        mWaitButton.setEnabled(false);
                        mWaitButton.setText("Too far away");
                    }
                    else {
                        mWaitButton.setEnabled(true);
                        mWaitButton.setText("Ask " + mBus2.getTitle() + " to wait");
                    }
                }
                if (mCurrMarker != null) {
                    mCurrMarker.hideInfoWindow();
                    mCurrMarker.showInfoWindow();
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void launchSettings(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }


    //DEBUG PURPOSES
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
