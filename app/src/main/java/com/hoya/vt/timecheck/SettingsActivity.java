package com.hoya.vt.timecheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    TextView statusText;
    Switch statusSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        statusText = (TextView) findViewById(R.id.settingsStatusText);
        statusSwitch = (Switch) findViewById(R.id.settingsStatusSwitch);
        setTitle("Settings");

        statusText.setText("Remove current status as ");


    }
}
