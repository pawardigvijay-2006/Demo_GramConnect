package com.tech_fusion.model.admin;

/**
 * A single block-development project record.
 *
 * Every project is tagged with the village it belongs to so that any
 * page (Dashboard, ProjectManagement, ReportsAnalytics, ...) can filter
 * the same underlying dataset by village without duplicating data.
 *
 * This class is intentionally a plain data holder (no JavaFX / UI code)
 * so it can later be mapped 1:1 onto a Firebase Firestore document.
 */
public class Project {

    public enum Status {
        COMPLETED, ONGOING, DELAYED
    }

    private final String name;
    private final String village;
    private final String category;      // e.g. "Infrastructure", "Water Supply", "Education", "Other"
    private final Status status;
    private final double budgetAllocated;   // in rupees
    private final double budgetUtilized;    // in rupees
    private final double approvalDays;      // days taken for approval

    public Project(String name, String village, String category, Status status,
                    double budgetAllocated, double budgetUtilized, double approvalDays) {
        this.name = name;
        this.village = village;
        this.category = category;
        this.status = status;
        this.budgetAllocated = budgetAllocated;
        this.budgetUtilized = budgetUtilized;
        this.approvalDays = approvalDays;
    }

    public String getName() { return name; }
    public String getVillage() { return village; }
    public String getCategory() { return category; }
    public Status getStatus() { return status; }
    public double getBudgetAllocated() { return budgetAllocated; }
    public double getBudgetUtilized() { return budgetUtilized; }
    public double getApprovalDays() { return approvalDays; }
}