package gov_service_api.security;

import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;

public class PasswordUtil {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    private PasswordUtil() {}
}
