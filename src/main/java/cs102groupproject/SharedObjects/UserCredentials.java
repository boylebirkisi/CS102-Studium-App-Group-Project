package cs102groupproject.SharedObjects;

public class UserCredentials {
    private String emailOrUsername;
    private String password;
    private boolean isEmail;

    public UserCredentials(String emailOrUsername, String password, boolean isEmail) {
        this.emailOrUsername = emailOrUsername;
        this.password = password;
        this.isEmail = isEmail;
    }

    public String getEmailOrUsername() {
        return emailOrUsername;
    }
    public void setEmailOrUsername(String emailOrUsername) {
        this.emailOrUsername = emailOrUsername;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmail() {
        return isEmail;
    }
}