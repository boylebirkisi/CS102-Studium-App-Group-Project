package cs102groupproject.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

import cs102groupproject.SharedObjects.*;
import cs102groupproject.App;

public class MarketController {
    SessionManager manager;
    @FXML
    private Label moneyLabel;

    public MarketController(SessionManager manager) {
        this.manager = manager;
        initialize();
    }

    @FXML
    public void initialize() {
        User u = manager.getCurrentUser();
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
