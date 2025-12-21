package cs102groupproject.SharedObjects;

public class UserCredentials {
    private UserCredentials email;
    private UserCredentials password;

    public UserCredentials(UserCredentials email, UserCredentials password) {
        this.email = email;
        this.password = password;
    }

    public UserCredentials getEmail() {
        return email;
    }
    public void setEmail(UserCredentials email) {
        this.email = email;
    }
    public UserCredentials getPassword() {
        return password;
    }
    public void setPassword(UserCredentials password) {
        this.password = password;
    }
}