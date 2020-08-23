import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyMiniClient {

    private int port = -1;
    private String host = "localhost";

    private PrintWriter cOut;
    private BufferedReader cIn;

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String send(String request) {
        if (port >= 0) {
            try (Socket clientSocket = new Socket(host, port);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(clientSocket.getInputStream())
                 )
            ) {
                out.println(request);
                String str;
                StringBuilder strB = new StringBuilder();
                while ((str = in.readLine()) != null) {
                    strB.append(str);
                }
                return strB.toString();
            } catch (UnknownHostException e) {
                return e.getMessage();
            } catch (IOException e) {
                return e.getMessage();
            }
        } else {
            return "#invalid port#";
        }
    }

    public boolean connect() {
        if (port >= 0) {
            try {
                Socket clientSocket = new Socket(host, port);
                 cOut = new PrintWriter(clientSocket.getOutputStream(), true);
                cIn = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );
                return true;
            } catch (UnknownHostException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public String request(String request) {
        try {
            cOut.println(request);
            cOut.flush();
            String str = cIn.readLine();
            return str;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public void disconnect() {
        if (cOut != null && cIn != null) {
            try {
                cIn.close();
                cOut.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
