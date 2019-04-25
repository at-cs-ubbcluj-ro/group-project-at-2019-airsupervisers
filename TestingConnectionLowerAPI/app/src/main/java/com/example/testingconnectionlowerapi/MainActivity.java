package com.example.testingconnectionlowerapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.testingconnectionlowerapi.websockets.WebSocketConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebSocketConnection webSocketConnection = new WebSocketConnection("http://192.168.100.4:3000");
        webSocketConnection.start();
    }
}
