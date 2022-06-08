package com.rajesh.gallary.utils;

import static com.rajesh.gallary.common.Constant.IS_DATA_SAVED_IN_CACHE;
import static com.rajesh.gallary.common.Constant.SHARED_P_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class SavedData {
    SharedPreferences sharedpreferences;


    @Inject
    public SavedData(@ApplicationContext Context context) {
        sharedpreferences = context.getSharedPreferences(SHARED_P_NAME, Context.MODE_PRIVATE);
    }

    @SuppressLint("CommitPrefEdits")
    public void Saved_On_Cache(boolean saved) {
        sharedpreferences.edit().putBoolean(IS_DATA_SAVED_IN_CACHE, true).commit();
    }

    public boolean Is_Data_Saved() {
        return sharedpreferences.getBoolean(IS_DATA_SAVED_IN_CACHE, false);
    }
}
