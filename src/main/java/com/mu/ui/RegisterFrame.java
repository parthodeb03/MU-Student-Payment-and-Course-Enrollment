package com.mu.ui;

import com.mu.model.Student;
import com.mu.service.RegistrationService;
import com.mu.ui.theme.UITheme;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    private JButton btnRegister;
    private JButton btnBack;

    private RegistrationService registrationService;

    public RegisterFrame() {
        registrationService = new RegistrationService();

        setTitle("Student Registration");
        setSize(520, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel(new BorderLayout(16, 16));
        contentPanel.setBackground(UITheme.BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setContentPane(contentPanel);

        JLabel lblTitle = UITheme.createLabel("Create Your Student Account", UITheme.HEADER_FONT, UITheme.PRIMARY_COLOR);
        JLabel lblSubtitle = UITheme.createLabel("Simple registration in just a few steps", UITheme.BODY_FONT, UITheme.TEXT_MUTED);

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
        formCard.add(UITheme.createLabel("Student Registration", UITheme.SUBHEADER_FONT, UITheme.TEXT_DARK), gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        formCard.add(UITheme.createLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        txtName = UITheme.createTextField(20);
        formCard.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formCard.add(UITheme.createLabel("Email Address:"), gbc);
        gbc.gridx = 1;
        txtEmail = UITheme.createTextField(20);
        formCard.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formCard.add(UITheme.createLabel("Password:"), gbc);
        gbc.gridx = 1;
        txtPassword = UITheme.createPasswordField(20);
        formCard.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel actions = new JPanel(new GridLayout(1, 2, 12, 0));
        actions.setOpaque(false);
        btnRegister = UITheme.createPrimaryButton("Register");
        btnBack = UITheme.createOutlineButton("Back");
        actions.add(btnRegister);
        actions.add(btnBack);
        formCard.add(actions, gbc);

        gbc.gridy++;
        formCard.add(UITheme.createLabel("Already have an account? Login instead.", UITheme.SMALL_FONT, UITheme.TEXT_MUTED), gbc);

        contentPanel.add(header, BorderLayout.NORTH);
        contentPanel.add(formCard, BorderLayout.CENTER);

        btnRegister.addActionListener(e -> registerStudent());
        btnBack.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    private void registerStudent() {
        try {
            Student student = new Student();
            student.setName(txtName.getText().trim());
            student.setEmail(txtEmail.getText().trim());
            student.setPassword(String.valueOf(txtPassword.getPassword()));

            boolean success = registrationService.register(student);
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please login to continue.", "Success", JOptionPane.INFORMATION_MESSAGE);
                new LoginFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}
