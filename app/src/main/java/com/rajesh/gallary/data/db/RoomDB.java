package com.rajesh.gallary.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.rajesh.gallary.model.Albums;
import com.rajesh.gallary.model.DateOfMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.mediaDao;

@Database(entities = {mediaModel.class, DateOfMedia.class, Albums.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
   public abstract mediaDao mediaDao();
}
