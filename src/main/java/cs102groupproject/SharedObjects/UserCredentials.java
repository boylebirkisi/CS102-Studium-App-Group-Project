package cs102groupproject.SharedObjects;

public class UserCredentials {
    private String username;
    private String email;
    private String accessToken;
    private String password;
    private boolean isEmail;
    private String department;

    public UserCredentials(String username, String email, String accessToken, String password, boolean isEmail, String department) {
        this.username = username;
        this.email = email;
        this.accessToken = accessToken;
        this.password = password;
        this.isEmail = isEmail;
        this.department = department;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String emailOrUsername) {
        this.username = emailOrUsername;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String token) {
        this.accessToken = token;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
}