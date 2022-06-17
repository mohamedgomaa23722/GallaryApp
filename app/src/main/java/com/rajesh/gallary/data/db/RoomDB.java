package com.rajesh.gallary.data.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rajesh.gallary.model.Albums;
import com.rajesh.gallary.model.DateOfMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.mediaDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Database(entities = {mediaModel.class, DateOfMedia.class, Albums.class}, version = 2, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    public abstract mediaDao mediaDao();


}
