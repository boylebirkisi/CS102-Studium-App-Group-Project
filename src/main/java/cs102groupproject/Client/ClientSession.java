package cs102groupproject.Client;

import cs102groupproject.SharedObjects.User;
import cs102groupproject.SharedObjects.LoginResponse;

/**
 * Client-side application context to be used by controllers
 * @author Begüm Göktaş
 */
public final class ClientSession {
    private static User currentUser;
    private static LoginResponse loginResponse;
    private static int activeGroupId;

    private ClientSession() {}
    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
        activeGroupId = -1;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static int getUserId(){
        return getCurrentUser().getId();
    }
    

    public static void setActiveGroupId(int groupId) {
        activeGroupId = groupId;
    }

    public static int getActiveGroupId() {
        return activeGroupId;
    }

    public static boolean isInGroup() {
        return activeGroupId != -1;
    }

    public static LoginResponse getLoginResponse() {
        return loginResponse;
    }

    public static void setLoginResponse(LoginResponse loginResponse) {
        ClientSession.loginResponse = loginResponse;
    }
}
