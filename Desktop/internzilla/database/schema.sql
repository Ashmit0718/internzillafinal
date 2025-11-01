DROP TABLE IF EXISTS applications;
DROP TABLE IF EXISTS internships;
DROP TABLE IF EXISTS student_profiles;
DROP TABLE IF EXISTS users;

-- Create the users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    reset_token VARCHAR(255)
);

-- Create the student_profiles table
CREATE TABLE student_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    cgpa DECIMAL(3, 2) CHECK (cgpa >= 0.00 AND cgpa <= 10.00),
    skills TEXT,
    resume_url VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create the internships table
CREATE TABLE internships (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recruiter_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    stipend VARCHAR(100),
    internship_type VARCHAR(50) NOT NULL, -- e.g., ONLINE, OFFLINE, HYBRID
    eligibility_criteria TEXT,
    posted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recruiter_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create the applications table
CREATE TABLE applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    internship_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'APPLIED', -- e.g., APPLIED, VIEWED, ACCEPTED
    UNIQUE (internship_id, student_id), -- Prevent multiple applications
    FOREIGN KEY (internship_id) REFERENCES internships(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Default Admin User
-- Email: admin@internzilla.com
-- Password: adminpassword
-- The password hash is pre-calculated for 'adminpassword'
INSERT INTO users (email, password, role, full_name) 
SELECT 'admin@internzilla.com', 'admin', 'ADMIN', 'Admin'
WHERE NOT EXISTS (SELECT * FROM users WHERE email='admin@internzilla.com');




