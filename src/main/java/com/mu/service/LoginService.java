package com.mu.service;

import com.mu.dao.StudentDAO;
import com.mu.model.Student;
import com.mu.util.InputValidator;

public class LoginService {

    private final StudentDAO studentDAO;

    public LoginService() {
        studentDAO = com.mu.factory.DAOFactory.createStudentDAO();
    }

    public Student login(String email, String password) {

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (!InputValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        return studentDAO.login(email, password);
    }
}
