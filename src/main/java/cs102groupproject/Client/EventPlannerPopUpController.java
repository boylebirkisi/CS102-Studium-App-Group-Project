package cs102groupproject.Client;

import java.time.LocalDateTime;

import cs102groupproject.SharedObjects.AppEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class EventPlannerPopUpController {
    PlannerController ownerController;
    ClientSession manager;
    @FXML
    private TextField eventNameTextField;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private HBox importanceButtonsHBox;
    @FXML
    private TextField startTimeTextField;
    @FXML
    private TextField endTimeTextField;

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
    private void handleCreateEventButton()
    {
        String eventName = eventNameTextField.getText();
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
        String[] startTimeParts = startTimeTextField.getText().split(":");
        int startHour = Integer.parseInt(startTimeParts[0]);
        int startMinute = Integer.parseInt(startTimeParts[1]);
        LocalDateTime startDate = startDatePicker.getValue().atTime(startHour, startMinute);
        String[] endTimeParts = endTimeTextField.getText().split(":");
        int endHour = Integer.parseInt(endTimeParts[0]);
        int endMinute = Integer.parseInt(endTimeParts[1]);
        LocalDateTime endDate = endDatePicker.getValue().atTime(endHour, endMinute);
        AppEvent event = new AppEvent(eventName, color, startDate, endDate,
            ClientSession.getUserId(), importance, null);
        ownerController.closePopUp();
    }

    @FXML
    private void handleCancelButton()
    {
        ownerController.closePopUp();
    }
}
