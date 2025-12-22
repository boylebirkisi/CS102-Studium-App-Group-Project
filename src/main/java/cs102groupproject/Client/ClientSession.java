package cs102groupproject.Client;

public class ClientSession {
    private static int userId;
    private static boolean loggedIn;

    public static void login(int uid) {
        userId = uid;
        loggedIn = true;
    }

    public static int getUserId() {
        return userId;
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static void logout() {
        userId = -1; //indicates no user is logged in
        loggedIn = false;
    } 
}
