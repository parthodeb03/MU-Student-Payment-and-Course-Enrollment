package com.mu.model;

import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private String studentId;
    private double amount;
    private String paymentMethod;
    private LocalDate paymentDate;
    private String paymentType;
    private String month;
    private String year;
    private String termName;
    private String status;
    private String referenceMessage;

    public Payment() {}

    public Payment(int paymentId, String studentId, double amount, String paymentMethod,
                   LocalDate paymentDate, String paymentType, String month, String year,
                   String termName, String status, String referenceMessage) {
        this.paymentId = paymentId;
        this.studentId = studentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
        this.month = month;
        this.year = year;
        this.termName = termName;
        this.status = status;
        this.referenceMessage = referenceMessage;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getTermName() { return termName; }
    public void setTermName(String termName) { this.termName = termName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReferenceMessage() { return referenceMessage; }
    public void setReferenceMessage(String referenceMessage) { this.referenceMessage = referenceMessage; }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", studentId='" + studentId + '\'' +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentDate=" + paymentDate +
                ", paymentType='" + paymentType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}