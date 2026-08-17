package com.tech_fusion.model.admin;

/**
 * A single grievance / complaint record, tagged by village so it can be
 * filtered the same way as {@link Project} records.
 */
public class ComplaintRecord {

    public enum Status {
        PENDING, RESOLVED
    }

    private final String village;
    private final String category;
    private final Status status;

    public ComplaintRecord(String village, String category, Status status) {
        this.village = village;
        this.category = category;
        this.status = status;
    }

    public String getVillage() { return village; }
    public String getCategory() { return category; }
    public Status getStatus() { return status; }
}