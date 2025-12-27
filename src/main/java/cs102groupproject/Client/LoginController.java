package cs102groupproject.Client;

import java.util.ArrayList;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.api.client.auth.oauth2.Credential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cs102groupproject.App;
import cs102groupproject.Server.AuthService;
import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.LoginResponse;
import cs102groupproject.SharedObjects.ProtocolMessage;
import cs102groupproject.SharedObjects.User;
import cs102groupproject.SharedObjects.UserCredentials;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
/**
 * LoginController handles the registration and login processes.
 * Author: Delfin Eryılmaz
 * Date: 27/12/2025
 */
public class LoginController implements UIController {
    String selectedAvatar;
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


    @FXML
    /**
     * Initializing login controller and attach the listeners for the incoming responeses of the server.
     */
    public void initialize() {
        Gson gson = Converters.registerAll(new GsonBuilder()).create();

        WebSocketClient.addListener(ActionType.ERROR, (payload) -> {
            String errorMessage = (String) payload;
            
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error is detected.");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            });
        });

        // After login success
        WebSocketClient.addListener(ActionType.LOGIN_SUCCESS, (payload) -> {
            String jsonString = gson.toJson(payload); 
            LoginResponse rs = gson.fromJson(jsonString, LoginResponse.class);

            if (rs == null || rs.getUser() == null) {
                System.err.println("❌ Critical Error: LoginResponse or User data is missing from server!");
                return;
            }

            User loggedInUser = rs.getUser(); 
            ClientSession.login(loggedInUser);
            ClientSession.setLoginResponse(rs);

            ArrayList<User> userList = new ArrayList<>();
            userList.add(loggedInUser);

            Platform.runLater(() -> {
                    try {
                        // Passing all the data we need on the app.
                        App.loadScrollableScene(rs.getHabits() != null ? new ArrayList<>(rs.getHabits()) : new ArrayList<>(),
                                                rs.getEventsOfUser() != null ? new ArrayList<>(rs.getEventsOfUser()) : new ArrayList<>(),
                                                rs.getTasksOfUser() != null ? new ArrayList<>(rs.getTasksOfUser()) : new ArrayList<>(),
                                                rs.getFriends() != null ? new ArrayList<>(rs.getFriends()) : new ArrayList<>(),
                                                userList, // This one is created locally, so it should be fine
                                                rs.getMessages() != null ? new ArrayList<>(rs.getMessages()) : new ArrayList<>(),
                                                rs.getAllUsers() != null ? new ArrayList<>(rs.getAllUsers()) : new ArrayList<>());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        });

        // After register success
        WebSocketClient.addListener(ActionType.REGISTER_SUCCESS, (payload) -> {
            String jsonString = gson.toJson(payload); 
            LoginResponse rs = gson.fromJson(jsonString, LoginResponse.class);

            User loggedInUser = rs.getUser(); 
            ClientSession.login(loggedInUser);
            ClientSession.setLoginResponse(rs);

            ArrayList<User> userList = new ArrayList<>();
            userList.add(loggedInUser);

            Platform.runLater(() -> {
                    try {
                        ClientSession.login(loggedInUser);
                        // Passing all the data we need on the app.
                        App.loadScrollableScene(rs.getHabits() != null ? new ArrayList<>(rs.getHabits()) : new ArrayList<>(),
                                                rs.getEventsOfUser() != null ? new ArrayList<>(rs.getEventsOfUser()) : new ArrayList<>(),
                                                rs.getTasksOfUser() != null ? new ArrayList<>(rs.getTasksOfUser()) : new ArrayList<>(),
                                                rs.getFriends() != null ? new ArrayList<>(rs.getFriends()) : new ArrayList<>(),
                                                userList, // This one is created locally, so it should be fine
                                                rs.getMessages() != null ? new ArrayList<>(rs.getMessages()) : new ArrayList<>(),
                                                rs.getAllUsers() != null ? new ArrayList<>(rs.getAllUsers()) : new ArrayList<>());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        });

        // After user enter the right code they are redireceted to the avatar page.
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
    /**
     * Open to verification page.
     */
    public void handleRegisterButton() { 
        try {
            App.setRoot("VerificationPage");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgetPassword() {
        // try {
        //     App.loadScrollableScene();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }

    @FXML
    /**
     * Handles login operations.
     */
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

        // Sends request to the server.
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
    /**
     * Handles login with Google Button.
     * @throws Exception
     */
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

                // Sends request to the server
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
    /**
     * Handles sending code request by sends request to the server
     */
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
    /**
     * Verifies the entered code by sending information to the database for checking
     */
    public void verifyCode() {
        if (!codeField.getText().isEmpty()) {
            String codeEntered = codeField.getText();
            
            String email = emailField.getText();
            if (email == null || email.isEmpty()) {
                email = LoginController.email; 
            }

            System.out.println("DEBUG: Verifying for Email -> " + email);

            // Sends request to the darabase
            WebSocketClient.send(new ProtocolMessage(
                ActionType.VERIFY_CODE,
                new UserCredentials(null, email, null, codeEntered, true, "")
            ));
        }
    }

    @FXML
    /**
     * Handles registration operations with googles.
     */
    public void registerWithGoogleButton() {
        new Thread(() -> {
            try {
                // Authenticates user with Google OAuth2
                GoogleOAuthClient oauthClient = new GoogleOAuthClient();
                // Gets OAuth2  Credential
                Credential credential = oauthClient.authenticate();
    
                // Extracts access token from Credential
                String accessToken = credential.getAccessToken();

                //
                //ProtocolMessage msg = new ProtocolMessage(
                    //ActionType.REGISTER_WITH_GOOGLE,
                                //accessToken
                //);

                //WebSocketClient.send(msg);
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
    private void selectAvatar1() {
        selectedAvatar = "Avatar1";
        System.out.println("Selected Avatar 1");
    }

    @FXML
    private void selectAvatar2() {
        selectedAvatar = "Avatar2";
        System.out.println("Selected Avatar 2");
    }

    @FXML
    private void selectAvatar3() {
        selectedAvatar = "Avatar3";
        System.out.println("Selected Avatar 3");
    }

    @FXML
    private void selectAvatar4() {
        selectedAvatar = "Avatar4";
        System.out.println("Selected Avatar 4");
    }


    @FXML
    /**
     * Handles two type of registrations, if it is google registration since we have access token
     * sends request to server accordingly.
     */
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
