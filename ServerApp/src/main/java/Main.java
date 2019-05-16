import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args){
        test();
    }

    private static void test(){
        String ipAddress= "172.30.118.103";
        InetSocketAddress inetSockAddress2 = new InetSocketAddress(ipAddress, 3000);
        MyWebSocketServer wsServer2 = new MyWebSocketServer(inetSockAddress2);
        wsServer2.start();
    }
}
