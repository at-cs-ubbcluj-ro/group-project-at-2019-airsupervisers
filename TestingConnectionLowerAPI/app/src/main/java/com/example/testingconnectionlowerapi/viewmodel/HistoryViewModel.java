package com.example.testingconnectionlowerapi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.testingconnectionlowerapi.domain.History;
import com.example.testingconnectionlowerapi.repository.HistoryRepository;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private HistoryRepository mHistoryRepo = HistoryRepository.getInstance();
    private boolean created = false;

    public HistoryViewModel(@NonNull Application application) {
        super(application);

        if (created == false) {
            mHistoryRepo.createDbEntities(application);
            created = true;
        }
    }

    public void insertHistoriesAsync(List<History> histories){
        mHistoryRepo.insertHistoriesAsync(histories);
    }

    public List<History> getLocalHistorySync(){
        return mHistoryRepo.getLocalHistory();
    }
}
