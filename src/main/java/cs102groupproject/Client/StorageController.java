package cs102groupproject.Client;

import javafx.fxml.FXML;
import java.io.IOException;
import cs102groupproject.App;

public class StorageController {

    @FXML private void initialize()
    {
    }
    
    @FXML
    private void goBack() throws IOException {
        System.out.println("BACK TO OFFICE");
        App.setRoot("Office1");  // Office1.fxml sahnesine geri dön
    }
}
