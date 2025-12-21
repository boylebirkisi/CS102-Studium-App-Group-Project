package cs102groupproject;

import java.util.Map;
import com.google.api.client.auth.oauth2.Credential;
import cs102groupproject.SharedObjects.AppEvent;
import cs102groupproject.Client.GoogleCalendarAPI;
import cs102groupproject.Client.GoogleOAuthClient;
import cs102groupproject.Server.AuthService;
import cs102groupproject.SharedObjects.AppEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.util.converter.LocalDateStringConverter;
import cs102groupproject.SharedObjects.ProtocolMessage;
import cs102groupproject.SharedObjects.ActionType;
import javafx.scene.control.TextField; //test

public class LoginController {

     //test used with Login1TEST.fxml
    @FXML
    private TextField usernameField;
    //test
    @FXML
    private TextField passwordField;

    
    @FXML
    private void switchToSecondary() throws Exception {

    }
    
    @FXML
    private void handleLoginButton() {
        System.out.println("Login button clicked");
    }

    //test 
    @FXML
    private void handleEnterLogin() {

        String username = usernameField.getText();
        String password = passwordField.getText(); // şimdilik kullanılmayacak

        System.out.println("ENTER pressed → dev login: " + username);

        if (!WebSocketClient.isConnected()) {
            System.out.println("❌ Not connected to server yet");
            return;
        }
        ProtocolMessage msg = new ProtocolMessage(
                ActionType.DEV_LOGIN,
                Map.of("username", username)
        );

        WebSocketClient.send(msg);
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
                
                ProtocolMessage msg = new ProtocolMessage(
                        ActionType.LOGIN_WITH_GOOGLE,
                        Map.of("accessToken", accessToken)
                );

                WebSocketClient.send(msg);
                //String userId = AuthService.registerWithGoogle(accessToken);
                //GoogleCalendarAPI calendarAPI = new GoogleCalendarAPI(credential);

                // Updates UI on JavaFX App Thread
                //Platform.runLater(() -> {
                    //System.out.println("Login successful, userId=" + userId); //CHECK THIS PART userID may be 
                    //try {
                        //App.setRoot("secondary");
                   // } catch (Exception e) {
                       // e.printStackTrace();
                    //}
               // });
    
    
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
