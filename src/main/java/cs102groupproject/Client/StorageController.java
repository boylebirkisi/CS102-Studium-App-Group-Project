
package cs102groupproject.Client;

import java.io.IOException;

import cs102groupproject.SharedObjects.Furniture;
import cs102groupproject.SharedObjects.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Controller for handling the user’s stored furniture.
 * @author Gülşen Mercan
 */
public class StorageController {

    @FXML
    private VBox contentBox;

    private static Storage storage;
    private static StorageController instance;

    public static void setStorage(Storage s) {
        storage = s;
        refreshStatic();
    }

    /**
     * Initializes the controller instance.
     */
    @FXML
    public void initialize() {
        instance = this;
        refresh();
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
     * Refreshes the storage, adds all owned storage furniture.
     */
    private void refresh() {
        contentBox.getChildren().clear();
        if (storage == null) return;

        for (Furniture f : storage.getOwnedFurnitures()) {

            Button btn = new Button(f.getName());
            btn.setMaxWidth(Double.MAX_VALUE);

            btn.setOnAction(e -> {
                storage.removeFurniture(f);
                OfficeController.setPendingFurniture(f);
                refresh();
                try {
                    App.setRoot("Office1");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            contentBox.getChildren().add(btn);
        }
    }

    /**
     * Returns to the office view and closes the side panel.
     * @param e
     * @throws IOException
     */
    @FXML
    private void goBack(ActionEvent e) throws IOException {
        App.setRoot("Office1");
    }
}
