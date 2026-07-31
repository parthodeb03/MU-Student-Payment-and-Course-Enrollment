package com.mu.ui;

import com.mu.service.PaymentService;
import com.mu.ui.theme.UITheme;
import com.mu.util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class PaymentFrame extends JFrame {

    private JTextField txtAmount;
    private JComboBox<String> cmbPaymentMethod;
    private JButton btnPay;
    private JButton btnBack;

    private final PaymentService paymentService;

    public PaymentFrame() {
        paymentService = new PaymentService();

        setTitle("Make Payment");
        setSize(500, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Header
        JPanel headerPanel = UITheme.createCardPanel();
        headerPanel.setLayout(new BorderLayout(8, 8));

        JLabel lblTitle = UITheme.createLabel("Course Fee Payment", UITheme.TITLE_FONT, UITheme.PRIMARY_COLOR);
        JLabel lblSubtitle = UITheme.createLabel("Student: " + Session.getCurrentStudent().getName(), UITheme.SUBHEADER_FONT, UITheme.TEXT_MUTED);
        JLabel lblHint = UITheme.createLabel("Enter an amount and choose your payment method.", UITheme.SMALL_FONT, UITheme.TEXT_MUTED);

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSubtitle, BorderLayout.CENTER);
        headerPanel.add(lblHint, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Card Panel
        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblAmount = new JLabel("Amount ($):");
        lblAmount.setFont(UITheme.BODY_BOLD);
        formCard.add(lblAmount, gbc);

        gbc.gridx = 1;
        txtAmount = UITheme.createTextField(15);
        formCard.add(txtAmount, gbc);

        // Payment Method
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblMethod = new JLabel("Payment Method:");
        lblMethod.setFont(UITheme.BODY_BOLD);
        formCard.add(lblMethod, gbc);

        gbc.gridx = 1;
        cmbPaymentMethod = new JComboBox<>(new String[]{"Cash", "bKash", "Card"});
        cmbPaymentMethod.setFont(UITheme.BODY_FONT);
        cmbPaymentMethod.setBackground(UITheme.SURFACE_COLOR);
        formCard.add(cmbPaymentMethod, gbc);

        mainPanel.add(formCard, BorderLayout.CENTER);

        // Actions Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(UITheme.BACKGROUND_COLOR);

        btnPay = UITheme.createPrimaryButton("Process Payment");
        btnBack = UITheme.createOutlineButton("Back to Dashboard");

        actionPanel.add(btnPay);
        actionPanel.add(btnBack);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        btnPay.addActionListener(e -> makePayment());
        btnBack.addActionListener(e -> {
            new DashboardFrame();
            dispose();
        });

        setVisible(true);
    }

    private void makePayment() {
        try {
            String amountStr = txtAmount.getText().trim();
            if (amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter payment amount.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double amount = Double.parseDouble(amountStr);
            String paymentMethod = (String) cmbPaymentMethod.getSelectedItem();
            int studentId = Session.getCurrentStudent().getStudentId();

            boolean success = paymentService.makePayment(studentId, amount, paymentMethod);

            if (success) {
                showReceiptDialog(amount, paymentMethod);
                new DashboardFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Payment processing failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showReceiptDialog(double amount, String paymentMethod) {
        JDialog receiptDialog = new JDialog(this, "Payment Receipt", true);
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
                "           OFFICIAL PAYMENT RECEIPT\n" +
                "=========================================\n" +
                "Date          : " + LocalDate.now() + "\n" +
                "Student ID    : " + Session.getCurrentStudent().getStudentId() + "\n" +
                "Student Name  : " + Session.getCurrentStudent().getName() + "\n" +
                "Payment Method: " + paymentMethod + "\n" +
                "-----------------------------------------\n" +
                "Amount Paid   : $" + String.format("%.2f", amount) + "\n" +
                "Status        : SUCCESSFUL / PAID\n" +
                "=========================================\n" +
                "       Thank you for your payment!"
        );

        JButton btnClose = UITheme.createPrimaryButton("Close Receipt");
        btnClose.addActionListener(e -> receiptDialog.dispose());

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(new JScrollPane(receiptText), BorderLayout.CENTER);
        panel.add(btnClose, BorderLayout.SOUTH);

        receiptDialog.add(panel);
        receiptDialog.setVisible(true);
    }
}