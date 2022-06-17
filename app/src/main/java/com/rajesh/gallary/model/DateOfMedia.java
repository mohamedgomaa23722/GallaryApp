package com.rajesh.gallary.model;

import static com.rajesh.gallary.common.Constant.MEDIA_DATE_ID;
import static com.rajesh.gallary.common.Constant.MEDIA_DATE_TABLE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = MEDIA_DATE_TABLE)
public class DateOfMedia {
    @PrimaryKey
    @ColumnInfo(name = MEDIA_DATE_ID)
    @NonNull
    String date;
    @ColumnInfo(name = "mediaDate")
    long realDate;
    public DateOfMedia() {
    }

    public DateOfMedia(@NonNull String date,long realDate) {
        this.date = date;
        this.realDate = realDate;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public long getRealDate() {
        return realDate;
    }

    public void setRealDate(long realDate) {
        this.realDate = realDate;
    }
}
