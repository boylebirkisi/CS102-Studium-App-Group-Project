package cs102groupproject.Client;

import cs102groupproject.SharedObjects.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AvatarController {
    String selectedAvatar;
    String email;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField departmentField;
    
    @FXML
    public void initialize() {
    }

    public void setFields(String email)
    {
        this.email = email;
    }

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
    private void saveButtonFunction() {
        if (selectedAvatar == null || usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || departmentField.getText().isEmpty()) {
            System.out.println("Please select an avatar before saving.");
            return;
        }
        String username = usernameField.getText();
        String password = passwordField.getText();
        String department = departmentField.getText();
        User newUser = new User(username, department, email, null, 0, 0, false, selectedAvatar);
        //save user to database

    }
}
