package cs102groupproject.Client;
import cs102groupproject.SharedObjects.User;
import jakarta.websocket.Session; 

public class SessionManager {
    User currentUser;
    int sessionID;
    boolean isLoggedIn;
    Session client;

    public SessionManager(Session client) {
        this.client = client;
        isLoggedIn = false;
        currentUser = null;
        sessionID = -1; //indicates no session
    }

    public void login(User user, int sessionID) {
        this.currentUser = user;
        this.sessionID = sessionID;
        this.isLoggedIn = true;
    }

    public void logout() {
        this.currentUser = null;
        this.sessionID = -1; //indicates no session
        this.isLoggedIn = false;
    }

    public boolean isLoggedIn() {return isLoggedIn;}
    public User getCurrentUser() {return currentUser;}
    public int getSessionID() {return sessionID;}
}
