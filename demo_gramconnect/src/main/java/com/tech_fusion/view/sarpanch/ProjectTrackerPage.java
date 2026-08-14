package com.sarpanch.view;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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


public class ProjectTrackerPage {

    /* ---------- Color palette (kept identical to SarpanchDashboard) ---------- */
    private static final String FOREST_DEEP   = "#0B3D2E";
    private static final String FOREST_LIGHT  = "#0F4736";
    private static final String SAFFRON_MAIN  = "#E07A1F";
    private static final String CONTEXT_TEAL  = "#0E8C8C";
    private static final String AI_VIOLET     = "#7C5CFC";
    private static final String DELAYED_RED   = "#D94C38";
    private static final String SIDEBAR_TOP   = "#CDEBD8";
    private static final String SIDEBAR_MID   = "#Bce3cc";
    private static final String SIDEBAR_BOT   = "#A9D8BD";

    private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

    private static final String BACKGROUND_IMAGE_PATH =
        "C:/Users/Ashish/Downloads/Background File of each Page/Background Image.png";

    /** The action to run to navigate back to the Dashboard (passed in from SarpanchDashboard). */
    private Runnable backToDashboardAction;

    /**
     * Builds the Project Tracker scene and returns it.
     * backToDashboardAction is stored and invoked when the user clicks "Dashboard"
     * in the sidebar, matching the demo's Runnable navigation pattern.
     */
    public Scene getProjectTrackerScene(Runnable backToDashboardAction) {
        this.backToDashboardAction = backToDashboardAction;

        BorderPane root = new BorderPane();
        Image backgroundImage = new Image(new File(BACKGROUND_IMAGE_PATH).toURI().toString());
        root.setBackground(new Background(new BackgroundImage(backgroundImage,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundPosition.CENTER,
                                                            new BackgroundSize(100, 100, true, true, false, true)
        )));

        root.setLeft(buildSidebar());

        BorderPane contentArea = new BorderPane();
        contentArea.setTop(buildTopBar());

        ScrollPane scroller = new ScrollPane(buildMainContent());
        scroller.setFitToWidth(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setStyle(
                "-fx-background: transparent; " +
                "-fx-background-color: transparent; " +
                "-fx-border-color: transparent;"
        );
        contentArea.setCenter(scroller);

        root.setCenter(contentArea);

        return new Scene(root, 1300, 800);
    }

    /* ============================================================
     *  SIDEBAR  (identical to SarpanchDashboard, "Project Tracker" active)
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
        Label avatarInitials = new Label("SP");
        avatarInitials.setFont(Font.font("Inter", FontWeight.BOLD, 16));
        avatarInitials.setTextFill(Color.WHITE);
        avatar.getChildren().addAll(avatarCircle, avatarInitials);

        VBox nameBox = new VBox(2);
        Label name = new Label("Sarpanch Patil");
        name.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label role = new Label("Gram Panchayat");
        role.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.65); -fx-letter-spacing: 0.05em;");
        nameBox.getChildren().addAll(name, role);

        header.getChildren().addAll(avatar, nameBox);

        VBox nav = new VBox(6);
        nav.setPadding(new Insets(16, 12, 16, 12));

        HBox dashboardNav = navItem("\u25A6", "Dashboard", false);
        dashboardNav.setOnMouseClicked(e -> {
            System.out.println("Back to Dashboard clicked");
            backToDashboardAction.run();
        });

        HBox complaintsNav = navItem("\u26A0", "Complaints", false);
        complaintsNav.setOnMouseClicked(e -> {
            System.out.println("Complaints clicked");
            SarpanchComplaintsPage complaintsPage = new SarpanchComplaintsPage();
            SarpanchDashboard.myStage.setTitle("GramConnect - Complaints");
            SarpanchDashboard.myStage.setScene(complaintsPage.getComplaintsScene(backToDashboardAction));
        });

        HBox citizenServicesNav = navItem("\uD83D\uDCC4", "Citizen Services", false);
        citizenServicesNav.setOnMouseClicked(e -> {
            System.out.println("Citizen Services clicked");
            CitizenServicesPage citizenServicesPage = new CitizenServicesPage();
            SarpanchDashboard.myStage.setTitle("GramConnect - Citizen Services");
            SarpanchDashboard.myStage.setScene(citizenServicesPage.getCitizenServicesScene(backToDashboardAction));
        });

        HBox announcementsNav = navItem("\uD83D\uDCE2", "Announcements", false);
        announcementsNav.setOnMouseClicked(e -> {
            System.out.println("Announcements clicked");
            AnnouncementsPage announcementsPage = new AnnouncementsPage();
            SarpanchDashboard.myStage.setTitle("GramConnect - Announcements");
            SarpanchDashboard.myStage.setScene(announcementsPage.getAnnouncementsScene(backToDashboardAction));
        });

        nav.getChildren().addAll(
            dashboardNav,
            navItem("\uD83D\uDDC2", "Project Tracker", true),
            complaintsNav,
            citizenServicesNav,
            announcementsNav
        );
        VBox.setVgrow(nav, Priority.ALWAYS);

        VBox footer = new VBox(10);
        footer.setPadding(new Insets(20, 24, 24, 24));

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: linear-gradient(to right, transparent, rgba(11,61,46,0.25), transparent);");

        Label createBtn = new Label("+   Create Project");
        createBtn.setMaxWidth(Double.MAX_VALUE);
        createBtn.setAlignment(Pos.CENTER);
        createBtn.setPadding(new Insets(14, 16, 14, 16));
        String createBase =
            "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
            "-fx-background-radius: 12; -fx-text-fill: white;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 700;" +
            "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.35), 10, 0.1, 0, 4); -fx-cursor: hand;";
        createBtn.setStyle(createBase);
        createBtn.setOnMouseEntered(e -> createBtn.setStyle(createBase +
            "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.55), 15, 0.2, 0, 5); -fx-translate-y: -1;"));
        createBtn.setOnMouseExited(e -> createBtn.setStyle(createBase));

        VBox smallLinks = new VBox(4);
        smallLinks.setPadding(new Insets(8, 0, 0, 0));
        smallLinks.getChildren().addAll(
            footerLink("\u2699", "Settings"),
            footerLink("\u2753", "Support")
        );

        footer.getChildren().addAll(divider, createBtn, smallLinks);

        sidebar.getChildren().addAll(header, nav, footer);
        return sidebar;
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
        item.setStyle("-fx-cursor: hand;");

        if (active) {
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
     *  TOP NAVIGATION BAR (identical to SarpanchDashboard)
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

        Image projectLogo = new Image("assets/images/ProjectLogo.png");
        ImageView imgView = new ImageView(projectLogo);
        imgView.setFitHeight(50);
        imgView.setFitWidth(60);

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
        searchField.setPromptText("Search projects...");
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

        topBar.getChildren().addAll(imgView, searchBox, spacer, bell, vDivider, profile);
        return topBar;
    }

    /* ============================================================
     *  MAIN CONTENT
     * ============================================================ */
    private VBox buildMainContent() {
        VBox main = new VBox(32);
        main.setPadding(new Insets(32, 40, 48, 40));
        main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");

        VBox welcome = new VBox(6);
        Label welcomeTitle = new Label("Project Tracker");
        welcomeTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label welcomeSub = new Label("Approve, allocate, monitor and verify every village project from one place.");
        welcomeSub.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.70);");
        welcomeSub.setWrapText(true);
        welcome.getChildren().addAll(welcomeTitle, welcomeSub);

        main.getChildren().addAll(
            welcome,
            buildQuickStatsRow(),
            buildFeatureGrid()
        );
        return main;
    }

