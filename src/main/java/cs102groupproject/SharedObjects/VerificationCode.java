package cs102groupproject.SharedObjects;

public class VerificationCode {
    private String storedCode;
    private String userID;
    private long expiryTime;

    public VerificationCode(String code, String userID, long expiry) {
        this.storedCode = code;
        this.userID = userID;
        this.expiryTime = expiryTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() <= expiryTime;
    }

    public String getUserID() {
        return userID;
    }

    public String getStoredCode() {
        return storedCode;
    }
}
