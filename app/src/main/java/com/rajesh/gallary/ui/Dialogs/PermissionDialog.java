package com.rajesh.gallary.ui.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rajesh.gallary.network.DialogCommunicator;

public class PermissionDialog extends DialogFragment {

   private DialogCommunicator<Boolean> permissionCommunicator;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Allow some permission to continue");
        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               permissionCommunicator.DialogMessage(true);
            }
        });
        return builder.create();
    }

    public void setPermissionCommunicator(DialogCommunicator<Boolean> permissionCommunicator) {
        this.permissionCommunicator = permissionCommunicator;
    }
}
