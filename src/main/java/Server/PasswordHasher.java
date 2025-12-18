package Server;

import at.favre.lib.crypto.bcrypt.BCrypt;
/**
 * Provides methods to hash and verify passwords using BCrypt library.
 * Author: Delfin Eryılmaz
 * Date: 18.12.2025
 */
public class PasswordHasher {
    private final int SALT_COUNTS = 12;

    public String hashPassword(String plainText) {
        if (!plainText.isEmpty()) {
            String hashedPassword = BCrypt.withDefaults().hashToString(SALT_COUNTS, plainText.toCharArray());
            return hashedPassword;
        }
        return null;
    }

    public boolean checkPassword(String plainText, String hashedPassword) {
        if (plainText.isEmpty() || hashedPassword.isEmpty()) {
            return false;
        }
        
        BCrypt.Result result = BCrypt.verifyer().verify(plainText.toCharArray(), hashedPassword);
        return result.verified;
    }
}
