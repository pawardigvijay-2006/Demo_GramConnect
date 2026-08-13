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

    private static final String BACKGROUND_IMAGE_PATH =
            "C:/Users/Ashish/Downloads/Background File of each Page/Background Image.png";

    private Label selectedNavItem;

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
                buildAnalyticsMidSection(),
                buildReportsPanel()
        );
        return main;
    }

    private HBox buildTitleRow() {
        VBox text = new VBox(6);
        Label title = new Label("Reports & Block Analytics");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label subtitle = new Label("Showing data for: All Villages (Block)");
        subtitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.60);");
        text.getChildren().addAll(title, subtitle);

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
     * ============================================================ */
    private HBox buildStatCardsRow() {
        HBox row = new HBox(20);

        VBox budgetCard = statCard("TOTAL BLOCK BUDGET", "\u20B916.8Cr", "\uD83D\uDCB0", "rgba(11,61,46,0.10)");
        VBox budgetExtra = new VBox(8);
        budgetExtra.getChildren().add(progressBar(0.74, CONTEXT_TEAL, 8));
        Label budgetFootnote = new Label("74% Utilized     \u20B912.4Cr");
        budgetFootnote.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.60);");
        budgetExtra.getChildren().add(budgetFootnote);
        budgetCard.getChildren().add(budgetExtra);

        VBox successCard = statCard("PROJECT SUCCESS RATE", "88%", "\u2705", "rgba(14,140,140,0.12)");
        Label successFooter = new Label("\u2197  5% from last quarter");
        successFooter.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + CONTEXT_TEAL + ";");
        successCard.getChildren().add(successFooter);

        VBox approvalCard = statCard("AVG. APPROVAL TIME", "4.2 Days", "\uD83D\uDCCB", "rgba(124,92,252,0.12)");
        Label approvalFooter = new Label("Target: <5 Days");
        approvalFooter.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.60);");
        approvalCard.getChildren().add(approvalFooter);

        VBox grievanceCard = statCard("ACTIVE GRIEVANCES", "12", "\u26A0", "rgba(217,76,56,0.12)");
        VBox grievanceValueWrap = (VBox) grievanceCard.getChildren().get(1);
        Label grievanceValue = (Label) grievanceValueWrap.getChildren().get(0);
        grievanceValue.setStyle(
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 26px; -fx-font-weight: 900; -fx-text-fill: " + DELAYED_RED + ";");
        VBox grievanceExtra = new VBox(4);
        Label pendingLbl = new Label("Pending");
        pendingLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + DELAYED_RED + ";");
        Label resolutionLbl = new Label("92% Resolution Rate");
        resolutionLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-text-fill: rgba(11,61,46,0.60);");
        grievanceExtra.getChildren().addAll(pendingLbl, resolutionLbl);
        grievanceCard.getChildren().add(grievanceExtra);

        HBox.setHgrow(budgetCard, Priority.ALWAYS);
        HBox.setHgrow(successCard, Priority.ALWAYS);
        HBox.setHgrow(approvalCard, Priority.ALWAYS);
        HBox.setHgrow(grievanceCard, Priority.ALWAYS);
        row.getChildren().addAll(budgetCard, successCard, approvalCard, grievanceCard);
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

        VBox valueWrap = new VBox(valueLabel);
        card.getChildren().addAll(top, valueWrap);
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

        String[] villages = {"Sitapur", "Rampur", "Kondli", "Main St.", "North Vill.", "East Ward"};
        double[] utilization = {0.62, 0.81, 0.45, 0.90, 0.58, 0.70};

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

        HBox bars = new HBox(24);
        bars.setAlignment(Pos.BOTTOM_CENTER);
        bars.setPadding(new Insets(0, 8, 0, 8));
        for (int i = 0; i < villages.length; i++) {
            VBox barWrap = new VBox();
            barWrap.setAlignment(Pos.BOTTOM_CENTER);
            Region bar = new Region();
            bar.setPrefWidth(34);
            bar.setStyle("-fx-background-color: linear-gradient(to top, " + FOREST_DEEP + ", " + CONTEXT_TEAL + ");" +
                    "-fx-background-radius: 6 6 0 0;");
            double h = utilization[i] * 200;
            bar.setPrefHeight(h);
            bar.setMinHeight(h);
            barWrap.getChildren().add(bar);
            barWrap.setPrefHeight(220);
            HBox.setHgrow(barWrap, Priority.ALWAYS);
            bars.getChildren().add(barWrap);
        }

        gridWrap.getChildren().addAll(gridLines, bars);
        chartArea.getChildren().addAll(yAxis, gridWrap);

        HBox xAxis = new HBox();
        xAxis.setPadding(new Insets(4, 4, 0, 40));
        for (String v : villages) {
            Label l = new Label(v);
            l.setMaxWidth(Double.MAX_VALUE);
            l.setAlignment(Pos.CENTER);
            l.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: rgba(11,61,46,0.60);");
            HBox.setHgrow(l, Priority.ALWAYS);
            xAxis.getChildren().add(l);
        }

        panel.getChildren().addAll(header, chartArea, xAxis);
        addHoverLift(panel, 24);
        return panel;
    }

    private VBox buildProjectDistributionPanel() {
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(24));

        Label title = new Label("Project Distribution");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        StackPane donut = buildDonutChart();
        StackPane donutWrap = new StackPane(donut);
        donutWrap.setAlignment(Pos.CENTER);
        donutWrap.setPadding(new Insets(8, 0, 8, 0));

        VBox legend = new VBox(10);
        legend.getChildren().addAll(
                legendRow(FOREST_DEEP, "Infrastructure", "45%"),
                legendRow(CONTEXT_TEAL, "Water Supply", "30%"),
                legendRow("#8FCB9E", "Education", "15%"),
                legendRow("#CDEBD8", "Other", "10%")
        );

        panel.getChildren().addAll(title, donutWrap, legend);
        addHoverLift(panel, 24);
        return panel;
    }

    private StackPane buildDonutChart() {
        StackPane stack = new StackPane();
        double size = 190;
        double radius = size / 2;

        String[] colors = {FOREST_DEEP, CONTEXT_TEAL, "#8FCB9E", "#CDEBD8"};
        double[] fractions = {0.45, 0.30, 0.15, 0.10};

        double startAngle = 90;
        for (int i = 0; i < fractions.length; i++) {
            double length = fractions[i] * 360;
            Arc arc = new Arc(radius, radius, radius, radius, startAngle, -length);
            arc.setType(ArcType.ROUND);
            arc.setFill(Color.web(colors[i]));
            StackPane arcHolder = new StackPane(arc);
            arcHolder.setPrefSize(size, size);
            arcHolder.setMaxSize(size, size);
            stack.getChildren().add(arcHolder);
            startAngle -= length;
        }

        Circle hole = new Circle(radius * 0.58);
        hole.setFill(Color.web("#FFFFFF", 0.92));
        stack.getChildren().add(hole);

        VBox centerText = new VBox(2);
        centerText.setAlignment(Pos.CENTER);
        Label totalValue = new Label("142");
        totalValue.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 26px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label totalLabel = new Label("Total Projects");
        totalLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: rgba(11,61,46,0.60);");
        centerText.getChildren().addAll(totalValue, totalLabel);
        stack.getChildren().add(centerText);

        stack.setPrefSize(size, size);
        return stack;
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

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(6);
        grid.setPadding(new Insets(6, 0, 0, 0));
        grid.getColumnConstraints().addAll(pct(40), pct(15), pct(25), pct(20));

        grid.add(headerCell("REPORT NAME"), 0, 0);
        grid.add(headerCell("TYPE"), 1, 0);
        grid.add(headerCell("GENERATED ON"), 2, 0);
        grid.add(headerCell("ACTIONS"), 3, 0);

        addReportRow(grid, 1, "Q3 Block Progress Report", "PDF", "Oct 24, 2023");
        grid.add(rowDivider(), 0, 2, 4, 1);

        addReportRow(grid, 3, "Village Budget Summary", "XLSX", "Oct 20, 2023");
        grid.add(rowDivider(), 0, 4, 4, 1);

        addReportRow(grid, 5, "Grievance Redressal Log", "PDF", "Oct 15, 2023");

        panel.getChildren().addAll(header, grid);
        addHoverLift(panel, 24);
        return panel;
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