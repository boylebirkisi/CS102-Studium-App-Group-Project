package cs102groupproject.Client;

public class PlannerController {
    ClientSession manager;
    ArrayList<Habit> habits;
    ArrayList<AppEvent> events;
    ArrayList<Task> tasks;
    Habit selectedHabit;
    LocalDateTime selectedDate;
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

    public PlannerController(SessionManager manager) {
        this.manager = manager;
    }

    
}