package com.mu.service;

import com.mu.dao.AdminDAO;
import com.mu.model.Admin;
import com.mu.model.Payment;
import com.mu.model.Student;

import java.util.List;

public class AdminService {

    private AdminDAO adminDAO;

    public AdminService() {
        adminDAO = com.mu.factory.DAOFactory.createAdminDAO();
    }

    public AdminService(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    public Admin login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        return adminDAO.login(username, password);
    }

<<<<<<< HEAD
    public boolean register(Admin admin) {

        if (admin == null) {
            throw new IllegalArgumentException("Admin object cannot be null.");
        }

        if (admin.getUsername() == null || admin.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if (admin.getPassword() == null || admin.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (admin.getPassword().trim().length() < 4) {
            throw new IllegalArgumentException("Password must contain at least 4 characters.");
        }

        if (adminDAO.existsByUsername(admin.getUsername().trim())) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        return adminDAO.register(admin);
    }

=======
>>>>>>> 0811db3 (database changed)
    public int getTotalStudents() {
        return adminDAO.getTotalStudents();
    }

    public double getTotalPayments() {
        return adminDAO.getTotalPayments();
    }

    public List<Student> getAllStudents() {
        return adminDAO.getAllStudents();
    }

    public List<Payment> getAllPayments() {
        return adminDAO.getAllPayments();
    }

    public List<Payment> getPendingPayments() {
        return adminDAO.getPendingPayments();
    }

    public boolean approvePayment(int paymentId) {
        if (paymentId <= 0) throw new IllegalArgumentException("Invalid payment ID.");
        return adminDAO.approvePayment(paymentId);
    }
}