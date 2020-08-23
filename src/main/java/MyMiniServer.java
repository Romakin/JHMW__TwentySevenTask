import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

public class MyMiniServer implements Runnable {

    int port;

    public MyMiniServer(int port) {
        this.port = port;
    }

    public void run() {

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            Socket client = serverSocket.accept();
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            System.out.println("New connection accepted");

            final String name = in.readLine();
            switch (name) {
                case "####":
                    miniLogic(out, in);
                    break;
                default:
                    out.println(String.format("Hi %s, your port is %d", name, client.getPort()));
                    break;
            }
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void miniLogic(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Write your name");
        final String name = in.readLine();
        out.println("Are you child? (yes/no)");
        final String isChild = in.readLine();
        if (isChild.matches("yes")) {
            out.println(String.format("Welcome to the kids area, %s! Let's play!", name));
        } else {
            out.println(String.format("Welcome to the adult zone, %s! Have a good rest, or a good working day!", name));
        }
    }

    public static int start(int port) {
        if (port > 0 && port < 65535) {
            boolean alreadyStarted = false;
            Set<Thread> threads = Thread.getAllStackTraces().keySet();
            for (Thread t : threads) {
                if (t.getName().equals(MyMiniServer.class.toString())) {
                    alreadyStarted = true;
                    break;
                }
            }
            if (alreadyStarted) {
                return 0;
            } else {
                Thread t = new Thread(new MyMiniServer(port));
                t.setName(MyMiniServer.class.toString());
                t.start();
                return 1;
            }
        } else {
            return -1;
        }
    }

    public static boolean stop() {
        Thread MyMiniServerThread = null;
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        for (Thread t : threads) {
            if (t.getName().equals(MyMiniServer.class.toString())) {
                MyMiniServerThread = t;
                break;
            }
        }
        if (MyMiniServerThread != null && MyMiniServerThread.isAlive()) {
            MyMiniServerThread.interrupt();
            return true;
        } else {
            return false;
        }
    }
}
