package com.hoya.vt.timecheck;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {


    protected GlobalClass globals;
    Resources res;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "75YiknXrR89hvbnIsqK3HUipp";
    private static final String TWITTER_SECRET = "lmUpO2m5m2Rd08klkPWPey4TDQAV9fKpwz7l13yfXrTIvoftcA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        globals = (GlobalClass) getApplication();
        res = getResources();
        //cardView = (CardView)


        if (globals.sharedPref.getString(res.getString(R.string.currentStatus), "empty").equals("Bus Driver")) {
            startActivity(new Intent (this, DriverActivity.class));
            finish();
        }

        if (globals.sharedPref.getString(res.getString(R.string.currentStatus), "empty").equals("Passenger")) {
            startActivity(new Intent(this, RiderActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);
    }

    public void launchDriverActivity(View view) {
        globals.editor.remove(res.getString(R.string.currentStatus));
        globals.editor.commit();
        globals.editor.putString(res.getString(R.string.currentStatus), "Bus Driver");
        globals.editor.commit();
        Intent intent = new Intent (this, DriverActivity.class);
        startActivity(intent);
        finish();
    }

    public void launchRiderActivity(View view) {
        globals.editor.remove(res.getString(R.string.currentStatus));
        globals.editor.commit();
        globals.editor.putString(res.getString(R.string.currentStatus), "Passenger");
        globals.editor.commit();
        Intent intent = new Intent (this, RiderActivity.class);
        startActivity(intent);
        finish();
    }
}