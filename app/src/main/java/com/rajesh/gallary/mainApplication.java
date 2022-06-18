package com.rajesh.gallary;

import android.app.Application;
import android.content.res.Resources;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class mainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
