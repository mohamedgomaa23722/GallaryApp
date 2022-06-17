package com.rajesh.gallary.model;

import static com.rajesh.gallary.common.Constant.MEDIA_DATA_TABLE;
import static com.rajesh.gallary.common.Constant.MEDIA_DATE;
import static com.rajesh.gallary.common.Constant.MEDIA_DATE_ID;
import static com.rajesh.gallary.common.Constant.MEDIA_ID;
import static com.rajesh.gallary.common.Constant.MEDIA_NAME;
import static com.rajesh.gallary.common.Constant.MEDIA_PATH;
import static com.rajesh.gallary.common.Constant.MEDIA_SIZE;
import static com.rajesh.gallary.common.Constant.MEDIA_TYPE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = MEDIA_DATA_TABLE)
public class mediaModel implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = MEDIA_PATH)
    @NonNull
    private String mediaPath;
    @ColumnInfo(name = MEDIA_ID)
    private long MediaID;
    @ColumnInfo(name = MEDIA_NAME)
    private String mediaName;
    @ColumnInfo(name = MEDIA_SIZE)
    private String mediaSize;
    @ColumnInfo(name = MEDIA_DATE_ID)
    private String mediaDateId;
    @ColumnInfo(name = MEDIA_DATE)
    private long mediaDate;
    @ColumnInfo(name = MEDIA_TYPE)
    private boolean isImage;
    @ColumnInfo(name = "AlbumID")
    private String albumID;
    @ColumnInfo(name = "fav")
    private boolean isFav;
    @ColumnInfo(name = "Vault")
    private int Vault;
    private Boolean selected = false;


    public mediaModel() {
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    @NonNull
    public String getMediaPath() {
        return mediaPath;
    }


    public void setMediaPath(@NonNull String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(String mediaSize) {
        this.mediaSize = mediaSize;
    }

    public String getMediaDateId() {
        return mediaDateId;
    }

    public void setMediaDateId(String mediaDateId) {
        this.mediaDateId = mediaDateId;
    }

    public long getMediaDate() {
        return mediaDate;
    }

    public void setMediaDate(long mediaDate) {
        this.mediaDate = mediaDate;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public int getVault() {
        return Vault;
    }

    public void setVault(int vault) {
        Vault = vault;
    }

    public long getMediaID() {
        return MediaID;
    }

    public void setMediaID(long mediaID) {
        MediaID = mediaID;
    }
}
