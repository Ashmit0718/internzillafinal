package com.internzilla.internzilla.dto;

import com.internzilla.internzilla.model.StudentProfile;
import com.internzilla.internzilla.model.User;

import java.math.BigDecimal;

public class ApplicantDTO {
    private Long id;
    private String fullName;
    private String email;
    private BigDecimal cgpa;
    private String skills;
    private String resumeUrl;

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
    public BigDecimal getCgpa() { return cgpa; }
    public String getSkills() { return skills; }
    public String getResumeUrl() { return resumeUrl; }

    // Setters (if needed for deserialization or modification)
    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setCgpa(BigDecimal cgpa) { this.cgpa = cgpa; }
    public void setSkills(String skills) { this.skills = skills; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
}