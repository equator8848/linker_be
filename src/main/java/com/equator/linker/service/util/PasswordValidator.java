package com.equator.linker.service.util;

import com.equator.core.util.security.PasswordUtil;

public class PasswordValidator {
    public static boolean verifyPassword(String userInput, String passwordInDatabase) {
        String salt = PasswordUtil.parseSaltFromEncryptedPassword(passwordInDatabase);
        String encryptedPassword = PasswordUtil.generateSha512CryptPassword(userInput, salt);
        return encryptedPassword.equals(passwordInDatabase);
    }
}
