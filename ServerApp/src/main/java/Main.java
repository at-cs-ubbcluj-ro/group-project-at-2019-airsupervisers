import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args){
        test();
    }

    private static void test(){
        String fmm = "192.168.100.4";
        InetSocketAddress inetSockAddress2 = new InetSocketAddress(fmm, 3000);
        MyWebSocketServer wsServer2 = new MyWebSocketServer(inetSockAddress2);
        wsServer2.start();
    }
}
