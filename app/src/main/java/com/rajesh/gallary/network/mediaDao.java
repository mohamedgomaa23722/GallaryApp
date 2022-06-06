package com.rajesh.gallary.network;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.DateOfMedia;
import com.rajesh.gallary.model.mediaModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface mediaDao {
    /**
     * insert Some Media Video or image
     * @param mediaData
     * @return
     */
    @Insert
    Completable insertMedia (mediaModel mediaData);

    /**
     * Insert the date of the image or video to sort them
     * @param dateOfMedia
     * @return
     */
    @Insert
    Completable insertMediaDate(DateOfMedia dateOfMedia);

    /**
     * Delete some media Image or Video
     * @param mediaData
     * @return
     */
    @Delete
    Completable deleteMedia(mediaModel mediaData);

    /**
     * Get the Media from table with date
     * @return
     */
    @Query("select * from MEDIA_DATE_TABLE")
    Observable<List<DateAndMedia>> getAllMediaData();


}
