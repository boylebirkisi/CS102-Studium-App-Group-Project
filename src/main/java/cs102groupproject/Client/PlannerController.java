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
import javafx.scene.Node;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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

    private void updateHabitTracker(Habit habit)
    {
        clearHabitTrackerCircles();
        if (habit == null) return;
        for (int i = 0; i < habitCircleFlowPane.getChildren().size(); i++) {
            CheckBox cb = (CheckBox) habitCircleFlowPane.getChildren().get(i);
            final int index = i; // Lambda içinde kullanmak için final olmalı
            cb.setOnAction(null);
            cb.setSelected(habit.getCompletionString().charAt(i) == '1');

            // clicking
            cb.setOnAction(e -> { // 2. Şimdi tertemiz yeni aksiyonu ata
                        habit.invertCompletedAtIndex(index); 
                        WebSocketClient.send(new ProtocolMessage(ActionType.UPDATE_HABIT, habit));
                        System.out.println("Habit güncellendi: " + habit.getCompletionString());
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
        habitTrackerComboBox.setValue(newHabit);
        selectedHabit = newHabit;
        habitTrackerComboBox.setDisable(false);
        habitTrackerComboBox.setVisible(true);
        addHabitTextField.setVisible(false);
        addHabitTextField.setDisable(true);
        addHabitTextField.clear();
        updateHabitTracker(newHabit);
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
            if (dateLabel.getScene() != null)
            {
                Scene scene = dateLabel.getScene();
                scene.getStylesheets().add(getClass().getResource("/taskStyle.css").toExternalForm());
            }
            dailyTasksVBoxHBox.setVisible(true);
            dailyTasksVBoxHBox.setManaged(true);
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
                    taskCheckBox.setOnAction(e -> {
                        if (taskCheckBox.isSelected())
                            {task.completeTask();}
                        else
                            {task.uncompleteTask();}
                        WebSocketClient.send(new ProtocolMessage(ActionType.UPDATE_TASK, task));     
                        updateDate();
                    });
                    Color importance = Color.GRAY;
                    if (task.getImportance() == 1) {importance = Color.LIGHTGREEN;}
                    else if (task.getImportance() == 2) {importance = Color.YELLOW;}
                    else if (task.getImportance() == 3) {importance = Color.ORANGE;}
                    else if (task.getImportance() == 4) {importance = Color.RED;}
                    Circle colorCircle = new Circle(8, importance);
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
                    String colorString = task.getColor();
                    Color bgColor = Color.web("0x" + colorString.substring(2));
                    taskBox.setBackground(
                        new Background(
                            new BackgroundFill(
                                bgColor,
                                new CornerRadii(20),
                                Insets.EMPTY
                            )
                        )
                    );
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
            if (totalTasks > 0) {
                int percent = (int) (((double) completedTasks / totalTasks) * 100);
                donePercentTextField.setText("%" + percent);
            } else {
                donePercentTextField.setText("%0");
            }        
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
                    Text label = new Text(task.getName());
                    if (task.getIsCompleted())
                        {label.setStyle("-fx-strikethrough: true;");}
                    ((VBox)weeklyTasksVBoxFlowPane.getChildren().get(dayOfWeek.getValue())).getChildren().add(label);
                }
            }
            donePercentTextField.setText("%" + (int)((double)completedTasks / totalTasks * 100));
        }
    }
}
