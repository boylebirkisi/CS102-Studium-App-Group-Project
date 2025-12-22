// package cs102groupproject;

// import javafx.fxml.FXML;
// import javafx.scene.image.ImageView;
// import javafx.scene.layout.Pane;

// public class OfficeController {

//     private static final double BASE_WIDTH = 600;
//     private static final double BASE_HEIGHT = 400;

//     @FXML private ImageView backgroundView;

//     @FXML private Pane wallLeftPane;
//     @FXML private Pane deskZonePane;
//     @FXML private Pane wallRightPane;
//     @FXML private Pane windowPane;
//     @FXML private Pane floorPane;

//     @FXML
//     public void initialize() {

//         backgroundView.sceneProperty().addListener((obs, oldScene, scene) -> {
//             if (scene == null) return;

//             // background scaling
//             backgroundView.fitWidthProperty().bind(scene.widthProperty());
//             backgroundView.fitHeightProperty().bind(scene.heightProperty());

//             // pane scaling
//             scene.widthProperty().addListener((o, oldW, newW) -> rescale(scene));
//             scene.heightProperty().addListener((o, oldH, newH) -> rescale(scene));

//             rescale(scene);
//         });
//         deskZonePane.setDisable(false);
//         deskZonePane.setMouseTransparent(false);
//         deskZonePane.setOpacity(1.0);
//         deskZonePane.setStyle(
//             "-fx-border-color: red; -fx-border-width: 3;"
//         );
//         deskZonePane.setOnMouseClicked(e -> System.out.println("DESK CLICKED"));
//         deskZonePane.toFront();
//     }

//     @FXML
//     private void deskClicked() {
//         System.out.println("DESK CLICKED");
//     }

//     private void rescale(javafx.scene.Scene scene) {

//         double scaleX = scene.getWidth() / BASE_WIDTH;
//         double scaleY = scene.getHeight() / BASE_HEIGHT;

//         scalePane(wallLeftPane, scaleX, scaleY);
//         scalePane(deskZonePane, scaleX, scaleY);
//         scalePane(wallRightPane, scaleX, scaleY);
//         scalePane(windowPane, scaleX, scaleY);
//         scalePane(floorPane, scaleX, scaleY);
//     }

//     private void scalePane(Pane pane, double sx, double sy) {
//         pane.setLayoutX(pane.getLayoutX() * sx);
//         pane.setLayoutY(pane.getLayoutY() * sy);
//         pane.setPrefWidth(pane.getPrefWidth() * sx);
//         pane.setPrefHeight(pane.getPrefHeight() * sy);
//     }
// }
package cs102groupproject.Client;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import cs102groupproject.App;

public class OfficeController {

    @FXML
    private void deskClicked() throws IOException {
        System.out.println("DESK CLICKED: Going to Market");
        App.setRoot("Market");  // Market.fxml sahnesine geç
    }

    @FXML
    private void floorClicked() throws IOException {
        System.out.println("FLOOR CLICKED: Going to Storage");
        App.setRoot("Storage"); // Storage.fxml sahnesine geç
    }

    @FXML
    private void wallLeftClicked() {
        System.out.println("Wall Left clicked!");
    }

    @FXML
    private void wallRightClicked() {
        System.out.println("Wall Right clicked!");
    }

    @FXML
    private void windowClicked() {
        System.out.println("Window clicked!");
    }

    @FXML
    private void doorClicked() {
        System.out.println("Door clicked!");
    }
}
