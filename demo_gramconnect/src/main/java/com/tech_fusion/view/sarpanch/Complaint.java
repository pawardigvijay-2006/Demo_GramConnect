package com.sarpanch.model;

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
    private final  String description;
    private final  String priority;         // Low, Medium, High
    private String status;           // Pending, In Progress, Resolved, Rejected
    private final  String gpsLocation;      // optional, "18.5204 N, 73.8567 E" style
    private String officerRemark;    // Sarpanch's note when action is taken
    private final LocalDateTime submittedOn;

    public Complaint(String villagerName, String village, String contactNumber, String category,
                      String description, String priority, String gpsLocation) {
        this.id = nextId();
        this.villagerName = villagerName;
        this.village = village;
        this.contactNumber = contactNumber;
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.gpsLocation = gpsLocation;
        this.status = "Pending";
        this.officerRemark = "";
        this.submittedOn = LocalDateTime.now();
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
        this.description = description;
        this.priority = priority;
        this.gpsLocation = gpsLocation;
        this.status = status;
        this.officerRemark = "";
        this.submittedOn = submittedOn;
    }

    public String getId() { return id; }
    public String getVillagerName() { return villagerName; }
    public String getVillage() { return village; }
    public String getContactNumber() { return contactNumber; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getGpsLocation() { return gpsLocation; }
    public String getOfficerRemark() { return officerRemark; }
    public void setOfficerRemark(String officerRemark) { this.officerRemark = officerRemark; }
    public LocalDateTime getSubmittedOn() { return submittedOn; }

    public String getFormattedDate() {
        return submittedOn.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    }
}