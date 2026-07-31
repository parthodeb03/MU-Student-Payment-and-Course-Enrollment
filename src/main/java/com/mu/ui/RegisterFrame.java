package com.mu.ui;

import com.mu.model.Student;
import com.mu.service.RegistrationService;

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
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel lblTitle = new JLabel("Student Registration");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Name:"), gbc);

        txtName = new JTextField(20);
        gbc.gridx = 1;
        add(txtName, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        add(txtEmail, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Password:"), gbc);

        txtPassword = new JPasswordField(20);
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

            student.setName(txtName.getText());
            student.setEmail(txtEmail.getText());
            student.setPassword(
                    String.valueOf(txtPassword.getPassword())
            );

            boolean success = registrationService.register(student);

            if(success){

                JOptionPane.showMessageDialog(
                        this,
                        "Registration Successful!"
                );

                new LoginFrame();

                dispose();

            }else{

                JOptionPane.showMessageDialog(
                        this,
                        "Registration Failed!"
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