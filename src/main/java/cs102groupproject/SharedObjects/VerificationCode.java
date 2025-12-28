package cs102groupproject.SharedObjects;
/**
 * Class representing a verification code sent to a user's email.
 */
public class VerificationCode extends TransferObject{
    private String storedCode;
    private String email;
    private long expiryTime;

    public VerificationCode(String code, String email, long expiryTime) {
        this.storedCode = code;
        this.email = email;
        this.expiryTime = expiryTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expiryTime;
    }

    public String email() {
        return email;
    }

    public String getStoredCode() {
        return storedCode;
    }

    public long getExpiryTime() {
        return expiryTime;
    }
}
