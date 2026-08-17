package com.tech_fusion.view.sarpanch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single complaint raised by a villager and tracked by the Sarpanch
 * on the Complaints page. Complaints are added to {@link ComplaintStore} by
 * whatever intake channel the panchayat uses (phone, in-person, a future
 * villager-facing form, etc.) and are all reviewed here in one place.
 */
public class Complaint {

    /** Simple in-process id generator, e.g. "CMP-1001". */
    private static int SEQUENCE = 1000;

    public static String nextId() {
        SEQUENCE++;
        return "CMP-" + SEQUENCE;
    }

    private final String id;
    private final  String villagerName;
    private final  String village;          // village / ward
    private final  String contactNumber;
    private final  String category;         // Water Supply, Roads, Electricity, Sanitation, Education, Other
    private String title;            // short complaint title, e.g. "Broken Water Pipe near Temple"
    private final  String description;
    private final  String priority;         // Low, Medium, High
    private String status;           // Pending, In Progress, Resolved, Rejected
    private final  String gpsLocation;      // optional, "18.5204 N, 73.8567 E" style
    private String officerRemark;    // Sarpanch's note when action is taken
    private final LocalDateTime submittedOn;

    // Optional path/URL to a photo attached to the complaint. Left null until a
    // real intake channel (e.g. the villager-facing NewComplaintPage) is wired
    // up to populate it; the Sarpanch Complaint Details screen already knows
    // how to render this field once it's populated, so no further UI changes
    // will be needed when that integration happens.
    private String photoPath;

    public Complaint(String villagerName, String village, String contactNumber, String category,
                      String description, String priority, String gpsLocation) {
        this.id = nextId();
        this.villagerName = villagerName;
        this.village = village;
        this.contactNumber = contactNumber;
        this.category = category;
        this.title = null;
        this.description = description;
        this.priority = priority;
        this.gpsLocation = gpsLocation;
        this.status = "Pending";
        this.officerRemark = "";
        this.submittedOn = LocalDateTime.now();
        this.photoPath = null;
    }

    /**
     * Convenience overload that also accepts an explicit title, so future
     * intake channels (e.g. a villager-submitted complaint) can supply one
     * directly instead of relying on {@link #getTitle()}'s fallback.
     */
    public Complaint(String villagerName, String village, String contactNumber, String category,
                      String title, String description, String priority, String gpsLocation) {
        this(villagerName, village, contactNumber, category, description, priority, gpsLocation);
        this.title = title;
    }

    /** Constructor used only for pre-seeded demo data with a custom timestamp. */
    public Complaint(String villagerName, String village, String contactNumber, String category,
                      String description, String priority, String gpsLocation,
                      String status, LocalDateTime submittedOn) {
        this.id = nextId();
        this.villagerName = villagerName;
        this.village = village;
        this.contactNumber = contactNumber;
        this.category = category;
        this.title = null;
        this.description = description;
        this.priority = priority;
        this.gpsLocation = gpsLocation;
        this.status = status;
        this.officerRemark = "";
        this.submittedOn = submittedOn;
        this.photoPath = null;
    }

    /** Same pre-seeded demo overload as above, but with an explicit title. */
    public Complaint(String villagerName, String village, String contactNumber, String category,
                      String title, String description, String priority, String gpsLocation,
                      String status, LocalDateTime submittedOn) {
        this(villagerName, village, contactNumber, category, description, priority, gpsLocation, status, submittedOn);
        this.title = title;
    }

    public String getId() { return id; }
    public String getVillagerName() { return villagerName; }
    public String getVillage() { return village; }
    public String getContactNumber() { return contactNumber; }
    public String getCategory() { return category; }

    /**
     * Returns the complaint's title. Falls back to a category-based label for
     * complaints that don't have one yet (e.g. legacy/demo data), so the UI
     * always has something sensible to show for the "Title of the Complaint"
     * field even before every complaint carries a real title.
     */
    public String getTitle() {
        if (title != null && !title.isBlank()) {
            return title;
        }
        return (category == null || category.isBlank() ? "General" : category) + " Complaint";
    }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getGpsLocation() { return gpsLocation; }
    public String getOfficerRemark() { return officerRemark; }
    public void setOfficerRemark(String officerRemark) { this.officerRemark = officerRemark; }
    public LocalDateTime getSubmittedOn() { return submittedOn; }

    /** Optional photo attached to the complaint; null until an intake channel sets one. */
    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public String getFormattedDate() {
        return submittedOn.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    }
}