    /* ============================================================
     *  QUICK STATS ROW (same KPI-card visual language as Dashboard)
     * ============================================================ */
    private HBox buildQuickStatsRow() {
        HBox row = new HBox(24);
        row.setAlignment(Pos.TOP_LEFT);

        VBox kpi1 = kpiCard(FOREST_DEEP, "\uD83D\uDCBC", "TOTAL PROJECTS", "24");
        VBox kpi2 = kpiCard(SAFFRON_MAIN, "\u23F3", "PENDING APPROVALS", "5");
        VBox kpi3 = kpiCard(DELAYED_RED, "\u26A0", "DELAYED PROJECTS", "2");
        VBox kpi4 = kpiCard(AI_VIOLET, "\u20B9", "BUDGET ALLOCATED", "\u20B96,65,000");

        HBox.setHgrow(kpi1, Priority.ALWAYS);
        HBox.setHgrow(kpi2, Priority.ALWAYS);
        HBox.setHgrow(kpi3, Priority.ALWAYS);
        HBox.setHgrow(kpi4, Priority.ALWAYS);
        row.getChildren().addAll(kpi1, kpi2, kpi3, kpi4);
        return row;
    }

    private VBox kpiCard(String accent, String icon, String labelText, String statText) {
        VBox card = new VBox();
        card.setPrefWidth(320);
        card.setMinHeight(140);
        card.setStyle(cardStyle(16));

        Region strip = new Region();
        strip.setPrefHeight(6);
        strip.setStyle("-fx-background-color: " + accent + "; -fx-background-radius: 16 16 0 0;");

        VBox inner = new VBox(16);
        inner.setPadding(new Insets(20, 24, 24, 24));

        HBox head = new HBox(12);
        head.setAlignment(Pos.CENTER_LEFT);
        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(44, 44);
        iconChip.setMinSize(44, 44);
        iconChip.setStyle("-fx-background-color: " + rgba(accent, 0.12) + "; -fx-background-radius: 12;");
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 17px; -fx-text-fill: " + accent + ";");
        iconChip.getChildren().add(ic);
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800;" +
            "-fx-text-fill: rgba(11,61,46,0.80); -fx-letter-spacing: 0.08em;");
        lbl.setWrapText(true);
        head.getChildren().addAll(iconChip, lbl);

