package com.mu.service;

import com.mu.dao.PaymentDAO;
import com.mu.factory.PaymentFactory;
import com.mu.factory.PaymentStrategy;
import com.mu.model.Payment;
import com.mu.observer.NotificationService;

import java.time.LocalDate;
import java.util.List;

public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final NotificationService notificationService;

    public PaymentService() {

        paymentDAO = new PaymentDAO();
        notificationService = new NotificationService();

    }

    public boolean makePayment(int studentId,
                               double amount,
                               String paymentMethod) {

        if (studentId <= 0) {
            throw new IllegalArgumentException("Invalid Student ID.");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method is required.");
        }

        // Factory Pattern
        PaymentStrategy strategy =
                PaymentFactory.createPayment(paymentMethod);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported payment method.");
        }

        strategy.pay(amount);

        Payment payment = new Payment();

        payment.setStudentId(studentId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentDate(LocalDate.now());

        boolean success = paymentDAO.makePayment(payment);

        if (success) {

            notificationService.notifyObservers(
                    "Payment completed successfully."
            );

        }

        return success;

    }

    public List<Payment> getPaymentHistory(int studentId) {

        return paymentDAO.getPaymentHistory(studentId);

    }

    public double getTotalPaid(int studentId) {

        return paymentDAO.getTotalPaid(studentId);

    }

}