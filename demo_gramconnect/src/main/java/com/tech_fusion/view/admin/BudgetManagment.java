package com.tech_fusion.view.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import java.io.File;

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
 * GramConnect - Budget Management Page
 * ------------------------------------------------------------------
 * Same glass / forest-saffron template as {@link Dashboard}. Navigates
 * through the shared {@code Dashboard.myStage} using Runnable callbacks.
 * (Filename/class name keep the original "BudgetManagment" spelling so
 * this drops straight into the existing project without breaking any
 * other references to it.)
 *
 * The "Managing Village" switcher lives in the sidebar (same as
 * {@link Dashboard}) so the BDO can toggle which village under their
 * block this budget view is currently scoped to ("All Villages" or
 * any single village).
 *
 * ------------------------------------------------------------------
 * VILLAGE-WISE DYNAMIC DATA
 * ------------------------------------------------------------------
 * This page now reads/writes {@link Dashboard#selectedVillage} and
 * {@link Dashboard#VILLAGES} directly (previously it kept its own
 * separate, instance-level copy of both — meaning the village picked
 * on the Dashboard page did NOT carry over here, and vice versa).
 * That's fixed: every page in the app now shares one authoritative
 * selection, per the original "single source of truth" requirement.
 *
 * Every KPI card, the Village-wise Utilization Overview, and the
 * Village Budget Inventory table are all computed from
 * {@link #allBudgets} via {@link #getSelectedVillageBudgets()} — never
 * hard-coded. Selecting a village in the sidebar calls
 * {@link #refreshBudgetView()} immediately (from
 * {@link #villageToggle(String, ToggleGroup)}), which rebuilds those
 * three sections in place — no page reload required.
 *
 * FIREBASE READINESS: {@link #loadSampleData()} is the single seam
 * where the local sample list is populated. Swap its body for a
 * Firestore fetch that populates {@code allBudgets} with the same
 * shape and nothing else in this class needs to change.
 */
public class BudgetManagment extends Application {

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

    private Label selectedNavItem;

    private Label villageNameLabel;
    private Label villageChevron;
    private VBox villageListBox;
    private Label scopeSubtitleStrong;
    private boolean villageListExpanded = false;

    /* ============================================================
     *  DATA MODEL — village-wise, Firebase-ready
     * ============================================================ */

    /** One village's budget record. {@code village} must match a Dashboard.VILLAGES entry. */
    private static class BudgetRecord {
        String village;
        double totalAllocatedCr;   // total block-budget share earmarked for this village
        double sanctionedCr;       // portion of the allocation formally sanctioned
        double releasedCr;         // portion of the sanctioned amount actually disbursed
        String lastDisbursedDate;

        BudgetRecord(String village, double totalAllocatedCr, double sanctionedCr, double releasedCr, String lastDisbursedDate) {
            this.village = village;
            this.totalAllocatedCr = totalAllocatedCr;
            this.sanctionedCr = sanctionedCr;
            this.releasedCr = releasedCr;
            this.lastDisbursedDate = lastDisbursedDate;
        }
    }

    /** Central, Firebase-ready budget list. Populated once by {@link #loadSampleData()}. */
    private List<BudgetRecord> allBudgets;

    /* ---- References kept so refreshBudgetView() can rebuild sections in place ---- */
    private VBox mainContentBox;
    private HBox statCardsRowRef;
    private VBox utilizationSectionRef;
    private VBox inventoryPanelRef;

    @Override
    public void start(Stage stage) {
        loadSampleData();
        Dashboard.myStage = stage;
        stage.setTitle("GramConnect - Budget Management");
        stage.setScene(getBudgetManagmentScene());
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.show();
    }

    /** Builds the Budget Management scene. Public so other pages can navigate here. */
    public Scene getBudgetManagmentScene() {
        if (allBudgets == null) {
            // Guard for callers that construct this page directly (sidebar navigation)
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
     *  Every other method reads through allBudgets, so nothing else
     *  needs to change.
     * ============================================================ */
    private void loadSampleData() {
        allBudgets = new ArrayList<>(Arrays.asList(
                new BudgetRecord("Rampur",         5.0, 3.6, 2.5, "12 Oct 2023"),
                new BudgetRecord("Sitapur",        4.0, 3.0, 2.0, "05 Sep 2023"),
                new BudgetRecord("Madhavpur",      3.8, 2.9, 2.4, "28 Aug 2023"),
                new BudgetRecord("Ward 4 Cluster", 4.0, 3.0, 1.5, "15 Jul 2023")
        ));
    }

    /* ============================================================
     *  VILLAGE FILTERING — single source of truth for all sections
     * ============================================================ */

    /** Budget records for the currently selected village, or all of them if "All Villages" is selected. */
    private List<BudgetRecord> getSelectedVillageBudgets() {
        if (Dashboard.VILLAGES.get(0).equals(Dashboard.selectedVillage)) {
            return allBudgets;
        }
        return allBudgets.stream()
                .filter(b -> b.village.equals(Dashboard.selectedVillage))
                .collect(Collectors.toList());
    }

    private double sumTotalAllocated(List<BudgetRecord> records) {
        return records.stream().mapToDouble(b -> b.totalAllocatedCr).sum();
    }

    private double sumSanctioned(List<BudgetRecord> records) {
        return records.stream().mapToDouble(b -> b.sanctionedCr).sum();
    }

    private double sumReleased(List<BudgetRecord> records) {
        return records.stream().mapToDouble(b -> b.releasedCr).sum();
    }

    /** Color-codes a utilization row by how much of the sanctioned amount has actually been released. */
    private String colorForUtilization(double releasedFraction) {
        if (releasedFraction >= 0.90) return DELAYED_RED;
        if (releasedFraction >= 0.70) return SAFFRON_MAIN;
        if (releasedFraction >= 0.50) return CONTEXT_TEAL;
        return FOREST_DEEP;
    }

    /* ============================================================
     *  REFRESH — called immediately after Dashboard.selectedVillage
     *  changes, so the whole page updates without a reload.
     * ============================================================ */
    private void refreshBudgetView() {
        updateScopeSubtitle();

        if (mainContentBox == null) return;

        HBox newStatRow = buildStatCardsRow();
        int statIdx = mainContentBox.getChildren().indexOf(statCardsRowRef);
        if (statIdx >= 0) mainContentBox.getChildren().set(statIdx, newStatRow);
        statCardsRowRef = newStatRow;

        VBox newUtilization = buildUtilizationSection();
        int utilIdx = mainContentBox.getChildren().indexOf(utilizationSectionRef);
        if (utilIdx >= 0) mainContentBox.getChildren().set(utilIdx, newUtilization);
        utilizationSectionRef = newUtilization;

        VBox newInventory = buildInventoryPanel();
        int invIdx = mainContentBox.getChildren().indexOf(inventoryPanelRef);
        if (invIdx >= 0) mainContentBox.getChildren().set(invIdx, newInventory);
        inventoryPanelRef = newInventory;
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

        HBox budgetNav = navItem("\uD83D\uDCB0", "Budget Management", true);   // <-- active on this page

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
     *  VILLAGE SWITCHER — lets the BDO toggle scope between
     *  "All Villages" and any single village under their block.
     *  Reads/writes the static Dashboard.selectedVillage so the
     *  choice is shared with every other page (Dashboard,
     *  ProjectManagement, etc), not just kept locally here.
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
                updateScopeSubtitle();
                // Immediately refresh every dynamic section on this page — no reload required.
                refreshBudgetView();
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
        searchField.setPromptText("Search villages, projects, or budgets...");
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

    /* ============================================================
     *  MAIN CONTENT
     * ============================================================ */
    private VBox buildMainContent() {
        VBox main = new VBox(24);
        main.setPadding(new Insets(32, 40, 48, 40));
        main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");

        statCardsRowRef = buildStatCardsRow();
        utilizationSectionRef = buildUtilizationSection();
        inventoryPanelRef = buildInventoryPanel();

        main.getChildren().addAll(
                buildTitleRow(),
                statCardsRowRef,
                utilizationSectionRef,
                inventoryPanelRef
        );

        mainContentBox = main;
        return main;
    }

    private HBox buildTitleRow() {
        VBox text = new VBox(6);
        Label title = new Label("Budget Management");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        HBox subtitleRow = new HBox(4);
        Label subtitle = new Label("Showing data for:");
        subtitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.60);");
        scopeSubtitleStrong = new Label("All Villages (Block)");
        scopeSubtitleStrong.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        updateScopeSubtitle();
        subtitleRow.getChildren().addAll(subtitle, scopeSubtitleStrong);
        text.getChildren().addAll(title, subtitleRow);


        Label allocate = new Label("+  Allocate Funds");
        allocate.setPadding(new Insets(12, 18, 12, 18));
        allocate.setStyle("-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
                "-fx-text-fill: white; -fx-background-radius: 10; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px; -fx-font-weight: 700; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.35), 10, 0.1, 0, 4);");

        HBox actions = new HBox(12, allocate);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerRow = new HBox(20, text, spacer, actions);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        return headerRow;
    }

    /* ============================================================
     *  STAT CARDS ROW — every value now computed from the
     *  village-filtered budget records, never hard-coded.
     * ============================================================ */
    private HBox buildStatCardsRow() {
        HBox row = new HBox(20);

        List<BudgetRecord> scope = getSelectedVillageBudgets();
        double totalAllocated = sumTotalAllocated(scope);
        double sanctioned = sumSanctioned(scope);
        double released = sumReleased(scope);
        double pending = totalAllocated - sanctioned;

        int sanctionedOfTotalPct = totalAllocated > 0 ? (int) Math.round((sanctioned / totalAllocated) * 100) : 0;
        int releasedOfSanctionedPct = sanctioned > 0 ? (int) Math.round((released / sanctioned) * 100) : 0;
        int sanctionedShareOfTotalPct = totalAllocated > 0 ? (int) Math.round((sanctioned / totalAllocated) * 100) : 0;

        VBox totalCard = kpiCard(FOREST_LIGHT, "\uD83C\uDFDB", "TOTAL BLOCK BUDGET", String.format("\u20B9%.1f Cr", totalAllocated));
        VBox utilRow = new VBox(6);
        HBox utilLabels = new HBox();
        utilLabels.setAlignment(Pos.CENTER_LEFT);
        // Label utilLeft = new Label(sanctionedOfTotalPct + "% Sanctioned");
        // utilLeft.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.60);");
        Region utilSpacer = new Region();
        HBox.setHgrow(utilSpacer, Priority.ALWAYS);
        utilLabels.getChildren().addAll( utilSpacer);
        // utilRow.getChildren().addAll(utilLabels, progressBar(sanctionedOfTotalPct / 100.0, FOREST_DEEP, 8));
        totalCard.getChildren().add(utilRow);

        VBox sanctionedCard = kpiCard(SAFFRON_MAIN, "\u2705", "SANCTIONED AMOUNT", String.format("\u20B9%.1f Cr", sanctioned));
        // Label sanctionedFooter = new Label(sanctionedShareOfTotalPct + "% of total block budget");
        // sanctionedFooter.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + CONTEXT_TEAL + ";");
        // sanctionedCard.getChildren().add(sanctionedFooter);

        VBox releasedCard = kpiCard(CONTEXT_TEAL, "\u20B9", "RELEASED FUNDS", String.format("\u20B9%.1f Cr", released));
        // Label releasedFooter = new Label(releasedOfSanctionedPct + "% of sanctioned amount");
        // releasedFooter.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.60);");
        // releasedCard.getChildren().add(releasedFooter);

        VBox pendingCard = kpiCard(AI_VIOLET, "\uD83D\uDCCB", "PENDING ALLOCATIONS", String.format("\u20B9%.1f Cr", pending));
        // Label pendingFooter = new Label(pending > 0 ? "Requires action this month" : "Fully allocated");
        // pendingFooter.setPadding(new Insets(6, 10, 6, 10));
        // pendingFooter.setMaxWidth(Region.USE_PREF_SIZE);
        // pendingFooter.setStyle("-fx-background-color: rgba(224,122,31,0.14); -fx-background-radius: 6;" +
        //         "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 700; -fx-text-fill: " + SAFFRON_MAIN + ";");
        // pendingCard.getChildren().add(pendingFooter);

        HBox.setHgrow(totalCard, Priority.ALWAYS);
        HBox.setHgrow(sanctionedCard, Priority.ALWAYS);
        HBox.setHgrow(releasedCard, Priority.ALWAYS);
        HBox.setHgrow(pendingCard, Priority.ALWAYS);
        row.getChildren().addAll(totalCard, sanctionedCard, releasedCard, pendingCard);
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
     *  VILLAGE-WISE UTILIZATION OVERVIEW — one row per village in
     *  scope (all four for "All Villages", just one otherwise).
     * ============================================================ */
    private VBox buildUtilizationSection() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Village-wise Utilization Overview");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label viewMap = new Label("View Map Mode");
        viewMap.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-font-weight: 700; -fx-cursor: hand;");
        header.getChildren().addAll(title, spacer, viewMap);

        panel.getChildren().add(header);

        for (BudgetRecord b : getSelectedVillageBudgets()) {
            double fraction = b.sanctionedCr > 0 ? (b.releasedCr / b.sanctionedCr) : 0;
            String rightText = String.format("\u20B9%.1fCr / \u20B9%.1fCr", b.releasedCr, b.sanctionedCr);
            panel.getChildren().add(utilizationRow(b.village, fraction, rightText, colorForUtilization(fraction)));
        }

        addHoverLift(panel, 24);
        return panel;
    }

    private VBox utilizationRow(String name, double fillRatio, String rightText, String barColor) {
        VBox row = new VBox(8);

        HBox labelRow = new HBox(16);
        labelRow.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label amountLabel = new Label(rightText);
        amountLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        labelRow.getChildren().addAll(nameLabel, spacer, amountLabel);

        StackPane bar = progressBar(fillRatio, barColor, 12);

        row.getChildren().addAll(labelRow, bar);
        return row;
    }

    /* ============================================================
     *  VILLAGE BUDGET INVENTORY TABLE — filtered to the selected
     *  village (or all four for "All Villages").
     * ============================================================ */
    private VBox buildInventoryPanel() {
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Village Budget Inventory");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        TextField filter = new TextField();
        filter.setPromptText("Filter villages...");
        filter.setPrefWidth(240);
        filter.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 12; -fx-border-radius: 12;" +
                "-fx-border-color: rgba(11,61,46,0.10); -fx-padding: 10 14; -fx-font-size: 13px;");
        header.getChildren().addAll(title, spacer, filter);

        List<BudgetRecord> scope = getSelectedVillageBudgets();

        if (scope.isEmpty()) {
            Label empty = new Label("No budget records for " + Dashboard.selectedVillage + ".");
            empty.setPadding(new Insets(20, 8, 4, 8));
            empty.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.55);");
            panel.getChildren().addAll(header, empty);
            addHoverLift(panel, 24);
            return panel;
        }

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(16);
        grid.setPadding(new Insets(6, 0, 0, 0));
        grid.getColumnConstraints().addAll(pct(28), pct(16), pct(16), pct(12), pct(18), pct(10));

        addInventoryHeader(grid);

        int row = 1;
        for (BudgetRecord b : scope) {
            double utilFraction = b.sanctionedCr > 0 ? (b.releasedCr / b.sanctionedCr) : 0;
            int utilPct = (int) Math.round(utilFraction * 100);
            addInventoryRow(grid, row,
                    b.village,
                    String.format("\u20B9%.2f Cr", b.sanctionedCr),
                    String.format("\u20B9%.2f Cr", b.releasedCr),
                    utilPct + "%",
                    colorForUtilization(utilFraction),
                    b.lastDisbursedDate);
            row++;
        }

        panel.getChildren().addAll(header, grid);
        addHoverLift(panel, 24);
        return panel;
    }

    private void addInventoryHeader(GridPane grid) {
        grid.add(headerCell("VILLAGE NAME"), 0, 0);
        grid.add(headerCell("SANCTIONED"), 1, 0);
        grid.add(headerCell("DISBURSED"), 2, 0);
        grid.add(headerCell("UTIL %"), 3, 0);
        grid.add(headerCell("LAST DISBURSED"), 4, 0);
        grid.add(headerCell("ACTION"), 5, 0);
    }

    private void addInventoryRow(GridPane grid, int row, String village, String sanctioned, String disbursed,
                                  String util, String utilColor, String lastDate) {
        Label villageLabel = new Label(village);
        villageLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Label sanctionedLabel = new Label(sanctioned);
        sanctionedLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");
        Label disbursedLabel = new Label(disbursed);
        disbursedLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");
        Label utilLabel = new Label(util);
        utilLabel.setPadding(new Insets(4, 10, 4, 10));
        utilLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + utilColor + "; -fx-background-color: " + rgba(utilColor, 0.12) + "; -fx-background-radius: 999;");
        Label dateLabel = new Label(lastDate);
        dateLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");
        Label actionLabel = new Label("\uD83D\uDC41");
        actionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");

        grid.add(villageLabel, 0, row);
        grid.add(sanctionedLabel, 1, row);
        grid.add(disbursedLabel, 2, row);
        grid.add(utilLabel, 3, row);
        grid.add(dateLabel, 4, row);
        grid.add(actionLabel, 5, row);
    }

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