package com.mu.dao;

import com.mu.config.DBConnection;
import com.mu.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean register(Student student) {

        String sql = "INSERT INTO students(name, email, password) VALUES (?, ?, ?)";

        try (
                Connection con = DBConnection.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            if (con == null) {
                return false;
            }

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPassword());

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

            if (con == null) {
                return null;
            }

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Student student = new Student();

                    student.setStudentId(rs.getInt("student_id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setPassword(rs.getString("password"));

                    return student;
                }
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

            if (con == null) {
                return students;
            }

            while (rs.next()) {

                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error loading students: " + e.getMessage());
        }

        return students;
    }

    public boolean existsByEmail(String email) {

        String sql = "SELECT 1 FROM students WHERE email=?";

        try (
                Connection con = DBConnection.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            if (con == null) {
                return false;
            }

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
        }

        return false;
    }
}