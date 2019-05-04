package local.wagenhuber.guenther;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class RPCServer extends Thread {

    private int port;
    private String service;

    public RPCServer(int port, String service) {
        this.port = port;
        this.service = service;
    }

    public void startServer() throws Exception {
        ServerSocket server = new ServerSocket(port);

        InetAddress addr = InetAddress.getLocalHost();
        System.out.println("RPCServer auf " + addr.getHostName() + "/" + addr.getHostAddress() + ":" + port + " gestartet ...");
        System.out.println("Service " + service);


        //For example, the type of String.class is Class<String>. Use Class<?> if the class being modeled is unknown.

        Class<?> serviceClass = Class.forName(service); //Returns the Class object associated with the class or interface with the given string name, using the given class loader
        Object serviceObject = serviceClass.newInstance();

        while (true) {
            Socket client = server.accept();
            new RPCThread(client, serviceObject).start();
        }
    }

    private class RPCThread extends Thread {
        private Socket client;
        private Object serviceObject;

        //Inner Class
        public RPCThread(Socket client, Object serviceObject) {
            this.client = client;
            this.serviceObject = serviceObject;
        }

        public void run() {

            try {
                ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                String name = ((String) in.readObject());
                Object[] params = ((Object[]) in.readObject());


                Class<?>[] types = new Class[params.length];
                for (int i = 0; i < params.length; i++) {
                    types[i] = params[i].getClass();

                    Object ret = null;

                    try {
                        Method m = serviceObject.getClass().getMethod(name, types);
                        ret = m.invoke(serviceObject, params);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();


                        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                        out.writeObject(ret);
                        out.flush();

                        in.close();
                        out.close();
                    }

                }
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                try {
                    if (client != null) {
                        client.close();
                    }
                } catch (IOException e) {

                }
            }

        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("java RPCServer <port> <service>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        String service = args[1];
        new RPCServer(port, service).startServer();
    }
}