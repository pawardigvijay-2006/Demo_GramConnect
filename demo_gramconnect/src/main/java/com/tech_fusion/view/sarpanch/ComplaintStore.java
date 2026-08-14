package com.tech_fusion.view.sarpanch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



/**
 * Simple in-memory, process-wide store of all complaints.
 *
 * This is intentionally a static/shared store (no database) so the Sarpanch
 * Complaints page always reads the current, single source of truth for
 * complaints raised by villagers. Swap this class's internals for a real
 * database/API call later without touching the view classes, since they
 * only depend on this class's methods.
 */
public final class ComplaintStore {

    private static final List<Complaint> COMPLAINTS = new ArrayList<>();

    static {
        // Seed with demo data so the Sarpanch Complaints page is populated on first run,
        // matching the sample-data style already used across the other pages.
        COMPLAINTS.add(new Complaint(
            "Ramesh Kadam", "Ward 4 - Main Street", "9822012345",
            "Roads", "Large pothole near the bus stop is causing accidents, especially at night.",
            "High", "18.5211 N, 73.8571 E", "Pending",
            LocalDateTime.now().minusHours(5)
        ));
        COMPLAINTS.add(new Complaint(
            "Sunita Pawar", "Near School Area", "9822098765",
            "Water Supply", "No water supply for the last 3 days in the eastern lane.",
            "High", "18.5204 N, 73.8567 E", "In Progress",
            LocalDateTime.now().minusDays(1)
        ));
        COMPLAINTS.add(new Complaint(
            "Ganesh More", "Gram Panchayat Office", "9822011122",
            "Electricity", "Streetlight outside the primary school has not worked for two weeks.",
            "Medium", null, "Pending",
            LocalDateTime.now().minusDays(2)
        ));
        COMPLAINTS.add(new Complaint(
            "Anita Jadhav", "Ward 2", "9822033445",
            "Sanitation", "Garbage has not been collected from the community bin in over a week.",
            "Medium", null, "Resolved",
            LocalDateTime.now().minusDays(4)
        ));
        COMPLAINTS.add(new Complaint(
            "Vitthal Shinde", "Ward 4 - Main Street", "9822055667",
            "Roads", "Drainage water is overflowing onto the road after every rain.",
            "Low", null, "Rejected",
            LocalDateTime.now().minusDays(6)
        ));
    }

    private ComplaintStore() { }

    /** Adds a new complaint at the top of the list (most recent first). */
    public static void addComplaint(Complaint complaint) {
        COMPLAINTS.add(0, complaint);
    }

    /** Returns a defensive copy of every complaint, most recent first. */
    public static List<Complaint> getAll() {
        return new ArrayList<>(COMPLAINTS);
    }

    /** Returns only the complaints filed by a specific villager (used by the Villager dashboard). */
    public static List<Complaint> getByVillager(String villagerName) {
        List<Complaint> mine = new ArrayList<>();
        for (Complaint c : COMPLAINTS) {
            if (c.getVillagerName() != null && c.getVillagerName().equalsIgnoreCase(villagerName)) {
                mine.add(c);
            }
        }
        return mine;
    }

    public static int countByStatus(String status) {
        int count = 0;
        for (Complaint c : COMPLAINTS) {
            if (c.getStatus().equalsIgnoreCase(status)) count++;
        }
        return count;
    }

    public static void updateStatus(String complaintId, String newStatus, String remark) {
        for (Complaint c : COMPLAINTS) {
            if (c.getId().equals(complaintId)) {
                c.setStatus(newStatus);
                if (remark != null && !remark.isBlank()) {
                    c.setOfficerRemark(remark);
                }
                return;
            }
        }
    }
}