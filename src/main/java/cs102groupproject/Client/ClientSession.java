package cs102groupproject.Client;

import cs102groupproject.SharedObjects.User;

/**
 * Client-side application context.
 * UI needs this, server does NOT.
 */
public final class ClientSession {
    //controllers will be added
    private static User currentUser;
    private static int activeGroupId; // Group / Study session ID (business)

    private ClientSession() {
    }

    /* ===== AUTH ===== */

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
    
    /* ===== GROUP SESSION ===== */

    public static void setActiveGroupId(int groupId) {
        activeGroupId = groupId;
    }

    public static int getActiveGroupId() {
        return activeGroupId;
    }

    public static boolean isInGroup() {
        return activeGroupId != -1;
    }
}
