package com.example.testingraspconn.websockets;//package com.example.testingconnection.websockets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketConnection {

    private OkHttpClient client;
    private String url;

    public WebSocketConnection(String url) {
        this.url = url;
    }

    public void start(){

        Request request = new Request.Builder().url(url).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        client = new OkHttpClient();
        WebSocket ws = client.newWebSocket(request, listener);
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.println("has opened");
            webSocket.send("test");
        }

        @Override
        public void onMessage(WebSocket webSocket, String response) {
            System.out.println("Received message " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                Gson gson=new GsonBuilder().create();
                MessageObject message = gson.fromJson(jsonObject.toString(), MessageObject.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println("Received message " + bytes);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            System.out.println("Closing websockets");
            webSocket.close(NORMAL_CLOSURE_STATUS, null);

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            System.out.println("Failed ...");
        }
    }
}
