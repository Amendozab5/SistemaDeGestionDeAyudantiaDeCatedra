package com.uteq.sgaac.dto;

import java.math.BigDecimal;

public class EstudianteDashboardDTO {

    private String studentName;
    private long activeApplications;
    private long scheduledTests;
    private BigDecimal gpa;

    public EstudianteDashboardDTO(String studentName, long activeApplications, long scheduledTests, BigDecimal gpa) {
        this.studentName = studentName;
        this.activeApplications = activeApplications;
        this.scheduledTests = scheduledTests;
        this.gpa = gpa;
    }

    // Getters and Setters

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public long getActiveApplications() {
        return activeApplications;
    }

    public void setActiveApplications(long activeApplications) {
        this.activeApplications = activeApplications;
    }

    public long getScheduledTests() {
        return scheduledTests;
    }

    public void setScheduledTests(long scheduledTests) {
        this.scheduledTests = scheduledTests;
    }

    public BigDecimal getGpa() {
        return gpa;
    }

    public void setGpa(BigDecimal gpa) {
        this.gpa = gpa;
    }
}
