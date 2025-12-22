package cs102groupproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import cs102groupproject.Client.WebSocketClient;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Login1TEST"), 640, 480); // to use login test, change primary to Login1TEST 
        stage.setScene(scene);
        stage.show();

        try {
            WebSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        var url = App.class.getResource("/" + fxml + ".fxml");
        System.out.println("FXML URL = " + url);

        if (url == null) {
            throw new RuntimeException("FXML FILE NOT FOUND");
        }

        FXMLLoader loader = new FXMLLoader(url);
        return loader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setScene(Scene newScene) {
        scene = newScene;
    }
}
