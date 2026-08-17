package com.tech_fusion.view.admin;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.tech_fusion.model.admin.VillageDataStore;
import com.tech_fusion.model.admin.GeneratedReport;
import com.tech_fusion.model.admin.Project;

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
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * GramConnect - BDO (Block Development Officer) Reports & Analytics Page
 * ------------------------------------------------------------------
 * Same glass / forest-saffron template as {@link Dashboard},
 * {@link BudgetManagment}, {@link ComplaintManagement} and
 * {@link ProjectManagement}. Reuses the identical sidebar / top bar /
 * card shell as Dashboard, with "Reports & Analytics" highlighted as
 * the active nav item, and Runnable-based navigation wired through
 * the shared {@code Dashboard.myStage} so every page can reach every
 * other page from the sidebar.
 *
 * ------------------------------------------------------------------
 * VILLAGE-WISE DYNAMIC DATA (this revision)
 * ------------------------------------------------------------------
 * This page keeps no local copy of the village list and no local
 * "currently selected village" field, and it does NOT depend on
 * {@link Dashboard} (or any other page) to get that data. Both the
 * village list and the current selection live directly on the data
 * layer, {@link VillageDataStore}:
 *
 *   VillageDataStore.VILLAGES          -> the fixed list of villages under this block
 *   VillageDataStore.selectedVillage   -> the block-wide "currently active" village
 *
 * That keeps this page fully self-contained for data purposes - it
 * only reaches into {@code Dashboard} for shared navigation plumbing
 * ({@code Dashboard.myStage}), the same as every other page in the app,
 * never for data.
 *
 * Every number on this page (KPI cards, the budget bar chart, the
 * project-distribution donut, and the recent-reports table) is
 * computed on demand from {@link VillageDataStore}, filtered by
 * {@code VillageDataStore.selectedVillage}. No numeric/percentage value
 * is hard-coded in this class. Selecting a different village in the
 * sidebar calls {@link #refreshReportAnalysis()}, which updates every
 * one of those components in place - the scene is never rebuilt, so
 * there is no flicker and no layout change.
 */
public class ReportsAnalytics extends Application {

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

    /** "%.1f" formatted crore currency helper shared by every dynamic label below. */
    private static final double ONE_CRORE = 10_000_000.0;

    private Label selectedNavItem;

    /* ---------- Live references so village selection can refresh in place ---------- */
    private Label subtitleLabel;
    private Label villageNameLabel;
    private Label villageChevron;
    private VBox villageListBox;
    private boolean villageListExpanded = false;

    // KPI cards
    private Label budgetValueLabel;
    private DynamicProgressBar budgetProgressBar;
    private Label budgetFootnoteLabel;
    private Label successValueLabel;
    private Label successFootnoteLabel;
    private Label approvalValueLabel;
    private Label approvalFootnoteLabel;
    private Label grievanceValueLabel;
    private Label grievancePendingLabel;
    private Label grievanceResolutionLabel;

    // Budget bar chart
    private HBox budgetBars;
    private HBox budgetXAxis;

    // Project distribution donut
    private StackPane donutArcsHolder;
    private Label donutTotalLabel;
    private VBox donutLegend;

    // Recent reports table
    private GridPane reportsGrid;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(getReportsAnalyticsScene());
        primaryStage.setTitle("GramConnect - Reports & Analytics");
        primaryStage.show();
    }

    /** Builds the Reports & Analytics scene. Public so other pages can navigate here. */
    public Scene getReportsAnalyticsScene() {
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
        // currently active on Dashboard before the scene is first shown.
        refreshReportAnalysis();

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
     *  VILLAGE FILTER HELPERS
     *  Reusable, page-level abstractions over VillageDataStore.selectedVillage
     *  and VillageDataStore's aggregate methods, per the "no filtering logic
     *  in UI methods" requirement.
     * ============================================================ */

    /** Currently selected village, defaulting safely if it hasn't been set yet. */
    private String currentVillage() {
        String v = VillageDataStore.selectedVillage;
        return (v == null) ? VillageDataStore.VILLAGES.get(0) : v;
    }

    private boolean isAllVillages() {
        return VillageDataStore.VILLAGES.get(0).equals(currentVillage());
    }

    private List<Project> getSelectedVillageProjects() {
        return VillageDataStore.getProjects(currentVillage());
    }

    private List<GeneratedReport> getSelectedVillageReports() {
        return VillageDataStore.getReports(currentVillage());
    }

    /**
     * Called whenever the selected village changes (from this page's own
     * sidebar switcher, or in principle from anywhere else that mutates
     * VillageDataStore.selectedVillage). Updates every dynamic component on
     * the page in place - no scene rebuild, no UI redesign.
     */
    private void refreshReportAnalysis() {
        updateScopeSubtitle();
        updateStatCards();
        updateBudgetChart();
        updateDistributionPanel();
        updateReportsTable();
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
        nav.setPadding(new Insets(4, 12, 16, 12));

        HBox dashboardNav = navItem("\u25A6", "Dashboard", false);
        Runnable toDashboard = () -> {
            Dashboard page = new Dashboard();
            Dashboard.myStage.setScene(page.getBDODashboardScene());
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

        HBox complaintNav = navItem("\u26A0", "Complaint Management", false);
        Runnable toComplaintManagement = () -> {
            ComplaintManagement page = new ComplaintManagement();
            Dashboard.myStage.setScene(page.getComplaintManagementScene());
        };
        complaintNav.setOnMouseClicked(e -> toComplaintManagement.run());


        HBox reportsNav = navItem("\uD83D\uDCCA", "Reports & Analytics", true);   // <-- active on this page

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
        divider.setStyle("-fx-background-color: linear-gradient(to right, transparent, rgba(11,61,46,0.25), transparent)");

        VBox smallLinks = new VBox(4);
        smallLinks.setPadding(new Insets(8, 0, 0, 0));
        smallLinks.getChildren().addAll(
                footerLink("\u2699", "Settings"),
                footerLink("\u2753", "Support")
        );
        footer.getChildren().addAll(divider,  smallLinks);

        sidebar.getChildren().addAll(header, villageSelector, nav, footer);
        return sidebar;
    }

    /* ============================================================
     *  VILLAGE SWITCHER — identical in look/behaviour to the one on
     *  Dashboard: a collapsed "Managing Village" row that expands
     *  into a selectable list of villages under this BDO's block.
     *  Reads/writes the shared VillageDataStore.VILLAGES / VillageDataStore.selectedVillage
     *  state instead of a page-local copy.
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

        villageNameLabel = new Label(displayName(currentVillage()));
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
        ToggleButton btn = new ToggleButton(displayName(village));
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
                villageNameLabel.setText(displayName(currentVillage()));

                // Collapse the list once a village has been chosen.
                villageListExpanded = false;
                villageListBox.setVisible(false);
                villageListBox.setManaged(false);
                villageChevron.setText("\u25BE");

                // Immediate, in-place refresh of every dynamic component.
                refreshReportAnalysis();
            }
        });

        return btn;
    }

    private String displayName(String village) {
        return village.equals(VillageDataStore.VILLAGES.get(0)) ? "All Villages (Block)" : village;
    }

    /** Keeps the page subtitle in sync with whichever village is currently selected. */
    private void updateScopeSubtitle() {
        if (subtitleLabel == null) return;
        subtitleLabel.setText("Showing reports for: " + (isAllVillages() ? "All Villages" : currentVillage()));
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

        main.getChildren().addAll(
                buildTitleRow(),
                buildStatCardsRow(),
                buildAnalyticsMidSection(),
                buildReportsPanel()
        );
        return main;
    }

    private HBox buildTitleRow() {
        VBox text = new VBox(6);
        Label title = new Label("Reports & Block Analytics");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        subtitleLabel = new Label();
        subtitleLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.60);");
        text.getChildren().addAll(title, subtitleLabel);

        Label dateRange = new Label("\uD83D\uDCC5  Q3 (Oct-Dec 2023)  \u25BE");
        dateRange.setPadding(new Insets(12, 18, 12, 18));
        dateRange.setStyle("-fx-background-color: rgba(255,255,255,0.75); -fx-text-fill: " + FOREST_DEEP + ";" +
                "-fx-background-radius: 10; -fx-border-color: rgba(11,61,46,0.15); -fx-border-radius: 10;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-cursor: hand;");

        Label exportAll = new Label("\u2B07  Export All Data");
        exportAll.setPadding(new Insets(12, 18, 12, 18));
        String exportBase = "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
                "-fx-text-fill: white; -fx-background-radius: 10; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px; -fx-font-weight: 700; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.35), 10, 0.1, 0, 4);";
        exportAll.setStyle(exportBase);
        exportAll.setOnMouseEntered(e -> exportAll.setStyle(exportBase +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.55), 14, 0.15, 0, 5); -fx-translate-y: -1;"));
        exportAll.setOnMouseExited(e -> exportAll.setStyle(exportBase));

        HBox actions = new HBox(12, dateRange, exportAll);
        actions.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerRow = new HBox(20, text, spacer, actions);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        return headerRow;
    }

    /* ============================================================
     *  STAT CARDS ROW
     *  Card shells/visuals unchanged; the value/footnote Labels are
     *  kept as instance fields and populated by updateStatCards().
     * ============================================================ */
   private HBox buildStatCardsRow() {
        HBox row = new HBox(20);

        VBox budgetCard = kpiCard(FOREST_LIGHT, "\uD83D\uDCB0", "TOTAL BLOCK BUDGET");
        budgetValueLabel = statValueLabel(budgetCard);
        VBox budgetExtra = new VBox(8);
        budgetProgressBar = dynamicProgressBar(CONTEXT_TEAL, 8);
        budgetFootnoteLabel = new Label();
        budgetFootnoteLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.60);");
        budgetExtra.getChildren().addAll(budgetProgressBar.track, budgetFootnoteLabel);
        budgetCard.getChildren().add(budgetExtra);

        VBox successCard = kpiCard(SAFFRON_MAIN, "\u2705", "PROJECT SUCCESS RATE");
        successValueLabel = statValueLabel(successCard);
        successFootnoteLabel = new Label();
        successFootnoteLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + CONTEXT_TEAL + ";");
        successCard.getChildren().add(successFootnoteLabel);

        VBox approvalCard = kpiCard(CONTEXT_TEAL, "\uD83D\uDCCB", "AVG. APPROVAL TIME");
        approvalValueLabel = statValueLabel(approvalCard);
        approvalFootnoteLabel = new Label("Target: <5 Days");
        approvalFootnoteLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.60);");
        approvalCard.getChildren().add(approvalFootnoteLabel);

        VBox grievanceCard = kpiCard(AI_VIOLET, "\u26A0", "ACTIVE GRIEVANCES");
        grievanceValueLabel = statValueLabel(grievanceCard);
        grievanceValueLabel.setStyle(
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 26px; -fx-font-weight: 900; -fx-text-fill: " + DELAYED_RED + ";");
        VBox grievanceExtra = new VBox(4);
        grievancePendingLabel = new Label();
        grievancePendingLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + DELAYED_RED + ";");
        grievanceResolutionLabel = new Label();
        grievanceResolutionLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.60);");
        grievanceExtra.getChildren().addAll(grievancePendingLabel, grievanceResolutionLabel);
        grievanceCard.getChildren().add(grievanceExtra);

        HBox.setHgrow(budgetCard, Priority.ALWAYS);
        HBox.setHgrow(successCard, Priority.ALWAYS);
        HBox.setHgrow(approvalCard, Priority.ALWAYS);
        HBox.setHgrow(grievanceCard, Priority.ALWAYS);
        row.getChildren().addAll(budgetCard, successCard, approvalCard, grievanceCard);
        return row;
    }

    /** Pulls the stat-value Label back out of a card built by kpiCard(), so it can be kept live. */
    private Label statValueLabel(VBox card) {
        VBox inner = (VBox) card.getChildren().get(1);
        VBox bottom = (VBox) inner.getChildren().get(2);
        return (Label) bottom.getChildren().get(0);
    }

    /** Recomputes and writes every KPI card value for the currently selected village. */
    private void updateStatCards() {
        String village = currentVillage();

        double totalBudget = VillageDataStore.getTotalBudget(village);
        double utilizedBudget = VillageDataStore.getUtilizedBudget(village);
        double utilizationFraction = VillageDataStore.getBudgetUtilizationFraction(village);
        budgetValueLabel.setText(String.format("%.1fCr", totalBudget / ONE_CRORE));
        budgetProgressBar.update(utilizationFraction);
        budgetFootnoteLabel.setText(String.format("%.0f%% Utilized     \u20B9%.1fCr",
                utilizationFraction * 100, utilizedBudget / ONE_CRORE));

        double successRate = VillageDataStore.getProjectSuccessRate(village);
        int totalProjects = VillageDataStore.getTotalProjectCount(village);
        successValueLabel.setText(String.format("%.0f%%", successRate));
        successFootnoteLabel.setText(String.format("Based on %d project%s", totalProjects, totalProjects == 1 ? "" : "s"));

        double avgApproval = VillageDataStore.getAvgApprovalTimeDays(village);
        approvalValueLabel.setText(String.format("%.1fday", avgApproval));

        int activeGrievances = VillageDataStore.getActiveGrievances(village);
        double resolutionRate = VillageDataStore.getGrievanceResolutionRate(village);
        grievanceValueLabel.setText(String.valueOf(activeGrievances));
        grievancePendingLabel.setText("Pending");
        grievanceResolutionLabel.setText(String.format("%.0f%% Resolution Rate", resolutionRate));
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

    /** Small holder so a progress-bar fill can be recomputed after the card is already on screen. */
    private static final class DynamicProgressBar {
        final StackPane track;
        final Region fill;
        double fraction;

        DynamicProgressBar(StackPane track, Region fill) {
            this.track = track;
            this.fill = fill;
        }

        void update(double newFraction) {
            this.fraction = newFraction;
            double width = track.getWidth();
            fill.setMaxWidth(width > 0 ? width * newFraction : 0);
        }
    }

    private DynamicProgressBar dynamicProgressBar(String color, double height) {
        StackPane track = new StackPane();
        track.setPrefHeight(height);
        track.setMinHeight(height);
        track.setMaxWidth(Double.MAX_VALUE);
        track.setStyle("-fx-background-color: rgba(11,61,46,0.08); -fx-background-radius: 999;");
        Region fill = new Region();
        fill.setPrefHeight(height);
        fill.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 999;");
        StackPane.setAlignment(fill, Pos.CENTER_LEFT);
        track.getChildren().add(fill);

        DynamicProgressBar bar = new DynamicProgressBar(track, fill);
        track.widthProperty().addListener((obs, o, w) -> bar.update(bar.fraction));
        fill.setMaxWidth(0);
        return bar;
    }

    /* ============================================================
     *  MID SECTION: Budget chart panel + Project Distribution donut
     * ============================================================ */
    private HBox buildAnalyticsMidSection() {
        HBox row = new HBox(20);
        row.setFillHeight(true);

        VBox budgetChart = buildBudgetChartPanel();
        VBox distribution = buildProjectDistributionPanel();

        distribution.setPrefWidth(340);
        distribution.setMinWidth(340);
        distribution.setMaxWidth(340);

        budgetChart.setMinWidth(0);
        HBox.setHgrow(budgetChart, Priority.ALWAYS);

        row.getChildren().addAll(budgetChart, distribution);
        return row;
    }

    private VBox buildBudgetChartPanel() {
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Village-wise Budget Utilization");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label viewDetails = new Label("View Details  \u203A");
        viewDetails.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-font-weight: 700; -fx-cursor: hand;");
        header.getChildren().addAll(title, spacer, viewDetails);

        HBox chartArea = new HBox(18);
        chartArea.setAlignment(Pos.BOTTOM_CENTER);
        chartArea.setPrefHeight(220);
        chartArea.setPadding(new Insets(10, 4, 0, 4));

        VBox yAxis = new VBox();
        yAxis.setPrefHeight(220);
        yAxis.setAlignment(Pos.TOP_RIGHT);
        String[] yLabels = {"100%", "75%", "50%", "25%", "0%"};
        for (String yl : yLabels) {
            Label l = new Label(yl);
            l.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 10.5px; -fx-text-fill: rgba(11,61,46,0.45);");
            VBox.setVgrow(l, Priority.ALWAYS);
            yAxis.getChildren().add(l);
        }

        StackPane gridWrap = new StackPane();
        HBox.setHgrow(gridWrap, Priority.ALWAYS);
        VBox gridLines = new VBox();
        gridLines.setPrefHeight(220);
        for (int i = 0; i < 5; i++) {
            Region line = new Region();
            line.setPrefHeight(1);
            line.setStyle("-fx-background-color: rgba(11,61,46,0.08);");
            VBox.setVgrow(line, Priority.ALWAYS);
            gridLines.getChildren().add(line);
        }

        budgetBars = new HBox(24);
        budgetBars.setAlignment(Pos.BOTTOM_CENTER);
        budgetBars.setPadding(new Insets(0, 8, 0, 8));

        gridWrap.getChildren().addAll(gridLines, budgetBars);
        chartArea.getChildren().addAll(yAxis, gridWrap);

        budgetXAxis = new HBox();
        budgetXAxis.setPadding(new Insets(4, 4, 0, 40));

        panel.getChildren().addAll(header, chartArea, budgetXAxis);
        addHoverLift(panel, 24);
        return panel;
    }

    /**
     * Rebuilds the bars/x-axis labels for the currently selected village.
     * "All Villages" shows one bar per village (block-wide comparison);
     * a single village shows just that village's own utilization bar.
     * Same chart component either way - never a separate chart per village.
     */
    private void updateBudgetChart() {
        budgetBars.getChildren().clear();
        budgetXAxis.getChildren().clear();

        Map<String, Double> series;
        if (isAllVillages()) {
            series = VillageDataStore.getBudgetUtilizationByVillage(VillageDataStore.VILLAGES);
        } else {
            series = new java.util.LinkedHashMap<>();
            series.put(currentVillage(), VillageDataStore.getBudgetUtilizationFraction(currentVillage()));
        }

        for (Map.Entry<String, Double> entry : series.entrySet()) {
            VBox barWrap = new VBox();
            barWrap.setAlignment(Pos.BOTTOM_CENTER);
            Region bar = new Region();
            bar.setPrefWidth(34);
            bar.setStyle("-fx-background-color: linear-gradient(to top, " + FOREST_DEEP + ", " + CONTEXT_TEAL + ");" +
                    "-fx-background-radius: 6 6 0 0;");
            double h = Math.max(0, Math.min(1, entry.getValue())) * 200;
            bar.setPrefHeight(h);
            bar.setMinHeight(h);
            barWrap.getChildren().add(bar);
            barWrap.setPrefHeight(220);
            HBox.setHgrow(barWrap, Priority.ALWAYS);
            budgetBars.getChildren().add(barWrap);

            Label l = new Label(entry.getKey());
            l.setMaxWidth(Double.MAX_VALUE);
            l.setAlignment(Pos.CENTER);
            l.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: rgba(11,61,46,0.60);");
            HBox.setHgrow(l, Priority.ALWAYS);
            budgetXAxis.getChildren().add(l);
        }
    }

    private VBox buildProjectDistributionPanel() {
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

        Label title = new Label("Project Distribution");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        donutArcsHolder = new StackPane();
        StackPane donutWrap = new StackPane(donutArcsHolder);
        donutWrap.setAlignment(Pos.CENTER);
        donutWrap.setPadding(new Insets(8, 0, 8, 0));

        donutLegend = new VBox(10);

        panel.getChildren().addAll(title, donutWrap, donutLegend);
        addHoverLift(panel, 24);
        return panel;
    }

    /** Rebuilds the donut arcs, center total, and legend from the current village's project categories. */
    private void updateDistributionPanel() {
        Map<String, Integer> distribution = VillageDataStore.getProjectCategoryDistribution(currentVillage());
        int total = 0;
        for (int count : distribution.values()) total += count;

        String[] palette = {FOREST_DEEP, CONTEXT_TEAL, "#8FCB9E", "#CDEBD8", AI_VIOLET};

        donutArcsHolder.getChildren().clear();
        double size = 190;
        double radius = size / 2;
        double startAngle = 90;
        int colorIndex = 0;

        if (total == 0) {
            Circle empty = new Circle(radius * 0.8);
            empty.setFill(Color.web("#EFEFEF"));
            donutArcsHolder.getChildren().add(empty);
        } else {
            for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
                double fraction = entry.getValue() / (double) total;
                double length = fraction * 360;
                String color = palette[colorIndex % palette.length];
                Arc arc = new Arc(radius, radius, radius, radius, startAngle, -length);
                arc.setType(ArcType.ROUND);
                arc.setFill(Color.web(color));
                StackPane arcHolder = new StackPane(arc);
                arcHolder.setPrefSize(size, size);
                arcHolder.setMaxSize(size, size);
                donutArcsHolder.getChildren().add(arcHolder);
                startAngle -= length;
                colorIndex++;
            }
        }

        Circle hole = new Circle(radius * 0.58);
        hole.setFill(Color.web("#FFFFFF", 0.92));
        donutArcsHolder.getChildren().add(hole);

        VBox centerText = new VBox(2);
        centerText.setAlignment(Pos.CENTER);
        donutTotalLabel = new Label(String.valueOf(total));
        donutTotalLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 26px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label totalLabel = new Label("Total Projects");
        totalLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: rgba(11,61,46,0.60);");
        centerText.getChildren().addAll(donutTotalLabel, totalLabel);
        donutArcsHolder.getChildren().add(centerText);
        donutArcsHolder.setPrefSize(size, size);

        donutLegend.getChildren().clear();
        colorIndex = 0;
        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            double pct = total == 0 ? 0 : (entry.getValue() * 100.0) / total;
            String color = palette[colorIndex % palette.length];
            donutLegend.getChildren().add(legendRow(color, entry.getKey(), String.format("%.0f%%", pct)));
            colorIndex++;
        }
        if (distribution.isEmpty()) {
            Label none = new Label("No projects recorded for this village.");
            none.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.55);");
            donutLegend.getChildren().add(none);
        }
    }

    private HBox legendRow(String color, String label, String pct) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        Circle dot = new Circle(5, Color.web(color));
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label pctLbl = new Label(pct);
        pctLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: rgba(11,61,46,0.60);");
        row.getChildren().addAll(dot, lbl, spacer, pctLbl);
        return row;
    }

    /* ============================================================
     *  RECENT GENERATED REPORTS
     * ============================================================ */
    private VBox buildReportsPanel() {
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Recent Generated Reports");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label viewAll = new Label("View All Reports");
        viewAll.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-font-weight: 700; -fx-cursor: hand;");
        header.getChildren().addAll(title, spacer, viewAll);

        reportsGrid = new GridPane();
        reportsGrid.setHgap(12);
        reportsGrid.setVgap(6);
        reportsGrid.setPadding(new Insets(6, 0, 0, 0));
        reportsGrid.getColumnConstraints().addAll(pct(40), pct(15), pct(25), pct(20));

        reportsGrid.add(headerCell("REPORT NAME"), 0, 0);
        reportsGrid.add(headerCell("TYPE"), 1, 0);
        reportsGrid.add(headerCell("GENERATED ON"), 2, 0);
        reportsGrid.add(headerCell("ACTIONS"), 3, 0);

        panel.getChildren().addAll(header, reportsGrid);
        addHoverLift(panel, 24);
        return panel;
    }

    /** Rebuilds the table body (keeping row 0's headers) from the current village's report list. */
    private void updateReportsTable() {
        reportsGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) != 0);

        List<GeneratedReport> reports = getSelectedVillageReports();
        int row = 1;
        for (int i = 0; i < reports.size(); i++) {
            GeneratedReport r = reports.get(i);
            addReportRow(reportsGrid, row, r.getName(), r.getType(), r.getDateGenerated());
            row++;
            if (i < reports.size() - 1) {
                reportsGrid.add(rowDivider(), 0, row, 4, 1);
                row++;
            }
        }

        if (reports.isEmpty()) {
            Label none = new Label("No reports generated for this village yet.");
            none.setPadding(new Insets(16, 8, 16, 8));
            none.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.55);");
            reportsGrid.add(none, 0, row, 4, 1);
        }
    }

    private void addReportRow(GridPane grid, int row, String name, String type, String date) {
        Label nameLabel = new Label(name);
        nameLabel.setPadding(new Insets(12, 8, 12, 8));
        nameLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");

        Label typeLabel = new Label(type);
        typeLabel.setPadding(new Insets(4, 10, 4, 10));
        typeLabel.setMaxWidth(Region.USE_PREF_SIZE);
        String typeColor = type.equals("PDF") ? DELAYED_RED : CONTEXT_TEAL;
        typeLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + typeColor + "; -fx-background-color: " + rgba(typeColor, 0.12) + "; -fx-background-radius: 999;");
        HBox typeCell = new HBox(typeLabel);
        typeCell.setAlignment(Pos.CENTER_LEFT);
        typeCell.setPadding(new Insets(12, 8, 12, 8));

        Label dateLabel = new Label(date);
        dateLabel.setPadding(new Insets(12, 8, 12, 8));
        dateLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");

        HBox actions = new HBox(10);
        actions.setPadding(new Insets(12, 8, 12, 8));
        Label download = new Label("\u2B07");
        download.setStyle("-fx-font-size: 14px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");
        Label view = new Label("\uD83D\uDC41");
        view.setStyle("-fx-font-size: 14px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");
        actions.getChildren().addAll(view, download);

        grid.add(nameLabel, 0, row);
        grid.add(typeCell, 1, row);
        grid.add(dateLabel, 2, row);
        grid.add(actions, 3, row);
    }

    private Region rowDivider() {
        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: rgba(11,61,46,0.06);");
        return divider;
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