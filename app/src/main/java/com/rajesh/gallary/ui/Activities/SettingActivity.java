package com.rajesh.gallary.ui.Activities;

import static com.rajesh.gallary.common.Constant.SHARED_P_NAME;
import static com.rajesh.gallary.common.Constant.THEME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.rajesh.gallary.R;
import com.rajesh.gallary.utils.SavedData;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingActivity extends AppCompatActivity {
    @Inject
    SavedData savedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}