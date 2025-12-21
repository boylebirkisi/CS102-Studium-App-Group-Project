package cs102groupproject.SharedObjects;

public class User {
    private String id;
    private String username;
    private String department;
    private UserCredentials credentials;
    private int soloCurrency;
    private int groupCurrency;
    private boolean isVerified;
    private String avatar;

    public User(String id, String username, String department, UserCredentials credentials, int soloCurrency, int groupCurrency, boolean isVerified, String avatar) {
        this.id = id;
        this.username = username;
        this.department = department;
        this.credentials = credentials;
        this.soloCurrency = soloCurrency;
        this.groupCurrency = groupCurrency;
        this.isVerified = isVerified;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getDepartment() {
        return department;
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