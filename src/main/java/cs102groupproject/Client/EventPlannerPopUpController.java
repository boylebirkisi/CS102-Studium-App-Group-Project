package cs102groupproject.Client;

import java.time.LocalDateTime;

import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.AppEvent;
import cs102groupproject.SharedObjects.ProtocolMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class EventPlannerPopUpController implements UIController {
    PlannerController ownerController;
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
        // WebSocketClient.addListener(ActionType.EVENT_CREATED, (payload) -> {
        //     AppEvent newEvent = (AppEvent) payload;
        //     Platform.runLater(() -> {
        //         System.out.println("new event is added " + newEvent.getName());
        //     });
        // });
    }

    public void setFields(PlannerController ownerController)
    {
        this.ownerController = ownerController;
    }

    @FXML
    private void handleCreateEventButton()
    {
        try {
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
        int startHour = Integer.parseInt(startTimeParts[0].trim());
        int startMinute = Integer.parseInt(startTimeParts[1].trim());
        LocalDateTime startDate = startDatePicker.getValue().atTime(startHour, startMinute);

        String[] endTimeParts = endTimeTextField.getText().split(":");
        int endHour = Integer.parseInt(endTimeParts[0].trim());
        int endMinute = Integer.parseInt(endTimeParts[1].trim());
        LocalDateTime endDate = endDatePicker.getValue().atTime(endHour, endMinute);

        AppEvent event = new AppEvent(eventName, color, startDate, endDate,
                ClientSession.getUserId(), importance, ""); // String png null olmasın boş olsun

        WebSocketClient.send(new ProtocolMessage(ActionType.CREATE_EVENT, event));
        ownerController.addEvent(event);
        ownerController.refreshUI();
        ownerController.closePopUp();
        } catch (Exception e) {
        System.err.println("Zaman formatı hatası! Lütfen HH:mm şeklinde girin.");
    }
    }

    @FXML
    private void handleCancelButton()
    {
        ownerController.closePopUp();
    }
}
