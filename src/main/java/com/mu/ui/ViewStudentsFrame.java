package com.mu.ui;

import com.mu.model.Student;
import com.mu.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewStudentsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JButton btnBack;

    private AdminService adminService;

    public ViewStudentsFrame() {

        adminService = new AdminService();

        setTitle("Registered Students");

        setSize(700, 400);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        model = new DefaultTableModel();

        model.addColumn("Student ID");
        model.addColumn("Name");
        model.addColumn("Email");

        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        btnBack = new JButton("Back");

        JPanel panel = new JPanel();

        panel.add(btnBack);

        add(panel, BorderLayout.SOUTH);

        loadStudents();

        btnBack.addActionListener(e -> {

            new AdminDashboardFrame();

            dispose();

        });

        setVisible(true);

    }

    private void loadStudents() {

        model.setRowCount(0);

        List<Student> students = adminService.getAllStudents();

        for (Student student : students) {

            model.addRow(new Object[]{

                    student.getStudentId(),

                    student.getName(),

                    student.getEmail()

            });

        }

    }

}