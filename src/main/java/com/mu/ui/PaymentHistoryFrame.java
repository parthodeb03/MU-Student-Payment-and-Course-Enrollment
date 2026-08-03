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
        setSize(850, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

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

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        model.addColumn("Payment ID");
        model.addColumn("Type");
        model.addColumn("Amount");
        model.addColumn("Method");
        model.addColumn("Details");
        model.addColumn("Date");
        model.addColumn("Status");

        table = new JTable(model);
        UITheme.styleTable(table);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filter(); }
            @Override public void removeUpdate(DocumentEvent e) { filter(); }
            @Override public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String text = txtSearch.getText().trim();
                sorter.setRowFilter(text.isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
            }
        });

        btnViewReceipt.addActionListener(e -> showSelectedReceipt());
        btnBack.addActionListener(e -> { new DashboardFrame(); dispose(); });

        setVisible(true);
    }

    private void loadHistory() {
        model.setRowCount(0);
        String studentId = Session.getCurrentStudent().getStudentId();
        List<Payment> list = paymentService.getPaymentHistory(studentId);
        double totalSum = 0;

        for (Payment p : list) {
            String details = "";
            if (p.getMonth() != null && !p.getMonth().isEmpty()) details = p.getMonth() + " " + p.getYear();
            else if (p.getTermName() != null && !p.getTermName().isEmpty()) details = p.getTermName();

            model.addRow(new Object[]{
                    p.getPaymentId(), p.getPaymentType(),
                    "$" + String.format("%.2f", p.getAmount()), p.getPaymentMethod(),
                    details, p.getPaymentDate(), p.getStatus()
            });
            if ("APPROVED".equalsIgnoreCase(p.getStatus())) totalSum += p.getAmount();
        }
        lblTotalPaid.setText("Total Paid: $" + String.format("%.2f", totalSum));
    }

    private void showSelectedReceipt() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payment transaction to view receipt.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int paymentId = (int) model.getValueAt(modelRow, 0);
        String type = (String) model.getValueAt(modelRow, 1);
        String amountStr = (String) model.getValueAt(modelRow, 2);
        String method = (String) model.getValueAt(modelRow, 3);
        String details = (String) model.getValueAt(modelRow, 4);
        Object dateStr = model.getValueAt(modelRow, 5);
        String status = (String) model.getValueAt(modelRow, 6);

        JDialog receiptDialog = new JDialog(this, "Transaction Receipt #" + paymentId, true);
        receiptDialog.setSize(420, 400);
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

        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("           TRANSACTION RECEIPT\n");
        sb.append("=========================================\n");
        sb.append("Receipt ID    : #").append(paymentId).append("\n");
        sb.append("Date          : ").append(dateStr).append("\n");
        sb.append("Student ID    : ").append(Session.getCurrentStudent().getStudentId()).append("\n");
        sb.append("Student Name  : ").append(Session.getCurrentStudent().getName()).append("\n");
        sb.append("Payment Type  : ").append(type).append("\n");
        if (!details.isEmpty()) sb.append("Details       : ").append(details).append("\n");
        sb.append("Payment Method: ").append(method).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append("Amount Paid   : ").append(amountStr).append("\n");
        sb.append("Status        : ").append(status).append("\n");
        sb.append("=========================================\n");
        sb.append("       Official University Record");

        receiptText.setText(sb.toString());

        JButton btnClose = UITheme.createPrimaryButton("Close");
        btnClose.addActionListener(e -> receiptDialog.dispose());

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(new JScrollPane(receiptText), BorderLayout.CENTER);
        panel.add(btnClose, BorderLayout.SOUTH);

        receiptDialog.add(panel);
        receiptDialog.setVisible(true);
    }
}