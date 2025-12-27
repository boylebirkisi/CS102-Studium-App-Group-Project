package cs102groupproject.Server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Tracks which users are connected to the server.
 * Acts as a global state for the server package
 * @author Begüm Göktaş
 * Date: 27/12/2025
 */
public class SessionManager {

    private final Map<Integer, ClientConnection> onlineUsers =
            new ConcurrentHashMap<>();

    /**
     * adds the logged-in user to the list of online users
     * @param int user id
     * @param ClientConnection 
     */
    public void login(int userId, ClientConnection connection) {
        onlineUsers.put(userId, connection);
        System.out.println("User logged in: " + userId);
    }

    /**
     * removes the given connection from the list
     * @param Client Connection
     */
    public void logout(ClientConnection connection) {
        onlineUsers.values().remove(connection);
        System.out.println("User logged out");
    }

    public ClientConnection getConnection(int userId) {
        return onlineUsers.get(userId);
    }

    public boolean isOnline(int userId) {
        return onlineUsers.containsKey(userId);
    }
}
