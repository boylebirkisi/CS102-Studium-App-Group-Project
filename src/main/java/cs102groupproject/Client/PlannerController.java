package cs102groupproject.Client;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.AppEvent;
import cs102groupproject.SharedObjects.Habit;
import cs102groupproject.SharedObjects.ProtocolMessage;
import cs102groupproject.SharedObjects.Task;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PlannerController implements UIController {
    ArrayList<Habit> habits;
    ArrayList<AppEvent> events;
    ArrayList<Task> tasks;
    Habit selectedHabit;
    Stage popUpStage;
    @FXML
    private Label donePercentTextField;
    @FXML
    private Label dateLabel;
    @FXML
    private ChoiceBox<Habit> habitTrackerComboBox;
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
    private DatePicker datePicker;
    @FXML
    private HBox dailyTasksVBoxHBox;
    @FXML
    private RadioButton dailyRadioButton;
    @FXML
    private RadioButton weeklyRadioButton;
    @FXML
    private FlowPane weeklyTasksVBoxFlowPane;
    @FXML
    private ScrollPane weeklyScrollPane;

    @FXML
    private void initialize()
    {
        WebSocketClient.addListener(ActionType.HABIT_CREATED, (payload) -> {
        Habit savedHabit = (Habit) payload;
        Platform.runLater(() -> {
                // habits.add(savedHabit);
                habitTrackerComboBox.getItems().add(savedHabit);
                System.out.println("habit is added to the list after the approval of server");
            });
        });

        WebSocketClient.addListener(ActionType.TASK_CREATED, (payload) -> {
            Task newTask = (Task) payload;
            Platform.runLater(() -> {
                tasks.add(newTask);
                updateDate();
                System.out.println("new task is added " + newTask.getName());
            });
        });

        WebSocketClient.addListener(ActionType.TASK_UPDATED, (payload) -> {
            Task updatedTask = (Task) payload;
            Platform.runLater(() -> {
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getId() == updatedTask.getId()) {
                        tasks.set(i, updatedTask);
                        break;
                    }
                }
                updateDate();
            });
        });
    }

    public void setFields(ArrayList<Habit> habits, ArrayList<AppEvent> events, ArrayList<Task> tasks) {

        this.habits = (habits != null) ? habits : new ArrayList<>();
        this.events = (events != null) ? events : new ArrayList<>();
        this.tasks = (tasks != null) ? tasks : new ArrayList<>();
        
        javafx.application.Platform.runLater(() -> {
        // Check for nulls just in case fx:id is missing in FXML
        if (datePicker != null) {
            if (datePicker.getValue() == null) {
                datePicker.setValue(LocalDate.now());
            }
        }
        
        if (habitTrackerComboBox != null) {
            refreshUI();
        } else {
            System.err.println("Error: habitTrackerComboBox is null. Check fx:id in Planner.fxml");
        }
        });
    }

    private void refreshUI()
    {
        updateHabitTracker(selectedHabit);
        populateEvents();
        habitTrackerComboBox.getSelectionModel()
            .selectedItemProperty()
            .addListener((obs, oldVal, newVal) -> {
                if (newVal != null)
                    {updateHabitTracker(newVal);}
            });
    }

    private void updateHabitTracker(Habit habit)
    {
        // for (int i = 0; i < habitCircleFlowPane.getChildren().size(); i++) {
        //     CheckBox cb = (CheckBox) habitCircleFlowPane.getChildren().get(i);
        //     final int index = i; // Lambda içinde kullanmak için final olmalı

        //     cb.setSelected(habit.getCompletionString().charAt(i) == '1');

        //     // clicking
        //     cb.setOnAction(e -> {
        //         // calling insert
        //         habit.invertCompletedAtIndex(index); 
                
        //         // send the recent one to server
        //         WebSocketClient.send(new ProtocolMessage(ActionType.UPDATE_HABIT, habit));
                
        //         System.out.println("Habit güncellendi, yeni completion: " + habit.getCompletionString());
        //     });
        // }
        clearHabitTrackerCircles();
        if (habit == null) return;
        boolean[] habitCompletionArray = new boolean[habit.getCompletionString().length()];
        for (int i = 0; i < habit.getCompletionString().length(); i++)
        {
            char character = habit.getCompletionString().toCharArray()[i];
            if (character == '1')
                {habitCompletionArray[i] = true;}
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
        Habit newHabit = new Habit(habitName, ClientSession.getUserId());
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
            {sortByDate = true;}
        else
            {sortByDate = false;}
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
    private void addEventButtonClicked() throws IOException
    {
        EventPlannerPopUpController eventController = (EventPlannerPopUpController) showPopUp("EventPlannerPopUp");
        eventController.setFields(this);
        popUpStage.setTitle("Add Event Pop-Up Menu");
    }

    @FXML
    private void addTaskButtonClicked() throws IOException
    {
        TaskPlannerPopUpController taskController = (TaskPlannerPopUpController) showPopUp("TaskPlannerPopUp");
        taskController.setFields(this);
        popUpStage.setTitle("Add Task Pop-Up Menu");
    }

    public UIController showPopUp(String fxmlPath) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlPath + ".fxml"));
        Parent popUpRoot = loader.load();
        UIController controller = loader.getController();
        Scene scene = new Scene(popUpRoot);
        popUpStage = new Stage();
        popUpStage.setScene(scene);
        popUpStage.initStyle(StageStyle.UTILITY);
        popUpStage.initModality(Modality.WINDOW_MODAL);
        popUpStage.initOwner(eventsVBox.getScene().getWindow());
        popUpStage.show();
        popUpStage.setResizable(false);
        popUpStage.centerOnScreen();
        return controller;
    }

    public void closePopUp()
    {
        popUpStage.close();
    }

    public LocalDate getSelectedDate()
    {
        return datePicker.getValue();
    }

    @FXML
    private void updateDate()
    {
        if (datePicker.getValue() == null)
            {datePicker.setValue(LocalDate.now());}
        calendarMonthLabel.setText(datePicker.getValue().getMonth().toString().toLowerCase() + " " + datePicker.getValue().getYear());
        dateLabel.setText(datePicker.getValue().getMonth().toString().toLowerCase() + " " + datePicker.getValue().getDayOfMonth() + ", " + datePicker.getValue().getYear());
        if (dailyRadioButton.isSelected())
            {drawTasksForDate(datePicker.getValue(), true);}
        else
            {drawTasksForDate(datePicker.getValue(), false);}
    }

    private void drawTasksForDate(LocalDate date, boolean daily)
    {
        dailyTasksVBoxHBox.setVisible(false);
        dailyTasksVBoxHBox.setManaged(false);
        weeklyScrollPane.setVisible(false);
        weeklyScrollPane.setManaged(false);
        int completedTasks = 0;
        int totalTasks = 0;
        for (Node node : dailyTasksVBoxHBox.getChildren())
        {
            ((VBox)node).getChildren().clear();
        }
        if (daily)
        {
            dailyTasksVBoxHBox.setVisible(true);
            dailyTasksVBoxHBox.setManaged(true);
            for (int i = 0; i < tasks.size(); i++)
            {
                Task task = tasks.get(i);
                if (task.getDueDate().isEqual(date))
                {
                    if (task.getIsCompleted())
                        {completedTasks++;}
                    totalTasks++;
                    CheckBox taskCheckBox = new CheckBox("");
                    taskCheckBox.setSelected(task.getIsCompleted());
                    taskCheckBox.setOnAction(e -> {
                        if (taskCheckBox.isSelected())
                            {task.completeTask();}
                        else
                            {task.uncompleteTask();}
                        WebSocketClient.send(new ProtocolMessage(ActionType.UPDATE_TASK, task));     
                        updateDate();
                    });
                    Circle colorCircle = new Circle(8, Color.web(task.getColor()));
                    HBox taskBox = new HBox(new Label(task.getName()), taskCheckBox, colorCircle);
                    if (i % 2 == 0)
                        ((VBox)dailyTasksVBoxHBox.getChildren().get(0)).getChildren().add(taskBox);
                    else
                        ((VBox)dailyTasksVBoxHBox.getChildren().get(1)).getChildren().add(taskBox);
                }
            }
            donePercentTextField.setText("%" + (int)((double)completedTasks / totalTasks * 100));
        }
        else
        {
            weeklyScrollPane.setVisible(true);
            weeklyScrollPane.setManaged(true);
            for (int i = 0; i < tasks.size(); i++)
            {
                Task task = tasks.get(i);
                LocalDate taskDueDate = task.getDueDate();
                LocalDate startOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
                LocalDate endOfWeek = startOfWeek.plusDays(6);
                if ((taskDueDate.isEqual(startOfWeek) || taskDueDate.isAfter(startOfWeek)) &&
                    (taskDueDate.isEqual(endOfWeek) || taskDueDate.isBefore(endOfWeek)))
                {
                    
                    if (task.getIsCompleted())
                        {completedTasks++;}
                    totalTasks++;
                    DayOfWeek dayOfWeek = taskDueDate.getDayOfWeek().minus(DayOfWeek.MONDAY.getValue());
                    Label label = new Label(task.getName());
                    if (task.getIsCompleted())
                        {label.setStyle("-fx-strikethrough: true;");}
                    ((VBox)weeklyTasksVBoxFlowPane.getChildren().get(dayOfWeek.getValue())).getChildren().add(label);
                }
            }
            donePercentTextField.setText("%" + (int)((double)completedTasks / totalTasks * 100));
        }
    }
}
