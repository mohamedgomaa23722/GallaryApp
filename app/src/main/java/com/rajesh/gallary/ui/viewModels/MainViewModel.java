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
import java.util.Collections;
import java.util.Comparator;
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
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
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

    private final MutableLiveData<List<DateAndMedia>> dateAndMediaData = new MutableLiveData<>();

    public void InitializeData() {
        repository.GetAllMedia()
                .subscribeOn(Schedulers.io())
                .map(dateAndMediaList -> {
                    List<DateAndMedia> dateAndMedia = new ArrayList<>();
                    for (DateAndMedia dateAndMediaObj : dateAndMediaList) {
                        List<mediaModel> mediaModelResult = new ArrayList<>();
                        for (mediaModel mediaData : dateAndMediaObj.mediaModelList) {
                            if (mediaData.getVault() == 0)
                                mediaModelResult.add(mediaData);
                        }
                        dateAndMedia.add(new DateAndMedia(dateAndMediaObj.date, mediaModelResult));
                    }
                    Collections.sort(dateAndMediaList, (t1, t2) -> Long.compare(t1.date.getRealDate(),t2.date.getRealDate()));
                    return dateAndMedia;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dateAndMediaData::setValue,
                        error -> Log.e(TAG, "InitializeData: ", error),
                        () -> Log.d(TAG, "InitializeData: "));
    }

    public MutableLiveData<List<DateAndMedia>> getAllMediaItems() {
        return dateAndMediaData;
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

    //Fav data
    private final MutableLiveData<List<mediaModel>> favMedia = new MutableLiveData<>();

    public void InitializeFavMedia() {
        repository.getFavMedia().
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favMedia::setValue,
                        error -> Log.e(TAG, "InitializeFavMedia: ", error),
                        () -> Log.d(TAG, "InitializeFavMedia: "));
    }

    public MutableLiveData<List<mediaModel>> getFavMedia() {
        return favMedia;
    }

    public void AddToVault(List<mediaModel> favList) {
        for (mediaModel mediaModel : favList) {
            if (mediaModel.getVault() == 0) {
                mediaModel.setVault(1);
            } else {
                mediaModel.setVault(0);
            }
            mediaModel.setSelected(false);
            UpdateMedia(mediaModel);
        }
    }

    /**
     * Menu operation Communication between Activity And fragments
     * as in main activity we need to create Communication tool to communicate
     * over fragment which belongs to this activity
     */
    //At the First we will create main communicator
    private final MutableLiveData<String> AllMediaCommunicatorData = new MutableLiveData<String>(ALL_MEDIA_FILTER);
    private final MutableLiveData<String> AlbumCommunicatorData = new MutableLiveData<String>(ALL_MEDIA_FILTER);
    private final MutableLiveData<String> FavouriteCommunicatorData = new MutableLiveData<String>(ALL_MEDIA_FILTER);

    //Then set the values we need to send it
    public void setAllMediaCommunicatorData(String data) {
        AllMediaCommunicatorData.setValue(data);
    }

    public void setAlbumCommunicatorData(String Operation) {
        AlbumCommunicatorData.setValue(Operation);
    }

    public void setFavouriteCommunicatorData(String Operation) {
        FavouriteCommunicatorData.setValue(Operation);
    }

    //Then get The Values from those Communicators
    public LiveData<String> getAllMediaCommunicatorData() {
        return AllMediaCommunicatorData;
    }

    public LiveData<String> getAlbumCommunicatorData() {
        return AlbumCommunicatorData;
    }

    public LiveData<String> getFavouriteCommunicatorData() {
        return FavouriteCommunicatorData;
    }

}
