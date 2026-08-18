package com.tech_fusion.model.admin;

import java.util.ArrayList;
import java.util.List;

/**
 * GramConnect - Official Complaint Store (TEMPORARY, in-memory)
 * ------------------------------------------------------------------
 * Stands in for the real shared complaint service that will
 * eventually sit between the Villager Login's NewComplaintPage and
 * the Admin Login's ComplaintManagement page.
 *
 * ------------------------------------------------------------------
 * NOT YET WIRED - HOW THE FUTURE CONNECTION WORKS
 * ------------------------------------------------------------------
 * Today this class only seeds itself with sample data (see
 * {@link #seedSampleData()}) so the Admin "Complaints Against
 * Sarpanch" panel has something real to render and link into.
 *
 * When the Villager and Admin logins are wired together, the ONLY
 * change needed on the villager side is for
 * NewComplaintPage.handleSubmit() to call
 *
 *     OfficialComplaintStore.add(title, location, officialName,
 *                                 designation, description, villagerHomeVillage);
 *
 * instead of (or in addition to) ComplaintsPage.addComplaint(...)
 * whenever selectedComplaintType.equals(TYPE_OFFICIAL). Because
 * NewComplaintPage already collects title / location / officialName
 * / designation / description as separate fields, no data
 * reshaping is required at that call site - see the field mapping
 * comment on {@link OfficialComplaint}.
 *
 * On the admin side, ComplaintManagement already reads from this
 * store (via {@link #getForVillage(String)}), so no further change
 * is required there once real data starts arriving - just remove
 * {@link #seedSampleData()} once real submissions are flowing.
 */
public class OfficialComplaintStore {

    private static final List<OfficialComplaint> COMPLAINTS = new ArrayList<>();
    private static int nextId = 1;

    static {
        seedSampleData();
    }

    /**
     * TODO: remove once NewComplaintPage is wired to push real Official
     * Complaints here. Kept for now purely so the redesigned Admin panel
     * has representative data to display and link into.
     */
    private static void seedSampleData() {
        String villageA = villageAt(1);
        String villageB = villageAt(2);

        add("Delay in Ration Card Renewal", "Panchayat Office, Ward 2",
                "Ramesh Patil", "Sarpanch",
                "My ration card renewal application has been pending for over two months "
                        + "with no update or response from the Panchayat office despite repeated visits.",
                villageA);

        add("Misuse of Village Development Funds", "Gram Panchayat Bhavan",
                "Ramesh Patil", "Sarpanch",
                "Funds allocated for the new community hall appear to have been diverted "
                        + "elsewhere - construction has not started despite the budget being released "
                        + "over four months ago.",
                villageA);

        add("No Response to RTI Application", "Village Office, Main Road",
                "Sunita Jadhav", "Deputy Sarpanch",
                "An RTI application filed 45 days ago regarding road repair expenditure has "
                        + "received no acknowledgement or response, well past the statutory deadline.",
                villageB);
    }

    /** Best-effort pick of a real village name from VillageDataStore, without hardcoding one. */
    private static String villageAt(int idx) {
        try {
            List<String> villages = VillageDataStore.VILLAGES;
            if (villages != null && villages.size() > idx) {
                return villages.get(idx);
            }
            if (villages != null && !villages.isEmpty()) {
                return villages.get(villages.size() - 1);
            }
        } catch (Exception ignored) {
            // Fall through to the placeholder below.
        }
        return "Unknown Village";
    }

    public static synchronized OfficialComplaint add(String title, String location, String officialName,
                                                       String designation, String description, String village) {
        String id = "#OFC-" + String.format("%04d", nextId++);
        OfficialComplaint complaint = new OfficialComplaint(id, title, location, officialName,
                designation, description, village);
        COMPLAINTS.add(0, complaint);
        return complaint;
    }

    public static synchronized List<OfficialComplaint> getAll() {
        return new ArrayList<>(COMPLAINTS);
    }

    public static synchronized List<OfficialComplaint> getForVillage(String village) {
        List<OfficialComplaint> result = new ArrayList<>();
        for (OfficialComplaint c : COMPLAINTS) {
            if (c.getVillage() != null && c.getVillage().equalsIgnoreCase(village)) {
                result.add(c);
            }
        }
        return result;
    }
}