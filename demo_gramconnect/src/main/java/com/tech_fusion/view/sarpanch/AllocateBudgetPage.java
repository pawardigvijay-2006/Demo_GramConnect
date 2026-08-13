package com.sarpanch.view;

import javafx.scene.Scene;

/** Page for allocating approved budget to a project. */
public class AllocateBudgetPage extends ProjectTrackerActionPage {

    public Scene getAllocateBudgetScene(Runnable backToProjectTrackerAction,
                                        Runnable backToDashboardAction) {
        return createActionScene(
            "Allocate Budget",
            "Assign approved funds to a project and its category.",
            buildAllocateBudget(),
            backToProjectTrackerAction,
            backToDashboardAction
        );
    }
}
