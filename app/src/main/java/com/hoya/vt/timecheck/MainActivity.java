package com.hoya.vt.timecheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    Button busButton;
    Button passengerButton;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "75YiknXrR89hvbnIsqK3HUipp";
    private static final String TWITTER_SECRET = "lmUpO2m5m2Rd08klkPWPey4TDQAV9fKpwz7l13yfXrTIvoftcA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        busButton = (Button) findViewById(R.id.busButtonID);
        passengerButton = (Button) findViewById(R.id.passengerButtonID);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        if (!sharedPref.contains("isBusDriver")) {
            editor.putBoolean("isBusDriver", false);
            editor.commit();
        }
        if (!sharedPref.contains("isPassenger")) {
            editor.putBoolean("isPassenger", false);
            editor.commit();
        }

        if(sharedPref.getBoolean("isBusDriver", false) == true) {
            startActivity(new Intent (this, DriverActivity.class));
        }
        else if (sharedPref.getBoolean("isPassenger", false == true)) {
            startActivity(new Intent(this, RiderActivity.class));
        }

        setContentView(R.layout.activity_main);
    }

    public void launchDriverActivity(View view) {
        editor.remove("isBusDriver");
        editor.commit();
        editor.putBoolean("isBusDriver", true);
        editor.commit();
        Intent intent = new Intent (this, DriverActivity.class);
        startActivity(intent);
    }

    public void launchRiderActivity(View view) {
        editor.remove("isPassenger");
        editor.commit();
        editor.putBoolean("isPassenger", true);
        editor.commit();
        Intent intent = new Intent (this, RiderActivity.class);
        startActivity(intent);
    }
}