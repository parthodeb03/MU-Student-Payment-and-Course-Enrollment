package com.mu.ui;

import com.mu.model.Admin;
import com.mu.service.AdminService;

import javax.swing.*;
import java.awt.*;

public class AdminRegistrationFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private JButton btnRegister;
    private JButton btnBack;

    private AdminService adminService;

    public AdminRegistrationFrame() {

        adminService = new AdminService();

        setTitle("Admin Registration");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitle = new JLabel("Create Admin Account");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        add(lblTitle, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;

        add(new JLabel("Username:"), gbc);

        txtUsername = new JTextField(18);

        gbc.gridx = 1;

        add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        add(new JLabel("Password:"), gbc);

        txtPassword = new JPasswordField(18);

        gbc.gridx = 1;

        add(txtPassword, gbc);

        btnRegister = new JButton("Register");
        btnBack = new JButton("Back");

        JPanel panel = new JPanel();

        panel.add(btnRegister);
        panel.add(btnBack);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        add(panel, gbc);

        btnRegister.addActionListener(e -> registerAdmin());

        btnBack.addActionListener(e -> {

            new LoginFrame();

            dispose();

        });

        setVisible(true);

    }

    private void registerAdmin() {

        try {

            String username = txtUsername.getText().trim();
            String password = String.valueOf(txtPassword.getPassword());

            if (username.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Username cannot be empty."
                );

                return;

            }

            if (password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Password cannot be empty."
                );

                return;

            }

            Admin admin = new Admin();

            admin.setUsername(username);
            admin.setPassword(password);

            boolean success = adminService.register(admin);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Admin Account Created Successfully."
                );

                new AdminLoginFrame();

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Registration Failed."
                );

            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()
            );

        }

    }

}