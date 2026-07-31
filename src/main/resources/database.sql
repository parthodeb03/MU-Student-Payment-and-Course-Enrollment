CREATE DATABASE mu_course_enrollment;

USE mu_course_enrollment;

CREATE TABLE students (

                          student_id INT AUTO_INCREMENT PRIMARY KEY,

                          name VARCHAR(100) NOT NULL,

                          email VARCHAR(100) UNIQUE NOT NULL,

                          password VARCHAR(100) NOT NULL

);

CREATE TABLE admin (

                       admin_id INT AUTO_INCREMENT PRIMARY KEY,

                       username VARCHAR(50),

                       password VARCHAR(50)

);

CREATE TABLE courses (

                         course_id INT AUTO_INCREMENT PRIMARY KEY,

                         course_name VARCHAR(100),

                         credit INT

);

CREATE TABLE enrollments (

                             enrollment_id INT AUTO_INCREMENT PRIMARY KEY,

                             student_id INT,

                             course_id INT,

                             FOREIGN KEY(student_id)
                                 REFERENCES students(student_id),

                             FOREIGN KEY(course_id)
                                 REFERENCES courses(course_id)

);

CREATE TABLE payments (

                          payment_id INT AUTO_INCREMENT PRIMARY KEY,

                          student_id INT,

                          amount DOUBLE,

                          payment_method VARCHAR(50),

                          payment_date DATE,

                          FOREIGN KEY(student_id)
                              REFERENCES students(student_id)

);

INSERT INTO admin(username,password)

VALUES

    ('admin','admin123');

INSERT INTO courses(course_name,credit)

VALUES

    ('Object Oriented Programming',3),

    ('Data Structures',3),

    ('Software Engineering',3),

    ('Database Management System',3),

    ('Operating System',3);

CREATE TABLE admin (
                       admin_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL
);
