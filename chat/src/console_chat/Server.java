package console_chat;

import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements ConnectionListener {
    private final ArrayList<TCPConnection> connectionsList = new ArrayList<>();
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        new Server(32000);
    }

    private Server(Integer port) {
        System.out.println("Server running...");
        try{
            serverSocket = new ServerSocket(port);
            while (true) {
                new TCPConnection(serverSocket.accept(), this, null);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public synchronized void onConnect(TCPConnection connection) {
        connectionsList.add(connection);
        System.out.println(connectionsList.size() + " Client connected");
    }

    @Override
    public synchronized void onReceiveMessage(String message) {
        sendMessageToAllConnections(message);
        System.out.println(message);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection connection) {
        connectionsList.remove(connection);
        System.out.println("Client disconnected");
    }

    private void sendMessageToAllConnections(String message) {
        for (TCPConnection connection: connectionsList) {
            connection.sendMessage(message);
        }
    }
}
