package cs102groupproject.Client;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import cs102groupproject.SharedObjects.AppEvent;
import cs102groupproject.SharedObjects.Habit;
import cs102groupproject.SharedObjects.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlannerController {
    ClientSession manager;
    ArrayList<Habit> habits;
    ArrayList<AppEvent> events;
    ArrayList<Task> tasks;
    Habit selectedHabit;
    @FXML
    private Label donePercentTextField;
    @FXML
    private Label dateLabel;
    @FXML
    private ComboBox<Habit> habitTrackerComboBox;
    @FXML
    private Label calendarMonthLabel;
    @FXML
    private AnchorPane paneDaily;
    @FXML
    private FlowPane habitCircleFlowPane;
    @FXML
    private TextField addHabitTextField;
    @FXML
    private VBox eventsVBox;
    @FXML
    private RadioButton dateRadioButton;

    @FXML
    private void initialize()
    {
        habitTrackerComboBox.getSelectionModel()
            .selectedItemProperty()
            .addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    updateHabitTracker(newVal);
                }
            });
    }

    public void setFields(ClientSession manager, ArrayList<Habit> habits, ArrayList<AppEvent> events, ArrayList<Task> tasks) {
        this.manager = manager;
        this.habits = habits;
        this.events = events;
        this.tasks = tasks;
        refreshUI();
    }

    private void refreshUI()
    {
        updateHabitTracker(selectedHabit);
        populateEvents();
    }

    private void updateHabitTracker(Habit habit)
    {
        clearHabitTrackerCircles();
        if (habit == null) return;
        boolean[] habitCompletionArray = new boolean[habit.getCompletionString().length()];
        for (int i = 0; i < habit.getCompletionString().length(); i++)
        {
            char character = habit.getCompletionString().toCharArray()[i];
            if (character == '1')
                habitCompletionArray[i] = true;
        }
        for (int i = 0; i < habitCircleFlowPane.getChildren().size(); i++)
        {
            ((CheckBox)habitCircleFlowPane.getChildren().get(i)).setSelected(habitCompletionArray[i]);;
        }
    }

    private void clearHabitTrackerCircles()
    {
        for (int i = 0; i < habitCircleFlowPane.getChildren().size(); i++)
        {
            ((CheckBox)habitCircleFlowPane.getChildren().get(i)).setSelected(false);
        }
    }

    @FXML
    private void addHabitCircleClicked()
    {
        addHabitTextField.setText("");
        addHabitTextField.setVisible(true);
        addHabitTextField.setDisable(false);
        addHabitTextField.requestFocus();
        habitTrackerComboBox.setDisable(true);
        habitTrackerComboBox.setVisible(false);
        updateHabitTracker(selectedHabit);
    }

    @FXML
    private void addHabitTextFieldAction()
    {
        String habitName = addHabitTextField.getText();
        if (habitName.isEmpty()) return;
        Habit newHabit = new Habit(habitName, manager.getCurrentUser().getId());
        habits.add(newHabit);
        habitTrackerComboBox.getItems().add(newHabit);
        habitTrackerComboBox.setDisable(false);
        habitTrackerComboBox.setVisible(true);
        addHabitTextField.setVisible(false);
        addHabitTextField.setDisable(true);
        addHabitTextField.clear();
    }

    @FXML
    private void populateEvents()
    {
        eventsVBox.getChildren().clear();
        boolean sortByDate = true;
        if (dateRadioButton.isSelected())
            sortByDate = true;
        else
            sortByDate = false;
        ArrayList<AppEvent> sortedEvents = new ArrayList<>();
        if (sortByDate)
        {
            LocalDateTime smallestDate = LocalDateTime.now();
            int index = -1;
            while (sortedEvents.size() < events.size())
            {
                for (int i = 0; i < events.size(); i++)
                {
                    if (sortedEvents.contains(events.get(i))) continue;
                    for (int j = 0; j < events.size(); j++)
                    {
                        if (sortedEvents.contains(events.get(j))) continue;
                        AppEvent event1 = events.get(i);
                        AppEvent event2 = events.get(j);
                        if (event1.getStart().isBefore(event2.getStart()) && event1.getStart().isBefore(smallestDate))
                        {
                            smallestDate = event1.getStart();
                            index = i;
                        }
                        else if (event2.getStart().isBefore(event1.getStart()) && event2.getStart().isBefore(smallestDate))
                        {
                            smallestDate = event2.getStart();
                            index = j;
                        }
                    }
                    if (index != -1) {
                        sortedEvents.add(events.get(index));
                        index = -1;
                    }
                }
            }
        }
        else
        {
            while (sortedEvents.size() < events.size())
            {
                int highestImportance = -1;
                int index = -1;
                for (int i = 0; i < events.size(); i++)
                {
                    if (sortedEvents.contains(events.get(i))) continue;
                    AppEvent event1 = events.get(i);
                    if (event1.getImportance() > highestImportance)
                    {
                        highestImportance = event1.getImportance();
                        index = i;
                    }
                }
                sortedEvents.add(events.get(index));
            }
        }
        for (int i = 0; i < events.size(); i++)
        {
            AppEvent event = events.get(i);
            Label eventLabel = new Label(event.getName());
            VBox dateAndTimeBox = new VBox(new Label(event.getStart().toLocalDate().toString()), new Label(event.getStart().toLocalTime().toString()));
            Circle circle = new Circle(24, event.getColor().isEmpty() ? Color.BLACK : Color.web(event.getColor()));
            HBox eventBox = new HBox(eventLabel, dateAndTimeBox, circle);
            eventBox.setSpacing(15);
            eventsVBox.getChildren().add(eventBox);
        }
    }

    @FXML
    private void addEventButtonClicked()
    {
        //Open event creation pop-up
    }
}