package com.tech_fusion.view.sarpanch;

import javafx.scene.Scene;

/** Page for validating GPS and timestamp evidence. */
public class VerifyGpsTimestampPage extends ProjectTrackerActionPage {

    public Scene getVerifyGpsTimestampScene(Runnable backToProjectTrackerAction,
                                            Runnable backToDashboardAction) {
        return createActionScene(
            "Verify GPS & Timestamp",
            "Validate site evidence before accepting an update.",
            buildGpsVerification(),
            backToProjectTrackerAction,
            backToDashboardAction
        );
    }
}
