package cs102groupproject;

import cs102groupproject.SharedObjects.Furniture;
import cs102groupproject.SharedObjects.PlaceType;
import cs102groupproject.SharedObjects.Storage;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.text.html.ImageView;

import org.w3c.dom.events.MouseEvent;

/**Controller class for the office view.
 * @author Gülşen Mercan
 * @date 24/12/2025
 */
public class OfficeController {

    private Furniture pendingFurniture;
    private boolean placementMode = false;
    private final Map<Pane, Furniture> occupiedZones = new HashMap<>();
    private final Map<Pane, PlaceType> zoneMap = new HashMap<>();
    private List<Pane> allZones;
    private boolean sideOpen = false;
    private static OfficeController instance;

    @FXML
    private ScrollPane sideDrawer;

    public static OfficeController getInstance() {
        return instance;
    }

    @FXML private Pane DESK_1, DESK_2, WALL_LEFT_1, WALL_RIGHT_1, FLOOR_1;

    @FXML
    /**Initializes the office controller, lists all zones. */
    public void initialize() {
        instance = this;
        System.out.println("OfficeController initialized");

        sideDrawer.setTranslateX(300);
        sideDrawer.setVisible(false);
        sideDrawer.setManaged(false);

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

    /**
     * Placement method for furnitures.
     * @param f furniture to place
     */
    private void enterPlacementMode(Furniture f) {
        pendingFurniture = f;
        placementMode = true;
        showPreviews();
    }

    /**Exist from the placement mode. */
    private void exitPlacementMode() {
        pendingFurniture = null;
        placementMode = false;

        for (Pane zone : allZones) {
            zone.setStyle("");
            zone.getChildren().removeIf(n -> n instanceof ImageView && !occupiedZones.containsKey(zone));
            zone.setVisible(occupiedZones.containsKey(zone));
            zone.setMouseTransparent(!occupiedZones.containsKey(zone));
        }
    }

    /**
     * Calls enter placement mode for the furniture f if conditions are met.
     * @param f furniture
     */
    public static void setPendingFurniture(Furniture f) {
        System.out.println("PENDING SET: " + f.getName() + " " + f.getCategory()); //Control
        if (instance != null && f != null) {
            instance.enterPlacementMode(f);
        }
    }

    /**Shows previews for furniture at the zones that are available. */
    private void showPreviews() {
        for (Pane zone : allZones) {
            if (!occupiedZones.containsKey(zone)) {
                zone.getChildren().clear();
                zone.setVisible(false);
                zone.setMouseTransparent(true);
                zone.setStyle("");
            }
        }
        if (!placementMode || pendingFurniture == null) return;

        PlaceType needed = pendingFurniture.getPlaceType();
        System.out.println("Preview for: " + needed);

        for (Pane zone : allZones) {
            PlaceType zoneType = zoneMap.get(zone);
            System.out.println(zone.getId() + " -> " + zoneType);

            if (zoneType != needed) continue;

            ImageView preview = new ImageView(
                new Image(getClass().getResourceAsStream(
                    pendingFurniture.getImagePath()
                ))
            );

            preview.setFitWidth(zone.getPrefWidth());
            preview.setFitHeight(zone.getPrefHeight());
            preview.setOpacity(0.5);
            preview.setMouseTransparent(true);

            zone.getChildren().setAll(preview);
            zone.setVisible(true);
            zone.setMouseTransparent(false);
            zone.setStyle("-fx-background-color: rgba(0,255,0,0.3);");
        }
    }

    /**
     * Placement method for furniture, shows a confirmation message, and handles left-right click.
     * @param e mouse event
     */
    @FXML
    private void placeFurniture(MouseEvent e) {

        if (e.getButton() != MouseButton.PRIMARY) return;
        if (!placementMode || pendingFurniture == null) return;

        Pane zone = (Pane) e.getSource();
        if (occupiedZones.containsKey(zone)) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Placement");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to place the furniture here?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            exitPlacementMode();
            return; 
        }

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

        zone.getChildren().setAll(real);
        zone.setVisible(true);
        zone.setMouseTransparent(false);

        occupiedZones.put(zone, placed);

        zone.setOnContextMenuRequested(ev -> {
            ContextMenu menu = new ContextMenu();
            MenuItem toStorage = new MenuItem("Send to storage");

            toStorage.setOnAction(ae -> {
                Furniture f = occupiedZones.get(zone);

                zone.getChildren().clear();
                occupiedZones.remove(zone);
                zone.setVisible(false);
                zone.setMouseTransparent(true);

                Storage.getInstance().addFurniture(f);
                StorageController.refreshStatic();
            });

            menu.getItems().add(toStorage);
            menu.show(zone, ev.getScreenX(), ev.getScreenY());
            ev.consume();
        });

        exitPlacementMode();
    }

    /**Resets zone visibility to false. */
    private void resetZones() {
        for (Pane zone : allZones) {
            zone.getChildren().clear();
            zone.setVisible(false);
            zone.setMouseTransparent(true);
            zone.setStyle("");
        }
    }

    /**Opens session pop up. */
    @FXML
    private void openSessionPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs102groupproject/StudySessionPopup.fxml"));
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

    /** Handles the opening and closing of the side panel.*/
    @FXML
    public void toggleSidePanel() {

        TranslateTransition tt = new TranslateTransition(Duration.millis(250), sideDrawer);

        if (!sideOpen) {
            sideDrawer.setVisible(true);
            sideDrawer.setManaged(true);
            tt.setFromX(300);
            tt.setToX(0);
            sideOpen = true;
        } else {
            tt.setFromX(0);
            tt.setToX(300);
            tt.setOnFinished(e -> {
                sideDrawer.setVisible(false);
                sideDrawer.setManaged(false);
            });
            sideOpen = false;
        }

        tt.play();
    }
//-----
    public static void toggleSidePanelStatic() {
        if (instance != null) {
            instance.toggleSidePanel();
        }
    }

    public static void openSessionPopupStatic() {
        if (instance != null) {
            instance.openSessionPopup();
        }
    }
}
