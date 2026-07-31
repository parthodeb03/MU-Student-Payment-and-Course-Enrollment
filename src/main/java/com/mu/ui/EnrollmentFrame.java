package com.mu.ui;

import com.mu.dao.CourseDAO;
import com.mu.model.Course;
import com.mu.service.EnrollmentService;
import com.mu.util.Session;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EnrollmentFrame extends JFrame {

    private JComboBox<Course> cmbCourses;

    private JButton btnEnroll;
    private JButton btnBack;

    private EnrollmentService enrollmentService;
    private CourseDAO courseDAO;

    public EnrollmentFrame() {

        enrollmentService = new EnrollmentService();
        courseDAO = new CourseDAO();

        setTitle("Course Enrollment");

        setSize(450,250);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);

        JLabel lblTitle = new JLabel("Enroll in a Course");

        lblTitle.setFont(new Font("Arial",Font.BOLD,18));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        add(lblTitle, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;

        add(new JLabel("Select Course:"), gbc);

        cmbCourses = new JComboBox<>();

        gbc.gridx = 1;

        add(cmbCourses, gbc);

        btnEnroll = new JButton("Enroll");

        btnBack = new JButton("Back");

        JPanel panel = new JPanel();

        panel.add(btnEnroll);
        panel.add(btnBack);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        add(panel, gbc);

        loadCourses();

        btnEnroll.addActionListener(e -> enrollCourse());

        btnBack.addActionListener(e -> {

            new DashboardFrame();

            dispose();

        });

        setVisible(true);

    }

    private void loadCourses() {

        List<Course> courses = courseDAO.getAllCourses();

        for (Course course : courses) {

            cmbCourses.addItem(course);

        }

    }

    private void enrollCourse() {

        try {

            Course selectedCourse =
                    (Course) cmbCourses.getSelectedItem();

            if (selectedCourse == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a course."
                );

                return;

            }

            boolean success =
                    enrollmentService.enrollCourse(

                            Session.getCurrentStudent().getStudentId(),

                            selectedCourse.getCourseId()

                    );

            if (success) {

                JOptionPane.showMessageDialog(

                        this,

                        "Enrollment Successful!"

                );

            } else {

                JOptionPane.showMessageDialog(

                        this,

                        "Enrollment Failed."

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