package com.tech_fusion.model.gramsevak;

// ============================================================
// COMPLAINT MODEL
// ============================================================
//
// Simple data holder for one complaint.
//
// Right now this is filled with temporary/sample data inside
// Complaints.java (see buildTempComplaint(...)).
//
// Later, when the backend/API is ready, this exact same class
// can be filled from the server response instead — nothing in
// ComplaintDetails.java needs to change, because it only reads
// from a Complaint object, it never creates the data itself.
// ============================================================

public class Complaint {

    private final String id;
    private final String category;
    private final String subject;
    private final String description;
    private final String submittedBy;
    private final String contact;
    private final String location;
    private final String dateSubmitted;
    private final String lastUpdated;
    private final String status;
    private final String priority;
    private final String department;
    private final String officer;

    public Complaint(
            String id,
            String category,
            String subject,
            String description,
            String submittedBy,
            String contact,
            String location,
            String dateSubmitted,
            String lastUpdated,
            String status,
            String priority,
            String department,
            String officer) {

        this.id = id;
        this.category = category;
        this.subject = subject;
        this.description = description;
        this.submittedBy = submittedBy;
        this.contact = contact;
        this.location = location;
        this.dateSubmitted = dateSubmitted;
        this.lastUpdated = lastUpdated;
        this.status = status;
        this.priority = priority;
        this.department = department;
        this.officer = officer;
    }

    public String getId() { return id; }
    public String getCategory() { return category; }
    public String getSubject() { return subject; }
    public String getDescription() { return description; }
    public String getSubmittedBy() { return submittedBy; }
    public String getContact() { return contact; }
    public String getLocation() { return location; }
    public String getDateSubmitted() { return dateSubmitted; }
    public String getLastUpdated() { return lastUpdated; }
    public String getStatus() { return status; }
    public String getPriority() { return priority; }
    public String getDepartment() { return department; }
    public String getOfficer() { return officer; }
}
