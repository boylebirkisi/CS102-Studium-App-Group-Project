package cs102groupproject.SharedObjects;

import java.time.LocalDate;
import java.util.ArrayList;

public class DayCell {
    private ArrayList<Task> weeklyTasks;
    private ArrayList<Task> dailyTasks;
    private ArrayList<AppEvent> events;
    private LocalDate date;

    public DayCell(LocalDate date) {
        this.date = date;
        weeklyTasks = new ArrayList<>();
        dailyTasks = new ArrayList<>();
        events = new ArrayList<>();
    }

    public ArrayList<Task> getWeeklyTasks() {return weeklyTasks;}
    public ArrayList<Task> getDailyTasks() {return dailyTasks;}
    public ArrayList<AppEvent> getEvents() {return events;}
    public LocalDate getDate() {return date;}
    public void addWeeklyTask(Task task) {weeklyTasks.add(task);}
    public void addDailyTask(Task task) {dailyTasks.add(task);}
    public void addEvent(AppEvent event) {events.add(event);}
}
