package com.mu.ui;

import com.mu.model.Admin;
import com.mu.service.AdminService;
import com.mu.ui.theme.UITheme;

import javax.swing.*;
import java.awt.*;

public class AdminLoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private JButton btnLogin;
    private JButton btnRegister;
    private JButton btnBack;

    private AdminService adminService;

    public AdminLoginFrame() {

        adminService = new AdminService();

        setTitle("Admin Portal");
        setSize(520, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel(new BorderLayout(16, 16));
        contentPanel.setBackground(UITheme.BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setContentPane(contentPanel);

        JLabel lblTitle = UITheme.createLabel("Administrator Login", UITheme.HEADER_FONT, UITheme.PRIMARY_COLOR);
        JLabel lblSubtitle = UITheme.createLabel("Secure access to university management tools", UITheme.BODY_FONT, UITheme.TEXT_MUTED);

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
        formCard.add(UITheme.createLabel("Admin Login", UITheme.SUBHEADER_FONT, UITheme.TEXT_DARK), gbc);

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
        JPanel buttonRow = new JPanel(new GridLayout(1, 3, 12, 0));
        buttonRow.setOpaque(false);
        btnLogin = UITheme.createPrimaryButton("Login");
        btnRegister = UITheme.createOutlineButton("Register");
        btnBack = UITheme.createOutlineButton("Back");
        buttonRow.add(btnLogin);
        buttonRow.add(btnRegister);
        buttonRow.add(btnBack);
        formCard.add(buttonRow, gbc);

        contentPanel.add(header, BorderLayout.NORTH);
        contentPanel.add(formCard, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> login());
        btnRegister.addActionListener(e -> {
            new AdminRegistrationFrame();
            dispose();
        });
        btnBack.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);

    }

    private void login(){

        try{

            String username=txtUsername.getText();

            String password=
                    String.valueOf(txtPassword.getPassword());

            Admin admin=
                    adminService.login(username,password);

            if(admin!=null){

                JOptionPane.showMessageDialog(

                        this,

                        "Admin Login Successful."

                );

                new AdminDashboardFrame();

                dispose();

            }else{

                JOptionPane.showMessageDialog(

                        this,

                        "Invalid Username or Password."

                );

            }

        }catch(Exception ex){

            JOptionPane.showMessageDialog(

                    this,

                    ex.getMessage()

            );

        }

    }

}