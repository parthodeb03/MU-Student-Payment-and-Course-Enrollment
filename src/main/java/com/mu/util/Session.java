package com.mu.util;

import com.mu.model.Student;

public class Session {

    private static Student currentStudent;

    private Session() {

    }

    public static void setCurrentStudent(Student student) {
        currentStudent = student;
    }

    public static Student getCurrentStudent() {
        return currentStudent;
    }

    public static void clearSession() {
        currentStudent = null;
    }

    public static boolean isLoggedIn() {
        return currentStudent != null;
    }
}