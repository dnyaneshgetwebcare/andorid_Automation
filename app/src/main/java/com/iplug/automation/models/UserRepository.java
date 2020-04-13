package com.iplug.automation.models;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.iplug.automation.models.modelsdao.UserProfileDao;

import java.util.List;

public class UserRepository {
    private String DB_NAME = "db_task";

    private AppDatabase noteDatabase;
    public UserRepository(Context context) {
        noteDatabase = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
    }


    public void insertUser(String name,
                           String email,
                           String contact_nos,
                           String address) {

        UserProfileEntity  userProfileEntity= new UserProfileEntity();
        userProfileEntity.setName(name);
        userProfileEntity.setEmail_id(email);
        userProfileEntity.setContact_nos(contact_nos);
        userProfileEntity.setAddress(address);

        insertUser(userProfileEntity);
    }

    public void insertUser(final UserProfileEntity userProfileEntity) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.userProfile().insertuser(userProfileEntity);
                return null;
            }
        }.execute();
    }

    public void updateUser(final UserProfileEntity userProfileEntity) {


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.userProfile().updateUser(userProfileEntity);
                return null;
            }
        }.execute();
    }
    public UserProfileEntity getUser(){

       return noteDatabase.userProfile().getAll().get(0);
    }

/*    public void deleteTask(final int id) {
        final LiveData<Note> task = getTask(id);
        if(task != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    noteDatabase.daoAccess().deleteTask(task.getValue());
                    return null;
                }
            }.execute();
        }
    }*/


}
