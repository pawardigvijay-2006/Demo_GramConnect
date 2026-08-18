package com.tech_fusion.view.sarpanch;

/**
 * Framework-agnostic shape for a single "Approved" project.
 *
 * The fields deliberately mirror {@code ProjectManagement.Project} (village,
 * locality, department, budget, date) so that when Admin Login and Sarpanch
 * Login are wired together, mapping one to the other is a straight
 * field-for-field copy rather than a redesign. This class lives outside
 * both the {@code view.admin} and {@code view.sarpanch} packages so neither
 * side has to depend on the other's internals to use it.
 */
public class ApprovedProject {

    public final String projectName;
    public final String projectId;
    public final String village;
    public final String locality;
    public final String department;
    public final String budget;
    public final String date;

    public ApprovedProject(String projectName, String projectId, String village, String locality,
                            String department, String budget, String date) {
        this.projectName = projectName;
        this.projectId = projectId;
        this.village = village;
        this.locality = locality;
        this.department = department;
        this.budget = budget;
        this.date = date;
    }
}