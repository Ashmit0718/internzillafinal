package com.internzilla.internzilla.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "internship_id", nullable = false)
    private Long internshipId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "application_date")
    private Date applicationDate = new Date();

    private String status = "APPLIED";

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getInternshipId() { return internshipId; }
    public void setInternshipId(Long internshipId) { this.internshipId = internshipId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}