package com.mu.ui;

import com.mu.model.Admin;
import com.mu.service.AdminService;
import com.mu.ui.theme.UITheme;

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
        setSize(520, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel(new BorderLayout(16, 16));
        contentPanel.setBackground(UITheme.BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setContentPane(contentPanel);

        JLabel lblTitle = UITheme.createLabel("Create Administrator Account", UITheme.HEADER_FONT, UITheme.PRIMARY_COLOR);
        JLabel lblSubtitle = UITheme.createLabel("Secure access for university management", UITheme.BODY_FONT, UITheme.TEXT_MUTED);

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 6));
        header.setOpaque(false);
        header.add(lblTitle);
        header.add(lblSubtitle);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formCard.add(UITheme.createLabel("Admin Registration", UITheme.SUBHEADER_FONT, UITheme.TEXT_DARK), gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        formCard.add(UITheme.createLabel("Username:"), gbc);
        gbc.gridx = 1;
        txtUsername = UITheme.createTextField(20);
        formCard.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formCard.add(UITheme.createLabel("Password:"), gbc);
        gbc.gridx = 1;
        txtPassword = UITheme.createPasswordField(20);
        formCard.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel buttonRow = new JPanel(new GridLayout(1, 2, 12, 0));
        buttonRow.setOpaque(false);
        btnRegister = UITheme.createPrimaryButton("Register");
        btnBack = UITheme.createOutlineButton("Back");
        buttonRow.add(btnRegister);
        buttonRow.add(btnBack);
        formCard.add(buttonRow, gbc);

        contentPanel.add(header, BorderLayout.NORTH);
        contentPanel.add(formCard, BorderLayout.CENTER);

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
                JOptionPane.showMessageDialog(this, "Username cannot be empty.", "Missing Field", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password cannot be empty.", "Missing Field", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setPassword(password);

            boolean success = adminService.register(admin);
            if (success) {
                JOptionPane.showMessageDialog(this, "Admin account created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                new AdminLoginFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}
