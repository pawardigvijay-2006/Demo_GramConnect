package com.tech_fusion.view.sarpanch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Project Initialize — lists every project approved via the Project
 * Management page (Admin Login) and lets the Sarpanch select and initialize
 * one or more of them. Once initialized, a project is handed off to {@link
 * ProjectUpdatesPage#addInitializedProject(String, String)} so it appears
 * there immediately, listed among the other tracked projects.
 *
 * <p>The approved-project data itself currently comes from {@link
 * ApprovedProjectsRepository}, a local placeholder — Admin Login and
 * Sarpanch Login are not wired together yet. See the Javadoc on that class
 * for how the real connection is meant to slot in later without touching
 * this page.
 */
public class ProjectInitializePage extends ProjectTrackerActionPage {

    private static final String FOREST_DEEP = "#0B3D2E";
    private static final String SAFFRON = "#E07A1F";
    private static final String TEAL = "#0E8C8C";
    private static final String FONT = "'Inter', 'Segoe UI', Arial, sans-serif";

    private final List<ApprovedProject> approvedProjects = ApprovedProjectsRepository.getApprovedProjects();

    /** projectId -> its row's checkbox, so we can read/disable selections on Initialize. */
    private final Map<String, CheckBox> checkboxByProjectId = new LinkedHashMap<>();

    /** projectIds the Sarpanch has already initialized this session. */
    private final Map<String, Boolean> initializedByProjectId = new LinkedHashMap<>();

    private VBox listBox;
    private Label feedback;

    public Scene getProjectInitializeScene(Runnable backToProjectTrackerAction, Runnable backToDashboardAction) {
        return createActionScene(
            "Project Initialize",
            "Review projects approved via Admin Login and initialize the ones ready to begin tracking.",
            buildProjectInitialize(),
            backToProjectTrackerAction,
            backToDashboardAction
        );
    }

    private VBox buildProjectInitialize() {
        VBox wrap = new VBox(22);

        VBox card = card();
        card.getChildren().add(sectionHeading(
            "Approved Projects",
            "These projects were approved on the Project Management page. Select one or more and initialize them to start tracking."
        ));

        listBox = new VBox(14);
        refreshList();
        card.getChildren().add(listBox);

        feedback = feedbackLabel();
        HBox actionRow = new HBox(14);
        actionRow.setAlignment(Pos.CENTER_LEFT);
        Label initializeBtn = actionButton("\u2192  Initialize Selected Projects", TEAL);
        initializeBtn.setOnMouseClicked(e -> handleInitialize());
        actionRow.getChildren().addAll(initializeBtn, feedback);
        card.getChildren().add(actionRow);

        wrap.getChildren().add(card);
        return wrap;
    }

    private void refreshList() {
        listBox.getChildren().clear();
        checkboxByProjectId.clear();
        if (approvedProjects.isEmpty()) {
            listBox.getChildren().add(mutedNote("No approved projects are available to initialize yet."));
            return;
        }
        for (ApprovedProject p : approvedProjects) {
            listBox.getChildren().add(buildProjectRow(p));
        }
    }

    private HBox buildProjectRow(ApprovedProject p) {
        boolean initialized = initializedByProjectId.getOrDefault(p.projectId, false);

        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16));
        row.setStyle("-fx-background-color: rgba(11,61,46,0.05); -fx-background-radius: 12;");

        CheckBox checkBox = new CheckBox();
        checkBox.setDisable(initialized);
        checkboxByProjectId.put(p.projectId, checkBox);

        VBox details = new VBox(4);
        Label name = new Label(p.projectName + "  \u00B7  " + p.projectId);
        name.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Label meta = new Label(p.village + " \u2014 " + p.locality + "   \u00B7   " + p.department + "   \u00B7   " + p.budget + "   \u00B7   Approved " + p.date);
        meta.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.60);");
        details.getChildren().addAll(name, meta);
        HBox.setHgrow(details, Priority.ALWAYS);

        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);

        Label statusPill = new Label(initialized ? "Initialized" : "Not Initialized");
        statusPill.setPadding(new Insets(5, 12, 5, 12));
        statusPill.setStyle("-fx-background-color: " + rgba(initialized ? TEAL : SAFFRON, 0.16) +
            "; -fx-background-radius: 999; -fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: " +
            (initialized ? TEAL : SAFFRON) + ";");

        row.getChildren().addAll(checkBox, details, grow, statusPill);
        return row;
    }

    private void handleInitialize() {
        int initializedCount = 0;
        for (ApprovedProject p : approvedProjects) {
            CheckBox checkBox = checkboxByProjectId.get(p.projectId);
            if (checkBox != null && checkBox.isSelected() && !checkBox.isDisabled()) {
                ProjectUpdatesPage.addInitializedProject(p.projectName, p.locality);
                initializedByProjectId.put(p.projectId, true);
                initializedCount++;
            }
        }
        if (initializedCount == 0) {
            feedback.setText("Select at least one project to initialize.");
        } else {
            feedback.setText(initializedCount + " project" + (initializedCount == 1 ? "" : "s") +
                " initialized and added to Project Updates.");
        }
        refreshList();
    }

    /* ---------- local styling helpers, matching the other action pages ---------- */

    private VBox card() {
        VBox card = new VBox(18);
        card.setPadding(new Insets(28));
        card.setStyle("-fx-background-color: rgba(255,255,255,0.92); -fx-background-radius: 20; -fx-border-color: white; -fx-border-radius: 20;" +
            " -fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 16, 0.1, 0, 4);");
        return card;
    }

    private VBox sectionHeading(String title, String description) {
        VBox box = new VBox(4);
        Label name = new Label(title);
        name.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 21px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        box.getChildren().addAll(name, mutedNote(description));
        return box;
    }

    private Label mutedNote(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.68);");
        return label;
    }

    private Label feedbackLabel() {
        Label label = new Label();
        label.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + TEAL + ";");
        return label;
    }

    private Label actionButton(String text, String color) {
        Label button = new Label(text);
        button.setPadding(new Insets(10, 16, 10, 16));
        button.setCursor(javafx.scene.Cursor.HAND);
        button.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 9; -fx-text-fill: white; -fx-font-family: " + FONT +
            "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-cursor: hand;");
        return button;
    }

    private String rgba(String hex, double alpha) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
    }
}