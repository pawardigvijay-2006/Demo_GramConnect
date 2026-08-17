package com.tech_fusion.model.admin;

/**
 * A single citizen complaint / grievance record.
 *
 * Every complaint is tagged with the village it belongs to, exactly like
 * {@link Project} and {@link GeneratedReport}, so any page can filter the
 * same underlying dataset by village without duplicating data.
 *
 * Plain data holder (no JavaFX / UI code) so it can later be mapped 1:1
 * onto a Firebase Firestore document.
 */
public class Complaint {

    public enum Status {
        PENDING, IN_PROGRESS, RESOLVED, REJECTED
    }

    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    private final String complaintId;
    private final String citizenName;
    private final String village;
    private final String category;
    private final String description;
    private final Priority priority;
    private final Status status;
    private final String dateFiled;
    private final String assignedOfficer;
    private final String resolution;       // empty/null until resolved
    private final Double resolutionDays;   // null until resolved - days taken to close it

    public Complaint(String complaintId, String citizenName, String village, String category,
                      String description, Priority priority, Status status, String dateFiled,
                      String assignedOfficer, String resolution, Double resolutionDays) {
        this.complaintId = complaintId;
        this.citizenName = citizenName;
        this.village = village;
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dateFiled = dateFiled;
        this.assignedOfficer = assignedOfficer;
        this.resolution = resolution;
        this.resolutionDays = resolutionDays;
    }

    public String getComplaintId() { return complaintId; }
    public String getCitizenName() { return citizenName; }
    public String getVillage() { return village; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }
    public String getDateFiled() { return dateFiled; }
    public String getAssignedOfficer() { return assignedOfficer; }
    public String getResolution() { return resolution; }
    public Double getResolutionDays() { return resolutionDays; }
}