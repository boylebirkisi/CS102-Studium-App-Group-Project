package cs102groupproject.Server;

import at.favre.lib.crypto.bcrypt.BCrypt;
/**
 * PasswordHasher handles the hashing procceses for the app to be more secure in terms of hiding the password indormation.
 * Author: Delfin Eryılmaz
 * Date: 27/12/2025
 */
public class PasswordHasher {
    private final int SALT_COUNTS = 12;

    /**
     * Hashes the password given.
     * @param plainText
     * @return
     */
    public String hashPassword(String plainText) {
        if (!plainText.isEmpty()) {
            String hashedPassword = BCrypt.withDefaults().hashToString(SALT_COUNTS, plainText.toCharArray());
            return hashedPassword;
        }
        return null;
    }

    /**
     * Checks whether the plain text matched with the hashed password or not.
     * @param plainText
     * @param hashedPassword
     * @return
     */
    public boolean checkPassword(String plainText, String hashedPassword) {
        if (plainText.isEmpty() || hashedPassword.isEmpty()) {
            return false;
        }
        
        BCrypt.Result result = BCrypt.verifyer().verify(plainText.toCharArray(), hashedPassword);
        return result.verified;
    }
}
