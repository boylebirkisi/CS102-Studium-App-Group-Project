package cs102groupproject.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

import cs102groupproject.App;
import cs102groupproject.SharedObjects.*;

public class MarketController {
    @FXML
    private Label moneyLabel;

    @FXML
    public void initialize() {
        User u = ClientSession.getCurrentUser();
        moneyLabel.setText(
            "Solo: " + u.getSoloCurrency() +
            " | Group: " + u.getGroupCurrency()
        );
    }
    @FXML
    private void goBack() throws IOException {
        System.out.println("BACK TO OFFICE");
        App.setRoot("Office1");
    }
}
