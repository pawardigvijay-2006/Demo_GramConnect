package com.tech_fusion.view.sarpanch;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * GramConnect - Sarpanch Login Home (Dashboard) Page
 */
public class SarpanchDashboard extends Application {

    /* ---------- Color palette (from the HTML template) ---------- */
    private static final String FOREST_DEEP   = "#0B3D2E";
    private static final String FOREST_LIGHT  = "#0F4736";
    private static final String SAFFRON_MAIN  = "#E07A1F";
    private static final String CONTEXT_TEAL  = "#0E8C8C";
    private static final String AI_VIOLET     = "#7C5CFC";
    private static final String DELAYED_RED   = "#D94C38";
    // Light green sidebar colors (requested change)
    private static final String SIDEBAR_TOP   = "#CDEBD8";
    private static final String SIDEBAR_MID   = "#Bce3cc";
    private static final String SIDEBAR_BOT   = "#A9D8BD";

    private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

    private static final String BACKGROUND_IMAGE_PATH =
    "C:/Users/Ashish/Downloads/Background File of each Page/Background Image.png";

    /** Shared Stage reference, following the same static-myStage pattern as HomePage.myStage. */
    public static Stage myStage;

    /** The built Dashboard scene, kept so back() can return to it without rebuilding. */
    private Scene dashboardScene;

    @Override
    public void start(Stage stage) {
        myStage = stage;
        dashboardScene = getDashboardScene();

        myStage.setTitle("GramConnect - Sarpanch Connect | Governance Dashboard");
        myStage.setScene(dashboardScene);
        myStage.show();
    }

    /**
     * Builds (or rebuilds) the Dashboard scene and returns it.
     * Public so ProjectTrackerPage's "Dashboard" nav item can jump straight back
     * here via SarpanchDashboard.myStage, the same way ToppingsDetails calls
     * back into PizzaDetails in the demo pattern.
     */
    public Scene getDashboardScene() {
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

        Scene scene = new Scene(root, 1300, 800);
        dashboardScene = scene;
        return scene;
    }

    /** Returns to the Dashboard scene. Called via the backToDashboardAction Runnable. */
    public void back() {
        myStage.setTitle("GramConnect - Sarpanch Connect | Governance Dashboard");
        myStage.setScene(dashboardScene);
    }

    /* ============================================================
     *  SIDEBAR  (Light Green version + Announcements section)
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

        /* ----- Header: avatar + name ----- */
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

        /* ----- Navigation items ----- */
        VBox nav = new VBox(6);
        nav.setPadding(new Insets(16, 12, 16, 12));

        HBox projectTrackerNav = navItem("\uD83D\uDDC2", "Project Tracker", false);
        projectTrackerNav.setOnMouseClicked(e -> {
            System.out.println("Project Tracker clicked");
            ProjectTrackerPage projectTrackerPage = new ProjectTrackerPage();
            Runnable backToDashboardAction = () -> {
                back();
            };

            myStage.setScene(projectTrackerPage.getProjectTrackerScene(backToDashboardAction));
        });

        HBox complaintNav = navItem("\u26A0", "Complaints", false);
        complaintNav.setOnMouseClicked(e -> {
            System.out.println("Complaint Clicked");
            SarpanchComplaintsPage sarpanchComplaintsPage = new SarpanchComplaintsPage();
            Runnable backToDashboardAction = () -> {
                back();
            };

            myStage.setScene(sarpanchComplaintsPage.getComplaintsScene(backToDashboardAction));
        });

        HBox citizenServicesNav = navItem("\uD83D\uDCC4", "Citizen Services", false);
        citizenServicesNav.setOnMouseClicked(e -> {
            System.out.println("Citizen Services clicked");
            CitizenServicesPage citizenServicesPage = new CitizenServicesPage();
            Runnable backToDashboardAction = () -> {
                back();
            };

            myStage.setTitle("GramConnect - Citizen Services");
            myStage.setScene(citizenServicesPage.getCitizenServicesScene(backToDashboardAction));
        });

