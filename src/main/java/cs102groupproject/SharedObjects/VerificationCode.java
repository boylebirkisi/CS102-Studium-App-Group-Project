package cs102groupproject.SharedObjects;

public class VerificationCode {
    private String storedCode;
    private String email;
    private long expiryTime;

    public VerificationCode(String code, String email, long expiryTime) {
        this.storedCode = code;
        this.email = email;
        this.expiryTime = expiryTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() <= expiryTime;
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
