package com.rajesh.gallary.utils;

import static com.rajesh.gallary.common.Constant.GRID_COUNT;
import static com.rajesh.gallary.common.Constant.IS_DATA_SAVED_IN_CACHE;
import static com.rajesh.gallary.common.Constant.LAST_DATE;
import static com.rajesh.gallary.common.Constant.QUESTION;
import static com.rajesh.gallary.common.Constant.SHARED_P_NAME;
import static com.rajesh.gallary.common.Constant.VIEW_TYPE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.rajesh.gallary.R;

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
        sharedpreferences.edit().putBoolean(IS_DATA_SAVED_IN_CACHE, saved).apply();
    }

    public boolean Is_Data_Saved() {
        return sharedpreferences.getBoolean(IS_DATA_SAVED_IN_CACHE, false);
    }


    @SuppressLint("CommitPrefEdits")
    public void SetLastMediaDate(long LastMediaDate) {
        sharedpreferences.edit().putLong(IS_DATA_SAVED_IN_CACHE, LastMediaDate).commit();
    }

    public long getLastMediaDate() {
        return sharedpreferences.getLong(IS_DATA_SAVED_IN_CACHE, 0);
    }


    //Recycler View Ui Data
    //VIew Type Data
    // if 0 grid view else list view
    @SuppressLint("CommitPrefEdits")
    public void setViewType(int Type, String key) {
        sharedpreferences.edit().putInt(key, Type).apply();
    }

    public int getViewType(String key) {
        return sharedpreferences.getInt(key, 0);
    }

    /**
     * Grid Count of items
     *
     * @param count
     */
    @SuppressLint("CommitPrefEdits")
    public void SetGridCount(int count, String Key) {
        sharedpreferences.edit().putInt(Key, count).apply();
    }

    public int getGridCount(String Key) {
        return sharedpreferences.getInt(Key, 4);
    }

    /**
     * Save last data date updated
     *
     * @param date
     */
    public void setLastDate(long date) {
        sharedpreferences.edit().putLong(LAST_DATE, date + 1).apply();
    }

    public long getLastDate() {
        return sharedpreferences.getLong(LAST_DATE, 0);
    }


    /**
     * Security Data
     */
    public void setSecurity(String flag, String value) {
        sharedpreferences.edit().putString(flag, value).apply();

    }

    public String getSecurity(String flag) {
        return sharedpreferences.getString(flag, "");
    }

    /**
     * Integers values
     */
    public void setIntegerValue(String flag, int value) {
        sharedpreferences.edit().putInt(flag, value).apply();
    }

    public int getIntegerValue(String flag) {
        return sharedpreferences.getInt(flag, 2000);
    }

    /**
     * boolean values
     */
    public void setBooleanValue(String flag, boolean value) {
        sharedpreferences.edit().putBoolean(flag, value).apply();
    }

    public boolean getBooleanValue(String flag,boolean defaultValue) {
        return sharedpreferences.getBoolean(flag, defaultValue);
    }

    /**
     * String Values
     */
    public void setStringValue(String flag, String value) {
        sharedpreferences.edit().putString(flag, value).apply();
    }

    public String getStringValue(String flag) {
        return sharedpreferences.getString(flag, null);
    }

}
