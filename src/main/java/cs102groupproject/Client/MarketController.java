package cs102groupproject.Client;

import cs102groupproject.SharedObjects.Furniture;
import cs102groupproject.SharedObjects.MarketPlace;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
/**
 * Controller for marketplace adjustments and it allows
 * users to purchase furniture items.
 * @author Gülşen Mercan
 */
public class MarketController {

    private static MarketController instance;
    private static MarketPlace market;

    @FXML
    private VBox marketBox;

    /**
     * Initializes the marketplace and controller.
     */
    @FXML
    public void initialize() {
        instance = this;
        refresh();
    }

    /**
     * Assigns the marketplace to the chosen market.
     * @param MarketPlace
     */
    public static void setMarket(MarketPlace m) {
        market = m;
        refreshStatic();
    }

    /**
     * Refreshes the controller instance.
     */
    public static void refreshStatic() {
        if (instance != null) {
            instance.refresh();
        }
    }

    /**
     * Refreshes the marketplace and adds furniture.
     */
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
