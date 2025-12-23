package cs102groupproject.Client;

import cs102groupproject.SharedObjects.Furniture;
import cs102groupproject.SharedObjects.MarketPlace;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MarketController {

    private static MarketController instance;
    private static MarketPlace market;

    @FXML
    private VBox marketBox;

    @FXML
    public void initialize() {
        instance = this;
        refresh();
    }

    public static void setMarket(MarketPlace m) {
        market = m;
        refreshStatic();
    }

    public static void refreshStatic() {
        if (instance != null) {
            instance.refresh();
        }
    }

    private void refresh() {
        marketBox.getChildren().clear();
        if (market == null) return;

        for (Furniture f : market.getAvailableItems()) {

            Button buyBtn = new Button(
                f.getName() + " - $" + f.getGroupPrice()
            );
            buyBtn.setMaxWidth(Double.MAX_VALUE);

            buyBtn.setOnAction(e -> {
                OfficeController.setPendingFurniture(f);
            });

            marketBox.getChildren().add(buyBtn);
        }
    }
}
