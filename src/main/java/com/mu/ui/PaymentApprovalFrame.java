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

public class PaymentApprovalFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch;
    private JButton btnApprove;
    private JButton btnRefresh;
    private JButton btnBack;

    private final AdminService adminService;

    public PaymentApprovalFrame() {
        adminService = new AdminService();

        setTitle("Pending Payment Approvals");
        setSize(950, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        JPanel topPanel = UITheme.createCardPanel();
        topPanel.setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("Pending Payment Approvals");
        lblTitle.setFont(UITheme.TITLE_FONT);
        lblTitle.setForeground(UITheme.PRIMARY_COLOR);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Search: ");
        lblSearch.setFont(UITheme.BODY_BOLD);
        txtSearch = UITheme.createTextField(16);

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Payment ID");
        model.addColumn("Student ID");
        model.addColumn("Type");
        model.addColumn("Amount");
        model.addColumn("Method");
        model.addColumn("Details");
        model.addColumn("Date");
        model.addColumn("Reference");

        table = new JTable(model);
        UITheme.styleTable(table);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footerPanel.setBackground(UITheme.BACKGROUND_COLOR);

        btnApprove = UITheme.createPrimaryButton("Approve Selected");
        btnRefresh = UITheme.createSecondaryButton("Refresh List");
        btnBack = UITheme.createOutlineButton("Back to Dashboard");

        footerPanel.add(btnApprove);
        footerPanel.add(btnRefresh);
        footerPanel.add(btnBack);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        loadPendingPayments();

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

        btnApprove.addActionListener(e -> approveSelectedPayment());
        btnRefresh.addActionListener(e -> loadPendingPayments());
        btnBack.addActionListener(e -> {
            new AdminDashboardFrame();
            dispose();
        });

        setVisible(true);
    }

    private void loadPendingPayments() {
        model.setRowCount(0);
        List<Payment> payments = adminService.getPendingPayments();

        for (Payment payment : payments) {
            String details = "";
            if (payment.getMonth() != null && !payment.getMonth().isEmpty()) {
                details = payment.getMonth() + " " + payment.getYear();
            } else if (payment.getTermName() != null && !payment.getTermName().isEmpty()) {
                details = payment.getTermName();
            }

            model.addRow(new Object[]{
                    payment.getPaymentId(),
                    payment.getStudentId(),
                    payment.getPaymentType(),
                    "$" + String.format("%.2f", payment.getAmount()),
                    payment.getPaymentMethod(),
                    details,
                    payment.getPaymentDate(),
                    payment.getReferenceMessage() != null ? payment.getReferenceMessage() : ""
            });
        }

        if (payments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No pending payments found.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void approveSelectedPayment() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payment to approve.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int paymentId = (int) model.getValueAt(modelRow, 0);
        String amount = (String) model.getValueAt(modelRow, 3);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Approve payment #" + paymentId + " (" + amount + ")?",
                "Confirm Approval",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = adminService.approvePayment(paymentId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Payment approved successfully.", "Approved", JOptionPane.INFORMATION_MESSAGE);
                loadPendingPayments();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to approve payment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}