package com.sarpanch.view;

import javafx.scene.Scene;

/** Page for browsing, editing, and approving completion of projects. */
public class ViewAllProjectsPage extends ProjectTrackerActionPage {

    public Scene getViewAllProjectsScene(Runnable backToProjectTrackerAction,
                                         Runnable backToDashboardAction) {
        return createActionScene(
            "View All Projects",
            "Browse projects, make required changes, and approve completion.",
            buildViewAllProjects(),
            backToProjectTrackerAction,
            backToDashboardAction
        );
    }
}
