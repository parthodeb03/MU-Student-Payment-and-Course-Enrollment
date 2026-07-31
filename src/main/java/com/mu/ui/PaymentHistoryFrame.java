package com.mu.ui;

import com.mu.model.Payment;
import com.mu.service.PaymentService;
import com.mu.ui.theme.UITheme;
import com.mu.util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class PaymentHistoryFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch;
    private JLabel lblTotalPaid;
    private JButton btnViewReceipt;
    private JButton btnBack;

    private final PaymentService paymentService;

    public PaymentHistoryFrame() {
        paymentService = new PaymentService();

        setTitle("My Payment History");
        setSize(750, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Header and Search Bar
        JPanel topPanel = UITheme.createCardPanel();
        topPanel.setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = UITheme.createLabel("My Transaction History", UITheme.TITLE_FONT, UITheme.PRIMARY_COLOR);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        JLabel lblSearch = UITheme.createLabel("Search History:", UITheme.BODY_BOLD, UITheme.TEXT_DARK);
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

        // Footer Action Panel
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(UITheme.BACKGROUND_COLOR);

        lblTotalPaid = UITheme.createLabel("Total Paid: $0.00", UITheme.HEADER_FONT, UITheme.SUCCESS_COLOR);

        JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightBtns.setBackground(UITheme.BACKGROUND_COLOR);

        btnViewReceipt = UITheme.createSecondaryButton("View Selected Receipt");
        btnBack = UITheme.createOutlineButton("Back to Dashboard");

        rightBtns.add(btnViewReceipt);
        rightBtns.add(btnBack);

        footerPanel.add(lblTotalPaid, BorderLayout.WEST);
        footerPanel.add(rightBtns, BorderLayout.EAST);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        loadHistory();

        // Search Filter Event
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

        btnViewReceipt.addActionListener(e -> showSelectedReceipt());
        btnBack.addActionListener(e -> {
            new DashboardFrame();
            dispose();
        });

        setVisible(true);
    }

    private void loadHistory() {
        model.setRowCount(0);
        int studentId = Session.getCurrentStudent().getStudentId();
        List<Payment> list = paymentService.getPaymentHistory(studentId);
        double totalSum = 0;

        for (Payment p : list) {
            model.addRow(new Object[]{
                    p.getPaymentId(),
                    "$" + String.format("%.2f", p.getAmount()),
                    p.getPaymentMethod(),
                    p.getPaymentDate()
            });
            totalSum += p.getAmount();
        }

        lblTotalPaid.setText("Total Paid: $" + String.format("%.2f", totalSum));
    }

    private void showSelectedReceipt() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payment transaction to view receipt.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int paymentId = (int) model.getValueAt(selectedRow, 0);
        String amountStr = (String) model.getValueAt(selectedRow, 1);
        String method = (String) model.getValueAt(selectedRow, 2);
        Object dateStr = model.getValueAt(selectedRow, 3);

        JDialog receiptDialog = new JDialog(this, "Transaction Receipt #" + paymentId, true);
        receiptDialog.setSize(400, 360);
        receiptDialog.setLocationRelativeTo(this);

        JPanel panel = UITheme.createCardPanel();
        panel.setLayout(new BorderLayout(15, 15));

        JLabel lblTitle = new JLabel("METROPOLITAN UNIVERSITY", SwingConstants.CENTER);
        lblTitle.setFont(UITheme.HEADER_FONT);
        lblTitle.setForeground(UITheme.PRIMARY_COLOR);

        JTextArea receiptText = new JTextArea();
        receiptText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptText.setEditable(false);
        receiptText.setBackground(UITheme.BACKGROUND_COLOR);
        receiptText.setBorder(new EmptyBorder(10, 10, 10, 10));

        receiptText.setText(
                "=========================================\n" +
                "           TRANSACTION RECEIPT\n" +
                "=========================================\n" +
                "Receipt ID    : #" + paymentId + "\n" +
                "Date          : " + dateStr + "\n" +
                "Student ID    : " + Session.getCurrentStudent().getStudentId() + "\n" +
                "Student Name  : " + Session.getCurrentStudent().getName() + "\n" +
                "Payment Method: " + method + "\n" +
                "-----------------------------------------\n" +
                "Amount Paid   : " + amountStr + "\n" +
                "Status        : VERIFIED & COMPLETED\n" +
                "=========================================\n" +
                "       Official University Record"
        );

        JButton btnClose = UITheme.createPrimaryButton("Close");
        btnClose.addActionListener(e -> receiptDialog.dispose());

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(new JScrollPane(receiptText), BorderLayout.CENTER);
        panel.add(btnClose, BorderLayout.SOUTH);

        receiptDialog.add(panel);
        receiptDialog.setVisible(true);
    }
}