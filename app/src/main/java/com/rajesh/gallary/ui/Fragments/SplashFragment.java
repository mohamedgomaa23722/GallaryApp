package com.rajesh.gallary.ui.Fragments;

import static com.rajesh.gallary.common.Constant.EXTERNAL_IMAGE;
import static com.rajesh.gallary.common.Constant.EXTERNAL_VIDEO;
import static com.rajesh.gallary.common.Constant.IMAGE_PROJECTION;
import static com.rajesh.gallary.common.Constant.IS_DATA_SAVED_IN_CACHE;
import static com.rajesh.gallary.common.Constant.LOCK_ENABLE;
import static com.rajesh.gallary.common.Constant.VIDEO_PROJECTION;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.ActivitySplashBinding;
import com.rajesh.gallary.databinding.FragmentSplashBinding;
import com.rajesh.gallary.ui.Activities.MainActivity;
import com.rajesh.gallary.ui.viewModels.MainViewModel;
import com.rajesh.gallary.utils.RequiredPermission;
import com.rajesh.gallary.utils.SavedData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding;
    private static final String TAG = "SplashActivity";
    private MainViewModel viewModel;
    boolean isok = false;
    @Inject
    SavedData savedData;

    @Inject
    RequiredPermission requiredPermission;
    int progressNumbers = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        if (savedData.getBooleanValue(IS_DATA_SAVED_IN_CACHE, false) && isPermissionGranted()) {
                GoToMain();
        }
    }

    private void SetupData() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = () -> {
            savedData.setBooleanValue(IS_DATA_SAVED_IN_CACHE, true);
            binding.finishp.setVisibility(View.INVISIBLE);
            GoToMain();
        };
        executorService.execute(() -> {
            Log.d(TAG, "SetupData: initialize data");
            viewModel.initializeMediaData(EXTERNAL_IMAGE, IMAGE_PROJECTION, true);
            viewModel.initializeMediaData(EXTERNAL_VIDEO, VIDEO_PROJECTION, false);
            handler.post(runnable);
        });
    }

    private void GoToMain() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return requiredPermission.isPermissionGranted();
        } else {
            return requiredPermission.isPermissionGrantedForLoweApi();
        }
    }

    private void ApplyPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requiredPermission.WritePermission(getActivity());
        } else {
            requiredPermission.ReadPermission(getActivity());
            requiredPermission.WritePermissionForLowerApi(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!savedData.getBooleanValue(IS_DATA_SAVED_IN_CACHE, false) && isPermissionGranted()) {
            SetupData();
        } else if (!isPermissionGranted()) {
            ApplyPermissions();
        }
    }
}