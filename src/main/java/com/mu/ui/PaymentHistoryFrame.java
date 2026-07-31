package com.mu.ui;

import com.mu.model.Payment;
import com.mu.service.PaymentService;
import com.mu.util.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PaymentHistoryFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JButton btnBack;

    private PaymentService paymentService;

    public PaymentHistoryFrame() {

        paymentService = new PaymentService();

        setTitle("Payment History");

        setSize(700,400);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        model = new DefaultTableModel();

        model.addColumn("Payment ID");
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

            new DashboardFrame();

            dispose();

        });

        setVisible(true);

    }

    private void loadPayments() {

        model.setRowCount(0);

        List<Payment> payments =
                paymentService.getPaymentHistory(

                        Session.getCurrentStudent().getStudentId()

                );

        for(Payment payment : payments){

            model.addRow(new Object[]{

                    payment.getPaymentId(),

                    payment.getAmount(),

                    payment.getPaymentMethod(),

                    payment.getPaymentDate()

            });

        }

    }

}