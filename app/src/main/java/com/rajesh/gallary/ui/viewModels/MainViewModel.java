package com.rajesh.gallary.ui.viewModels;

import static com.rajesh.gallary.common.Constant.MAIN_VIEW_MODEL;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rajesh.gallary.Repository.MediaRepository;
import com.rajesh.gallary.model.Albums;
import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.DateOfMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.utils.SavedData;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final MediaRepository repository;


    @Inject
    public MainViewModel(MediaRepository repository) {
        this.repository = repository;
    }

    private void insertMedia(mediaModel mediaModelData) {
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
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "insertMedia: Deleted successfully"),
                        error -> Log.e(MAIN_VIEW_MODEL, "insertMedia: ", error));
    }


    private void insertAlbum(Albums albums) {
        repository.insertAlbum(albums)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "insertMedia: Date is Added"),
                        error -> Log.e(MAIN_VIEW_MODEL, "insertMedia: ", error));
    }

    public void initializeMediaData(Context context, Uri mediaUri, String[] projection, boolean isImage) {
        Cursor cursor = context.getContentResolver().query(mediaUri, projection, null, null, "date_modified DESC");
        try {
            cursor.moveToFirst();
            do {
                mediaModel mediaData = new mediaModel();
                Albums albums = new Albums();

                mediaData.setMediaName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                mediaData.setMediaPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                mediaData.setMediaSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                mediaData.setImage(isImage);
                mediaData.setFav(false);

                String date = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED));
                long dateInMileSeconds = TimeUnit.SECONDS.toDays(Long.parseLong(date));
                mediaData.setMediaDate(dateInMileSeconds);
                String AlbumName = "";
                String AlbumID = "";

                if (isImage) {
                    AlbumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    AlbumID = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID));
                } else {
                    AlbumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                    AlbumID = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID));
                }
                mediaData.setAlbumID(AlbumID);
                albums.setAlbumID(AlbumID);
                albums.setAlbumName(AlbumName);

                Log.d("TAG", "initializeMediaData: Name:" + AlbumName + " , ID :" + AlbumID);
                insertMedia(mediaData);
                insertMediaDate(new DateOfMedia(dateInMileSeconds));
                insertAlbum(albums);

            } while (cursor.moveToNext());
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public LiveData<List<DateAndMedia>> getAllMediaItems() {
        return repository.GetAllMedia();
    }
}
