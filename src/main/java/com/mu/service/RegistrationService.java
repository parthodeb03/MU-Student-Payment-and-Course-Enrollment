package com.mu.service;

import com.mu.dao.StudentDAO;
import com.mu.model.Student;
import com.mu.util.InputValidator;

public class RegistrationService {

    private final StudentDAO studentDAO;

    public RegistrationService() {
        this.studentDAO = com.mu.factory.DAOFactory.createStudentDAO();
    }

    public RegistrationService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public boolean register(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student object is required.");
        }

        if (!InputValidator.isValidName(student.getName())) {
            throw new IllegalArgumentException("Invalid student name.");
        }

        if (!InputValidator.isValidEmail(student.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (!InputValidator.isValidPassword(student.getPassword())) {
            throw new IllegalArgumentException("Password must contain 6 to 50 characters.");
        }

        return studentDAO.register(student);
    }
}