package cs102groupproject.SharedObjects;

public class User {
    private String id;
    private String username;
    private String googleID;
    private String email;
    private int soloCurrency;
    private int groupCurrency;
    private boolean isVerified;
    private String avatar;

    public User(String id, String username, String email, String googleID,int soloCurrency, int groupCurrency, boolean isVerified, String avatar) {
        this.id = id;
        this.username = username;
        this.googleID = googleID;
        this.email = email;
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
    public String getGoogleID() {
        return googleID;
    }
    public String getEmail() {
        return email;
    }
    public int getSoloCurrency() {
        return soloCurrency;
    }
    public int getGroupCurrency() {
        return groupCurrency;
    }
    public boolean isVerified() {
        return isVerified;
    }
    public String getAvatar() {
        return avatar;
    }
}
