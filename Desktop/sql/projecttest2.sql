-- Create and use database
DROP DATABASE IF EXISTS project1;
CREATE DATABASE project1;
USE project1;

-- COMPANY TABLE
CREATE TABLE company (
    company_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    industry VARCHAR(100),
    contact_email VARCHAR(100),
    user_id INT
);

-- STUDENT TABLE
CREATE TABLE student (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
);

-- INTERNSHIP TABLE
CREATE TABLE internship (
    internship_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    stipend INT,
    duration_weeks INT,
    mode ENUM('onsite', 'remote', 'hybrid'),
    company_id INT,
    FOREIGN KEY (company_id) REFERENCES company(company_id) ON DELETE CASCADE
);

-- APPLICATION TABLE
CREATE TABLE application (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    internship_id INT,
    apply_date DATE,
    status ENUM('applied', 'accepted', 'rejected'),
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (internship_id) REFERENCES internship(internship_id) ON DELETE CASCADE
);

-- SAMPLE DATA

-- COMPANY INSERT
INSERT INTO company (name, industry, contact_email, user_id) VALUES
('TechNova', 'IT', 'hr@technova.com', 3);

-- STUDENT INSERTS
INSERT INTO student (name) VALUES
('Alice'), ('Bob');

-- INTERNSHIP INSERT
INSERT INTO internship (title, stipend, duration_weeks, mode, company_id) VALUES
('Web Developer Intern', 8000, 6, 'remote', 1),
('Backend Intern', 9000, 8, 'onsite', 1);

-- APPLICATION INSERT
INSERT INTO application (student_id, internship_id, apply_date, status) VALUES
(1, 1, CURDATE(), 'applied'),
(2, 2, CURDATE(), 'accepted');

-- QUERIES FROM YOUR CODE

-- Select internships applied by students (your join logic)
SELECT 
    s.name AS student,
    i.title AS internship,
    a.status
FROM application a
JOIN student s ON a.student_id = s.student_id
JOIN internship i ON a.internship_id = i.internship_id;

-- Select company ID by user_id (your other query)
SELECT company_id FROM company WHERE user_id = 3;
