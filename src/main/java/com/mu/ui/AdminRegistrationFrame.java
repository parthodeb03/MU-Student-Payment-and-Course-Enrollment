package com.mu.ui;

import com.mu.model.Admin;
import com.mu.service.AdminService;
import com.mu.ui.theme.FormSupport;
import com.mu.ui.theme.UITheme;
import com.mu.util.InputValidator;

import javax.swing.*;
import java.awt.*;

public class AdminRegistrationFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JLabel lblError;
    private final AdminService adminService = new AdminService();

    public AdminRegistrationFrame() {
        setTitle("Admin Registration");
        setSize(540, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout(16, 16));
        content.setBackground(UITheme.BACKGROUND_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setContentPane(content);
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 6));
        header.setOpaque(false);
        header.add(UITheme.createLabel("Create Administrator Account", UITheme.HEADER_FONT, UITheme.SECONDARY_COLOR));
        header.add(UITheme.createLabel("Administrator accounts have access to management tools", UITheme.BODY_FONT, UITheme.TEXT_MUTED));
        content.add(header, BorderLayout.NORTH);
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new GridBagLayout());
        GridBagConstraints g = c();
        label(card, g, "Admin Registration", UITheme.SUBHEADER_FONT);
        g.gridy++;
        g.gridwidth = 1;
        label(card, g, "Username *", UITheme.BODY_BOLD);
        g.gridx = 1;
        txtUsername = UITheme.createTextField(20);
        txtUsername.setToolTipText("4-30 letters, numbers, or underscores");
        card.add(txtUsername, g);
        g.gridx = 0;
        g.gridy++;
        label(card, g, "Password *", UITheme.BODY_BOLD);
        g.gridx = 1;
        txtPassword = UITheme.createPasswordField(20);
        card.add(txtPassword, g);
        g.gridx = 0;
        g.gridy++;
        label(card, g, "Confirm Password *", UITheme.BODY_BOLD);
        g.gridx = 1;
        txtConfirmPassword = UITheme.createPasswordField(20);
        card.add(txtConfirmPassword, g);
        g.gridx = 0;
        g.gridy++;
        g.gridwidth = 2;
        card.add(FormSupport.createShowPasswordToggle(txtPassword, txtConfirmPassword), g);
        g.gridy++;
        lblError = FormSupport.createErrorLabel();
        card.add(lblError, g);
        g.gridy++;
        JPanel buttons = new JPanel(new GridLayout(1, 2, 12, 0));
        buttons.setOpaque(false);
        JButton register = UITheme.createPrimaryButton("Register"), back = UITheme.createOutlineButton("Back to Admin Login");
        buttons.add(register);
        buttons.add(back);
        card.add(buttons, g);
        content.add(card, BorderLayout.CENTER);
        register.addActionListener(e -> register());
        back.addActionListener(e -> {
            new AdminLoginFrame();
            dispose();
        });
        setVisible(true);
    }

    private GridBagConstraints c() {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        return g;
    }

    private void label(JPanel p, GridBagConstraints g, String s, Font f) {
        p.add(UITheme.createLabel(s, f, f == UITheme.SMALL_FONT ? UITheme.TEXT_MUTED : UITheme.TEXT_DARK), g);
    }

    private void register() {
        FormSupport.clearError(lblError);
        String username = txtUsername.getText().trim(), password = String.valueOf(txtPassword.getPassword());
        if (!InputValidator.isValidUsername(username)) {
            FormSupport.showError(lblError, "Username must be 4-30 letters, numbers, or underscores.");
            return;
        }
        if (!InputValidator.isValidPassword(password)) {
            FormSupport.showError(lblError, "Password must contain 6 to 50 characters.");
            return;
        }
        if (!password.equals(String.valueOf(txtConfirmPassword.getPassword()))) {
            FormSupport.showError(lblError, "Passwords do not match.");
            return;
        }
        try {
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setPassword(password);
            adminService.register(admin);
            JOptionPane.showMessageDialog(this, "Admin account created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            new AdminLoginFrame();
            dispose();
        } catch (Exception ex) {
            FormSupport.showError(lblError, ex.getMessage());
        }
    }
}
