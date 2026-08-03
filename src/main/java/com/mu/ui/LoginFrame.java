package com.mu.ui;

import com.mu.model.Student;
import com.mu.service.LoginService;
import com.mu.ui.theme.UITheme;
import com.mu.util.Session;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JCheckBox chkShowPassword;

    private JButton btnLogin;
    private JButton btnRegister;
    private JButton btnAdmin;

    private LoginService loginService;

    public LoginFrame() {
        loginService = new LoginService();

        setTitle("Metropolitan University Portal");
        setSize(520, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel(new BorderLayout(16, 16));
        contentPanel.setBackground(UITheme.BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setContentPane(contentPanel);

        JLabel lblTitle = UITheme.createLabel("Metropolitan University", UITheme.HEADER_FONT, UITheme.PRIMARY_COLOR);
        JLabel lblSubtitle = UITheme.createLabel("Sign in as a student or admin", UITheme.BODY_FONT, UITheme.TEXT_MUTED);

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

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formCard.add(UITheme.createLabel("Student Login", UITheme.SUBHEADER_FONT, UITheme.TEXT_DARK), gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        formCard.add(UITheme.createLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = UITheme.createTextField(20);
        formCard.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formCard.add(UITheme.createLabel("Password:"), gbc);
        gbc.gridx = 1;
        txtPassword = UITheme.createPasswordField(20);
        formCard.add(txtPassword, gbc);

        gbc.gridx = 1; gbc.gridy++;
        chkShowPassword = new JCheckBox("Show Password");
        chkShowPassword.setFont(UITheme.SMALL_FONT);
        chkShowPassword.setBackground(UITheme.SURFACE_COLOR);
        chkShowPassword.setForeground(UITheme.TEXT_MUTED);
        chkShowPassword.addActionListener(e -> {
            txtPassword.setEchoChar(chkShowPassword.isSelected() ? (char) 0 : '•');
        });
        formCard.add(chkShowPassword, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JPanel authButtons = new JPanel(new GridLayout(1, 2, 12, 0));
        authButtons.setOpaque(false);
        btnLogin = UITheme.createPrimaryButton("Login");
        btnRegister = UITheme.createOutlineButton("Register");
        authButtons.add(btnLogin);
        authButtons.add(btnRegister);
        formCard.add(authButtons, gbc);

        gbc.gridy++;
        JPanel adminButtons = new JPanel(new GridLayout(1, 1, 12, 0));
        adminButtons.setOpaque(false);
        btnAdmin = UITheme.createSecondaryButton("Admin Login");
        adminButtons.add(btnAdmin);
        formCard.add(adminButtons, gbc);

        gbc.gridy++;
        formCard.add(UITheme.createLabel("Need help? Contact support@mu.edu", UITheme.SMALL_FONT, UITheme.TEXT_MUTED), gbc);

        contentPanel.add(header, BorderLayout.NORTH);
        contentPanel.add(formCard, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> login());
        btnRegister.addActionListener(e -> { new RegisterFrame(); dispose(); });
        btnAdmin.addActionListener(e -> { new AdminLoginFrame(); dispose(); });

        setVisible(true);
    }

    private void login() {
        try {
            String email = txtEmail.getText().trim();
            String password = String.valueOf(txtPassword.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both email and password.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Student student = loginService.login(email, password);
            if (student != null) {
                Session.setCurrentStudent(student);
                JOptionPane.showMessageDialog(this, "Login Successful", "Welcome", JOptionPane.INFORMATION_MESSAGE);
                new DashboardFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}