        Label stat = new Label(statText);
        stat.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        inner.getChildren().addAll(head, stat);
        card.getChildren().addAll(strip, inner);
        addHoverLift(card, 16);
        return card;
    }

    /* ============================================================
     *  FEATURE GRID
     * ============================================================ */
    private VBox buildFeatureGrid() {
        VBox wrap = new VBox(20);

        HBox head = new HBox(12);
        head.setAlignment(Pos.CENTER_LEFT);
        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(40, 40);
        iconChip.setMinSize(40, 40);
        iconChip.setStyle("-fx-background-color: rgba(11,61,46,0.10); -fx-background-radius: 999;");
        Label hIcon = new Label("\u25A6");
        hIcon.setStyle("-fx-font-size: 16px; -fx-text-fill: " + FOREST_DEEP + ";");
        iconChip.getChildren().add(hIcon);
        Label title = new Label("Project Tracker Actions");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        head.getChildren().addAll(iconChip, title);

        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(24);
        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(25);
            grid.getColumnConstraints().add(cc);
        }

        String[][] features = {
            {"\uD83D\uDCC1", "View All Projects", "Browse the complete list of ongoing, completed and pending projects.", CONTEXT_TEAL},
            {"\uD83D\uDCB0", "Allocate Budget", "Assign budget allocations to approved projects across categories.", SAFFRON_MAIN},
            {"\u2696",       "Approve Budget Changes", "Review and sanction requested changes to allocated project budgets.", AI_VIOLET},
            {"\uD83D\uDCC8", "Monitor Project Progress", "Track real-time progress, milestones and delays for every project.", FOREST_DEEP},
            {"\uD83E\uDD16", "View AI Analysis", "See AI-generated insights and risk flags for project execution.", AI_VIOLET},
            {"\uD83D\uDCCD", "Verify GPS & Timestamp", "Validate site photos against GPS location and capture timestamp.", CONTEXT_TEAL}
        };

        Runnable[] openActions = {
            () -> openViewAllProjectsPage(),
            () -> openAllocateBudgetPage(),
            () -> openApproveBudgetChangesPage(),
            () -> openMonitorProjectProgressPage(),
            () -> openViewAiAnalysisPage(),
            () -> openVerifyGpsTimestampPage()
        };

        for (int i = 0; i < features.length; i++) {
            VBox card = featureCard(features[i][0], features[i][1], features[i][2], features[i][3], openActions[i]);
            grid.add(card, i % 4, i / 4);
        }

        wrap.getChildren().addAll(head, grid);
        return wrap;
    }

    /** A single clickable action tile for one Project Tracker feature. */
    private VBox featureCard(String icon, String titleText, String description, String accent,
                             Runnable openAction) {
        VBox card = new VBox(16);
        card.setPadding(new Insets(24));
        card.setPrefHeight(190);
        card.setStyle(cardStyle(20));
        card.setCursor(javafx.scene.Cursor.HAND);

        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(48, 48);
        iconChip.setMinSize(48, 48);
        iconChip.setStyle("-fx-background-color: " + rgba(accent, 0.12) + "; -fx-background-radius: 14;");
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 20px; -fx-text-fill: " + accent + ";");
        iconChip.getChildren().add(ic);

        Label title = new Label(titleText);
        title.setWrapText(true);
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");

        Label desc = new Label(description);
        desc.setWrapText(true);
        desc.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.65);");

        Region grow = new Region();
        VBox.setVgrow(grow, Priority.ALWAYS);

        Label openPill = new Label("Open \u2192");
        openPill.setPadding(new Insets(6, 14, 6, 14));
        openPill.setMaxWidth(Region.USE_PREF_SIZE);
        openPill.setStyle("-fx-background-color: " + rgba(accent, 0.10) + "; -fx-background-radius: 999;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + accent + ";");

        card.getChildren().addAll(iconChip, title, desc, grow, openPill);
        addHoverLift(card, 20);

        // Placeholder handler — wire this up to the actual feature screen/action.
        card.setOnMouseClicked(e -> openAction.run());

        return card;
    }

    private Runnable createBackToProjectTrackerAction() {
        return () -> {
            System.out.println("Back to Project Tracker clicked");
            SarpanchDashboard.myStage.setTitle("GramConnect - Project Tracker");
            SarpanchDashboard.myStage.setScene(getProjectTrackerScene(backToDashboardAction));
        };
    }

    private void openViewAllProjectsPage() {
        System.out.println("[Project Tracker] Opened: View All Projects");
        ViewAllProjectsPage page = new ViewAllProjectsPage();
        SarpanchDashboard.myStage.setScene(
            page.getViewAllProjectsScene(createBackToProjectTrackerAction(), backToDashboardAction)
        );
    }

    private void openAllocateBudgetPage() {
        System.out.println("[Project Tracker] Opened: Allocate Budget");
        AllocateBudgetPage page = new AllocateBudgetPage();
        SarpanchDashboard.myStage.setScene(
            page.getAllocateBudgetScene(createBackToProjectTrackerAction(), backToDashboardAction)
        );
    }

    private void openApproveBudgetChangesPage() {
        System.out.println("[Project Tracker] Opened: Approve Budget Changes");
        ApproveBudgetChangesPage page = new ApproveBudgetChangesPage();
        SarpanchDashboard.myStage.setScene(
            page.getApproveBudgetChangesScene(createBackToProjectTrackerAction(), backToDashboardAction)
        );
    }

    private void openMonitorProjectProgressPage() {
        System.out.println("[Project Tracker] Opened: Monitor Project Progress");
        MonitorProjectProgressPage page = new MonitorProjectProgressPage();
        SarpanchDashboard.myStage.setScene(
            page.getMonitorProjectProgressScene(createBackToProjectTrackerAction(), backToDashboardAction)
        );
    }

    private void openViewAiAnalysisPage() {
        System.out.println("[Project Tracker] Opened: View AI Analysis");
        ViewAiAnalysisPage page = new ViewAiAnalysisPage();
        SarpanchDashboard.myStage.setScene(
            page.getViewAiAnalysisScene(createBackToProjectTrackerAction(), backToDashboardAction)
        );
    }

    private void openVerifyGpsTimestampPage() {
        System.out.println("[Project Tracker] Opened: Verify GPS & Timestamp");
        VerifyGpsTimestampPage page = new VerifyGpsTimestampPage();
        SarpanchDashboard.myStage.setScene(
            page.getVerifyGpsTimestampScene(createBackToProjectTrackerAction(), backToDashboardAction)
        );
    }

    /* ============================================================
     *  HELPERS (kept identical to SarpanchDashboard)
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