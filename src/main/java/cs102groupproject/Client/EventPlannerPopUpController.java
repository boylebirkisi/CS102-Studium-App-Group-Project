package cs102groupproject.Client;

import java.time.LocalDateTime;
import java.util.ArrayList;

import cs102groupproject.SharedObjects.AppEvent;
import cs102groupproject.SharedObjects.GroupSession;
import cs102groupproject.SharedObjects.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

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
        //for ()
        String[] startTimeParts = startTimeTextField.getText().split(":");
        int startHour = Integer.parseInt(startTimeParts[0]);
        int startMinute = Integer.parseInt(startTimeParts[1]);
        LocalDateTime startDate = startDatePicker.getValue().atTime(startHour, startMinute);
        String[] endTimeParts = endTimeTextField.getText().split(":");
        int endHour = Integer.parseInt(endTimeParts[0]);
        int endMinute = Integer.parseInt(endTimeParts[1]);
        LocalDateTime endDate = endDatePicker.getValue().atTime(endHour, endMinute);
        AppEvent event = new AppEvent(eventName, color, startDate, endDate, ClientSession.getUserId(), 0, eventName);
        //ownerController.closePopUp();
    }

    @FXML
    private void handleCancelButton()
    {
        //ownerController.closePopUp();
    }
}
