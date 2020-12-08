package console_chat;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPConnection {
    private final Socket socket;
    private final ConnectionListener connectionListener;
    private final BufferedReader server;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final Thread thread;
    private final String name;

    public TCPConnection(Socket socket, ConnectionListener connectionListener, String name) throws IOException {
        this.name = name;
        this.socket = socket;
        this.connectionListener = connectionListener;
        this.server = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TCPConnection.this.connectionListener.onConnect(TCPConnection.this);
                try{
                    while (true) {
                        if (TCPConnection.this.server.ready()) {
                            connectionListener.onReceiveMessage(
                                    TCPConnection.this.server.readLine());
                        }
                        if (TCPConnection.this.in.ready()) {
                            TCPConnection.this.sendMessage(TCPConnection.this.in.readLine());
                        }
                    }
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                    connectionListener.onDisconnect(TCPConnection.this);
                    disconnect();
                }
            }
        });
        this.thread.start();
    }

    public synchronized void sendMessage(String message) {
        try {
            if (this.name != null) {
                message = this.name + ": " + message;
            }
            out.write(message + "\n");
            out.flush();
        } catch (Exception e) {
            disconnect();
        }
    }

    public synchronized void disconnect() {
        try {
            socket.close();
            out.close();
            in.close();
            server.close();
        } catch (Exception e) {
            System.out.println("TCPConnection exception: " + e.getMessage());
        }
    }
}
