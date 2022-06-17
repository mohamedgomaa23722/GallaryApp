package com.rajesh.gallary.utils;

import static com.rajesh.gallary.common.Constant.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.rajesh.gallary.BuildConfig;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * We need to get the permission to access all media files by reading and write
 * so we need to handle all Api's permissions because there is diff of taken the permission
 */
public class RequiredPermission {

    private Context context;

    @Inject
    public RequiredPermission(@ApplicationContext Context context) {
        this.context = context;
    }

    /**
     * This Permission is for Api Bigger Than R
     */

    // Read permission
    public void ReadPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    //Write Permission
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void WritePermission(Activity activity) {
        Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
        activity.startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri));
    }
    //Check the permission is taken or not

    @RequiresApi(api = Build.VERSION_CODES.R)
    public Boolean isPermissionGranted() {
        return (Environment.isExternalStorageManager() && ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED);
    }

    /**
     * This permissions is for Api smaller than R
     */
    //write Permission
    public void WritePermissionForLowerApi(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                2);
    }

    //check the permission is taken or not
    public Boolean isPermissionGrantedForLoweApi() {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED);
    }
}
