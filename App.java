package cs102groupproject;

import cs102groupproject.SharedObjects.Furniture;
import cs102groupproject.SharedObjects.MarketPlace;
import cs102groupproject.SharedObjects.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**Represents the main app by extending application.
 * @author Gülşen Mercan
 * @date 24/12/2025
 */
public class App extends Application {

    private static Scene scene;

    public static MarketPlace market = new MarketPlace();
    public static User currentUser = new User(
            1,
            "testUser",
            "CS",
            null,        
            500,        
            0, 
            true,
            null
    );

    @Override
    /**
     * Starts the application by displaying the office screen.
     * @param primaryStage
     * @throws IOException
     */
    public void start(Stage primaryStage) throws IOException {
        scene = new Scene(loadFXML("Office1"), 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Sets the scene to the FXML input.
     * @param fxml
     */
    public static void setRoot(String fxml) {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load method for FXML input.
     * @param fxml file
     * @return root
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(
            App.class.getResource("/cs102groupproject/" + fxml + ".fxml")
        );

        Parent root = loader.load();

        Object controller = loader.getController();

        if (controller instanceof MarketController) {
            MarketController.setMarket(market);
        }

        return root;
    }

    public static void main(String[] args) {
        launch();
    }
}