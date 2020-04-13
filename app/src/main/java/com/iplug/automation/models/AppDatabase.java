package com.iplug.automation.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.iplug.automation.models.modelsdao.UserProfileDao;

@Database(entities = {UserProfileEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserProfileDao userProfile();
}
