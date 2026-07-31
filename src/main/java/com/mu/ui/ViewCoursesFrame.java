package com.mu.ui;

import com.mu.dao.CourseDAO;
import com.mu.model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewCoursesFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JButton btnBack;

    private CourseDAO courseDAO;

    public ViewCoursesFrame() {

        courseDAO = new CourseDAO();

        setTitle("Available Courses");

        setSize(650,400);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        model = new DefaultTableModel();

        model.addColumn("Course ID");
        model.addColumn("Course Name");
        model.addColumn("Credit");

        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        btnBack = new JButton("Back");

        JPanel panel = new JPanel();

        panel.add(btnBack);

        add(panel, BorderLayout.SOUTH);

        loadCourses();

        btnBack.addActionListener(e -> {

            new DashboardFrame();

            dispose();

        });

        setVisible(true);

    }

    private void loadCourses() {

        model.setRowCount(0);

        List<Course> courses = courseDAO.getAllCourses();

        for (Course course : courses) {

            model.addRow(new Object[]{

                    course.getCourseId(),

                    course.getCourseName(),

                    course.getCredit()

            });

        }

    }

}