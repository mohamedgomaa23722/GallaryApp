package com.rajesh.gallary.model;

import static com.rajesh.gallary.common.Constant.MEDIA_DATE_ID;
import static com.rajesh.gallary.common.Constant.MEDIA_DATE_TABLE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = MEDIA_DATE_TABLE)
public class DateOfMedia {
    @PrimaryKey
    @ColumnInfo(name = MEDIA_DATE_ID)
    long date;

    public DateOfMedia() {
    }

    public DateOfMedia(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
