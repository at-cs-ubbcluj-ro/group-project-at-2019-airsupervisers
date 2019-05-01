package com.example.testingconnectionlowerapi.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.testingconnectionlowerapi.domain.History;
import com.example.testingconnectionlowerapi.persistance.DatabaseCreator;
import com.example.testingconnectionlowerapi.persistance.HistoryDao;

import java.util.List;

public class HistoryRepository {

    private static HistoryRepository INSTANCE = null;
    private HistoryDao mHistoryDao = null;

    public static HistoryRepository getInstance(){

        if (INSTANCE == null){
            INSTANCE = new HistoryRepository();
        }

        return INSTANCE;
    }

    public void createDbEntities(Application application) {
        if(application != null && mHistoryDao == null){
            DatabaseCreator db = DatabaseCreator.getDatabase(application);
            mHistoryDao = db.historyDao();
        }
    }

    public void insertHistoriesSync(List<History> histories){
        mHistoryDao.insertMultipleHistories(histories);
    }

    public void insertHistoriesAsync(List<History> histories){
        new InsertHistory(histories).execute();
    }

    private class InsertHistory extends AsyncTask<Void, Void, Void> {

        private List<History> histories;

        InsertHistory(List<History> histories){
            this.histories = histories;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mHistoryDao.insertMultipleHistories(histories);
            return null;
        }
    }

    public List<History> getLocalHistory(){
        return mHistoryDao.getAllHistories();
    }
}
