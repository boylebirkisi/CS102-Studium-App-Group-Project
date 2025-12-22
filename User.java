package cs102groupproject.SharedObjects;

import java.util.ArrayList;

public class User {
    private int id;
    private String username;
    private String department;
    private UserCredentials credentials;
    private int soloCurrency;
    private int groupCurrency;
    private int individualSessionsCompleted;
    private int groupSessionsCompleted;
    private int totalMinutesSpent;
    private ArrayList<Habit> habitsCompleted;
    private boolean isVerified;
    private String avatar;

    public User(int id, String username, String department, UserCredentials credentials, int soloCurrency, int groupCurrency, boolean isVerified, String avatar) {
        this.id = id;
        this.username = username;
        this.department = department;
        this.credentials = credentials;
        this.soloCurrency = soloCurrency;
        this.groupCurrency = groupCurrency;
        this.isVerified = isVerified;
        this.avatar = avatar;
        individualSessionsCompleted = 0;
        groupSessionsCompleted = 0;
        totalMinutesSpent = 0;
        habitsCompleted = new ArrayList<>();
    }

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public UserCredentials getCredentials() {
        return credentials;
    }
    public int getSoloCurrency() {
        return soloCurrency;
    }
    public void setSoloCurrency(int soloCurrency) {
        this.soloCurrency = soloCurrency;
    }
    public int getGroupCurrency() {
        return groupCurrency;
    }
    public void setGroupCurrency(int groupCurrency) {
        this.groupCurrency = groupCurrency;
    }
    public int getIndividualSessionsCompleted() {
        return individualSessionsCompleted;
    }
    public void incrementIndividualSessionsCompleted() {
        this.individualSessionsCompleted++;
    }
    public int getGroupSessionsCompleted() {
        return groupSessionsCompleted;
    }
    public void incrementGroupSessionsCompleted() {
        this.groupSessionsCompleted++;
    }
    public int getTotalMinutesSpent() {
        return totalMinutesSpent;
    }
    public void addMinutesSpent(int minutes) {
        this.totalMinutesSpent += minutes;
    }
    public ArrayList<Habit> getHabitsCompleted() {
        return habitsCompleted;
    }
    public void addHabitCompleted(Habit habit) {
        this.habitsCompleted.add(habit);
    }
    public boolean isVerified() {
        return isVerified;
    }
    public String getAvatar() {
        return avatar;
    }

    /**
     * Sets isVerified to true if it is false.
     */
    public void verifyUser()
    {
        if (!isVerified)
            {isVerified = true;}
    }

    /**
     * Updates the user’s money, adds to solo or group currency depending on the session type.
     */
    public void addCurrency(int amount, boolean isGroupSession)
    {
        if (isGroupSession)
            {groupCurrency += amount;}
        else
            {soloCurrency += amount;}
    }
}