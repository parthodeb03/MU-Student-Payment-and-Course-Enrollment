package com.mu.ui;

import com.mu.dao.CourseDAO;
import com.mu.service.AdminService;
import com.mu.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminDashboardFrame extends JFrame {

    private JButton btnCourse;
    private JButton btnStudents;
    private JButton btnPayments;
    private JButton btnApprovePayments;
    private JButton btnLogout;

    private final AdminService adminService;
    private final CourseDAO courseDAO;

    public AdminDashboardFrame() {
        adminService = new AdminService();
        courseDAO = com.mu.factory.DAOFactory.createCourseDAO();

        setTitle("Metropolitan University - Admin Dashboard");
        setSize(750, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        JPanel headerPanel = UITheme.createCardPanel();
        headerPanel.setLayout(new BorderLayout());
        JLabel title = new JLabel("Administrator Dashboard");
        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(UITheme.PRIMARY_COLOR);
        JLabel subtitle = new JLabel("System Overview & Management Controls");
        subtitle.setFont(UITheme.SUBHEADER_FONT);
        subtitle.setForeground(UITheme.TEXT_MUTED);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        int totalStudents = adminService.getTotalStudents();
        double totalPayments = adminService.getTotalPayments();
        int totalCourses = courseDAO.getAllCourses().size();

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        statsPanel.setBackground(UITheme.BACKGROUND_COLOR);
        statsPanel.add(UITheme.createStatCard("Total Students", String.valueOf(totalStudents), UITheme.PRIMARY_COLOR));
        statsPanel.add(UITheme.createStatCard("Total Revenue", "TK" + String.format("%.2f", totalPayments), UITheme.SUCCESS_COLOR));
        statsPanel.add(UITheme.createStatCard("Active Courses", String.valueOf(totalCourses), UITheme.SECONDARY_COLOR));

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 12, 12));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        btnCourse = UITheme.createPrimaryButton("Manage Courses");
        btnStudents = UITheme.createSecondaryButton("View Registered Students");
        btnPayments = UITheme.createSecondaryButton("View All Payments");
        btnApprovePayments = UITheme.createSecondaryButton("Approve Pending Payments");
        btnLogout = UITheme.createDangerButton("Logout");

        buttonPanel.add(btnCourse);
        buttonPanel.add(btnStudents);
        buttonPanel.add(btnPayments);
        buttonPanel.add(btnApprovePayments);
        buttonPanel.add(btnLogout);

        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(UITheme.BACKGROUND_COLOR);
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        btnCourse.addActionListener(e -> { new CourseManagementFrame(); dispose(); });
        btnStudents.addActionListener(e -> { new ViewStudentsFrame(); dispose(); });
        btnPayments.addActionListener(e -> { new ViewPaymentsFrame(); dispose(); });
        btnApprovePayments.addActionListener(e -> { new PaymentApprovalFrame(); dispose(); });
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Do you want to logout?", "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) { new LoginFrame(); dispose(); }
        });

        setVisible(true);
    }
}