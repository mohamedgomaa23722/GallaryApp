package com.rajesh.gallary.ui.Fragments;

import static com.rajesh.gallary.common.Constant.EXTERNAL_IMAGE;
import static com.rajesh.gallary.common.Constant.EXTERNAL_VIDEO;
import static com.rajesh.gallary.common.Constant.IMAGE_PROJECTION;
import static com.rajesh.gallary.common.Constant.IS_DATA_SAVED_IN_CACHE;
import static com.rajesh.gallary.common.Constant.LOCK_ENABLE;
import static com.rajesh.gallary.common.Constant.VIDEO_PROJECTION;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rajesh.gallary.BuildConfig;

import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.FragmentSplashBinding;
import com.rajesh.gallary.network.DialogCommunicator;
import com.rajesh.gallary.ui.Activities.MainActivity;
import com.rajesh.gallary.ui.Dialogs.PermissionDialog;
import com.rajesh.gallary.ui.viewModels.MainViewModel;
import com.rajesh.gallary.utils.SavedData;

import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashFragment extends Fragment implements DialogCommunicator<Boolean>, View.OnClickListener {

    private FragmentSplashBinding binding;
    private MainViewModel viewModel;
    private ActivityResultLauncher<String[]> permissionLauncher;
    private ActivityResultContracts.RequestMultiplePermissions multiplePermissions;
    private boolean isReadPermissionGranted = false;
    private boolean isWritePermissionGranted = false;
    private boolean isManagePermissionGranted = false;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    @Inject
    SavedData savedData;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            CheckDataProgress();
        } else {
            multiplePermissions = new ActivityResultContracts.RequestMultiplePermissions();
            permissionLauncher = registerForActivityResult(multiplePermissions, permissions -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    isReadPermissionGranted = permissions.get(Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (isReadPermissionGranted && isManagePermissionGranted && !savedData.getBooleanValue(IS_DATA_SAVED_IN_CACHE, false)) {
                        SetupData();
                        permissionLauncher.unregister();
                    }
                } else {
                    isReadPermissionGranted = permissions.get(Manifest.permission.READ_EXTERNAL_STORAGE);
                    isWritePermissionGranted = permissions.get(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (isReadPermissionGranted && isWritePermissionGranted && !savedData.getBooleanValue(IS_DATA_SAVED_IN_CACHE, false)) {
                        SetupData();
                        permissionLauncher.unregister();
                    }
                }
            });
            requestPermissions();
            if (isReadPermissionGranted && isWritePermissionGranted) {
                CheckDataProgress();
            }
        }
        binding.AllowPermission.setOnClickListener(this);

    }

    private void CheckDataProgress() {
        //check if data is already saved or not
        //Then Set up data
        if (savedData.getBooleanValue(IS_DATA_SAVED_IN_CACHE, false)) {
            //saved then move to main
            GoToMain();
        } else {
            //setup data
            SetupData();
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

//    public boolean isPermissionGranted() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            return requiredPermission.isPermissionGranted();
//        } else {
//            return requiredPermission.isPermissionGrantedForLoweApi();
//        }
//    }

//    private void ApplyPermissions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            requiredPermission.WritePermission(getActivity());
//        } else {
//            requiredPermission.ReadPermission(getActivity());
//            requiredPermission.WritePermissionForLowerApi(getActivity());
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void requestPermissions() {
        boolean isReadPermission = ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        boolean isWritePermission = ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            isManagePermissionGranted = ContextCompat.checkSelfPermission(
                    getContext(),
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }

        boolean minSdk = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

        isReadPermissionGranted = isReadPermission;
        isWritePermissionGranted = isWritePermission || minSdk;
        ArrayList<String> RequestedPermissions = new ArrayList<>();

        if (!isReadPermissionGranted) {
            RequestedPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!isWritePermissionGranted) {
            RequestedPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!isManagePermissionGranted) {
                RequestedPermissions.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
            }
        }

        if (!RequestedPermissions.isEmpty()) {
            String[] requested = new String[RequestedPermissions.size()];
            permissionLauncher.launch(RequestedPermissions.toArray(requested));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void InitializePermissionForRApi() {

        ActivityResultContracts.StartActivityForResult startActivityForResult = new ActivityResultContracts.StartActivityForResult();
        activityResultLauncher = registerForActivityResult(startActivityForResult, result -> {
            if (Environment.isExternalStorageManager() && !savedData.getBooleanValue(IS_DATA_SAVED_IN_CACHE, false)) {
                // Permission granted. Now resume your workflow.
                binding.finishp.setVisibility(View.VISIBLE);
                binding.PermissionStatue.setText("Wait until finished some operations");
                binding.AllowPermission.setVisibility(View.INVISIBLE);
                SetupData();
                activityResultLauncher.unregister();
            } else {
                DisplayPermissionDialog();
                binding.finishp.setVisibility(View.INVISIBLE);
                binding.AllowPermission.setVisibility(View.VISIBLE);
                binding.PermissionStatue.setText("Please Allow Application to access all files to improve your features");
            }
        });
        AddPermissionToLauncher();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void DisplayPermissionDialog() {
        PermissionDialog PermissionDialog = new PermissionDialog();
        PermissionDialog.show(getActivity().getSupportFragmentManager(), "permission");
        PermissionDialog.setPermissionCommunicator(this);
    }

    private void AddPermissionToLauncher() {
        Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
        Intent StorageIntent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
        activityResultLauncher.launch(StorageIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void DialogMessage(Boolean isValidate) {
        if (isValidate) {
            AddPermissionToLauncher();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.AllowPermission) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R)
                AddPermissionToLauncher();
            else
                requestPermissions();
        }
    }
}