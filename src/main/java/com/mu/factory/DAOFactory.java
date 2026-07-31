package com.mu.factory;

import com.mu.dao.AdminDAO;
import com.mu.dao.CourseDAO;
import com.mu.dao.EnrollmentDAO;
import com.mu.dao.PaymentDAO;
import com.mu.dao.StudentDAO;

/**
 * DAOFactory – Factory Design Pattern
 *
 * WHY: Centralises the creation of all DAO objects so that the rest of the
 *      codebase never uses "new XxxDAO()" directly.  If we ever swap to a
 *      different data source (e.g., in-memory DB for tests), we only change
 *      this factory.
 *
 * WHEN TO USE: Any time a service layer or other component needs a DAO
 *              instance.  This avoids tight coupling to concrete classes.
 */
public final class DAOFactory {

    // Prevent instantiation
    private DAOFactory() { }

    public static AdminDAO createAdminDAO() {
        return new AdminDAO();
    }

    public static StudentDAO createStudentDAO() {
        return new StudentDAO();
    }

    public static CourseDAO createCourseDAO() {
        return new CourseDAO();
    }

    public static PaymentDAO createPaymentDAO() {
        return new PaymentDAO();
    }

    public static EnrollmentDAO createEnrollmentDAO() {
        return new EnrollmentDAO();
    }
}
