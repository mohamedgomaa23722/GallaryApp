package com.rajesh.gallary.data.di;

import android.app.Application;

import androidx.room.Room;

import com.rajesh.gallary.data.db.RoomDB;
import com.rajesh.gallary.network.mediaDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RoomDBModule {

    @Provides
    @Singleton
    public static RoomDB provideDB(Application application){
        return Room.databaseBuilder(application, RoomDB.class, "Fav_DB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public static mediaDao provideDao(RoomDB mediaDB){
        return mediaDB.mediaDao();
    }
}
