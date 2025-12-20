package cs102groupproject;

public class UserCredentials {
    protected String email;
    protected String enteredPassword;

    public UserCredentials(String email, String enteredPassword)
    {
        this.email = email;
        this.enteredPassword = enteredPassword;
    }

    public String getEmail() {return email;}
    public void setEmail(String newEmail) {email = newEmail;}
    public String getPassword() {return enteredPassword;}
    public void setPassword(String newEnteredPassword) {enteredPassword = newEnteredPassword;}
}