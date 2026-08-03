CREATE DATABASE IF NOT EXISTS mu_course_enrollment;
USE mu_course_enrollment;

CREATE TABLE IF NOT EXISTS departments (
                                           dept_id varchar(100),
                                           dept_code VARCHAR(20) UNIQUE NOT NULL,
                                           dept_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
                                        student_id VARCHAR(100),
                                        name VARCHAR(100) NOT NULL,
                                        email VARCHAR(100) UNIQUE NOT NULL,
                                        password VARCHAR(100) NOT NULL,
                                        department VARCHAR(50) NOT NULL,
                                        batch VARCHAR(20) NOT NULL,
                                        phone VARCHAR(20) NOT NULL
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
                                           student_id VARCHAR NOT NULL,
                                           course_id VARCHAR NULL,
                                           FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
                                           FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
                                           UNIQUE (student_id, course_id)
);

CREATE TABLE IF NOT EXISTS payments (
                                        payment_id INT AUTO_INCREMENT PRIMARY KEY,
                                        student_id varchar NOT NULL,
                                        amount DOUBLE NOT NULL CHECK (amount > 0),
                                        payment_method VARCHAR(50) NOT NULL,
                                        payment_date DATE NOT NULL,
                                        payment_type VARCHAR(50) NOT NULL,
                                        month VARCHAR(20),
                                        year VARCHAR(10),
                                        term_name VARCHAR(50),
                                        status VARCHAR(20) DEFAULT 'PENDING',
                                        reference_message TEXT,
                                        FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- NEW: Fee structure table (admin can update these rows to change prices)
CREATE TABLE IF NOT EXISTS fee_structure (
                                             fee_id INT AUTO_INCREMENT PRIMARY KEY,
                                             fee_type VARCHAR(100) UNIQUE NOT NULL,
                                             amount DOUBLE NOT NULL CHECK (amount > 0),
                                             description VARCHAR(255)
);

INSERT INTO departments (dept_code, dept_name) VALUES
                                                   ('CSE', 'Computer Science & Engineering'),
                                                   ('EEE', 'Electrical & Electronic Engineering'),
                                                   ('SWE', 'Software Engineering'),
                                                   ('BBA', 'Bachelor of Business Administration'),
                                                   ('ECO', 'Economics'),
                                                   ('ENGLISH', 'English'),
                                                   ('DATA SCIENCE', 'Data Science'),
                                                   ('LAW', 'Law');

INSERT INTO admin (username, password) VALUES ('admin', 'admin123');

INSERT INTO courses (course_name, credit) VALUES
                                              ('Object Oriented Programming', 3),
                                              ('Data Structures', 3),
                                              ('Software Engineering', 3),
                                              ('Database Management System', 3),
                                              ('Operating System', 3);

-- Default fee amounts (admin can change directly in DB or via future UI)
INSERT INTO fee_structure (fee_type, amount, description) VALUES
                                                              ('Monthly Tuition Fee', 100.00, 'Monthly tuition fee'),
                                                              ('Monthly Campus Activities Fee', 25.00, 'Monthly campus activities fee'),
                                                              ('New Term Admission Fee', 200.00, 'Admission fee for new term');