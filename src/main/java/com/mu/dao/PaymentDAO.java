package com.mu.dao;

import com.mu.config.DBConnection;
import com.mu.model.Payment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    private final Connection connection;

    public PaymentDAO() {
        connection = DBConnection.getInstance().getConnection();
    }

    // Make Payment
    public boolean makePayment(Payment payment) {

        String sql = """
                INSERT INTO payments
                (student_id, amount, payment_method, payment_date)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, payment.getStudentId());
            ps.setDouble(2, payment.getAmount());
            ps.setString(3, payment.getPaymentMethod());
            ps.setDate(4, Date.valueOf(payment.getPaymentDate()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // View Payment History of One Student
    public List<Payment> getPaymentHistory(int studentId) {

        List<Payment> payments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM payments
                WHERE student_id=?
                ORDER BY payment_date DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                payments.add(new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("student_id"),
                        rs.getDouble("amount"),
                        rs.getString("payment_method"),
                        rs.getDate("payment_date").toLocalDate()
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    // View All Payments (Admin)
    public List<Payment> getAllPayments() {

        List<Payment> payments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM payments
                ORDER BY payment_date DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                payments.add(new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("student_id"),
                        rs.getDouble("amount"),
                        rs.getString("payment_method"),
                        rs.getDate("payment_date").toLocalDate()
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    // Search Payments by Student
    public List<Payment> searchPaymentsByStudent(int studentId) {

        return getPaymentHistory(studentId);

    }

    // Total Paid by Student (Optional)
    public double getTotalPaid(int studentId) {

        String sql =
                "SELECT SUM(amount) FROM payments WHERE student_id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getDouble(1);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return 0;

    }

}