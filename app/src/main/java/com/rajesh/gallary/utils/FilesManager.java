package com.rajesh.gallary.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.rajesh.gallary.model.mediaModel;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class FilesManager {
    private Context context;

    @Inject
    public FilesManager(@ApplicationContext Context context) {
        this.context = context;
    }
}
