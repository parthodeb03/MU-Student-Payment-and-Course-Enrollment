package com.mu.dao;

import com.mu.config.DBConnection;
import com.mu.model.Admin;
import com.mu.model.Payment;
import com.mu.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    // -------------------------
    // Admin Login
    // -------------------------
    public Admin login(String username, String password) {

        String sql = "SELECT * FROM admin WHERE username=? AND password=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

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

    // -------------------------
    // Admin Registration
    // -------------------------
    public boolean register(Admin admin) {

        String sql = "INSERT INTO admin(username,password) VALUES(?,?)";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;
    }

    // -------------------------
    // Total Students
    // -------------------------
    public int getTotalStudents() {

        String sql = "SELECT COUNT(*) FROM students";

        try (Connection con = DBConnection.getInstance().getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // -------------------------
    // Total Payment Amount
    // -------------------------
    public double getTotalPayments() {

        String sql = "SELECT SUM(amount) FROM payments";

        try (Connection con = DBConnection.getInstance().getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // -------------------------
    // View Students
    // -------------------------
    public List<Student> getAllStudents() {

        List<Student> list = new ArrayList<>();

        String sql = "SELECT * FROM students";

        try (Connection con = DBConnection.getInstance().getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                Student student = new Student();

                student.setStudentId(rs.getInt("student_id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));

                list.add(student);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // -------------------------
    // View Payments
    // -------------------------
    public List<Payment> getAllPayments() {

        List<Payment> list = new ArrayList<>();

        String sql = "SELECT * FROM payments";

        try (Connection con = DBConnection.getInstance().getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                Payment payment = new Payment();

                payment.setPaymentId(rs.getInt("payment_id"));
                payment.setStudentId(rs.getInt("student_id"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setPaymentMethod(rs.getString("payment_method"));
                payment.setPaymentDate(rs.getDate("payment_date").toLocalDate());

                list.add(payment);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}