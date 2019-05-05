package local.wagenhuber.guenther;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.util.Vector;

public class DemoService {

    public String getEcho(String text) {
        return text;
    }

    public int getSumme(Integer x, Integer y) {
        return x + y;
    }

    public Date getDate() {
        return new Date();
    }

    public void sendMessage(String msg) {
        System.out.println(msg);
    }

    public Vector<String> getMessages(String file) throws Exception {
        Vector<String> lines = new Vector<String>();

        BufferedReader in = new BufferedReader(new FileReader(file));
        String line;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        in.close();
        return lines;
    }
}
