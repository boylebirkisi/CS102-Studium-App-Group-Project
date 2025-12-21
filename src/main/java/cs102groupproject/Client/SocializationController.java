package cs102groupproject.Client;

import java.util.ArrayList;

import cs102groupproject.SharedObjects.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SocializationController {
    SessionManager manager;
    ArrayList<User> friendsList;
    @FXML
    private Label currencyEarnedLabel;

    public SocializationController(SessionManager manager) {
        this.manager = manager;
        friendsList = new ArrayList<>();
    }

    public ArrayList<User> getFriendsList() {return friendsList;}
    public void addFriend(User friend) {friendsList.add(friend);}
    public void removeFriend(User friend) {friendsList.remove(friend);}

    @FXML
    private void displayCurrencyEarned()
    {
        currencyEarnedLabel.setText("You have earned " + manager.getCurrentUser().getGroupCurrency() + " so far. Study more, earn more.");
    }

    @FXML
    private void createGroupSessionButtonFunctionality()
    {
        // Open the group session creation pop-up window
    }
}
