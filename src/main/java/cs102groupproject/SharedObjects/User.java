package cs102groupproject.SharedObjects;
/**
 * Class representing a user in the application.
 */
public class User {
    private int id;
    private String username;
    private String department;
    private String email;
    private String googleID;
    private int soloCurrency;
    private int groupCurrency;
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
     */
    public User(int id, String username, String department, String email, String googleID, int soloCurrency, int groupCurrency, boolean isVerified, String avatar) {
        this.id = id;
        this.username = username;
        this.department = department;
        this.email = email;
        this.googleID = googleID;
        this.soloCurrency = soloCurrency;
        this.groupCurrency = groupCurrency;
        this.isVerified = isVerified;
        this.avatar = avatar;
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
    public User(int id, String username, String department, String email, String googleID, boolean isVerified, String avatar) {
        this.id = id;
        this.username = username;
        this.department = department;
        this.email = email;
        this.googleID = googleID;
        soloCurrency = 0;
        groupCurrency = 0;
        this.isVerified = isVerified;
        this.avatar = avatar;
    }

    /**
     * Constructor for new users that doesn't have an ID yet.
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
    public User(String username, String department, String email, String googleID, int soloCurrency, int groupCurrency, boolean isVerified, String avatar) {
        id = -1; //indicates that the user has not been assigned an ID yet
        this.username = username;
        this.department = department;
        this.email = email;
        this.googleID = googleID;
        this.soloCurrency = soloCurrency;
        this.groupCurrency = groupCurrency;
        this.isVerified = isVerified;
        this.avatar = avatar;
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