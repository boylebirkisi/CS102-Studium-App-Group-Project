package cs102groupproject.Server;

import cs102groupproject.SharedObjects.Habit;

public class HabitService {

    private final DBManager db;

    
    public HabitService(DBManager db){
        this.db = db;
    }

    public Habit createHabit(Habit habit){
        return db.addHabit(habit.getName(), habit.getUserId());

    }

    public void removeHabit(Habit habit){
        db.removeHabit(habit.getId());
    }

    public void updateHabit(Habit habit) {
        //db.updateHabitCompletionString(habit.getId(),habit.getCompletionString(), habit.isExpired());
    }
    
}
