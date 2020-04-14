package com.iplug.automation.models.modelsdao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.iplug.automation.models.UserProfileEntity;

import java.util.List;
@Dao
public interface UserProfileDao {

    @Query("select * from  user_profile")
    List<UserProfileEntity> getAll();
    @Insert
    void insertuser(UserProfileEntity user);
    @Update
    void updateUser(UserProfileEntity user);
    @Delete
    void deleteUser(UserProfileEntity user);
}

