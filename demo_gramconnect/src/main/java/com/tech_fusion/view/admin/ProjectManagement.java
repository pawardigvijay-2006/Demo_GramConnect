package com.tech_fusion.view.admin;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
 * GramConnect - Project Management Page
 * ------------------------------------------------------------------
 * Same glass / forest-saffron template as {@link Dashboard} and
 * {@link BudgetManagment}. Navigates through the shared
 * {@code Dashboard.myStage} using Runnable callbacks.
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
            "C:/Users/Ashish/Downloads/Background File of each Page/Background Image.png";

    private Label selectedNavItem;

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
        Runnable toDashboard = () -> {
            Dashboard page = new Dashboard();
            Dashboard.myStage.setScene(page.getBDODashboardScene() );
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
                buildApprovedPendingSection(),
                buildInventoryPanel()
        );
        return main;
    }

    private HBox buildTitleRow() {
        VBox text = new VBox(6);
        Label title = new Label("Project Management");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        HBox subtitleRow = new HBox(4);
        Label subtitle = new Label("Showing data for:");
        subtitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.60);");
        Label subtitleStrong = new Label("All Villages (Block)");
        subtitleStrong.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        subtitleRow.getChildren().addAll(subtitle, subtitleStrong);
        text.getChildren().addAll(title, subtitleRow);

        Label export = new Label("\u2B07  Export Report");
        export.setPadding(new Insets(12, 18, 12, 18));
        export.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-border-color: white; -fx-border-radius: 10;" +
                "-fx-background-radius: 10; -fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px;" +
                "-fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;");

        Label createProject = new Label("+  Create Project");
        createProject.setPadding(new Insets(12, 18, 12, 18));
        createProject.setStyle("-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
                "-fx-text-fill: white; -fx-background-radius: 10; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px; -fx-font-weight: 700; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.35), 10, 0.1, 0, 4);");

        HBox actions = new HBox(12, export, createProject);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerRow = new HBox(20, text, spacer, actions);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        return headerRow;
    }

    private HBox buildStatCardsRow() {
        HBox row = new HBox(20);

        VBox totalCard = statCard("TOTAL PROJECTS", "24", "\uD83D\uDCBC", "rgba(11,61,46,0.10)");
        Label totalFooter = new Label("All village projects");
        totalFooter.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.60);");
        totalCard.getChildren().add(totalFooter);

        VBox activeCard = statCard("ACTIVE PROJECTS", "18", "\u25B6", "rgba(14,140,140,0.12)");
        VBox activeRow = new VBox(6);
        activeRow.getChildren().add(progressBar(0.75, CONTEXT_TEAL, 8));
        Label activeFooter = new Label("75% currently in progress");
        activeFooter.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.60);");
        activeRow.getChildren().add(activeFooter);
        activeCard.getChildren().add(activeRow);

        VBox completedCard = statCard("COMPLETED", "6", "\u2705", "rgba(124,92,252,0.12)");
        Label completedFooter = new Label("25% of all projects");
        completedFooter.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.60);");
        completedCard.getChildren().add(completedFooter);

        VBox delayedCard = statCard("DELAYED PROJECTS", "2", "\u26A0", "rgba(224,122,31,0.12)");
        Label delayedFooter = new Label("Requires action this month");
        delayedFooter.setPadding(new Insets(6, 10, 6, 10));
        delayedFooter.setMaxWidth(Region.USE_PREF_SIZE);
        delayedFooter.setStyle("-fx-background-color: rgba(224,122,31,0.14); -fx-background-radius: 6;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 700; -fx-text-fill: " + SAFFRON_MAIN + ";");
        delayedCard.getChildren().add(delayedFooter);

        HBox.setHgrow(totalCard, Priority.ALWAYS);
        HBox.setHgrow(activeCard, Priority.ALWAYS);
        HBox.setHgrow(completedCard, Priority.ALWAYS);
        HBox.setHgrow(delayedCard, Priority.ALWAYS);
        row.getChildren().addAll(totalCard, activeCard, completedCard, delayedCard);
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
     *  APPROVED PROJECTS  +  PENDING APPROVAL
     *  (replaces the old Department-wise Progress Overview section)
     * ============================================================ */
    private HBox buildApprovedPendingSection() {
        HBox row = new HBox(20);

        VBox approved = buildApprovedProjectsPanel();
        VBox pending = buildPendingApprovalPanel();

        HBox.setHgrow(approved, Priority.ALWAYS);
        approved.setPrefWidth(900);
        pending.setPrefWidth(420);
        pending.setMinWidth(360);

        row.getChildren().addAll(approved, pending);
        return row;
    }

    /* ---------------- Approved Projects ---------------- */
    private VBox buildApprovedProjectsPanel() {
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

        addApprovedRow(grid, 1, "Village Road Construction", "#PRJ-089", "Main Street", "\u20B91,20,000", "24 May 2025");
        addApprovedRow(grid, 2, "Water Tank Renovation", "#PRJ-088", "Near School Area", "\u20B985,000", "22 May 2025");
        addApprovedRow(grid, 3, "Panchayat Bhavan Repair", "#PRJ-085", "Gram Panchayat Office", "\u20B92,50,000", "20 May 2025");

        panel.getChildren().addAll(header, grid);
        addHoverLift(panel, 24);
        return panel;
    }

    private void addApprovedRow(GridPane grid, int row, String projectName, String projectId, String village,
                                 String budget, String date) {
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
        Label nameLabel = new Label(projectName);
        nameLabel.setWrapText(true);
        nameLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Label idLabel = new Label(projectId);
        idLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.55);");
        projectText.getChildren().addAll(nameLabel, idLabel);

        projectCell.getChildren().addAll(thumb, projectText);

        Label villageLabel = new Label(village);
        villageLabel.setWrapText(true);
        villageLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");

        Label budgetLabel = new Label(budget);
        budgetLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");

        Label dateLabel = new Label(date);
        dateLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_LEFT);
        Label viewIcon = new Label("\uD83D\uDC41");
        viewIcon.setStyle("-fx-font-size: 14px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");
        Label editIcon = new Label("\uD83D\uDCDD");
        editIcon.setStyle("-fx-font-size: 14px; -fx-text-fill: " + SAFFRON_MAIN + "; -fx-cursor: hand;");
        actions.getChildren().addAll(viewIcon, editIcon);

        grid.add(projectCell, 0, row);
        grid.add(villageLabel, 1, row);
        grid.add(budgetLabel, 2, row);
        grid.add(dateLabel, 3, row);
        grid.add(actions, 4, row);
    }

    /* ---------------- Pending Approval ---------------- */
    private VBox buildPendingApprovalPanel() {
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
        Label countLabel = new Label("3");
        countLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        countBadge.getChildren().add(countLabel);

        header.getChildren().addAll(icon, title, spacer, countBadge);

        VBox list = new VBox(14);
        list.getChildren().addAll(
                pendingApprovalCard("Community Hall Construction", "HIGH PRIORITY", DELAYED_RED,
                        "Dept: Rural Development | #PRJ-092", "\u20B95,50,000"),
                pendingApprovalCard("Solar Street Lights", "MEDIUM", SAFFRON_MAIN,
                        "Dept: Energy | #PRJ-095", "\u20B91,20,000"),
                pendingApprovalCard("Library Construction", "NORMAL", CONTEXT_TEAL,
                        "Dept: Education | #PRJ-098", "\u20B93,00,000")
        );

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

    private VBox pendingApprovalCard(String projectName, String priorityText, String priorityColor,
                                      String deptLine, String budget) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: rgba(240,244,242,0.6); -fx-background-radius: 14;" +
                "-fx-border-color: rgba(11,61,46,0.08); -fx-border-radius: 14; -fx-border-width: 1;");

        HBox titleRow = new HBox(8);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label(projectName);
        nameLabel.setWrapText(true);
        nameLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label priorityBadge = new Label(priorityText);
        priorityBadge.setPadding(new Insets(3, 8, 3, 8));
        priorityBadge.setMaxWidth(Region.USE_PREF_SIZE);
        priorityBadge.setStyle("-fx-background-color: " + rgba(priorityColor, 0.14) + "; -fx-background-radius: 999;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 10.5px; -fx-font-weight: 800; -fx-text-fill: " + priorityColor + ";");
        titleRow.getChildren().addAll(nameLabel, spacer, priorityBadge);

        Label deptLabel = new Label(deptLine);
        deptLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.60);");

        HBox bottomRow = new HBox(10);
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        Label budgetLabel = new Label(budget);
        budgetLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 15px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        StackPane editBtn = new StackPane();
        editBtn.setPrefSize(34, 34);
        editBtn.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 8;" +
                "-fx-border-color: rgba(11,61,46,0.10); -fx-border-radius: 8; -fx-cursor: hand;");
        Label editIcon = new Label("\uD83D\uDCDD");
        editIcon.setStyle("-fx-font-size: 13px;");
        editBtn.getChildren().add(editIcon);

        Label approveBtn = new Label("Approve");
        approveBtn.setPadding(new Insets(9, 18, 9, 18));
        approveBtn.setStyle("-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
                "-fx-text-fill: white; -fx-background-radius: 8; -fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 12.5px; -fx-font-weight: 800; -fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.30), 8, 0.1, 0, 3);");

        bottomRow.getChildren().addAll(budgetLabel, bottomSpacer, editBtn, approveBtn);

        card.getChildren().addAll(titleRow, deptLabel, bottomRow);
        return card;
    }

    /* ============================================================
     *  PROJECT INVENTORY TABLE
     * ============================================================ */
    private VBox buildInventoryPanel() {
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

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

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(16);
        grid.setPadding(new Insets(6, 0, 0, 0));
        grid.getColumnConstraints().addAll(pct(28), pct(16), pct(18), pct(14), pct(14), pct(10));

        addInventoryHeader(grid);
        addInventoryRow(grid, 1, "Ward 4 Road Metaling", "\u0930\u093E\u092E\u092A\u0941\u0930 (Rampur)", "Rural Development", "\u20B91,20,000", "Approved", FOREST_DEEP, "12 Oct 2023");
        addInventoryRow(grid, 2, "Water Tank Renovation", "\u0938\u0940\u0924\u093E\u092A\u0941\u0930 (Sitapur)", "Water Supply", "\u20B985,000", "In Progress", SAFFRON_MAIN, "05 Sep 2023");
        addInventoryRow(grid, 3, "Primary School Repair", "\u092E\u093E\u0927\u0935\u092A\u0941\u0930 (Madhavpur)", "Infrastructure", "\u20B92,50,000", "Delayed", DELAYED_RED, "28 Aug 2023");

        panel.getChildren().addAll(header, grid);
        addHoverLift(panel, 24);
        return panel;
    }

    private void addInventoryHeader(GridPane grid) {
        grid.add(headerCell("PROJECT NAME"), 0, 0);
        grid.add(headerCell("VILLAGE"), 1, 0);
        grid.add(headerCell("DEPARTMENT"), 2, 0);
        grid.add(headerCell("BUDGET"), 3, 0);
        grid.add(headerCell("STATUS"), 4, 0);
        grid.add(headerCell("ACTION"), 5, 0);
    }

    private void addInventoryRow(GridPane grid, int row, String project, String village, String department,
                                  String budget, String status, String statusColor, String lastDate) {
        Label projectLabel = new Label(project);
        projectLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        Label villageLabel = new Label(village);
        villageLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");
        Label departmentLabel = new Label(department);
        departmentLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.75);");
        Label budgetLabel = new Label(budget);
        budgetLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");
        Label statusLabel = new Label(status);
        statusLabel.setPadding(new Insets(4, 10, 4, 10));
        statusLabel.setMaxWidth(Region.USE_PREF_SIZE);
        statusLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + statusColor + "; -fx-background-color: " + rgba(statusColor, 0.12) + "; -fx-background-radius: 999;");
        Label actionLabel = new Label("\uD83D\uDC41");
        actionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");

        grid.add(projectLabel, 0, row);
        grid.add(villageLabel, 1, row);
        grid.add(departmentLabel, 2, row);
        grid.add(budgetLabel, 3, row);
        grid.add(statusLabel, 4, row);
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