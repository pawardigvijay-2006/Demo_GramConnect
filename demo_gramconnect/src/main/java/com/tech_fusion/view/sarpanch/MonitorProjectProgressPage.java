package com.sarpanch.view;

import javafx.scene.Scene;

/** Page for tracking milestones, progress, and delays. */
public class MonitorProjectProgressPage extends ProjectTrackerActionPage {

    public Scene getMonitorProjectProgressScene(Runnable backToProjectTrackerAction,
                                                Runnable backToDashboardAction) {
        return createActionScene(
            "Monitor Project Progress",
            "Track milestones, progress, and reported delays.",
            buildProgressMonitor(),
            backToProjectTrackerAction,
            backToDashboardAction
        );
    }
}
