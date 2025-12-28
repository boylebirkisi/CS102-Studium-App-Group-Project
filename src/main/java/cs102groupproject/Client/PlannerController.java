package cs102groupproject.Client;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import cs102groupproject.SharedObjects.ActionType;
import cs102groupproject.SharedObjects.AppEvent;
import cs102groupproject.SharedObjects.Habit;
import cs102groupproject.SharedObjects.ProtocolMessage;
import cs102groupproject.SharedObjects.Task;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * Controller for the planner UI.
 * @authors: Ali Mersin / Begüm Göktaş(partially)
 */
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
        // HABIT_CREATED
        WebSocketClient.addListener(ActionType.HABIT_CREATED, (payload) -> {
            String json = WebSocketClient.getGson().toJson(payload);
            Habit savedHabit = WebSocketClient.getGson().fromJson(json, Habit.class);
            Platform.runLater(() -> {
                if (habits == null) habits = new ArrayList<>();
                habits.add(savedHabit);
                habitTrackerComboBox.getItems().add(savedHabit);
                habitTrackerComboBox.setValue(savedHabit);
                refreshUI();
            });
        });

        // TASK_CREATED
        WebSocketClient.addListener(ActionType.TASK_CREATED, (payload) -> {
            String json = WebSocketClient.getGson().toJson(payload);
            Task newTask = WebSocketClient.getGson().fromJson(json, Task.class);
            Platform.runLater(() -> {
                if (tasks == null) tasks = new ArrayList<>();
                tasks.add(newTask);
                updateDate();
            });
        });

        // TASK_UPDATED
        WebSocketClient.addListener(ActionType.TASK_UPDATED, (payload) -> {
            try {
                String json = WebSocketClient.getGson().toJson(payload);
                Task updatedTask = WebSocketClient.getGson().fromJson(json, Task.class);

                Platform.runLater(() -> {
                    if (tasks != null) {
                        for (int i = 0; i < tasks.size(); i++) {
                            if (tasks.get(i).getId() == updatedTask.getId()) {
                                tasks.set(i, updatedTask);
                                break;
                            }
                        }
                        updateDate(); 
                    }
                });
            } catch (Exception e) {
                System.err.println("Task Update hatası: " + e.getMessage());
            }
        });

        WebSocketClient.addListener(ActionType.EVENT_CREATED, (payload) -> {
            // Yanlış: AppEvent ev = (AppEvent) payload; 
            
            // Doğru: Önce JSON stringine çevir, sonra AppEvent olarak oku
            String json = WebSocketClient.getGson().toJson(payload);
            AppEvent newEvent = WebSocketClient.getGson().fromJson(json, AppEvent.class);

            Platform.runLater(() -> {
                if (events == null) events = new ArrayList<>();
                events.add(newEvent);
                populateEvents();
                System.out.println("Başarıyla eklendi: " + newEvent.getName());
            });
        });
    }

    /**
     * Sets the data fields for the planner.
     * @param habits the list of habits
     * @param events the list of events
     * @param tasks the list of tasks
     */
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

    /**
     * Refreshes the UI components of the planner
    */
    public void refreshUI()
    {
        updateHabitTracker(selectedHabit);
        populateEvents();
        habitTrackerComboBox.getSelectionModel()
            .selectedItemProperty()
            .addListener((obs, oldVal, newVal) -> {
                if (newVal != null)
                    {updateHabitTracker(newVal);}
            });
        updateDate();
    }

    /**
     * Updates the habit tracker display based on the selected habit.
     * @param habit the selected habit to display
     */
    private void updateHabitTracker(Habit habit)
    {
        clearHabitTrackerCircles();
        if (habit == null) return;
        for (int i = 0; i < habitCircleFlowPane.getChildren().size(); i++) {
            CheckBox cb = (CheckBox) habitCircleFlowPane.getChildren().get(i);
            final int index = i;
            cb.setOnAction(null);
            cb.setSelected(habit.getCompletionString().charAt(i) == '1');

            // clicking
            cb.setOnAction(e -> {
                        habit.invertCompletedAtIndex(index); 
                        WebSocketClient.send(new ProtocolMessage(ActionType.UPDATE_HABIT, habit));
                        System.out.println("Habit is updated " + habit.getCompletionString());
            });
        }
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

    /**
     * Clears all habit tracker checkboxes
     */
    private void clearHabitTrackerCircles()
    {
        for (int i = 0; i < habitCircleFlowPane.getChildren().size(); i++)
        {
            ((CheckBox)habitCircleFlowPane.getChildren().get(i)).setSelected(false);
        }
    }

    /**
     * Called when add habit circle is clicked, shows text field to add new habit
    */
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

    /**
     * Called when enter is pressed in add habit text field, adds new habit
    */
    @FXML
    private void addHabitTextFieldAction()
    {
        String habitName = addHabitTextField.getText();
        if (habitName.isEmpty()) return;
        Habit newHabit = new Habit(habitName, ClientSession.getUserId());
        habits.add(newHabit);
        habitTrackerComboBox.getItems().add(newHabit);
        habitTrackerComboBox.setValue(newHabit);
        selectedHabit = newHabit;
        habitTrackerComboBox.setDisable(false);
        habitTrackerComboBox.setVisible(true);
        addHabitTextField.setVisible(false);
        addHabitTextField.setDisable(true);
        addHabitTextField.clear();
        updateHabitTracker(newHabit);
    }

    /**
     * Updates the events list display
    */
    @FXML
    private void populateEvents()
    {
        eventsVBox.getChildren().clear();
        boolean sortByDate = true;
        if (dateRadioButton.isSelected())
            {sortByDate = true;}
        else
            {sortByDate = false;}
        if (sortByDate)
        {
            events.sort(Comparator.comparing(AppEvent::getStart));
        }
        else
        {
           events.sort(Comparator.comparingInt(AppEvent::getImportance).reversed());
        }
        for (int i = 0; i < events.size(); i++)
        {
            // Create event box
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            AppEvent event = events.get(i);
            Label eventLabel = new Label(event.getName());
            VBox dateAndTimeBox = new VBox(new Label(event.getStart().toLocalDate().toString()), new Label(event.getStart().toLocalTime().toString()));
            Circle circle = new Circle(24, event.getColor().isEmpty() ? Color.BLACK : Color.web(event.getColor()));
            HBox eventBox = new HBox(eventLabel, spacer, dateAndTimeBox, circle);
            eventBox.setSpacing(15);
            eventsVBox.getChildren().add(eventBox);
        }
        eventsVBox.requestLayout();
    }

    /**
     * Called when add event button is clicked
    */
    @FXML
    private void addEventButtonClicked() throws IOException
    {
        EventPlannerPopUpController eventController = (EventPlannerPopUpController) showPopUp("EventPlannerPopUp");
        eventController.setFields(this);
        popUpStage.setTitle("Add Event Pop-Up Menu");
    }

    /**
     * Called when add task button is clicked
    */
    @FXML
    private void addTaskButtonClicked() throws IOException
    {
        TaskPlannerPopUpController taskController = (TaskPlannerPopUpController) showPopUp("TaskPlannerPopUp");
        taskController.setFields(this);
        popUpStage.setTitle("Add Task Pop-Up Menu");
    }

    /**
     * Shows a pop-up window with the given FXML file.
     * @param fxmlPath the path to the FXML file without .fxml extension and starting slash
     * @return the controller of the pop-up window
     * @throws IOException if the FXML file cannot be loaded
     */
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

    public void addTask(Task task)
    {
        tasks.add(task);
    }

    public void addEvent(AppEvent event)
    {
        events.add(event);
    }

    public void closePopUp()
    {
        popUpStage.close();
    }

    public LocalDate getSelectedDate()
    {
        return datePicker.getValue();
    }

    /**
     * Called when date is changed by date picker
    */
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

    /**
     * Draws tasks for either a given date or the week of that date.
     * @param date the date to draw tasks for
     * @param daily whether to draw daily tasks (true) or weekly tasks (false)
     */
    private void drawTasksForDate(LocalDate date, boolean daily)
    {
        // Set visibility of components to false
        dailyTasksVBoxHBox.setVisible(false);
        dailyTasksVBoxHBox.setManaged(false);
        weeklyScrollPane.setVisible(false);
        weeklyScrollPane.setManaged(false);
        int completedTasks = 0;
        int totalTasks = 0;
        // Draw tasks for a given day
        if (daily)
        {
            // Clear previous daily tasks
            ((VBox)dailyTasksVBoxHBox.getChildren().get(0)).getChildren().clear();
            ((VBox)dailyTasksVBoxHBox.getChildren().get(1)).getChildren().clear();
            if (dateLabel.getScene() != null)
            {
                // Add stylesheet to scene
                Scene scene = dateLabel.getScene();
                scene.getStylesheets().add(getClass().getResource("/taskStyle.css").toExternalForm());
            }
            // Set visibility of daily tasks components to true
            dailyTasksVBoxHBox.setVisible(true);
            dailyTasksVBoxHBox.setManaged(true);
            // Draw each task for the given date
            for (int i = 0; i < tasks.size(); i++)
            {
                Task task = tasks.get(i);
                if (task.getDueDate().isEqual(date))
                {
                    Text taskLabel = new Text(task.getName());
                    taskLabel.getStyleClass().add("task-label");
                    if (task.getIsCompleted())
                        {completedTasks++;}
                    totalTasks++;
                    CheckBox taskCheckBox = new CheckBox("");
                    taskCheckBox.setSelected(task.getIsCompleted());
                    if (task.getIsCompleted())
                        {taskLabel.setStyle("-fx-strikethrough: true;");}
                    // Handle checkbox action for completing/uncompleting task
                    taskCheckBox.setOnAction(e -> {
                        if (taskCheckBox.isSelected())
                            {task.completeTask();}
                        else
                            {task.uncompleteTask();}
                        WebSocketClient.send(new ProtocolMessage(ActionType.UPDATE_TASK, task));     
                        updateDate();
                    });
                    // Handle importance color circle
                    Color importance = Color.GRAY;
                    if (task.getImportance() == 1) {importance = Color.LIGHTGREEN;}
                    else if (task.getImportance() == 2) {importance = Color.YELLOW;}
                    else if (task.getImportance() == 3) {importance = Color.ORANGE;}
                    else if (task.getImportance() == 4) {importance = Color.RED;}
                    Circle colorCircle = new Circle(8, importance);
                    // Create and format task box
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    HBox taskBox = new HBox(taskLabel, spacer, taskCheckBox, colorCircle);
                    taskBox.getStyleClass().add("task-pill");
                    taskBox.setAlignment(Pos.CENTER_LEFT);
                    taskBox.setSpacing(10);
                    taskBox.setMinHeight(40);
                    taskBox.setPrefHeight(40);
                    taskBox.setMaxWidth(Double.MAX_VALUE);
                    HBox.setHgrow(taskBox, Priority.ALWAYS);
                    VBox.setVgrow(taskBox, Priority.NEVER);
                    System.out.println(task.getColor());
                    taskBox.setPadding(new Insets(8, 14, 8, 14));
                    // Set aesthetic color of task box based on task color
                    String cssColor = toCssColor(task.getColor());
                    taskBox.setStyle(
                        "-fx-background-color: " + cssColor + ";" +
                        "-fx-background-radius: 20;"
                    );
                    // Add task box to left or right VBox based on index
                    if (i % 2 == 0)
                    {
                        ((VBox)dailyTasksVBoxHBox.getChildren().get(0)).getChildren().add(taskBox);
                        HBox.setHgrow((VBox)dailyTasksVBoxHBox.getChildren().get(0), Priority.ALWAYS);
                    }
                    else
                    {
                        ((VBox)dailyTasksVBoxHBox.getChildren().get(1)).getChildren().add(taskBox);
                        HBox.setHgrow((VBox)dailyTasksVBoxHBox.getChildren().get(1), Priority.ALWAYS);
                    }
                }
            }
            // Update done percentage text
            if (totalTasks > 0) {
                int percent = (int) (((double) completedTasks / totalTasks) * 100);
                donePercentTextField.setText("%" + percent);
            } else {
                donePercentTextField.setText("%0");
            }        
        }
        // Draw tasks for the week of a given date
        else
        {
            // Clear previous weekly tasks and set day labels
            LocalDate startOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
            for (int i = 0; i < weeklyTasksVBoxFlowPane.getChildren().size(); i++)
            {
                VBox dayBox = (VBox) weeklyTasksVBoxFlowPane.getChildren().get(i);
                dayBox.getChildren().clear();
                LocalDate currentDate = startOfWeek.plusDays(i);
                DayOfWeek day = currentDate.getDayOfWeek();
                Label dayLabel = new Label(day.toString());
                Label dateLabel = new Label(currentDate.toString());
                dayBox.getChildren().addAll(dayLabel, dateLabel);
            }
            // Set visibility of weekly tasks components to true
            weeklyScrollPane.setVisible(true);
            weeklyScrollPane.setManaged(true);
            // Draw each task for the given week
            for (int i = 0; i < tasks.size(); i++)
            {
                Task task = tasks.get(i);
                LocalDate taskDueDate = task.getDueDate();
                LocalDate endOfWeek = startOfWeek.plusDays(6);
                if ((taskDueDate.isEqual(startOfWeek) || taskDueDate.isAfter(startOfWeek)) &&
                    (taskDueDate.isEqual(endOfWeek) || taskDueDate.isBefore(endOfWeek)))
                {
                    if (task.getIsCompleted())
                        {completedTasks++;}
                    totalTasks++;
                    DayOfWeek dayOfWeek = taskDueDate.getDayOfWeek().minus(DayOfWeek.MONDAY.getValue());
                    Text label = new Text(task.getName());
                    if (task.getIsCompleted())
                        {label.setStyle("-fx-strikethrough: true;");}
                    ((VBox)weeklyTasksVBoxFlowPane.getChildren().get(dayOfWeek.getValue())).getChildren().add(label);
                }
            }
            // Update done percentage text
            donePercentTextField.setText("%" + (int)((double)completedTasks / totalTasks * 100));
        }
    }

    /**
     * Converts a color string in the ARGB format to RGBA format, so CSS can use it.
     * @param color ARGB color string starting with "0x"
     * @return CSS-compatible color string in RGBA format
     */
    private String toCssColor(String color) {
        if (color.startsWith("0x")) {
            String a = color.substring(2, 4);
            String r = color.substring(4, 6);
            String g = color.substring(6, 8);
            String b = color.substring(8, 10);

            int alpha = Integer.parseInt(a, 16);
            return String.format(
                "rgba(%d,%d,%d,%.2f)",
                Integer.parseInt(r, 16),
                Integer.parseInt(g, 16),
                Integer.parseInt(b, 16),
                alpha / 255.0
            );
        }
        return color;
    }
}
