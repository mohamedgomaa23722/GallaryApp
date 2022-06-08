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

    public Albums() {
    }

    public Albums(@NonNull String albumID, String albumName) {
        this.albumID = albumID;
        this.albumName = albumName;
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
}
