package com.tech_fusion.view.admin;

import java.util.List;
<<<<<<< HEAD
import java.util.stream.Collectors;
=======
>>>>>>> c93dffcf5bbb024bbc0e0d7fbbe47b72c71c37a2
import java.io.File;

import com.tech_fusion.model.admin.VillageDataStore;
import com.tech_fusion.model.admin.Complaint;
import com.tech_fusion.model.admin.OfficialComplaint;
import com.tech_fusion.model.admin.OfficialComplaintStore;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
<<<<<<< HEAD
=======
import javafx.scene.control.Button;
>>>>>>> c93dffcf5bbb024bbc0e0d7fbbe47b72c71c37a2
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
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
 * GramConnect - Complaint Management Page
 * ------------------------------------------------------------------
 * Same glass / forest-saffron template as {@link Dashboard} and
 * {@link BudgetManagment}. Navigates through the shared
 * {@code Dashboard.myStage} using Runnable callbacks - that is the
 * only thing this page still reaches into {@link Dashboard} for.
 *
 * ------------------------------------------------------------------
 * VILLAGE-WISE DYNAMIC DATA (Sarpanch-centric revision)
 * ------------------------------------------------------------------
 * This page keeps NO local copy of the village list, the currently
 * selected village, the current village's Sarpanch, or any complaint
 * data - all of it lives on {@link VillageDataStore} and is read
 * fresh every time {@link #refreshComplaintManagement()} runs.
 *
 * Whenever the BDO picks a different village in the sidebar selector
 * ({@link #villageToggle(String, ToggleGroup)}), three things update
 * in place, instantaneously, with no scene rebuild:
 *   1. The KPI cards (total / resolved / in-progress / pending),
 *      scoped to the selected village.
 *   2. The Sarpanch info line, showing that village's Sarpanch.
 *   3. The "Complaints Against Sarpanch" table, scoped to the
 *      selected village's Sarpanch specifically.
 *
 * There is NO project filtering anywhere on this page anymore - all
 * complaint data shown is general, village-level complaint data.
 *
 * ------------------------------------------------------------------
 * SARPANCH RESOLUTION
 * ------------------------------------------------------------------
 * Each village has its own, distinct Sarpanch, and nothing here is
 * hardcoded to a single name. This resolves through
 * {@code VillageDataStore.getSarpanchName(String village)}, which
 * returns that village's Sarpanch's display name (or null/blank if
 * unassigned). See {@link #currentSarpanchName()}.
 *
<<<<<<< HEAD
 * REMAINING ASSUMPTION: Complaint.getTargetPerson() /
 * getComplainantRole() / getCitizenName() - guessed at the most
 * natural method names for the Complaint class. If your real
 * Complaint API differs, update just
 * {@link #isTargetedAtSarpanch(Complaint, String)},
 * {@link #isFiledBySarpanch(Complaint, String)}, and
 * {@link #complainantDisplayName(Complaint)} - everything else keeps
 * working unchanged.
=======
 * ------------------------------------------------------------------
 * "COMPLAINTS AGAINST SARPANCH" PANEL - OFFICIAL COMPLAINTS ONLY
 * ------------------------------------------------------------------
 * This panel shows ONLY Official Complaints - the complaint type a
 * villager selects on the Villager Login's NewComplaintPage when
 * filing against an official (as opposed to a general issue). Each
 * row shows just Title + Location, with a "View Complaint" action
 * that opens {@link SarpanchComplaintDetailsPage}, showing the full
 * Title / Location / Official's Name / Designation / Description and
 * letting the officer mark the complaint Resolved or Rejected.
 *
 * Data comes from {@code OfficialComplaintStore} (see
 * {@link #getOfficialComplaintsForSelectedVillage()}) - a temporary,
 * in-memory stand-in for the real shared service that will eventually
 * connect the Villager Login's submissions to this page. The Villager
 * and Admin logins are NOT wired together yet; see
 * {@code OfficialComplaintStore}'s class doc for exactly what a future
 * connection looks like.
>>>>>>> c93dffcf5bbb024bbc0e0d7fbbe47b72c71c37a2
 */
public class ComplaintManagement extends Application {

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

    /** Fallback text shown when a village has no Sarpanch on record. */
    private static final String SARPANCH_UNASSIGNED = "Not Assigned";

    /**
     * Generic role string some complaints may use for "target"/"filed by"
     * instead of (or alongside) the Sarpanch's actual name. Kept as a
     * secondary match so this still works if your Complaint data tags
     * things by role rather than by name - see
     * {@link #isTargetedAtSarpanch(Complaint, String)}.
     */
    private static final String SARPANCH_ROLE = "Sarpanch";

    private Label selectedNavItem;

    /* ---------- Live references so village changes can refresh in place ---------- */
    private Label villageNameLabel;
    private Label villageChevron;
    private VBox villageListBox;
    private Label scopeSubtitle;
    private Label sarpanchNameLabel;
    private boolean villageListExpanded = false;

    // KPI cards (unchanged from the original page - still village-scoped)
    private Label totalValueLabel;
    private Label resolvedValueLabel;
    private Label resolvedFooterLabel;
    private Label progressValueLabel;
    private Label progressFooterLabel;
    private Label pendingValueLabel;

<<<<<<< HEAD
    // Sarpanch-targeted complaints (village-scoped) - the only complaint list on this page
    private GridPane sarpanchComplaintGrid;
=======
    // Official Complaints against the Sarpanch (village-scoped) - the only
    // complaint list on this page. Sourced from OfficialComplaintStore, i.e.
    // complaints filed as "Official Complaint" on the Villager Login's
    // NewComplaintPage - see the class doc above and OfficialComplaintStore
    // for how this will connect to real submissions in future.
    private VBox sarpanchComplaintList;
>>>>>>> c93dffcf5bbb024bbc0e0d7fbbe47b72c71c37a2
    private Label sarpanchComplaintEmptyLabel;

    @Override
    public void start(Stage stage) {
        Dashboard.myStage = stage;
        stage.setTitle("GramConnect - Complaint Management");
        stage.setScene(getComplaintManagementScene());
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.show();
    }

    /** Builds the Complaint Management scene. Public so other pages can navigate here. */
    public Scene getComplaintManagementScene() {
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

        // Populate every dynamic component for whichever village is
        // currently active before the scene is first shown.
        refreshComplaintManagement();

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
     *  VILLAGE HELPERS (unchanged - still drive the village selector
     *  and the KPI cards, exactly as in the original page)
     * ============================================================ */

    /** Currently selected village, defaulting safely if it hasn't been set yet. */
    private String currentVillage() {
        String v = VillageDataStore.selectedVillage;
        return (v == null) ? VillageDataStore.VILLAGES.get(0) : v;
    }

    private boolean isAllVillages() {
        return VillageDataStore.VILLAGES.get(0).equals(currentVillage());
    }

    /**
     * Every complaint belonging to the currently selected village
     * ("All Villages" returns everything). Drives both the KPI cards
     * AND (as of this revision) the Sarpanch-targeted complaint list -
     * see {@link #getSarpanchComplaintsForSelectedVillage()}.
     */
    private List<Complaint> getComplaintsForSelectedVillage() {
        return VillageDataStore.getComplaints(currentVillage());
    }

    /* ============================================================
     *  SARPANCH RESOLUTION (village-scoped, dynamic - no hardcoding)
     * ============================================================ */

    /**
     * The current village's Sarpanch, looked up fresh every call so it
     * always reflects whatever village is currently selected. Never a
     * fixed name - falls back to {@link #SARPANCH_UNASSIGNED} only when
     * the store genuinely has nothing on record for this village.
     */
    private String currentSarpanchName() {
        String name = VillageDataStore.getSarpanchName(currentVillage());
        return (name == null || name.trim().isEmpty()) ? SARPANCH_UNASSIGNED : name;
    }

    /* ============================================================
<<<<<<< HEAD
     *  SARPANCH-TARGETED COMPLAINTS (VILLAGE-SCOPED, NOT PROJECT-SCOPED)
     * ============================================================
     * ASSUMPTION: Complaint.getTargetPerson() / getComplainantRole() /
     * getCitizenName() - guessed at the most natural names. Update
     * them once you confirm the real Complaint API.
     */

    /**
     * Complaints for the currently selected village that are targeted
     * at that village's Sarpanch, excluding any complaint the Sarpanch
     * filed themselves. Fully village-driven: switching villages in
     * the sidebar changes both which complaints are pulled AND which
     * Sarpanch name they're matched against - there is no project
     * filtering anywhere in this method.
     */
    private List<Complaint> getSarpanchComplaintsForSelectedVillage() {
        String sarpanchName = currentSarpanchName();
        if (SARPANCH_UNASSIGNED.equals(sarpanchName)) {
            return List.of();
        }

        return getComplaintsForSelectedVillage().stream()
                .filter(c -> isTargetedAtSarpanch(c, sarpanchName))
                .filter(c -> !isFiledBySarpanch(c, sarpanchName))
                .collect(Collectors.toList());
    }

    /**
     * True when a complaint is targeted at this village's Sarpanch.
     * Matches on the Sarpanch's actual name first (so this stays
     * correct per-village); also accepts a generic {@code "Sarpanch"}
     * role tag as a fallback, in case your Complaint data marks the
     * target by role rather than by name.
     *
     * ASSUMPTION: Complaint.getTargetPerson() holds who the complaint
     * is against.
     */
    private boolean isTargetedAtSarpanch(Complaint c, String sarpanchName) {
        String target = c.getTargetPerson();
        if (target == null) return false;
        String trimmed = target.trim();
        return sarpanchName.equalsIgnoreCase(trimmed) || SARPANCH_ROLE.equalsIgnoreCase(trimmed);
    }

    /**
     * True when a complaint was filed BY this village's Sarpanch (and
     * so should be excluded from the "against the Sarpanch" list).
     * Matches on the filer's name first, with the same role-tag
     * fallback as {@link #isTargetedAtSarpanch(Complaint, String)}.
     *
     * ASSUMPTION: Complaint.getCitizenName() holds the filer's name,
     * Complaint.getComplainantRole() holds the filer's role.
     */
    private boolean isFiledBySarpanch(Complaint c, String sarpanchName) {
        String filerName = c.getCitizenName();
        if (filerName != null && sarpanchName.equalsIgnoreCase(filerName.trim())) {
            return true;
        }
        String role = c.getComplainantRole();
        return role != null && SARPANCH_ROLE.equalsIgnoreCase(role.trim());
    }

    /** "Anonymous" whenever the complainant name is missing/blank. */
    private String complainantDisplayName(Complaint c) {
        String name = c.getCitizenName();
        return (name == null || name.trim().isEmpty()) ? "Anonymous" : name;
=======
     *  OFFICIAL COMPLAINTS AGAINST THE SARPANCH (VILLAGE-SCOPED)
     * ============================================================
     * Sourced from OfficialComplaintStore - the future data contract
     * between the Villager Login's NewComplaintPage ("Official
     * Complaint" type) and this page. See OfficialComplaint /
     * OfficialComplaintStore for the field-for-field mapping and the
     * TODOs marking exactly where a real submission pipeline plugs in.
     */

    /**
     * Official Complaints for the currently selected village. "All
     * Villages" shows nothing here (same empty-state behaviour as
     * before) since this panel is specifically about a single
     * village's Sarpanch.
     */
    private List<OfficialComplaint> getOfficialComplaintsForSelectedVillage() {
        return isAllVillages() ? OfficialComplaintStore.getAll() : OfficialComplaintStore.getForVillage(currentVillage());
>>>>>>> c93dffcf5bbb024bbc0e0d7fbbe47b72c71c37a2
    }

    /**
     * Called whenever the selected village changes. Updates every
     * dynamic component on the page in place - no scene rebuild, no
     * UI redesign.
     */
    private void refreshComplaintManagement() {
        updateScopeSubtitle();
        updateSarpanchInfo();
        updateStatCards();
        updateSarpanchComplaintPanel();
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
            Dashboard.myStage.setScene(page.getBDODashboardScene() );
        };
        dashboardNav.setOnMouseClicked(e -> toDashboard.run());

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

        HBox complaintNav = navItem("\u26A0", "Complaint Management", true);   // <-- active on this page

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
     *  VILLAGE SWITCHER — lets the BDO toggle scope between
     *  "All Villages" and any single village under their block.
     *  Reads/writes the shared VillageDataStore.VILLAGES /
     *  VillageDataStore.selectedVillage state instead of a
     *  page-local copy. Unchanged from the original page.
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

        villageNameLabel = new Label(currentVillage());
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
        for (String village : VillageDataStore.VILLAGES) {
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
        btn.setSelected(village.equals(currentVillage()));
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
                // Write through to the shared, page-independent selection state.
                VillageDataStore.selectedVillage = village;
                villageNameLabel.setText(currentVillage());

                // Collapse the list once a village has been chosen.
                villageListExpanded = false;
                villageListBox.setVisible(false);
                villageListBox.setManaged(false);
                villageChevron.setText("\u25BE");

                // Immediate, in-place refresh of every dynamic component -
                // KPI cards, Sarpanch info line, and the Sarpanch complaint
                // table all pick up the newly selected village here.
                refreshComplaintManagement();
            }
        });

        return btn;
    }

    /** Keeps the page subtitle in sync with whichever village is currently selected. */
    private void updateScopeSubtitle() {
        if (scopeSubtitle == null) return;
        scopeSubtitle.setText(isAllVillages() ? "All Villages" : currentVillage());
    }

    /**
     * Keeps the Sarpanch info line in sync with whichever village is
     * currently selected - always re-reads through
     * {@link #currentSarpanchName()}, never cached, never hardcoded.
     */
    private void updateSarpanchInfo() {
        if (sarpanchNameLabel == null) return;
        if (isAllVillages()) {
            sarpanchNameLabel.setText("Select a village to view its Sarpanch");
        } else {
            sarpanchNameLabel.setText(currentSarpanchName());
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
     *  TOP NAVIGATION BAR — shared shell, unchanged
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
        javafx.scene.control.TextField topSearchField = new javafx.scene.control.TextField();
        topSearchField.setPromptText("Search complaints, villages, or categories...");
        topSearchField.setStyle("-fx-background-color: transparent; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 14px; -fx-text-fill: " + FOREST_DEEP + "; -fx-prompt-text-fill: rgba(11,61,46,0.40);");
        HBox.setHgrow(topSearchField, Priority.ALWAYS);
        searchBox.getChildren().addAll(searchIcon, topSearchField);

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

    /* ============================================================
     *  MAIN CONTENT
     *  Title row (with the Sarpanch info line), KPI cards, and the
     *  Sarpanch complaint panel. No project-related content anywhere.
     * ============================================================ */
    private VBox buildMainContent() {
        VBox main = new VBox(24);
        main.setPadding(new Insets(32, 40, 48, 40));
        main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");

        main.getChildren().addAll(
                buildTitleRow(),
                buildStatCardsRow(),
                buildSarpanchComplaintPanel()
        );
        return main;
    }

    private HBox buildTitleRow() {
        VBox text = new VBox(6);
        Label title = new Label("Complaints Against Sarpanch");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        HBox subtitleRow = new HBox(4);
        Label subtitle = new Label("Managing complaints for:");
        subtitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.60);");
        scopeSubtitle = new Label();
        scopeSubtitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        subtitleRow.getChildren().addAll(subtitle, scopeSubtitle);

        // Sarpanch info line - the core "Sarpanch-centric" element. Always
        // re-populated by updateSarpanchInfo(), never set once and forgotten.
        HBox sarpanchRow = new HBox(6);
        sarpanchRow.setAlignment(Pos.CENTER_LEFT);
        Label sarpanchIcon = new Label("\uD83E\uDDD1\u200D\uD83D\uDCBC");
        sarpanchIcon.setStyle("-fx-font-size: 13px;");
        Label sarpanchCaption = new Label("Sarpanch:");
        sarpanchCaption.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.60);");
        sarpanchNameLabel = new Label();
        sarpanchNameLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + SAFFRON_MAIN + ";");
        sarpanchRow.getChildren().addAll(sarpanchIcon, sarpanchCaption, sarpanchNameLabel);

        text.getChildren().addAll(title, subtitleRow, sarpanchRow);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerRow = new HBox(20, text, spacer, actions);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        return headerRow;
    }

    /* ============================================================
     *  STAT CARDS ROW (unchanged design/behaviour from the original
     *  page - still describes the currently selected village)
     * ============================================================ */
    private HBox buildStatCardsRow() {
        HBox row = new HBox(20);

        VBox totalCard = kpiCard(FOREST_LIGHT, "\u26A0", "TOTAL COMPLAINTS");
        totalValueLabel = statValueLabel(totalCard);

        VBox resolvedCard = kpiCard(SAFFRON_MAIN, "\u2705", "RESOLVED");
        resolvedValueLabel = statValueLabel(resolvedCard);
        resolvedFooterLabel = new Label();
        resolvedFooterLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + CONTEXT_TEAL + ";");
        resolvedCard.getChildren().add(resolvedFooterLabel);

        VBox progressCard = kpiCard(CONTEXT_TEAL, "\u25B6", "IN PROGRESS");
        progressValueLabel = statValueLabel(progressCard);
        progressFooterLabel = new Label();
        progressFooterLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.60);");
        progressCard.getChildren().add(progressFooterLabel);

        VBox pendingCard = kpiCard(AI_VIOLET, "\uD83D\uDCCB", "PENDING COMPLAINTS");
        pendingValueLabel = statValueLabel(pendingCard);

        HBox.setHgrow(totalCard, Priority.ALWAYS);
        HBox.setHgrow(resolvedCard, Priority.ALWAYS);
        HBox.setHgrow(progressCard, Priority.ALWAYS);
        HBox.setHgrow(pendingCard, Priority.ALWAYS);
        row.getChildren().addAll(totalCard, resolvedCard, progressCard, pendingCard);
        return row;
    }

    /** Pulls the stat-value Label back out of a card built by kpiCard(), so it can be kept live. */
    private Label statValueLabel(VBox card) {
        VBox inner = (VBox) card.getChildren().get(1);
        VBox bottom = (VBox) inner.getChildren().get(2);
        return (Label) bottom.getChildren().get(0);
    }

    /**
     * Recomputes and writes every KPI card value from
     * {@link #getComplaintsForSelectedVillage()} - unchanged from the
     * original page, still village-scoped, never project-scoped.
     */
    private void updateStatCards() {
        List<Complaint> complaints = getComplaintsForSelectedVillage();
        int total = complaints.size();

        long resolved = complaints.stream().filter(c -> c.getStatus() == Complaint.Status.RESOLVED).count();
        long inProgress = complaints.stream().filter(c -> c.getStatus() == Complaint.Status.IN_PROGRESS).count();
        long pending = complaints.stream().filter(c -> c.getStatus() == Complaint.Status.PENDING).count();
<<<<<<< HEAD
        long highPriority = complaints.stream().filter(c -> c.getPriority() == Complaint.Priority.HIGH).count();
        long critical = complaints.stream().filter(c -> c.getPriority() == Complaint.Priority.CRITICAL).count();

        totalValueLabel.setText(String.valueOf(total));
        // FIX: was concatenating a boolean into the footer text
        // ("trueSitapur" / "falseSitapur"). Now shows a proper scope label.
        totalFooterLabel.setText(isAllVillages() ? "All Villages" : "Village: " + currentVillage());
=======

        totalValueLabel.setText(String.valueOf(total));
>>>>>>> c93dffcf5bbb024bbc0e0d7fbbe47b72c71c37a2

        resolvedValueLabel.setText(String.valueOf(resolved));

        progressValueLabel.setText(String.valueOf(inProgress));

        pendingValueLabel.setText(String.valueOf(pending));
    }

    private VBox kpiCard(String accent, String icon, String labelText) {
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
        Label stat = new Label();
        stat.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 40px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        bottom.getChildren().add(stat);

        inner.getChildren().addAll(head, grow, bottom);
        card.getChildren().addAll(strip, inner);
        addHoverLift(card, 16);
        return card;
    }

    /* ============================================================
     *  SARPANCH COMPLAINT PANEL (village-scoped, NOT project-scoped)
<<<<<<< HEAD
     *  This is the only complaint list rendered on the page. No
     *  PROJECT column - project data is not shown anywhere here.
=======
     *  This is the only complaint list rendered on the page. Shows
     *  ONLY the Official Complaints filed against the Sarpanch via
     *  the Villager Login's NewComplaintPage - Title + Location per
     *  row, with a "View Complaint" action opening the full review
     *  page (SarpanchComplaintDetailsPage), where an officer can see
     *  the Official's Name, Designation, Description, and mark the
     *  complaint Resolved or Rejected.
>>>>>>> c93dffcf5bbb024bbc0e0d7fbbe47b72c71c37a2
     * ============================================================ */
    private VBox buildSarpanchComplaintPanel() {
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Complaints Against Sarpanch \u2014 Selected Village");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
<<<<<<< HEAD
        header.getChildren().addAll(title);

        sarpanchComplaintGrid = new GridPane();
        sarpanchComplaintGrid.setHgap(12);
        sarpanchComplaintGrid.setVgap(16);
        sarpanchComplaintGrid.setPadding(new Insets(6, 0, 0, 0));
        sarpanchComplaintGrid.getColumnConstraints().addAll(
                pct(11), pct(15), pct(10), pct(12), pct(28), pct(10), pct(14));
=======
        Label subtitle = new Label("Official Complaints filed via the Villager Login");
        subtitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.55);");
        VBox titleBox = new VBox(2, title, subtitle);
        header.getChildren().addAll(titleBox);

        sarpanchComplaintList = new VBox(12);
        sarpanchComplaintList.setPadding(new Insets(6, 0, 0, 0));
>>>>>>> c93dffcf5bbb024bbc0e0d7fbbe47b72c71c37a2

        sarpanchComplaintEmptyLabel = new Label();
        sarpanchComplaintEmptyLabel.setWrapText(true);
        sarpanchComplaintEmptyLabel.setPadding(new Insets(16, 8, 16, 8));
        sarpanchComplaintEmptyLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.55);");
        sarpanchComplaintEmptyLabel.setManaged(false);
        sarpanchComplaintEmptyLabel.setVisible(false);

<<<<<<< HEAD
        panel.getChildren().addAll(header, sarpanchComplaintGrid, sarpanchComplaintEmptyLabel);
=======
        panel.getChildren().addAll(header, sarpanchComplaintList, sarpanchComplaintEmptyLabel);
>>>>>>> c93dffcf5bbb024bbc0e0d7fbbe47b72c71c37a2
        addHoverLift(panel, 24);
        return panel;
    }

<<<<<<< HEAD
    private void addSarpanchComplaintHeader(GridPane grid) {
        grid.add(headerCell("COMPLAINT ID"), 0, 0);
        grid.add(headerCell("COMPLAINANT"), 1, 0);
        grid.add(headerCell("DATE FILED"), 2, 0);
        grid.add(headerCell("CATEGORY"), 3, 0);
        grid.add(headerCell("DESCRIPTION"), 4, 0);
        grid.add(headerCell("PRIORITY"), 5, 0);
        grid.add(headerCell("STATUS"), 6, 0);
    }

    /**
     * Rebuilds the Sarpanch-targeted panel from
     * {@link #getSarpanchComplaintsForSelectedVillage()}. Shows the
     * required empty-state message when nothing matches (including
     * when the selected village has no Sarpanch on record, or when
     * "All Villages" is selected). Every complaint shown here already
     * satisfies both conditions: targeted at the current village's
     * Sarpanch AND not filed by that Sarpanch.
     */
    private void updateSarpanchComplaintPanel() {
        sarpanchComplaintGrid.getChildren().clear();
        addSarpanchComplaintHeader(sarpanchComplaintGrid);

        List<Complaint> complaints = getSarpanchComplaintsForSelectedVillage();

        if (complaints.isEmpty()) {
            sarpanchComplaintGrid.setManaged(false);
            sarpanchComplaintGrid.setVisible(false);
            sarpanchComplaintEmptyLabel.setManaged(true);
            sarpanchComplaintEmptyLabel.setVisible(true);
            sarpanchComplaintEmptyLabel.setText(isAllVillages()
                    ? "Select a specific village to view complaints against its Sarpanch."
                    : "No complaints have been reported against the Sarpanch for " + currentVillage() + ".");
            return;
        }

        sarpanchComplaintGrid.setManaged(true);
        sarpanchComplaintGrid.setVisible(true);
        sarpanchComplaintEmptyLabel.setManaged(false);
        sarpanchComplaintEmptyLabel.setVisible(false);

        int row = 1;
        for (Complaint c : complaints) {
            addSarpanchComplaintRow(sarpanchComplaintGrid, row, c);
            row++;
        }
    }

    private void addSarpanchComplaintRow(GridPane grid, int row, Complaint c) {
        String statusText = displayStatus(c.getStatus());
        String statusColor = statusColor(c.getStatus());

        Label idLabel = new Label(c.getComplaintId());
        idLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");

        Label nameLabel = new Label(complainantDisplayName(c));
        nameLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.80);");

        Label dateLabel = new Label(c.getDateFiled());
        dateLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");

        Label categoryLabel = new Label(c.getCategory());
        categoryLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");

        Label descLabel = new Label(c.getDescription());
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.80);");

        Label priorityLabel = new Label(c.getPriority().toString());
        priorityLabel.setPadding(new Insets(4, 10, 4, 10));
        priorityLabel.setMaxWidth(Region.USE_PREF_SIZE);
        priorityLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + DELAYED_RED + "; -fx-background-color: " + rgba(DELAYED_RED, 0.12) + "; -fx-background-radius: 999;");

        Label statusLabel = new Label(statusText);
        statusLabel.setPadding(new Insets(4, 10, 4, 10));
        statusLabel.setMaxWidth(Region.USE_PREF_SIZE);
        statusLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + statusColor + "; -fx-background-color: " + rgba(statusColor, 0.12) + "; -fx-background-radius: 999;");

        grid.add(idLabel, 0, row);
        grid.add(nameLabel, 1, row);
        grid.add(dateLabel, 2, row);
        grid.add(categoryLabel, 3, row);
        grid.add(descLabel, 4, row);
        grid.add(priorityLabel, 5, row);
        grid.add(statusLabel, 6, row);
    }

    private String displayStatus(Complaint.Status status) {
=======
    /**
     * Rebuilds the Sarpanch panel from
     * {@link #getOfficialComplaintsForSelectedVillage()}. Shows the
     * required empty-state message when nothing matches (including
     * when "All Villages" is selected).
     */
    private void updateSarpanchComplaintPanel() {
        sarpanchComplaintList.getChildren().clear();

        List<OfficialComplaint> complaints = getOfficialComplaintsForSelectedVillage();

        if (complaints.isEmpty()) {
            sarpanchComplaintList.setManaged(false);
            sarpanchComplaintList.setVisible(false);
            sarpanchComplaintEmptyLabel.setManaged(true);
            sarpanchComplaintEmptyLabel.setVisible(true);
            sarpanchComplaintEmptyLabel.setText(isAllVillages()
                    ? "No Official Complaints have been reported against any Sarpanch yet."
                    : "No Official Complaints have been reported against the Sarpanch for " + currentVillage() + ".");
            return;
        }

        sarpanchComplaintList.setManaged(true);
        sarpanchComplaintList.setVisible(true);
        sarpanchComplaintEmptyLabel.setManaged(false);
        sarpanchComplaintEmptyLabel.setVisible(false);

        for (OfficialComplaint c : complaints) {
            sarpanchComplaintList.getChildren().add(buildOfficialComplaintRow(c));
        }
    }

    /**
     * One row: category icon, Title + Location, a status pill, and a
     * "View Complaint" button that opens SarpanchComplaintDetailsPage
     * for this specific complaint.
     */
    private HBox buildOfficialComplaintRow(OfficialComplaint c) {
        StackPane iconCircle = new StackPane();
        iconCircle.setPrefSize(42, 42);
        iconCircle.setMinSize(42, 42);
        iconCircle.setStyle("-fx-background-color: " + rgba(SAFFRON_MAIN, 0.14) + "; -fx-background-radius: 21;");
        Label icon = new Label("\u26A0");
        icon.setStyle("-fx-font-size: 16px; -fx-text-fill: " + SAFFRON_MAIN + ";");
        iconCircle.getChildren().add(icon);

        Label titleLabel = new Label(c.getTitle());
        titleLabel.setWrapText(true);
        titleLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14.5px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");

        Label locationLabel = new Label("\uD83D\uDCCD  " + c.getLocation());
        locationLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-text-fill: rgba(11,61,46,0.65);");

        VBox textBox = new VBox(4, titleLabel, locationLabel);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Label statusPill = new Label(officialStatusText(c.getStatus()));
        statusPill.setPadding(new Insets(4, 10, 4, 10));
        statusPill.setMaxWidth(Region.USE_PREF_SIZE);
        String statusColor = officialStatusColor(c.getStatus());
        statusPill.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + statusColor + "; -fx-background-color: " + rgba(statusColor, 0.14) + "; -fx-background-radius: 999;");

        Button viewBtn = new Button("View Complaint  \u2192");
        String viewBase = "-fx-background-color: " + FOREST_DEEP + "; -fx-text-fill: white;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800;" +
                "-fx-background-radius: 9; -fx-padding: 9 16 9 16; -fx-cursor: hand;";
        String viewHover = "-fx-background-color: " + FOREST_LIGHT + "; -fx-text-fill: white;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800;" +
                "-fx-background-radius: 9; -fx-padding: 9 16 9 16; -fx-cursor: hand;";
        viewBtn.setStyle(viewBase);
        viewBtn.setOnMouseEntered(e -> viewBtn.setStyle(viewHover));
        viewBtn.setOnMouseExited(e -> viewBtn.setStyle(viewBase));
        viewBtn.setOnAction(e -> openComplaintDetails(c));

        HBox row = new HBox(16, iconCircle, textBox, statusPill, viewBtn);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16, 18, 16, 18));
        row.setStyle(
                "-fx-background-color: rgba(255,255,255,0.85);" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: rgba(11,61,46,0.08);" +
                "-fx-border-radius: 14;" +
                "-fx-border-width: 1;"
        );
        return row;
    }

    /**
     * Opens SarpanchComplaintDetailsPage for the given complaint. The
     * "back" action rebuilds this page fresh so any status change made
     * on the review page (Resolved / Rejected) is reflected here.
     */
    private void openComplaintDetails(OfficialComplaint c) {
        Runnable backToManagement = () -> {
            ComplaintManagement page = new ComplaintManagement();
            Dashboard.myStage.setScene(page.getComplaintManagementScene());
        };
        SarpanchComplaintDetailsPage detailsPage = new SarpanchComplaintDetailsPage();
        Dashboard.myStage.setScene(detailsPage.getComplaintDetailsScene(c, backToManagement));
    }

    private String officialStatusText(OfficialComplaint.Status status) {
>>>>>>> c93dffcf5bbb024bbc0e0d7fbbe47b72c71c37a2
        switch (status) {
            case RESOLVED: return "Resolved";
            case REJECTED: return "Rejected";
            default: return "Pending";
        }
    }

    private String officialStatusColor(OfficialComplaint.Status status) {
        switch (status) {
            case RESOLVED: return CONTEXT_TEAL;
            case REJECTED: return "#6B7B74";
            default: return SAFFRON_MAIN;
        }
    }

<<<<<<< HEAD
    private Label headerCell(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.60); -fx-letter-spacing: 0.06em;");
        return label;
    }

    private ColumnConstraints pct(double width) {
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(width);
        return cc;
    }

=======
>>>>>>> c93dffcf5bbb024bbc0e0d7fbbe47b72c71c37a2
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