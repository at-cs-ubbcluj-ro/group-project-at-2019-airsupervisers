package com.example.testingconnectionlowerapi;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.testingconnectionlowerapi.domain.History;
import com.example.testingconnectionlowerapi.viewmodel.HistoryViewModel;
import com.example.testingconnectionlowerapi.websockets.WebSocketConnection;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static HistoryViewModel mHistoryViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHistoryViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);


//        insertDummyHistories();

//        WebSocketConnection webSocketConnection = new WebSocketConnection("http://192.168.100.4:3000");
//        webSocketConnection.start();
    }

    private void insertDummyHistories() {

        List<History> historyList = new ArrayList<>();
        historyList.add(new History("Acetone", 222));
        historyList.add(new History("Acetone", 223));

//        mHistoryViewModel.insertHistoriesAsync(historyList);

        new GetLocalHistory().execute();

    }

    private class GetLocalHistory extends AsyncTask<Void, Void, List<History>> {

        private List<History> histories;


        @Override
        protected List<History> doInBackground(Void... voids) {
            histories = mHistoryViewModel.getLocalHistorySync();
            return histories;
        }

        @Override
        protected void onPostExecute(List<History> histories) {
            System.out.println("here");

        }
    }
}
