package com.mu.ui;

import com.mu.model.Payment;
import com.mu.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewPaymentsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JButton btnBack;

    private AdminService adminService;

    public ViewPaymentsFrame() {

        adminService = new AdminService();

        setTitle("All Payments");

        setSize(800, 450);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        model = new DefaultTableModel();

        model.addColumn("Payment ID");
        model.addColumn("Student ID");
        model.addColumn("Amount");
        model.addColumn("Method");
        model.addColumn("Payment Date");

        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        btnBack = new JButton("Back");

        JPanel panel = new JPanel();

        panel.add(btnBack);

        add(panel, BorderLayout.SOUTH);

        loadPayments();

        btnBack.addActionListener(e -> {

            new AdminDashboardFrame();

            dispose();

        });

        setVisible(true);

    }

    private void loadPayments() {

        model.setRowCount(0);

        List<Payment> payments = adminService.getAllPayments();

        for (Payment payment : payments) {

            model.addRow(new Object[]{

                    payment.getPaymentId(),

                    payment.getStudentId(),

                    payment.getAmount(),

                    payment.getPaymentMethod(),

                    payment.getPaymentDate()

            });

        }

    }

}