package com.rajesh.gallary.model;

import static com.rajesh.gallary.common.Constant.MEDIA_DATA_TABLE;
import static com.rajesh.gallary.common.Constant.MEDIA_DATE;
import static com.rajesh.gallary.common.Constant.MEDIA_ID;
import static com.rajesh.gallary.common.Constant.MEDIA_NAME;
import static com.rajesh.gallary.common.Constant.MEDIA_PATH;
import static com.rajesh.gallary.common.Constant.MEDIA_SIZE;
import static com.rajesh.gallary.common.Constant.MEDIA_TYPE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = MEDIA_DATA_TABLE)
public class mediaModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MEDIA_ID)
    private int media_Id;
    @ColumnInfo(name = MEDIA_NAME)
    private String mediaName;
    @ColumnInfo(name = MEDIA_PATH)
    private String mediaPath;
    @ColumnInfo(name = MEDIA_SIZE)
    private String mediaSize;
    @ColumnInfo(name = MEDIA_DATE)
    private long mediaDate;
    @ColumnInfo(name = MEDIA_TYPE)
    private boolean isImage;
    @ColumnInfo(name= "AlbumID")
    private String albumID;
    @ColumnInfo(name = "fav")
    private boolean isFav;
    private Boolean selected = false;

    public mediaModel() {
    }

    public mediaModel(String mediaName, String mediaPath, String mediaSize, long mediaDate, boolean isImage, String albumID, boolean isFav, Boolean selected) {
        this.mediaName = mediaName;
        this.mediaPath = mediaPath;
        this.mediaSize = mediaSize;
        this.mediaDate = mediaDate;
        this.isImage = isImage;
        this.albumID = albumID;
        this.isFav = isFav;
        this.selected = selected;
    }



    public int getMedia_Id() {
        return media_Id;
    }

    public void setMedia_Id(int media_Id) {
        this.media_Id = media_Id;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(String mediaSize) {
        this.mediaSize = mediaSize;
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

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
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
}
