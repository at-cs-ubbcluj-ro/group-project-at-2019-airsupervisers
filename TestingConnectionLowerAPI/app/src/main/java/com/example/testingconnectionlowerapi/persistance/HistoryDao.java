package com.example.testingconnectionlowerapi.persistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.testingconnectionlowerapi.domain.History;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleHistories(List<History> histories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(History history);

    @Query("SELECT * FROM History")
    List<History> getAllHistories();

    @Query("DELETE FROM history")
    void deleteAll();

}
