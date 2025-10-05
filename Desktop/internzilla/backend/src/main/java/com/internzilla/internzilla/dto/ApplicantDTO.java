package com.internzilla.internzilla.dto;

import com.internzilla.internzilla.model.StudentProfile;
import com.internzilla.internzilla.model.User;

public class ApplicantDTO {
    private Long id;
    private String fullName;
    private String email;
    private java.math.BigDecimal cgpa;
    private String skills;
    private String resumeUrl;

    // This constructor combines a User and a StudentProfile into one object
    public ApplicantDTO(User user, StudentProfile profile) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        if (profile != null) {
            this.cgpa = profile.getCgpa();
            this.skills = profile.getSkills();
            this.resumeUrl = profile.getResumeUrl();
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public java.math.BigDecimal getCgpa() { return cgpa; }
    public String getSkills() { return skills; }
    public String getResumeUrl() { return resumeUrl; }
}