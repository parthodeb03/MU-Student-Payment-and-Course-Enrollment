package com.mu.ui;

import com.mu.model.Admin;
import com.mu.service.AdminService;

import javax.swing.*;
import java.awt.*;

public class AdminLoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private JButton btnLogin;
    private JButton btnBack;

    private AdminService adminService;

    public AdminLoginFrame() {

        adminService = new AdminService();

        setTitle("Admin Login");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel lblTitle = new JLabel("Administrator Login");
        lblTitle.setFont(new Font("Arial",Font.BOLD,20));

        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=2;

        add(lblTitle,gbc);

        gbc.gridwidth=1;

        gbc.gridy++;

        add(new JLabel("Username:"),gbc);

        txtUsername=new JTextField(18);

        gbc.gridx=1;

        add(txtUsername,gbc);

        gbc.gridx=0;
        gbc.gridy++;

        add(new JLabel("Password:"),gbc);

        txtPassword=new JPasswordField(18);

        gbc.gridx=1;

        add(txtPassword,gbc);

        btnLogin=new JButton("Login");
        btnBack=new JButton("Back");

        JPanel panel=new JPanel();

        panel.add(btnLogin);
        panel.add(btnBack);

        gbc.gridx=0;
        gbc.gridy++;
        gbc.gridwidth=2;

        add(panel,gbc);

        btnLogin.addActionListener(e->login());

        btnBack.addActionListener(e->{

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