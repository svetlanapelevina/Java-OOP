package console_chat;

import java.io.*;
import java.net.Socket;

public class Client implements ConnectionListener {
    private static final String IP_ADDRESS = "0.0.0.0";
    private static final int PORT = 32000;
    private TCPConnection connection;
    private String name;

    private Client(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter your name: ");
            this.name = reader.readLine();
            Socket socket = new Socket(IP_ADDRESS, PORT);
            this.connection = new TCPConnection(socket, this, this.name);
        } catch (IOException e) {
            System.out.println("Client exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Client();
    }

    @Override
    public synchronized void onReceiveMessage(String message) {
        System.out.println(message);
    }

    @Override
    public synchronized void onConnect(TCPConnection connection) {
        System.out.println("Connected to the server. Now you can start the chat.");
    }

    @Override
    public synchronized void onDisconnect(TCPConnection connection) {
        System.out.println("Disconnected from the server");
    }
}
