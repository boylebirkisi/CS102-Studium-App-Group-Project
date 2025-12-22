package cs102groupproject.Server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Tracks which users are connected to the server.
 */
public class SessionManager {

    private final Map<Integer, ClientConnection> onlineUsers =
            new ConcurrentHashMap<>();

    public void login(int userId, ClientConnection connection) {
        onlineUsers.put(userId, connection);
        System.out.println("🟢 User logged in: " + userId);
    }

    public void logout(ClientConnection connection) {
        onlineUsers.values().remove(connection);
        System.out.println("🔴 User logged out");
    }

    public ClientConnection getConnection(int userId) {
        return onlineUsers.get(userId);
    }

    public boolean isOnline(int userId) {
        return onlineUsers.containsKey(userId);
    }
}
