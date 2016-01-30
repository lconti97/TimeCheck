package com.hoya.vt.timecheck;

        import android.app.Application;

/**
 * Created by Akshay on 1/30/2016.
 */
public class GlobalClass extends Application {

    public String currentStatus;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String newString) {
        currentStatus = newString;
    }
}
