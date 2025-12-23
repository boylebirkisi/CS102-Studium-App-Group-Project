package cs102groupproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class SidePanelController {

    @FXML
    private VBox contentBox;

    @FXML
    public void initialize() {
    }

    @FXML
    private void showMarket() {
        load("Market");
    }

    @FXML
    private void showStorage() {
        load("Storage");
    }

    private void load(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/cs102groupproject/" + fxml + ".fxml")
            );

            Parent view = loader.load();
            Object controller = loader.getController();

            if (controller instanceof MarketController) {
                MarketController mc = (MarketController) controller;
                MarketController.setMarket(App.market);
                mc.refreshStatic();
            }

            if (controller instanceof StorageController) {
                StorageController sc = (StorageController) controller;
                StorageController.setStorage(App.storage);
                sc.refreshStatic();
            }

            contentBox.getChildren().setAll(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
