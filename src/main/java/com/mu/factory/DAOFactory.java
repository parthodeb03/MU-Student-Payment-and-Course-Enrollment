package com.mu.factory;

import com.mu.dao.AdminDAO;
import com.mu.dao.CourseDAO;
import com.mu.dao.EnrollmentDAO;
import com.mu.dao.PaymentDAO;
import com.mu.dao.StudentDAO;

public final class DAOFactory {

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
