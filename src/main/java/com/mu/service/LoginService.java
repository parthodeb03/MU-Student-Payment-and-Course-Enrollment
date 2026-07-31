package com.mu.service;

import com.mu.dao.StudentDAO;
import com.mu.model.Student;

public class LoginService {

    private final StudentDAO studentDAO;

    public LoginService() {
        studentDAO = new StudentDAO();
    }

    public Student login(String email, String password) {

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        return studentDAO.login(email, password);
    }
}