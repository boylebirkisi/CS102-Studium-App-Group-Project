package cs102groupproject.Server;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

import cs102groupproject.SharedObjects.*;

import java.io.IOException;
import java.util.List;

public class AuthService {
    private static DBManager dbManager = new DBManager();
    private static PasswordHasher hasher = new PasswordHasher();
    // Login with Google
    public static User loginWithGoogle(String accessToken) {

        try {
            // Sets the access token
            HttpRequestInitializer initializer = request -> request.getHeaders().setAuthorization("Bearer " + accessToken);

            // Build Oauth2 service
            Oauth2 oauth2 = new Oauth2.Builder(
                    GoogleCredentialManager.HTTP_TRANSPORT,
                    GoogleCredentialManager.JSON_FACTORY,
                    initializer
            ).setApplicationName("Studium").build();

            // Gets user info from Google
            Userinfo userInfo = oauth2.userinfo().get().execute();
            String googleId = userInfo.getId();

            if (dbManager.getUserByGoogleID(googleId) == null) {
                throw new RuntimeException("No user found with this Google ID");
            } else {
                return dbManager.getUserByGoogleID(googleId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Google login failed", e);
        }
    }

    /**
     * Register with google
     * @param credentials
     * @return
     */
    public static User registerWithGoogle(UserCredentials credentials) {
        String accessToken = credentials.getAccessToken();

        try {
            // Sets the access token
            HttpRequestInitializer requestInitializer = request -> request.getHeaders().setAuthorization("Bearer " + accessToken);

            // Build Oauth2 service
            Oauth2 oauth2 = new Oauth2.Builder(
                    GoogleCredentialManager.HTTP_TRANSPORT,
                    GoogleCredentialManager.JSON_FACTORY,
                    requestInitializer
            ).setApplicationName("Studium").build();

            // Gets the user info
            Userinfo userInfo = oauth2.userinfo().get().execute();
            String googleId = userInfo.getId();
            
            System.out.println("Google ID: " + googleId);
            if (dbManager.getUserByGoogleID(googleId) != null) {
                throw new RuntimeException("User with this Google ID already exists");
            } else {
                dbManager.insertUser(
                    credentials.getUsername(),
                    userInfo.getEmail(),
                    hasher.hashPassword(credentials.getPassword()),
                    googleId,
                    credentials.getDepartment()
                );
            }
            return dbManager.getUserByGoogleID(googleId);

        } catch (Exception e) {
            throw new RuntimeException("Google registration failed", e);
        } 
    }

    /**
     * Logs in a user with given username and password.
     * @return
     */
    public static User login(UserCredentials credentials) {
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        } else {
            String password = dbManager.getPasswordByUsername(credentials.getUsername());
            PasswordHasher hasher = new PasswordHasher();
            if (password != null && hasher.checkPassword(credentials.getPassword(), password)) {
                User user = dbManager.getUserByUsername(credentials.getUsername());
                return user;
            } 
        }
        return null;
    }

    /**
    * Registers a new user with given details.
    * @return
    */
    public static User register(UserCredentials credentials) {
        System.out.println("dsfsaf register");
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        } else {
            if (dbManager.getUserByEmail(credentials.getEmail()) == null) {
                System.out.println("INSERT USER DB");
                dbManager.insertUser(credentials.getUsername(), credentials.getEmail(), hasher.hashPassword(credentials.getPassword()), null, credentials.getDepartment());
            
                return dbManager.getUserByEmail(credentials.getEmail());
            } else {
                throw new IllegalArgumentException("User already exists.");
            }
        }
    }

    /**
     * Send verification code.
     * @param email
     * @return
     */
    public static VerificationCode sendVerificationCode(String email) {

        String randomCode = EmailService.generateRandomCode(6);
        long expiry = EmailService.calculateExpiryTime();
        VerificationCode code = new VerificationCode(randomCode, email, expiry);

        boolean emailSent = EmailService.sendMail(email, "Your Verification Code", "Your code is: " + code.getStoredCode());

        if (emailSent) {
            boolean isInserted = dbManager.insertVerificationCode(email, code.getStoredCode(), code.getExpiryTime());
            if (!isInserted) {
                throw new RuntimeException("Failed to store verification code in the database.");
            }
            return code;
        } 
        return null;
    }

    public static boolean verifyCode(String email, String code) {
        VerificationCode vCode = dbManager.getVerificationCode(email);
        System.out.println("vCode: " + vCode);
        if (vCode != null) {
            if (code.equals(vCode.getStoredCode())) {
                return true;
            }
        }
        return false;
    }

    public LoginResponse loginResponse(int userId, User user) {
    System.out.println("\n--- Starting LoginResponse Construction for User ID: " + userId + " ---");

    try {
        System.out.print("1/9 Fetching Friends... ");
        List<User> friends = dbManager.getFriends(userId);
        System.out.println("Done (" + friends.size() + " found)");

        System.out.print("2/9 Fetching All Users... ");
        List<User> allUsers = dbManager.getAllUsers();
        System.out.println("Done (" + allUsers.size() + " found)");

        System.out.print("3/9 Fetching Notifications... ");
        List<Notification> notifications = dbManager.getAllNotifications();
        System.out.println("Done");

        System.out.print("4/9 Fetching Chat Messages... ");
        List<ChatMessage> messages = dbManager.getChatMessages(userId);
        System.out.println("Done");

        System.out.print("5/9 Fetching Events... ");
        List<AppEvent> events = dbManager.getAllEvents(userId);
        System.out.println("Done");

        System.out.print("6/9 Fetching Tasks... ");
        List<Task> tasks = dbManager.getAllTasks(userId);
        System.out.println("Done");

        System.out.print("7/9 Fetching Habits... ");
        List<Habit> habits = dbManager.getAllHabits(userId);
        System.out.println("Done");

        System.out.print("8/9 Fetching Sessions... ");
        List<Session> sessions = dbManager.getAllIndividualSessions(userId);
        System.out.println("Done");

        System.out.println("9/9 Creating final LoginResponse object...");
        LoginResponse rs = new LoginResponse(
            user, 
            friends, 
            allUsers, 
            notifications, 
            messages, 
            events, 
            tasks, 
            habits, 
            sessions
        );

        System.out.println("--- LoginResponse Successfully Built ---\n");
        return rs;

    } catch (Exception e) {
        System.err.println("\n❌ CRITICAL FAILURE during LoginResponse construction!");
        System.err.println("Error Message: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}

}