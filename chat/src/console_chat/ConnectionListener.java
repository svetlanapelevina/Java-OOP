package console_chat;

public interface ConnectionListener {
    void onReceiveMessage(String value);
    void onConnect(TCPConnection connection);
    void onDisconnect(TCPConnection connection);
}
