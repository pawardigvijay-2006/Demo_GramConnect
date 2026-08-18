package com.tech_fusion.model.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Single source of truth for all village-tagged report/analytics data,
 * AND for which village is currently selected block-wide.
 *
 * This class is deliberately self-contained: any page (Dashboard,
 * ReportsAnalytics, ProjectManagement, ...) can read {@link #VILLAGES}
 * and {@link #selectedVillage} directly from here without depending on
 * any other page/file. Pages should never keep their own private copy
 * of the village list or the current selection - they read and write
 * the two fields below, exactly like every page already shares
 * {@code Dashboard.myStage} for navigation.
 *
 * UI pages must never embed sample numbers directly in their layout
 * code either - they call the filter/aggregate methods here, passing
 * in whichever village is currently selected.
 *
 * ---------------------------------------------------------------------
 * SARPANCH NAMES
 * ---------------------------------------------------------------------
 * {@link #SARPANCH_NAMES} maps each village to its Sarpanch's display
 * name. {@link ComplaintManagement} (and any other page) reads this
 * through {@link #getSarpanchName(String)} - never hardcode a Sarpanch
 * name anywhere else. Update the map below when real names are
 * available; every caller keeps working unchanged since they only go
 * through the accessor method.
 * ---------------------------------------------------------------------
 * FIREBASE READINESS
 * ---------------------------------------------------------------------
 * The three private lists below (PROJECTS / COMPLAINTS / REPORTS), plus
 * SARPANCH_NAMES, are the only place that currently holds "local/sample"
 * data. To move to Firebase:
 *   1. Replace the static initializer blocks with a Firestore listener /
 *      one-time fetch that populates the same lists/map (or swap the
 *      fields for ObservableList/ObservableMap and attach a Firestore
 *      snapshot listener).
 *   2. Every method below (getProjects, getTotalBudget, getSarpanchName,
 *      etc.) keeps working unchanged, because none of the UI code talks
 *      to Firestore directly - it only talks to this class.
 * ---------------------------------------------------------------------
 */
public final class VillageDataStore {

    /** Sentinel used throughout the app for the block-wide aggregate view. */
    public static final String ALL_VILLAGES = "All Villages";

    /** Fixed list of villages under this BDO's block. Index 0 is always the aggregate view. */
    public static final List<String> VILLAGES = Arrays.asList(
            ALL_VILLAGES, "Sitapur", "Rampur", "Kondli", "Main St.", "North Vill.", "East Ward"
    );

    /**
     * The block-wide "currently active" village. Shared, mutable, static
     * state - any page that changes this and then re-renders will see the
     * new selection everywhere. Defaults to the aggregate view.
     */
    public static String selectedVillage = VILLAGES.get(0);

    private static final List<Project> PROJECTS = new ArrayList<>();
    private static final List<Complaint> COMPLAINTS = new ArrayList<>();
    private static final List<GeneratedReport> REPORTS = new ArrayList<>();

    /**
     * Village -> Sarpanch display name. Every real village (i.e. every
     * entry in {@link #VILLAGES} except {@link #ALL_VILLAGES}) should
     * have an entry here. Missing/blank entries are treated as
     * "unassigned" by {@link #getSarpanchName(String)} callers.
     *
     * NOTE: names below are placeholders in the same style as the
     * seeded officer names - replace with real Sarpanch names whenever
     * you have them; nothing else in the codebase needs to change.
     */
    private static final Map<String, String> SARPANCH_NAMES = new LinkedHashMap<>();

    static {
        SARPANCH_NAMES.put("Sitapur", "Ramesh Bhosale");
        SARPANCH_NAMES.put("Rampur", "Vasant Kadam");
        SARPANCH_NAMES.put("Kondli", "Sunanda Pawar");
        SARPANCH_NAMES.put("Main St.", "Dilip Chavan");
        SARPANCH_NAMES.put("North Vill.", "Manisha Jadhav");
        SARPANCH_NAMES.put("East Ward", "Anil Thakur");
    }

    static {
        seedProjects();
        seedComplaints();
        seedReports();

        // ASSUMPTION: default selected project until ProjectManagement wires
        // real user selection (mirrors selectedVillage defaulting above).
        if (!PROJECTS.isEmpty()) {
            ProjectDataStore.selectedProject = PROJECTS.get(0);
        }
    }

    private VillageDataStore() {
        // static utility class
    }

    /* ============================================================
     *  SARPANCH LOOKUP
     * ============================================================ */

    /**
     * The Sarpanch's display name for the given village, or {@code null}
     * if the village is unrecognized, is {@link #ALL_VILLAGES}, or has
     * no Sarpanch on record. Callers (e.g. {@code ComplaintManagement})
     * are responsible for turning a null/blank result into whatever
     * "unassigned" fallback text they want to show.
     */
    public static String getSarpanchName(String village) {
        if (village == null || ALL_VILLAGES.equals(village)) return null;
        return SARPANCH_NAMES.get(village);
    }

    /* ============================================================
     *  RAW FILTERED ACCESS
     * ============================================================ */

    public static List<Project> getProjects(String village) {
        List<Project> result = new ArrayList<>();
        for (Project p : PROJECTS) {
            if (matchesVillage(p.getVillage(), village)) result.add(p);
        }
        return result;
    }

    /**
     * Complaints for the given village, or every complaint when village is
     * "All Villages" (or null). This is the single reusable village-filter
     * entry point every page should call - {@code ComplaintManagement}'s
     * own {@code getComplaintsForSelectedVillage()} delegates straight to
     * this method.
     */
    public static List<Complaint> getComplaints(String village) {
        List<Complaint> result = new ArrayList<>();
        for (Complaint c : COMPLAINTS) {
            if (matchesVillage(c.getVillage(), village)) result.add(c);
        }
        return result;
    }

    public static List<GeneratedReport> getReports(String village) {
        List<GeneratedReport> result = new ArrayList<>();
        for (GeneratedReport r : REPORTS) {
            if (matchesVillage(r.getVillage(), village)) result.add(r);
        }
        return result;
    }

    private static boolean matchesVillage(String recordVillage, String selectedVillage) {
        return selectedVillage == null
                || ALL_VILLAGES.equals(selectedVillage)
                || recordVillage.equals(selectedVillage);
    }

    /* ============================================================
     *  BUDGET AGGREGATES
     * ============================================================ */

    public static double getTotalBudget(String village) {
        double total = 0;
        for (Project p : getProjects(village)) total += p.getBudgetAllocated();
        return total;
    }

    public static double getUtilizedBudget(String village) {
        double total = 0;
        for (Project p : getProjects(village)) total += p.getBudgetUtilized();
        return total;
    }

    /** Fraction (0.0 - 1.0) of allocated budget that has been utilized. */
    public static double getBudgetUtilizationFraction(String village) {
        double allocated = getTotalBudget(village);
        if (allocated <= 0) return 0;
        return getUtilizedBudget(village) / allocated;
    }

    /** Village -> utilization fraction, used by the bar chart when "All Villages" is selected. */
    public static Map<String, Double> getBudgetUtilizationByVillage(List<String> villages) {
        Map<String, Double> result = new LinkedHashMap<>();
        for (String v : villages) {
            if (ALL_VILLAGES.equals(v)) continue;
            result.put(v, getBudgetUtilizationFraction(v));
        }
        return result;
    }

    /* ============================================================
     *  PROJECT AGGREGATES
     * ============================================================ */

    public static int getTotalProjectCount(String village) {
        return getProjects(village).size();
    }

    /** Percentage (0-100) of projects with status COMPLETED. */
    public static double getProjectSuccessRate(String village) {
        List<Project> projects = getProjects(village);
        if (projects.isEmpty()) return 0;
        long completed = projects.stream().filter(p -> p.getStatus() == Project.Status.COMPLETED).count();
        return (completed * 100.0) / projects.size();
    }

    public static double getAvgApprovalTimeDays(String village) {
        List<Project> projects = getProjects(village);
        if (projects.isEmpty()) return 0;
        double sum = 0;
        for (Project p : projects) sum += p.getApprovalDays();
        return sum / projects.size();
    }

    /** Category -> project count, used to drive the "Project Distribution" donut. */
    public static Map<String, Integer> getProjectCategoryDistribution(String village) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Project p : getProjects(village)) {
            result.merge(p.getCategory(), 1, Integer::sum);
        }
        return result;
    }

    /* ============================================================
     *  GRIEVANCE / COMPLAINT AGGREGATES
     * ============================================================ */

    /** Complaints that are still open (PENDING or IN_PROGRESS) for the given village. */
    public static int getActiveGrievances(String village) {
        List<Complaint> complaints = getComplaints(village);
        int active = 0;
        for (Complaint c : complaints) {
            if (c.getStatus() == Complaint.Status.PENDING || c.getStatus() == Complaint.Status.IN_PROGRESS) active++;
        }
        return active;
    }

    /** Percentage (0-100) of complaints that have been resolved. */
    public static double getGrievanceResolutionRate(String village) {
        List<Complaint> complaints = getComplaints(village);
        if (complaints.isEmpty()) return 0;
        long resolved = complaints.stream().filter(c -> c.getStatus() == Complaint.Status.RESOLVED).count();
        return (resolved * 100.0) / complaints.size();
    }

    /* ============================================================
     *  SAMPLE DATA SEEDING (swap for Firestore fetch when ready)
     * ============================================================ */

    private static void seedProjects() {
        PROJECTS.add(new Project("Sitapur Approach Road", "Sitapur", "Infrastructure", Project.Status.COMPLETED, 4_200_000, 4_050_000, 3));
        PROJECTS.add(new Project("Sitapur Overhead Tank", "Sitapur", "Water Supply", Project.Status.ONGOING, 2_800_000, 1_540_000, 6));
        PROJECTS.add(new Project("Sitapur Primary School Block", "Sitapur", "Education", Project.Status.COMPLETED, 1_900_000, 1_880_000, 4));
        PROJECTS.add(new Project("Sitapur Community Hall", "Sitapur", "Other", Project.Status.DELAYED, 1_200_000, 400_000, 11));

        PROJECTS.add(new Project("Rampur Drainage Line", "Rampur", "Infrastructure", Project.Status.COMPLETED, 3_600_000, 3_550_000, 2));
        PROJECTS.add(new Project("Rampur Borewell Cluster", "Rampur", "Water Supply", Project.Status.COMPLETED, 2_100_000, 2_050_000, 3));
        PROJECTS.add(new Project("Rampur Anganwadi Upgrade", "Rampur", "Education", Project.Status.ONGOING, 1_400_000, 900_000, 5));
        PROJECTS.add(new Project("Rampur Solar Streetlights", "Rampur", "Infrastructure", Project.Status.COMPLETED, 1_800_000, 1_760_000, 4));

        PROJECTS.add(new Project("Kondli Culvert Repair", "Kondli", "Infrastructure", Project.Status.DELAYED, 1_500_000, 300_000, 14));
        PROJECTS.add(new Project("Kondli Pipeline Extension", "Kondli", "Water Supply", Project.Status.ONGOING, 3_200_000, 1_100_000, 9));
        PROJECTS.add(new Project("Kondli Adult Literacy Center", "Kondli", "Education", Project.Status.COMPLETED, 900_000, 880_000, 3));

        PROJECTS.add(new Project("Main St. Market Paving", "Main St.", "Infrastructure", Project.Status.COMPLETED, 2_600_000, 2_540_000, 3));
        PROJECTS.add(new Project("Main St. Overhead Tank", "Main St.", "Water Supply", Project.Status.COMPLETED, 2_000_000, 1_980_000, 2));
        PROJECTS.add(new Project("Main St. Skill Center", "Main St.", "Education", Project.Status.ONGOING, 1_700_000, 700_000, 7));
        PROJECTS.add(new Project("Main St. Public Toilets", "Main St.", "Other", Project.Status.COMPLETED, 600_000, 590_000, 2));

        PROJECTS.add(new Project("North Vill. Check Dam", "North Vill.", "Water Supply", Project.Status.ONGOING, 3_900_000, 1_950_000, 8));
        PROJECTS.add(new Project("North Vill. Road Widening", "North Vill.", "Infrastructure", Project.Status.DELAYED, 2_400_000, 700_000, 16));
        PROJECTS.add(new Project("North Vill. School Renovation", "North Vill.", "Education", Project.Status.COMPLETED, 1_300_000, 1_270_000, 4));

        PROJECTS.add(new Project("East Ward Storm Drain", "East Ward", "Infrastructure", Project.Status.COMPLETED, 3_100_000, 3_040_000, 3));
        PROJECTS.add(new Project("East Ward Water Kiosk", "East Ward", "Water Supply", Project.Status.COMPLETED, 1_100_000, 1_080_000, 2));
        PROJECTS.add(new Project("East Ward Library", "East Ward", "Education", Project.Status.ONGOING, 800_000, 350_000, 6));
        PROJECTS.add(new Project("East Ward Panchayat Bhavan", "East Ward", "Other", Project.Status.ONGOING, 1_600_000, 640_000, 5));
    }

    private static void seedComplaints() {
        seedVillageComplaints("Sitapur", new Object[][]{
            {"CMP-1001", "Rakesh Kumar",   "Water Supply",           "Broken hand pump near primary school", Complaint.Priority.HIGH,     Complaint.Status.RESOLVED,    "05 Aug 2026", "Officer Nilesh Patil", "Hand pump replaced and tested", 3.0},
            {"CMP-1002", "Sunita Devi",    "Sanitation",             "Garbage not collected for over a week", Complaint.Priority.MEDIUM,   Complaint.Status.IN_PROGRESS, "10 Aug 2026", "Officer Nilesh Patil", null, null},
            {"CMP-1003", "Manoj Yadav",    "Electricity",            "Transformer sparking near market road", Complaint.Priority.CRITICAL, Complaint.Status.PENDING,     "13 Aug 2026", "Unassigned", null, null},
            {"CMP-1004", "Geeta Sharma",   "Road & Infrastructure",  "Pothole causing accidents on approach road", Complaint.Priority.MEDIUM, Complaint.Status.RESOLVED, "01 Aug 2026", "Officer Nilesh Patil", "Pothole filled and road resurfaced", 6.0},
            {"CMP-1005", "Vikram Singh",   "Water Supply",           "Low water pressure in eastern lane", Complaint.Priority.LOW,      Complaint.Status.REJECTED,    "28 Jul 2026", "Officer Nilesh Patil", "Duplicate of an existing scheme request", null},
        });

        seedVillageComplaints("Rampur", new Object[][]{
            {"CMP-1101", "Anita Kumari",   "Sanitation",             "Open drain overflowing near bus stand", Complaint.Priority.HIGH,    Complaint.Status.RESOLVED,    "03 Aug 2026", "Officer Priya Naik", "Drain cleared and covered", 2.0},
            {"CMP-1102", "Deepak Rao",     "Electricity",            "Streetlights not working - Ward 3", Complaint.Priority.MEDIUM,       Complaint.Status.IN_PROGRESS, "09 Aug 2026", "Officer Priya Naik", null, null},
            {"CMP-1103", "Kavita Joshi",   "Water Supply",           "Borewell motor burnt out", Complaint.Priority.CRITICAL,              Complaint.Status.PENDING,     "12 Aug 2026", "Unassigned", null, null},
            {"CMP-1104", "Suresh Patil",   "Road & Infrastructure",  "Culvert damaged after last rains", Complaint.Priority.HIGH,          Complaint.Status.PENDING,     "11 Aug 2026", "Officer Priya Naik", null, null},
        });

        seedVillageComplaints("Kondli", new Object[][]{
            {"CMP-1201", "Rahul Verma",    "Water Supply",           "Pipeline leak flooding the lane", Complaint.Priority.HIGH,           Complaint.Status.RESOLVED,    "04 Aug 2026", "Officer Aarti Deshmukh", "Pipeline section replaced", 4.0},
            {"CMP-1202", "Meena Kumari",   "Sanitation",             "No public toilet cleaning for 2 weeks", Complaint.Priority.MEDIUM,   Complaint.Status.RESOLVED,    "30 Jul 2026", "Officer Aarti Deshmukh", "Cleaning schedule restored", 8.0},
            {"CMP-1203", "Ashok Mishra",   "Electricity",            "Frequent power cuts in evenings", Complaint.Priority.MEDIUM,         Complaint.Status.IN_PROGRESS, "08 Aug 2026", "Officer Aarti Deshmukh", null, null},
            {"CMP-1204", "Nisha Gupta",    "Road & Infrastructure",  "Culvert repair request pending since June", Complaint.Priority.LOW,  Complaint.Status.PENDING,     "14 Aug 2026", "Unassigned", null, null},
            {"CMP-1205", "Om Prakash",     "Water Supply",           "Requesting a new hand pump - duplicate request", Complaint.Priority.LOW, Complaint.Status.REJECTED, "25 Jul 2026", "Officer Aarti Deshmukh", "Already scheduled under ongoing scheme", null},
        });

        seedVillageComplaints("Main St.", new Object[][]{
            {"CMP-1301", "Farhan Sheikh",  "Electricity",            "Exposed wiring near the market", Complaint.Priority.CRITICAL,        Complaint.Status.RESOLVED,    "06 Aug 2026", "Officer Rohan Kale", "Wiring insulated and secured", 1.0},
            {"CMP-1302", "Pooja Nair",     "Sanitation",             "Public toilets need urgent repair", Complaint.Priority.HIGH,          Complaint.Status.PENDING,     "13 Aug 2026", "Unassigned", null, null},
            {"CMP-1303", "Ganesh Iyer",    "Road & Infrastructure",  "Market paving cracked after monsoon", Complaint.Priority.MEDIUM,      Complaint.Status.IN_PROGRESS, "07 Aug 2026", "Officer Rohan Kale", null, null},
        });

        seedVillageComplaints("North Vill.", new Object[][]{
            {"CMP-1401", "Ritika Chauhan", "Water Supply",           "Check dam overflow damaging nearby fields", Complaint.Priority.HIGH, Complaint.Status.IN_PROGRESS, "09 Aug 2026", "Officer Sanjay Bhosale", null, null},
            {"CMP-1402", "Devendra Rathi", "Road & Infrastructure",  "Road widening work blocking access", Complaint.Priority.MEDIUM,       Complaint.Status.RESOLVED,    "02 Aug 2026", "Officer Sanjay Bhosale", "Temporary access lane opened", 5.0},
            {"CMP-1403", "Lata Pawar",     "Sanitation",             "Drainage backing up near school", Complaint.Priority.CRITICAL,        Complaint.Status.PENDING,     "12 Aug 2026", "Unassigned", null, null},
        });

        seedVillageComplaints("East Ward", new Object[][]{
            {"CMP-1501", "Imran Qureshi",  "Water Supply",           "Water kiosk tap broken", Complaint.Priority.MEDIUM,                  Complaint.Status.RESOLVED,    "31 Jul 2026", "Officer Kiran Shetty", "Tap fitting replaced", 3.0},
            {"CMP-1502", "Shalini Reddy",  "Road & Infrastructure",  "Storm drain blocked with debris", Complaint.Priority.HIGH,           Complaint.Status.PENDING,     "13 Aug 2026", "Unassigned", null, null},
            {"CMP-1503", "Vinod Kamble",   "Electricity",            "Panchayat Bhavan wiring needs inspection", Complaint.Priority.LOW,   Complaint.Status.IN_PROGRESS, "10 Aug 2026", "Officer Kiran Shetty", null, null},
            {"CMP-1504", "Sneha Gaikwad",  "Sanitation",             "Library restroom needs cleaning supplies", Complaint.Priority.LOW,   Complaint.Status.REJECTED,    "27 Jul 2026", "Officer Kiran Shetty", "Handled by facility staff directly", null},
        });

        seedSarpanchTargetedComplaints();
    }

    /**
     * Sample complaints filed AGAINST the Sarpanch, tied to the first
     * seeded project ("Sitapur Approach Road") so the Complaint
     * Management page's Sarpanch panel has visible sample data by
     * default. Target person is now the Sitapur Sarpanch's ACTUAL name
     * (from {@link #SARPANCH_NAMES}), matching how
     * {@code ComplaintManagement.isTargetedAtSarpanch()} checks name
     * first - the "Sarpanch" role tag is kept too, purely as the
     * fallback path. CMP-9004 is deliberately filed BY the Sarpanch and
     * must never appear in that panel - it exercises the self-filed
     * exclusion.
     */
    private static void seedSarpanchTargetedComplaints() {
        String firstProjectId = PROJECTS.isEmpty() ? null : PROJECTS.get(0).getProjectId();
        String sitapurSarpanch = SARPANCH_NAMES.get("Sitapur"); // "Ramesh Bhosale"

        COMPLAINTS.add(new Complaint(
                "CMP-9001", "Ramesh Bhosale", "Sitapur", "Road & Infrastructure",
                "Sarpanch allegedly diverted road-widening funds to a private contractor",
                Complaint.Priority.CRITICAL, Complaint.Status.PENDING, "15 Aug 2026",
                "Unassigned", null, null,
                firstProjectId, sitapurSarpanch, "Villager"));

        COMPLAINTS.add(new Complaint(
                "CMP-9002", null, "Sitapur", "Road & Infrastructure",
                "Anonymous tip: work on approach road stalled but bills already cleared",
                Complaint.Priority.HIGH, Complaint.Status.IN_PROGRESS, "12 Aug 2026",
                "Officer Nilesh Patil", null, null,
                firstProjectId, sitapurSarpanch, "Villager"));

        COMPLAINTS.add(new Complaint(
                "CMP-9003", "Sunil Gaikwad", "Sitapur", "Other",
                "Sarpanch reportedly favored relatives in project labor contracts",
                Complaint.Priority.MEDIUM, Complaint.Status.RESOLVED, "20 Jul 2026",
                "Officer Nilesh Patil", "Contracts reviewed; process corrected", 12.0,
                firstProjectId, sitapurSarpanch, "Villager"));

        // NOTE: filed BY the Sarpanch (name matches SARPANCH_NAMES.get("Sitapur"))
        // - must never appear in the "against Sarpanch" panel.
        COMPLAINTS.add(new Complaint(
                "CMP-9004", sitapurSarpanch, "Sitapur", "Water Supply",
                "Requesting urgent repair funds for overhead tank",
                Complaint.Priority.HIGH, Complaint.Status.PENDING, "14 Aug 2026",
                "Unassigned", null, null,
                firstProjectId, null, "Sarpanch"));
    }

    @SuppressWarnings("unchecked")
    private static void seedVillageComplaints(String village, Object[][] rows) {
        for (Object[] row : rows) {
            COMPLAINTS.add(new Complaint(
                    (String) row[0],                      // complaintId
                    (String) row[1],                       // citizenName
                    village,                                // village
                    (String) row[2],                        // category
                    (String) row[3],                        // description
                    (Complaint.Priority) row[4],             // priority
                    (Complaint.Status) row[5],               // status
                    (String) row[6],                         // dateFiled
                    (String) row[7],                         // assignedOfficer
                    (String) row[8],                         // resolution
                    (Double) row[9]                          // resolutionDays
            ));
        }
    }

    private static void seedReports() {
        REPORTS.add(new GeneratedReport("Sitapur Progress Report", "PDF", "Sitapur", "Oct 24, 2023"));
        REPORTS.add(new GeneratedReport("Sitapur Budget Summary", "XLSX", "Sitapur", "Oct 18, 2023"));

        REPORTS.add(new GeneratedReport("Rampur Progress Report", "PDF", "Rampur", "Oct 22, 2023"));
        REPORTS.add(new GeneratedReport("Rampur Grievance Log", "PDF", "Rampur", "Oct 12, 2023"));

        REPORTS.add(new GeneratedReport("Kondli Budget Summary", "XLSX", "Kondli", "Oct 20, 2023"));
        REPORTS.add(new GeneratedReport("Kondli Grievance Log", "PDF", "Kondli", "Oct 15, 2023"));

        REPORTS.add(new GeneratedReport("Main St. Progress Report", "PDF", "Main St.", "Oct 21, 2023"));
        REPORTS.add(new GeneratedReport("Main St. Budget Summary", "XLSX", "Main St.", "Oct 16, 2023"));

        REPORTS.add(new GeneratedReport("North Vill. Progress Report", "PDF", "North Vill.", "Oct 19, 2023"));
        REPORTS.add(new GeneratedReport("North Vill. Grievance Log", "PDF", "North Vill.", "Oct 11, 2023"));

        REPORTS.add(new GeneratedReport("East Ward Budget Summary", "XLSX", "East Ward", "Oct 23, 2023"));
        REPORTS.add(new GeneratedReport("East Ward Progress Report", "PDF", "East Ward", "Oct 14, 2023"));
    }
}