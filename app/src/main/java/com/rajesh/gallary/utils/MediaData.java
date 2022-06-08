package com.rajesh.gallary.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.rajesh.gallary.Repository.MediaRepository;
import com.rajesh.gallary.model.DateOfMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.mediaDao;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class MediaData {
    /**
     * We have three main values we need to have
     * 1- Uri of the media
     * 2- projection for query
     * 3- check the media type : image or video
     * 4- insert the media data
     * 5- insert the media date
     */
    private Context context;
    private MediaRepository mediaRepository;




}
