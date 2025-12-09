package com.cmed.medic.auth.validation;

import com.cmed.medic.utils.ValidationResult;

public class UserValidation {

    public static ValidationResult validateUsername(String username) {

        if (username == null || username.isBlank()) {
            return new ValidationResult(false, "Username cannot be empty");
        }

        int len = username.length();
        if (len < 3) {
            return new ValidationResult(false, "Username must be at least 3 characters");
        }

        if (len > 15) {
            return new ValidationResult(false, "Username cannot exceed 15 characters");
        }

        boolean hasLetter = false;
        for (char c : username.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
                break;
            }
        }

        if (!hasLetter) {
            return new ValidationResult(false, "Username must contain at least one letter");
        }

        return new ValidationResult(true, null);
    }

    public static ValidationResult validatePassword(String password) {

        if (password == null || password.isBlank()) {
            return new ValidationResult(false, "Password cannot be empty");
        }

        int len = password.length();
        if (len < 6) {
            return new ValidationResult(false, "Password must be at least 6 characters");
        }

        if (len > 20) {
            return new ValidationResult(false, "Password cannot exceed 20 characters");
        }

        return new ValidationResult(true, null);
    }
}
