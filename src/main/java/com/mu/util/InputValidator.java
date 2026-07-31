package com.mu.util;

/**
 * InputValidator – provides pure validation logic for student and admin inputs.
 *
 * This class is intentionally free of any dependencies so it is easy to unit-test
 * using Boundary Value Analysis, Equivalence Partitioning, Decision Table, and
 * Cause-Effect Graphing (Blackbox Techniques).
 */
public final class InputValidator {

    private InputValidator() { }

    // -------------------------------------------------------
    // Student Name
    // -------------------------------------------------------
    /**
     * Valid: 2-100 chars, letters+spaces only.
     * BVA boundaries: 1 (invalid), 2 (valid), 100 (valid), 101 (invalid).
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        String trimmed = name.trim();
        return trimmed.length() >= 2
                && trimmed.length() <= 100
                && trimmed.matches("[A-Za-z ]+");
    }

    // -------------------------------------------------------
    // Email
    // -------------------------------------------------------
    /**
     * Valid: must contain exactly one '@' and at least one dot after it.
     * Equivalence Partitions: valid email | no '@' | no domain | null
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    // -------------------------------------------------------
    // Password strength
    // -------------------------------------------------------
    /**
     * Valid: 6-50 chars.
     * BVA: 5 (invalid), 6 (valid), 50 (valid), 51 (invalid).
     */
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return password.length() >= 6 && password.length() <= 50;
    }

    // -------------------------------------------------------
    // Admin username
    // -------------------------------------------------------
    /**
     * Valid: 4-30 chars, alphanumeric + underscore.
     * BVA: 3 (invalid), 4 (valid), 30 (valid), 31 (invalid).
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        String u = username.trim();
        return u.length() >= 4
                && u.length() <= 30
                && u.matches("[A-Za-z0-9_]+");
    }

    // -------------------------------------------------------
    // Payment amount
    // -------------------------------------------------------
    /**
     * Valid: 0 < amount <= 100000.
     * BVA: 0 (invalid), 0.01 (valid), 100000 (valid), 100000.01 (invalid).
     */
    public static boolean isValidPaymentAmount(double amount) {
        return amount > 0 && amount <= 100_000;
    }

    // -------------------------------------------------------
    // Student ID
    // -------------------------------------------------------
    public static boolean isValidStudentId(int id) {
        return id > 0;
    }
}
