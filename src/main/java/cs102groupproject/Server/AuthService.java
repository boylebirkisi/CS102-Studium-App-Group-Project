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
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        } else {
            if (dbManager.getUserByEmail(credentials.getEmail()) == null) {
                int userID = dbManager.insertUser(credentials.getUsername(), credentials.getEmail(), hasher.hashPassword(credentials.getPassword()), null, credentials.getDepartment());
                return dbManager.getUserByID(userID);
            }
        }
        return null;
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
        if (vCode != null && !vCode.isExpired()) {
            if (code.equals(vCode.getStoredCode())) {
                return true;
            }
        }
        return false;
    }

    public List<User> getFriendsOfUser(int userId) {
        return dbManager.getFriends(userId);
    }

    public List<Habit> getAllOfUser(int userId) {
        return dbManager.getAllHabits(userId);
    }

    public List<AppEvent> getAllEvents(int userId) {
        return dbManager.getAllEvents(userId);
    }

    public List<Habit> getAllHabits(int userId) {
        return dbManager.getAllHabits(userId);
    }

    public List<ChatMessage> getAllMessages(int userId) {
        return dbManager.getChatMessages(userId);
    }

    public List<Session> getAllIndividualSessions(int userId) {
        return dbManager.getAllIndividualSessions(userId);
    }

    public List<Notification> getAllNotifications(int userId) {
        return dbManager.getAllNotifications();
    }
}