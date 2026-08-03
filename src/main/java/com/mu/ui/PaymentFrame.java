package com.mu.ui;

import com.mu.model.Student;
import com.mu.service.PaymentService;
import com.mu.ui.theme.UITheme;
import com.mu.util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class PaymentFrame extends JFrame {

    private JComboBox<String> cmbPaymentType;
    private JTextField txtMonth;
    private JTextField txtYear;
    private JTextField txtTermName;
    private JLabel lblPeriod;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private JLabel lblCalculatedAmount;
    private JComboBox<String> cmbPaymentMethod;
    private JTextArea txtReferenceMessage;
    private JButton btnPay;
    private JButton btnBack;

    private final PaymentService paymentService;

    public PaymentFrame() {
        paymentService = new PaymentService();

        setTitle("Make Payment");
        setSize(560, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        JPanel headerPanel = UITheme.createCardPanel();
        headerPanel.setLayout(new BorderLayout(8, 8));

        JLabel lblTitle = UITheme.createLabel("Fee Payment", UITheme.TITLE_FONT, UITheme.PRIMARY_COLOR);
        JLabel lblSubtitle = UITheme.createLabel("Student: " + Session.getCurrentStudent().getName(), UITheme.SUBHEADER_FONT, UITheme.TEXT_MUTED);
        JLabel lblHint = UITheme.createLabel("Select fee type, fill the details, and complete your payment.", UITheme.SMALL_FONT, UITheme.TEXT_MUTED);

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSubtitle, BorderLayout.CENTER);
        headerPanel.add(lblHint, BorderLayout.SOUTH);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblType = new JLabel("Payment Type:");
        lblType.setFont(UITheme.BODY_BOLD);
        formCard.add(lblType, gbc);

        gbc.gridx = 1;
        cmbPaymentType = new JComboBox<>(new String[]{
                "Monthly Tuition Fee",
                "Monthly Campus Activities Fee",
                "New Term Admission Fee"
        });
        cmbPaymentType.setFont(UITheme.BODY_FONT);
        cmbPaymentType.setBackground(UITheme.SURFACE_COLOR);
        cmbPaymentType.addActionListener(e -> updateFormFields());
        formCard.add(cmbPaymentType, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        lblPeriod = new JLabel("Month / Year:");
        lblPeriod.setFont(UITheme.BODY_BOLD);
        formCard.add(lblPeriod, gbc);

        gbc.gridx = 1;
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        JPanel monthlyCard = new JPanel(new GridLayout(1, 2, 8, 0));
        monthlyCard.setOpaque(false);
        txtMonth = createPlainTextField();
        txtMonth.setToolTipText("Month");
        txtYear = createPlainTextField();
        txtYear.setToolTipText("Year");
        monthlyCard.add(txtMonth);
        monthlyCard.add(txtYear);

        JPanel termCard = new JPanel(new BorderLayout());
        termCard.setOpaque(false);
        txtTermName = createPlainTextField();
        termCard.add(txtTermName, BorderLayout.CENTER);

        cardPanel.add(monthlyCard, "MONTHLY");
        cardPanel.add(termCard, "TERM");
        formCard.add(cardPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblAmount = new JLabel("Calculated Amount:");
        lblAmount.setFont(UITheme.BODY_BOLD);
        formCard.add(lblAmount, gbc);
        gbc.gridx = 1;
        lblCalculatedAmount = UITheme.createLabel("$0.00", UITheme.HEADER_FONT, UITheme.SUCCESS_COLOR);
        formCard.add(lblCalculatedAmount, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblMethod = new JLabel("Payment Method:");
        lblMethod.setFont(UITheme.BODY_BOLD);
        formCard.add(lblMethod, gbc);
        gbc.gridx = 1;
        cmbPaymentMethod = new JComboBox<>(new String[]{"Cash", "bKash", "Card"});
        cmbPaymentMethod.setFont(UITheme.BODY_FONT);
        cmbPaymentMethod.setBackground(UITheme.SURFACE_COLOR);
        cmbPaymentMethod.addActionListener(e -> updateReferenceMessage());
        formCard.add(cmbPaymentMethod, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblRef = new JLabel("Reference:");
        lblRef.setFont(UITheme.BODY_BOLD);
        formCard.add(lblRef, gbc);
        gbc.gridx = 1;
        txtReferenceMessage = new JTextArea(2, 20);
        txtReferenceMessage.setFont(UITheme.BODY_FONT);
        txtReferenceMessage.setBackground(UITheme.BACKGROUND_COLOR);
        txtReferenceMessage.setEditable(false);
        txtReferenceMessage.setFocusable(false);
        txtReferenceMessage.setLineWrap(true);
        txtReferenceMessage.setWrapStyleWord(true);
        txtReferenceMessage.setBorder(BorderFactory.createLineBorder(UITheme.TEXT_MUTED));
        formCard.add(new JScrollPane(txtReferenceMessage), gbc);

        mainPanel.add(formCard, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(UITheme.BACKGROUND_COLOR);
        btnPay = UITheme.createPrimaryButton("Process Payment");
        btnBack = UITheme.createOutlineButton("Back to Dashboard");
        actionPanel.add(btnPay);
        actionPanel.add(btnBack);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        btnPay.addActionListener(e -> makePayment());
        btnBack.addActionListener(e -> { new DashboardFrame(); dispose(); });

        updateFormFields();
        setVisible(true);
    }

    private JTextField createPlainTextField() {
        JTextField field = new JTextField(15);
        field.setFont(UITheme.BODY_FONT);
        field.setBackground(Color.WHITE);
        field.setForeground(UITheme.TEXT_DARK);
        field.setCaretColor(UITheme.TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.TEXT_MUTED),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return field;
    }

    private void updateFormFields() {
        String type = (String) cmbPaymentType.getSelectedItem();
        if (type == null) type = "";

        double amount = 0;
        try {
            amount = paymentService.calculateFee(type);
        } catch (Exception ex) {
            System.err.println("Fee lookup failed: " + ex.getMessage());
        }

        if (amount <= 0) {
            lblCalculatedAmount.setText("Not Configured");
            lblCalculatedAmount.setForeground(UITheme.DANGER_COLOR);
        } else {
            lblCalculatedAmount.setText("$" + String.format("%.2f", amount));
            lblCalculatedAmount.setForeground(UITheme.SUCCESS_COLOR);
        }

        boolean isMonthly = type.contains("Monthly");
        boolean isTerm = type.contains("Term");

        if (isMonthly) {
            lblPeriod.setText("Month / Year:");
            cardLayout.show(cardPanel, "MONTHLY");
            txtMonth.setText("");
            txtYear.setText("");
        } else if (isTerm) {
            lblPeriod.setText("Term Name:");
            cardLayout.show(cardPanel, "TERM");
            txtTermName.setText("");
        }

        updateReferenceMessage();
    }

    private void updateReferenceMessage() {
        String method = (String) cmbPaymentMethod.getSelectedItem();
        if ("bKash".equalsIgnoreCase(method)) {
            Student s = Session.getCurrentStudent();
            txtReferenceMessage.setText(s.getName() + "-" + s.getDepartment() + "-" + s.getBatch() + "-" + s.getStudentId());
        } else {
            txtReferenceMessage.setText("");
        }
    }

    private void makePayment() {
        try {
            String paymentType = (String) cmbPaymentType.getSelectedItem();
            String paymentMethod = (String) cmbPaymentMethod.getSelectedItem();
            String month = txtMonth.getText().trim();
            String year = txtYear.getText().trim();
            String termName = txtTermName.getText().trim();
            String studentId = Session.getCurrentStudent().getStudentId();

            double amount = paymentService.calculateFee(paymentType);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Fee amount is not configured for this payment type. Contact admin.", "Fee Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (paymentType.contains("Monthly") && (month.isEmpty() || year.isEmpty())) {
                JOptionPane.showMessageDialog(this, "Please enter month and year.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (paymentType.contains("Term") && termName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter term name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String reference = txtReferenceMessage.getText().trim();
            boolean success = paymentService.makePayment(studentId, paymentType, month, year, termName, paymentMethod, reference);

            if (success) {
                showReceiptDialog(amount, paymentMethod, paymentType, month, year, termName);
                new DashboardFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Payment processing failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showReceiptDialog(double amount, String paymentMethod, String paymentType,
                                   String month, String year, String termName) {
        JDialog receiptDialog = new JDialog(this, "Payment Receipt", true);
        receiptDialog.setSize(420, 420);
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
        sb.append("           OFFICIAL PAYMENT RECEIPT\n");
        sb.append("=========================================\n");
        sb.append("Date          : ").append(LocalDate.now()).append("\n");
        sb.append("Student ID    : ").append(Session.getCurrentStudent().getStudentId()).append("\n");
        sb.append("Student Name  : ").append(Session.getCurrentStudent().getName()).append("\n");
        sb.append("Payment Type  : ").append(paymentType).append("\n");
        if (!month.isEmpty()) sb.append("Month         : ").append(month).append("\n");
        if (!year.isEmpty()) sb.append("Year          : ").append(year).append("\n");
        if (!termName.isEmpty()) sb.append("Term          : ").append(termName).append("\n");
        sb.append("Payment Method: ").append(paymentMethod).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append("Amount Paid   : $").append(String.format("%.2f", amount)).append("\n");
        sb.append("Status        : PENDING (Awaiting Approval)\n");
        sb.append("=========================================\n");
        sb.append("       Thank you for your payment!");

        receiptText.setText(sb.toString());

        JButton btnClose = UITheme.createPrimaryButton("Close Receipt");
        btnClose.addActionListener(e -> receiptDialog.dispose());

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(new JScrollPane(receiptText), BorderLayout.CENTER);
        panel.add(btnClose, BorderLayout.SOUTH);

        receiptDialog.add(panel);
        receiptDialog.setVisible(true);
    }
}