package com.mu.ui;

import com.mu.dao.CourseDAO;
import com.mu.dao.EnrollmentDAO;
import com.mu.model.Course;
import com.mu.service.EnrollmentService;
import com.mu.ui.theme.UITheme;
import com.mu.util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class EnrollmentFrame extends JFrame {

    private JComboBox<Course> cmbCourses;
    private JLabel lblCapacity;
    private JButton btnEnroll;
    private JButton btnBack;

    private final EnrollmentService enrollmentService;
    private final CourseDAO courseDAO;
    private final EnrollmentDAO enrollmentDAO;

    public EnrollmentFrame() {
        enrollmentService = new EnrollmentService();
        courseDAO = com.mu.factory.DAOFactory.createCourseDAO();
        enrollmentDAO = com.mu.factory.DAOFactory.createEnrollmentDAO();

        setTitle("Course Enrollment");
        setSize(520, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Header
        JPanel headerPanel = UITheme.createCardPanel();
        headerPanel.setLayout(new BorderLayout(8, 8));

        JLabel lblTitle = UITheme.createLabel("Enroll in a Course", UITheme.TITLE_FONT, UITheme.PRIMARY_COLOR);
        String studentId = Session.getCurrentStudent().getStudentId();
        int enrolledCount = enrollmentDAO.countEnrolledCourses(studentId);
        lblCapacity = UITheme.createLabel("Status: " + enrolledCount + " / 6 Enrolled", UITheme.SUBHEADER_FONT, enrolledCount >= 6 ? UITheme.DANGER_COLOR : UITheme.SUCCESS_COLOR);
        JLabel lblHint = UITheme.createLabel("Choose your next course and complete enrollment.", UITheme.SMALL_FONT, UITheme.TEXT_MUTED);

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblCapacity, BorderLayout.CENTER);
        headerPanel.add(lblHint, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblSelect = new JLabel("Select Course:");
        lblSelect.setFont(UITheme.BODY_BOLD);
        formCard.add(lblSelect, gbc);

        gbc.gridx = 1;
        cmbCourses = new JComboBox<>();
        cmbCourses.setFont(UITheme.BODY_FONT);
        cmbCourses.setBackground(UITheme.SURFACE_COLOR);
        cmbCourses.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Course) {
                    Course c = (Course) value;
                    setText(c.getCourseName() + " (" + c.getCredit() + " Credits)");
                }
                return this;
            }
        });
        formCard.add(cmbCourses, gbc);

        mainPanel.add(formCard, BorderLayout.CENTER);

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(UITheme.BACKGROUND_COLOR);

        btnEnroll = UITheme.createPrimaryButton("Enroll Now");
        btnBack = UITheme.createOutlineButton("Back to Dashboard");

        actionPanel.add(btnEnroll);
        actionPanel.add(btnBack);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        loadCourses();

        btnEnroll.addActionListener(e -> enrollCourse());
        btnBack.addActionListener(e -> {
            new DashboardFrame();
            dispose();
        });

        setVisible(true);
    }

    private void loadCourses() {
        cmbCourses.removeAllItems();
        List<Course> courses = courseDAO.getAllCourses();
        for (Course course : courses) {
            cmbCourses.addItem(course);
        }
    }

    private void enrollCourse() {
        try {
            Course selectedCourse = (Course) cmbCourses.getSelectedItem();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select a course to enroll.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String studentId = Session.getCurrentStudent().getStudentId();
            boolean success = enrollmentService.enrollCourse(studentId, selectedCourse.getCourseId());

            if (success) {
                JOptionPane.showMessageDialog(this, "Enrollment Successful in: " + selectedCourse.getCourseName(), "Success", JOptionPane.INFORMATION_MESSAGE);
                new DashboardFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Enrollment Failed. Check if already enrolled or capacity reached.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Enrollment Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}