        HBox announcementsNav = navItem("\uD83D\uDCE2", "Announcements", false);
        announcementsNav.setOnMouseClicked(e -> {
            System.out.println("Announcements clicked");
            AnnouncementsPage announcementsPage = new AnnouncementsPage();
            Runnable backToDashboardAction = () -> {
                back();
            };

            myStage.setTitle("GramConnect - Announcements");
            myStage.setScene(announcementsPage.getAnnouncementsScene(backToDashboardAction));
        });

        nav.getChildren().addAll(
            navItem("\u25A6", "Dashboard", true),
            projectTrackerNav,
            //navItem("\u26A0", "Complaints", false),
            complaintNav,
            citizenServicesNav,
            announcementsNav   
        );
        VBox.setVgrow(nav, Priority.ALWAYS);

        /* ----- CTA + footer links ----- */
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
            // active pill with saffron indicator bar on the left
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
     *  TOP NAVIGATION BAR
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

        // Label brand = new Label("Sarpanch Connect");
        // brand.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        Image projectLogo = new Image("assets/images/ProjectLogo.png");
        ImageView imgView = new ImageView(projectLogo);
        imgView.setFitHeight(50);
        imgView.setFitWidth(60);

        /* Search box */
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
        searchField.setPromptText("Search anything...");
        searchField.setStyle("-fx-background-color: transparent; -fx-font-family: " + FONT_FAMILY + ";" +
            "-fx-font-size: 14px; -fx-text-fill: " + FOREST_DEEP + "; -fx-prompt-text-fill: rgba(11,61,46,0.40);");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBox.getChildren().addAll(searchIcon, searchField);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        /* Notification bell with saffron dot */
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

        /* Profile / language chip */
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

