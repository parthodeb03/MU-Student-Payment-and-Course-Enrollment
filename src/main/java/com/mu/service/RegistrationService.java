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

        if (student.getDepartment() == null || student.getDepartment().trim().isEmpty()) {
            throw new IllegalArgumentException("Department is required.");
        }

        if (student.getBatch() == null || student.getBatch().trim().isEmpty()) {
            throw new IllegalArgumentException("Batch is required.");
        }

        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            throw new IllegalArgumentException("University ID is required.");
        }

        if (student.getPhone() == null || student.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required.");
        }

        if (studentDAO.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        if (studentDAO.existsByUniversityId(student.getStudentId())){
            throw new IllegalArgumentException("University ID is already registered.");
        }

        return studentDAO.register(student);
    }
}