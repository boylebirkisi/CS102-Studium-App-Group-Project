package cs102groupproject.SharedObjects;

import cs102groupproject.UserCredentials;

public class User {
    protected String ID;
    protected String username;
    protected UserCredentials credentials;
    protected int soloCurrency;
    protected int groupCurrency;
    protected boolean isVerified;
    protected int avatar;

    public User (String ID, String username, UserCredentials credentials, int avatar)
    {
        this.ID = ID;
        this.username = username;
        this.credentials = credentials;
        this.avatar = avatar;
        soloCurrency = 0;
        groupCurrency = 0;
        isVerified = false;
    }

    public String getID() {return ID;}
    public void setUsername(String username) {this.username = username;}
    public String getUsername() {return username;}
    public void setCredentials(UserCredentials credentials) {this.credentials = credentials;}
    public UserCredentials getCredentials() {return credentials;}
    public void modifySoloCurrency(int modifyAmount) {soloCurrency = Math.max(0, soloCurrency + modifyAmount);}
    public int getSoloCurrency() {return soloCurrency;}
    public void modifyGroupCurrency(int modifyAmount) {groupCurrency = Math.max(0, groupCurrency + modifyAmount);}
    public int getGroupCurrency() {return groupCurrency;}
    public boolean getIsVerified() {return isVerified;}
    public int getAvatar() {return avatar;}
    public void setAvatar(int newAvatar) {avatar = newAvatar;}

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
