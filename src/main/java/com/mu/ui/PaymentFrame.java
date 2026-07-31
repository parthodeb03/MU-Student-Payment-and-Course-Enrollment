package com.mu.ui;

import com.mu.service.PaymentService;
import com.mu.util.Session;

import javax.swing.*;
import java.awt.*;

public class PaymentFrame extends JFrame {

    private JTextField txtAmount;
    private JComboBox<String> cmbMethod;

    private JButton btnPay;
    private JButton btnBack;

    private PaymentService paymentService;

    public PaymentFrame() {

        paymentService = new PaymentService();

        setTitle("Tuition Payment");

        setSize(450,300);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);

        JLabel lblTitle = new JLabel("Tuition Payment");

        lblTitle.setFont(new Font("Arial",Font.BOLD,20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        add(lblTitle, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;

        add(new JLabel("Amount:"), gbc);

        txtAmount = new JTextField(15);

        gbc.gridx = 1;

        add(txtAmount, gbc);

        gbc.gridx = 0;

        gbc.gridy++;

        add(new JLabel("Payment Method:"), gbc);

        cmbMethod = new JComboBox<>();

        cmbMethod.addItem("Cash");
        cmbMethod.addItem("Bkash");
        cmbMethod.addItem("Card");

        gbc.gridx = 1;

        add(cmbMethod, gbc);

        btnPay = new JButton("Pay");

        btnBack = new JButton("Back");

        JPanel panel = new JPanel();

        panel.add(btnPay);
        panel.add(btnBack);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        add(panel, gbc);

        btnPay.addActionListener(e -> makePayment());

        btnBack.addActionListener(e -> {

            new DashboardFrame();

            dispose();

        });

        setVisible(true);

    }

    private void makePayment() {

        try {

            double amount =
                    Double.parseDouble(txtAmount.getText());

            String method =
                    cmbMethod.getSelectedItem().toString();

            boolean success =
                    paymentService.makePayment(

                            Session.getCurrentStudent().getStudentId(),

                            amount,

                            method

                    );

            if(success){

                JOptionPane.showMessageDialog(

                        this,

                        "Payment Successful."

                );

                txtAmount.setText("");

            }else{

                JOptionPane.showMessageDialog(

                        this,

                        "Payment Failed."

                );

            }

        }catch(NumberFormatException ex){

            JOptionPane.showMessageDialog(

                    this,

                    "Enter a valid amount."

            );

        }catch(Exception ex){

            JOptionPane.showMessageDialog(

                    this,

                    ex.getMessage()

            );

        }

    }

}