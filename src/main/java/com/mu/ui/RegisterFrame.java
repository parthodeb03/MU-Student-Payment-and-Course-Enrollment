package com.mu.ui;

import com.mu.model.Student;
import com.mu.service.RegistrationService;
import com.mu.ui.theme.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RegisterFrame extends JFrame {

    private JTextField txtName;
    private JComboBox<String> cmbDepartment;
    private JTextField txtBatch;
    private JTextField txtStudentID;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JPasswordField txtPassword;
    private JCheckBox chkShowPassword;

    private JButton btnRegister;
    private JButton btnBack;

    private RegistrationService registrationService;

    public RegisterFrame() {
        registrationService = new RegistrationService();

        setTitle("Student Registration");
        setSize(560, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel(new BorderLayout(16, 16));
        contentPanel.setBackground(UITheme.BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setContentPane(contentPanel);

        JLabel lblTitle = UITheme.createLabel("Create Your Student Account", UITheme.HEADER_FONT, UITheme.PRIMARY_COLOR);
        JLabel lblSubtitle = UITheme.createLabel("Complete all fields to register", UITheme.BODY_FONT, UITheme.TEXT_MUTED);

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 6));
        header.setOpaque(false);
        header.add(lblTitle);
        header.add(lblSubtitle);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formCard.add(UITheme.createLabel("Student Registration", UITheme.SUBHEADER_FONT, UITheme.TEXT_DARK), gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        formCard.add(UITheme.createLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        txtName = UITheme.createTextField(20);
        formCard.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formCard.add(UITheme.createLabel("Department:"), gbc);
        gbc.gridx = 1;
        cmbDepartment = new JComboBox<>();
        cmbDepartment.setFont(UITheme.BODY_FONT);
        cmbDepartment.setBackground(UITheme.SURFACE_COLOR);
        loadDepartments();
        formCard.add(cmbDepartment, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formCard.add(UITheme.createLabel("Batch:"), gbc);
        gbc.gridx = 1;
        txtBatch = UITheme.createTextField(20);
        formCard.add(txtBatch, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formCard.add(UITheme.createLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        txtStudentID = UITheme.createTextField(20);
        formCard.add(txtStudentID, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formCard.add(UITheme.createLabel("Email Address:"), gbc);
        gbc.gridx = 1;
        txtEmail = UITheme.createTextField(20);
        formCard.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formCard.add(UITheme.createLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        txtPhone = UITheme.createTextField(20);
        formCard.add(txtPhone, gbc);

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
        btnBack.addActionListener(e -> { new LoginFrame(); dispose(); });

        setVisible(true);
    }

    private void loadDepartments() {
        com.mu.dao.StudentDAO studentDAO = com.mu.factory.DAOFactory.createStudentDAO();
        List<String> departments = studentDAO.getAllDepartments();
        cmbDepartment.removeAllItems();
        cmbDepartment.addItem("-- Select Department --");
        for (String dept : departments) cmbDepartment.addItem(dept);
    }

    private void registerStudent() {
        try {
            Student student = new Student();
            student.setName(txtName.getText().trim());
            student.setDepartment(cmbDepartment.getSelectedIndex() > 0 ? cmbDepartment.getSelectedItem().toString() : "");
            student.setBatch(txtBatch.getText().trim());
            student.setStudentId(txtStudentID.getText().trim());
            student.setEmail(txtEmail.getText().trim());
            student.setPhone(txtPhone.getText().trim());
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