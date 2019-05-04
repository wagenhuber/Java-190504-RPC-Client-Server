package local.wagenhuber.guenther;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RPCClient {
    private String host;
    private int port;

    public RPCClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    //Name=Entfernte Methode, Object[]=Parameter zur Übergabe in Methode
    public Object call(String name, Object[] params) throws Exception {
        Socket socket = null;

        try {
            socket = new Socket(host, port);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(name);
            out.writeObject(params);
            out.flush();

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Object ret = in.readObject();

            in.close();
            out.close();

            if (ret instanceof Exception) {
                throw (Exception) ret;
            }

            return ret;
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
