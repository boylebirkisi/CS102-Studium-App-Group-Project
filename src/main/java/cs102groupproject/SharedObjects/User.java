package cs102groupproject.SharedObjects;

import java.util.ArrayList;

public class User {
    private String id;
    private String username;
    private String department;
    private String email;
    private String googleID;
    private int soloCurrency;
    private int groupCurrency;
    private int individualSessionsCompleted;
    private int groupSessionsCompleted;
    private int totalMinutesSpent;
    private ArrayList<Habit> habitsCompleted;
    private boolean isVerified;
    private String avatar;

    /**
     * Constructor for existing users.
     * @param id
     * @param username
     * @param department
     * @param email
     * @param googleID
     * @param soloCurrency
     * @param groupCurrency
     * @param isVerified
     * @param avatar
     * @param individualSessionsCompleted
     * @param groupSessionsCompleted
     * @param totalMinutesSpent
     * @param habitsCompleted
     */
    public User(String id, String username, String department, String email, String googleID, int soloCurrency, int groupCurrency, boolean isVerified, String avatar, int individualSessionsCompleted, int groupSessionsCompleted, int totalMinutesSpent, ArrayList<Habit> habitsCompleted) {
        this.id = id;
        this.username = username;
        this.department = department;
        this.email = email;
        this.googleID = googleID;
        this.soloCurrency = soloCurrency;
        this.groupCurrency = groupCurrency;
        this.isVerified = isVerified;
        this.avatar = avatar;
        this.individualSessionsCompleted = individualSessionsCompleted;
        this.groupSessionsCompleted = groupSessionsCompleted;
        this.totalMinutesSpent = totalMinutesSpent;
        this.habitsCompleted = habitsCompleted;
    }

    /**
     * Constructor for new users.
     * @param id
     * @param username
     * @param department
     * @param email
     * @param googleID
     * @param soloCurrency
     * @param groupCurrency
     * @param isVerified
     * @param avatar
     */
    public User(String id, String username, String department, String email, String googleID, int soloCurrency, int groupCurrency, boolean isVerified, String avatar) {
        this.id = id;
        this.username = username;
        this.department = department;
        this.email = email;
        this.googleID = googleID;
        this.soloCurrency = soloCurrency;
        this.groupCurrency = groupCurrency;
        this.isVerified = isVerified;
        this.avatar = avatar;
        individualSessionsCompleted = 0;
        groupSessionsCompleted = 0;
        totalMinutesSpent = 0;
        habitsCompleted = new ArrayList<>();
    }

    public String getId() {
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
    public String getGoogleID() {
        return googleID;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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