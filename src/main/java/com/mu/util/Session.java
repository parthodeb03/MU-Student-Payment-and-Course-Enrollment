package com.mu.util;

import com.mu.model.Student;

public class Session {

    private static Student currentStudent;

    private Session() {
        // Prevent object creation
    }

    // Login
    public static void setCurrentStudent(Student student) {
        currentStudent = student;
    }

    // Get Logged-in Student
    public static Student getCurrentStudent() {
        return currentStudent;
    }

    // Logout
    public static void clearSession() {
        currentStudent = null;
    }

    // Check Login
    public static boolean isLoggedIn() {
        return currentStudent != null;
    }
}