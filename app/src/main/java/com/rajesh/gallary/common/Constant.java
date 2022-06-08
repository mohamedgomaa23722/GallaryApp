package com.rajesh.gallary.common;

import android.net.Uri;
import android.provider.MediaStore;

public class Constant {

    public static final String MEDIA_DATE_TABLE = "MEDIA_DATE_TABLE";
    public static final String MEDIA_DATA_TABLE = "MEDIA_DATA_TABLE";
    public static final String ALBUM_TABLE = "ALBUM_TABLE";

    public static final String MEDIA_DATE_ID = "MEDIA_DATE_ID";
    // Media Data Table
    public static final String MEDIA_ID = "MEDIA_ID";
    public static final String MEDIA_NAME = "MEDIA_NAME";
    public static final String MEDIA_PATH = "MEDIA_PATH";
    public static final String MEDIA_SIZE = "MEDIA_SIZE";
    public static final String MEDIA_DATE = "MEDIA_DATE";
    public static final String MEDIA_TYPE = "MEDIA_TYPE";

    //Tags
    public static final String MAIN_VIEW_MODEL = "MAIN_VIEW_MODEL";

    //Content Provider Constants
    // Media URIS
    public static final Uri EXTERNAL_IMAGE = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public static final Uri EXTERNAL_VIDEO = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    //Media projection
    public static final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID};

    public static final String[] VIDEO_PROJECTION = {
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID};

    // Saved Data and Dates
    public static final String SHARED_P_NAME = "SHARED_DATA";
    public static final String IS_DATA_SAVED_IN_CACHE = "IS_SAVED";

    //Permission Request
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    //Video Constants
    public static final int PLAY_VIDEO = 0;
    public static final int PAUSE_VIDEO = 1;


}
