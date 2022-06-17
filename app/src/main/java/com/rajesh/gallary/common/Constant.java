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
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.SIZE,
            MediaStore.Images.ImageColumns.DATE_ADDED,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.IS_PRIVATE,
            MediaStore.Images.ImageColumns._ID
    };

    public static final String[] VIDEO_PROJECTION = {
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.VideoColumns.IS_PRIVATE,
            MediaStore.Video.VideoColumns._ID
    };




    //Permission Request
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    //Video Constants
    public static final int PLAY_VIDEO = 0;
    public static final int PAUSE_VIDEO = 1;

    //Intent Data
    public static final String DATA = "DATA";
    public static final String ALBUM_DATA = "ALBUM_DATA";


    //Menu Constants include Filters and actions
    public static final String GRID_COUNT="GRID_COUNT";
    public static final String VIEW_TYPE="VIEW_TYPE";

    public static final String LIST_VIEW_TYPE = "LIST_VIEW_TYPE";
    public static final String GRID_VIEW_TYPE = "GRID_VIEW_TYPE";
    public static final String REDUCE_COLOUMN = "REDUCE";
    public static final String INCREASE_COLOUMN = "INCREASE";
    public static final String NEW_FILTER_DATE = "NEW_FILTER_DATE";
    public static final String OLD_FILTER_DATE = "OLD_FILTER_DATE";
    public static final String ALL_MEDIA_FILTER = "ALL_MEDIA_FILTER";
    public static final String IMAGE_MEDIA_FILTER = "IMAGE_MEDIA_FILTER";
    public static final String VIDEO_MEDIA_FILTER = "VIDEO_MEDIA_FILTER";
    public static final String SIZE_MEDIA_FILTER = "SIZE_MEDIA_FILTER";
    public static final String NAME_MEDIA_FILTER = "NAME_MEDIA_FILTER";


    //Album constants
    public static final String ALBUM_GRID_COUNT="ALBUM_GRID_COUNT";
    public static final String ALBUM_VIEW_TYPE="ALBUM_VIEW_TYPE";

    public static final String ALBUM_LIST_VIEW_TYPE = "ALBUM_LIST_VIEW_TYPE";
    public static final String ALBUM_GRID_VIEW_TYPE = "ALBUM_GRID_VIEW_TYPE";
    public static final String ALBUM_REDUCE_COLOUMN = "ALBUM_REDUCE";
    public static final String ALBUM_INCREASE_COLOUMN = "ALBUM_INCREASE";
    public static final String ALBUM_NEW_FILTER_DATE = "ALBUM_NEW_FILTER_DATE";
    public static final String ALBUM_OLD_FILTER_DATE = "ALBUM_OLD_FILTER_DATE";
    public static final String ALBUM_SIZE_MEDIA_FILTER = "ALBUM_SIZE_MEDIA_FILTER";

    // Saved Data and Dates
    public static final String SHARED_P_NAME = "SHARED_DATA";
    public static final String IS_DATA_SAVED_IN_CACHE = "IS_SAVED";
    public static final String LAST_DATE="LAST_DATE";
}
