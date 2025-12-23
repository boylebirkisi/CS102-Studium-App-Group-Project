package cs102groupproject.Client;

import java.time.LocalDateTime;
import java.util.Map;

import com.google.api.client.auth.oauth2.Credential;

import cs102groupproject.App;
import cs102groupproject.Server.AuthService;
import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.AppEvent;
import cs102groupproject.SharedObjects.ProtocolMessage;
import cs102groupproject.SharedObjects.User;
import cs102groupproject.SharedObjects.UserCredentials;
import cs102groupproject.SharedObjects.VerificationCode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

public class LoginController{
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField departmentField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField codeField;

    private static String accessToken = null;
    private static String email = null;
    private static final AuthService manager = new AuthService();

    @FXML
    public void initialize() {

        WebSocketClient.addListener(ActionType.LOGIN_SUCCESS, (payload) -> {
            
            Platform.runLater(() -> {
                    try {
                        App.loadScrollableScene();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        });

        WebSocketClient.addListener(ActionType.REGISTER_SUCCESS, (payload) -> {
            
            Platform.runLater(() -> {
                    try {
                        App.loadScrollableScene();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        });

        WebSocketClient.addListener(ActionType.CODE_SUCCESS, (payload) -> {

            Platform.runLater(() -> {
                    try {
                        App.setRoot("Avatar");;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        });
    }

    //! LOGIN1 METHODS

    @FXML
    public void handleRegisterButton() { 
        try {
            App.setRoot("VerificationPage");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgetPassword() {
        try {
            App.setRoot("VerificationPage");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        ProtocolMessage msg = new ProtocolMessage(ActionType.DEV_LOGIN, 
            new UserCredentials(
                username,
                null,
                null,
                password,
                false,
                ""
            )
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
                    accessToken
                );

                WebSocketClient.send(msg);

                System.out.println("Event create lala");
        
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    //! VERIFICATION PAGE METHODS

    @FXML
    private void handleSendCode() {
        System.out.println("Send Code button clicked!");
        this.email = emailField.getText();
        System.out.println("Email: " + email);

        WebSocketClient.send(new ProtocolMessage(
            ActionType.SEND_VERIFICATION_CODE,
            email
        ));
    }

    @FXML
    public void verifyCode() {
        if (!codeField.getText().isEmpty()) {
            String codeEntered = codeField.getText();
            WebSocketClient.send(new ProtocolMessage(
                ActionType.VERIFY_CODE,
                // In order not to create a new object
                new UserCredentials(null, email, null, codeEntered, true, "")
            ));
        }
    }

    @FXML
    public void registerWithGoogleButton() {
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

                Platform.runLater(() -> {
                    this.accessToken = accessToken; 
                        try {
                            App.setRoot("Avatar"); 
                        } catch (Exception e) { e.printStackTrace();
                    }
                });
        
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    //! AVATAR PAGE

    @FXML
    public void handleRegistration() {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || departmentField.getText().isEmpty())
            return;

        String username = usernameField.getText();
        String password = passwordField.getText();
        String department = departmentField.getText();

        System.out.println("ACCESSTOKEN: " + accessToken);
        ProtocolMessage msg = null;
        if (accessToken == null) {
            msg = new ProtocolMessage(
                ActionType.REGISTER,
                new UserCredentials(username, this.email, null, password, false, department)
            );
        } else {
            msg = new ProtocolMessage(
                ActionType.REGISTER_WITH_GOOGLE,
                new UserCredentials(username, null, accessToken, password, false, department)
            );
        }

        WebSocketClient.send(msg);
    }  
}
