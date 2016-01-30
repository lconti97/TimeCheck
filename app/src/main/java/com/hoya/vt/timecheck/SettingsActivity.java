package com.hoya.vt.timecheck;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    TextView statusTextView;
    Switch statusSwitch;
    GlobalClass globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_settings_activitty);

        globals = (GlobalClass) getApplication();
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        statusSwitch = (Switch) findViewById(R.id.statusSwitch);

        if (globals.getCurrentStatus().equals("Bus Driver")) {
            statusTextView.setText("Reset current status from Bus Driver");
        }
        else {
            statusTextView.setText("Reset current status from Passenger");
        }

    }

    public void resetCurrentStatus(View view) {

    }

    public void applyChanges(View view) {

        if (statusSwitch.isChecked()) {

        }
    }


}
