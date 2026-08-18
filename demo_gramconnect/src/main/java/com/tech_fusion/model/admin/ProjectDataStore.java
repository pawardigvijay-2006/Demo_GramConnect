package com.tech_fusion.model.admin;
 
/**
 * Single source of truth for "which project is currently selected"
 * block-wide - mirrors {@link VillageDataStore#selectedVillage}.
 *
 * Any page that lets the user pick a project (e.g. ProjectManagement)
 * should write to {@link #selectedProject} on selection; any page that
 * needs to know the active project (e.g. ComplaintManagement's Sarpanch
 * panel) reads it from here.
 */
public final class ProjectDataStore {
 
    /** Currently selected project across the app. Null until one is chosen. */
    public static Project selectedProject;
 
    private ProjectDataStore() {
        // static utility class
    }
}
 