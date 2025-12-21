package cs102groupproject;

import com.google.api.client.auth.oauth2.Credential;
import cs102groupproject.SharedObjects.AppEvent;
import cs102groupproject.Client.GoogleCalendarAPI;
import cs102groupproject.Client.GoogleOAuthClient;
import cs102groupproject.Server.AuthService;
import cs102groupproject.SharedObjects.AppEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.util.converter.LocalDateStringConverter;

public class LoginController {

    @FXML
    private void switchToSecondary() throws Exception {

    }
    
    @FXML
    private void handleLoginButton() {
        System.out.println("Login button clicked");
    }
    
    @FXML
    private void handleLoginWithGoogleButton() throws Exception {
        System.out.println("Login with Google button clicked");
        // Creates a new thread to avoid blocking the JavaFX UI thread (which creates exceptions)
        new Thread(() -> {
            try {
                // Authenticates user with Google OAuth2
                GoogleOAuthClient oauthClient = new GoogleOAuthClient();
                // Gets OAuth2  Credential
                Credential credential = oauthClient.authenticate();
    
                // Extracts access token from Credential
                String accessToken = credential.getAccessToken();
                if (accessToken == null) {
                        throw new RuntimeException("No access token received");
                }
    
                String userId = AuthService.registerWithGoogle(accessToken);
                GoogleCalendarAPI calendarAPI = new GoogleCalendarAPI(credential);

                // Updates UI on JavaFX App Thread
                Platform.runLater(() -> {
                    System.out.println("Login successful, userId=" + userId);
                    try {
                        App.setRoot("secondary");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    
    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleRegisterButton() throws Exception {
        System.out.println("Register button clicked");
        App.setRoot("Register");
    }

    @FXML
    private void handleForgetPassword() {
        System.out.println("Forget Password clicked");
    }
}
