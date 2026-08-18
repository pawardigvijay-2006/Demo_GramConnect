package com.tech_fusion.model.admin;

/**
 * A single infrastructure/development project tracked for a village.
 *
 * Plain data holder (no JavaFX / UI code) so it maps 1:1 onto a future
 * Firebase Firestore document, same as {@link Complaint} and
 * {@link GeneratedReport}.
 */
public class Project {

    public enum Status {
        ONGOING, COMPLETED, DELAYED
    }

    private final String projectId;
    private final String projectName;
    private final String village;
    private final String category;
    private final Status status;
    private final double budgetAllocated;
    private final double budgetUtilized;
    private final int approvalDays;

    /** Full constructor - explicit project ID. Use this going forward. */
    public Project(String projectId, String projectName, String village, String category,
                    Status status, double budgetAllocated, double budgetUtilized, int approvalDays) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.village = village;
        this.category = category;
        this.status = status;
        this.budgetAllocated = budgetAllocated;
        this.budgetUtilized = budgetUtilized;
        this.approvalDays = approvalDays;
    }

    /**
     * Back-compat constructor matching VillageDataStore's existing
     * seedProjects() calls (no ID param). Auto-generates a stable-looking
     * ID so every project still gets one without editing every existing
     * call site.
     */
    public Project(String projectName, String village, String category, Status status,
                    double budgetAllocated, double budgetUtilized, int approvalDays) {
        this(generateId(village), projectName, village, category, status,
                budgetAllocated, budgetUtilized, approvalDays);
    }

    private static int idCounter = 1000;
    private static synchronized String generateId(String village) {
        String prefix = (village == null || village.isEmpty())
                ? "PRJ" : village.substring(0, Math.min(3, village.length())).toUpperCase();
        return prefix + "-" + (idCounter++);
    }

    public String getProjectId() { return projectId; }
    public String getProjectName() { return projectName; }
    public String getVillage() { return village; }
    public String getCategory() { return category; }
    public Status getStatus() { return status; }
    public double getBudgetAllocated() { return budgetAllocated; }
    public double getBudgetUtilized() { return budgetUtilized; }
    public int getApprovalDays() { return approvalDays; }
}