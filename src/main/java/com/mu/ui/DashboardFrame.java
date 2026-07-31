package com.mu.ui;

import com.mu.util.Session;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private JButton btnCourses;
    private JButton btnEnroll;
    private JButton btnPayment;
    private JButton btnHistory;
    private JButton btnLogout;

    public DashboardFrame() {

        setTitle("Student Dashboard");

        setSize(500, 400);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JLabel lblWelcome = new JLabel(

                "Welcome, " + Session.getCurrentStudent().getName(),

                SwingConstants.CENTER

        );

        lblWelcome.setFont(new Font("Arial", Font.BOLD, 22));

        add(lblWelcome, BorderLayout.NORTH);

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(5, 1, 10, 10));

        btnCourses = new JButton("View Courses");

        btnEnroll = new JButton("Enroll Course");

        btnPayment = new JButton("Make Payment");

        btnHistory = new JButton("Payment History");

        btnLogout = new JButton("Logout");

        panel.add(btnCourses);
        panel.add(btnEnroll);
        panel.add(btnPayment);
        panel.add(btnHistory);
        panel.add(btnLogout);

        add(panel, BorderLayout.CENTER);

        // Button Actions

        btnCourses.addActionListener(e -> {

            new ViewCoursesFrame();

            dispose();

        });

        btnEnroll.addActionListener(e -> {

            new EnrollmentFrame();

            dispose();

        });

        btnPayment.addActionListener(e -> {

            new PaymentFrame();

            dispose();

        });

        btnHistory.addActionListener(e -> {

            new PaymentHistoryFrame();

            dispose();

        });

        btnLogout.addActionListener(e -> {

            Session.clearSession();

            JOptionPane.showMessageDialog(

                    this,

                    "Logged Out Successfully."

            );

            new LoginFrame();

            dispose();

        });

        setVisible(true);

    }

}