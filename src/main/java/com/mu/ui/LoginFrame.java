package com.mu.ui;

import com.mu.model.Student;
import com.mu.service.LoginService;
import com.mu.util.Session;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;

    private JButton btnLogin;
    private JButton btnRegister;
    private JButton btnAdmin;
    private JButton btnAdminRegister;

    private LoginService loginService;

    public LoginFrame() {

        loginService = new LoginService();

        setTitle("Metropolitan University Login");

        setSize(450, 380);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitle = new JLabel("Student Login");

        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        add(lblTitle, gbc);

        gbc.gridwidth = 1;

        // Email
        gbc.gridy++;

        add(new JLabel("Email :"), gbc);

        txtEmail = new JTextField(18);

        gbc.gridx = 1;

        add(txtEmail, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy++;

        add(new JLabel("Password :"), gbc);

        txtPassword = new JPasswordField(18);

        gbc.gridx = 1;

        add(txtPassword, gbc);

        // Buttons
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
        btnAdmin = new JButton("Admin Login");
        btnAdminRegister = new JButton("Admin Register");

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        panel.add(btnLogin);
        panel.add(btnRegister);
        panel.add(btnAdmin);
        panel.add(btnAdminRegister);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        add(panel, gbc);

        // Student Login
        btnLogin.addActionListener(e -> login());

        // Student Register
        btnRegister.addActionListener(e -> {

            new RegisterFrame();

            dispose();

        });

        // Admin Login
        btnAdmin.addActionListener(e -> {

            new AdminLoginFrame();

            dispose();

        });

        // Admin Register
        btnAdminRegister.addActionListener(e -> {

            new AdminRegistrationFrame();

            dispose();

        });

        setVisible(true);

    }

    private void login() {

        try {

            String email = txtEmail.getText().trim();

            String password = String.valueOf(txtPassword.getPassword());

            Student student = loginService.login(email, password);

            if (student != null) {

                Session.setCurrentStudent(student);

                JOptionPane.showMessageDialog(
                        this,
                        "Login Successful"
                );

                new DashboardFrame();

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Email or Password"
                );

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );

        }

    }

}