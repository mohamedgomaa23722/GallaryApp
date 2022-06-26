package com.rajesh.gallary.ui.Fragments.SettingFragments;

import static android.content.Context.FINGERPRINT_SERVICE;
import static com.rajesh.gallary.common.Constant.CONFIRM_PATTERN;
import static com.rajesh.gallary.common.Constant.FINGER_PRINT_ENABLE;
import static com.rajesh.gallary.common.Constant.FIRST_STEP;
import static com.rajesh.gallary.common.Constant.FROM_PASSWORD_TO_SECURITY;
import static com.rajesh.gallary.common.Constant.FROM_PASSWORD_TO_VAULT;
import static com.rajesh.gallary.common.Constant.FROM_SETTINGS_TO_SECURITY;
import static com.rajesh.gallary.common.Constant.FROM_SETTINGS_TO_VAULT;
import static com.rajesh.gallary.common.Constant.FROM_SPLASH_SCREEN_TO_HOME;
import static com.rajesh.gallary.common.Constant.NEW_PATTERN;
import static com.rajesh.gallary.common.Constant.PATTERN_ENABLE;
import static com.rajesh.gallary.common.Constant.PATTERN_KEY;
import static com.rajesh.gallary.common.Constant.QUESTION;
import static com.rajesh.gallary.common.Constant.RESTART_FRAGMENT;
import static com.rajesh.gallary.common.Constant.SECOND_STEP;
import static com.rajesh.gallary.common.Constant.SHEMA_FAILED;
import static com.rajesh.gallary.common.Constant.STATUS_FIRST_STEP;
import static com.rajesh.gallary.common.Constant.STATUS_Next_STEP;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.FragmentPasswordBinding;
import com.rajesh.gallary.network.SecurityCommunicator;
import com.rajesh.gallary.ui.Activities.MainActivity;
import com.rajesh.gallary.ui.Dialogs.SecurityQuestionDialog;
import com.rajesh.gallary.ui.viewModels.SettingsViewModel;
import com.rajesh.gallary.utils.FIngerPrintHelper.FingerPrintChecker;
import com.rajesh.gallary.utils.PasswordHelper;
import com.rajesh.gallary.utils.SavedData;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PasswordFragment extends Fragment implements View.OnClickListener, SecurityCommunicator {
    private FragmentPasswordBinding binding;

    private String FirstPattern = "";
    private String SecondPattern = "";
    private int Step = FIRST_STEP;
    private FingerprintManager fingerprintManager;
    private SettingsViewModel viewModel;
    private CancellationSignal cancellationSignal;
    @Inject
    SavedData savedData;

    String Dis = FROM_SPLASH_SCREEN_TO_HOME;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPasswordBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(SettingsViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null)
            Dis = bundle.getString("DES");
        SetUpView();
    }

    private void InitFingerPrint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.fingerPrintIcon.setVisibility(View.VISIBLE);
            fingerprintManager = (FingerprintManager) getActivity().getSystemService(FINGERPRINT_SERVICE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void FingerListener() {
        cancellationSignal = new CancellationSignal();
        FingerprintManager.AuthenticationCallback authenticationCallback = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.d("TAG", "onAuthenticationError: " + errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (savedData.getBooleanValue(FINGER_PRINT_ENABLE, false) && getActivity().getPackageName() != null) {
                    Toast.makeText(getActivity(), "Succeeded", Toast.LENGTH_SHORT).show();
                    GoToDestination();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                super.onAuthenticationFailed();
            }
        };

        fingerprintManager.authenticate(null, cancellationSignal, 0, authenticationCallback, null);
    }

    private void SetUpView() {
        //Get Scenario Value
        SetUpLayout();
        initPatternListener();
        if (savedData.getBooleanValue(FINGER_PRINT_ENABLE, false)) {
            InitFingerPrint();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                FingerListener();
            }
        }
        //Handle Forget Password clicked
        binding.forgetPasswordTxt.setOnClickListener(this);

    }

    private void initPatternListener() {
        binding.patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String PatternPassword = PatternLockUtils.patternToString(binding.patternLockView, pattern);
                if (PatternPassword.length() >= 4) {
                    if (savedData.getStringValue(PATTERN_KEY) == null || !savedData.getBooleanValue("IsValidate", true)) {
                        //This Mean that This Case is new one
                        //so we will initialize pattern as input
                        if (Step == FIRST_STEP) {
                            //when First pattern is already available
                            // then show step two
                            binding.PatternInstruction.setText(STATUS_Next_STEP);
                            binding.stepView.go(1, true);
                            Step = SECOND_STEP;
                            FirstPattern = PatternPassword;
                        } else {
                            //second Step
                            SecondPattern = PatternPassword;
                            binding.PatternInstruction.setText(STATUS_Next_STEP);

                            if (FirstPattern.equals(SecondPattern)) {
                                //Save Pattern
                                savedData.setStringValue(PATTERN_KEY, FirstPattern);
                                savedData.setBooleanValue("IsValidate", true);
                                //Continue To Destination
                                GoToDestination();
                            }
                        }

                    } else {
                        //Show single pattern entered
                        if (PatternPassword.equals(savedData.getStringValue(PATTERN_KEY))) {
                            //Continue To our Destination
                            GoToDestination();
                        } else {
                            //Show Message to let him enter pattern again
                            binding.PatternInstruction.setText("Faild Pattern, Try Again");
                        }
                    }
                } else if (PatternPassword.length() != 0 && PatternPassword.length() < 4) {

                    binding.PatternInstruction.setText(SHEMA_FAILED);
                    binding.patternLockView.clearPattern();
                    return;
                }

                binding.patternLockView.clearPattern();
            }

            @Override
            public void onCleared() {

            }
        });
    }

    private void SetUpLayout() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            binding.fingerPrintIcon.setVisibility(View.INVISIBLE);
        }
        if (savedData.getStringValue(PATTERN_KEY) == null || !savedData.getBooleanValue("IsValidate", true)) {
            binding.patternLockView.setVisibility(View.VISIBLE);
            binding.PatternInstruction.setText(STATUS_FIRST_STEP);
            binding.stepView.setVisibility(View.VISIBLE);
            binding.stepView.setStepsNumber(2);
            binding.PatternInstruction.setText(NEW_PATTERN);
            binding.stepView.go(0, true);
            binding.fingerPrintIcon.setVisibility(View.INVISIBLE);
            binding.forgetPasswordTxt.setVisibility(View.INVISIBLE);
        } else {
            binding.PatternInstruction.setText(STATUS_FIRST_STEP);
            binding.stepView.setVisibility(View.GONE);
            if (!savedData.getBooleanValue(PATTERN_ENABLE, false)) {
                binding.patternLockView.setVisibility(View.INVISIBLE);
            } else if (!savedData.getBooleanValue(FINGER_PRINT_ENABLE, false)) {
                binding.fingerPrintIcon.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void GoToDestination() {
        if (Dis.equals(FROM_SETTINGS_TO_VAULT)) {
            viewModel.setSettingsData(FROM_PASSWORD_TO_VAULT);
        } else if (Dis.equals(FROM_SETTINGS_TO_SECURITY)) {
            //go to vault
            viewModel.setSettingsData(FROM_PASSWORD_TO_SECURITY);
        } else if (Dis.equals(FROM_SPLASH_SCREEN_TO_HOME)) {
            //go to home
            Intent intent = new Intent(getActivity(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.forgetPassword_txt) {
            //Display Question Dialog
            SecurityQuestionDialog(savedData.getSecurity(QUESTION));
        }
    }

    private void SecurityQuestionDialog(String Question) {
        SecurityQuestionDialog SecurityQuestionDialog = new SecurityQuestionDialog();
        Bundle bundle = new Bundle();
        bundle.putString(QUESTION, Question);
        SecurityQuestionDialog.setArguments(bundle);
        SecurityQuestionDialog.show(getActivity().getSupportFragmentManager(), "SecurityQuestionDialog");
        SecurityQuestionDialog.setSecurityCommunicator(this);
    }


    @Override
    public void ValidateSecurity(boolean isValidate) {
        if (isValidate) {
            // Here we need to give user permission to change the password
            savedData.setBooleanValue("IsValidate", false);
            savedData.setBooleanValue(FINGER_PRINT_ENABLE, false);
            //Then we need to reInitialize this fragment
            SetUpView();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPause() {
        super.onPause();
        if(cancellationSignal != null)
        cancellationSignal.cancel();
    }
}