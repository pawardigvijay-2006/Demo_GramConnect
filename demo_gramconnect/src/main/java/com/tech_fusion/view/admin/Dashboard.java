package com.tech_fusion.view.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
// import javafx.scene.control.Separator;
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
 * GramConnect - BDO (Block Development Officer) Dashboard Page
 * ------------------------------------------------------------------
 * Same glass / forest-saffron template as {@link ProjectManagement},
 * {@link BudgetManagment}, {@link ComplaintManagement} and
 * {@link ReportsAnalytics}. Keeps BDO's own content (Block
 * Development Overview, Recent Projects requiring Approval,
 * Emergency Queue, Recent Citizen Complaints) but rebuilt with the
 * shared sidebar / top bar / card shell and Runnable-based full mesh
 * navigation through the shared {@code Dashboard.myStage}.
 *
 * A "Managing Village" switcher lives in the sidebar so the BDO
 * can toggle which village under their block the dashboard is
 * currently scoped to ("All Villages" or any single village).
 *
 * NOTE: {@link #VILLAGES} and {@link #selectedVillage} are static so
 * the currently-selected village is shared across every page in the
 * app (Dashboard, ProjectManagement, ReportsAnalytics, etc). Whichever
 * village the BDO picks on one page stays selected when they navigate
 * to another page, since every page reads/writes this same
 * authoritative state instead of keeping its own copy.
 *
 * ------------------------------------------------------------------
 * VILLAGE-WISE DYNAMIC DATA
 * ------------------------------------------------------------------
 * Every number and row on this page is now derived from
 * {@link #allProjects}, {@link #allComplaints}, {@link #allEmergencies}
 * and {@link #villageBudgetCr} — filtered through
 * {@link #getSelectedVillageProjects()}, {@link #getSelectedVillageComplaints()},
 * {@link #getSelectedVillageEmergencies()} and {@link #getSelectedVillageBudget()}.
 * Nothing is hard-coded per village any more. When the BDO taps a
 * village in the sidebar, {@link #villageToggle(String, ToggleGroup)}
 * updates {@code selectedVillage} and immediately calls
 * {@link #refreshDashboard()}, which rebuilds the stat cards, the
 * projects panel, the emergency queue and the complaints panel in
 * place — no page reload required.
 *
 * FIREBASE READINESS: {@link #loadSampleData()} is the single seam
 * where local sample lists are populated. To switch to Firebase,
 * replace the body of that method with a Firestore fetch that
 * populates the same {@code allProjects} / {@code allComplaints} /
 * {@code allEmergencies} / {@code villageBudgetCr} fields — every
 * other method in this class (filtering, KPI math, UI building)
 * already reads through those fields and needs no further changes.
 */
public class Dashboard extends Application {

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

    private static final String BACKGROUND_IMAGE_PATH ="demo_gramconnect\\src\\main\\resources\\assets\\images\\WhatsApp Image 2026-08-10 at 11.55.38 PM.jpeg";

    /**
     * Villages under this BDO's block. "All Villages" aggregates every one
     * below it. Public + static so every page in the app shares the exact
     * same village list (no more mismatched lists between pages).
     */
    public static final List<String> VILLAGES = Arrays.asList(
            "All Villages", "Rampur", "Sitapur", "Madhavpur", "Ward 4 Cluster"
    );

    /**
     * Currently active village scope, defaults to the aggregate view.
     * Static so the selection persists across page navigation — every
     * page reads/writes this same field instead of keeping its own
     * instance-level copy that would reset on every {@code new Page()}.
     */
    public static String selectedVillage = VILLAGES.get(0);

    private Label selectedNavItem;

    private Label villageNameLabel;
    private Label villageChevron;
    private VBox villageListBox;
    private Label scopeSubtitle;
    private boolean villageListExpanded = false;

    public static Stage myStage;

    /* ============================================================
     *  DATA MODEL — village-wise, Firebase-ready
     * ============================================================ */

    /** A single project entry. {@code village} must be one of {@link #VILLAGES} (excluding "All Villages"). */
    private static class Project {
        String id;
        String title;
        String location;   // e.g. "Main Street" — sub-label shown under the title, NOT the filter key
        String village;    // filter key — must match a Dashboard.VILLAGES entry
        String status;     // "In Review" | "Approved" | "Completed" | "Delayed"
        String budget;     // display string, e.g. "₹1,20,000"

        Project(String id, String title, String location, String village, String status, String budget) {
            this.id = id;
            this.title = title;
            this.location = location;
            this.village = village;
            this.status = status;
            this.budget = budget;
        }

        boolean isApprovable() { return "In Review".equals(status); }
    }

    /** A single citizen complaint entry. */
    private static class Complaint {
        String id;
        String type;
        String village;
        String description;
        String date;
        String status;      // "Pending" | "In Progress" | "Resolved"

        Complaint(String id, String type, String village, String description, String date, String status) {
            this.id = id;
            this.type = type;
            this.village = village;
            this.description = description;
            this.date = date;
            this.status = status;
        }

        boolean isAssignable() { return "Pending".equals(status); }
    }

    /** A single emergency-queue entry. */
    private static class EmergencyItem {
        String title;
        String village;
        String description;
        String timeAgo;
        boolean waterType; // true = water-style red card, false = road-style saffron card

        EmergencyItem(String title, String village, String description, String timeAgo, boolean waterType) {
            this.title = title;
            this.village = village;
            this.description = description;
            this.timeAgo = timeAgo;
            this.waterType = waterType;
        }
    }

    /** Central, Firebase-ready data lists. Populated once by {@link #loadSampleData()}. */
    private List<Project> allProjects;
    private List<Complaint> allComplaints;
    private List<EmergencyItem> allEmergencies;

    /** village name -> {allocatedCr, usedCr}. "All Villages" is derived by summing these, never stored directly. */
    private Map<String, double[]> villageBudgetCr;

    /* ---- References kept so refreshDashboard() can rebuild sections in place ---- */
    private VBox mainContentBox;
    private HBox statCardsRowRef;
    private HBox midSectionRef;
    private VBox complaintsPanelRef;

    @Override
    public void start(Stage stage) {
        loadSampleData();
        Dashboard.myStage = stage;
        stage.setTitle("GramConnect - BDO Office | Block Development Dashboard");
        stage.setScene(getBDODashboardScene());
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.show();
    }

    /** Builds the BDO Dashboard scene. Public so other pages can navigate here. */
    public Scene getBDODashboardScene() {
        if (allProjects == null) {
            // Guard for callers that construct Dashboard directly (e.g. sidebar navigation)
            // without going through start(), so sample data is always available.
            loadSampleData();
        }

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
     *  SAMPLE DATA — swap this method for a Firebase fetch later.
     *  Every other method reads through allProjects / allComplaints /
     *  allEmergencies / villageBudgetCr, so nothing else needs to change.
     * ============================================================ */
    private void loadSampleData() {
        allProjects = new ArrayList<>(Arrays.asList(
                new Project("#PRJ-089", "Village Road Construction", "Main Street", "Rampur", "In Review", "\u20B91,20,000"),
                new Project("#PRJ-088", "Water Tank Renovation", "Near School Area", "Sitapur", "Approved", "\u20B985,000"),
                new Project("#PRJ-085", "Panchayat Bhavan Repair", "Gram Panchayat Office", "Madhavpur", "In Review", "\u20B92,50,000"),
                new Project("#PRJ-091", "Community Hall Flooring", "Ward Office", "Ward 4 Cluster", "Completed", "\u20B960,000"),
                new Project("#PRJ-093", "Drainage Line Upgrade", "Market Road", "Rampur", "Completed", "\u20B995,000"),
                new Project("#PRJ-094", "Solar Streetlight Installation", "Bus Stand", "Sitapur", "Delayed", "\u20B91,10,000"),
                new Project("#PRJ-095", "Anganwadi Renovation", "Ward 3", "Madhavpur", "In Review", "\u20B970,000"),
                new Project("#PRJ-096", "Check Dam Construction", "River Side", "Ward 4 Cluster", "In Review", "\u20B93,00,000")
        ));

        allComplaints = new ArrayList<>(Arrays.asList(
                new Complaint("#CMP-102", "Water Supply", "Rampur", "Low pressure in Ward 2 for 3 days.", "Oct 24, 2023", "Pending"),
                new Complaint("#CMP-098", "Street Lighting", "Sitapur", "Main road lights flickering near temple.", "Oct 23, 2023", "Resolved"),
                new Complaint("#CMP-101", "Drainage Blockage", "Madhavpur", "Overflow near market area.", "Oct 22, 2023", "Pending"),
                new Complaint("#CMP-097", "Road Damage", "Ward 4 Cluster", "Potholes near bus stand.", "Oct 20, 2023", "In Progress")
        ));

        allEmergencies = new ArrayList<>(Arrays.asList(
                new EmergencyItem("Water Shortage", "Ward 4 Cluster", "Ward 4 main supply line burst. Immediate repair needed.", "10m ago", true),
                new EmergencyItem("Road Blockage", "Rampur", "Fallen tree blocking access to North Village clinic.", "1h ago", false)
        ));

        villageBudgetCr = new LinkedHashMap<>();
        villageBudgetCr.put("Rampur",          new double[]{1.6, 1.2});
        villageBudgetCr.put("Sitapur",         new double[]{1.2, 0.9});
        villageBudgetCr.put("Madhavpur",       new double[]{0.9, 0.55});
        villageBudgetCr.put("Ward 4 Cluster",  new double[]{0.7, 0.35});
    }

    /* ============================================================
     *  VILLAGE FILTERING — single source of truth for all sections
     * ============================================================ */

    /** Projects for the currently selected village, or all projects if "All Villages" is selected. */
    private List<Project> getSelectedVillageProjects() {
        if (Dashboard.VILLAGES.get(0).equals(Dashboard.selectedVillage)) {
            return allProjects;
        }
        return allProjects.stream()
                .filter(p -> p.village.equals(Dashboard.selectedVillage))
                .collect(Collectors.toList());
    }

    /** Complaints for the currently selected village, or all complaints if "All Villages" is selected. */
    private List<Complaint> getSelectedVillageComplaints() {
        if (Dashboard.VILLAGES.get(0).equals(Dashboard.selectedVillage)) {
            return allComplaints;
        }
        return allComplaints.stream()
                .filter(c -> c.village.equals(Dashboard.selectedVillage))
                .collect(Collectors.toList());
    }

    /** Emergency-queue items for the currently selected village, or all if "All Villages" is selected. */
    private List<EmergencyItem> getSelectedVillageEmergencies() {
        if (Dashboard.VILLAGES.get(0).equals(Dashboard.selectedVillage)) {
            return allEmergencies;
        }
        return allEmergencies.stream()
                .filter(e -> e.village.equals(Dashboard.selectedVillage))
                .collect(Collectors.toList());
    }

    /** {allocatedCr, usedCr} for the selected village, summed across all villages for "All Villages". */
    private double[] getSelectedVillageBudget() {
        if (Dashboard.VILLAGES.get(0).equals(Dashboard.selectedVillage)) {
            double allocated = 0, used = 0;
            for (double[] v : villageBudgetCr.values()) {
                allocated += v[0];
                used += v[1];
            }
            return new double[]{allocated, used};
        }
        double[] v = villageBudgetCr.get(Dashboard.selectedVillage);
        return v != null ? v : new double[]{0, 0};
    }

    private String colorForProjectStatus(String status) {
        switch (status) {
            case "In Review": return SAFFRON_MAIN;
            case "Delayed":   return DELAYED_RED;
            case "Approved":
            case "Completed":
            default:          return CONTEXT_TEAL;
        }
    }

    private String colorForComplaintStatus(String status) {
        switch (status) {
            case "Pending":     return SAFFRON_MAIN;
            case "In Progress": return AI_VIOLET;
            case "Resolved":
            default:            return CONTEXT_TEAL;
        }
    }

    /* ============================================================
     *  REFRESH — called immediately after Dashboard.selectedVillage
     *  changes, so the whole page updates without a reload.
     * ============================================================ */
    private void refreshDashboard() {
        updateScopeSubtitle();

        if (mainContentBox == null) return;

        HBox newStatRow = buildStatCardsRow();
        int statIdx = mainContentBox.getChildren().indexOf(statCardsRowRef);
        if (statIdx >= 0) mainContentBox.getChildren().set(statIdx, newStatRow);
        statCardsRowRef = newStatRow;

        HBox newMidSection = buildMidSection();
        int midIdx = mainContentBox.getChildren().indexOf(midSectionRef);
        if (midIdx >= 0) mainContentBox.getChildren().set(midIdx, newMidSection);
        midSectionRef = newMidSection;

        VBox newComplaintsPanel = buildComplaintsPanel();
        int cIdx = mainContentBox.getChildren().indexOf(complaintsPanelRef);
        if (cIdx >= 0) mainContentBox.getChildren().set(cIdx, newComplaintsPanel);
        complaintsPanelRef = newComplaintsPanel;
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

        HBox dashboardNav = navItem("\u25A6", "Dashboard", true);   // <-- active on this page

        HBox projectNav = navItem("\uD83D\uDDC2", "Project Management", false);
        Runnable toProjectManagement = () -> {
            ProjectManagement page = new ProjectManagement();
            Dashboard.myStage.setScene(page.getProjectManagementScene());
        };
        projectNav.setOnMouseClicked(e -> toProjectManagement.run());

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

        footer.getChildren().addAll(divider,smallLinks);

        sidebar.getChildren().addAll(header, villageSelector, nav, footer);
        return sidebar;
    }

    /* ============================================================
     *  VILLAGE SWITCHER — lets the BDO toggle scope between
     *  "All Villages" and any single village under their block.
     *  Reads/writes the static Dashboard.selectedVillage so the
     *  choice is preserved when navigating to other pages.
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

        villageNameLabel = new Label(selectedVillage);
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
        for (String village : VILLAGES) {
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
        btn.setSelected(village.equals(selectedVillage));
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
                selectedVillage = village;
                villageNameLabel.setText(selectedVillage);
                updateScopeSubtitle();
                // Immediately refresh every dynamic section on this page — no reload required.
                refreshDashboard();
                // Collapse the list once a village has been chosen.
                villageListExpanded = false;
                villageListBox.setVisible(false);
                villageListBox.setManaged(false);
                villageChevron.setText("\u25BE");
            }
        });

        return btn;
    }

    /** Keeps the page subtitle in sync with whichever village is currently selected. */
    private void updateScopeSubtitle() {
        if (scopeSubtitle == null) return;
        if (VILLAGES.get(0).equals(selectedVillage)) {
            scopeSubtitle.setText("Monitor village development, projects and governance \u2014 Showing data for: All Villages (Block)");
        } else {
            scopeSubtitle.setText("Monitor village development, projects and governance \u2014 Showing data for: " + selectedVillage);
        }
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
     *  TOP NAVIGATION BAR — shared shell
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
        searchField.setPromptText("Search villages, projects, or approvals...");
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
        Circle dot = new Circle(5, Color.web(DELAYED_RED));
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

    /* ============================================================
     *  MAIN CONTENT
     * ============================================================ */
    private VBox buildMainContent() {
        VBox main = new VBox(24);
        main.setPadding(new Insets(32, 40, 48, 40));
        main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");

        statCardsRowRef = buildStatCardsRow();
        midSectionRef = buildMidSection();
        complaintsPanelRef = buildComplaintsPanel();

        main.getChildren().addAll(
                buildTitleRow(),
                statCardsRowRef,
                midSectionRef,
                complaintsPanelRef
        );

        mainContentBox = main;
        return main;
    }

    private HBox buildTitleRow() {
        VBox text = new VBox(6);
        Label title = new Label("Block Development Overview");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        scopeSubtitle = new Label("Monitor village development, projects and governance");
        scopeSubtitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.60);");
        updateScopeSubtitle();
        text.getChildren().addAll(title, scopeSubtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerRow = new HBox(20, text, spacer);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        return headerRow;
    }

    /* ============================================================
     *  STAT CARDS ROW — every value now computed from the
     *  village-filtered data, never hard-coded.
     * ============================================================ */
    private HBox buildStatCardsRow() {
        HBox row = new HBox(20);

        List<Project> villageProjects = getSelectedVillageProjects();
        List<Complaint> villageComplaints = getSelectedVillageComplaints();
        double[] budget = getSelectedVillageBudget();

        int totalProjects = villageProjects.size();
        long pendingApprovals = villageProjects.stream().filter(Project::isApprovable).count();
        long completedProjects = villageProjects.stream().filter(p -> "Completed".equals(p.status)).count();

        double allocatedCr = budget[0];
        double usedCr = budget[1];
        int utilizationPct = allocatedCr > 0 ? (int) Math.round((usedCr / allocatedCr) * 100) : 0;

        int completionRate = totalProjects > 0 ? (int) Math.round((completedProjects * 100.0) / totalProjects) : 0;

        VBox totalCard = kpiCard(FOREST_LIGHT, "\uD83D\uDCBC", "TOTAL PROJECTS", String.valueOf(totalProjects));
        Label totalFooter = new Label(completionRate + "% completed for this scope");
        totalFooter.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + CONTEXT_TEAL + ";");
        totalCard.getChildren().add(totalFooter);

        VBox pendingCard = kpiCard(SAFFRON_MAIN, "\u23F3", "PENDING APPROVALS", String.valueOf(pendingApprovals));
        Label pendingBadge = new Label(pendingApprovals > 0 ? "High Priority" : "All Clear");
        pendingBadge.setPadding(new Insets(6, 10, 6, 10));
        pendingBadge.setMaxWidth(Region.USE_PREF_SIZE);
        pendingBadge.setStyle("-fx-background-color: rgba(224,122,31,0.14); -fx-background-radius: 6;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 700; -fx-text-fill: " + SAFFRON_MAIN + ";");
        pendingCard.getChildren().add(pendingBadge);

        VBox budgetCard = kpiCard(CONTEXT_TEAL, "\uD83D\uDCCA", "BUDGET UTILIZATION", utilizationPct + "%");
        VBox budgetExtra = new VBox(8);
        budgetExtra.getChildren().add(progressBar(utilizationPct / 100.0, CONTEXT_TEAL, 8));
        Label budgetFootnote = new Label(String.format("\u20B9%.1fCr / \u20B9%.1fCr Sanctioned", usedCr, allocatedCr));
        budgetFootnote.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.60);");
        budgetExtra.getChildren().add(budgetFootnote);
        budgetCard.getChildren().add(budgetExtra);

        VBox reportsCard = kpiCard(AI_VIOLET, "\uD83D\uDCC8", "REPORTS & ANALYTICS", "+" + completionRate + "%");
        VBox reportsExtra = new VBox(4);
        Label reportsFootnote = new Label(villageComplaints.size() + " citizen complaints on record");
        reportsFootnote.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.60);");
        Label reportsTrend = new Label(completionRate >= 50 ? "\u2197  Growth on track" : "\u26A0  Needs attention");
        reportsTrend.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " +
                (completionRate >= 50 ? CONTEXT_TEAL : DELAYED_RED) + ";");
        reportsExtra.getChildren().addAll(reportsFootnote, reportsTrend);
        reportsCard.getChildren().add(reportsExtra);

        HBox.setHgrow(totalCard, Priority.ALWAYS);
        HBox.setHgrow(pendingCard, Priority.ALWAYS);
        HBox.setHgrow(budgetCard, Priority.ALWAYS);
        HBox.setHgrow(reportsCard, Priority.ALWAYS);
        row.getChildren().addAll(totalCard, pendingCard, budgetCard, reportsCard);
        return row;
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

    /* ============================================================
     *  MID SECTION: Recent Projects panel + Emergency Queue panel
     * ============================================================ */
    private HBox buildMidSection() {
        HBox row = new HBox(20);
        row.setFillHeight(true);

        VBox projects = buildProjectsPanel();
        VBox emergency = buildEmergencyQueuePanel();

        emergency.setPrefWidth(340);
        emergency.setMinWidth(340);
        emergency.setMaxWidth(340);

        projects.setMinWidth(0);
        HBox.setHgrow(projects, Priority.ALWAYS);

        row.getChildren().addAll(projects, emergency);
        return row;
    }

    private VBox buildProjectsPanel() {
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Recent Projects requiring Approval");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label viewAll = new Label("View All");
        viewAll.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-font-weight: 700; -fx-cursor: hand;");
        Runnable toProjectManagement = () -> {
            ProjectManagement page = new ProjectManagement();
            Dashboard.myStage.setScene(page.getProjectManagementScene());
        };
        viewAll.setOnMouseClicked(e -> toProjectManagement.run());
        header.getChildren().addAll(title, spacer, viewAll);

        List<Project> villageProjects = getSelectedVillageProjects();
        // Show up to 3 rows, pending ("In Review") projects surfaced first.
        List<Project> shown = villageProjects.stream()
                .sorted((a, b) -> Boolean.compare(!a.isApprovable(), !b.isApprovable()))
                .limit(3)
                .collect(Collectors.toList());

        long pendingCount = villageProjects.stream().filter(Project::isApprovable).count();

        if (shown.isEmpty()) {
            Label empty = new Label("No projects on record for " + Dashboard.selectedVillage + ".");
            empty.setPadding(new Insets(20, 8, 20, 8));
            empty.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.55);");
            panel.getChildren().addAll(header, empty);
            addHoverLift(panel, 24);
            return panel;
        }

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(6);
        grid.setPadding(new Insets(6, 0, 0, 0));
        grid.getColumnConstraints().addAll(pct(12), pct(46), pct(17), pct(15), pct(10));

        grid.add(headerCell("ID"), 0, 0);
        grid.add(headerCell("PROJECT TITLE & VILLAGE"), 1, 0);
        grid.add(headerCell("STATUS"), 2, 0);
        grid.add(headerCell("BUDGET"), 3, 0);
        grid.add(headerCell("ACTIONS"), 4, 0);

        int gridRow = 1;
        for (int i = 0; i < shown.size(); i++) {
            Project p = shown.get(i);
            addProjectRow(grid, gridRow, p.id, p.title, p.location + ", " + p.village,
                    p.status, colorForProjectStatus(p.status), p.budget, p.isApprovable());
            gridRow++;
            if (i < shown.size() - 1) {
                grid.add(rowDivider(), 0, gridRow, 5, 1);
                gridRow++;
            }
        }

        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(16, 0, 0, 0));
        footer.setStyle("-fx-border-color: rgba(11,61,46,0.08) transparent transparent transparent; -fx-border-width: 1 0 0 0;");
        Label selected = new Label(pendingCount + " item" + (pendingCount == 1 ? "" : "s") + " pending review");
        selected.setPadding(new Insets(14, 0, 0, 0));
        selected.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.60);");
        Region fSpacer = new Region();
        HBox.setHgrow(fSpacer, Priority.ALWAYS);
        Label bulkApprove = new Label("Bulk Approve Selected");
        bulkApprove.setPadding(new Insets(10, 18, 10, 18));
        bulkApprove.setStyle("-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
                "-fx-text-fill: white; -fx-background-radius: 8; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px; -fx-font-weight: 700; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.30), 8, 0.1, 0, 3);");
        footer.getChildren().addAll(selected, fSpacer, bulkApprove);

        panel.getChildren().addAll(header, grid, footer);
        addHoverLift(panel, 24);
        return panel;
    }

    private void addProjectRow(GridPane grid, int row, String id, String title, String village,
                                String status, String statusColor, String budget, boolean approvable) {
        Label idLabel = new Label(id);
        idLabel.setPadding(new Insets(12, 8, 12, 8));
        idLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.55);");

        VBox titleBox = new VBox(2);
        titleBox.setPadding(new Insets(12, 8, 12, 8));
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Label villageLabel = new Label(village);
        villageLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.55);");
        titleBox.getChildren().addAll(titleLabel, villageLabel);

        Label statusLabel = new Label(status);
        statusLabel.setPadding(new Insets(4, 10, 4, 10));
        statusLabel.setMaxWidth(Region.USE_PREF_SIZE);
        statusLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + statusColor + "; -fx-background-color: " + rgba(statusColor, 0.12) + "; -fx-background-radius: 999;");
        HBox statusCell = new HBox(statusLabel);
        statusCell.setAlignment(Pos.CENTER_LEFT);
        statusCell.setPadding(new Insets(12, 8, 12, 8));

        Label budgetLabel = new Label(budget);
        budgetLabel.setPadding(new Insets(12, 8, 12, 8));
        budgetLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");

        HBox actions = new HBox(10);
        actions.setPadding(new Insets(12, 8, 12, 8));
        Label view = new Label("\uD83D\uDC41");
        view.setStyle("-fx-font-size: 14px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");
        actions.getChildren().add(view);
        if (approvable) {
            Label approve = new Label("\u2713");
            approve.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");
            actions.getChildren().add(approve);
        }

        grid.add(idLabel, 0, row);
        grid.add(titleBox, 1, row);
        grid.add(statusCell, 2, row);
        grid.add(budgetLabel, 3, row);
        grid.add(actions, 4, row);
    }

    private Region rowDivider() {
        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: rgba(11,61,46,0.06);");
        return divider;
    }

    /* ============================================================
     *  EMERGENCY QUEUE PANEL
     * ============================================================ */
    private VBox buildEmergencyQueuePanel() {
        VBox panel = new VBox(16);
        panel.setStyle(cardStyle(24));
        panel.setPadding(new Insets(0, 0, 20, 0));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 24, 20, 24));
        header.setStyle("-fx-background-color: " + rgba(DELAYED_RED, 0.10) + "; -fx-background-radius: 24 24 0 0;");
        Label icon = new Label("\u26A0");
        icon.setStyle("-fx-text-fill: " + DELAYED_RED + "; -fx-font-size: 16px;");
        Label title = new Label("Emergency Queue");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 17px; -fx-font-weight: 900; -fx-text-fill: " + DELAYED_RED + ";");
        header.getChildren().addAll(icon, title);

        panel.getChildren().add(header);

        List<EmergencyItem> shown = getSelectedVillageEmergencies();

        if (shown.isEmpty()) {
            Label empty = new Label("No active emergencies in " + Dashboard.selectedVillage + ".");
            empty.setWrapText(true);
            empty.setPadding(new Insets(4, 20, 20, 20));
            empty.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.55);");
            panel.getChildren().add(empty);
            addHoverLift(panel, 24);
            return panel;
        }

        for (EmergencyItem item : shown) {
            String accent = item.waterType ? DELAYED_RED : SAFFRON_MAIN;

            VBox card = new VBox(10);
            card.setPadding(new Insets(14, 16, 14, 16));
            VBox.setMargin(card, new Insets(0, 20, 0, 20));
            card.setStyle("-fx-background-color: " + rgba(accent, item.waterType ? 0.06 : 0.08) + "; -fx-border-color: transparent transparent transparent " + accent + ";" +
                    "-fx-border-width: 0 0 0 4; -fx-background-radius: 8;");

            HBox cardRow = new HBox();
            cardRow.setAlignment(Pos.CENTER_LEFT);
            Label cardTitle = new Label(item.title);
            cardTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
            Region cardSpacer = new Region();
            HBox.setHgrow(cardSpacer, Priority.ALWAYS);
            Label cardTime = new Label(item.timeAgo);
            cardTime.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.55);");
            cardRow.getChildren().addAll(cardTitle, cardSpacer, cardTime);

            Label cardDesc = new Label(item.description);
            cardDesc.setWrapText(true);
            cardDesc.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-text-fill: rgba(11,61,46,0.70);");

            card.getChildren().addAll(cardRow, cardDesc);

            if (item.waterType) {
                HBox buttons = new HBox(8);
                Label escalate = new Label("Escalate");
                escalate.setPadding(new Insets(7, 14, 7, 14));
                escalate.setStyle("-fx-background-color: " + DELAYED_RED + "; -fx-text-fill: white; -fx-font-family: " + FONT_FAMILY + ";" +
                        "-fx-font-size: 12px; -fx-font-weight: 700; -fx-background-radius: 6; -fx-cursor: hand;");
                Label quickApprove = new Label("Quick Approve Funds");
                quickApprove.setWrapText(true);
                quickApprove.setPadding(new Insets(7, 12, 7, 12));
                quickApprove.setStyle("-fx-background-color: rgba(255,255,255,0.75); -fx-text-fill: " + FOREST_DEEP + "; -fx-font-family: " + FONT_FAMILY + ";" +
                        "-fx-font-size: 12px; -fx-font-weight: 700; -fx-border-color: rgba(11,61,46,0.15); -fx-border-radius: 6;" +
                        "-fx-background-radius: 6; -fx-cursor: hand;");
                buttons.getChildren().addAll(escalate, quickApprove);
                card.getChildren().add(buttons);
            } else {
                Label assignTeam = new Label("Assign Team");
                assignTeam.setPadding(new Insets(7, 14, 7, 14));
                assignTeam.setMaxWidth(Region.USE_PREF_SIZE);
                assignTeam.setStyle("-fx-background-color: rgba(255,255,255,0.75); -fx-text-fill: " + FOREST_DEEP + "; -fx-font-family: " + FONT_FAMILY + ";" +
                        "-fx-font-size: 12px; -fx-font-weight: 700; -fx-border-color: rgba(11,61,46,0.15); -fx-border-radius: 6;" +
                        "-fx-background-radius: 6; -fx-cursor: hand;");
                card.getChildren().add(assignTeam);
            }

            panel.getChildren().add(card);
        }

        addHoverLift(panel, 24);
        return panel;
    }

    /* ============================================================
     *  RECENT CITIZEN COMPLAINTS
     * ============================================================ */
    private VBox buildComplaintsPanel() {
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Recent Citizen Complaints");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label viewAll = new Label("View All Complaints");
        viewAll.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-font-weight: 700; -fx-cursor: hand;");
        Runnable toComplaintManagement = () -> {
            ComplaintManagement page = new ComplaintManagement();
            Dashboard.myStage.setScene(page.getComplaintManagementScene());
        };
        viewAll.setOnMouseClicked(e -> toComplaintManagement.run());
        header.getChildren().addAll(title, spacer, viewAll);

        List<Complaint> shown = getSelectedVillageComplaints().stream().limit(2).collect(Collectors.toList());

        if (shown.isEmpty()) {
            Label empty = new Label("No complaints on record for " + Dashboard.selectedVillage + ".");
            empty.setPadding(new Insets(20, 8, 4, 8));
            empty.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.55);");
            panel.getChildren().addAll(header, empty);
            addHoverLift(panel, 24);
            return panel;
        }

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(6);
        grid.setPadding(new Insets(6, 0, 0, 0));
        double[] widths = {10, 15, 12, 33, 13, 10, 7};
        for (double w : widths) grid.getColumnConstraints().add(pct(w));

        grid.add(headerCell("ID"), 0, 0);
        grid.add(headerCell("COMPLAINT TYPE"), 1, 0);
        grid.add(headerCell("VILLAGE"), 2, 0);
        grid.add(headerCell("DESCRIPTION"), 3, 0);
        grid.add(headerCell("DATE RECEIVED"), 4, 0);
        grid.add(headerCell("STATUS"), 5, 0);
        grid.add(headerCell("ACTIONS"), 6, 0);

        int gridRow = 1;
        for (int i = 0; i < shown.size(); i++) {
            Complaint c = shown.get(i);
            addComplaintRow(grid, gridRow, c.id, c.type, c.village, c.description, c.date,
                    c.status, colorForComplaintStatus(c.status), c.isAssignable());
            gridRow++;
            if (i < shown.size() - 1) {
                grid.add(rowDivider(), 0, gridRow, 7, 1);
                gridRow++;
            }
        }

        panel.getChildren().addAll(header, grid);
        addHoverLift(panel, 24);
        return panel;
    }

    private void addComplaintRow(GridPane grid, int row, String id, String type, String village, String description,
                                  String date, String status, String statusColor, boolean assignable) {
        Label idLabel = new Label(id);
        idLabel.setPadding(new Insets(12, 8, 12, 8));
        idLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.55);");

        Label typeLabel = new Label(type);
        typeLabel.setPadding(new Insets(12, 8, 12, 8));
        typeLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");

        Label villageLabel = new Label(village);
        villageLabel.setPadding(new Insets(12, 8, 12, 8));
        villageLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");

        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        descLabel.setPadding(new Insets(12, 8, 12, 8));
        descLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");

        Label dateLabel = new Label(date);
        dateLabel.setPadding(new Insets(12, 8, 12, 8));
        dateLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");

        Label statusLabel = new Label(status);
        statusLabel.setPadding(new Insets(4, 10, 4, 10));
        statusLabel.setMaxWidth(Region.USE_PREF_SIZE);
        statusLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + statusColor + "; -fx-background-color: " + rgba(statusColor, 0.12) + "; -fx-background-radius: 999;");
        HBox statusCell = new HBox(statusLabel);
        statusCell.setAlignment(Pos.CENTER_LEFT);
        statusCell.setPadding(new Insets(12, 8, 12, 8));

        HBox actions = new HBox(10);
        actions.setPadding(new Insets(12, 8, 12, 8));
        Label view = new Label("\uD83D\uDC41");
        view.setStyle("-fx-font-size: 14px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");
        actions.getChildren().add(view);
        if (assignable) {
            Label assign = new Label("\uD83D\uDC64");
            assign.setStyle("-fx-font-size: 14px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");
            actions.getChildren().add(assign);
        }

        grid.add(idLabel, 0, row);
        grid.add(typeLabel, 1, row);
        grid.add(villageLabel, 2, row);
        grid.add(descLabel, 3, row);
        grid.add(dateLabel, 4, row);
        grid.add(statusCell, 5, row);
        grid.add(actions, 6, row);
    }

    private Label headerCell(String text) {
        Label label = new Label(text);
        label.setPadding(new Insets(0, 8, 10, 8));
        label.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.60); -fx-letter-spacing: 0.06em;");
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