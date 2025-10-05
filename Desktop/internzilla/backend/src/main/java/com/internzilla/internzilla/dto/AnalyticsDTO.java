package com.internzilla.internzilla.dto;

public class AnalyticsDTO {
    private long totalUsers;
    private long totalStudents;
    private long totalRecruiters;
    private long totalInternships;

    // Getters and Setters
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }
    public long getTotalRecruiters() { return totalRecruiters; }
    public void setTotalRecruiters(long totalRecruiters) { this.totalRecruiters = totalRecruiters; }
    public long getTotalInternships() { return totalInternships; }
    public void setTotalInternships(long totalInternships) { this.totalInternships = totalInternships; }
}