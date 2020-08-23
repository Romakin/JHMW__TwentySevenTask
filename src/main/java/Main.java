import java.util.Scanner;

public class Main {

    public static MyMiniClient client = new MyMiniClient();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        showMenu(sc);
    }

    private static void showMenu(Scanner sc) {
        while (true) {
            System.out.println("Menu:\n" +
                    "1. Start listen\n" +
                    "2. Stop listen\n" +
                    "3. Send single request\n" +
                    "4. Send messages\n" +
                    "5. Exit\n" +
                    "Выберите пункт из меню (1-4):");
            switch (sc.nextLine()) {
                case "1":
                    startServer(sc);
                    break;
                case "2":
                    stopServer(sc);
                    break;
                case "3":
                    sendRequest(sc);
                    break;
                case "4":
                    sendMessages(sc);
                    break;
                case "5":
                    System.out.println("Exit");
                    return;
            }
        }
    }

    private static void startServer(Scanner sc) {
        int port = askForPort(sc);
        switch (MyMiniServer.start(port)) {
            case 1:
                System.out.println("Server started");
                break;
            case 0:
                System.out.println("Server is already started before");
                break;
            case -1:
                System.out.println("Invalid port number");
                break;
        }
        sc.nextLine();
    }

    public static int askForPort(Scanner sc) {
        System.out.println("Put port to start \n[Or empty to exit]");
        int port = sc.nextInt();
        client.setPort(port);
        return port;
    }

    private static void stopServer(Scanner sc) {
        if (MyMiniServer.stop()) {
            System.out.println("OK, it stopped");
        } else {
            System.out.println("Already stopped");
        }
    }

    private static void sendRequest(Scanner sc) {
        if (client.getPort() < 0)
            client.setPort(askForPort(sc));
        System.out.println("Put request \n[Or empty to exit]");
        String in = sc.nextLine();
        if (in.length() == 0) {
            return;
        } else  {
            System.out.println(client.send(in));
        }
    }

    private static void sendMessages(Scanner sc) {
        if (client.getPort() < 0)
            client.setPort(askForPort(sc));
        client.setHost("netology.homework");
        if (!client.connect()) {
            return;
        }
        System.out.println("Put requests [Or empty to exit]");
        System.out.println(client.request("####"));
        while (true) {
            String in = sc.nextLine();
            if (in.length() == 0) {
                break;
            } else {
                System.out.println(client.request(in));
            }
        }
        client.disconnect();
    }
}
