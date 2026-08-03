package com.mu.ui;

import com.mu.dao.EnrollmentDAO;
import com.mu.dao.PaymentDAO;
import com.mu.ui.theme.UITheme;
import com.mu.util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private JButton btnMyCourses;
    private JButton btnViewAllCourses;
    private JButton btnEnroll;
    private JButton btnPayment;
    private JButton btnHistory;
    private JButton btnLogout;

    private final EnrollmentDAO enrollmentDAO;
    private final PaymentDAO paymentDAO;

    public DashboardFrame() {
        enrollmentDAO = com.mu.factory.DAOFactory.createEnrollmentDAO();
        paymentDAO = com.mu.factory.DAOFactory.createPaymentDAO();

        setTitle("Metropolitan University - Student Dashboard");
        setSize(750, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        JPanel headerPanel = UITheme.createCardPanel();
        headerPanel.setLayout(new BorderLayout(8, 8));

        JLabel lblWelcome = UITheme.createLabel("Welcome back, " + Session.getCurrentStudent().getName() + "!", UITheme.TITLE_FONT, UITheme.PRIMARY_COLOR);
        JLabel lblSubtitle = UITheme.createLabel(
                Session.getCurrentStudent().getDepartment() + " | Batch: " + Session.getCurrentStudent().getBatch() + " | ID: " + Session.getCurrentStudent().getStudentId(),
                UITheme.SUBHEADER_FONT, UITheme.TEXT_MUTED
        );
        JLabel lblHint = UITheme.createLabel("Quick actions keep your enrollment and payments up to date.", UITheme.SMALL_FONT, UITheme.TEXT_MUTED);

        headerPanel.add(lblWelcome, BorderLayout.NORTH);
        headerPanel.add(lblSubtitle, BorderLayout.CENTER);
        headerPanel.add(lblHint, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        String studentId = Session.getCurrentStudent().getStudentId();
        String universityId = Session.getCurrentStudent().getStudentId();
        int enrolledCount = enrollmentDAO.countEnrolledCourses(studentId);
        double totalPaid = paymentDAO.getTotalPaid(universityId);

        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        statsPanel.setBackground(UITheme.BACKGROUND_COLOR);

        statsPanel.add(UITheme.createStatCard("Enrolled Courses", enrolledCount + " / 6 Courses", UITheme.PRIMARY_COLOR));
        statsPanel.add(UITheme.createStatCard("Total Amount Paid", "$" + String.format("%.2f", totalPaid), UITheme.SUCCESS_COLOR));

        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 12, 12));
        gridPanel.setBackground(UITheme.BACKGROUND_COLOR);

        btnMyCourses = UITheme.createPrimaryButton("My Enrolled Courses");
        btnViewAllCourses = UITheme.createSecondaryButton("Available Courses");
        btnEnroll = UITheme.createSecondaryButton("Enroll Course");
        btnPayment = UITheme.createSecondaryButton("Make Payment");
        btnHistory = UITheme.createSecondaryButton("Payment History");
        btnLogout = UITheme.createDangerButton("Logout");

        gridPanel.add(btnMyCourses);
        gridPanel.add(btnViewAllCourses);
        gridPanel.add(btnEnroll);
        gridPanel.add(btnPayment);
        gridPanel.add(btnHistory);
        gridPanel.add(btnLogout);

        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(UITheme.BACKGROUND_COLOR);
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(gridPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        btnMyCourses.addActionListener(e -> {
            new MyEnrolledCoursesFrame();
            dispose();
        });

        btnViewAllCourses.addActionListener(e -> {
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
            JOptionPane.showMessageDialog(this, "Logged Out Successfully.", "Logged Out", JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }
}