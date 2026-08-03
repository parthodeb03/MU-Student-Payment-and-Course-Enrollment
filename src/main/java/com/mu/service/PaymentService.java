package com.mu.service;

import com.mu.dao.PaymentDAO;
import com.mu.factory.PaymentFactory;
import com.mu.factory.PaymentStrategy;
import com.mu.model.Payment;

import java.time.LocalDate;
import java.util.List;

public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final com.mu.observer.NotificationService notificationService;

    public PaymentService() {
        paymentDAO = com.mu.factory.DAOFactory.createPaymentDAO();
        notificationService = new com.mu.observer.NotificationService();
    }

    public PaymentService(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
        this.notificationService = new com.mu.observer.NotificationService();
    }

    public boolean makePayment(String studentId, String paymentType, String month, String year,
                               String termName, String paymentMethod, String referenceMessage) {

        if (studentId == null || studentId.trim().isEmpty()) throw new IllegalArgumentException("Invalid Student ID.");
        if (paymentType == null || paymentType.trim().isEmpty()) throw new IllegalArgumentException("Payment type is required.");
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) throw new IllegalArgumentException("Payment method is required.");

        double amount = calculateFee(paymentType);
        if (amount <= 0) throw new IllegalArgumentException("Fee amount not configured for this payment type.");

        PaymentStrategy strategy = PaymentFactory.createPayment(paymentMethod);
        if (strategy == null) throw new IllegalArgumentException("Unsupported payment method.");

        strategy.pay(amount);

        Payment payment = new Payment();
        payment.setStudentId(studentId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentType(paymentType);
        payment.setMonth(month);
        payment.setYear(year);
        payment.setTermName(termName);
        payment.setStatus("PENDING");
        payment.setReferenceMessage(referenceMessage);

        boolean success = paymentDAO.makePayment(payment);

        if (success) {
            notificationService.notifyObservers("Payment submitted successfully. Awaiting admin approval.");
        }
        return success;
    }

    public double calculateFee(String paymentType) {
        if (paymentType == null || paymentType.trim().isEmpty()) return 0;
        return paymentDAO.getFeeAmount(paymentType);
    }

    public List<Payment> getPaymentHistory(String studentId) {
        return paymentDAO.getPaymentHistory(studentId);
    }

    public double getTotalPaid(String studentId) {
        return paymentDAO.getTotalPaid(studentId);
    }

    public List<Payment> getPendingPayments() {
        return paymentDAO.getPendingPayments();
    }

    public boolean approvePayment(int paymentId) {
        return paymentDAO.approvePayment(paymentId);
    }
}