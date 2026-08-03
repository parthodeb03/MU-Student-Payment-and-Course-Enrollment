package com.mu.dao;

import com.mu.config.DBConnection;
import com.mu.model.Admin;
import com.mu.model.Payment;
import com.mu.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public Admin login(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username=? AND password=?";
        Connection con = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Admin(
                        rs.getInt("admin_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM admin WHERE username=?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking admin username: " + e.getMessage());
            return false;
        }
    }

    public boolean register(Admin admin) {
        String sql = "INSERT INTO admin(username, password) VALUES (?, ?)";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registering admin: " + e.getMessage());
            return false;
        }
    }

    public int getTotalStudents() {
        String sql = "SELECT COUNT(*) FROM students";
        Connection con = DBConnection.getInstance().getConnection();

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalPayments() {
        String sql = "SELECT SUM(amount) FROM payments WHERE status='APPROVED'";
        Connection con = DBConnection.getInstance().getConnection();

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";
        Connection con = DBConnection.getInstance().getConnection();

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getString("student_id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setDepartment(rs.getString("department"));
                student.setBatch(rs.getString("batch"));
                student.setStudentId(rs.getString("student_id"));
                student.setPhone(rs.getString("phone"));
                list.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY payment_date DESC";
        Connection con = DBConnection.getInstance().getConnection();

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) list.add(extractPayment(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Payment> getPendingPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE status='PENDING' ORDER BY payment_date DESC";
        Connection con = DBConnection.getInstance().getConnection();

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) list.add(extractPayment(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean approvePayment(int paymentId) {
        String sql = "UPDATE payments SET status='APPROVED' WHERE payment_id=?";
        Connection con = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
