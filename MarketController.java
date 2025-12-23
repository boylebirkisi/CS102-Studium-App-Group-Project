package cs102groupproject;

import cs102groupproject.SharedObjects.Furniture;
import cs102groupproject.SharedObjects.MarketPlace;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MarketController {

    private static MarketController instance;
    private static MarketPlace market;

    @FXML
    private VBox marketBox;

    @FXML
    public void initialize() {
        instance = this;
            if (market == null) {
        market = new MarketPlace(); // TEST İÇİN
    }
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
        System.out.println("Market items: " + market.getAvailableItems().size());

        marketBox.getChildren().clear();
        if (market == null) return;

        for (Furniture f : market.getAvailableItems()) {
            marketBox.getChildren().add(createItemRow(f));
        }
    }

    private HBox createItemRow(Furniture f) {

        HBox row = new HBox(12);
        row.setStyle(
            "-fx-background-color: white;" +
            "-fx-padding: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2);"
        );

        ImageView imageView = new ImageView();
        Image img = new Image(
                getClass().getResource(f.getImagePath()).toExternalForm()
        );
        imageView.setImage(img);
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(f.getName());
        nameLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Label priceLabel = new Label(f.getSoloPrice() + " coins");
        priceLabel.setStyle("-fx-text-fill: #666;");

        VBox textBox = new VBox(4, nameLabel, priceLabel);

        Button buyButton = new Button("Buy");
        buyButton.setStyle("-fx-background-color: #6FCF97; -fx-text-fill: white;");
        buyButton.setOnAction(e -> {
            OfficeController.setPendingFurniture(f);
        });

        row.getChildren().addAll(imageView, textBox, buyButton);
        return row;
    }
}
