package local.wagenhuber.guenther;

import java.util.Date;
import java.util.Vector;

public class TestClient {

    public static void main(String[] args) throws Exception{
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        RPCClient rpcClient = new RPCClient(host, port);

        Object[] params1 = {"Hello"};
        String s = ((String) rpcClient.call("getEcho", params1));
        System.out.println("getEcho: " + s);

        Object[] params2 = {10, 33};
        int summe = ((Integer) rpcClient.call("getSumme", params2));
        System.out.println("getSumme: " + summe);

        Object[] params3 = {};
        Date date = ((Date) rpcClient.call("getDate", params3));
        System.out.println("getDate: " + date);

        Object[] params4 = {"Dies ist ein Test..."};
        rpcClient.call("sendMessage", params4);

        Object[] params5 = {"msg.txt"};

        @SuppressWarnings("unchecked")
        Vector<String> v = ((Vector<String>) rpcClient.call("getMessages", params5));
        System.out.println("getMessages: ");
        for (String msg : v) {
            System.out.println(msg);
        }

    }
}
