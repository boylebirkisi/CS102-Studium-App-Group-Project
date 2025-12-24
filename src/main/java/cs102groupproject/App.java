package cs102groupproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import cs102groupproject.Client.OfficeController;
import cs102groupproject.Client.PlannerController;
import cs102groupproject.Client.SocializationController;
import cs102groupproject.Client.WebSocketClient;
import cs102groupproject.SharedObjects.AppEvent;
import cs102groupproject.SharedObjects.ChatMessage;
import cs102groupproject.SharedObjects.GroupSession;
import cs102groupproject.SharedObjects.Habit;
import cs102groupproject.SharedObjects.Task;
import cs102groupproject.SharedObjects.User;

import java.io.IOException;
import java.util.ArrayList;

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
        stage.setMaximized(true);

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

    public static void loadScrollableScene(ArrayList<Habit> habits,
        ArrayList<AppEvent> events, ArrayList<Task> tasks, ArrayList<User> friendsList,
        ArrayList<User> onlineUsers, ArrayList<ChatMessage> chatMessages, ArrayList<User> allUsers, ArrayList<GroupSession> allGroupSessions) throws IOException
    {
        ScrollPane scrollPane = new ScrollPane();
        VBox contentBox = new VBox();
        scrollPane.fitToWidthProperty().set(true);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        contentBox.setSpacing(10);
        scrollPane.setContent(contentBox);

        FXMLLoader officeLoader = loadFXMLReturnLoader("Office1");
        Parent officeView = officeLoader.load();
        contentBox.getChildren().add(officeView);
        OfficeController officeController = officeLoader.getController();

        FXMLLoader plannerLoader = loadFXMLReturnLoader("Planner");
        Parent plannerView = plannerLoader.load();
        contentBox.getChildren().add(plannerView);
        PlannerController plannerController = plannerLoader.getController();
        plannerController.setFields(habits, events, tasks);

        FXMLLoader socializationLoader = loadFXMLReturnLoader("Socialization");
        Parent socializationView = socializationLoader.load();
        contentBox.getChildren().add(socializationView);
        SocializationController socializationController = socializationLoader.getController();
        socializationController.setFields(friendsList, onlineUsers, chatMessages, allUsers, allGroupSessions);

        scene.setRoot(scrollPane);
    }

    public static FXMLLoader loadFXMLReturnLoader(String fxml) throws IOException {
        var url = App.class.getResource("/" + fxml + ".fxml");
        System.out.println("FXML URL = " + url);

        if (url == null) {
            throw new RuntimeException("FXML FILE NOT FOUND");
        }

        FXMLLoader loader = new FXMLLoader(url);
        return loader;
    }
}
