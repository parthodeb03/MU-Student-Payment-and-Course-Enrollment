package com.mu.ui;

import com.mu.model.Student;
import com.mu.service.LoginService;
import com.mu.ui.theme.UITheme;
import com.mu.util.InputValidator;
import com.mu.util.Session;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JLabel lblValidation;
    private final LoginService loginService = new LoginService();

    public LoginFrame() {
        setTitle("Metropolitan University Portal");
        setSize(560, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(16, 16));
        content.setBackground(UITheme.BACKGROUND_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setContentPane(content);
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 6));
        header.setOpaque(false);
        header.add(UITheme.createLabel("Metropolitan University", UITheme.HEADER_FONT, UITheme.PRIMARY_COLOR));
        header.add(UITheme.createLabel("Sign in as a student or admin", UITheme.BODY_FONT, UITheme.TEXT_MUTED));

        JPanel card = UITheme.createCardPanel();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = constraints();
        gbc.gridwidth = 2;
        card.add(UITheme.createLabel("Student Login", UITheme.SUBHEADER_FONT, UITheme.TEXT_DARK), gbc);
        gbc.gridy++; gbc.gridwidth = 1;
        card.add(UITheme.createLabel("Email Address *:"), gbc);
        gbc.gridx = 1; txtEmail = UITheme.createTextField(20); txtEmail.setToolTipText("name@example.com"); card.add(txtEmail, gbc);
        gbc.gridx = 0; gbc.gridy++; card.add(UITheme.createLabel("Password *:"), gbc);
        gbc.gridx = 1; txtPassword = UITheme.createPasswordField(20); card.add(txtPassword, gbc);
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JCheckBox show = new JCheckBox("Show password"); show.setOpaque(false); show.setFont(UITheme.SMALL_FONT);
        show.addActionListener(e -> txtPassword.setEchoChar(show.isSelected() ? (char) 0 : '*')); card.add(show, gbc);
        gbc.gridy++; lblValidation = UITheme.createLabel(" ", UITheme.SMALL_FONT, UITheme.DANGER_COLOR); card.add(lblValidation, gbc);
        gbc.gridy++; JPanel studentButtons = new JPanel(new GridLayout(1, 2, 12, 0)); studentButtons.setOpaque(false);
        JButton login = UITheme.createPrimaryButton("Login"), register = UITheme.createOutlineButton("Register");
        studentButtons.add(login); studentButtons.add(register); card.add(studentButtons, gbc);
        gbc.gridy++; JPanel adminButtons = new JPanel(new GridLayout(1, 2, 12, 0)); adminButtons.setOpaque(false);
        JButton adminLogin = UITheme.createSecondaryButton("Admin Login"), adminRegister = UITheme.createSecondaryButton("Create Admin Account");
        adminButtons.add(adminLogin); adminButtons.add(adminRegister); card.add(adminButtons, gbc);
        gbc.gridy++; card.add(UITheme.createLabel("Need help? Contact support@mu.edu", UITheme.SMALL_FONT, UITheme.TEXT_MUTED), gbc);
        content.add(header, BorderLayout.NORTH); content.add(card, BorderLayout.CENTER);
        login.addActionListener(e -> login());
        register.addActionListener(e -> { new RegisterFrame(); dispose(); });
        adminLogin.addActionListener(e -> { new AdminLoginFrame(); dispose(); });
        adminRegister.addActionListener(e -> { new AdminRegistrationFrame(); dispose(); });
        setVisible(true);
    }

    private GridBagConstraints constraints() { GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(12,12,12,12); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST; gbc.gridx = 0; gbc.gridy = 0; return gbc; }
    private void login() { try { String email = txtEmail.getText().trim(), password = String.valueOf(txtPassword.getPassword()); if (email.isEmpty() || password.isEmpty()) { error("Email address and password are required."); return; } if (!InputValidator.isValidEmail(email)) { error("Enter a valid email address, for example name@example.com."); return; } Student student = loginService.login(email, password); if (student != null) { Session.setCurrentStudent(student); new DashboardFrame(); dispose(); } else JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE); } catch (Exception ex) { error(ex.getMessage()); } }
    private void error(String message) { lblValidation.setText(message); }
}
