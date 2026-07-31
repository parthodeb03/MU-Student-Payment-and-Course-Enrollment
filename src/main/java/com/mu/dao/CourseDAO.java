package com.mu.dao;

import com.mu.config.DBConnection;
import com.mu.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
// Add Course
    public boolean addCourse(Course course) {

        String sql = "INSERT INTO courses(course_name, credit) VALUES(?, ?)";

        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql)) {

            ps.setString(1, course.getCourseName());
            ps.setInt(2, course.getCredit());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Update Course
    public boolean updateCourse(Course course) {

        String sql = "UPDATE courses SET course_name=?, credit=? WHERE course_id=?";

        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql)) {

            ps.setString(1, course.getCourseName());
            ps.setInt(2, course.getCredit());
            ps.setInt(3, course.getCourseId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete Course
    public boolean deleteCourse(int courseId) {

        String sql = "DELETE FROM courses WHERE course_id=?";

        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql)) {

            ps.setInt(1, courseId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Get Course By ID
    public Course getCourseById(int courseId) {

        String sql = "SELECT * FROM courses WHERE course_id=?";

        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql)) {

            ps.setInt(1, courseId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("credit")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // View All Courses
    public List<Course> getAllCourses() {

        List<Course> courses = new ArrayList<>();

        String sql = "SELECT * FROM courses ORDER BY course_id";

        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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

}