package com.mu.service;

import com.mu.dao.StudentDAO;
import com.mu.model.Student;

public class RegistrationService {

    private final StudentDAO studentDAO;

    public RegistrationService() {
        studentDAO = new StudentDAO();
    }

    public boolean register(Student student) {

        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }

        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if (!student.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (student.getPassword() == null || student.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must contain at least 6 characters.");
        }

        return studentDAO.register(student);
    }
}