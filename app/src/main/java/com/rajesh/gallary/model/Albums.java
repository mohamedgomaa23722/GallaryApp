package com.rajesh.gallary.model;

import static com.rajesh.gallary.common.Constant.ALBUM_TABLE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = ALBUM_TABLE)
public class Albums {

    @PrimaryKey
    @ColumnInfo(name = "albumId")
    @NonNull
    private String albumID;
    @ColumnInfo(name = "albumName")
    private String albumName;
    @ColumnInfo(name = "albumPath")
    private String albumPath;
    private boolean isSelected = false;

    public Albums() {
    }

    public Albums(@NonNull String albumID, String albumName, String albumPath) {
        this.albumID = albumID;
        this.albumName = albumName;
        this.albumPath = albumPath;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(@NonNull String albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(String albumPath) {
        this.albumPath = albumPath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
