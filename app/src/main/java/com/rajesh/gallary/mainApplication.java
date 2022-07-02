package com.rajesh.gallary;

import static com.rajesh.gallary.common.Constant.BLACK_THEME;

import android.app.Application;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatDelegate;

import com.rajesh.gallary.utils.SavedData;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class mainApplication extends Application {
    @Inject
    SavedData savedData;
    @Override
    public void onCreate() {
        super.onCreate();
        //Check the theme of the device
        if (savedData.getBooleanValue(BLACK_THEME,false)){
            //Apply Dark Theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            //Apply Light Theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
