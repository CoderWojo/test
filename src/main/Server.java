import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done; // czy wylaczamy serwer
    private ExecutorService pool;

    public Server() {
        this.done = false;
        connections = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
    
            while(!done) {
                Socket client = server.accept();// oczekuj na żądania
                ConnectionHandler handler = new ConnectionHandler(client);// zajmij się tym klientem
                connections.add(handler);
                pool.execute(handler);
            }
        } catch(IOException e) {
            shutdown(); // shutdown the server
        }
        
    }

    public void broadcast(String message) {
        for(ConnectionHandler ch : connections) {
            ch.sendMessage(message);
        }
    }
    
    public void shutdown() {
        try {
            done = true;
            pool.shutdown();
            if(!server.isClosed()) {
                server.close();
            }
            for(ConnectionHandler ch : connections) {
                ch.shutdown();
            }
        } catch(IOException e) {
            // ignore
        }
    }
    
    class ConnectionHandler implements Runnable {
        private BufferedReader in; // odczytuj od gniazda klienta
        private PrintWriter out; // wysyłaj do klienta
        private Socket client;
        private String nickname;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {// zainicjuj strumienie
            try {
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);
                // zapytaj o nick
                out.println("Please enter a nickname: ");
                nickname = in.readLine();
                System.out.println(nickname + " connedted!."); // logujemy w konsoli serwera
                broadcast(nickname + " joined the chat!");

                String message;
                while((message = in.readLine()) != null) {
                    String[] messageSplit = message.split(" ", 2);// użyj spacji jako sepilitor, podziel na 2
                    if(message.startsWith("/nick")) {    
                        if(messageSplit[1] != null) {
                            broadcast(nickname + " renamed themeselves to " + messageSplit[1]);
                            nickname = messageSplit[1];
                            out.println("Successfully nickname changed to " + nickname);
                        } else {
                            out.println("No nickname provided!");
                        }
                    } else if(message.equals("/quit")) {
                        broadcast(nickname + " left the chat!");
                        shutdown(); // zamknij połączenie odpowiedzialne za tego klienta
                    } else {
                        // basic message
                        broadcast(nickname + ": " + message);
                    }
                    
                }
            } catch(IOException e) {
                shutdown();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void shutdown() {
            try {
                in.close();
                out.close();
                if(!client.isClosed()) {
                    client.close();
                }
            } catch(IOException e) {
                // ignore
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}