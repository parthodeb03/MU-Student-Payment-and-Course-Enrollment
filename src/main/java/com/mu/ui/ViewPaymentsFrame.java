package com.mu.ui;

import com.mu.model.Payment;
import com.mu.service.AdminService;
import com.mu.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class ViewPaymentsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch;
    private JLabel lblTotalRevenue;
    private JButton btnBack;

    private final AdminService adminService;

    public ViewPaymentsFrame() {
        adminService = new AdminService();

        setTitle("Payment Records");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Header
        JPanel topPanel = UITheme.createCardPanel();
        topPanel.setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("All Payment Transactions");
        lblTitle.setFont(UITheme.TITLE_FONT);
        lblTitle.setForeground(UITheme.PRIMARY_COLOR);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Search Payments: ");
        lblSearch.setFont(UITheme.BODY_BOLD);
        txtSearch = UITheme.createTextField(16);

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Payment ID");
        model.addColumn("Student ID");
        model.addColumn("Amount");
        model.addColumn("Payment Method");
        model.addColumn("Payment Date");

        table = new JTable(model);
        UITheme.styleTable(table);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(UITheme.BACKGROUND_COLOR);

        lblTotalRevenue = new JLabel("Total Revenue: $0.00");
        lblTotalRevenue.setFont(UITheme.HEADER_FONT);
        lblTotalRevenue.setForeground(UITheme.SUCCESS_COLOR);

        btnBack = UITheme.createOutlineButton("Back to Dashboard");

        footerPanel.add(lblTotalRevenue, BorderLayout.WEST);
        footerPanel.add(btnBack, BorderLayout.EAST);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        loadPayments();

        // Search Listener
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = txtSearch.getText().trim();
                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        btnBack.addActionListener(e -> {
            new AdminDashboardFrame();
            dispose();
        });

        setVisible(true);
    }

    private void loadPayments() {
        model.setRowCount(0);
        double totalSum = 0;
        List<Payment> payments = adminService.getAllPayments();

        for (Payment payment : payments) {
            model.addRow(new Object[]{
                    payment.getPaymentId(),
                    payment.getStudentId(),
                    "$" + String.format("%.2f", payment.getAmount()),
                    payment.getPaymentMethod(),
                    payment.getPaymentDate()
            });
            totalSum += payment.getAmount();
        }

        lblTotalRevenue.setText("Total Revenue: $" + String.format("%.2f", totalSum));
    }
}