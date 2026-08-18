package com.tech_fusion.model.admin;

/**
 * GramConnect - Official Complaint (against a Sarpanch)
 * ------------------------------------------------------------------
 * This is the shared data contract for complaints filed as
 * "Official Complaint" via the Villager Login's NewComplaintPage,
 * eventually consumed by the Admin Login's ComplaintManagement page
 * (the "Complaints Against Sarpanch - Selected Village" panel).
 *
 * FIELD-FOR-FIELD MAPPING TO THE VILLAGER SUBMISSION FORM:
 *   title        <- NewComplaintPage.titleField                 ("2. Complaint Title")
 *   location     <- NewComplaintPage.locationField               ("3. Location / Area")
 *   officialName <- NewComplaintPage.officialNameField           ("Official's Name")
 *   designation  <- NewComplaintPage.officialDesignationField    ("Official's Designation")
 *   description  <- NewComplaintPage.descriptionField            ("4. Description")
 *   village      <- the village the complaint was filed from (not yet collected on the
 *                    Villager side - defaulted/assigned by whichever store creates the
 *                    record until villager accounts carry a home-village of their own)
 *
 * Keeping this as its own small, self-contained model (rather than
 * reusing the existing project-wide Complaint class) means the
 * "Complaints Against Sarpanch" panel can be redesigned to show
 * exactly Title + Location + a View Complaint action, without
 * touching the existing Complaint / VillageDataStore-driven KPI
 * cards elsewhere on the same page.
 *
 * NOT YET WIRED to a real submission pipeline - see
 * OfficialComplaintStore for the temporary in-memory holding store,
 * and NewComplaintPage.handleSubmit() for where a future call into
 * that store (or a real backend service) belongs.
 */
public class OfficialComplaint {

    public enum Status {
        PENDING,
        RESOLVED,
        REJECTED
    }

    private final String id;
    private final String title;
    private final String location;
    private final String officialName;
    private final String designation;
    private final String description;
    private final String village;
    private Status status;

    public OfficialComplaint(String id, String title, String location, String officialName,
                              String designation, String description, String village) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.officialName = officialName;
        this.designation = designation;
        this.description = description;
        this.village = village;
        this.status = Status.PENDING;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getOfficialName() { return officialName; }
    public String getDesignation() { return designation; }
    public String getDescription() { return description; }
    public String getVillage() { return village; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}