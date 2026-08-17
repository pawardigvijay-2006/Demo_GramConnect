package com.tech_fusion.view.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.tech_fusion.model.admin.VillageDataStore;
import com.tech_fusion.model.admin.Complaint;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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
 * GramConnect - Complaint Management Page
 * ------------------------------------------------------------------
 * Same glass / forest-saffron template as {@link Dashboard} and
 * {@link BudgetManagment}. Navigates through the shared
 * {@code Dashboard.myStage} using Runnable callbacks - that is the
 * only thing this page still reaches into {@link Dashboard} for.
 *
 * ------------------------------------------------------------------
 * VILLAGE-WISE DYNAMIC DATA (this revision)
 * ------------------------------------------------------------------
 * This page keeps no local copy of the village list, no local
 * "currently selected village" field, and no local complaint data.
 * All three live on the shared data layer, {@link VillageDataStore}:
 *
 *   VillageDataStore.VILLAGES          -> the fixed list of villages under this block
 *   VillageDataStore.selectedVillage   -> the block-wide "currently active" village
 *   VillageDataStore.getComplaints(v)  -> every Complaint tagged with village v
 *                                          ("All Villages" / null returns everything)
 *
 * {@link #getComplaintsForSelectedVillage()} is this page's single
 * required entry point into that data: it always resolves against
 * {@code VillageDataStore.selectedVillage}. Every KPI card, the
 * category breakdown, and the complaint table are computed from it -
 * no numeric/percentage value is hard-coded in this class.
 *
 * Filtering pipeline (exactly as specified):
 *   Selected Village -> Complaint Data -> Search -> Category -> Status -> Priority -> Display
 *
 * KPI cards and the category breakdown use ONLY the village filter
 * (getComplaintsForSelectedVillage()); the search/category/status/
 * priority controls further narrow just the table below them, so the
 * KPIs always describe the whole selected village even while someone
 * is mid-search.
 *
 * Selecting a different village, or changing a table filter, calls
 * {@link #refreshComplaintManagement()} (or the narrower
 * {@link #updateComplaintTable()} for filter-only changes), which
 * update every dynamic component in place - the scene is never
 * rebuilt, so there is no flicker and no layout change.
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

    /** The four categories shown in the breakdown panel - matches the original UI exactly (still 4 rows). */
    private static final String[] BREAKDOWN_CATEGORIES = {
            "Water Supply", "Road & Infrastructure", "Sanitation", "Electricity"
    };

    private Label selectedNavItem;

    /* ---------- Live references so village/filter changes can refresh in place ---------- */
    private Label villageNameLabel;
    private Label villageChevron;
    private VBox villageListBox;
    private Label scopeSubtitle;
    private boolean villageListExpanded = false;

    // KPI cards
    private Label totalValueLabel;
    private Label totalFooterLabel;
    private Label resolvedValueLabel;
    private Label resolvedFooterLabel;
    private Label progressValueLabel;
    private Label progressFooterLabel;
    private Label pendingValueLabel;
    private Label pendingFooterLabel;
    private Label prioritySecondaryLabel;

    // Category breakdown
    private VBox categoryRowsHolder;

    // Complaint table + filters
    private TextField searchField;
    private ComboBox<String> categoryFilterCombo;
    private ComboBox<String> statusFilterCombo;
    private ComboBox<String> priorityFilterCombo;
    private GridPane complaintGrid;

    private static final String ALL_CATEGORIES = "All Categories";
    private static final String ALL_STATUSES = "All Statuses";
    private static final String ALL_PRIORITIES = "All Priorities";

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
     *  VILLAGE + COMPLAINT FILTER HELPERS
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
     * Required reusable entry point: every complaint belonging to the
     * currently selected village ("All Villages" returns everything).
     * KPI cards and the category breakdown are computed from this list
     * only - search/category/status/priority never affect them, only
     * the table below.
     */
    private List<Complaint> getComplaintsForSelectedVillage() {
        return VillageDataStore.getComplaints(currentVillage());
    }

    /**
     * Applies the search box plus the category/status/priority combo
     * filters on top of the village-filtered list, in the required
     * order: Village (already applied) -> Search -> Category -> Status -> Priority.
     * Used only to decide what the table displays.
     */
    private List<Complaint> getTableFilteredComplaints() {
        List<Complaint> result = new ArrayList<>(getComplaintsForSelectedVillage());

        String query = searchField == null ? "" : searchField.getText();
        if (query != null && !query.trim().isEmpty()) {
            String q = query.trim().toLowerCase();
            result.removeIf(c ->
                    !(c.getDescription().toLowerCase().contains(q)
                            || c.getCitizenName().toLowerCase().contains(q)
                            || c.getCategory().toLowerCase().contains(q)
                            || c.getVillage().toLowerCase().contains(q)
                            || c.getComplaintId().toLowerCase().contains(q)));
        }

        String category = categoryFilterCombo == null ? null : categoryFilterCombo.getValue();
        if (category != null && !ALL_CATEGORIES.equals(category)) {
            result.removeIf(c -> !c.getCategory().equals(category));
        }

        String status = statusFilterCombo == null ? null : statusFilterCombo.getValue();
        if (status != null && !ALL_STATUSES.equals(status)) {
            Complaint.Status target = Complaint.Status.valueOf(status.toUpperCase().replace(' ', '_'));
            result.removeIf(c -> c.getStatus() != target);
        }

        String priority = priorityFilterCombo == null ? null : priorityFilterCombo.getValue();
        if (priority != null && !ALL_PRIORITIES.equals(priority)) {
            Complaint.Priority target = Complaint.Priority.valueOf(priority.toUpperCase());
            result.removeIf(c -> c.getPriority() != target);
        }

        return result;
    }

    /**
     * Called whenever the selected village changes. Updates every
     * dynamic component on the page in place - no scene rebuild, no
     * UI redesign.
     */
    private void refreshComplaintManagement() {
        updateScopeSubtitle();
        updateStatCards();
        updateCategoryBreakdown();
        updateComplaintTable();
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
     *  page-local copy.
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

                // Immediate, in-place refresh of every dynamic component.
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
        TextField topSearchField = new TextField();
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
     * ============================================================ */
    private VBox buildMainContent() {
        VBox main = new VBox(24);
        main.setPadding(new Insets(32, 40, 48, 40));
        main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");

        main.getChildren().addAll(
                buildTitleRow(),
                buildStatCardsRow(),
                buildCategorySection(),
                buildInventoryPanel()
        );
        return main;
    }

    private HBox buildTitleRow() {
        VBox text = new VBox(6);
        Label title = new Label("Complaint Management");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        HBox subtitleRow = new HBox(4);
        Label subtitle = new Label("Managing complaints for:");
        subtitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.60);");
        scopeSubtitle = new Label();
        scopeSubtitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        subtitleRow.getChildren().addAll(subtitle, scopeSubtitle);
        text.getChildren().addAll(title, subtitleRow);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

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

        VBox totalCard = kpiCard(FOREST_LIGHT, "\u26A0", "TOTAL COMPLAINTS");
        totalValueLabel = statValueLabel(totalCard);
        totalFooterLabel = new Label();
        totalFooterLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.60);");
        totalCard.getChildren().add(totalFooterLabel);

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
        pendingFooterLabel = new Label();
        pendingFooterLabel.setPadding(new Insets(6, 10, 6, 10));
        pendingFooterLabel.setMaxWidth(Region.USE_PREF_SIZE);
        pendingFooterLabel.setStyle("-fx-background-color: rgba(224,122,31,0.14); -fx-background-radius: 6;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 700; -fx-text-fill: " + SAFFRON_MAIN + ";");
        prioritySecondaryLabel = new Label();
        prioritySecondaryLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.60);");
        VBox pendingExtra = new VBox(6, pendingFooterLabel, prioritySecondaryLabel);
        pendingCard.getChildren().add(pendingExtra);

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
     * {@link #getComplaintsForSelectedVillage()} - never from the
     * table-filtered list, so the cards always describe the whole
     * selected village.
     */
    private void updateStatCards() {
        List<Complaint> complaints = getComplaintsForSelectedVillage();
        int total = complaints.size();

        long resolved = complaints.stream().filter(c -> c.getStatus() == Complaint.Status.RESOLVED).count();
        long inProgress = complaints.stream().filter(c -> c.getStatus() == Complaint.Status.IN_PROGRESS).count();
        long pending = complaints.stream().filter(c -> c.getStatus() == Complaint.Status.PENDING).count();
        long rejected = complaints.stream().filter(c -> c.getStatus() == Complaint.Status.REJECTED).count();
        long highPriority = complaints.stream().filter(c -> c.getPriority() == Complaint.Priority.HIGH).count();
        long critical = complaints.stream().filter(c -> c.getPriority() == Complaint.Priority.CRITICAL).count();

        // double avgResolutionDays = complaints.stream()
                // .filter(c -> c.getStatus() == Complaint.Status.RESOLVED && c.getResolutionDays() != null)
                // .mapToDouble(Complaint::getResolutionDays)
                // .average()
                // .orElse(-1);

         totalValueLabel.setText(String.valueOf(total));
         totalFooterLabel.setText((isAllVillages() + currentVillage()));

        // double resolutionRate = total == 0 ? 0 : (resolved * 100.0) / total;
        resolvedValueLabel.setText(String.valueOf(resolved));
        // resolvedFooterLabel.setText(avgResolutionDays < 0
                // ? String.format("%.0f%% resolution rate", resolutionRate)
                // : String.format("%.0f%% resolution rate \u00B7 avg %.1fd to resolve", resolutionRate, avgResolutionDays));

        progressValueLabel.setText(String.valueOf(inProgress));
        // progressFooterLabel.setText("Being actively worked on");

        pendingValueLabel.setText(String.valueOf(pending));
          pendingFooterLabel.setText(pending == 0 ? "No action required" : "Requires action this week");
         prioritySecondaryLabel.setText(highPriority + " High \u00B7 " + critical + " Critical priority");
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
     *  COMPLAINT CATEGORY BREAKDOWN
     * ============================================================ */
    private VBox buildCategorySection() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Complaint Category Breakdown");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label viewAll = new Label("View All Categories");
        viewAll.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-font-weight: 700; -fx-cursor: hand;");
        header.getChildren().addAll(title, spacer, viewAll);

        categoryRowsHolder = new VBox(20);

        panel.getChildren().addAll(header, categoryRowsHolder);
        addHoverLift(panel, 24);
        return panel;
    }

    /** Rebuilds the 4 category rows from the village-filtered complaint list - same 4 categories, same layout. */
    private void updateCategoryBreakdown() {
        List<Complaint> complaints = getComplaintsForSelectedVillage();
        String[] colors = {FOREST_DEEP, SAFFRON_MAIN, CONTEXT_TEAL, DELAYED_RED};

        categoryRowsHolder.getChildren().clear();
        for (int i = 0; i < BREAKDOWN_CATEGORIES.length; i++) {
            String category = BREAKDOWN_CATEGORIES[i];
            long categoryTotal = complaints.stream().filter(c -> c.getCategory().equals(category)).count();
            long categoryResolved = complaints.stream()
                    .filter(c -> c.getCategory().equals(category) && c.getStatus() == Complaint.Status.RESOLVED)
                    .count();
            double fraction = categoryTotal == 0 ? 0 : (double) categoryResolved / categoryTotal;
            String rightText = categoryResolved + " / " + categoryTotal + " resolved";
            categoryRowsHolder.getChildren().add(categoryRow(category, fraction, rightText, colors[i]));
        }
    }

    private VBox categoryRow(String name, double fillRatio, String rightText, String barColor) {
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
     *  COMPLAINT LOG TABLE
     * ============================================================ */
    private VBox buildInventoryPanel() {
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Complaint Log");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        searchField = new TextField();
        searchField.setPromptText("Filter complaints...");
        searchField.setPrefWidth(220);
        searchField.setStyle(filterFieldStyle());
        searchField.textProperty().addListener((obs, oldV, newV) -> updateComplaintTable());

        categoryFilterCombo = filterCombo(ALL_CATEGORIES, BREAKDOWN_CATEGORIES);
        statusFilterCombo = filterCombo(ALL_STATUSES, new String[]{"Pending", "In Progress", "Resolved", "Rejected"});
        priorityFilterCombo = filterCombo(ALL_PRIORITIES, new String[]{"Low", "Medium", "High", "Critical"});

        header.getChildren().addAll(title, spacer, categoryFilterCombo, statusFilterCombo, priorityFilterCombo, searchField);

        complaintGrid = new GridPane();
        complaintGrid.setHgap(12);
        complaintGrid.setVgap(16);
        complaintGrid.setPadding(new Insets(6, 0, 0, 0));
        complaintGrid.getColumnConstraints().addAll(pct(30), pct(16), pct(18), pct(14), pct(14), pct(8));

        addInventoryHeader(complaintGrid);

        panel.getChildren().addAll(header, complaintGrid);
        addHoverLift(panel, 24);
        return panel;
    }

    /** Small ComboBox styled to match the existing "Filter complaints..." text field, defaulting to "All ...". */
    private ComboBox<String> filterCombo(String allLabel, String[] options) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().add(allLabel);
        combo.getItems().addAll(options);
        combo.setValue(allLabel);
        combo.setPrefWidth(150);
        combo.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 12; -fx-border-radius: 12;" +
                "-fx-border-color: rgba(11,61,46,0.10); -fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px;");
        combo.valueProperty().addListener((obs, oldV, newV) -> updateComplaintTable());
        return combo;
    }

    private String filterFieldStyle() {
        return "-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 12; -fx-border-radius: 12;" +
                "-fx-border-color: rgba(11,61,46,0.10); -fx-padding: 10 14; -fx-font-size: 13px;";
    }

    /**
     * Rebuilds the table body (keeping row 0's headers) from
     * {@link #getTableFilteredComplaints()} - the village-filtered list
     * narrowed further by search/category/status/priority.
     */
    private void updateComplaintTable() {
        complaintGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) != 0);

        List<Complaint> villageComplaints = getComplaintsForSelectedVillage();
        List<Complaint> filtered = getTableFilteredComplaints();

        if (villageComplaints.isEmpty()) {
            Label none = new Label("No complaints found for " + (isAllVillages() ? "any village" : currentVillage()) + ".");
            none.setPadding(new Insets(16, 8, 16, 8));
            none.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.55);");
            complaintGrid.add(none, 0, 1, 6, 1);
            return;
        }

        if (filtered.isEmpty()) {
            Label none = new Label("No complaints match the current search/filters.");
            none.setPadding(new Insets(16, 8, 16, 8));
            none.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.55);");
            complaintGrid.add(none, 0, 1, 6, 1);
            return;
        }

        int row = 1;
        for (Complaint c : filtered) {
            String statusText = displayStatus(c.getStatus());
            String statusColor = statusColor(c.getStatus());
            addInventoryRow(complaintGrid, row, c.getDescription(), c.getVillage(), c.getCategory(),
                    statusText, statusColor, c.getDateFiled());
            row++;
        }
    }

    private String displayStatus(Complaint.Status status) {
        switch (status) {
            case IN_PROGRESS: return "In Progress";
            case RESOLVED: return "Resolved";
            case REJECTED: return "Rejected";
            default: return "Pending";
        }
    }

    private String statusColor(Complaint.Status status) {
        switch (status) {
            case RESOLVED: return CONTEXT_TEAL;
            case IN_PROGRESS: return SAFFRON_MAIN;
            case REJECTED: return "#6B7B74";
            default: return DELAYED_RED;
        }
    }

    private void addInventoryHeader(GridPane grid) {
        grid.add(headerCell("COMPLAINT"), 0, 0);
        grid.add(headerCell("VILLAGE"), 1, 0);
        grid.add(headerCell("CATEGORY"), 2, 0);
        grid.add(headerCell("STATUS"), 3, 0);
        grid.add(headerCell("DATE FILED"), 4, 0);
        grid.add(headerCell("ACTION"), 5, 0);
    }

    private void addInventoryRow(GridPane grid, int row, String complaint, String village, String category,
                                  String status, String statusColor, String date) {
        Label complaintLabel = new Label(complaint);
        complaintLabel.setWrapText(true);
        complaintLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Label villageLabel = new Label(village);
        villageLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");
        Label categoryLabel = new Label(category);
        categoryLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");
        Label statusLabel = new Label(status);
        statusLabel.setPadding(new Insets(4, 10, 4, 10));
        statusLabel.setMaxWidth(Region.USE_PREF_SIZE);
        statusLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + statusColor + "; -fx-background-color: " + rgba(statusColor, 0.12) + "; -fx-background-radius: 999;");
        Label dateLabel = new Label(date);
        dateLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");
        Label actionLabel = new Label("\uD83D\uDC41");
        actionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");

        grid.add(complaintLabel, 0, row);
        grid.add(villageLabel, 1, row);
        grid.add(categoryLabel, 2, row);
        grid.add(statusLabel, 3, row);
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