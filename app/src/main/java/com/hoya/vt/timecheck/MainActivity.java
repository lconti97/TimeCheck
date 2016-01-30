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
    protected GlobalClass globals;

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
        globals = (GlobalClass) getApplication();


        if (!globals.sharedPref.contains("isBusDriver")) {
            globals.editor.putBoolean("isBusDriver", false);
            globals.editor.commit();
        }
        if (!globals.sharedPref.contains("isPassenger")) {
            globals.editor.putBoolean("isPassenger", false);
            globals.editor.commit();
        }

        if(globals.sharedPref.getBoolean("isBusDriver", false) == true) {
            globals.setCurrentStatus("Bus Driver");
            startActivity(new Intent (this, DriverActivity.class));
        }
        else if (globals.sharedPref.getBoolean("isPassenger", false == true)) {
            globals.setCurrentStatus("Passenger");
            startActivity(new Intent(this, RiderActivity.class));
        }

        setContentView(R.layout.activity_main);
    }

    public void launchDriverActivity(View view) {
        globals.editor.remove("isBusDriver");
        globals.editor.commit();
        globals.editor.remove("isPassenger");
        globals.editor.commit();
        globals.editor.putBoolean("isBusDriver", true);
        globals.editor.commit();
        globals.setCurrentStatus("Bus Driver");
        Intent intent = new Intent (this, DriverActivity.class);
        startActivity(intent);
    }

    public void launchRiderActivity(View view) {
        globals.editor.remove("isPassenger");
        globals.editor.commit();
        globals.editor.remove("isBusDriver");
        globals.editor.commit();
        globals.editor.putBoolean("isPassenger", true);
        globals.editor.commit();
        globals.setCurrentStatus("Passenger");
        Intent intent = new Intent (this, RiderActivity.class);
        startActivity(intent);
    }
}