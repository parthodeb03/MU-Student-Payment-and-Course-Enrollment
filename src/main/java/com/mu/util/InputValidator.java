package com.mu.util;

public final class InputValidator {

    private InputValidator() {
    }

    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty())
            return false;
        String trimmed = name.trim();
        return trimmed.length() >= 2
                && trimmed.length() <= 100
                && trimmed.matches("[A-Za-z ]+");
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty())
            return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public static boolean isValidPassword(String password) {
        if (password == null)
            return false;
        return password.length() >= 6 && password.length() <= 50;
    }

    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty())
            return false;
        String u = username.trim();
        return u.length() >= 4
                && u.length() <= 30
                && u.matches("[A-Za-z0-9_]+");
    }

    public static boolean isValidPaymentAmount(double amount) {
        return amount > 0 && amount <= 100_000;
    }

    public static boolean isValidStudentId(int id) {
        return id > 0;
    }
}
