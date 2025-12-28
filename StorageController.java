package cs102groupproject;

import cs102groupproject.SharedObjects.Furniture;
import cs102groupproject.SharedObjects.Storage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**Controller class for storage view in the side panel.
 * @author Gülşen Mercan
 * @date 24/12/2025
 */
public class StorageController {

    @FXML
    private VBox contentBox;

    private static StorageController instance;

    /**Initializes the controller instance. */
    @FXML
    public void initialize() {
        instance = this;
        refresh();
    }

    /**Returns to the office view, and closes the side panel. */
    @FXML
    private void goBack() {
        OfficeController.toggleSidePanelStatic();
    }

    /**Refreshes the controller instance. */
    public static void refreshStatic() {
        if (instance != null) {
            instance.refresh();
        }
    }

    /**Refreshes the storage, adds all owned storage furniture. */
    private void refresh() {
        contentBox.getChildren().clear();

        Storage storage = Storage.getInstance();

        for (Furniture f : storage.getOwnedFurnitures()) {
            contentBox.getChildren().add(createItemCard(f));
        }
    }

    /**
     * Creates the item card for a furniture in the storage view.
     * @param f furniture
     * @return card which includes info of the item and a place button
     */
    private HBox createItemCard(Furniture f) {

        HBox card = new HBox(12);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-padding: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2);"
        );

        ImageView imageView = new ImageView(
            new Image(getClass().getResource(f.getImagePath()).toExternalForm())
        );
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(f.getName());
        nameLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Label priceLabel = new Label(f.getSoloPrice() + " coins");
        priceLabel.setStyle("-fx-text-fill: #666;");

        VBox textBox = new VBox(4, nameLabel, priceLabel);

        Button placeButton = new Button("Place");
        placeButton.setStyle("-fx-background-color: #6FCF97; -fx-text-fill: white;");

        placeButton.setOnAction(e -> {
            Storage.getInstance().removeFurniture(f);
            OfficeController.setPendingFurniture(f);
            refresh();
            OfficeController.toggleSidePanelStatic();
        });

        card.getChildren().addAll(imageView, textBox, placeButton);
        return card;
    }
}
