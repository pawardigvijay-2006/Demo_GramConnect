package com.tech_fusion.view.sarpanch;

import javafx.scene.Scene;

/** Page for displaying AI risk analysis and recommendations. */
public class ViewAiAnalysisPage extends ProjectTrackerActionPage {

    public Scene getViewAiAnalysisScene(Runnable backToProjectTrackerAction,
                                        Runnable backToDashboardAction) {
        return createActionScene(
            "View AI Analysis",
            "Review AI-generated project risks and recommendations.",
            buildAiAnalysis(),
            backToProjectTrackerAction,
            backToDashboardAction
        );
    }
}
