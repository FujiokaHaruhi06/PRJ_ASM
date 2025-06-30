package auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtil {

    /**
     * Hashes a password using the SHA-256 algorithm.
     * @param password The plain text password.
     * @return The hashed password, encoded as a Base64 string.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            // This should never happen with a standard algorithm like SHA-256
            throw new RuntimeException("Could not find SHA-256 algorithm", e);
        }
    }

    /**
     * Verifies a plain text password against a hashed one.
     * @param plainPassword The password entered by the user.
     * @param hashedPassword The hashed password from the database.
     * @return true if the passwords match, false otherwise.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        String hashedPlainPassword = hashPassword(plainPassword);
        return hashedPlainPassword.equals(hashedPassword);
    }
} 