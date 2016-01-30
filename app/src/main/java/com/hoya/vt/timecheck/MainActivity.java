package com.hoya.vt.timecheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "75YiknXrR89hvbnIsqK3HUipp";
    private static final String TWITTER_SECRET = "lmUpO2m5m2Rd08klkPWPey4TDQAV9fKpwz7l13yfXrTIvoftcA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        Button busButton = (Button) findViewById(R.id.busButtonID);
        Button passengerButton = (Button) findViewById(R.id.passengerButtonID);
    }

    public void launchDriverActivity() {
        Intent intent = new Intent (this, DriverActivity.class);
        startActivity(intent);
    }

    public void launchRiderActivity() {
        Intent intent = new Intent (this, RiderActivity.class);
        startActivity(intent);
    }
}
