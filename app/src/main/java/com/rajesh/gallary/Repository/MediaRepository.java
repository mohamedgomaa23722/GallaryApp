package com.rajesh.gallary.Repository;

import androidx.lifecycle.LiveData;

import com.rajesh.gallary.model.Albums;
import com.rajesh.gallary.model.AlbumsAndMedia;
import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.DateOfMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.mediaDao;
import com.rajesh.gallary.utils.MediaData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

public class MediaRepository {

    private mediaDao mediaDao;

    /**
     * initialize our repo by inject the dao into it
     *
     * @param mediaDao
     */
    @Inject
    public MediaRepository(mediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }

    /**
     * Insert Some new Media
     *
     * @param mediaModelDate
     * @return
     */
    public Completable insertMedia(mediaModel mediaModelDate) {
        return mediaDao.insertMedia(mediaModelDate);
    }

    /**
     * insert Album
     *
     * @param albums
     * @return
     */
    public Completable insertAlbum(Albums albums) {
        return mediaDao.insertAlbum(albums);
    }

    /**
     * Insert the date of the media
     *
     * @param dateOfMedia
     * @return
     */
    public Completable insertMediaDate(DateOfMedia dateOfMedia) {
        return mediaDao.insertMediaDate(dateOfMedia);
    }

    /**
     * Delete media item image or video
     *
     * @param mediaModelData
     * @return
     */
    public Completable deleteMedia(mediaModel mediaModelData) {
        return mediaDao.deleteMedia(mediaModelData);
    }

    /**
     * Delete Album Data
     *
     * @param albumId
     * @return
     */
    public Completable deleteAlbum(String albumId) {
        return mediaDao.DeleteAlbum(albumId);
    }

    public LiveData<List<mediaModel>> getMediaTableData(int vault) {
        return mediaDao.getMediaTableData(vault);
    }

    public Observable<List<mediaModel>> getFavMedia() {
        return mediaDao.getFavMedia(true, 0);
    }

    public Observable<List<AlbumsAndMedia>> getAlbums() {
        return mediaDao.getAlbums();
    }

    public Observable<List<DateAndMedia>> GetAllMedia() {
        return mediaDao.getAllMediaData();
    }

    //Update fav
    public Completable AddToFav(String ID, boolean Fav) {
        return mediaDao.addToFav(ID, Fav);
    }

    //Get media by album
    public Observable<List<mediaModel>> getMediaByAlbum(String albumID) {
        return mediaDao.getMediaByAlbum(albumID, 0);
    }

    //Get media for some specific album by it's type
    public LiveData<List<mediaModel>> GetAlbumMediaByType(String albumID, boolean type) {
        return mediaDao.GetAlbumMediaByType(albumID, type);
    }

    //Update media
    public Completable UpdateMedia(mediaModel mediaModel) {
        return mediaDao.UpdateMedia(mediaModel);
    }

    //Update vaultItem
    public Completable AddMediaToVault(String ID, int vault) {
        return mediaDao.AddMediaToVault(ID, vault);
    }

    /**
     * Return Album by id
     */
    public Maybe<List<AlbumsAndMedia>> getAlbumsByID(String AlbumId) {
        return mediaDao.getAlbumsByID(AlbumId);
    }

}
