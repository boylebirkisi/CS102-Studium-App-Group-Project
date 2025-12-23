package cs102groupproject;

import cs102groupproject.SharedObjects.Furniture;
import cs102groupproject.SharedObjects.MarketPlace;
import cs102groupproject.SharedObjects.Storage;
import cs102groupproject.SharedObjects.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    public static Storage storage = new Storage();
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
    public void start(Stage stage) throws IOException {

        // MARKET DUMMY ITEMS
        market.getAvailableItems().add(
            new Furniture(
                1,
                "Computer",
                "A simple desk lamp",
                50, 30,
                100, 0,
                "DESK_ZONE",
                "/images/computer_middle_01.png"
            )
        );

        market.getAvailableItems().add(
            new Furniture(
                2,
                "Wall Painting",
                "Vintage painting",
                80, 80,
                150, 0,
                "WALL_LEFT",
                "/images/painting_vintage_small_01.png"
            )
        );

        market.getAvailableItems().add(
            new Furniture(
                3,
                "Floor Plant",
                "Green plant",
                120, 60,
                200, 0,
                "FLOOR",
                "/images/plant_cactus_small_01.png"
            )
        );

        scene = new Scene(loadFXML("Office1"), 1280, 720);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader loader = new FXMLLoader(
        App.class.getResource("/cs102groupproject/" + fxml + ".fxml")
    );

    Parent root = loader.load();

    Object controller = loader.getController();

    if (controller instanceof StorageController) {
        StorageController.setStorage(storage);
    }

    if (controller instanceof MarketController) {
        MarketController.setMarket(market);
    }

    return root;
}


    public static void main(String[] args) {
        launch();
    }
}
