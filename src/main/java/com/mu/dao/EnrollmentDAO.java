package com.mu.dao;

import com.mu.config.DBConnection;
import com.mu.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    private final Connection connection;

    public EnrollmentDAO() {
        connection = DBConnection.getInstance().getConnection();
    }

    // Enroll Student in a Course
    public boolean enrollCourse(int studentId, int courseId) {

        if (isAlreadyEnrolled(studentId, courseId)) {
            return false;
        }

        if (countEnrolledCourses(studentId) >= 3) {
            return false;
        }

        String sql = "INSERT INTO enrollments(student_id, course_id) VALUES(?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Check Duplicate Enrollment
    public boolean isAlreadyEnrolled(int studentId, int courseId) {

        String sql = "SELECT * FROM enrollments WHERE student_id=? AND course_id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Count Student Courses
    public int countEnrolledCourses(int studentId) {

        String sql = "SELECT COUNT(*) FROM enrollments WHERE student_id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // View Enrolled Courses
    public List<Course> getEnrolledCourses(int studentId) {

        List<Course> courses = new ArrayList<>();

        String sql = """
                SELECT c.course_id,
                       c.course_name,
                       c.credit
                FROM courses c
                INNER JOIN enrollments e
                ON c.course_id = e.course_id
                WHERE e.student_id = ?
                ORDER BY c.course_name
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                courses.add(new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("credit")
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }

    // Drop Course (Optional)
    public boolean dropCourse(int studentId, int courseId) {

        String sql =
                "DELETE FROM enrollments WHERE student_id=? AND course_id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}