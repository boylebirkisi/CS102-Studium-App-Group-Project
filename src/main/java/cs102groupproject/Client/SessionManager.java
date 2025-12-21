package cs102groupproject.Client;
import cs102groupproject.SharedObjects.User;
import jakarta.websocket.Session; 

public class SessionManager {
    User currentUser;
    String sessionID;
    boolean isLoggedIn;
    Session client;

    public SessionManager(Session client) {
        this.client = client;
        isLoggedIn = false;
        currentUser = null;
        sessionID = null;
    }

    public void login(User user, String sessionID) {
        this.currentUser = user;
        this.sessionID = sessionID;
        this.isLoggedIn = true;
    }

    public void logout() {
        this.currentUser = null;
        this.sessionID = null;
        this.isLoggedIn = false;
    }

    public boolean isLoggedIn() {return isLoggedIn;}
    public User getCurrentUser() {return currentUser;}
    public String getSessionID() {return sessionID;}
}
