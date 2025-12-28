package cs102groupproject.Server;

import cs102groupproject.SharedObjects.Habit;
/**
 * Performs server-side processing for habit-related operations
 * and provides database interactions.
 * @author Begüm Göktaş
 * Date: 27/12/2025
 */
public class HabitService {

    private final DBManager db;

    public HabitService(DBManager db){
        this.db = db;
    }

    /**
     * saves the given habit to the database
     * @param habit
     * @return saved habit
     */
    public Habit createHabit(Habit habit){
        return db.addHabit(habit.getName(), habit.getUserId());

    }
    
    /**
     * removes the given habit from the database
     * @param habit
     */
    public void removeHabit(Habit habit){
        db.removeHabit(habit.getId());
    }

    /**
     * updates the given habit
     * @param habit
     */
    public void updateHabit(Habit habit) {
        db.updateHabitCompletionString(habit.getId(),habit.getCompletionString(), false);
    }
    
}
