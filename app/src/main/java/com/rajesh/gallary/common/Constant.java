package com.rajesh.gallary.common;

import android.net.Uri;
import android.provider.MediaStore;

public class Constant {
    public static final String AppName = "com.rajesh.gallary";

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
    public static final String GRID_COUNT = "GRID_COUNT";
    public static final String VIEW_TYPE = "VIEW_TYPE";

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
    //Favourities
    public static final String FAV_GRID_COUNT = "FAV_GRID_COUNT";
    public static final String FAV_VIEW_TYPE = "FAV_VIEW_TYPE";

    //Album constants
    public static final String ALBUM_GRID_COUNT = "ALBUM_GRID_COUNT";
    public static final String ALBUM_VIEW_TYPE = "ALBUM_VIEW_TYPE";

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
    public static final String LAST_DATE = "LAST_DATE";


    //Security Constants
    public static final String QUESTION = "QUESTION";
    public static final String ANSWER = "ANSWER";

    //Theme
    public static final String THEME = "THEME";
    public static final String WHITE_THEME = "WHITE_THEME";
    public static final String BLACK_THEME = "BLACK_THEME";

    //Slider
    public static final String TIME = "TIME";
    public static final String INCLUDE_VIDEO = "INCLUDE_VIDEO";
    public static final String LOOP_VIDEO = "LOOP_VIDEO";


    //Passwords
    public static final String ENABLE_FINGER_PRINT_KEY = "FINGER_PRINT_KEY";
    public static final String PATTERN_KEY = "Pattern_Key";
    public static final String Pin_Key = "Pin_Key";

    public static final String STATUS_FIRST_STEP = "Draw an unlock pattern";
    public static final String STATUS_Next_STEP = "Draw pattern again to confirm";
    public static final String STATUS_PASSWORD_CORRECT = "Pattern set";
    public static final String STATUS_PASSWORD_INCORRECT = "Try Again";
    public static final String SHEMA_FAILED = "Connect at least 4 dots";
    //pattern contants steps
    public static final String OLD_PATTERN = "Enter old pattern";
    public static final String NEW_PATTERN = "Enter new pattern";
    public static final String CONFIRM_PATTERN = "Confirm new pattern";

    public static final int FIRST_STEP = 0;
    public static final int SECOND_STEP = 1;


    public static final String FROM_PASSWORD_TO_SECURITY = "FROM_PASSWORD_TO_SECURITY";
    public static final String FROM_SPLASH_SCREEN_TO_HOME = "FROM_SPLASH_SCREEN_TO_HOME";
    public static final String FROM_PASSWORD_TO_VAULT = "FROM_PASSWORD_TO_VAULT";
    public static final String FROM_VAULT_TO_SETTINGS = "FROM_VAULT_TO_SETTINGS";
    public static final String FROM_SECURITY_TO_SETTINGS = "FROM_SECURITY_TO_SETTINGS";
    public static final String FROM_SETTINGS_TO_SECURITY = "FROM_SETTINGS_TO_SECURITY";
    public static final String FROM_SETTINGS_TO_VAULT = "FROM_SETTINGS_TO_VAULT";
    public static final String FROM_SETTING_TO_PASSWORD = "FROM_SETTING_TO_PASSWORD";

    //Password boolean values
    public static final String FINGER_PRINT_ENABLE = "FINGER_PRINT_ENABLE";
    public static final String LOCK_ENABLE = "LOCK_ENABLE";
    public static final String PATTERN_ENABLE = "PATTERN_ENABLE";
    public static final String PLAY_BACK_ENABLE="PLAY_BACK_ENABLE";


    //popup menu operations for RecyclerView
    public static final int SELECT_ALL = 0;
    public static final int DELETE_SELECTED = 1;
    public static final int SHARE_SELECTED = 2;
    public static final int ADD_FAV_SELECTED = 3;
    public static final int COPY_SELECTED = 4;
    public static final int MOVE_TO_VAULT = 5;


    public static final String RESTART_FRAGMENT = "RESTART_FRAGMENT";


}
