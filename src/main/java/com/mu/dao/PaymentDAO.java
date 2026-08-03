package com.mu.dao;

import com.mu.config.DBConnection;
import com.mu.model.Payment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public boolean makePayment(Payment payment) {
        String sql = """
                INSERT INTO payments
                (student_id, amount, payment_method, payment_date, payment_type, month, year, term_name, status, reference_message)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, payment.getStudentId());
            ps.setDouble(2, payment.getAmount());
            ps.setString(3, payment.getPaymentMethod());
            ps.setDate(4, Date.valueOf(payment.getPaymentDate()));
            ps.setString(5, payment.getPaymentType());
            ps.setString(6, payment.getMonth());
            ps.setString(7, payment.getYear());
            ps.setString(8, payment.getTermName());
            ps.setString(9, payment.getStatus());
            ps.setString(10, payment.getReferenceMessage());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Payment> getPaymentHistory(String studentId) {
        List<Payment> payments = new ArrayList<>();
        String sql = """
                SELECT p.*, s.university_id
                FROM payments p
                JOIN students s ON p.student_id = s.student_id
                WHERE p.student_id=?
                ORDER BY p.payment_date DESC
                """;

        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) payments.add(extractPayment(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = """
                SELECT p.*, s.university_id
                FROM payments p
                JOIN students s ON p.student_id = s.student_id
                ORDER BY p.payment_date DESC
                """;

        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) payments.add(extractPayment(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public List<Payment> getPendingPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = """
                SELECT p.*, s.university_id
                FROM payments p
                JOIN students s ON p.student_id = s.student_id
                WHERE p.status='PENDING'
                ORDER BY p.payment_date DESC
                """;

        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) payments.add(extractPayment(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public boolean approvePayment(int paymentId) {
        String sql = "UPDATE payments SET status='APPROVED' WHERE payment_id=?";
        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getTotalPaid(String studentId) {
        String sql = "SELECT SUM(amount) FROM payments WHERE student_id=? AND status='APPROVED'";
        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getFeeAmount(String feeType) {
        String sql = "SELECT amount FROM fee_structure WHERE fee_type = ?";
        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, feeType);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Payment extractPayment(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getInt("payment_id"),
                rs.getString("student_id"),
                rs.getDouble("amount"),
                rs.getString("payment_method"),
                rs.getDate("payment_date").toLocalDate(),
                rs.getString("payment_type"),
                rs.getString("month"),
                rs.getString("year"),
                rs.getString("term_name"),
                rs.getString("status"),
                rs.getString("reference_message")
        );
    }
}