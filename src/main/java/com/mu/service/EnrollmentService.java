package com.mu.service;

import com.mu.dao.EnrollmentDAO;
import com.mu.observer.NotificationService;

import java.util.List;

public class EnrollmentService {

    private final EnrollmentDAO enrollmentDAO;
    private final NotificationService notificationService;

    public EnrollmentService() {

        enrollmentDAO = com.mu.factory.DAOFactory.createEnrollmentDAO();
        notificationService = new NotificationService();

    }

    public boolean enrollCourse(String studentId, int courseId) {

        if (studentId==null) {
            throw new IllegalArgumentException("Invalid Student ID.");
        }

        if (courseId <= 0) {
            throw new IllegalArgumentException("Invalid Course ID.");
        }

        if (enrollmentDAO.isAlreadyEnrolled(studentId, courseId)) {
            throw new IllegalArgumentException("You are already enrolled in this course.");
        }

        if (enrollmentDAO.countEnrolledCourses(studentId) >= 6) {
            throw new IllegalArgumentException("Maximum 6 courses allowed.");
        }

        boolean success = enrollmentDAO.enrollCourse(studentId, courseId);

        if (success) {

            notificationService.notifyObservers(
                    "Course enrollment completed successfully."
            );

        }

        return success;
    }

    public List<com.mu.model.Course> getEnrolledCourses(String studentId) {

        return enrollmentDAO.getEnrolledCourses(studentId);

    }

    public boolean dropCourse(String studentId, int courseId) {

        return enrollmentDAO.dropCourse(studentId, courseId);

    }

}