package com.sarpanch.view;

import javafx.scene.Scene;

/** Page for reviewing requested budget revisions. */
public class ApproveBudgetChangesPage extends ProjectTrackerActionPage {

    public Scene getApproveBudgetChangesScene(Runnable backToProjectTrackerAction,
                                              Runnable backToDashboardAction) {
        return createActionScene(
            "Approve Budget Changes",
            "Review and decide on requested budget revisions.",
            buildBudgetChanges(),
            backToProjectTrackerAction,
            backToDashboardAction
        );
    }
}
