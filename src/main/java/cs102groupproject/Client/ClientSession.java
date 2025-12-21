package cs102groupproject.Client;

public class ClientSession {
    private static String userId;
    private static boolean loggedIn;

    public static void login(String uid) {
        userId = uid;
        loggedIn = true;
    }

    public static String getUserId() {
        return userId;
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static void logout() {
        userId = null;
        loggedIn = false;
    } 
}
