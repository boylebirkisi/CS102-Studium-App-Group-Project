package cs102groupproject.Client;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GroupSessionPlannerPopUpController {
    SocializationController ownerController;
    SessionManager manager;
    @FXML
    private Button createGroupSessionButton;
    
    private void initialize()
    {
    }

    public void setControllerAndManager(SocializationController ownerController, SessionManager manager)
    {
        this.ownerController = ownerController;
        this.manager = manager;
    }

    public void showPopUp() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GroupSessionPlannerPopUp.fxml"));
        Parent popUpRoot = loader.load();
        Scene scene = new Scene(popUpRoot);
        Stage popUpStage = new Stage();
        popUpStage.setScene(scene);
        popUpStage.setTitle("Group Session Planner");
        popUpStage.setScene(new Scene(popUpRoot));
        popUpStage.initStyle(StageStyle.UTILITY);
        popUpStage.initModality(Modality.WINDOW_MODAL);
        popUpStage.initOwner(createGroupSessionButton.getScene().getWindow());
        popUpStage.show();
        popUpStage.setResizable(false);
        popUpStage.centerOnScreen();
    }
}
