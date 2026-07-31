package com.mu.ui;

import com.mu.model.Course;
import com.mu.service.EnrollmentService;
import com.mu.ui.theme.UITheme;
import com.mu.util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MyEnrolledCoursesFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotalCredits;
    private JButton btnDrop;
    private JButton btnBack;

    private final EnrollmentService enrollmentService;

    public MyEnrolledCoursesFrame() {
        enrollmentService = new EnrollmentService();

        setTitle("My Enrolled Courses");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Header Panel
        JPanel headerPanel = UITheme.createCardPanel();
        headerPanel.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("My Enrolled Courses");
        lblTitle.setFont(UITheme.TITLE_FONT);
        lblTitle.setForeground(UITheme.PRIMARY_COLOR);

        lblTotalCredits = new JLabel("Total Credits: 0");
        lblTotalCredits.setFont(UITheme.HEADER_FONT);
        lblTotalCredits.setForeground(UITheme.SECONDARY_COLOR);

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(lblTotalCredits, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Course ID");
        model.addColumn("Course Name");
        model.addColumn("Credits");

        table = new JTable(model);
        UITheme.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(UITheme.BACKGROUND_COLOR);

        btnDrop = UITheme.createDangerButton("Drop Selected Course");
        btnBack = UITheme.createOutlineButton("Back to Dashboard");

        actionPanel.add(btnDrop);
        actionPanel.add(btnBack);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        loadEnrolledCourses();

        btnDrop.addActionListener(e -> dropSelectedCourse());
        btnBack.addActionListener(e -> {
            new DashboardFrame();
            dispose();
        });

        setVisible(true);
    }

    private void loadEnrolledCourses() {
        model.setRowCount(0);
        int totalCredits = 0;

        int studentId = Session.getCurrentStudent().getStudentId();
        List<Course> courses = enrollmentService.getEnrolledCourses(studentId);

        for (Course course : courses) {
            model.addRow(new Object[]{
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getCredit()
            });
            totalCredits += course.getCredit();
        }

        lblTotalCredits.setText("Total Credits: " + totalCredits);
    }

    private void dropSelectedCourse() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to drop.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int courseId = (int) model.getValueAt(selectedRow, 0);
        String courseName = (String) model.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to drop '" + courseName + "'?",
                "Confirm Drop Course",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int studentId = Session.getCurrentStudent().getStudentId();
            boolean success = enrollmentService.dropCourse(studentId, courseId);

            if (success) {
                JOptionPane.showMessageDialog(this, "Successfully dropped course: " + courseName, "Course Dropped", JOptionPane.INFORMATION_MESSAGE);
                loadEnrolledCourses();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to drop course.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
