package cs102groupproject.SharedObjects;

import java.time.LocalDate;

public class Habit extends TransferObject{
    private String name;
    private LocalDate creationDate;
    private boolean[] completedArr;
    private String id;
    private String userId;

    public Habit(String name, String id, String userId)
    {
        this.name = name;
        completedArr = new boolean[30];
        this.id = id;
        this.userId = userId;
        creationDate = LocalDate.now();
    }

    public String getName() {return name;}
    public LocalDate getCreationDate() {return creationDate;}
    public boolean[] getCompletedArr() {return completedArr;}
    public String getId() {return id;}
    public String getUserId() {return userId;}

    public int calculateCompletionCount()
    {
        int completedDays = 0;
        for (boolean completed : getCompletedArr())
        {
            if (completed) completedDays++;
        }
        return completedDays;
    }

    public void invertCompletedAtIndex(int index)
    {
        completedArr[index] = !completedArr[index];
    }

    public void updateHabitAtDate(LocalDate date)
    {
        int daysBetween = (int)java.time.temporal.ChronoUnit.DAYS.between(creationDate, date);
        if (date.equals(LocalDate.now()))
        {
            invertCompletedAtIndex(daysBetween);
        }
    }
}
