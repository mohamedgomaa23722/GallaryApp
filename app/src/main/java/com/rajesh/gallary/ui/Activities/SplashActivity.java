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
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.ActivitySplashBinding;
import com.rajesh.gallary.ui.viewModels.MainViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private MainViewModel viewModel;
    private ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if(Ask_For_permission()){
            Log.d(TAG, "onCreate: permion ");
            GoToMain();
        }
    }
    private void SetupData() {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(() -> {
            Log.d(TAG, "SetupData: initialize data");
            viewModel.initializeMediaData(getApplicationContext(), EXTERNAL_IMAGE, IMAGE_PROJECTION, true);
            viewModel.initializeMediaData(getApplicationContext(), EXTERNAL_VIDEO, VIDEO_PROJECTION, false);
            handler.post(() -> {
                Log.d(TAG, "SetupData: observe data");
                GoToMain();
            });
        });
    }

    private void GoToMain(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean Ask_For_permission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            SetupData();
        }
    }
}