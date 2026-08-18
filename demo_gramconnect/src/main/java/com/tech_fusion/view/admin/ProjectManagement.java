package com.tech_fusion.view.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * GramConnect - Project Management Page
 * ------------------------------------------------------------------
 * Same glass / forest-saffron template as {@link Dashboard} and
 * {@link BudgetManagment}. Navigates through the shared
 * {@code Dashboard.myStage} using Runnable callbacks.
 *
 * Every table, KPI card, and count on this page is now driven by
 * {@code Dashboard.selectedVillage} — the single source of truth for
 * village scope, shared with the sidebar "Managing Village" switcher.
 * Selecting a village here (or on any other page) filters everything
 * below via {@link #getProjectsForSelectedVillage()} and triggers
 * {@link #refreshVillageData()}, which clears and repopulates the
 * KPI row, Approved Projects panel, Pending Approval panel, and the
 * Project Inventory table in place — no UI structure, styling, or
 * layout changes from the previous version.
 */
public class ProjectManagement extends Application {

    /* ---------- Shared color palette (identical across all pages) ---------- */
    private static final String FOREST_DEEP   = "#0B3D2E";
    private static final String FOREST_LIGHT  = "#0F4736";
    private static final String SAFFRON_MAIN  = "#E07A1F";
    private static final String CONTEXT_TEAL  = "#0E8C8C";
    private static final String AI_VIOLET     = "#7C5CFC";
    private static final String DELAYED_RED   = "#D94C38";
    private static final String SIDEBAR_TOP   = "#CDEBD8";
    private static final String SIDEBAR_MID   = "#Bce3cc";
    private static final String SIDEBAR_BOT   = "#A9D8BD";
    private static final String FONT_FAMILY   = "'Inter', 'Segoe UI', 'Arial', sans-serif";

    private static final String BACKGROUND_IMAGE_PATH =
            "demo_gramconnect\\src\\main\\resources\\assets\\images\\WhatsApp Image 2026-08-10 at 11.55.38 PM.jpeg";

    /* ============================================================
     *  DATA MODEL
     * ============================================================ */

    /**
     * Plain project record. Deliberately flat / framework-agnostic so the
     * local {@link #ALL_PROJECTS} list below can later be swapped for a
     * Firebase Firestore query that returns the same shape — the rest of
     * this class (filtering, KPI math, table rendering) doesn't need to
     * change at all.
     */
    private static class Project {
        final String projectName;
        final String projectId;
        final String village;
        final String locality;   // sub-address shown under the project name
        final String department;
        final String budget;
        final String status;     // "Pending" | "Approved" | "In Progress" | "Completed" | "Delayed"
        final String date;
        final String priority;   // only meaningful for "Pending" projects: "HIGH PRIORITY" | "MEDIUM" | "NORMAL"

        /*
         * ---- Fields aligned with CreateProjectPage (Sarpanch Login) ----
         * These use the exact same names/shape as the fields collected on
         * CreateProjectPage's "Project Details" / "Budget & Timeline" form
         * (CATEGORY, LOCATION / WARD, EXPECTED BUDGET, EXPECTED DURATION).
         * Nothing sends real data into these yet — that wiring comes later —
         * but keeping the shape identical here means that whenever
         * CreateProjectPage (and eventually the Villager Login's New
         * Complaints Page -> SarpanchComplaintsPage) starts submitting real
         * data, the Pending Approval panel below can consume it as-is,
         * with no field renaming/mapping required.
         */
        final String category;         // e.g. "Roads", "Water Supply", "Electricity", "Sanitation", "Education", "Other"
        final String location;         // free-text village/ward, as typed on CreateProjectPage
        final String expectedBudget;   // e.g. "Rs. 1,50,000"
        final String expectedDuration; // e.g. "3 Months"

        /** Full constructor — use this once a project actually carries data submitted from CreateProjectPage. */
        Project(String projectName, String projectId, String village, String locality, String department,
                String budget, String status, String date, String priority,
                String category, String location, String expectedBudget, String expectedDuration) {
            this.projectName = projectName;
            this.projectId = projectId;
            this.village = village;
            this.locality = locality;
            this.department = department;
            this.budget = budget;
            this.status = status;
            this.date = date;
            this.priority = priority;
            this.category = category;
            this.location = location;
            this.expectedBudget = expectedBudget;
            this.expectedDuration = expectedDuration;
        }

        /**
         * Back-compat constructor for sample/legacy data that hasn't been given
         * explicit category/location/expectedDuration yet. Derives reasonable
         * fallbacks from the existing fields so the Approved Projects panel and
         * Project Inventory table (which don't use the new fields) keep working
         * unchanged.
         */
        Project(String projectName, String projectId, String village, String locality, String department,
                String budget, String status, String date, String priority) {
            this(projectName, projectId, village, locality, department, budget, status, date, priority,
                    department, village + ", " + locality, budget, "\u2014");
        }
    }

    /** A villager-submitted report — title + description, same shape as ReportProject.java's form fields. Not wired to it yet. */
    private static class Report {
        final String projectId;
        final String title;
        final String description;

        Report(String projectId, String title, String description) {
            this.projectId = projectId;
            this.title = title;
            this.description = description;
        }
    }

    /** Mock reports keyed by projectId, standing in for a future ComplaintService/Firestore query. */
    private static final List<Report> ALL_REPORTS = new ArrayList<>(Arrays.asList(
            new Report("#PRJ-089", "Pothole near main gate",
                    "There is a large pothole forming right at the entrance of the site, near Main Street. It's getting worse after the rain and is a hazard for two-wheelers."),
            new Report("#PRJ-089", "Debris left on roadside",
                    "Construction debris has been piled up on the roadside for over a week and hasn't been cleared, blocking part of the walking path."),
            new Report("#PRJ-110", "Uneven road widening work",
                    "The widened section near the NH link road has an uneven surface with a sudden drop on one side, which is risky for vehicles at night.")
    ));

    /** Returns every report filed against a given project, oldest-submission order preserved as stored. */
    private List<Report> getReportsForProject(String projectId) {
        List<Report> matched = new ArrayList<>();
        for (Report r : ALL_REPORTS) {
            if (r.projectId.equals(projectId)) matched.add(r);
        }
        return matched;
    }

    /**
     * Full-page Scene listing every villager-submitted report for one
     * Approved project (title + description). Opened from the "View
     * Reports" action in the Project Inventory table. Intentionally not
     * wired to ReportProject.java / ProjectTransparency1.java yet — this
     * only renders {@link #ALL_REPORTS} mock data in the same shape those
     * pages will eventually submit/display.
     */
    private Scene buildProjectReportsScene(Project p) {
        BorderPane root = new BorderPane();
        root.setBackground(buildBackground());
        root.setLeft(buildSidebar());

        BorderPane contentArea = new BorderPane();
        contentArea.setTop(buildTopBar());

        VBox main = new VBox(20);
        main.setPadding(new Insets(32, 40, 48, 40));
        main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");

        Label back = new Label("\u2190  Back to Project Management");
        back.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;");
        back.setOnMouseClicked(e -> Dashboard.myStage.setScene(getProjectManagementScene()));

        Label title = new Label("Reports \u2014 " + p.projectName);
        title.setWrapText(true);
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        VBox listCard = new VBox(14);
        listCard.setPadding(new Insets(28));
        listCard.setStyle(cardStyle(24));

        List<Report> reports = getReportsForProject(p.projectId);
        if (reports.isEmpty()) {
            listCard.getChildren().add(emptyState("No reports have been filed for this project yet."));
        } else {
            for (Report r : reports) {
                listCard.getChildren().add(reportCard(r));
            }
        }
        addHoverLift(listCard, 24);

        main.getChildren().addAll(back, title, listCard);
        ScrollPane scroller = new ScrollPane(main);
        scroller.setFitToWidth(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        contentArea.setCenter(scroller);

        root.setCenter(contentArea);
        return new Scene(root, 1500, 820);
    }

    /** One report's title + description, rendered as a plain read-only card. */
    private VBox reportCard(Report r) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: rgba(240,244,242,0.6); -fx-background-radius: 14;" +
                "-fx-border-color: rgba(11,61,46,0.08); -fx-border-radius: 14; -fx-border-width: 1;");
        Label t = new Label(r.title);
        t.setWrapText(true);
        t.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14.5px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Label d = new Label(r.description);
        d.setWrapText(true);
        d.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-text-fill: rgba(11,61,46,0.70);");
        card.getChildren().addAll(t, d);
        return card;
    }

    /**
     * Central data source for the whole page. Sample data for now — swap
     * this for a Firebase-backed loader later; every consumer below reads
     * through {@link #getProjectsForSelectedVillage()} rather than this
     * field directly, so that's the only place a future async load would
     * need to plug in (e.g. re-run {@link #refreshVillageData()} in the
     * Firestore listener's callback).
     */
    private static final List<Project> ALL_PROJECTS = new ArrayList<>(Arrays.asList(
            // ---- Rampur ----
            new Project("Village Road Construction", "#PRJ-089", "Rampur", "Main Street",
                    "Rural Development", "\u20B91,20,000", "Approved", "24 May 2025", null),
            new Project("Water Tank Renovation", "#PRJ-088", "Rampur", "Near School Area",
                    "Water Supply", "\u20B985,000", "In Progress", "22 May 2025", null),
            new Project("Community Hall Construction", "#PRJ-092", "Rampur", "Panchayat Grounds",
                    "Rural Development", "\u20B95,50,000", "Pending", "18 May 2025", "HIGH PRIORITY",
                    "Other", "Rampur, Panchayat Grounds", "\u20B95,50,000", "6 Months"),
            new Project("Rampur Drainage Upgrade", "#PRJ-101", "Rampur", "East Ward",
                    "Sanitation", "\u20B92,10,000", "Completed", "02 Mar 2025", null),

            // ---- Sitapur ----
            new Project("Primary School Repair", "#PRJ-085", "Sitapur", "Gram Panchayat Office",
                    "Infrastructure", "\u20B92,50,000", "Delayed", "28 Aug 2023", null),
            new Project("Solar Street Lights", "#PRJ-095", "Sitapur", "Main Road",
                    "Energy", "\u20B91,20,000", "Pending", "15 May 2025", "MEDIUM",
                    "Electricity", "Sitapur, Main Road", "\u20B91,20,000", "2 Months"),
            new Project("Sitapur Health Sub-Center", "#PRJ-076", "Sitapur", "Ward 2",
                    "Health", "\u20B93,80,000", "In Progress", "10 Apr 2025", null),
            new Project("Sitapur Bus Shelter", "#PRJ-063", "Sitapur", "Bus Stand Road",
                    "Rural Development", "\u20B940,000", "Completed", "20 Jan 2025", null),

            // ---- Madhavpur ----
            new Project("Library Construction", "#PRJ-098", "Madhavpur", "School Campus",
                    "Education", "\u20B93,00,000", "Pending", "12 May 2025", "NORMAL",
                    "Education", "Madhavpur, School Campus", "\u20B93,00,000", "8 Months"),
            new Project("Madhavpur Check Dam", "#PRJ-071", "Madhavpur", "Riverside",
                    "Irrigation", "\u20B94,60,000", "In Progress", "05 Apr 2025", null),
            new Project("Anganwadi Renovation", "#PRJ-058", "Madhavpur", "Ward 1",
                    "Welfare", "\u20B975,000", "Delayed", "14 Feb 2025", null),
            new Project("Madhavpur Community Toilet", "#PRJ-052", "Madhavpur", "Market Road",
                    "Sanitation", "\u20B91,10,000", "Completed", "30 Dec 2024", null),

            // ---- Gopalganj ----
            new Project("Gopalganj Road Widening", "#PRJ-110", "Gopalganj", "NH Link Road",
                    "Rural Development", "\u20B96,20,000", "Approved", "20 May 2025", null),
            new Project("Gopalganj Water Pipeline", "#PRJ-104", "Gopalganj", "Colony Road",
                    "Water Supply", "\u20B92,90,000", "Pending", "08 May 2025", "HIGH PRIORITY",
                    "Water Supply", "Gopalganj, Colony Road", "\u20B92,90,000", "4 Months"),
            new Project("Gopalganj Panchayat Bhavan", "#PRJ-047", "Gopalganj", "Gram Panchayat Office",
                    "Infrastructure", "\u20B91,45,000", "In Progress", "22 Mar 2025", null),
            new Project("Gopalganj Playground", "#PRJ-039", "Gopalganj", "Ward 3",
                    "Welfare", "\u20B960,000", "Completed", "05 Feb 2025", null)
    ));

    private Label selectedNavItem;

    private Label villageNameLabel;
    private Label villageChevron;
    private VBox villageListBox;
    private Label scopeSubtitleStrong;
    private boolean villageListExpanded = false;

    /** Live containers — cleared and repopulated by refreshVillageData(), never replaced/re-parented. */
    private HBox statCardsRow;
    private HBox approvedPendingRow;
    private VBox inventoryPanel;

    @Override
    public void start(Stage stage) {
        Dashboard.myStage = stage;
        stage.setTitle("GramConnect - Project Management");
        stage.setScene(getProjectManagementScene());
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.show();
    }

    /** Builds the Project Management scene. Public so other pages can navigate here. */
    public Scene getProjectManagementScene() {
        BorderPane root = new BorderPane();
        root.setBackground(buildBackground());

        root.setLeft(buildSidebar());

        BorderPane contentArea = new BorderPane();
        contentArea.setTop(buildTopBar());

        ScrollPane scroller = new ScrollPane(buildMainContent());
        scroller.setFitToWidth(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        contentArea.setCenter(scroller);

        root.setCenter(contentArea);

        return new Scene(root, 1500, 820);
    }

    private Background buildBackground() {
        try {
            Image backgroundImage = new Image(new File(BACKGROUND_IMAGE_PATH).toURI().toString());
            return new Background(new BackgroundImage(backgroundImage,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true)));
        } catch (Exception ex) {
            return null;
        }
    }

    /* ============================================================
     *  SIDEBAR — shared shell, wired for full mesh navigation
     * ============================================================ */
    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(288);
        sidebar.setMinWidth(288);
        sidebar.setStyle(
                "-fx-background-color: linear-gradient(to bottom, " + SIDEBAR_TOP + ", " + SIDEBAR_MID + ", " + SIDEBAR_BOT + ");" +
                "-fx-border-color: transparent rgba(11,61,46,0.10) transparent transparent;" +
                "-fx-border-width: 0 1 0 0;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.20), 24, 0.2, 4, 0);"
        );

        HBox header = new HBox(14);
        header.setPadding(new Insets(24));
        header.setAlignment(Pos.CENTER_LEFT);

        StackPane avatar = new StackPane();
        Circle avatarCircle = new Circle(24);
        avatarCircle.setFill(Color.web(FOREST_DEEP));
        avatarCircle.setStroke(Color.web(SAFFRON_MAIN, 0.85));
        avatarCircle.setStrokeWidth(2.5);
        Label avatarInitials = new Label("SD");
        avatarInitials.setFont(Font.font("Inter", FontWeight.BOLD, 16));
        avatarInitials.setTextFill(Color.WHITE);
        avatar.getChildren().addAll(avatarCircle, avatarInitials);

        VBox nameBox = new VBox(2);
        Label name = new Label("Officer Sameer Deshmukh");
        name.setWrapText(true);
        name.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 17px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label role = new Label("BDO, Gram Panchayat");
        role.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.65); -fx-letter-spacing: 0.05em;");
        nameBox.getChildren().addAll(name, role);

        header.getChildren().addAll(avatar, nameBox);

        VBox villageSelector = buildVillageSelector();

        VBox nav = new VBox(6);
        nav.setPadding(new Insets(12, 12, 16, 12));

        HBox dashboardNav = navItem("\u25A6", "Dashboard", false);
        Runnable toDashboard = () -> {
            Dashboard page = new Dashboard();
            Dashboard.myStage.setScene(page.getBDODashboardScene());
        };
        dashboardNav.setOnMouseClicked(e -> toDashboard.run());

        HBox projectNav = navItem("\uD83D\uDDC2", "Project Management", true);   // <-- active on this page

        HBox budgetNav = navItem("\uD83D\uDCB0", "Budget Management", false);
        Runnable toBudgetManagement = () -> {
            BudgetManagment page = new BudgetManagment();
            Dashboard.myStage.setScene(page.getBudgetManagmentScene());
        };
        budgetNav.setOnMouseClicked(e -> toBudgetManagement.run());

        HBox complaintNav = navItem("\u26A0", "Complaint Management", false);
        Runnable toComplaintManagement = () -> {
            ComplaintManagement page = new ComplaintManagement();
            Dashboard.myStage.setScene(page.getComplaintManagementScene());
        };
        complaintNav.setOnMouseClicked(e -> toComplaintManagement.run());

        HBox reportsNav = navItem("\uD83D\uDCCA", "Reports & Analytics", false);
        Runnable toReportsAnalytics = () -> {
            ReportsAnalytics page = new ReportsAnalytics();
            Dashboard.myStage.setScene(page.getReportsAnalyticsScene());
        };
        reportsNav.setOnMouseClicked(e -> toReportsAnalytics.run());

        nav.getChildren().addAll(
                dashboardNav,
                projectNav,
                budgetNav,
                complaintNav,
                reportsNav
        );
        VBox.setVgrow(nav, Priority.ALWAYS);

        VBox footer = new VBox(10);
        footer.setPadding(new Insets(20, 24, 24, 24));

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: linear-gradient(to right, transparent, rgba(11,61,46,0.25), transparent);");

        VBox smallLinks = new VBox(4);
        smallLinks.setPadding(new Insets(8, 0, 0, 0));
        smallLinks.getChildren().addAll(
                footerLink("\u2699", "Settings"),
                footerLink("\u2753", "Support")
        );

        footer.getChildren().addAll(divider, smallLinks);

        sidebar.getChildren().addAll(header, villageSelector, nav, footer);
        return sidebar;
    }

    /* ============================================================
     *  VILLAGE SWITCHER — reads/writes Dashboard.VILLAGES /
     *  Dashboard.selectedVillage, and is the single trigger for a
     *  full data refresh across this page.
     * ============================================================ */
    private VBox buildVillageSelector() {
        VBox wrap = new VBox(8);
        wrap.setPadding(new Insets(0, 20, 16, 20));

        Label caption = new Label("MANAGING VILLAGE");
        caption.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 10.5px; -fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.55); -fx-letter-spacing: 0.08em;");

        HBox toggleRow = new HBox(10);
        toggleRow.setAlignment(Pos.CENTER_LEFT);
        toggleRow.setPadding(new Insets(11, 14, 11, 14));
        toggleRow.setMaxWidth(Double.MAX_VALUE);
        String toggleBase = "-fx-background-color: rgba(255,255,255,0.65); -fx-background-radius: 10;" +
                "-fx-border-color: rgba(11,61,46,0.12); -fx-border-radius: 10; -fx-border-width: 1; -fx-cursor: hand;";
        String toggleHover = "-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 10;" +
                "-fx-border-color: rgba(11,61,46,0.18); -fx-border-radius: 10; -fx-border-width: 1; -fx-cursor: hand;";
        toggleRow.setStyle(toggleBase);

        Label pin = new Label("\uD83D\uDCCD");
        pin.setStyle("-fx-font-size: 13px;");

        villageNameLabel = new Label(Dashboard.selectedVillage);
        villageNameLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        villageChevron = new Label("\u25BE");
        villageChevron.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.65);");

        toggleRow.getChildren().addAll(pin, villageNameLabel, spacer, villageChevron);
        toggleRow.setOnMouseEntered(e -> toggleRow.setStyle(toggleHover));
        toggleRow.setOnMouseExited(e -> toggleRow.setStyle(toggleBase));

        villageListBox = new VBox(2);
        villageListBox.setPadding(new Insets(6, 0, 0, 0));
        villageListBox.setVisible(false);
        villageListBox.setManaged(false);

        ToggleGroup group = new ToggleGroup();
        for (String village : Dashboard.VILLAGES) {
            villageListBox.getChildren().add(villageToggle(village, group));
        }

        toggleRow.setOnMouseClicked(e -> {
            villageListExpanded = !villageListExpanded;
            villageListBox.setVisible(villageListExpanded);
            villageListBox.setManaged(villageListExpanded);
            villageChevron.setText(villageListExpanded ? "\u25B4" : "\u25BE");
        });

        wrap.getChildren().addAll(caption, toggleRow, villageListBox);
        return wrap;
    }

    private ToggleButton villageToggle(String village, ToggleGroup group) {
        ToggleButton btn = new ToggleButton(village);
        btn.setToggleGroup(group);
        btn.setSelected(village.equals(Dashboard.selectedVillage));
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(9, 14, 9, 14));

        String baseStyle = "-fx-background-color: transparent; -fx-background-radius: 8;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 600;" +
                "-fx-text-fill: rgba(11,61,46,0.75); -fx-cursor: hand;";
        String activeStyle = "-fx-background-color: rgba(224,122,31,0.14); -fx-background-radius: 8;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + SAFFRON_MAIN + "; -fx-cursor: hand;";

        btn.setStyle(btn.isSelected() ? activeStyle : baseStyle);

        btn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            btn.setStyle(isSelected ? activeStyle : baseStyle);
            if (isSelected) {
                Dashboard.selectedVillage = village;
                villageNameLabel.setText(Dashboard.selectedVillage);
                refreshVillageData();
                // Collapse the list once a village has been chosen.
                villageListExpanded = false;
                villageListBox.setVisible(false);
                villageListBox.setManaged(false);
                villageChevron.setText("\u25BE");
            }
        });

        return btn;
    }

    /** Keeps the page's "Showing data for:" label in sync with the selected village. */
    private void updateScopeSubtitle() {
        if (scopeSubtitleStrong == null) return;
        if (Dashboard.VILLAGES.get(0).equals(Dashboard.selectedVillage)) {
            scopeSubtitleStrong.setText("All Villages (Block)");
        } else {
            scopeSubtitleStrong.setText(Dashboard.selectedVillage);
        }
    }

    /* ============================================================
     *  VILLAGE FILTERING — single source of truth for every table
     *  and KPI on this page.
     * ============================================================ */

    /**
     * Returns every project belonging to {@code Dashboard.selectedVillage},
     * or the full list when "All Villages" (index 0 of Dashboard.VILLAGES)
     * is selected.
     */
    private List<Project> getProjectsForSelectedVillage() {
        if (Dashboard.VILLAGES.get(0).equals(Dashboard.selectedVillage)) {
            return new ArrayList<>(ALL_PROJECTS);
        }
        List<Project> filtered = new ArrayList<>();
        for (Project p : ALL_PROJECTS) {
            if (p.village.equals(Dashboard.selectedVillage)) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    /**
     * Central refresh entry point — called once when the page is first
     * built, and again every time the village toggle changes selection.
     * Clears and repopulates the KPI row, Approved/Pending panels, and
     * the inventory table in place (no nodes are re-parented, so hover
     * effects / layout stay exactly as before).
     */
    private void refreshVillageData() {
        updateScopeSubtitle();
        refreshStatCards();
        refreshApprovedPendingSection();
        refreshInventoryPanel();
    }

    /* ============================================================
     *  TOP NAVIGATION BAR — shared shell (unchanged)
     * ============================================================ */
    private HBox buildTopBar() {
        HBox topBar = new HBox(24);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(72);
        topBar.setPadding(new Insets(0, 32, 0, 32));
        topBar.setStyle(
                "-fx-background-color: rgba(255,255,255,0.92);" +
                "-fx-border-color: transparent transparent rgba(255,255,255,0.6) transparent;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 8, 0.1, 0, 2);"
        );

        Label brand = new Label("GramConnect");
        brand.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 16, 0, 16));
        searchBox.setPrefWidth(480);
        searchBox.setPrefHeight(42);
        searchBox.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 12;" +
                "-fx-border-color: rgba(11,61,46,0.10); -fx-border-radius: 12; -fx-border-width: 1;");
        Label searchIcon = new Label("\uD83D\uDD0D");
        searchIcon.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.5);");
        TextField searchField = new TextField();
        searchField.setPromptText("Search projects, villages, or departments...");
        searchField.setStyle("-fx-background-color: transparent; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 14px; -fx-text-fill: " + FOREST_DEEP + "; -fx-prompt-text-fill: rgba(11,61,46,0.40);");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBox.getChildren().addAll(searchIcon, searchField);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        StackPane bell = new StackPane();
        Label bellIcon = new Label("\uD83D\uDD14");
        bellIcon.setStyle("-fx-font-size: 16px;");
        StackPane bellBtn = new StackPane(bellIcon);
        bellBtn.setPrefSize(42, 42);
        bellBtn.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 50;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 4, 0.1, 0, 1); -fx-cursor: hand;");
        Circle dot = new Circle(5, Color.web(SAFFRON_MAIN));
        dot.setStroke(Color.WHITE);
        dot.setStrokeWidth(2);
        StackPane.setAlignment(dot, Pos.TOP_RIGHT);
        StackPane.setMargin(dot, new Insets(7, 7, 0, 0));
        bell.getChildren().addAll(bellBtn, dot);

        Region vDivider = new Region();
        vDivider.setPrefSize(1, 32);
        vDivider.setStyle("-fx-background-color: rgba(11,61,46,0.10);");

        HBox profile = new HBox(10);
        profile.setAlignment(Pos.CENTER_LEFT);
        profile.setPadding(new Insets(6, 12, 6, 6));
        profile.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 4, 0.1, 0, 1); -fx-cursor: hand;");
        Circle pAvatar = new Circle(16, Color.web(CONTEXT_TEAL));
        pAvatar.setStroke(Color.WHITE);
        pAvatar.setStrokeWidth(2);
        Label lang = new Label("\u092E\u0930\u093E\u0920\u0940");
        lang.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");
        Label chevron = new Label("\u25BE");
        chevron.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.7);");
        profile.getChildren().addAll(pAvatar, lang, chevron);

        topBar.getChildren().addAll(brand, searchBox, spacer, bell, vDivider, profile);
        return topBar;
    }

    private HBox navItem(String icon, String text, boolean active) {
        HBox item = new HBox(14);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(14, 16, 14, 16));
        item.setMaxWidth(Double.MAX_VALUE);

        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 17px; -fx-text-fill: " + (active ? SAFFRON_MAIN : FOREST_DEEP) + ";");

        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px;" +
                "-fx-font-weight: " + (active ? "800" : "600") + ";" +
                "-fx-text-fill: " + (active ? SAFFRON_MAIN : "rgba(11,61,46,0.80)") + ";" +
                "-fx-letter-spacing: 0.05em;");

        item.getChildren().addAll(ic, lbl);

        if (active) {
            selectedNavItem = lbl;
            Region bar = new Region();
            bar.setPrefWidth(6);
            bar.setMinWidth(6);
            bar.setStyle("-fx-background-color: " + SAFFRON_MAIN + "; -fx-background-radius: 8 0 0 8;" +
                    "-fx-effect: dropshadow(gaussian, rgba(224,122,31,0.6), 8, 0.3, 0, 0);");
            HBox wrap = new HBox(bar, item);
            HBox.setHgrow(item, Priority.ALWAYS);
            wrap.setStyle("-fx-background-color: rgba(255,255,255,0.65); -fx-background-radius: 10;" +
                    "-fx-effect: innershadow(gaussian, rgba(11,61,46,0.10), 6, 0.2, 0, 1);");
            wrap.setMaxWidth(Double.MAX_VALUE);
            return wrap;
        } else {
            String base = "-fx-background-radius: 10; -fx-background-color: transparent; -fx-cursor: hand;";
            item.setStyle(base);
            item.setOnMouseEntered(e -> item.setStyle("-fx-background-radius: 10; -fx-background-color: rgba(255,255,255,0.45); -fx-cursor: hand;"));
            item.setOnMouseExited(e -> item.setStyle(base));
            return item;
        }
    }

    private HBox footerLink(String icon, String text) {
        HBox link = new HBox(10);
        link.setAlignment(Pos.CENTER_LEFT);
        link.setPadding(new Insets(8, 16, 8, 16));
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.65);");
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.65);");
        link.getChildren().addAll(ic, lbl);
        String base = "-fx-background-radius: 8; -fx-background-color: transparent; -fx-cursor: hand;";
        link.setStyle(base);
        link.setOnMouseEntered(e -> link.setStyle("-fx-background-radius: 8; -fx-background-color: rgba(255,255,255,0.45); -fx-cursor: hand;"));
        link.setOnMouseExited(e -> link.setStyle(base));
        return link;
    }

    /* ============================================================
     *  MAIN CONTENT — dynamic containers are created once here and
     *  refreshed in place by refreshVillageData().
     * ============================================================ */
    private VBox buildMainContent() {
        VBox main = new VBox(24);
        main.setPadding(new Insets(32, 40, 48, 40));
        main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");

        statCardsRow = new HBox(20);
        approvedPendingRow = new HBox(20);
        inventoryPanel = new VBox(18);
        inventoryPanel.setPadding(new Insets(28));
        inventoryPanel.setStyle(cardStyle(24));
        addHoverLift(inventoryPanel, 24);

        HBox titleRow = buildTitleRow();

        refreshVillageData();

        main.getChildren().addAll(titleRow, statCardsRow, approvedPendingRow, inventoryPanel);
        return main;
    }

    private HBox buildTitleRow() {
        VBox text = new VBox(6);
        Label title = new Label("Project Management");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        HBox subtitleRow = new HBox(4);
        Label subtitle = new Label("Showing data for:");
        subtitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.60);");
        scopeSubtitleStrong = new Label("All Villages (Block)");
        scopeSubtitleStrong.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        subtitleRow.getChildren().addAll(subtitle, scopeSubtitleStrong);
        text.getChildren().addAll(title, subtitleRow);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerRow = new HBox(20, text, spacer, actions);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        return headerRow;
    }

    /* ---------------- KPI cards (dynamic) ---------------- */
    private void refreshStatCards() {
        List<Project> scoped = getProjectsForSelectedVillage();
        // boolean allVillages = Dashboard.VILLAGES.get(0).equals(Dashboard.selectedVillage);

        int total = scoped.size();
        int active = 0, completed = 0, delayed = 0;
        for (Project p : scoped) {
            if ("In Progress".equals(p.status)) active++;
            else if ("Completed".equals(p.status)) completed++;
            else if ("Delayed".equals(p.status)) delayed++;
        }

        VBox totalCard = kpiCard(FOREST_LIGHT, "\uD83D\uDCBC", "TOTAL PROJECTS", String.valueOf(total));
        // Label totalFooter = new Label(allVillages ? "All village projects" : "Projects in " + Dashboard.selectedVillage);
        // totalFooter.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.60);");
        // totalCard.getChildren().add(totalFooter);

        VBox activeCard = kpiCard(SAFFRON_MAIN, "\u25B6", "ACTIVE PROJECTS", String.valueOf(active));
        // Progress bar removed — the card now shows just the stat, like the others.

        VBox completedCard = kpiCard(CONTEXT_TEAL, "\u2705", "COMPLETED", String.valueOf(completed));
        // Label completedFooter = new Label(safePct(completed, total) + " of all projects");
        // completedFooter.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.60);");
        // completedCard.getChildren().add(completedFooter);

        VBox delayedCard = kpiCard(AI_VIOLET, "\u26A0", "DELAYED PROJECTS", String.valueOf(delayed));

        HBox.setHgrow(totalCard, Priority.ALWAYS);
        HBox.setHgrow(activeCard, Priority.ALWAYS);
        HBox.setHgrow(completedCard, Priority.ALWAYS);
        HBox.setHgrow(delayedCard, Priority.ALWAYS);

        statCardsRow.getChildren().setAll(totalCard, activeCard, completedCard, delayedCard);
    }

    private VBox kpiCard(String accent, String icon, String labelText, String statText) {
        VBox card = new VBox();
        card.setPrefWidth(320);
        card.setMinHeight(190);
        card.setStyle(cardStyle(16));

        Region strip = new Region();
        strip.setPrefHeight(6);
        strip.setStyle("-fx-background-color: " + accent + "; -fx-background-radius: 16 16 0 0;");

        VBox inner = new VBox(20);
        inner.setPadding(new Insets(20, 24, 24, 24));
        VBox.setVgrow(inner, Priority.ALWAYS);

        HBox head = new HBox(12);
        head.setAlignment(Pos.CENTER_LEFT);
        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(48, 48);
        iconChip.setMinSize(48, 48);
        iconChip.setStyle("-fx-background-color: " + rgba(accent, 0.12) + "; -fx-background-radius: 12;");
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 18px; -fx-text-fill: " + accent + ";");
        iconChip.getChildren().add(ic);
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.80); -fx-letter-spacing: 0.08em;");
        lbl.setWrapText(true);
        head.getChildren().addAll(iconChip, lbl);

        Region grow = new Region();
        VBox.setVgrow(grow, Priority.ALWAYS);

        VBox bottom = new VBox(10);
        Label stat = new Label(statText);
        stat.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 40px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        bottom.getChildren().add(stat);

        inner.getChildren().addAll(head, grow, bottom);
        card.getChildren().addAll(strip, inner);
        addHoverLift(card, 16);
        return card;
    }

    private StackPane progressBar(double fraction, String color, double height) {
        StackPane track = new StackPane();
        track.setPrefHeight(height);
        track.setMinHeight(height);
        track.setMaxWidth(Double.MAX_VALUE);
        track.setStyle("-fx-background-color: rgba(11,61,46,0.08); -fx-background-radius: 999;");
        Region fill = new Region();
        fill.setPrefHeight(height);
        fill.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 999;");
        StackPane.setAlignment(fill, Pos.CENTER_LEFT);
        track.widthProperty().addListener((obs, o, w) -> fill.setMaxWidth(w.doubleValue() * fraction));
        fill.setMaxWidth(0);
        track.getChildren().add(fill);
        return track;
    }

    private double safeFraction(int part, int total) {
        return total == 0 ? 0 : (double) part / total;
    }

    private String safePct(int part, int total) {
        return total == 0 ? "0%" : Math.round(part * 100.0 / total) + "%";
    }

    /* ---------------- Approved Projects + Pending Approval (dynamic) ---------------- */
    private void refreshApprovedPendingSection() {
        List<Project> scoped = getProjectsForSelectedVillage();

        List<Project> approvedList = new ArrayList<>();
        List<Project> pendingList = new ArrayList<>();
        for (Project p : scoped) {
            if ("Pending".equals(p.status)) pendingList.add(p);
            else approvedList.add(p);
        }

        VBox approved = buildApprovedProjectsPanel(approvedList);
        VBox pending = buildPendingApprovalPanel(pendingList);

        HBox.setHgrow(approved, Priority.ALWAYS);
        approved.setPrefWidth(900);
        pending.setPrefWidth(420);
        pending.setMinWidth(360);

        approvedPendingRow.getChildren().setAll(approved, pending);
    }

    private VBox buildApprovedProjectsPanel(List<Project> approvedList) {
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("\u2705");
        icon.setStyle("-fx-font-size: 16px;");
        Label title = new Label("Approved Projects");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label viewAll = new Label("View All");
        viewAll.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-font-weight: 700; -fx-cursor: hand;");
        header.getChildren().addAll(icon, title, spacer, viewAll);

        if (approvedList.isEmpty()) {
            panel.getChildren().addAll(header, emptyState("No approved projects found for "
                    + (Dashboard.VILLAGES.get(0).equals(Dashboard.selectedVillage) ? "any village" : Dashboard.selectedVillage) + "."));
            addHoverLift(panel, 24);
            return panel;
        }

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(16);
        grid.setPadding(new Insets(6, 0, 0, 0));
        grid.getColumnConstraints().addAll(pct(34), pct(22), pct(18), pct(16), pct(10));

        grid.add(headerCell("PROJECT DETAILS"), 0, 0);
        grid.add(headerCell("VILLAGE"), 1, 0);
        grid.add(headerCell("BUDGET"), 2, 0);
        grid.add(headerCell("DATE"), 3, 0);
        grid.add(headerCell("ACTIONS"), 4, 0);

        int row = 1;
        for (Project p : approvedList) {
            addApprovedRow(grid, row++, p);
        }

        panel.getChildren().addAll(header, grid);
        addHoverLift(panel, 24);
        return panel;
    }

    private void addApprovedRow(GridPane grid, int row, Project p) {
        HBox projectCell = new HBox(10);
        projectCell.setAlignment(Pos.CENTER_LEFT);
        StackPane thumb = new StackPane();
        thumb.setPrefSize(38, 38);
        thumb.setMinSize(38, 38);
        thumb.setStyle("-fx-background-color: rgba(11,61,46,0.10); -fx-background-radius: 8;");
        Label thumbIcon = new Label("\uD83D\uDDBC");
        thumbIcon.setStyle("-fx-font-size: 14px;");
        thumb.getChildren().add(thumbIcon);

        VBox projectText = new VBox(2);
        Label nameLabel = new Label(p.projectName);
        nameLabel.setWrapText(true);
        nameLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Label idLabel = new Label(p.projectId);
        idLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.55);");
        projectText.getChildren().addAll(nameLabel, idLabel);

        projectCell.getChildren().addAll(thumb, projectText);

        VBox villageCell = new VBox(2);
        Label villageLabel = new Label(p.village);
        villageLabel.setWrapText(true);
        villageLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: rgba(11,61,46,0.85);");
        Label localityLabel = new Label(p.locality);
        localityLabel.setWrapText(true);
        localityLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.55);");
        villageCell.getChildren().addAll(villageLabel, localityLabel);

        Label budgetLabel = new Label(p.budget);
        budgetLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");

        Label dateLabel = new Label(p.date);
        dateLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_LEFT);
        Label viewIcon = new Label("\uD83D\uDC41");
        viewIcon.setStyle("-fx-font-size: 14px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");
        Label editIcon = new Label("\uD83D\uDCDD");
        editIcon.setStyle("-fx-font-size: 14px; -fx-text-fill: " + SAFFRON_MAIN + "; -fx-cursor: hand;");
        actions.getChildren().addAll(viewIcon, editIcon);

        grid.add(projectCell, 0, row);
        grid.add(villageCell, 1, row);
        grid.add(budgetLabel, 2, row);
        grid.add(dateLabel, 3, row);
        grid.add(actions, 4, row);
    }

    private VBox buildPendingApprovalPanel(List<Project> pendingList) {
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("\uD83D\uDCC1");
        icon.setStyle("-fx-font-size: 16px;");
        Label title = new Label("Pending Approval");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        StackPane countBadge = new StackPane();
        countBadge.setPrefSize(24, 24);
        countBadge.setMinSize(24, 24);
        countBadge.setStyle("-fx-background-color: " + rgba(FOREST_DEEP, 0.10) + "; -fx-background-radius: 50%;");
        Label countLabel = new Label(String.valueOf(pendingList.size()));
        countLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        countBadge.getChildren().add(countLabel);

        header.getChildren().addAll(icon, title, spacer, countBadge);

        if (pendingList.isEmpty()) {
            panel.getChildren().addAll(header, emptyState("No pending approvals for "
                    + (Dashboard.VILLAGES.get(0).equals(Dashboard.selectedVillage) ? "any village" : Dashboard.selectedVillage) + "."));
            addHoverLift(panel, 24);
            return panel;
        }

        VBox list = new VBox(14);
        for (Project p : pendingList) {
            String priorityColor = "HIGH PRIORITY".equals(p.priority) ? DELAYED_RED
                    : "MEDIUM".equals(p.priority) ? SAFFRON_MAIN : CONTEXT_TEAL;
            list.getChildren().add(pendingApprovalCard(p, priorityColor));
        }

        Label bulkReview = new Label("Bulk Review All");
        bulkReview.setMaxWidth(Double.MAX_VALUE);
        bulkReview.setAlignment(Pos.CENTER);
        bulkReview.setPadding(new Insets(12, 16, 12, 16));
        bulkReview.setStyle("-fx-background-color: transparent; -fx-border-color: " + FOREST_DEEP + ";" +
                "-fx-border-radius: 10; -fx-background-radius: 10;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;");

        panel.getChildren().addAll(header, list, bulkReview);
        addHoverLift(panel, 24);
        return panel;
    }

    /**
     * Renders one Pending Approval card summarizing the project — Project
     * Name, Category, Location — via {@link Project#category} and
     * {@link Project#location}. Expected Budget and Expected Duration
     * (and the budget-approval action) no longer live on the card itself;
     * "View Details" opens {@link #buildProjectDetailsScene(Project)},
     * which shows those fields plus an editable budget field and an
     * "Approve Project" action. Not wired to CreateProjectPage yet; this only makes sure the
     * shape matches so a future submit-and-fetch (here, and later on
     * SarpanchComplaintsPage) needs no field renaming.
     */
    private VBox pendingApprovalCard(Project p, String priorityColor) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: rgba(240,244,242,0.6); -fx-background-radius: 14;" +
                "-fx-border-color: rgba(11,61,46,0.08); -fx-border-radius: 14; -fx-border-width: 1;");

        // ---- Project Name + Priority ----
        HBox titleRow = new HBox(8);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label(p.projectName);
        nameLabel.setWrapText(true);
        nameLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label priorityBadge = new Label(p.priority);
        priorityBadge.setPadding(new Insets(3, 8, 3, 8));
        priorityBadge.setMaxWidth(Region.USE_PREF_SIZE);
        priorityBadge.setStyle("-fx-background-color: " + rgba(priorityColor, 0.14) + "; -fx-background-radius: 999;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 10.5px; -fx-font-weight: 800; -fx-text-fill: " + priorityColor + ";");
        titleRow.getChildren().addAll(nameLabel, spacer, priorityBadge);

        // ---- Category ----
        Label categoryLabel = new Label(p.category);
        categoryLabel.setPadding(new Insets(3, 9, 3, 9));
        categoryLabel.setMaxWidth(Region.USE_PREF_SIZE);
        categoryLabel.setStyle("-fx-background-color: " + rgba(AI_VIOLET, 0.14) + "; -fx-background-radius: 999;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: " + AI_VIOLET + ";");

        // ---- Location ----
        Label locationLabel = new Label("\uD83D\uDCCD " + p.location);
        locationLabel.setWrapText(true);
        locationLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.60);");

        // ---- Bottom row: quick edit + View Details (opens the full details page,
        //      which is where Expected Budget / Expected Duration and the budget
        //      approval action now live) ----
        HBox bottomRow = new HBox(10);
        bottomRow.setAlignment(Pos.CENTER_RIGHT);

        StackPane editBtn = new StackPane();
        editBtn.setPrefSize(34, 34);
        editBtn.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 8;" +
                "-fx-border-color: rgba(11,61,46,0.10); -fx-border-radius: 8; -fx-cursor: hand;");
        Label editIcon = new Label("\uD83D\uDCDD");
        editIcon.setStyle("-fx-font-size: 13px;");
        editBtn.getChildren().add(editIcon);

        Label viewDetailsBtn = new Label("View Details  \u2192");
        viewDetailsBtn.setPadding(new Insets(9, 18, 9, 18));
        viewDetailsBtn.setStyle("-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
                "-fx-text-fill: white; -fx-background-radius: 8; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 12.5px; -fx-font-weight: 800; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.30), 8, 0.1, 0, 3);");
        // Opens the dedicated Project Details page, which shows the Expected
        // Budget / Expected Duration removed from this card, plus the budget
        // adjustment field + "Approve Project" action.
        viewDetailsBtn.setOnMouseClicked(e -> Dashboard.myStage.setScene(buildProjectDetailsScene(p)));

        bottomRow.getChildren().addAll(editBtn, viewDetailsBtn);

        card.getChildren().addAll(titleRow, categoryLabel, locationLabel, bottomRow);
        return card;
    }

    /** Small labeled tile used to show Expected Budget / Expected Duration on a Pending Approval card. */
    private VBox pendingStatTile(String label, String value, String accent) {
        VBox tile = new VBox(2);
        tile.setPadding(new Insets(8, 10, 8, 10));
        tile.setStyle("-fx-background-color: " + rgba(accent, 0.08) + "; -fx-background-radius: 10;");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 9.5px; -fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.55); -fx-letter-spacing: 0.04em;");
        Label val = new Label(value);
        val.setWrapText(true);
        val.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 900; -fx-text-fill: " + accent + ";");
        tile.getChildren().addAll(lbl, val);
        return tile;
    }

    /* ============================================================
     *  PROJECT DETAILS PAGE
     *  ------------------------------------------------------------
     *  Opened from a Pending Approval card's "View Details" action.
     *  Shows the fields removed from the card itself (Expected
     *  Budget / Expected Duration) plus an editable budget field
     *  and an "Approve Project" action that commits the (possibly
     *  revised) budget and moves the project to "Approved".
     * ============================================================ */

    /** Builds the full-page Scene for a single project's details. */
    private Scene buildProjectDetailsScene(Project p) {
        BorderPane root = new BorderPane();
        root.setBackground(buildBackground());

        root.setLeft(buildSidebar());

        BorderPane contentArea = new BorderPane();
        contentArea.setTop(buildTopBar());

        ScrollPane scroller = new ScrollPane(buildProjectDetailsContent(p));
        scroller.setFitToWidth(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        contentArea.setCenter(scroller);

        root.setCenter(contentArea);

        return new Scene(root, 1500, 820);
    }

    private VBox buildProjectDetailsContent(Project p) {
        VBox main = new VBox(22);
        main.setPadding(new Insets(32, 40, 48, 40));
        main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");

        Label back = new Label("\u2190  Back to Project Management");
        back.setPadding(new Insets(6, 2, 6, 2));
        back.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;");
        back.setOnMouseClicked(e -> Dashboard.myStage.setScene(getProjectManagementScene()));

        main.getChildren().addAll(back, buildDetailsHero(p), buildDetailsInfoGrid(p), buildBudgetAdjustmentPanel(p));
        return main;
    }

    /** Solid-dark hero header — project name/id, status, category, location. High-contrast on purpose. */
    private VBox buildDetailsHero(Project p) {
        VBox hero = new VBox(16);
        hero.setPadding(new Insets(32));
        hero.setStyle("-fx-background-color: linear-gradient(135deg, #08291F, " + FOREST_DEEP + " 60%, " + FOREST_LIGHT + ");" +
                "-fx-background-radius: 24;" +
                "-fx-border-color: rgba(255,255,255,0.08); -fx-border-radius: 24; -fx-border-width: 1;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 26, 0.2, 0, 10);");

        Label idBadge = new Label(p.projectId);
        idBadge.setPadding(new Insets(4, 12, 4, 12));
        idBadge.setMaxWidth(Region.USE_PREF_SIZE);
        idBadge.setStyle("-fx-background-color: rgba(255,255,255,0.22); -fx-background-radius: 999;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 800; -fx-text-fill: white;");
        Label statusBadge = new Label("\u23F3 " + p.status);
        statusBadge.setPadding(new Insets(4, 12, 4, 12));
        statusBadge.setMaxWidth(Region.USE_PREF_SIZE);
        statusBadge.setStyle("-fx-background-color: " + SAFFRON_MAIN + "; -fx-background-radius: 999;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 800; -fx-text-fill: white;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.getChildren().addAll(idBadge, spacer, statusBadge);

        Label name = new Label(p.projectName);
        name.setWrapText(true);
        name.setMaxWidth(Double.MAX_VALUE);
        name.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 30px; -fx-font-weight: 900; -fx-text-fill: #FFFFFF;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 6, 0.5, 0, 2);");

        Label categoryTag = new Label(p.category);
        categoryTag.setPadding(new Insets(5, 13, 5, 13));
        categoryTag.setMaxWidth(Region.USE_PREF_SIZE);
        categoryTag.setStyle("-fx-background-color: rgba(255,255,255,0.20); -fx-background-radius: 999;" +
                "-fx-border-color: rgba(255,255,255,0.35); -fx-border-radius: 999; -fx-border-width: 1;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: #FFFFFF;");

        Label location = new Label("\uD83D\uDCCD " + p.location);
        location.setWrapText(true);
        location.setMaxWidth(Double.MAX_VALUE);
        location.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: #F1F6F3;");

        // Project name plus the info directly above it (id / status) and
        // below it (category / location) grouped into one dedicated VBox,
        // on its own frosted panel, so the text reads clearly against the
        // gradient behind it instead of floating loosely on top of it.
        VBox infoBox = new VBox(14);
        infoBox.setPadding(new Insets(20, 22, 20, 22));
        infoBox.setStyle("-fx-background-color: rgba(0,0,0,0.22); -fx-background-radius: 18;" +
                "-fx-border-color: rgba(255,255,255,0.10); -fx-border-radius: 18; -fx-border-width: 1;");
        infoBox.getChildren().addAll(topRow, name, categoryTag, location);

        hero.getChildren().add(infoBox);
        return hero;
    }

    /** Village / locality / department / date quick-reference grid. */
    private VBox buildDetailsInfoGrid(Project p) {
        VBox card = new VBox(16);
        card.setPadding(new Insets(28));
        card.setStyle(cardStyle(24));

        Label title = new Label("Project Information");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 18px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(18);
        grid.getColumnConstraints().addAll(pct(25), pct(25), pct(25), pct(25));

        grid.add(infoField("VILLAGE", p.village), 0, 0);
        grid.add(infoField("LOCALITY", p.locality), 1, 0);
        grid.add(infoField("DEPARTMENT", p.department), 2, 0);
        grid.add(infoField("DATE SUBMITTED", p.date), 3, 0);

        card.getChildren().addAll(title, grid);
        addHoverLift(card, 24);
        return card;
    }

    private VBox infoField(String label, String value) {
        VBox box = new VBox(4);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 10px; -fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.55); -fx-letter-spacing: 0.06em;");
        Label val = new Label(value);
        val.setWrapText(true);
        val.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        box.getChildren().addAll(lbl, val);
        return box;
    }

    /**
     * Budget review panel. Shows the Sarpanch-submitted Expected Budget as
     * a plain read-only fact alongside Expected Duration, with a clearly
     * separate "Customize Budget" section below where the reviewing
     * officer can type a different final figure. "Approve Project" commits
     * whatever is in the customize field (or the original figure if left
     * untouched) and moves the project to "Approved"; "Reject Project"
     * moves it to "Rejected" instead — neither reads from the other.
     */
    private VBox buildBudgetAdjustmentPanel(Project p) {
        VBox card = new VBox(22);
        card.setPadding(new Insets(28));
        card.setStyle(cardStyle(24));

        Label title = new Label("Expected Budget & Duration");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 18px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        long originalBudget = parseRupeeAmount(p.expectedBudget);

        // ---- Section 1: read-only facts as submitted, unrelated to customization below ----
        VBox budgetStat = pendingStatTile("EXPECTED BUDGET", p.expectedBudget, CONTEXT_TEAL);
        VBox durationStat = pendingStatTile("EXPECTED DURATION", p.expectedDuration, SAFFRON_MAIN);
        HBox.setHgrow(budgetStat, Priority.ALWAYS);
        HBox.setHgrow(durationStat, Priority.ALWAYS);
        HBox statsRow = new HBox(14);
        statsRow.setAlignment(Pos.CENTER_LEFT);
        statsRow.getChildren().addAll(budgetStat, durationStat);

        Region sectionDivider = new Region();
        sectionDivider.setPrefHeight(1);
        sectionDivider.setStyle("-fx-background-color: rgba(11,61,46,0.10);");

        // ---- Section 2: a separate, standalone budget customization block ----
        Label customizeTitle = new Label("CUSTOMIZE BUDGET");
        customizeTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.55); -fx-letter-spacing: 0.06em;");

        VBox customizeBox = new VBox(8);
        customizeBox.setPadding(new Insets(14, 16, 14, 16));
        customizeBox.setStyle("-fx-background-color: " + rgba(AI_VIOLET, 0.07) + "; -fx-background-radius: 12;" +
                "-fx-border-color: " + rgba(AI_VIOLET, 0.20) + "; -fx-border-radius: 12; -fx-border-width: 1;");
        Label customizeHint = new Label("Type a different final amount to override the submitted budget above.");
        customizeHint.setWrapText(true);
        customizeHint.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.60);");

        HBox inputRow = new HBox(8);
        inputRow.setAlignment(Pos.CENTER_LEFT);
        Label rupeeSign = new Label("\u20B9");
        rupeeSign.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 18px; -fx-font-weight: 900; -fx-text-fill: " + AI_VIOLET + ";");
        TextField budgetField = new TextField(String.valueOf(originalBudget));
        budgetField.setPrefWidth(200);
        budgetField.setStyle("-fx-background-color: rgba(255,255,255,0.95); -fx-background-radius: 8;" +
                "-fx-border-color: rgba(11,61,46,0.15); -fx-border-radius: 8; -fx-border-width: 1;" +
                "-fx-padding: 9 12; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        inputRow.getChildren().addAll(rupeeSign, budgetField);

        Label previewLabel = new Label(formatRupees(originalBudget));
        previewLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 700; -fx-text-fill: " + AI_VIOLET + ";");

        // Digits-only guard, with a live formatted preview underneath.
        budgetField.textProperty().addListener((obs, oldV, newV) -> {
            String sanitized = newV.replaceAll("[^0-9]", "");
            if (!sanitized.equals(newV)) {
                budgetField.setText(sanitized);
                return; // re-triggers this listener with the sanitized value
            }
            long val = sanitized.isEmpty() ? 0 : Long.parseLong(sanitized);
            previewLabel.setText(formatRupees(val));
        });

        customizeBox.getChildren().addAll(customizeHint, inputRow, previewLabel);

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: rgba(11,61,46,0.10);");

        HBox actionRow = new HBox(12);
        actionRow.setAlignment(Pos.CENTER_RIGHT);

        Label cancelBtn = new Label("Cancel");
        cancelBtn.setPadding(new Insets(11, 22, 11, 22));
        cancelBtn.setStyle("-fx-background-color: transparent; -fx-border-color: rgba(11,61,46,0.20);" +
                "-fx-border-radius: 10; -fx-background-radius: 10;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;");
        cancelBtn.setOnMouseClicked(e -> Dashboard.myStage.setScene(getProjectManagementScene()));

        Label rejectBtn = new Label("\u2715  Reject Project");
        rejectBtn.setPadding(new Insets(11, 22, 11, 22));
        rejectBtn.setStyle("-fx-background-color: transparent; -fx-border-color: " + DELAYED_RED + ";" +
                "-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-width: 1.5;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + DELAYED_RED + "; -fx-cursor: hand;");
        // Demo-only in-memory rejection: flips this project to "Rejected"
        // (budget is left untouched) and returns to Project Management.
        rejectBtn.setOnMouseClicked(e -> {
            ALL_PROJECTS.remove(p);
            ALL_PROJECTS.add(new Project(p.projectName, p.projectId, p.village, p.locality, p.department,
                    p.budget, "Rejected", p.date, null,
                    p.category, p.location, p.expectedBudget, p.expectedDuration));
            Dashboard.myStage.setScene(getProjectManagementScene());
        });

        Label approveProjectBtn = new Label("\u2713  Approve Project");
        approveProjectBtn.setPadding(new Insets(11, 24, 11, 24));
        approveProjectBtn.setStyle("-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
                "-fx-text-fill: white; -fx-background-radius: 10; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13.5px; -fx-font-weight: 800; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.30), 10, 0.1, 0, 4);");
        // Demo-only in-memory approval: commits the customized budget (or the
        // original figure if untouched), flips this project to "Approved",
        // and returns to Project Management.
        approveProjectBtn.setOnMouseClicked(e -> {
            String digits = budgetField.getText().replaceAll("[^0-9]", "");
            long finalBudget = digits.isEmpty() ? originalBudget : Long.parseLong(digits);
            String newBudget = formatRupees(finalBudget);
            ALL_PROJECTS.remove(p);
            ALL_PROJECTS.add(new Project(p.projectName, p.projectId, p.village, p.locality, p.department,
                    newBudget, "Approved", p.date, null,
                    p.category, p.location, newBudget, p.expectedDuration));
            Dashboard.myStage.setScene(getProjectManagementScene());
        });

        actionRow.getChildren().addAll(cancelBtn, rejectBtn, approveProjectBtn);

        card.getChildren().addAll(title, statsRow, sectionDivider, customizeTitle, customizeBox, divider, actionRow);
        addHoverLift(card, 24);
        return card;
    }

    /** Parses a formatted rupee string like "\u20B91,20,000" back into a plain long. Returns 0 for non-numeric values. */
    private long parseRupeeAmount(String rupeeStr) {
        if (rupeeStr == null) return 0;
        String digits = rupeeStr.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return 0;
        try {
            return Long.parseLong(digits);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    /** Formats a plain long as an Indian-grouped rupee string, e.g. 150000 -> "\u20B91,50,000". */
    private String formatRupees(long amount) {
        String num = String.valueOf(Math.abs(amount));
        String grouped;
        if (num.length() <= 3) {
            grouped = num;
        } else {
            String last3 = num.substring(num.length() - 3);
            String rest = num.substring(0, num.length() - 3);
            StringBuilder sb = new StringBuilder();
            for (int i = rest.length(); i > 0; i -= 2) {
                int start = Math.max(0, i - 2);
                sb.insert(0, rest.substring(start, i));
                if (start > 0) sb.insert(0, ",");
            }
            grouped = sb + "," + last3;
        }
        return "\u20B9" + (amount < 0 ? "-" : "") + grouped;
    }

    /* ---------------- Project Inventory (dynamic) ---------------- */
    private void refreshInventoryPanel() {
        inventoryPanel.getChildren().clear();

        List<Project> scoped = getProjectsForSelectedVillage();

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Project Inventory");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        TextField filter = new TextField();
        filter.setPromptText("Filter projects...");
        filter.setPrefWidth(240);
        filter.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 12; -fx-border-radius: 12;" +
                "-fx-border-color: rgba(11,61,46,0.10); -fx-padding: 10 14; -fx-font-size: 13px;");
        header.getChildren().addAll(title, spacer, filter);

        if (scoped.isEmpty()) {
            inventoryPanel.getChildren().addAll(header,
                    emptyState("No projects found for " + Dashboard.selectedVillage + "."));
            return;
        }

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(16);
        grid.setPadding(new Insets(6, 0, 0, 0));
        grid.getColumnConstraints().addAll(pct(28), pct(16), pct(18), pct(14), pct(14), pct(10));

        addInventoryHeader(grid);

        int row = 1;
        for (Project p : scoped) {
            String statusColor = statusColor(p.status);
            addInventoryRow(grid, row++, p, statusColor);
        }

        inventoryPanel.getChildren().addAll(header, grid);
    }

    private String statusColor(String status) {
        switch (status) {
            case "Delayed": return DELAYED_RED;
            case "Rejected": return DELAYED_RED;
            case "In Progress": return SAFFRON_MAIN;
            case "Pending": return AI_VIOLET;
            case "Completed": return CONTEXT_TEAL;
            default: return FOREST_DEEP; // Approved
        }
    }

    private void addInventoryHeader(GridPane grid) {
        grid.add(headerCell("PROJECT NAME"), 0, 0);
        grid.add(headerCell("VILLAGE"), 1, 0);
        grid.add(headerCell("DEPARTMENT"), 2, 0);
        grid.add(headerCell("BUDGET"), 3, 0);
        grid.add(headerCell("STATUS"), 4, 0);
        grid.add(headerCell("ACTION"), 5, 0);
    }

    private void addInventoryRow(GridPane grid, int row, Project p, String statusColor) {
        Label projectLabel = new Label(p.projectName);
        projectLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Label villageLabel = new Label(p.village);
        villageLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");
        Label departmentLabel = new Label(p.department);
        departmentLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");
        Label budgetLabel = new Label(p.budget);
        budgetLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");
        Label statusLabel = new Label(p.status);
        statusLabel.setPadding(new Insets(4, 10, 4, 10));
        statusLabel.setMaxWidth(Region.USE_PREF_SIZE);
        statusLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + statusColor + "; -fx-background-color: " + rgba(statusColor, 0.12) + "; -fx-background-radius: 999;");

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_LEFT);
        Label viewLabel = new Label("\uD83D\uDC41");
        viewLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");
        actions.getChildren().add(viewLabel);
        // "View Reports" — Approved projects only; not wired to a live data
        // source yet, just opens the mock reports page for this project.
        if (!"Pending".equals(p.status)) {
            Label reportsLabel = new Label("\uD83D\uDCC4");
            reportsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + SAFFRON_MAIN + "; -fx-cursor: hand;");
            reportsLabel.setOnMouseClicked(e -> Dashboard.myStage.setScene(buildProjectReportsScene(p)));
            actions.getChildren().add(reportsLabel);
        }

        grid.add(projectLabel, 0, row);
        grid.add(villageLabel, 1, row);
        grid.add(departmentLabel, 2, row);
        grid.add(budgetLabel, 3, row);
        grid.add(statusLabel, 4, row);
        grid.add(actions, 5, row);
    }

    private Label headerCell(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.60); -fx-letter-spacing: 0.06em;");
        return label;
    }

    private Label emptyState(String message) {
        Label label = new Label(message);
        label.setWrapText(true);
        label.setPadding(new Insets(24, 0, 8, 0));
        label.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 600;" +
                "-fx-text-fill: rgba(11,61,46,0.55);");
        return label;
    }

    private ColumnConstraints pct(double width) {
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(width);
        return cc;
    }

    /* ============================================================
     *  SHARED STYLE HELPERS — same copies as every other page
     * ============================================================ */

    private String cardStyle(int radius) {
        return "-fx-background-color: rgba(255,255,255,0.88);" +
               "-fx-background-radius: " + radius + ";" +
               "-fx-border-color: rgba(255,255,255,0.5);" +
               "-fx-border-radius: " + radius + ";" +
               "-fx-border-width: 1;" +
               "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.06), 16, 0.1, 0, 4);";
    }

    private void addHoverLift(Region card, int radius) {
        String base = cardStyle(radius);
        String hover = "-fx-background-color: rgba(255,255,255,0.92);" +
               "-fx-background-radius: " + radius + ";" +
               "-fx-border-color: rgba(255,255,255,0.6);" +
               "-fx-border-radius: " + radius + ";" +
               "-fx-border-width: 1;" +
               "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.12), 24, 0.15, 0, 8);" +
               "-fx-translate-y: -2;";
        card.setOnMouseEntered(e -> card.setStyle(hover));
        card.setOnMouseExited(e -> card.setStyle(base));
    }

    private String rgba(String hex, double alpha) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
    }
}