package com.mu.ui;

import com.mu.model.Student;
import com.mu.service.LoginService;
import com.mu.ui.theme.FormSupport;
import com.mu.ui.theme.UITheme;
import com.mu.util.InputValidator;
import com.mu.util.Session;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JLabel lblError;
    private final LoginService loginService = new LoginService();

    public LoginFrame() {
        setTitle("Metropolitan University Portal");
        setSize(540, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout(16, 16));
        content.setBackground(UITheme.BACKGROUND_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setContentPane(content);

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 6)); header.setOpaque(false);
        header.add(UITheme.createLabel("Metropolitan University", UITheme.HEADER_FONT, UITheme.PRIMARY_COLOR));
        header.add(UITheme.createLabel("Student portal", UITheme.BODY_FONT, UITheme.TEXT_MUTED));
        content.add(header, BorderLayout.NORTH);

        JPanel card = UITheme.createCardPanel(); card.setLayout(new GridBagLayout());
        GridBagConstraints g = constraints();
        add(card, g, "Student Login", UITheme.SUBHEADER_FONT); g.gridy++;
        add(card, g, "Email Address *", UITheme.BODY_BOLD); g.gridx = 1;
        txtEmail = UITheme.createTextField(20); txtEmail.setToolTipText("name@example.com"); card.add(txtEmail, g); g.gridx = 0; g.gridy++;
        g.gridwidth = 2; add(card, g, "Use your university email address.", UITheme.SMALL_FONT); g.gridy++;
        g.gridwidth = 1; add(card, g, "Password *", UITheme.BODY_BOLD); g.gridx = 1;
        txtPassword = UITheme.createPasswordField(20); card.add(txtPassword, g); g.gridx = 0; g.gridy++;
        g.gridwidth = 2; card.add(FormSupport.createShowPasswordToggle(txtPassword), g); g.gridy++;
        lblError = FormSupport.createErrorLabel(); card.add(lblError, g); g.gridy++;
        JPanel buttons = new JPanel(new GridLayout(1, 2, 12, 0)); buttons.setOpaque(false);
        JButton login = UITheme.createPrimaryButton("Login"); JButton register = UITheme.createOutlineButton("Register");
        buttons.add(login); buttons.add(register); card.add(buttons, g); g.gridy++;
        JPanel admin = new JPanel(new GridLayout(1, 2, 12, 0)); admin.setOpaque(false);
        JButton adminLogin = UITheme.createSecondaryButton("Admin Login"); JButton adminRegister = UITheme.createSecondaryButton("Create Admin Account");
        admin.add(adminLogin); admin.add(adminRegister); card.add(admin, g); g.gridy++;
        add(card, g, "Forgot password? Contact support@mu.edu", UITheme.SMALL_FONT);
        content.add(card, BorderLayout.CENTER);
        login.addActionListener(e -> login());
        register.addActionListener(e -> { new RegisterFrame(); dispose(); });
        adminLogin.addActionListener(e -> { new AdminLoginFrame(); dispose(); });
        adminRegister.addActionListener(e -> { new AdminRegistrationFrame(); dispose(); });
        setVisible(true);
    }
    private GridBagConstraints constraints() { GridBagConstraints g = new GridBagConstraints(); g.gridx=0; g.gridy=0; g.gridwidth=2; g.insets=new Insets(8,8,8,8); g.fill=GridBagConstraints.HORIZONTAL; g.anchor=GridBagConstraints.WEST; return g; }
    private void add(JPanel p, GridBagConstraints g, String text, Font font) { p.add(UITheme.createLabel(text, font, font == UITheme.SMALL_FONT ? UITheme.TEXT_MUTED : UITheme.TEXT_DARK), g); }
    private void login() {
        FormSupport.clearError(lblError);
        String email = txtEmail.getText().trim(), password = String.valueOf(txtPassword.getPassword());
        if (!InputValidator.isValidEmail(email)) { FormSupport.showError(lblError, "Enter a valid email address."); return; }
        if (password.isEmpty()) { FormSupport.showError(lblError, "Password is required."); return; }
        try { Student student = loginService.login(email, password); if (student != null) { Session.setCurrentStudent(student); new DashboardFrame(); dispose(); } else FormSupport.showError(lblError, "Invalid email or password."); }
        catch (Exception ex) { FormSupport.showError(lblError, ex.getMessage()); }
    }
}
