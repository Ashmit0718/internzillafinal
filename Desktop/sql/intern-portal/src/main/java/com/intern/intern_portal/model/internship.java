package com.intern.intern_portal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "internship")
public class internship {

    @Id
    @Column(name = "internship_id")
    private int internshipId;

    private String title;
    private int stipend;

    @Column(name = "duration_weeks")
    private int durationWeeks;

    @Enumerated(EnumType.STRING)
    private Mode mode;

    @Column(name = "company_id")
    private int companyId;

    public enum Mode {
        ONLINE, OFFLINE, DUAL
    }

    // Default constructor (required by JPA)
    public internship() {}

    // Getters and Setters
    public int getInternshipId() { return internshipId; }
    public void setInternshipId(int internshipId) { this.internshipId = internshipId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getStipend() { return stipend; }
    public void setStipend(int stipend) { this.stipend = stipend; }

    public int getDurationWeeks() { return durationWeeks; }
    public void setDurationWeeks(int durationWeeks) { this.durationWeeks = durationWeeks; }

    public Mode getMode() { return mode; }
    public void setMode(Mode mode) { this.mode = mode; }

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
}