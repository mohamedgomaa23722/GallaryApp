package com.rajesh.gallary.ui.Activities;

import static com.rajesh.gallary.common.Constant.EXTERNAL_IMAGE;
import static com.rajesh.gallary.common.Constant.EXTERNAL_VIDEO;
import static com.rajesh.gallary.common.Constant.IMAGE_PROJECTION;
import static com.rajesh.gallary.common.Constant.IS_DATA_SAVED_IN_CACHE;
import static com.rajesh.gallary.common.Constant.LOCK_ENABLE;
import static com.rajesh.gallary.common.Constant.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.rajesh.gallary.common.Constant.SHARED_P_NAME;
import static com.rajesh.gallary.common.Constant.THEME;
import static com.rajesh.gallary.common.Constant.VIDEO_PROJECTION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.ActivitySplashBinding;
import com.rajesh.gallary.network.InsertProgress;
import com.rajesh.gallary.ui.viewModels.MainViewModel;
import com.rajesh.gallary.utils.RequiredPermission;
import com.rajesh.gallary.utils.SavedData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    private ActivitySplashBinding binding;
    @Inject
    SavedData savedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.SplashfragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        if (savedData.getBooleanValue(LOCK_ENABLE, false))
            navController.navigate(R.id.action_settingsFragment_to_passwordFragment);
        else
        navController.navigate(R.id.action_settingsFragment_to_splashFragment);
    }
}