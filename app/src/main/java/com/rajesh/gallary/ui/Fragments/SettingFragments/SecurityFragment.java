package com.rajesh.gallary.ui.Fragments.SettingFragments;

import static com.rajesh.gallary.common.Constant.FINGER_PRINT_ENABLE;
import static com.rajesh.gallary.common.Constant.FROM_SECURITY_TO_SETTINGS;
import static com.rajesh.gallary.common.Constant.LOCK_ENABLE;
import static com.rajesh.gallary.common.Constant.PATTERN_ENABLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.FragmentSecurityBinding;
import com.rajesh.gallary.ui.viewModels.SettingsViewModel;
import com.rajesh.gallary.utils.SavedData;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SecurityFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private FragmentSecurityBinding binding;
    private SettingsViewModel viewModel;

    @Inject
    SavedData savedData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSecurityBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(SettingsViewModel.class);
        SetUpView();
    }

    private void SetUpView() {
        //Set Data
        binding.EnableLock.setChecked(savedData.getBooleanValue(LOCK_ENABLE, false));
        binding.EnablePattern.setChecked(savedData.getBooleanValue(PATTERN_ENABLE, false));
        binding.EnableFingerPrint.setChecked(savedData.getBooleanValue(FINGER_PRINT_ENABLE, false));
        //Handle On clicks interface
        binding.EnableFingerPrint.setOnCheckedChangeListener(this);
        binding.EnableLock.setOnCheckedChangeListener(this);
        binding.EnablePattern.setOnCheckedChangeListener(this);
        binding.TitleBack.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.Enable_Lock:
                savedData.setBooleanValue(LOCK_ENABLE, b);
                break;
            case R.id.EnablePattern:
                savedData.setBooleanValue(PATTERN_ENABLE, b);
                break;
            case R.id.EnableFingerPrint:
                savedData.setBooleanValue(FINGER_PRINT_ENABLE, b);
                break;
        }
        //if pattern and finger print is disable so we will set
        //the lock as disable
        if (!savedData.getBooleanValue(PATTERN_ENABLE, false) &&
                !savedData.getBooleanValue(FINGER_PRINT_ENABLE, false)) {
            //disable lock
            savedData.setBooleanValue(LOCK_ENABLE, false);
            binding.EnableLock.setChecked(false);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.TitleBack)
            viewModel.setSettingsData(FROM_SECURITY_TO_SETTINGS);
    }
}