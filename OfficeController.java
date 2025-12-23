package cs102groupproject;

import cs102groupproject.SharedObjects.Furniture;
import cs102groupproject.SharedObjects.PlaceType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfficeController {

    private static OfficeController instance;

    public static void setPendingFurniture(Furniture f) {
        if (instance != null && f != null) {
            instance.enterPlacementMode(f);
        }
    }

    private Furniture pendingFurniture;
    private boolean placementMode = false;
    private final Map<Pane, Furniture> occupiedZones = new HashMap<>();
    private final Map<Pane, PlaceType> zoneMap = new HashMap<>();
    private List<Pane> allZones;

    @FXML private Pane DESK_1, DESK_2, WALL_LEFT_1, WALL_RIGHT_1, FLOOR_1;

    @FXML
    public void initialize() {
        instance = this;

        allZones = List.of(
            DESK_1, DESK_2,
            WALL_LEFT_1, WALL_RIGHT_1,
            FLOOR_1
        );

        zoneMap.put(DESK_1, PlaceType.DESK_ZONE);
        zoneMap.put(DESK_2, PlaceType.DESK_ZONE);
        zoneMap.put(WALL_LEFT_1, PlaceType.WALL_LEFT);
        zoneMap.put(WALL_RIGHT_1, PlaceType.WALL_RIGHT);
        zoneMap.put(FLOOR_1, PlaceType.FLOOR);

        resetZones();
    }

    private void enterPlacementMode(Furniture f) {
        pendingFurniture = f;
        placementMode = true;
        showPreviews();
    }

    private void exitPlacementMode() {
        pendingFurniture = null;
        placementMode = false;

        for (Pane zone : allZones) {
            zone.setStyle("");
            zone.getChildren().removeIf(n -> n instanceof ImageView
                    && !occupiedZones.containsKey(zone));

            zone.setVisible(occupiedZones.containsKey(zone));
            zone.setMouseTransparent(!occupiedZones.containsKey(zone));
        }
    }

    private void showPreviews() {
        if (!placementMode || pendingFurniture == null) return;

        PlaceType needed = PlaceType.valueOf(pendingFurniture.getCategory());

        for (Pane zone : allZones) {

            if (occupiedZones.containsKey(zone)) continue;
            if (zoneMap.get(zone) != needed) continue;

            ImageView preview = new ImageView(
                new Image(getClass().getResourceAsStream(
                    pendingFurniture.getImagePath()
                ))
            );

            preview.setFitWidth(zone.getPrefWidth());
            preview.setFitHeight(zone.getPrefHeight());
            preview.setPreserveRatio(true);
            preview.setOpacity(0.5);
            preview.setMouseTransparent(true);

            zone.getChildren().setAll(preview);
            zone.setVisible(true);
            zone.setMouseTransparent(false);
            zone.setStyle("-fx-background-color: lightgreen;");
        }
    }

    @FXML
    private void placeFurniture(MouseEvent e) {

        if (e.getButton() != MouseButton.PRIMARY) return;
        if (!placementMode || pendingFurniture == null) return;

        Pane zone = (Pane) e.getSource();
        if (occupiedZones.containsKey(zone)) return;

        Furniture placed = pendingFurniture;

        ImageView real = new ImageView(
            new Image(getClass().getResourceAsStream(
                placed.getImagePath()
            ))
        );

        real.setFitWidth(zone.getPrefWidth());
        real.setFitHeight(zone.getPrefHeight());
        real.setPreserveRatio(true);
        real.setMouseTransparent(true);
        zone.setOnContextMenuRequested(ev -> {
            ContextMenu menu = new ContextMenu();
            MenuItem toStorage = new MenuItem("Storage'a gönder");

            toStorage.setOnAction(ae -> {
                zone.getChildren().clear();
                occupiedZones.remove(zone);
                zone.setVisible(false);
                zone.setMouseTransparent(true);

                App.storage.addFurniture(placed);
                StorageController.refreshStatic();
            });

            menu.getItems().add(toStorage);
            menu.show(zone, ev.getScreenX(), ev.getScreenY());
            ev.consume();
        });

        zone.getChildren().setAll(real);
        occupiedZones.put(zone, placed);

        App.market.removeItem(placed);
        StorageController.refreshStatic();
        MarketController.refreshStatic();

        exitPlacementMode();
    }

    private void resetZones() {
        for (Pane zone : allZones) {
            zone.getChildren().clear();
            zone.setVisible(false);
            zone.setMouseTransparent(true);
            zone.setStyle("");
        }
    }

    @FXML
    private void openSessionPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cs102groupproject/SessionPopup.fxml")
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Study Session");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
