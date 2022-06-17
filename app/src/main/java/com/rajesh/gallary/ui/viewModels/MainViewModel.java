package com.rajesh.gallary.ui.viewModels;

import static com.rajesh.gallary.common.Constant.ALL_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.EXTERNAL_IMAGE;
import static com.rajesh.gallary.common.Constant.EXTERNAL_VIDEO;
import static com.rajesh.gallary.common.Constant.IMAGE_PROJECTION;
import static com.rajesh.gallary.common.Constant.MAIN_VIEW_MODEL;
import static com.rajesh.gallary.common.Constant.VIDEO_PROJECTION;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rajesh.gallary.Repository.MediaRepository;
import com.rajesh.gallary.model.Albums;
import com.rajesh.gallary.model.AlbumsAndMedia;
import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.DateOfMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.utils.SavedData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    private final MediaRepository repository;
    private Context context;
    @Inject
    SavedData savedData;

    @Inject
    public MainViewModel(@ApplicationContext Context context, MediaRepository repository) {
        this.repository = repository;
        this.context = context;
    }

    public void insertMedia(mediaModel mediaModelData) {
        repository.insertMedia(mediaModelData)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "insertMedia: Media is Added"),
                        error -> Log.e(MAIN_VIEW_MODEL, "insertMedia: ", error));
    }


    private void insertMediaDate(DateOfMedia dateOfMedia) {
        repository.insertMediaDate(dateOfMedia)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "insertMedia: Date is Added"),
                        error -> Log.e(MAIN_VIEW_MODEL, "insertMedia: ", error));
    }

    public void deleteMedia(mediaModel mediaModelData) {
        repository.deleteMedia(mediaModelData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "deleteMedia: Deleted successfully"),
                        error -> Log.e(MAIN_VIEW_MODEL, "deleteMedia: ", error));
    }

    public void deleteAlbum(String albumId) {
        repository.deleteAlbum(albumId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "deleteAlbum: Deleted successfully"),
                        error -> Log.e(MAIN_VIEW_MODEL, "deleteAlbum: ", error));
    }

    public void UpdateMedia(mediaModel mediaModel) {
        repository.UpdateMedia(mediaModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "insertMedia: Updated successfully"),
                        error -> Log.e(MAIN_VIEW_MODEL, "insertMedia: ", error));
    }

    private void insertAlbum(Albums albums) {
        repository.insertAlbum(albums)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "insertMedia: Date is Added"),
                        error -> Log.e(MAIN_VIEW_MODEL, "insertMedia: ", error));
    }

    private final MutableLiveData<List<AlbumsAndMedia>> albumsAndDataById = new MutableLiveData<>();

    public void InitializeAlbumAndDataById(String albumId) {
        repository.getAlbumsByID(albumId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(albumsAndDataById::setValue,
                        error -> Log.e(TAG, "InitializeAlbumAndDataById: ", error),
                        () -> Log.d(TAG, "InitializeAlbumAndDataById: completed"));
    }

    public MutableLiveData<List<AlbumsAndMedia>> getAlbumsAndDataById() {
        return albumsAndDataById;
    }

    public void initializeMediaData(Uri mediaUri, String[] projection, boolean isImage) {
        long lastDate = 0;
        long CurrentDate = 0;
        Cursor cursor = context.getContentResolver().query(mediaUri, projection, null, null, "date_modified DESC");
        try {
            cursor.moveToFirst();
            do {
                mediaModel mediaData = new mediaModel();
                Albums albums = new Albums();
                String Path = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
                if (CheckPath(Path)) {
                    mediaData.setMediaPath(Path);
                    mediaData.setMediaName(cursor.getString(cursor.getColumnIndexOrThrow(projection[1])));
                    mediaData.setMediaSize(cursor.getString(cursor.getColumnIndexOrThrow(projection[2])));
                    mediaData.setVault(cursor.getInt(cursor.getColumnIndexOrThrow(projection[6])));
                    mediaData.setMediaID(cursor.getLong(cursor.getColumnIndexOrThrow(projection[7])));

                    mediaData.setImage(isImage);
                    mediaData.setFav(false);

                    String date = cursor.getString(cursor.getColumnIndexOrThrow(projection[3]));
                    @SuppressLint("SimpleDateFormat") String RealDate = new SimpleDateFormat("dd MMM ,yyyy").format(Long.parseLong(date) * 1000);

                    long dateInMileSeconds = TimeUnit.SECONDS.toDays(Long.parseLong(date));
                    mediaData.setMediaDateId(RealDate);
                    mediaData.setMediaDate(Long.parseLong(date));

                    String AlbumName = cursor.getString(cursor.getColumnIndexOrThrow(projection[4]));
                    String AlbumID = cursor.getString(cursor.getColumnIndexOrThrow(projection[5]));

                    int Privacy = cursor.getInt(cursor.getColumnIndexOrThrow(projection[6]));


                    mediaData.setAlbumID(AlbumID);
                    albums.setAlbumID(AlbumID);
                    albums.setAlbumName(AlbumName);
                    insertMedia(mediaData);
                    insertMediaDate(new DateOfMedia(RealDate, dateInMileSeconds));
                    insertAlbum(albums);

                    //check the biggest date
                    CurrentDate = Long.parseLong(date);
                    if (CurrentDate > lastDate)
                        lastDate = CurrentDate;

                }
            } while (cursor.moveToNext());
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        savedData.setLastDate(lastDate);
    }


    public LiveData<List<mediaModel>> getMediaTableData(int vault) {
        return repository.getMediaTableData(vault);
    }

    public LiveData<List<DateAndMedia>> getAllMediaItems() {
        return repository.GetAllMedia();
    }

    // Fav
    public void AddToFav(String ID, boolean Fav) {
        repository.AddToFav(ID, Fav)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "insertMedia: Date is Added"),
                        error -> Log.e(MAIN_VIEW_MODEL, "insertMedia: ", error));
    }

    //Update vaultItem
    public void AddMediaToVault(String ID, int vault) {
        repository.AddMediaToVault(ID, vault)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "insertMedia: Date is Added"),
                        error -> Log.e(MAIN_VIEW_MODEL, "insertMedia: ", error));
    }

    //Update List every Time

    public void UpdateMedia(Uri mediaUri, String[] projection, boolean isImage, String LastDate) {
        long lastDate = Long.parseLong(LastDate);
        long CurrentDate = 0;
        Cursor cursor = context.getContentResolver().query(mediaUri, projection,
                MediaStore.MediaColumns.DATE_ADDED + ">?"
                , new String[]{"" + LastDate},
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        try {
            cursor.moveToFirst();
            do {
                mediaModel mediaData = new mediaModel();
                Albums albums = new Albums();
                String Path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                if (CheckPath(Path)) {
                    mediaData.setMediaPath(Path);
                    mediaData.setMediaName(cursor.getString(cursor.getColumnIndexOrThrow(projection[1])));
                    mediaData.setMediaSize(cursor.getString(cursor.getColumnIndexOrThrow(projection[2])));
                    mediaData.setVault(cursor.getInt(cursor.getColumnIndexOrThrow(projection[6])));
                    mediaData.setMediaID(cursor.getLong(cursor.getColumnIndexOrThrow(projection[7])));

                    mediaData.setImage(isImage);
                    mediaData.setFav(false);

                    String date = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED));
                    @SuppressLint("SimpleDateFormat") String RealDate = new SimpleDateFormat("dd MMM ,yyyy").format(Long.parseLong(date) * 1000);

                    long dateInMileSeconds = TimeUnit.SECONDS.toDays(Long.parseLong(date));
                    mediaData.setMediaDateId(RealDate);
                    mediaData.setMediaDate(Long.parseLong(date));

                    String AlbumName = cursor.getString(cursor.getColumnIndexOrThrow(projection[4]));
                    String AlbumID = cursor.getString(cursor.getColumnIndexOrThrow(projection[5]));

                    int Privacy = cursor.getInt(cursor.getColumnIndexOrThrow(projection[6]));

                    mediaData.setAlbumID(AlbumID);
                    albums.setAlbumID(AlbumID);
                    albums.setAlbumName(AlbumName);

                    insertMedia(mediaData);
                    insertMediaDate(new DateOfMedia(RealDate, dateInMileSeconds));
                    insertAlbum(albums);

                    Log.d("TAG", "initializeMediaData: Name:" + AlbumName + " , ID :" + AlbumID);
                    //check the biggest date
                    CurrentDate = Long.parseLong(date);
                    if (CurrentDate > lastDate)
                        lastDate = CurrentDate;
                }
            } while (cursor.moveToNext());
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        savedData.setLastDate(lastDate);
    }

    private final MutableLiveData<mediaModel> selectedItem = new MutableLiveData<mediaModel>();

    public void selectItem(mediaModel item) {
        selectedItem.setValue(item);
    }

    public LiveData<mediaModel> getSelectedItem() {
        return selectedItem;
    }


    /**
     * Handle on fragment Clicked
     */
    private final MutableLiveData<Boolean> clickedOnItem = new MutableLiveData<Boolean>(true);

    public void clickedItem(boolean clicked) {
        clickedOnItem.setValue(clicked);
    }

    public LiveData<Boolean> getClickedItem() {
        return clickedOnItem;
    }

    /**
     * Get Media by Album
     */
    private final MutableLiveData<List<mediaModel>> mediaByAlbum = new MutableLiveData<>();

    public void InitializeMediaByAlbumData(String albumID) {
        repository.getMediaByAlbum(albumID)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mediaByAlbum::setValue,
                        error -> Log.e(TAG, "InitializeMediaByAlbumData: ", error),
                        () -> Log.d(TAG, "InitializeMediaByAlbumData:  completed"));
    }

    public MutableLiveData<List<mediaModel>> getMediaByAlbum() {
        return mediaByAlbum;
    }


    //Menu operation communication with fragment
    private final MutableLiveData<String> MenuOperation = new MutableLiveData<String>(ALL_MEDIA_FILTER);

    public LiveData<String> getMenuOperation() {
        return MenuOperation;
    }

    public void setMenuOperation(String Operation) {
        MenuOperation.setValue(Operation);
    }


    //Get media data
    public LiveData<List<mediaModel>> GetAlbumMediaByType(String albumID, boolean type) {
        return repository.GetAlbumMediaByType(albumID, type);
    }


    /**
     * Album Model
     */
    private final MutableLiveData<List<AlbumsAndMedia>> albums = new MutableLiveData<>();

    public void initializeAlbumsData() {
        repository.getAlbums().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(albums::setValue,
                        error -> Log.e(TAG, "initializeAlbumsData: ", error),
                        () -> Log.d(TAG, "initializeAlbumsData: completed"));
    }

    public MutableLiveData<List<AlbumsAndMedia>> getAlbums() {
        return albums;
    }

    //Update Image privacy
    public void UpdateImage(mediaModel updatedData, int isPrivate, long mediaId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_PROJECTION[0], updatedData.getMediaPath());
        contentValues.put(IMAGE_PROJECTION[1], updatedData.getMediaName());
        contentValues.put(MediaStore.Images.Media.IS_PRIVATE, isPrivate);
        Uri uri = ContentUris.withAppendedId(EXTERNAL_IMAGE, mediaId);

        context.getContentResolver().update(uri, contentValues, null, null);
    }

    public void DeleteImage(Uri mediaUri, mediaModel data) {
        Uri uri = ContentUris.withAppendedId(mediaUri, data.getMediaID());
        context.getContentResolver().delete(uri, null, null);
        File file = new File(data.getMediaPath());
        file.delete();
    }


    public void insertMedia(Uri uri, mediaModel updatedData, String[] projection) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(projection[0], updatedData.getMediaPath());
        contentValues.put(projection[1], updatedData.getMediaName());
        contentValues.put(projection[2], updatedData.getMediaSize());
        contentValues.put(projection[3], updatedData.getMediaDate());
        contentValues.put(projection[5], updatedData.getAlbumID());
        contentValues.put(projection[6], updatedData.getVault());
        contentValues.put(projection[7], updatedData.getMediaID());
        context.getContentResolver().insert(uri, contentValues);
    }

    public void insertMediaForNewAlbum(Uri uri, mediaModel updatedData, String[] projection, String AlbumName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(projection[0], updatedData.getMediaPath());
        contentValues.put(projection[1], updatedData.getMediaName());
        contentValues.put(projection[2], updatedData.getMediaSize());
        contentValues.put(projection[3], updatedData.getMediaDate());
        contentValues.put(projection[4], AlbumName);
        contentValues.put(projection[5], updatedData.getAlbumID());
        contentValues.put(projection[6], updatedData.getVault());
        context.getContentResolver().insert(uri, contentValues);
    }


    public void RefreshData() {
        UpdateMedia(EXTERNAL_IMAGE, IMAGE_PROJECTION, true, String.valueOf(savedData.getLastDate()));
        UpdateMedia(EXTERNAL_VIDEO, VIDEO_PROJECTION, false, String.valueOf(savedData.getLastDate()));
    }

    public boolean CheckPath(String CheckedUri) {
        File file = new File(CheckedUri);
        if (file.exists()) {
            //here file is exist
            return true;
        } else {
            //here file is missed
            return false;
        }
    }

}
