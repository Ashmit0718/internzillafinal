package com.internzilla.internzilla.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "internships")
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ** THIS IS THE MISSING FIELD **
    @Column(name = "recruiter_id", nullable = false)
    private Long recruiterId;

    private String title;
    
    @Lob 
    private String description;
    
    private String stipend;
    
    @Enumerated(EnumType.STRING)
    private InternshipType internshipType;
    
    @Lob 
    private String eligibilityCriteria;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "posted_date")
    private Date postedDate = new Date();

    public enum InternshipType { ONLINE, OFFLINE, HYBRID }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // ** AND THESE ARE THE MISSING METHODS **
    public Long getRecruiterId() { return recruiterId; }
    public void setRecruiterId(Long recruiterId) { this.recruiterId = recruiterId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getStipend() { return stipend; }
    public void setStipend(String stipend) { this.stipend = stipend; }
    
    public InternshipType getInternshipType() { return internshipType; }
    public void setInternshipType(InternshipType internshipType) { this.internshipType = internshipType; }
    
    public String getEligibilityCriteria() { return eligibilityCriteria; }
    public void setEligibilityCriteria(String eligibilityCriteria) { this.eligibilityCriteria = eligibilityCriteria; }
    
    public Date getPostedDate() { return postedDate; }
    public void setPostedDate(Date postedDate) { this.postedDate = postedDate; }
}

