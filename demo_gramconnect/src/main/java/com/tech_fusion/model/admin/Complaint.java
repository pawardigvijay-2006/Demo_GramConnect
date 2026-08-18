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
 *
 * ------------------------------------------------------------------
 * PROJECT / SARPANCH-TARGETING FIELDS
 * ------------------------------------------------------------------
 * Three additional fields support the "Complaints Against Sarpanch"
 * panel on the Complaint Management page:
 *   - projectId:        links this complaint to a specific Project
 *                        (see ProjectDataStore.selectedProject). Null
 *                        for complaints that aren't tied to any project.
 *   - targetPerson:      who/what the complaint is against, e.g. "Sarpanch".
 *                        Null/blank means "not targeted at anyone specific".
 *   - complainantRole:    the role of whoever filed the complaint, e.g.
 *                        "Villager", "Sarpanch", "Officer". Used to
 *                        exclude complaints the Sarpanch filed themselves
 *                        from the "against Sarpanch" panel.
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

    private final String projectId;
    private final String targetPerson;
    private final String complainantRole;

    /** Full constructor - use this for any complaint that needs project/Sarpanch targeting. */
    public Complaint(String complaintId, String citizenName, String village, String category,
                      String description, Priority priority, Status status, String dateFiled,
                      String assignedOfficer, String resolution, Double resolutionDays,
                      String projectId, String targetPerson, String complainantRole) {
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
        this.projectId = projectId;
        this.targetPerson = targetPerson;
        this.complainantRole = complainantRole;
    }

    /**
     * Back-compat constructor matching every existing call in
     * VillageDataStore.seedComplaints(). Defaults to "no project",
     * "no specific target", and complainant role "Villager".
     */
    public Complaint(String complaintId, String citizenName, String village, String category,
                      String description, Priority priority, Status status, String dateFiled,
                      String assignedOfficer, String resolution, Double resolutionDays) {
        this(complaintId, citizenName, village, category, description, priority, status, dateFiled,
                assignedOfficer, resolution, resolutionDays, null, null, "Villager");
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

    public String getProjectId() { return projectId; }
    public String getTargetPerson() { return targetPerson; }
    public String getComplainantRole() { return complainantRole; }
}