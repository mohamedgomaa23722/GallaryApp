package com.rajesh.gallary.ui.Fragments.SettingFragments;

import static com.rajesh.gallary.common.Constant.QUESTION;
import static com.rajesh.gallary.common.Constant.THEME;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.FragmentSettingsBinding;
import com.rajesh.gallary.ui.Activities.SettingActivity;
import com.rajesh.gallary.ui.Dialogs.AutoSliderDialog;
import com.rajesh.gallary.ui.Dialogs.SecurityQuestionDialog;
import com.rajesh.gallary.utils.SavedData;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class settingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private FragmentSettingsBinding binding;
    @Inject
    SavedData savedData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SetUpView();
    }

    private void SetUpView() {
        //On switch clicked
        binding.DarkTheme.setOnCheckedChangeListener(this);
        binding.playbackSwitch.setOnCheckedChangeListener(this);
        // on click items
        //Lock app on clicked
        binding.LockApp.setOnClickListener(this);
        binding.lockInstructions.setOnClickListener(this);
        //vault on click
        binding.Vault.setOnClickListener(this);
        binding.VaultInstructions.setOnClickListener(this);
        //Auto Slider on click
        binding.Slider.setOnClickListener(this);
        binding.SliderInstructions.setOnClickListener(this);
        //Rate on click
        binding.rateApp.setOnClickListener(this);
        //share on click
        binding.shareApp.setOnClickListener(this);

        //Initialize theme
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            binding.DarkTheme.setChecked(true);
        else
            binding.DarkTheme.setChecked(false);


    }

    private void AutoSliderSettingDialog() {
        DialogFragment AutoSliderDialog = new AutoSliderDialog();
        AutoSliderDialog.show(getActivity().getSupportFragmentManager(), "AutoSliderDialog");
    }

    private void SecurityQuestionDialog() {
        DialogFragment SecurityQuestionDialog = new SecurityQuestionDialog();
        SecurityQuestionDialog.show(getActivity().getSupportFragmentManager(), "SecurityQuestionDialog");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Go to lock app
            case R.id.LockApp:
            case R.id.lockInstructions:
                if (savedData.getSecurity(QUESTION).length() == 0)
                    SecurityQuestionDialog();
                else {

                }

                break;
            // Go to vault data
            case R.id.Vault:
            case R.id.VaultInstructions:
                if (savedData.getSecurity(QUESTION).length() == 0)
                    SecurityQuestionDialog();
                else {
                    //G
                }
                break;
            // Rate App
            case R.id.rateApp:
                break;
            //Share App
            case R.id.shareApp:
                break;
            //Auto Slider Settings
            case R.id.Slider:
            case R.id.SliderInstructions:
                //Open Bottom Sheet
                AutoSliderSettingDialog();
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.DarkTheme:
//                if (b)
//                    savedData.setIntegerValue(THEME, R.style.Dark);
//                else
//                    savedData.setIntegerValue(THEME, R.style.Light);
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                break;
            case R.id.playbackSwitch:
                Toast.makeText(getContext(), "play back", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void RestartActivity() {
        startActivity(new Intent(getActivity(), SettingActivity.class));
        getActivity().finish();
    }
}