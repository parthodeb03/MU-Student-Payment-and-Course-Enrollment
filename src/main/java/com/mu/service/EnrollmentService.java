package com.mu.service;

import com.mu.dao.EnrollmentDAO;
import com.mu.observer.NotificationService;

import java.util.List;

public class EnrollmentService {

    private final EnrollmentDAO enrollmentDAO;
    private final NotificationService notificationService;

    public EnrollmentService() {

        enrollmentDAO = new EnrollmentDAO();
        notificationService = new NotificationService();

    }

    public boolean enrollCourse(int studentId, int courseId) {

        if (studentId <= 0) {
            throw new IllegalArgumentException("Invalid Student ID.");
        }

        if (courseId <= 0) {
            throw new IllegalArgumentException("Invalid Course ID.");
        }

        if (enrollmentDAO.isAlreadyEnrolled(studentId, courseId)) {
            throw new IllegalArgumentException("You are already enrolled in this course.");
        }

        if (enrollmentDAO.countEnrolledCourses(studentId) >= 3) {
            throw new IllegalArgumentException("Maximum 3 courses allowed.");
        }

        boolean success = enrollmentDAO.enrollCourse(studentId, courseId);

        if (success) {

            notificationService.notifyObservers(
                    "Course enrollment completed successfully."
            );

        }

        return success;
    }

    public List<com.mu.model.Course> getEnrolledCourses(int studentId) {

        return enrollmentDAO.getEnrolledCourses(studentId);

    }

    public boolean dropCourse(int studentId, int courseId) {

        return enrollmentDAO.dropCourse(studentId, courseId);

    }

}