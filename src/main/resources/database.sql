CREATE DATABASE IF NOT EXISTS mu_course_enrollment;

USE mu_course_enrollment;

CREATE TABLE IF NOT EXISTS students (
                                        student_id INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL,
                                        email VARCHAR(100) UNIQUE NOT NULL,
                                        password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS admin (
                                     admin_id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) UNIQUE NOT NULL,
                                     password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS courses (
                                       course_id INT AUTO_INCREMENT PRIMARY KEY,
                                       course_name VARCHAR(100) NOT NULL,
                                       credit INT NOT NULL
);

CREATE TABLE IF NOT EXISTS enrollments (
                                           enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
                                           student_id INT NOT NULL,
                                           course_id INT NOT NULL,
                                           FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
                                           FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
                                           UNIQUE (student_id, course_id)
);

CREATE TABLE IF NOT EXISTS payments (
                                        payment_id INT AUTO_INCREMENT PRIMARY KEY,
                                        student_id INT NOT NULL,
                                        amount DOUBLE NOT NULL CHECK (amount > 0),
                                        payment_method VARCHAR(50) NOT NULL,
                                        payment_date DATE NOT NULL,
                                        FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

INSERT INTO admin (username, password) VALUES ('admin', 'admin123');

INSERT INTO courses (course_name, credit) VALUES
                                              ('Object Oriented Programming', 3),
                                              ('Data Structures', 3),
                                              ('Software Engineering', 3),
                                              ('Database Management System', 3),
                                              ('Operating System', 3);