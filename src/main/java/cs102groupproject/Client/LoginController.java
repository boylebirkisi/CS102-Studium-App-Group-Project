package cs102groupproject.Client;

import java.util.Map;

import com.google.api.client.auth.oauth2.Credential;

import cs102groupproject.App;
import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.ProtocolMessage;
import cs102groupproject.SharedObjects.User;
import cs102groupproject.SharedObjects.UserCredentials;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

public class LoginController{
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    SessionManager manager;

    // public String getCurrentUserID() {
    //     if (manager.getCurrentUser() != null) {
    //         return manager.getCurrentUser().getId();
    //     }
    //     return null;
    // }

    // public boolean isloggedin() {return manager.isLoggedIn();}
    // public String getCurrentSessionID() {return manager.getSessionID();}
    // public User getCurrentUser() {return manager.getCurrentUser();}

    // public void logout() {
    //     manager.logout();
    // }

    public boolean login(User user, int sessionID) {
        manager.login(user, sessionID);
        return manager.isLoggedIn();
    }

    @FXML
    public void handleRegisterButton() { 
        try {
            Scene registerScene = new Scene(App.loadFXML("RegisterPage"), 600, 400);
            App.setScene(registerScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgetPassword() {
        System.out.println("Forget Password clicked!");
        // Add your logic here (e.g., opening a new window)
    }

    @FXML
    public void handleEnterLogin() {
        System.out.println("Login button clicked in LoginController");

        if (usernameField.getText().isEmpty()) {
            System.out.println("Username field is empty");
            return;
        }

        if (passwordField.getText().isEmpty()) {
            System.out.println("Password field is empty");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText(); 

        System.out.println("ENTER pressed → dev login: " + username);

        if (!WebSocketClient.isConnected()) {
            System.out.println("❌ Not connected to server yet");
            return;
        }

        ProtocolMessage msg = new ProtocolMessage(ActionType.DEV_LOGIN, new UserCredentials(username, password, false));

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
    public void returnMainLoginPage() {
        try
        {
            Scene mainLoginScene = new Scene(App.loadFXML("Login1TEST"), 600, 400);
            App.setScene(mainLoginScene);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
