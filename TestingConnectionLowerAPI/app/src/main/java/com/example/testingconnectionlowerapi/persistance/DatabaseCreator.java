package com.example.testingconnectionlowerapi.persistance;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import com.example.testingconnectionlowerapi.domain.History;

import java.util.concurrent.ExecutionException;


@Database(entities = {History.class}, version =  1)
public abstract class DatabaseCreator extends RoomDatabase {

    public abstract HistoryDao historyDao();


    private static volatile DatabaseCreator INSTANCE = null;

    public static DatabaseCreator getDatabase(final Context context){

        if (INSTANCE == null) {
            synchronized (DatabaseCreator.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseCreator.class, "app_database")
                            .build();

                }
            }
        }
        return INSTANCE;

    }

    public static void nukeTablesSync(){

        NukeTables task = new NukeTables(INSTANCE);
        try {
            task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private static class NukeTables extends AsyncTask<Void, Void, Void> {

        private final HistoryDao mHistoryDao;

        NukeTables(DatabaseCreator db) {

            mHistoryDao = db.historyDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mHistoryDao.deleteAll();
            return null;
        }
    }





}


