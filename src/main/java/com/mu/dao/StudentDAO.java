package com.mu.dao;

import com.mu.config.DBConnection;
import com.mu.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean register(Student student) {
        String sql = "INSERT INTO students(name, email, password, department, batch, student_id, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DBConnection.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            if (con == null) return false;

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPassword());
            ps.setString(4, student.getDepartment());
            ps.setString(5, student.getBatch());
            ps.setString(6, student.getStudentId());   // FIXED: was getStudentId()
            ps.setString(7, student.getPhone());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error registering student: " + e.getMessage());
            return false;
        }
    }

    public Student login(String email, String password) {
        String sql = "SELECT * FROM students WHERE email=? AND password=?";

        try (
                Connection con = DBConnection.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            if (con == null) return null;

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractStudent(rs);
            }

        } catch (SQLException e) {
            System.err.println("Login Error: " + e.getMessage());
        }
        return null;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (
                Connection con = DBConnection.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (con == null) return students;

            while (rs.next()) students.add(extractStudent(rs));

        } catch (SQLException e) {
            System.err.println("Error loading students: " + e.getMessage());
        }
        return students;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM students WHERE email=?";   // FIXED: was "SELECT student"
        try (
                Connection con = DBConnection.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            if (con == null) return false;
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
        }
        return false;
    }

    public boolean existsByUniversityId(String universityId) {
        String sql = "SELECT 1 FROM students WHERE student_id=?";
        try (
                Connection con = DBConnection.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            if (con == null) return false;
            ps.setString(1, universityId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            System.err.println("Error checking university ID: " + e.getMessage());
        }
        return false;
    }

    public List<String> getAllDepartments() {
        List<String> departments = new ArrayList<>();
        String sql = "SELECT dept_code FROM departments ORDER BY dept_code";

        try (
                Connection con = DBConnection.getInstance().getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ) {
            while (rs.next()) departments.add(rs.getString("dept_code"));
        } catch (SQLException e) {
            System.err.println("Error loading departments: " + e.getMessage());
        }
        return departments;
    }

    private Student extractStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getString("student_id"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        student.setPassword(rs.getString("password"));
        student.setDepartment(rs.getString("department"));
        student.setBatch(rs.getString("batch"));
        student.setPhone(rs.getString("phone"));
        return student;
    }
}