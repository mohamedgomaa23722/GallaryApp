package com.rajesh.gallary.ui.Dialogs;

import static com.rajesh.gallary.common.Constant.INCLUDE_VIDEO;
import static com.rajesh.gallary.common.Constant.LOOP_VIDEO;
import static com.rajesh.gallary.common.Constant.TIME;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.rajesh.gallary.R;
import com.rajesh.gallary.utils.SavedData;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AutoSliderDialog extends DialogFragment {
    @Inject
    SavedData savedData;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog
        View view = inflater.inflate(R.layout.slider_sheet, null);
        //Declare Views
        TextInputEditText Time_edx = view.findViewById(R.id.Time_mill);
        SwitchMaterial Video_Include_switch = view.findViewById(R.id.slider_video_enable);
        SwitchMaterial Repeat = view.findViewById(R.id.slider_repeat);
        Button ChangeBtn = view.findViewById(R.id.Slider_ok_Btn);
        Button cancelBtn = view.findViewById(R.id.SLider_cancle_Btn);
        //set Data
        Time_edx.setText(String.valueOf(savedData.getIntegerValue(TIME)));
        Video_Include_switch.setChecked(savedData.getBooleanValue(INCLUDE_VIDEO,true));
        Repeat.setChecked(savedData.getBooleanValue(LOOP_VIDEO,true));
        //Handle On click
        ChangeBtn.setOnClickListener(v -> {
            savedData.setIntegerValue(TIME, Integer.parseInt(Time_edx.getText().toString()));
            savedData.setBooleanValue(INCLUDE_VIDEO, Video_Include_switch.isChecked());
            savedData.setBooleanValue(LOOP_VIDEO, Repeat.isChecked());
            this.dismiss();
        });

        cancelBtn.setOnClickListener(v -> {
            this.dismiss();
        });
        builder.setView(view);
        return builder.create();
    }
}
