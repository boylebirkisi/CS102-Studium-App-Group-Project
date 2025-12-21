package cs102groupproject.Client;

import cs102groupproject.App;
import cs102groupproject.SharedObjects.User;
import javafx.scene.Scene;

public class LoginController{
    SessionManager manager;

    public LoginController(SessionManager manager) {
        this.manager = manager;
    }

    public String getCurrentUserID() {
        if (manager.getCurrentUser() != null) {
            return manager.getCurrentUser().getId();
        }
        return null;
    }

    public boolean isloggedin() {return manager.isLoggedIn();}
    public String getCurrentSessionID() {return manager.getSessionID();}
    public User getCurrentUser() {return manager.getCurrentUser();}

    public void logout() {
        manager.logout();
    }

    public boolean login(User user, String sessionID) {
        manager.login(user, sessionID);
        return manager.isLoggedIn();
    }

    public boolean register(String username, String password) {
        //TO-DO: implement registration logic
        return false;
    }

    public boolean forgotPassword(String username) {
        //TO-DO: implement forgot password logic
        return false;
    }

    public void returnMainLoginPage()
    {
        try
        {
            Scene mainLoginScene = new Scene(App.loadFXML("LogIn.fxml"), 600, 400);
            App.setScene(mainLoginScene);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
