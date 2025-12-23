package cs102groupproject.Client;

import cs102groupproject.SharedObjects.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class TaskPlannerPopUpController {
    PlannerController ownerController;
    ClientSession manager;
    @FXML
    private TextField taskNameTextField;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private HBox importanceButtonsHBox;

    @FXML
    private void initialize()
    {
    }

    public void setFields(PlannerController ownerController, ClientSession manager)
    {
        this.ownerController = ownerController;
        this.manager = manager;
    }

    @FXML
    private void handleCreateTaskButton()
    {
        String taskName = taskNameTextField.getText();
        String color = colorPicker.getValue().toString();
        int importance = 1;
        for (int i = 0; i < importanceButtonsHBox.getChildren().size(); i++)
        {
            RadioButton rb = (RadioButton) importanceButtonsHBox.getChildren().get(i);
            if (rb.isSelected())
            {
                importance = Integer.parseInt(rb.getText().charAt(0) + "");
            }
        }
        Task task = new Task(taskName, color, importance, ClientSession.getUserId(),
            ownerController.getSelectedDate(), null);
        ownerController.closePopUp();
    }

    @FXML
    private void handleCancelButton()
    {
        ownerController.closePopUp();
    }
}
