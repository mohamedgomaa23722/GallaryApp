package com.rajesh.gallary.ui.Activities;

import static com.rajesh.gallary.common.Constant.BLACK_THEME;
import static com.rajesh.gallary.common.Constant.FROM_PASSWORD_TO_SECURITY;
import static com.rajesh.gallary.common.Constant.FROM_PASSWORD_TO_VAULT;
import static com.rajesh.gallary.common.Constant.FROM_SECURITY_TO_SETTINGS;
import static com.rajesh.gallary.common.Constant.FROM_SETTINGS_TO_SECURITY;
import static com.rajesh.gallary.common.Constant.FROM_SETTINGS_TO_VAULT;
import static com.rajesh.gallary.common.Constant.FROM_SETTING_TO_PASSWORD;
import static com.rajesh.gallary.common.Constant.FROM_VAULT_TO_SETTINGS;
import static com.rajesh.gallary.common.Constant.LOCK_ENABLE;
import static com.rajesh.gallary.common.Constant.RESTART_FRAGMENT;
import static com.rajesh.gallary.common.Constant.SHARED_P_NAME;
import static com.rajesh.gallary.common.Constant.THEME;
import static com.rajesh.gallary.common.Constant.WHITE_THEME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.common.graph.Graph;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.ActivitySettingBinding;
import com.rajesh.gallary.network.SecurityCommunicator;
import com.rajesh.gallary.ui.viewModels.SettingsViewModel;
import com.rajesh.gallary.utils.SavedData;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingActivity extends AppCompatActivity implements SecurityCommunicator {
    @Inject
    SavedData savedData;

    ActivitySettingBinding binding;
    private SettingsViewModel viewModel;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.SettingsfragmentContainerView);
        navController = navHostFragment.getNavController();

        viewModel.getSettingsCommunication().observe(this, s -> {
            switch (s) {
                case FROM_SETTINGS_TO_VAULT:
                case FROM_SETTINGS_TO_SECURITY:
                        //Go to password destination
                        Bundle destination = new Bundle();
                        destination.putString("DES", s);
                        navController.popBackStack(R.id.action_settingsFragment_to_passwordFragment, true);
                        navController.navigate(R.id.action_settingsFragment_to_passwordFragment, destination);
                    break;
                case FROM_SECURITY_TO_SETTINGS:
                    MoveToDestination(R.id.action_securityFragment_to_settingsFragment);
                    break;
                case FROM_VAULT_TO_SETTINGS:
                    MoveToDestination(R.id.action_vaultFragmnet_to_settingsFragment);
                    break;
                case FROM_PASSWORD_TO_VAULT:
                    MoveToDestination(R.id.action_passwordFragment_to_vaultFragmnet);
                    break;
                case FROM_PASSWORD_TO_SECURITY:
                    MoveToDestination(R.id.action_passwordFragment_to_securityFragment);
                    break;
                case BLACK_THEME:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    savedData.setBooleanValue(BLACK_THEME,true);
                    break;
                case WHITE_THEME:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    savedData.setBooleanValue(BLACK_THEME,false);
                    break;
            }
        });

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (navController.getCurrentDestination().getId() == R.id.passwordFragment) {
            MoveToDestination(R.id.action_passwordFragment_to_settingsFragment);
            Toast.makeText(this, "move from password", Toast.LENGTH_SHORT).show();
        }
    }


    private void MoveToDestination(int DestinationID){
        navController.popBackStack(DestinationID, true);
        navController.navigate(DestinationID);
    }

    @Override
    public void ValidateSecurity(boolean isValidate) {
      if (isValidate){

      }
    }
}