package cs102groupproject.Client;

import java.util.Map;

import com.google.api.client.auth.oauth2.Credential;

import cs102groupproject.Client.GoogleOAuthClient;
import cs102groupproject.Server.service.AuthService;
import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.ProtocolMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {

    //test
    @FXML
    private TextField usernameField;
    //test
    @FXML
    private TextField passwordField;


    @FXML
    private void switchToSecondary() throws Exception {

    }
    
    // @FXML
    // private void handleLoginButton() {
    //     System.out.println("Login button clicked");
    // }
   
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
                GoogleOAuthClient oauthClient = new GoogleOAuthClient();
                Credential credential = oauthClient.authenticate();

                String accessToken = credential.getAccessToken();

                ProtocolMessage msg = new ProtocolMessage(
                        ActionType.LOGIN_WITH_GOOGLE,
                        Map.of("accessToken", accessToken)
                );

                WebSocketClient.send(msg);

            } 
            catch (Exception e) {
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
