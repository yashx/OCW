package com.github.yashx.mit_ocw;

import android.app.Application;

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;

public class OcwApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InternetAvailabilityChecker.init(this);
    }
}