        /* Welcome section */
        VBox welcome = new VBox(6);
        Label welcomeTitle = new Label("Welcome back, Sarpanch");
        welcomeTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label welcomeSub = new Label("Gram Panchayat Dashboard \u2013 Manage projects, budget, complaints and citizen services efficiently.");
        welcomeSub.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.70);");
        welcomeSub.setWrapText(true);
        welcome.getChildren().addAll(welcomeTitle, welcomeSub);

        main.getChildren().addAll(
            welcome,
            buildKpiRow(),
            buildChartsRow(),
            buildRecentProjectsTable()
        );
        return main;
    }

    /* ============================================================
     *  KPI CARDS ROW
     * ============================================================ */
    private HBox buildKpiRow() {
        HBox row = new HBox(24);
        row.setAlignment(Pos.TOP_LEFT);

        VBox kpi1 = kpiCard(FOREST_DEEP, "\uD83D\uDCBC", "TOTAL PROJECTS", "24");
        Label sub1 = new Label("All Village Projects");
        sub1.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.60);");
        kpiBottom(kpi1).getChildren().add(sub1);

        VBox kpi2 = kpiCard(CONTEXT_TEAL, "\u25B6", "ACTIVE PROJECTS", "18");
        VBox bottom2 = kpiBottom(kpi2);
        StackPane pBar = progressBar(0.75, CONTEXT_TEAL, 8);
        Label sub2 = new Label("In Progress");
        sub2.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.60);");
        bottom2.getChildren().addAll(pBar, sub2);

        VBox kpi3 = kpiCard(SAFFRON_MAIN, "\u26A0", "PENDING COMPLAINTS", "7");
        VBox bottom3 = kpiBottom(kpi3);
        Label attention = new Label("\u26A0  Need Attention");
        attention.setPadding(new Insets(5, 12, 5, 12));
        attention.setStyle("-fx-background-color: rgba(224,122,31,0.12); -fx-background-radius: 6;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + SAFFRON_MAIN + ";");
        bottom3.getChildren().add(attention);

        VBox kpi4 = kpiCard(AI_VIOLET, "\u25D4", "BUDGET UTILIZED", "68%");
        VBox bottom4 = kpiBottom(kpi4);
        HBox amount = new HBox(4);
        amount.setAlignment(Pos.CENTER_LEFT);
        amount.setPadding(new Insets(5, 12, 5, 12));
        amount.setMaxWidth(Region.USE_PREF_SIZE);
        amount.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 6;" +
            "-fx-border-color: rgba(255,255,255,0.7); -fx-border-radius: 6;");
        Label amt1 = new Label("\u20B94,52,000");
        amt1.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Label amt2 = new Label(" / \u20B96,65,000");
        amt2.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.60);");
        amount.getChildren().addAll(amt1, amt2);
        bottom4.getChildren().add(amount);

        HBox.setHgrow(kpi1, Priority.ALWAYS);
        HBox.setHgrow(kpi2, Priority.ALWAYS);
        HBox.setHgrow(kpi3, Priority.ALWAYS);
        HBox.setHgrow(kpi4, Priority.ALWAYS);
        row.getChildren().addAll(kpi1, kpi2, kpi3, kpi4);
        return row;
    }

    /** Base KPI card: colored top strip, icon chip + label, big stat number. */
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

    /** Returns the bottom VBox of a KPI card (holds stat number + extra sub-content). */
    private VBox kpiBottom(VBox kpiCard) {
        VBox inner = (VBox) kpiCard.getChildren().get(1);
        return (VBox) inner.getChildren().get(inner.getChildren().size() - 1);
    }

    /* ============================================================
     *  CHARTS ROW (Project Progress + Budget Overview)
     * ============================================================ */
    private HBox buildChartsRow() {
        HBox row = new HBox(24);
        VBox progress = buildProgressCard();
        VBox budget = buildBudgetCard();
        HBox.setHgrow(progress, Priority.ALWAYS);
        HBox.setHgrow(budget, Priority.ALWAYS);
        progress.setPrefWidth(600);
        budget.setPrefWidth(600);
        row.getChildren().addAll(progress, budget);
        return row;
    }

    /* ----- Project Progress Overview (donut chart) ----- */
    private VBox buildProgressCard() {
        VBox card = new VBox(28);
        card.setPadding(new Insets(32));
        card.setStyle(cardStyle(24));

        Label title = new Label("Project Progress Overview");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        HBox body = new HBox(40);
        body.setAlignment(Pos.CENTER_LEFT);

        /* Donut chart drawn on Canvas: 25% green, 50% saffron, 17% empty, 8% red */
        StackPane donut = new StackPane();
        Canvas canvas = new Canvas(192, 192);
        drawDonut(canvas.getGraphicsContext2D());
        VBox center = new VBox(2);
        center.setAlignment(Pos.CENTER);
        Label pct = new Label("75%");
        pct.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 30px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label pctSub = new Label("OVERALL\nCOMPLETION");
        pctSub.setTextAlignment(TextAlignment.CENTER);
        pctSub.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: rgba(11,61,46,0.70); -fx-letter-spacing: 0.08em;");
        center.getChildren().addAll(pct, pctSub);
        donut.getChildren().addAll(canvas, center);

        /* Legend */
        VBox legend = new VBox(14);
        legend.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(legend, Priority.ALWAYS);
        legend.getChildren().addAll(
            legendRow(FOREST_DEEP, "Completed", "6 (25%)", false),
            legendRow(SAFFRON_MAIN, "In Progress", "12 (50%)", false),
            legendRow("rgba(11,61,46,0.20)", "Not Started", "4 (17%)", false),
            legendRow(DELAYED_RED, "Delayed", "2 (8%)", true)
        );

        body.getChildren().addAll(donut, legend);
        card.getChildren().addAll(title, body);
        addHoverLift(card, 24);
        return card;
    }

    private void drawDonut(GraphicsContext g) {
        double cx = 96, cy = 96, r = 76, stroke = 30;
        g.setLineWidth(stroke);
        g.setLineCap(StrokeLineCap.BUTT);
        // Track (Not Started - light)
        g.setStroke(Color.web(FOREST_DEEP, 0.07));
        g.strokeArc(cx - r, cy - r, 2 * r, 2 * r, 0, 360, ArcType.OPEN);
        // Completed 25% (green) - start at top (90deg), clockwise
        g.setStroke(Color.web(FOREST_DEEP));
        g.strokeArc(cx - r, cy - r, 2 * r, 2 * r, 90, -90, ArcType.OPEN);
        // In Progress 50% (saffron)
        g.setStroke(Color.web(SAFFRON_MAIN, 0.95));
        g.strokeArc(cx - r, cy - r, 2 * r, 2 * r, 0, -180, ArcType.OPEN);
        // Delayed 8% (red)
        g.setStroke(Color.web(DELAYED_RED, 0.95));
        g.strokeArc(cx - r, cy - r, 2 * r, 2 * r, 180, -29, ArcType.OPEN);
    }

    private HBox legendRow(String color, String label, String value, boolean delayed) {
        HBox rowBox = new HBox(12);
        rowBox.setAlignment(Pos.CENTER_LEFT);
        rowBox.setPadding(new Insets(8, 10, 8, 10));
        if (delayed) {
            rowBox.setStyle("-fx-background-color: rgba(217,76,56,0.10); -fx-background-radius: 8;" +
                "-fx-border-color: rgba(217,76,56,0.20); -fx-border-radius: 8;");
        }
        Circle dotC = new Circle(7);
        dotC.setStyle("-fx-fill: " + color + ";");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 600;" +
            "-fx-text-fill: " + (delayed ? DELAYED_RED : FOREST_DEEP) + ";");
        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);
        Label val = new Label(value);
        val.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800;" +
            "-fx-text-fill: " + (delayed ? DELAYED_RED : FOREST_DEEP) + ";");
        rowBox.getChildren().addAll(dotC, lbl, grow, val);
        return rowBox;
    }

    /* ----- Budget Overview card ----- */
    private VBox buildBudgetCard() {
        VBox card = new VBox(28);
        card.setPadding(new Insets(32));
        card.setStyle(cardStyle(24));

        HBox head = new HBox();
        head.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Budget Overview");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);
        Label viewReport = new Label("View Report \u2192");
        viewReport.setPadding(new Insets(6, 16, 6, 16));
        viewReport.setStyle("-fx-background-color: rgba(14,140,140,0.10); -fx-background-radius: 999;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");
        head.getChildren().addAll(title, grow, viewReport);

        /* Total budget / Utilized boxes */
        HBox statBoxes = new HBox(24);
        VBox totalBox = whiteBox();
        Label tLbl = new Label("TOTAL BUDGET");
        tLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: rgba(11,61,46,0.60); -fx-letter-spacing: 0.08em;");
        HBox tRow = new HBox(12);
        tRow.setAlignment(Pos.CENTER_LEFT);
        StackPane rupeeChip = new StackPane();
        rupeeChip.setPrefSize(40, 40);
        rupeeChip.setMinSize(40, 40);
        rupeeChip.setStyle("-fx-background-color: rgba(11,61,46,0.10); -fx-background-radius: 999;");
        Label rupee = new Label("\u20B9");
        rupee.setStyle("-fx-font-size: 18px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");
        rupeeChip.getChildren().add(rupee);
        Label tVal = new Label("\u20B96,65,000");
        tVal.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        tRow.getChildren().addAll(rupeeChip, tVal);
        totalBox.getChildren().addAll(tLbl, tRow);

        VBox utilBox = whiteBox();
        Label uLbl = new Label("UTILIZED");
        uLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: rgba(11,61,46,0.60); -fx-letter-spacing: 0.08em;");
        Label uVal = new Label("\u20B94,52,000");
        uVal.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: " + CONTEXT_TEAL + ";");
        Label uPct = new Label("68% of total");
        uPct.setPadding(new Insets(2, 8, 2, 8));
        uPct.setMaxWidth(Region.USE_PREF_SIZE);
        uPct.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 4;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.60);");
        utilBox.getChildren().addAll(uLbl, uVal, uPct);

        HBox.setHgrow(totalBox, Priority.ALWAYS);
        HBox.setHgrow(utilBox, Priority.ALWAYS);
        statBoxes.getChildren().addAll(totalBox, utilBox);

        /* Category progress bars */
        VBox bars = new VBox(24);
        bars.getChildren().addAll(
            budgetBar(CONTEXT_TEAL, "Infrastructure", "\u20B92,50,000 / \u20B93,00,000", 0.83),
            budgetBar(AI_VIOLET, "Welfare & Services", "\u20B92,02,000 / \u20B93,65,000", 0.55)
        );

        card.getChildren().addAll(head, statBoxes, bars);
        addHoverLift(card, 24);
        return card;
    }

    private VBox whiteBox() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 12;" +
            "-fx-border-color: rgba(255,255,255,0.7); -fx-border-radius: 12; -fx-border-width: 1;");
        return box;
    }

    private VBox budgetBar(String color, String label, String amounts, double fraction) {
        VBox box = new VBox(8);
        HBox head = new HBox(8);
        head.setAlignment(Pos.CENTER_LEFT);
        Circle dotC = new Circle(4, Color.web(color));
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");
        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);
        Label amt = new Label(amounts);
        amt.setPadding(new Insets(2, 8, 2, 8));
        amt.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 6;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.70);");
        head.getChildren().addAll(dotC, lbl, grow, amt);

        box.getChildren().addAll(head, progressBar(fraction, color, 10));
        return box;
    }

    /** Rounded progress bar: track + colored fill sized by fraction. */
    private StackPane progressBar(double fraction, String color, double height) {
        StackPane track = new StackPane();
        track.setPrefHeight(height);
        track.setMinHeight(height);
        track.setMaxWidth(Double.MAX_VALUE);
        track.setStyle("-fx-background-color: rgba(11,61,46,0.06); -fx-background-radius: 999;");
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
     *  RECENT PROJECTS STATUS TABLE
     * ============================================================ */
    private VBox buildRecentProjectsTable() {
        VBox card = new VBox(24);
        card.setPadding(new Insets(32));
        card.setStyle(cardStyle(24));

        /* Header row */
        HBox head = new HBox(12);
        head.setAlignment(Pos.CENTER_LEFT);
        head.setPadding(new Insets(0, 0, 16, 0));
        head.setStyle("-fx-border-color: transparent transparent rgba(11,61,46,0.05) transparent; -fx-border-width: 0 0 1 0;");
        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(40, 40);
        iconChip.setMinSize(40, 40);
        iconChip.setStyle("-fx-background-color: rgba(11,61,46,0.10); -fx-background-radius: 999;");
        Label vIcon = new Label("\u2714");
        vIcon.setStyle("-fx-font-size: 16px; -fx-text-fill: " + FOREST_DEEP + ";");
        iconChip.getChildren().add(vIcon);
        Label title = new Label("Recent Projects Status");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);
        Label viewAll = new Label("View All Projects \u2192");
        viewAll.setPadding(new Insets(8, 16, 8, 16));
        viewAll.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 999;" +
            "-fx-border-color: white; -fx-border-radius: 999;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;");
        head.getChildren().addAll(iconChip, title, grow, viewAll);

        /* Column headers */
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(0);
        double[] widths = {30, 17, 20, 15, 18};   // percentage widths
        for (double w : widths) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(w);
            grid.getColumnConstraints().add(cc);
        }
        String[] headers = {"PROJECT NAME", "VILLAGE", "DEPARTMENT", "BUDGET", "STATUS"};
        for (int i = 0; i < headers.length; i++) {
            Label h = new Label(headers[i]);
            h.setPadding(new Insets(0, 8, 14, 8));
            h.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.60); -fx-letter-spacing: 0.08em;");
            if (i == 4) GridPane.setHalignment(h, javafx.geometry.HPos.RIGHT);
            grid.add(h, i, 0);
        }

        /* Data rows */
        addProjectRow(grid, 1, "Ward 4 Road Metaling",   "Main Street",           "Rural Development", "\u20B91,20,000", "Approved",    FOREST_DEEP);
        addProjectRow(grid, 2, "Water Tank Renovation",  "Near School Area",      "Water Supply",      "\u20B985,000",   "In Progress", SAFFRON_MAIN);
        addProjectRow(grid, 3, "Primary School Repair",  "Gram Panchayat Office", "Infrastructure",    "\u20B92,50,000", "Delayed",     DELAYED_RED);

        card.getChildren().addAll(head, grid);
        addHoverLift(card, 24);
        return card;
    }

    private void addProjectRow(GridPane grid, int row, String project, String village,
                               String dept, String budget, String status, String statusColor) {
        /* Project name with thumbnail placeholder */
        HBox nameCell = new HBox(14);
        nameCell.setAlignment(Pos.CENTER_LEFT);
        nameCell.setPadding(new Insets(16, 8, 16, 8));
        StackPane thumb = new StackPane();
        thumb.setPrefSize(48, 48);
        thumb.setMinSize(48, 48);
        thumb.setStyle("-fx-background-color: rgba(11,61,46,0.08); -fx-background-radius: 12;" +
            "-fx-border-color: rgba(255,255,255,0.7); -fx-border-radius: 12;");
        Label thumbIcon = new Label("\uD83C\uDFD7");
        thumbIcon.setStyle("-fx-font-size: 18px;");
        thumb.getChildren().add(thumbIcon);
        Label pName = new Label(project);
        pName.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        nameCell.getChildren().addAll(thumb, pName);

        Label vLbl = new Label(village);
        vLbl.setPadding(new Insets(16, 8, 16, 8));
        vLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.70);");

        Label dLbl = new Label(dept);
        dLbl.setPadding(new Insets(4, 12, 4, 12));
        dLbl.setStyle("-fx-background-color: rgba(11,61,46,0.05); -fx-background-radius: 6;" +
            "-fx-border-color: rgba(11,61,46,0.10); -fx-border-radius: 6;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.80);");
        HBox dCell = new HBox(dLbl);
        dCell.setAlignment(Pos.CENTER_LEFT);
        dCell.setPadding(new Insets(16, 8, 16, 8));

        Label bLbl = new Label(budget);
        bLbl.setPadding(new Insets(16, 8, 16, 8));
        bLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        /* Status pill */
        HBox pill = new HBox(6);
        pill.setAlignment(Pos.CENTER);
        pill.setPadding(new Insets(6, 14, 6, 14));
        pill.setMaxWidth(Region.USE_PREF_SIZE);
        pill.setStyle("-fx-background-color: " + statusColor + "; -fx-background-radius: 999;" +
            "-fx-effect: dropshadow(gaussian, " + rgba(statusColor, 0.25) + ", 6, 0.2, 0, 2);");
        Circle pillDot = new Circle(3, Color.WHITE);
        Label pillLbl = new Label(status);
        pillLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: white;");
        pill.getChildren().addAll(pillDot, pillLbl);
        HBox sCell = new HBox(pill);
        sCell.setAlignment(Pos.CENTER_RIGHT);
        sCell.setPadding(new Insets(16, 8, 16, 8));

        grid.add(nameCell, 0, row);
        grid.add(vLbl, 1, row);
        grid.add(dCell, 2, row);
        grid.add(bLbl, 3, row);
        grid.add(sCell, 4, row);

        /* thin row separator under each data row (except the last) */
        if (row < 3) {
            nameCell.setStyle("-fx-border-color: transparent transparent rgba(11,61,46,0.05) transparent; -fx-border-width: 0 0 1 0;");
        }
    }

    /* ============================================================
     *  HELPERS
     * ============================================================ */

    /** Glass-panel style shared by all cards. */
    private String cardStyle(int radius) {
        return "-fx-background-color: rgba(255,255,255,0.88);" +
               "-fx-background-radius: " + radius + ";" +
               "-fx-border-color: rgba(255,255,255,0.5);" +
               "-fx-border-radius: " + radius + ";" +
               "-fx-border-width: 1;" +
               "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.06), 16, 0.1, 0, 4);";
    }

    /** Hover lift effect matching the HTML .stat-card-shadow:hover. */
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

    /** Convert #RRGGBB hex to rgba(r,g,b,a) CSS string. */
    private String rgba(String hex, double alpha) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
    }

}