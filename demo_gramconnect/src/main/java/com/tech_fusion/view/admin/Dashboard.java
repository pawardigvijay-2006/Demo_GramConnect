package com.tech_fusion.view.admin;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
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
 * Same glass / forest-saffron template as {@link Dashboard},
 * {@link BudgetManagment}, {@link ComplaintManagement} and
 * {@link ProjectManagement}. Keeps BDO's own content (Block
 * Development Overview, Recent Projects requiring Approval,
 * Emergency Queue, Recent Citizen Complaints) but rebuilt with the
 * shared sidebar / top bar / card shell and Runnable-based full mesh
 * navigation through the shared {@code Dashboard.myStage}.
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

    private static final String BACKGROUND_IMAGE_PATH =
            "C:/Users/Ashish/Downloads/Background File of each Page/Background Image.png";

    private Label selectedNavItem;

    public static Stage myStage;

    @Override
    public void start(Stage stage) {
        Dashboard.myStage = stage;
        stage.setTitle("GramConnect - BDO Office | Block Development Dashboard");
        stage.setScene(getBDODashboardScene());
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.show();
    }

    /** Builds the BDO Dashboard scene. Public so other pages can navigate here. */
    public Scene getBDODashboardScene() {
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

        VBox nav = new VBox(6);
        nav.setPadding(new Insets(16, 12, 16, 12));

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
                reportsNav,
                navItem("\uD83D\uDCC4", "Citizen Services", false),
                navItem("\uD83D\uDCE2", "Announcements", false)
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
                buildMidSection(),
                buildComplaintsPanel()
        );
        return main;
    }

    private HBox buildTitleRow() {
        VBox text = new VBox(6);
        Label title = new Label("Block Development Overview");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label subtitle = new Label("Monitor village development, projects and governance");
        subtitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.60);");
        text.getChildren().addAll(title, subtitle);

        Label createProject = new Label("+  Create Project");
        createProject.setPadding(new Insets(12, 18, 12, 18));
        createProject.setStyle("-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
                "-fx-text-fill: white; -fx-background-radius: 10; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px; -fx-font-weight: 700; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.35), 10, 0.1, 0, 4);");
        createProject.setOnMouseEntered(e -> createProject.setStyle(
                "-fx-background-color: linear-gradient(to right, " + FOREST_DEEP + ", " + FOREST_DEEP + ");" +
                "-fx-text-fill: white; -fx-background-radius: 10; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px; -fx-font-weight: 700; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.55), 14, 0.15, 0, 5); -fx-translate-y: -1;"));
        createProject.setOnMouseExited(e -> createProject.setStyle(
                "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
                "-fx-text-fill: white; -fx-background-radius: 10; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px; -fx-font-weight: 700; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.35), 10, 0.1, 0, 4);"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerRow = new HBox(20, text, spacer, createProject);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        return headerRow;
    }

    /* ============================================================
     *  STAT CARDS ROW
     * ============================================================ */
    private HBox buildStatCardsRow() {
        HBox row = new HBox(20);

        VBox totalCard = statCard("TOTAL PROJECTS", "142", "\uD83D\uDCBC", "rgba(11,61,46,0.10)");
        Label totalFooter = new Label("\u2197  12% vs last month");
        totalFooter.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + CONTEXT_TEAL + ";");
        totalCard.getChildren().add(totalFooter);

        VBox pendingCard = statCard("PENDING APPROVALS", "18", "\u23F3", "rgba(224,122,31,0.12)");
        Label pendingBadge = new Label("High Priority");
        pendingBadge.setPadding(new Insets(6, 10, 6, 10));
        pendingBadge.setMaxWidth(Region.USE_PREF_SIZE);
        pendingBadge.setStyle("-fx-background-color: rgba(224,122,31,0.14); -fx-background-radius: 6;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 700; -fx-text-fill: " + SAFFRON_MAIN + ";");
        pendingCard.getChildren().add(pendingBadge);

        VBox budgetCard = statCard("BUDGET UTILIZATION", "74%", "\uD83D\uDCCA", "rgba(14,140,140,0.12)");
        VBox budgetExtra = new VBox(8);
        budgetExtra.getChildren().add(progressBar(0.74, CONTEXT_TEAL, 8));
        Label budgetFootnote = new Label("\u20B91.2Cr / \u20B91.6Cr Sanctioned");
        budgetFootnote.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.60);");
        budgetExtra.getChildren().add(budgetFootnote);
        budgetCard.getChildren().add(budgetExtra);

        VBox reportsCard = statCard("REPORTS & ANALYTICS", "+12%", "\uD83D\uDCC8", "rgba(124,92,252,0.12)");
        VBox reportsExtra = new VBox(4);
        Label reportsFootnote = new Label("Monthly Progress Insight");
        reportsFootnote.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.60);");
        Label reportsTrend = new Label("\u2197  Growth on track");
        reportsTrend.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + CONTEXT_TEAL + ";");
        reportsExtra.getChildren().addAll(reportsFootnote, reportsTrend);
        reportsCard.getChildren().add(reportsExtra);

        HBox.setHgrow(totalCard, Priority.ALWAYS);
        HBox.setHgrow(pendingCard, Priority.ALWAYS);
        HBox.setHgrow(budgetCard, Priority.ALWAYS);
        HBox.setHgrow(reportsCard, Priority.ALWAYS);
        row.getChildren().addAll(totalCard, pendingCard, budgetCard, reportsCard);
        return row;
    }

    private VBox statCard(String label, String value, String icon, String iconBg) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setStyle(cardStyle(18));
        card.setMinHeight(150);

        HBox top = new HBox();
        top.setAlignment(Pos.CENTER_LEFT);
        Label heading = new Label(label);
        heading.setWrapText(true);
        heading.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.60); -fx-letter-spacing: 0.06em;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16px; -fx-padding: 8; -fx-background-color: " + iconBg + "; -fx-background-radius: 50%;");
        top.getChildren().addAll(heading, spacer, iconLabel);

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 26px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        card.getChildren().addAll(top, valueLabel);
        addHoverLift(card, 18);
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

        addProjectRow(grid, 1, "#PRJ-089", "Village Road Construction", "Main Street", "In Review", SAFFRON_MAIN, "\u20B91,20,000", true);
        grid.add(rowDivider(), 0, 2, 5, 1);

        addProjectRow(grid, 3, "#PRJ-088", "Water Tank Renovation", "Near School Area", "Approved", CONTEXT_TEAL, "\u20B985,000", false);
        grid.add(rowDivider(), 0, 4, 5, 1);

        addProjectRow(grid, 5, "#PRJ-085", "Panchayat Bhavan Repair", "Gram Panchayat Office", "In Review", SAFFRON_MAIN, "\u20B92,50,000", true);

        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(16, 0, 0, 0));
        footer.setStyle("-fx-border-color: rgba(11,61,46,0.08) transparent transparent transparent; -fx-border-width: 1 0 0 0;");
        Label selected = new Label("3 items selected for review");
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

        VBox waterCard = new VBox(10);
        waterCard.setPadding(new Insets(14, 16, 14, 16));
        VBox.setMargin(waterCard, new Insets(0, 20, 0, 20));
        waterCard.setStyle("-fx-background-color: " + rgba(DELAYED_RED, 0.06) + "; -fx-border-color: transparent transparent transparent " + DELAYED_RED + ";" +
                "-fx-border-width: 0 0 0 4; -fx-background-radius: 8;");

        HBox waterRow = new HBox();
        waterRow.setAlignment(Pos.CENTER_LEFT);
        Label waterTitle = new Label("Water Shortage");
        waterTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Region waterSpacer = new Region();
        HBox.setHgrow(waterSpacer, Priority.ALWAYS);
        Label waterTime = new Label("10m ago");
        waterTime.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.55);");
        waterRow.getChildren().addAll(waterTitle, waterSpacer, waterTime);

        Label waterDesc = new Label("Ward 4 main supply line burst. Immediate repair needed.");
        waterDesc.setWrapText(true);
        waterDesc.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-text-fill: rgba(11,61,46,0.70);");

        HBox waterButtons = new HBox(8);
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
        waterButtons.getChildren().addAll(escalate, quickApprove);

        waterCard.getChildren().addAll(waterRow, waterDesc, waterButtons);

        VBox roadCard = new VBox(10);
        roadCard.setPadding(new Insets(14, 16, 14, 16));
        VBox.setMargin(roadCard, new Insets(0, 20, 0, 20));
        roadCard.setStyle("-fx-background-color: " + rgba(SAFFRON_MAIN, 0.08) + "; -fx-border-color: transparent transparent transparent " + SAFFRON_MAIN + ";" +
                "-fx-border-width: 0 0 0 4; -fx-background-radius: 8;");

        HBox roadRow = new HBox();
        roadRow.setAlignment(Pos.CENTER_LEFT);
        Label roadTitle = new Label("Road Blockage");
        roadTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Region roadSpacer = new Region();
        HBox.setHgrow(roadSpacer, Priority.ALWAYS);
        Label roadTime = new Label("1h ago");
        roadTime.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.55);");
        roadRow.getChildren().addAll(roadTitle, roadSpacer, roadTime);

        Label roadDesc = new Label("Fallen tree blocking access to North Village clinic.");
        roadDesc.setWrapText(true);
        roadDesc.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-text-fill: rgba(11,61,46,0.70);");

        Label assignTeam = new Label("Assign Team");
        assignTeam.setPadding(new Insets(7, 14, 7, 14));
        assignTeam.setMaxWidth(Region.USE_PREF_SIZE);
        assignTeam.setStyle("-fx-background-color: rgba(255,255,255,0.75); -fx-text-fill: " + FOREST_DEEP + "; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 12px; -fx-font-weight: 700; -fx-border-color: rgba(11,61,46,0.15); -fx-border-radius: 6;" +
                "-fx-background-radius: 6; -fx-cursor: hand;");

        roadCard.getChildren().addAll(roadRow, roadDesc, assignTeam);

        panel.getChildren().addAll(header, waterCard, roadCard);
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

        addComplaintRow(grid, 1, "#CMP-102", "Water Supply", "Rampur", "Low pressure in Ward 2 for 3 days.", "Oct 24, 2023", "Pending", SAFFRON_MAIN, true);
        grid.add(rowDivider(), 0, 2, 7, 1);

        addComplaintRow(grid, 3, "#CMP-098", "Street Lighting", "Sitapur", "Main road lights flickering near temple.", "Oct 23, 2023", "Resolved", CONTEXT_TEAL, false);

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