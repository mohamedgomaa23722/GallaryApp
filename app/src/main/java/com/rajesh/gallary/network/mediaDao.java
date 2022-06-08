package com.rajesh.gallary.network;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.rajesh.gallary.model.Albums;
import com.rajesh.gallary.model.AlbumsAndMedia;
import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.DateOfMedia;
import com.rajesh.gallary.model.mediaModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface mediaDao {

    @Insert
    Completable insertMedia (mediaModel mediaData);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMediaDate(DateOfMedia dateOfMedia);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAlbum(Albums albums);

    @Delete
    Completable deleteMedia(mediaModel mediaData);

    @Transaction
    @Query("SELECT * FROM MEDIA_DATE_TABLE order by MEDIA_DATE_ID DESC")
    LiveData<List<DateAndMedia>> getAllMediaData();

    @Transaction
    @Query("Select * FROM ALBUM_TABLE")
    Observable<List<AlbumsAndMedia>> getAlbums();

    @Query("SELECT * FROM MEDIA_DATA_TABLE where fav = :fav")
    Observable<List<mediaModel>> getFavMedia(boolean fav);

}
