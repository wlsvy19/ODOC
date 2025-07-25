package gdtcs.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {

    public static String encryptSHA256(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input string must not be null or empty");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                hexString.append(String.format("%02x", b)); // 2자리 16진수로 변환
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error initializing SHA-256 algorithm", e);
        }
    }
}