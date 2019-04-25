import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class MyWebSocketServer extends WebSocketServer {
    private Set<WebSocket> clientsConnections;
//    private WebSocket conn;

    public MyWebSocketServer(InetSocketAddress address) {
        super(address);
        clientsConnections = new HashSet<>();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onError(WebSocket arg0, Exception arg1) {
        // TODO Auto-generated method stub
        System.out.println("Error" + arg1.getMessage());

    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    @Override
    public void onMessage(WebSocket webSocket, String arg1) {
        // TODO Auto-generated method stub
        System.out.println("received: " + arg1);
        broadcastMessage(arg1);
    }

    private void broadcastMessage(String msg){
        Gson gson=new GsonBuilder().create();
        broadcast(msg);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake arg1) {
        // TODO Auto-generated method stub
        System.out.println("new connection to " + webSocket.getRemoteSocketAddress());
    }
}
