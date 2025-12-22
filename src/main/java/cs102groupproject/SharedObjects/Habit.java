package cs102groupproject.SharedObjects;

import java.time.LocalDate;

public class Habit extends TransferObject{
    private String name;
    private LocalDate creationDate;
    private String completionString;
    private int id;
<<<<<<< HEAD
    private String userId;

    public Habit(String name, int id, String userId)
=======
    private int userId;

    public Habit(String name, int id, int userId)
>>>>>>> main
    {
        this.name = name;
        completionString = "000000000000000000000000000000";
        this.id = id;
        this.userId = userId;
        creationDate = LocalDate.now();
    }
    
    public Habit(String name, int userId)
    {
        this.name = name;
        completionString = "000000000000000000000000000000";
        id = -1 ; //indicates that the habit has not been assigned an ID yet
        this.userId = userId;
        creationDate = LocalDate.now();
    }

    public String getName() {return name;}
    public LocalDate getCreationDate() {return creationDate;}
    public String getCompletionString() {return completionString;}
    public int getId() {return id;}
<<<<<<< HEAD
    public String getUserId() {return userId;}
=======
    public int getUserId() {return userId;}
>>>>>>> main

    public int calculateCompletionCount()
    {
        int completedDays = 0;
        for (char completed : getCompletionString().toCharArray())
        {
            if (completed == '1') completedDays++;
        }
        return completedDays;
    }

    public void invertCompletedAtIndex(int index)
    {
        char[] completionChars = completionString.toCharArray();
        completionChars[index] = (completionChars[index] == '0') ? '1' : '0';
        completionString = new String(completionChars);
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
