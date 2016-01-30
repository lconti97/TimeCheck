package com.hoya.vt.timecheck;

        import android.app.Application;
        import android.content.Context;
        import android.content.SharedPreferences;

/**
 * Created by Akshay on 1/30/2016.
 */
public class GlobalClass extends Application {

    public String currentStatus;
    public static final String PREFS_NAME = "MyPrefsFile";

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPref = getSharedPreferences(PREFS_NAME, 0);
        editor = sharedPref.edit();
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String newString) {
        currentStatus = newString;
    }
}
