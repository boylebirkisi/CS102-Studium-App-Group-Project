
package cs102groupproject.Client;

import java.io.IOException;

import cs102groupproject.App;
import cs102groupproject.SharedObjects.Furniture;
import cs102groupproject.SharedObjects.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class StorageController {

    @FXML
    private VBox contentBox;

    private static Storage storage;
    private static StorageController instance;

    public static void setStorage(Storage s) {
        storage = s;
        refreshStatic();
    }

    @FXML
    public void initialize() {
        instance = this;
        refresh();
    }

    public static void refreshStatic() {
        if (instance != null) {
            instance.refresh();
        }
    }

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
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            });

            contentBox.getChildren().add(btn);
        }
    }

    @FXML
    private void goBack(ActionEvent e) throws IOException {
        App.setRoot("Office1");
    }
}
