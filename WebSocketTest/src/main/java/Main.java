import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args){

        String ipAddress = "192.168.0.100";
        InetSocketAddress inetSockAddress = new InetSocketAddress(ipAddress, 3000);
        MyWebSocketServer wsServer = new MyWebSocketServer(inetSockAddress);
        wsServer.start();


    }
}
