DROP DATABASE IF EXISTS mu_course_enrollment;
CREATE DATABASE mu_course_enrollment;
USE mu_course_enrollment;

-- =========================
-- Departments
-- =========================
CREATE TABLE departments (
                             dept_id VARCHAR(100) PRIMARY KEY,
                             dept_code VARCHAR(20) UNIQUE NOT NULL,
                             dept_name VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- =========================
-- Students
-- =========================
CREATE TABLE students (
                          student_id VARCHAR(100) PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          password VARCHAR(100) NOT NULL,
                          department VARCHAR(50) NOT NULL,
                          batch VARCHAR(20) NOT NULL,
                          phone VARCHAR(20) NOT NULL
) ENGINE=InnoDB;

-- =========================
-- Admin
-- =========================
CREATE TABLE admin (
                       admin_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- =========================
-- Courses
-- =========================
CREATE TABLE courses (
                         course_id INT AUTO_INCREMENT PRIMARY KEY,
                         course_name VARCHAR(100) NOT NULL,
                         credit INT NOT NULL
) ENGINE=InnoDB;

-- =========================
-- Enrollments
-- =========================
CREATE TABLE enrollments (
                             enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
                             student_id VARCHAR(100) NOT NULL,
                             course_id INT NOT NULL,

                             CONSTRAINT fk_enroll_student
                                 FOREIGN KEY (student_id)
                                     REFERENCES students(student_id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_enroll_course
                                 FOREIGN KEY (course_id)
                                     REFERENCES courses(course_id)
                                     ON DELETE CASCADE,

                             UNIQUE(student_id, course_id)
) ENGINE=InnoDB;

-- =========================
-- Payments
-- =========================
CREATE TABLE payments (
                          payment_id INT AUTO_INCREMENT PRIMARY KEY,
                          student_id VARCHAR(100) NOT NULL,
                          amount DOUBLE NOT NULL,
                          payment_method VARCHAR(50) NOT NULL,
                          payment_date DATE NOT NULL,
                          payment_type VARCHAR(50) NOT NULL,
                          month VARCHAR(20),
                          year VARCHAR(10),
                          term_name VARCHAR(50),
                          status VARCHAR(20) DEFAULT 'PENDING',
                          reference_message TEXT,

                          CONSTRAINT fk_payment_student
                              FOREIGN KEY (student_id)
                                  REFERENCES students(student_id)
                                  ON DELETE CASCADE
) ENGINE=InnoDB;

-- =========================
-- Fee Structure
-- =========================
CREATE TABLE fee_structure (
                               fee_id INT AUTO_INCREMENT PRIMARY KEY,
                               fee_type VARCHAR(100) UNIQUE NOT NULL,
                               amount DOUBLE NOT NULL,
                               description VARCHAR(255)
) ENGINE=InnoDB;

-- =========================
-- Departments
-- =========================
INSERT IGNORE INTO departments (dept_id, dept_code, dept_name) VALUES
                                                                   ('D001','CSE','Computer Science & Engineering'),
                                                                   ('D002','EEE','Electrical & Electronic Engineering'),
                                                                   ('D003','SWE','Software Engineering'),
                                                                   ('D004','BBA','Bachelor of Business Administration'),
                                                                   ('D005','ECO','Economics'),
                                                                   ('D006','ENGLISH','English'),
                                                                   ('D007','DATA SCIENCE','Data Science'),
                                                                   ('D008','LAW','Law');

-- =========================
-- Admin
-- =========================
INSERT IGNORE INTO admin(username,password)
VALUES ('admin','admin123');

-- =========================
-- Courses
-- =========================
INSERT IGNORE INTO courses(course_name,credit) VALUES
                                                   ('Object Oriented Programming',3),
                                                   ('Data Structures',3),
                                                   ('Software Engineering',3),
                                                   ('Database Management System',3),
                                                   ('Operating System',3);

-- =========================
-- Fee Structure
-- =========================
INSERT IGNORE INTO fee_structure(fee_type,amount,description) VALUES
                                                                  ('Monthly Tuition Fee',100.00,'Monthly tuition fee'),
                                                                  ('Monthly Campus Activities Fee',25.00,'Monthly campus activities fee'),
                                                                  ('New Term Admission Fee',200.00,'Admission fee for new term');