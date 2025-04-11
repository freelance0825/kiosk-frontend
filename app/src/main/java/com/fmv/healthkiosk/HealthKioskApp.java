package com.fmv.healthkiosk;

import android.app.Application;
import com.jakewharton.threetenabp.AndroidThreeTen;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class HealthKioskApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize ThreeTenABP
        AndroidThreeTen.init(this);
    }
}