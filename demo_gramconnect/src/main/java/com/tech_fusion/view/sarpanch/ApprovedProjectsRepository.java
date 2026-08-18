package com.tech_fusion.view.sarpanch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Source of "Approved" projects for the Sarpanch's Project Initialize page.
 *
 * <p><b>Not yet wired to Admin Login.</b> {@code com.tech_fusion.view.admin.
 * ProjectManagement} currently owns its own private, in-memory
 * {@code ALL_PROJECTS} list and flips a project's status to "Approved"
 * locally when the admin clicks "Approve" on the Project Management page.
 * That data doesn't leave {@code ProjectManagement} today.
 *
 * <p>This class exists so connecting the two is a one-line change later:
 * once Admin Login and Sarpanch Login are wired together, replace the body
 * of {@link #getApprovedProjects()} with whatever real fetch reaches
 * {@code ProjectManagement}'s approved projects (a shared static accessor,
 * a small service class, a Firestore query — whatever the eventual data
 * layer turns out to be). Everything downstream — {@code
 * ProjectInitializePage}, its checkboxes, and the hand-off into {@code
 * ProjectUpdatesPage} — only ever talks to {@link ApprovedProject} objects,
 * never to {@code ProjectManagement.Project} directly, so nothing else
 * needs to change when that swap happens.
 */
public final class ApprovedProjectsRepository {

    private ApprovedProjectsRepository() {
    }

    public static List<ApprovedProject> getApprovedProjects() {
        // Placeholder data only, deliberately shaped like the "Approved"
        // rows in ProjectManagement.ALL_PROJECTS (e.g. #PRJ-089, #PRJ-110).
        // Swap this list body for the real fetch when the two logins are
        // connected — callers below don't need to change.
        return new ArrayList<>(Arrays.asList(
            new ApprovedProject("Village Road Construction", "#PRJ-089", "Rampur",
                "Main Street", "Rural Development", "\u20B91,20,000", "24 May 2025"),
            new ApprovedProject("Gopalganj Road Widening", "#PRJ-110", "Gopalganj",
                "NH Link Road", "Rural Development", "\u20B96,20,000", "20 May 2025")
        ));
    }
}