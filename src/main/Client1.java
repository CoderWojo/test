import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client1 implements Runnable{
    private Socket client;
    // TO ZADANIE poniżej przeznaczymy dla oddzielnej klasy INNER
    private BufferedReader in;// odczytuj z serwera 
    private PrintWriter out; // zapisuj do serwera, użyjemy w InputHandler 
    private boolean done;

    private String host;
    private int port_number;

    public Client1(String host, int port_number) {
        this.host = host;
        this.port_number = port_number;
        this.done = false;
    }

    @Override
    public void run() {
        // spróbuj się połączyć, jak sie polaczysz to odczytuj dane z serwera
        try {
            client = new Socket(host, port_number);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            InputHandler inputHandler = new InputHandler();
            // rób to w osobnym wątku
            Thread t = new Thread(inputHandler);
            t.start();

            String message;
            while((message = in.readLine()) != null) {
                System.out.println(message);// wiadomość od serwera (broadcast())
            }

            // zajmij sie przetwarzaniem danych z cmd

        } catch(IOException e) {
            shutdown(); // rozlacz sie z serwerem
        }
        
    }

    public void shutdown() {
        try {
            done = true; // przerwnij odczytywanie z konsoli
            if(!client.isClosed()) {
                client.close();
            }
            in.close();
            out.close();
        } catch (IOException e) {
            
        }
    }

    // klasa która odczytuje wiadomości z konsoli i przetwwarza je tzn wysyła do serwera
    class InputHandler implements Runnable {
        private String inMessage;

        @Override
        public void run() {
            // odczytuj wiadomości z cmd
            try {
                BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                while(!done) { // dopóki 
                    // odczytuj z konsoli i wysyłaj do serwera
                    inMessage = stdin.readLine();
                    out.println(inMessage);
                    if(inMessage.equals("/quit")) {
                        // rozłącz się z serwerem
                        stdin.close();
                        shutdown();
                    }
                }
            } catch(IOException e) {
                shutdown();
            }
        }
    }

    public static void main(String[] args) {
        Client1 client1 = new Client1("localhost", 9999);
        client1.run();
    }
}