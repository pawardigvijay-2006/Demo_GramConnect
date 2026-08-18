package com.tech_fusion.view.sarpanch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * Project Updates — a visually rich, per-project timeline (Submitted -&gt;
 * Completed) with mandatory proof-of-expenditure upload before a stage can
 * be marked complete. Each project gets its own accent colour, a progress
 * bar, stage-specific icons, and a glowing active-stage indicator. Reuses
 * the shared sidebar/top-bar scaffold from ProjectTrackerActionPage.
 */
public class ProjectUpdatesPage extends ProjectTrackerActionPage {

    private static final String FOREST_DEEP = "#0B3D2E";
    private static final String FOREST_LIGHT = "#0F4736";
    private static final String SAFFRON = "#E07A1F";
    private static final String TEAL = "#0E8C8C";
    private static final String VIOLET = "#7C5CFC";
    private static final String FONT = "'Inter', 'Segoe UI', Arial, sans-serif";

    private static final String[] STAGES = {
        "Project Submitted", "Sarpanch Reviewed", "Gram Sabha Recommended",
        "Technical Verification", "Sanction Approval", "Fund Released",
        "Work In Progress", "Inspection", "Completed"
    };

    private static final String[] STAGE_ICONS = {
        "\uD83D\uDCDD", "\uD83D\uDD0D", "\uD83C\uDFDB", "\uD83E\uDDEA",
        "\u2705", "\uD83D\uDCB0", "\uD83D\uDEA7", "\uD83D\uDD75", "\uD83C\uDFC1"
    };

    /** Rotating accent per project card so the list feels varied, not uniform. */
    private static final String[] ACCENTS = { TEAL, VIOLET, SAFFRON };

    private static class Project {
        String name, location;
        int current;      // index into STAGES of the active (in-progress) stage
        boolean proofOn;  // proof uploaded for the current stage?
        File proofFile;   // the document the Sarpanch actually picked from disk
        boolean open;     // card expanded?
        Project(String n, String l, int c, boolean open) { name = n; location = l; current = c; this.open = open; }
    }

    // Static so a project the Sarpanch initializes on the Project Initialize
    // page is still here the next time this page is opened (each click on
    // the "Project Updates" tile builds a fresh ProjectUpdatesPage instance).
    private static final List<Project> projects = new ArrayList<>(List.of(
        new Project("Ward 4 Road Metaling", "Main Street", 6, true),
        new Project("Water Tank Renovation", "Near School Area", 7, false),
        new Project("Primary School Repair", "Gram Panchayat Office", 3, false)
    ));

    /**
     * Adds a newly initialized project to Project Updates at its first
     * stage. Called by {@code ProjectInitializePage} once the Sarpanch
     * initializes an approved project. Safe to call more than once for the
     * same project name — duplicates are skipped.
     */
    public static void addInitializedProject(String name, String location) {
        boolean alreadyPresent = projects.stream().anyMatch(p -> p.name.equals(name));
        if (!alreadyPresent) {
            projects.add(new Project(name, location, 0, false));
        }
    }

    public Scene getProjectUpdatesScene(Runnable backToProjectTrackerAction, Runnable backToDashboardAction) {
        return createActionScene(
            "Project Updates",
            "Track every project's journey from submission to completion, with proof of expenditure required at each stage.",
            buildProjectUpdates(),
            backToProjectTrackerAction,
            backToDashboardAction
        );
    }

    private VBox buildProjectUpdates() {
        VBox wrap = new VBox(22);
        wrap.getChildren().add(buildSummaryRow());
        VBox list = new VBox(20);
        for (Project p : projects) list.getChildren().add(buildProjectCard(p, list));
        wrap.getChildren().add(list);
        return wrap;
    }

    private void refresh(VBox list) {
        list.getChildren().clear();
        for (Project p : projects) list.getChildren().add(buildProjectCard(p, list));
    }

    /* ---------- Summary strip: quick stats across all projects ---------- */
    private HBox buildSummaryRow() {
        HBox row = new HBox(16);
        int total = projects.size();
        long done = projects.stream().filter(p -> p.current == STAGES.length - 1).count();
        double avgPct = projects.stream().mapToDouble(p -> (p.current + 1) * 100.0 / STAGES.length).average().orElse(0);

        row.getChildren().addAll(
            statCard("\uD83D\uDDC2", "TOTAL PROJECTS", String.valueOf(total), FOREST_DEEP),
            statCard("\uD83C\uDFC1", "COMPLETED", String.valueOf(done), TEAL),
            statCard("\uD83D\uDCC8", "AVG. PROGRESS", Math.round(avgPct) + "%", SAFFRON),
            statCard("\uD83D\uDCCE", "STAGES TRACKED", String.valueOf(STAGES.length), VIOLET)
        );
        for (var node : row.getChildren()) HBox.setHgrow(node, Priority.ALWAYS);
        return row;
    }

