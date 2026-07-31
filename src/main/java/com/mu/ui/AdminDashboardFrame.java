package com.mu.ui;

import com.mu.service.AdminService;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardFrame extends JFrame {

    private JLabel lblStudents;
    private JLabel lblPayments;

    private JButton btnCourse;
    private JButton btnStudents;
    private JButton btnPayments;
    private JButton btnLogout;

    private AdminService adminService;

    public AdminDashboardFrame() {

        adminService = new AdminService();

        setTitle("Admin Dashboard");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel(
                "Metropolitan University Admin Dashboard",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 20));

        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));

        lblStudents = new JLabel();
        lblPayments = new JLabel();

        lblStudents.setHorizontalAlignment(SwingConstants.CENTER);
        lblPayments.setHorizontalAlignment(SwingConstants.CENTER);

        lblStudents.setFont(new Font("Arial", Font.BOLD, 16));
        lblPayments.setFont(new Font("Arial", Font.BOLD, 16));

        centerPanel.add(lblStudents);
        centerPanel.add(lblPayments);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2,2,10,10));

        btnCourse = new JButton("Manage Courses");
        btnStudents = new JButton("View Students");
        btnPayments = new JButton("View Payments");
        btnLogout = new JButton("Logout");

        buttonPanel.add(btnCourse);
        buttonPanel.add(btnStudents);
        buttonPanel.add(btnPayments);
        buttonPanel.add(btnLogout);

        add(buttonPanel, BorderLayout.SOUTH);

        loadDashboard();

        btnCourse.addActionListener(e -> {

            new CourseManagementFrame();

            dispose();

        });

        btnStudents.addActionListener(e -> {

            new ViewStudentsFrame();

            dispose();

        });

        btnPayments.addActionListener(e -> {

            new ViewPaymentsFrame();

            dispose();

        });

        btnLogout.addActionListener(e -> {

            new LoginFrame();

            dispose();

        });

        setVisible(true);

    }

    private void loadDashboard() {

        lblStudents.setText(
                "Total Students : " +
                        adminService.getTotalStudents()
        );

        lblPayments.setText(
                "Total Payment : " +
                        adminService.getTotalPayments()
        );

    }

}