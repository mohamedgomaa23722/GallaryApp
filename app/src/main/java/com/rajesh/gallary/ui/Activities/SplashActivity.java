package com.rajesh.gallary.ui.Activities;

import static com.rajesh.gallary.common.Constant.EXTERNAL_IMAGE;
import static com.rajesh.gallary.common.Constant.EXTERNAL_VIDEO;
import static com.rajesh.gallary.common.Constant.IMAGE_PROJECTION;
import static com.rajesh.gallary.common.Constant.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.rajesh.gallary.common.Constant.VIDEO_PROJECTION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
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
    private MainViewModel viewModel;
    private ActivitySplashBinding binding;
    boolean isok = false;
    @Inject
    SavedData savedData;

    @Inject
    RequiredPermission requiredPermission;
    int progressNumbers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        Ask_For_permission();
        if (savedData.Is_Data_Saved()) {
            Log.d(TAG, "onCreate: permion ");
            Toast.makeText(this, "Go to main", Toast.LENGTH_SHORT).show();
            GoToMain();
        }
    }

    private void SetupData() {
//        ExecutorService executorService = Executors.newFixedThreadPool(50);
//        Handler handler = new Handler(Looper.getMainLooper());
//        executorService.execute(() -> {
//            Log.d(TAG, "SetupData: initialize data");
//            viewModel.initializeMediaData(EXTERNAL_IMAGE, IMAGE_PROJECTION, true);
//            viewModel.initializeMediaData(EXTERNAL_VIDEO, VIDEO_PROJECTION, false);
//
//            handler.post(() -> {
//                Log.d(TAG, "SetupData: observe data");
//            });
//        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "SetupData: initialize data");
                viewModel.initializeMediaData(EXTERNAL_IMAGE, IMAGE_PROJECTION, true);
                viewModel.initializeMediaData(EXTERNAL_VIDEO, VIDEO_PROJECTION, false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        savedData.Saved_On_Cache(true);
                        binding.finishp.setVisibility(View.INVISIBLE);
                        GoToMain();
                    }
                });
            }
        }).start();
    }

    private void GoToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean Ask_For_permission() {
        requiredPermission.ReadPermission(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requiredPermission.WritePermission(this);
            return requiredPermission.isPermissionGranted();
        } else {
            requiredPermission.WritePermissionForLowerApi(this);
            return requiredPermission.isPermissionGrantedForLoweApi();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            SetupData();
        }
    }

}