    private VBox statCard(String icon, String label, String value, String accent) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: rgba(255,255,255,0.90); -fx-background-radius: 16;" +
            " -fx-border-color: white; -fx-border-radius: 16;" +
            " -fx-effect: dropshadow(gaussian, rgba(11,61,46,0.07), 12, 0.1, 0, 3);");
        StackPane chip = chip(icon, accent, 36);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10.5px; -fx-font-weight: 800; -fx-text-fill: rgba(11,61,46,0.55); -fx-letter-spacing: 0.06em;");
        Label val = new Label(value);
        val.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: " + accent + ";");
        card.getChildren().addAll(chip, lbl, val);
        return card;
    }

    /* ---------- One project card: header + progress bar + (optional) timeline ---------- */
    private VBox buildProjectCard(Project p, VBox listWrap) {
        String accent = ACCENTS[projects.indexOf(p) % ACCENTS.length];
        double pct = (p.current + 1) * 100.0 / STAGES.length;
        boolean isComplete = p.current == STAGES.length - 1;

        VBox card = new VBox(16);
        card.setPadding(new Insets(24));
        String baseStyle = "-fx-background-color: rgba(255,255,255,0.93); -fx-background-radius: 18;" +
            " -fx-border-color: " + rgba(accent, 0.55) + " white white " + accent + "; -fx-border-radius: 18; -fx-border-width: 1 1 1 5;" +
            " -fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 14, 0.1, 0, 4);";
        card.setStyle(baseStyle);

        HBox head = new HBox(14);
        head.setAlignment(Pos.CENTER_LEFT);
        head.setCursor(Cursor.HAND);
        StackPane icon = chip(isComplete ? "\uD83C\uDFC1" : "\uD83D\uDDC2", accent, 44);

        VBox titleBox = new VBox(6);
        HBox nameRow = new HBox(10);
        nameRow.setAlignment(Pos.CENTER_LEFT);
        Label name = new Label(p.name);
        name.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 17px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Label statusPill = pill(isComplete ? "Completed" : "In Progress", isComplete ? TEAL : SAFFRON);
        nameRow.getChildren().addAll(name, statusPill);
        Label loc = new Label("\uD83D\uDCCD " + p.location + "   \u00B7   Stage " + (p.current + 1) + " of " + STAGES.length + ": " + STAGES[p.current]);
        loc.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.60);");

        HBox barRow = new HBox(10);
        barRow.setAlignment(Pos.CENTER_LEFT);
        Region track = new Region();
        track.setPrefHeight(8);
        track.setStyle("-fx-background-color: rgba(11,61,46,0.10); -fx-background-radius: 999;");
        HBox.setHgrow(track, Priority.ALWAYS);
        StackPane fillWrap = new StackPane(track);
        Region fill = new Region();
        fill.setPrefHeight(8);
        fill.setMaxWidth(Region.USE_PREF_SIZE);
        fill.setStyle("-fx-background-color: linear-gradient(to right, " + accent + ", " + FOREST_LIGHT + "); -fx-background-radius: 999;");
        StackPane.setAlignment(fill, Pos.CENTER_LEFT);
        fillWrap.getChildren().add(fill);
        // bind width proportionally via listener on the track's actual layout width
        track.widthProperty().addListener((obs, o, n) -> fill.setPrefWidth(n.doubleValue() * pct / 100.0));
        Label pctLbl = new Label(Math.round(pct) + "%");
        pctLbl.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: " + accent + ";");
        barRow.getChildren().addAll(fillWrap, pctLbl);

        titleBox.getChildren().addAll(nameRow, loc, barRow);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        Label chevron = new Label(p.open ? "\u25B4" : "\u25BE");
        chevron.setPadding(new Insets(8));
        chevron.setStyle("-fx-font-size: 15px; -fx-text-fill: " + FOREST_DEEP + "; -fx-background-color: " + rgba(accent, 0.10) + "; -fx-background-radius: 999;");

        head.getChildren().addAll(icon, titleBox, chevron);
        head.setOnMouseClicked(e -> { p.open = !p.open; refresh(listWrap); });

        card.getChildren().add(head);
        if (p.open) {
            Region divider = new Region();
            divider.setPrefHeight(1);
            divider.setStyle("-fx-background-color: rgba(11,61,46,0.10);");
            card.getChildren().addAll(divider, buildTimeline(p, listWrap, accent));
        }

        String hoverStyle = baseStyle + " -fx-effect: dropshadow(gaussian, " + rgba(accent, 0.35) + ", 20, 0.15, 0, 6);";
        card.setOnMouseEntered(e -> card.setStyle(hoverStyle));
        card.setOnMouseExited(e -> card.setStyle(baseStyle));

        return card;
    }

    /** Vertical stepper: gradient rail, glowing pulsed dot for the active stage, numbered dots for pending ones,
     *  and each stage wrapped in its own soft content card so the journey reads like a modern order-tracker. */
    private VBox buildTimeline(Project p, VBox listWrap, String accent) {
        VBox timeline = new VBox(0);
        timeline.setPadding(new Insets(16, 0, 0, 4));
        for (int i = 0; i < STAGES.length; i++) {
            boolean done = i < p.current, active = i == p.current, last = i == STAGES.length - 1;
            HBox row = new HBox(16);
            row.setAlignment(Pos.TOP_LEFT);

            // ---- rail: dot (with soft outer glow ring for the active stage) + connecting line ----
            StackPane dotWrap = new StackPane();
            dotWrap.setMinSize(38, 38);
            dotWrap.setPrefSize(38, 38);
            if (active) {
                Region ring = new Region();
                ring.setPrefSize(38, 38);
                ring.setMinSize(38, 38);
                ring.setStyle("-fx-background-color: " + rgba(SAFFRON, 0.16) + "; -fx-background-radius: 999;" +
                    " -fx-effect: dropshadow(gaussian, " + rgba(SAFFRON, 0.5) + ", 14, 0.35, 0, 0);");
                dotWrap.getChildren().add(ring);
            }
            Label dot = new Label(done ? "\u2713" : active ? STAGE_ICONS[i] : String.valueOf(i + 1));
            dot.setMinSize(30, 30);
            dot.setMaxSize(30, 30);
            dot.setAlignment(Pos.CENTER);
            String dotStyle = "-fx-background-radius: 999; -fx-font-size: 13px; -fx-font-weight: 900;";
            if (done) {
                dotStyle += " -fx-background-color: linear-gradient(to bottom right, " + accent + ", " + FOREST_LIGHT + ");" +
                    " -fx-text-fill: white; -fx-effect: dropshadow(gaussian, " + rgba(accent, 0.45) + ", 8, 0.2, 0, 2);";
            } else if (active) {
                dotStyle += " -fx-background-color: linear-gradient(to bottom right, " + SAFFRON + ", #C96214);" +
                    " -fx-text-fill: white; -fx-effect: dropshadow(gaussian, " + rgba(SAFFRON, 0.55) + ", 8, 0.25, 0, 2);";
            } else {
                dotStyle += " -fx-background-color: white; -fx-border-color: rgba(11,61,46,0.20); -fx-border-width: 1.6;" +
                    " -fx-border-radius: 999; -fx-text-fill: rgba(11,61,46,0.40);";
            }
            dot.setStyle(dotStyle);
            dotWrap.getChildren().add(dot);

            VBox rail = new VBox(0);
            rail.setAlignment(Pos.TOP_CENTER);
            Region line = new Region();
            line.setPrefWidth(3);
            line.setMinHeight(last ? 0 : (active ? 78 : 46));
            String lineTop = done ? accent : active ? rgba(SAFFRON, 0.5) : "rgba(11,61,46,0.14)";
            String lineBottom = (i + 1 <= p.current) ? accent : "rgba(11,61,46,0.14)";
            line.setStyle("-fx-background-color: linear-gradient(to bottom, " + lineTop + ", " + lineBottom + ");" +
                " -fx-background-radius: 3;");
            rail.getChildren().addAll(dotWrap, line);

            // ---- content card ----
            VBox stageCard = new VBox(6);
            stageCard.setPadding(new Insets(active ? 14 : 10, 16, active ? 16 : 10, 16));
            stageCard.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(stageCard, Priority.ALWAYS);
            if (active) {
                stageCard.setStyle("-fx-background-color: " + rgba(SAFFRON, 0.08) + "; -fx-background-radius: 14;" +
                    " -fx-border-color: " + rgba(SAFFRON, 0.30) + "; -fx-border-width: 1; -fx-border-radius: 14;");
            } else if (done) {
                stageCard.setStyle("-fx-background-color: " + rgba(accent, 0.05) + "; -fx-background-radius: 14;");
            }

            HBox nameRow = new HBox(8);
            nameRow.setAlignment(Pos.CENTER_LEFT);
            Label stageName = new Label(STAGES[i]);
            stageName.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 14px; -fx-font-weight: " +
                (active ? "800" : "700") + "; -fx-text-fill: " + (done ? accent : active ? FOREST_DEEP : "rgba(11,61,46,0.42)") + ";");
            nameRow.getChildren().add(stageName);
            if (active) {
                Label activeTag = new Label("CURRENT STAGE");
                activeTag.setPadding(new Insets(3, 9, 3, 9));
                activeTag.setStyle("-fx-background-color: " + SAFFRON + "; -fx-background-radius: 999; -fx-font-family: " + FONT +
                    "; -fx-font-size: 9.5px; -fx-font-weight: 900; -fx-text-fill: white; -fx-letter-spacing: 0.06em;");
                nameRow.getChildren().add(activeTag);
            } else if (done) {
                Label doneTag = new Label("\u2713 Verified");
                doneTag.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 10.5px; -fx-font-weight: 800; -fx-text-fill: " + accent + ";");
                nameRow.getChildren().add(doneTag);
            }
            stageCard.getChildren().add(nameRow);

            if (done) {
                Label proofNote = new Label("\uD83D\uDCCE Proof of expenditure verified");
                proofNote.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11.5px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.55);");
                stageCard.getChildren().add(proofNote);
            } else if (active) {
                stageCard.getChildren().add(buildActiveStageControls(p, listWrap));
            }

            VBox rowContentPad = new VBox(stageCard);
            rowContentPad.setPadding(new Insets(0, 0, 16, 0));
            row.getChildren().addAll(rail, rowContentPad);
            timeline.getChildren().add(row);
        }
        return timeline;
    }

    /** Upload-proof + mark-complete controls for the single active stage of a project. */
    private HBox buildActiveStageControls(Project p, VBox listWrap) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 0, 0, 0));

        Label uploadBtn = new Label(p.proofOn ? "\u2705  " + p.proofFile.getName() : "\uD83D\uDCCE  Upload Proof of Expenditure");
        uploadBtn.setPadding(new Insets(9, 16, 9, 16));
        uploadBtn.setMaxWidth(260);
        uploadBtn.setCursor(Cursor.HAND);
        String upStyle = "-fx-background-radius: 10; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-cursor: hand;" +
            (p.proofOn ? " -fx-background-color: rgba(14,140,140,0.14); -fx-text-fill: " + TEAL + "; -fx-border-color: rgba(14,140,140,0.35); -fx-border-radius: 10; -fx-border-width: 1;"
                       : " -fx-background-color: rgba(124,92,252,0.12); -fx-text-fill: " + VIOLET + "; -fx-border-color: rgba(124,92,252,0.35); -fx-border-radius: 10; -fx-border-width: 1;");
        uploadBtn.setStyle(upStyle);
        uploadBtn.setOnMouseClicked(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Upload Proof of Expenditure - " + STAGES[p.current]);
            chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Documents & Images", "*.pdf", "*.doc", "*.docx", "*.jpg", "*.jpeg", "*.png"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            File file = chooser.showOpenDialog(uploadBtn.getScene() == null ? null : uploadBtn.getScene().getWindow());
            if (file != null) {
                p.proofFile = file;
                p.proofOn = true;
                refresh(listWrap);
            }
        });

        Label completeBtn = new Label("\u2192  Mark Stage Complete");
        completeBtn.setPadding(new Insets(9, 18, 9, 18));
        boolean canComplete = p.proofOn;
        completeBtn.setStyle("-fx-background-radius: 10; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: white;" +
            (canComplete ? " -fx-background-color: linear-gradient(to right, " + SAFFRON + ", " + FOREST_DEEP + "); -fx-cursor: hand;" +
                           " -fx-effect: dropshadow(gaussian, " + rgba(SAFFRON, 0.40) + ", 10, 0.15, 0, 3);"
                         : " -fx-background-color: rgba(11,61,46,0.22); -fx-cursor: default;"));
        if (canComplete) {
            completeBtn.setCursor(Cursor.HAND);
            completeBtn.setOnMouseClicked(e -> {
                if (p.current < STAGES.length - 1) p.current++;
                p.proofOn = false;
                p.proofFile = null;
                refresh(listWrap);
            });
        }

        Label hint = new Label(canComplete ? "Ready to advance" : "Upload proof to unlock this action");
        hint.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 600; -fx-text-fill: " +
            (canComplete ? TEAL : "rgba(11,61,46,0.45)") + ";");

        row.getChildren().addAll(uploadBtn, completeBtn, hint);
        return row;
    }

    private StackPane chip(String icon, String accent, double size) {
        StackPane s = new StackPane();
        s.setPrefSize(size, size);
        s.setMinSize(size, size);
        s.setStyle("-fx-background-color: " + rgba(accent, 0.14) + "; -fx-background-radius: 13;");
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: " + (size * 0.42) + "px; -fx-text-fill: " + accent + ";");
        s.getChildren().add(ic);
        return s;
    }

    private Label pill(String text, String accent) {
        Label l = new Label(text);
        l.setPadding(new Insets(4, 12, 4, 12));
        l.setStyle("-fx-background-color: " + rgba(accent, 0.16) + "; -fx-background-radius: 999; -fx-font-family: " + FONT +
            "; -fx-font-size: 10.5px; -fx-font-weight: 800; -fx-text-fill: " + accent + ";");
        return l;
    }

    private String rgba(String hex, double alpha) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
    }
}