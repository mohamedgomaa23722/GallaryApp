package com.rajesh.gallary.network;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.rajesh.gallary.model.Albums;
import com.rajesh.gallary.model.AlbumsAndMedia;
import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.DateOfMedia;
import com.rajesh.gallary.model.mediaModel;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface mediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMedia(mediaModel mediaData);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMediaDate(DateOfMedia dateOfMedia);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAlbum(Albums albums);

    @Delete
    Completable deleteMedia(mediaModel mediaData);

    @Transaction
    @Query("SELECT * FROM MEDIA_DATE_TABLE order by mediaDate DESC")
    Observable<List<DateAndMedia>> getAllMediaData();

    @Transaction
    @Query("Select * FROM ALBUM_TABLE")
    Observable<List<AlbumsAndMedia>> getAlbums();

    @Transaction
    @Query("Select * FROM ALBUM_TABLE where albumId=:albumId")
    Maybe<List<AlbumsAndMedia>> getAlbumsByID(String albumId);

    @Query("SELECT * from MEDIA_DATA_TABLE where Vault=:v  order by MEDIA_DATE DESC")
    LiveData<List<mediaModel>> getMediaTableData(int v);

    //Fav Queries
    @Query("SELECT * FROM MEDIA_DATA_TABLE where fav = :fav and Vault=:v")
    Observable<List<mediaModel>> getFavMedia(boolean fav,int v);

    @Query("UPDATE MEDIA_DATA_TABLE set fav = :fav where MEDIA_PATH=:ID ")
    Completable addToFav(String ID, boolean fav);


    //Get Albums
    @Query("Select * FROM MEDIA_DATA_TABLE where AlbumID =:albumID And Vault=:v order by MEDIA_DATE DESC")
    Observable<List<mediaModel>> getMediaByAlbum(String albumID,int v);


    // Get Media By it's Type Image Or video
    @Query("Select * from MEDIA_DATA_TABLE where MEDIA_TYPE =:Type and AlbumID=:albumID")
    LiveData<List<mediaModel>> GetAlbumMediaByType(String albumID, boolean Type);


    //Update media data
    @Update
    Completable UpdateMedia(mediaModel mediaModel);

    //Update media by set vault item
    @Query("UPDATE MEDIA_DATA_TABLE set Vault = :vault where MEDIA_PATH=:ID ")
    Completable AddMediaToVault(String ID, int vault);

    //delete Album by id
    @Query("Delete  from ALBUM_TABLE where albumId = :albumId")
    Completable DeleteAlbum(String albumId);
}
