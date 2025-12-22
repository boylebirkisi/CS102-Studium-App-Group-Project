package cs102groupproject.Server;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

import cs102groupproject.SharedObjects.User;
import cs102groupproject.SharedObjects.UserCredentials;

import java.io.IOException;

public class AuthService {
    private static DBManager dbManager = new DBManager();
    // Login with Google
    public static String loginWithGoogle(String accessToken) {

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

            return googleId;
        } catch (Exception e) {
            throw new RuntimeException("Google login failed", e);
        }
    }

    // Google Register
    public static String registerWithGoogle(String accessToken) {

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
            return googleId;

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
            String password = dbManager.getPasswordByUsername(credentials.getEmailOrUsername());
            PasswordHasher hasher = new PasswordHasher();
            if (password != null && hasher.checkPassword(credentials.getPassword(), password)) {
                User user = dbManager.getUserByUsername(credentials.getEmailOrUsername());
                return user;
            } 
        }
        return null;
    }

    /**
     * Registers a new user with given details.
     * @return
     */
    public static User register(User user, UserCredentials credentials) {
        if (user == null || credentials == null) {
            throw new IllegalArgumentException("User and credentials cannot be null");
        } else {
            PasswordHasher hasher = new PasswordHasher();
            String hashedPassword = hasher.hashPassword(credentials.getPassword());
            int userID = dbManager.insertUser(user.getUsername(), user.getEmail(), hashedPassword, user.getGoogleID());
            if (userID != -1) {
                return dbManager.getUserByID(userID);
            } else {
                return null;
            }
        }
    }
}