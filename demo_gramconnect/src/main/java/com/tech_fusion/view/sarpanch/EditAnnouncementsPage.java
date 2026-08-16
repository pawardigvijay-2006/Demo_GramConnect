package com.tech_fusion.view.sarpanch;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;



public class EditAnnouncementsPage {

    /* ---------- Color palette (kept identical to the other pages) ---------- */
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
        "/assets/images/BackgroundImage.png";

    /** Navigates back to AnnouncementsPage (passed in from AnnouncementsPage). */
    private Runnable backToAnnouncementsAction;
    /** Navigates back to the Dashboard (threaded through so the sidebar still works here). */
    private Runnable backToDashboardAction;

    private static final String[][] CATEGORY_ACCENTS = {
        {"General", FOREST_DEEP},
        {"Gram Sabha", AI_VIOLET},
        {"Water Supply", CONTEXT_TEAL},
        {"Health", "#2E9E5B"},
        {"Infrastructure", SAFFRON_MAIN},
        {"Emergency", DELAYED_RED}
    };

    private static final String[][] PRIORITY_ACCENTS = {
        {"Low", CONTEXT_TEAL},
        {"Normal", FOREST_DEEP},
        {"High", SAFFRON_MAIN},
        {"Urgent", DELAYED_RED}
    };

    /** The in-memory list of announcements shown on this page (mock data). */
    private final ObservableList<AnnouncementItem> announcements = FXCollections.observableArrayList();

    /** Container the announcement cards are rendered into; refreshed on every edit/delete. */
    private VBox listContainer;
    private Label countLabel;
    private Label publishedStatLabel;
    private Label draftStatLabel;

    /** Which card (if any) currently has its inline edit form open. */
    private AnnouncementItem currentlyEditing = null;
    /** Which card (if any) is currently in the "confirm delete" state. */
    private AnnouncementItem pendingDelete = null;

    /* ============================================================
     *  SIMPLE DATA MODEL
     * ============================================================ */
    private static class AnnouncementItem {
        String title;
        String message;
        String category;
        String categoryAccent;
        String priority;
        String priorityAccent;
        String date;
        String status; // "Published" or "Draft"

        AnnouncementItem(String title, String message, String category, String categoryAccent,
                          String priority, String priorityAccent, String date, String status) {
            this.title = title;
            this.message = message;
            this.category = category;
            this.categoryAccent = categoryAccent;
            this.priority = priority;
            this.priorityAccent = priorityAccent;
            this.date = date;
            this.status = status;
        }
    }

    private void seedMockData() {
        announcements.addAll(
            new AnnouncementItem(
                "Water Supply Interruption - Ward 2",
                "Water supply to Ward 2 will be interrupted on 18th August from 9 AM to 2 PM for pipeline repair work near the main junction.",
                "Water Supply", CONTEXT_TEAL, "High", SAFFRON_MAIN, "2026-08-18", "Published"),
            new AnnouncementItem(
                "Free Health Check-up Camp",
                "A free health check-up camp will be organised at the Primary Health Centre on 22nd August. All senior citizens are encouraged to attend.",
                "Health", "#2E9E5B", "Normal", FOREST_DEEP, "2026-08-22", "Published"),
            new AnnouncementItem(
                "Road Widening Work Update",
                "Road widening work on the main village road is progressing well and is expected to be completed by the end of this month.",
                "Infrastructure", SAFFRON_MAIN, "Normal", FOREST_DEEP, "2026-08-14", "Draft"),
            new AnnouncementItem(
                "Village Cleanliness Drive",
                "Join the village cleanliness drive this Sunday morning starting from the Gram Panchayat office. Cleaning tools will be provided.",
                "General", FOREST_DEEP, "Low", CONTEXT_TEAL, "2026-08-16", "Published"),
            new AnnouncementItem(
                "Heavy Rainfall Alert",
                "The district administration has issued a heavy rainfall alert for the next 48 hours. Villagers near the river bank are advised to stay cautious.",
                "Emergency", DELAYED_RED, "Urgent", DELAYED_RED, "2026-08-15", "Draft")
        );
    }

    /**
     * Builds the Edit Announcements scene and returns it.
     */
    public Scene getEditAnnouncementsScene(Runnable backToAnnouncementsAction, Runnable backToDashboardAction) {
        this.backToAnnouncementsAction = backToAnnouncementsAction;
        this.backToDashboardAction = backToDashboardAction;

        if (announcements.isEmpty()) {
            seedMockData();
        }

        BorderPane root = new BorderPane();
        Image backgroundImage = new Image(BACKGROUND_IMAGE_PATH);
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
     *  SIDEBAR  (identical to the other pages; "Announcements" stays active)
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
        Label name = new Label("Sarpanch");
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

        HBox projectTrackerNav = navItem("\uD83D\uDDC2", "Project Tracker", false);
        projectTrackerNav.setOnMouseClicked(e -> {
            System.out.println("Project Tracker clicked");
            ProjectTrackerPage projectTrackerPage = new ProjectTrackerPage();
            SarpanchDashboard.myStage.setScene(projectTrackerPage.getProjectTrackerScene(backToDashboardAction));
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

        HBox announcementsNav = navItem("\uD83D\uDCE2", "Announcements", true);
        announcementsNav.setOnMouseClicked(e -> {
            System.out.println("Announcements clicked");
            backToAnnouncementsAction.run();
        });

        nav.getChildren().addAll(
            dashboardNav,
            projectTrackerNav,
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
            wrap.setCursor(Cursor.HAND);
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
     *  TOP NAVIGATION BAR (identical to the other pages)
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
        searchField.setPromptText("Search announcements...");
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
        VBox main = new VBox(28);
        main.setPadding(new Insets(32, 40, 48, 40));
        main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");

        main.getChildren().addAll(
            buildBreadcrumbAndHeader(),
            buildBodyRow()
        );
        return main;
    }

    /* ---------- Breadcrumb + page header + Done/back action ---------- */
    private VBox buildBreadcrumbAndHeader() {
        VBox wrap = new VBox(10);

        HBox crumbRow = new HBox(8);
        crumbRow.setAlignment(Pos.CENTER_LEFT);

        Label crumbBack = new Label("\u2190");
        crumbBack.setStyle("-fx-font-size: 13px; -fx-text-fill: " + SAFFRON_MAIN + "; -fx-font-weight: 800;");

        Label crumbAnnouncements = new Label("Announcements");
        crumbAnnouncements.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700;" +
            "-fx-text-fill: " + SAFFRON_MAIN + ";");

        HBox crumbLink = new HBox(6, crumbBack, crumbAnnouncements);
        crumbLink.setAlignment(Pos.CENTER_LEFT);
        crumbLink.setCursor(Cursor.HAND);
        crumbLink.setOnMouseClicked(e -> {
            System.out.println("Breadcrumb: Back to Announcements clicked");
            backToAnnouncementsAction.run();
        });

        Label crumbSep = new Label("/");
        crumbSep.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.35); -fx-font-weight: 700;");

        Label crumbCurrent = new Label("Edit Announcements");
        crumbCurrent.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");

        crumbRow.getChildren().addAll(crumbLink, crumbSep, crumbCurrent);

        HBox headRow = new HBox(16);
        headRow.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(6);
        Label pageTitle = new Label("Edit Announcements");
        pageTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label pageSub = new Label("Update the text, category, priority or attachments of an announcement — or remove one that's no longer needed.");
        pageSub.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 15px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.70);");
        pageSub.setWrapText(true);
        titleBox.getChildren().addAll(pageTitle, pageSub);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        Label doneBtn = new Label("\u2713  Done");
        doneBtn.setPadding(new Insets(12, 22, 12, 22));
        String doneBase = "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
            "-fx-background-radius: 12; -fx-text-fill: white;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 800;" +
            "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.35), 10, 0.1, 0, 4); -fx-cursor: hand;";
        doneBtn.setStyle(doneBase);
        doneBtn.setOnMouseEntered(e -> doneBtn.setStyle(doneBase +
            "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.55), 15, 0.2, 0, 5); -fx-translate-y: -1;"));
        doneBtn.setOnMouseExited(e -> doneBtn.setStyle(doneBase));
        doneBtn.setOnMouseClicked(e -> {
            System.out.println("Done clicked -> back to Announcements");
            backToAnnouncementsAction.run();
        });

        headRow.getChildren().addAll(titleBox, doneBtn);

        wrap.getChildren().addAll(crumbRow, headRow);
        return wrap;
    }

    /* ---------- Two-column row: announcement list (left) + summary panel (right) ---------- */
    private HBox buildBodyRow() {
        HBox row = new HBox(24);
        row.setAlignment(Pos.TOP_LEFT);

        VBox leftColumn = buildListColumn();
        HBox.setHgrow(leftColumn, Priority.ALWAYS);

        VBox rightColumn = buildSummaryColumn();
        rightColumn.setPrefWidth(340);
        rightColumn.setMinWidth(300);

        row.getChildren().addAll(leftColumn, rightColumn);
        return row;
    }

    /* ============================================================
     *  LEFT COLUMN: filter row + announcement cards list
     * ============================================================ */
    private VBox buildListColumn() {
        VBox wrap = new VBox(18);

        HBox filterRow = new HBox(10);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        countLabel = new Label();
        countLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: rgba(11,61,46,0.65);");
        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);
        Label sortHint = new Label("Most recent first");
        sortHint.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.45);");
        filterRow.getChildren().addAll(countLabel, grow, sortHint);

        listContainer = new VBox(18);

        wrap.getChildren().addAll(filterRow, listContainer);
        refreshList();
        return wrap;
    }

    /** Re-renders the list of announcement cards from the current data + UI state. */
    private void refreshList() {
        listContainer.getChildren().clear();
        countLabel.setText(announcements.size() + " announcement" + (announcements.size() == 1 ? "" : "s") + " found");

        int published = 0, draft = 0;
        for (AnnouncementItem item : announcements) {
            if ("Published".equals(item.status)) published++; else draft++;
        }
        if (publishedStatLabel != null) publishedStatLabel.setText(String.valueOf(published));
        if (draftStatLabel != null) draftStatLabel.setText(String.valueOf(draft));

        if (announcements.isEmpty()) {
            listContainer.getChildren().add(buildEmptyState());
            return;
        }

        // copy to avoid ConcurrentModificationException when an item is deleted mid-loop
        List<AnnouncementItem> snapshot = new ArrayList<>(announcements);
        for (AnnouncementItem item : snapshot) {
            listContainer.getChildren().add(buildAnnouncementCard(item));
        }
    }

    private VBox buildEmptyState() {
        VBox empty = new VBox(10);
        empty.setAlignment(Pos.CENTER);
        empty.setPadding(new Insets(50));
        empty.setStyle(cardStyle(20));
        Label icon = new Label("\uD83D\uDCED");
        icon.setStyle("-fx-font-size: 32px;");
        Label msg = new Label("No announcements yet");
        msg.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 15px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");
        empty.getChildren().addAll(icon, msg);
        return empty;
    }

    /** Builds one announcement card, including its inline edit form / delete-confirm states. */
    private VBox buildAnnouncementCard(AnnouncementItem item) {
        VBox card = new VBox(16);
        card.setPadding(new Insets(24));
        card.setStyle(cardStyle(20));
        addHoverLift(card, 20);

        HBox topRow = new HBox(14);
        topRow.setAlignment(Pos.CENTER_LEFT);

        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(44, 44);
        iconChip.setMinSize(44, 44);
        iconChip.setStyle("-fx-background-color: " + rgba(item.categoryAccent, 0.12) + "; -fx-background-radius: 14;");
        Label ic = new Label("\uD83D\uDCE2");
        ic.setStyle("-fx-font-size: 18px; -fx-text-fill: " + item.categoryAccent + ";");
        iconChip.getChildren().add(ic);

        VBox titleBox = new VBox(6);
        Label title = new Label(item.title);
        title.setWrapText(true);
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 17px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");

        HBox pillsRow = new HBox(8);
        pillsRow.setAlignment(Pos.CENTER_LEFT);
        Label catPill = pill(item.category, item.categoryAccent);
        Label prioPill = pill("\u26A1 " + item.priority, item.priorityAccent);
        Label statusPill = pill(item.status, "Published".equals(item.status) ? "#2E9E5B" : AI_VIOLET);
        pillsRow.getChildren().addAll(catPill, prioPill, statusPill);
        titleBox.getChildren().addAll(title, pillsRow);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        Label dateLbl = new Label(item.date);
        dateLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.50);");

        topRow.getChildren().addAll(iconChip, titleBox, dateLbl);

        Label desc = new Label(item.message);
        desc.setWrapText(true);
        desc.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.68);");

        card.getChildren().addAll(topRow, desc);

        boolean isEditingThis = item == currentlyEditing;
        boolean isDeletingThis = item == pendingDelete;

        if (isEditingThis) {
            card.getChildren().add(buildInlineEditForm(item));
        } else {
            card.getChildren().add(buildActionRow(item, isDeletingThis));
        }

        return card;
    }

    private Label pill(String text, String accent) {
        Label p = new Label(text);
        p.setPadding(new Insets(5, 12, 5, 12));
        p.setStyle("-fx-background-color: " + rgba(accent, 0.14) + "; -fx-background-radius: 999;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: " + accent + ";");
        return p;
    }

    /** Row of Edit / Delete pill buttons shown when a card is in its normal (read-only) state. */
    private HBox buildActionRow(AnnouncementItem item, boolean isDeletingThis) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label editBtn = new Label("\uD83D\uDD8A  Edit");
        editBtn.setPadding(new Insets(9, 18, 9, 18));
        String editBase = "-fx-background-color: " + rgba(CONTEXT_TEAL, 0.10) + "; -fx-background-radius: 10;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800; -fx-text-fill: " + CONTEXT_TEAL + ";" +
            "-fx-cursor: hand;";
        editBtn.setStyle(editBase);
        editBtn.setOnMouseEntered(e -> editBtn.setStyle(editBase.replace("0.10", "0.20")));
        editBtn.setOnMouseExited(e -> editBtn.setStyle(editBase));
        editBtn.setOnMouseClicked(e -> {
            currentlyEditing = item;
            pendingDelete = null;
            refreshList();
        });

        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);

        if (isDeletingThis) {
            Label confirmLbl = new Label("Delete this announcement permanently?");
            confirmLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + DELAYED_RED + ";");

            Label confirmBtn = new Label("\uD83D\uDDD1  Confirm Delete");
            confirmBtn.setPadding(new Insets(9, 16, 9, 16));
            String confirmBase = "-fx-background-color: " + DELAYED_RED + "; -fx-background-radius: 10;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800; -fx-text-fill: white;" +
                "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, " + rgba(DELAYED_RED, 0.45) + ", 10, 0.2, 0, 3);";
            confirmBtn.setStyle(confirmBase);
            confirmBtn.setOnMouseClicked(e -> {
                System.out.println("[Edit Announcements] Deleted: " + item.title);
                announcements.remove(item);
                pendingDelete = null;
                refreshList();
            });

            Label keepBtn = new Label("Keep it");
            keepBtn.setPadding(new Insets(9, 16, 9, 16));
            String keepBase = "-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 10;" +
                "-fx-border-color: rgba(11,61,46,0.15); -fx-border-radius: 10; -fx-border-width: 1;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";" +
                "-fx-cursor: hand;";
            keepBtn.setStyle(keepBase);
            keepBtn.setOnMouseClicked(e -> {
                pendingDelete = null;
                refreshList();
            });

            row.getChildren().addAll(editBtn, grow, confirmLbl, keepBtn, confirmBtn);
        } else {
            Label deleteBtn = new Label("\uD83D\uDDD1  Delete");
            deleteBtn.setPadding(new Insets(9, 18, 9, 18));
            String deleteBase = "-fx-background-color: " + rgba(DELAYED_RED, 0.10) + "; -fx-background-radius: 10;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800; -fx-text-fill: " + DELAYED_RED + ";" +
                "-fx-cursor: hand;";
            deleteBtn.setStyle(deleteBase);
            deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle(deleteBase.replace("0.10", "0.20")));
            deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle(deleteBase));
            deleteBtn.setOnMouseClicked(e -> {
                pendingDelete = item;
                currentlyEditing = null;
                refreshList();
            });

            row.getChildren().addAll(editBtn, grow, deleteBtn);
        }

        return row;
    }

    /** The inline, pre-filled edit form shown inside a card when its Edit button is clicked. */
    private VBox buildInlineEditForm(AnnouncementItem item) {
        VBox form = new VBox(16);
        form.setPadding(new Insets(18, 0, 0, 0));

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: rgba(11,61,46,0.10);");

        VBox titleField = fieldLabelBox("TITLE");
        TextField titleInput = new TextField(item.title);
        titleInput.setPrefHeight(44);
        titleInput.setStyle(inputStyle());
        titleField.getChildren().add(titleInput);

        VBox messageField = fieldLabelBox("MESSAGE");
        TextArea messageInput = new TextArea(item.message);
        messageInput.setWrapText(true);
        messageInput.setPrefRowCount(4);
        messageInput.setStyle(inputStyle());
        messageField.getChildren().add(messageInput);

        VBox categoryField = fieldLabelBox("CATEGORY");
        FlowPane catChips = new FlowPane(8, 8);
        ToggleGroup catGroup = new ToggleGroup();
        String[] selectedCategory = {item.category};
        String[] selectedCategoryAccent = {item.categoryAccent};
        for (String[] cat : CATEGORY_ACCENTS) {
            ToggleButton chip = new ToggleButton(cat[0]);
            chip.setToggleGroup(catGroup);
            chip.setCursor(Cursor.HAND);
            String accent = cat[1];
            String off = "-fx-background-color: " + rgba(accent, 0.08) + "; -fx-background-radius: 999;" +
                "-fx-border-color: " + rgba(accent, 0.30) + "; -fx-border-radius: 999; -fx-border-width: 1;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + accent + ";" +
                "-fx-padding: 7 14 7 14;";
            String on = "-fx-background-color: " + accent + "; -fx-background-radius: 999;" +
                "-fx-border-color: " + accent + "; -fx-border-radius: 999; -fx-border-width: 1;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: white;" +
                "-fx-padding: 7 14 7 14;";
            chip.setStyle(cat[0].equals(item.category) ? on : off);
            chip.setSelected(cat[0].equals(item.category));
            chip.selectedProperty().addListener((obs, wasSel, isSel) -> chip.setStyle(isSel ? on : off));
            chip.setOnAction(e -> {
                if (chip.isSelected()) {
                    selectedCategory[0] = cat[0];
                    selectedCategoryAccent[0] = accent;
                } else {
                    chip.setSelected(true);
                }
            });
            catChips.getChildren().add(chip);
        }
        categoryField.getChildren().add(catChips);

        VBox priorityField = fieldLabelBox("PRIORITY");
        HBox prioRow = new HBox(8);
        ToggleGroup prioGroup = new ToggleGroup();
        String[] selectedPriority = {item.priority};
        String[] selectedPriorityAccent = {item.priorityAccent};
        for (String[] p : PRIORITY_ACCENTS) {
            ToggleButton btn = new ToggleButton(p[0]);
            btn.setToggleGroup(prioGroup);
            btn.setCursor(Cursor.HAND);
            btn.setPrefWidth(100);
            String accent = p[1];
            String off = "-fx-background-color: rgba(240,244,242,0.7); -fx-background-radius: 10;" +
                "-fx-border-color: rgba(11,61,46,0.14); -fx-border-radius: 10; -fx-border-width: 1;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: rgba(11,61,46,0.65);";
            String on = "-fx-background-color: " + accent + "; -fx-background-radius: 10;" +
                "-fx-border-color: " + accent + "; -fx-border-radius: 10; -fx-border-width: 1;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: white;";
            btn.setStyle(p[0].equals(item.priority) ? on : off);
            btn.setSelected(p[0].equals(item.priority));
            btn.selectedProperty().addListener((obs, wasSel, isSel) -> btn.setStyle(isSel ? on : off));
            btn.setOnAction(e -> {
                if (btn.isSelected()) {
                    selectedPriority[0] = p[0];
                    selectedPriorityAccent[0] = accent;
                } else {
                    btn.setSelected(true);
                }
            });
            prioRow.getChildren().add(btn);
        }
        priorityField.getChildren().add(prioRow);

        HBox actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        actionRow.setPadding(new Insets(4, 0, 0, 0));

        Label cancelBtn = new Label("Cancel");
        cancelBtn.setPadding(new Insets(11, 20, 11, 20));
        String cancelBase = "-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 10;" +
            "-fx-border-color: rgba(11,61,46,0.15); -fx-border-radius: 10; -fx-border-width: 1;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 700;" +
            "-fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;";
        cancelBtn.setStyle(cancelBase);
        cancelBtn.setOnMouseClicked(e -> {
            currentlyEditing = null;
            refreshList();
        });

        Label saveBtn = new Label("\u2713  Save Changes");
        saveBtn.setPadding(new Insets(11, 22, 11, 22));
        String saveBase = "-fx-background-color: linear-gradient(to right, " + CONTEXT_TEAL + ", " + FOREST_DEEP + ");" +
            "-fx-background-radius: 10; -fx-text-fill: white;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800;" +
            "-fx-effect: dropshadow(gaussian, rgba(14,140,140,0.40), 10, 0.15, 0, 4); -fx-cursor: hand;";
        saveBtn.setStyle(saveBase);
        saveBtn.setOnMouseClicked(e -> {
            item.title = titleInput.getText().isBlank() ? item.title : titleInput.getText();
            item.message = messageInput.getText().isBlank() ? item.message : messageInput.getText();
            item.category = selectedCategory[0];
            item.categoryAccent = selectedCategoryAccent[0];
            item.priority = selectedPriority[0];
            item.priorityAccent = selectedPriorityAccent[0];
            System.out.println("[Edit Announcements] Saved changes to: " + item.title);
            currentlyEditing = null;
            refreshList();
        });

        actionRow.getChildren().addAll(cancelBtn, saveBtn);

        form.getChildren().addAll(divider, titleField, messageField, categoryField, priorityField, actionRow);
        return form;
    }

    private VBox fieldLabelBox(String text) {
        VBox box = new VBox(8);
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 800;" +
            "-fx-text-fill: rgba(11,61,46,0.70); -fx-letter-spacing: 0.05em;");
        box.getChildren().add(lbl);
        return box;
    }

    private String inputStyle() {
        return "-fx-background-color: rgba(240,244,242,0.65); -fx-background-radius: 12;" +
               "-fx-border-color: rgba(11,61,46,0.14); -fx-border-radius: 12; -fx-border-width: 1;" +
               "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-text-fill: " + FOREST_DEEP + ";" +
               "-fx-prompt-text-fill: rgba(11,61,46,0.38);";
    }

    /* ============================================================
     *  RIGHT COLUMN: quick stats + guidelines
     * ============================================================ */
    private VBox buildSummaryColumn() {
        VBox wrap = new VBox(18);

        HBox headRow = new HBox(10);
        headRow.setAlignment(Pos.CENTER_LEFT);
        Label statsIcon = new Label("\uD83D\uDCCA");
        statsIcon.setStyle("-fx-font-size: 15px;");
        Label headLbl = new Label("Overview");
        headLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        headRow.getChildren().addAll(statsIcon, headLbl);

        VBox statsCard = new VBox(16);
        statsCard.setPadding(new Insets(22));
        statsCard.setStyle(cardStyle(20));

        publishedStatLabel = new Label("0");
        HBox publishedRow = statRow("\u2705", "Published", publishedStatLabel, "#2E9E5B");
        Region divider1 = new Region();
        divider1.setPrefHeight(1);
        divider1.setStyle("-fx-background-color: rgba(11,61,46,0.08);");
        draftStatLabel = new Label("0");
        HBox draftRow = statRow("\uD83D\uDCDD", "Drafts", draftStatLabel, AI_VIOLET);

        statsCard.getChildren().addAll(publishedRow, divider1, draftRow);

        VBox tipCard = new VBox(10);
        tipCard.setPadding(new Insets(18));
        tipCard.setStyle(
            "-fx-background-color: " + rgba(CONTEXT_TEAL, 0.08) + "; -fx-background-radius: 16;" +
            "-fx-border-color: " + rgba(CONTEXT_TEAL, 0.25) + "; -fx-border-radius: 16; -fx-border-width: 1;"
        );
        Label tipHead = new Label("\u270F\uFE0F  Editing tip");
        tipHead.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: " + CONTEXT_TEAL + ";");
        Label tipBody = new Label("Changes to a Published announcement go live immediately for villagers. Editing a Draft keeps it hidden until you publish it.");
        tipBody.setWrapText(true);
        tipBody.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.70);");
        tipCard.getChildren().addAll(tipHead, tipBody);

        VBox warnCard = new VBox(10);
        warnCard.setPadding(new Insets(18));
        warnCard.setStyle(
            "-fx-background-color: " + rgba(DELAYED_RED, 0.08) + "; -fx-background-radius: 16;" +
            "-fx-border-color: " + rgba(DELAYED_RED, 0.25) + "; -fx-border-radius: 16; -fx-border-width: 1;"
        );
        Label warnHead = new Label("\u26A0\uFE0F  Before you delete");
        warnHead.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: " + DELAYED_RED + ";");
        Label warnBody = new Label("Deleting an announcement removes it permanently for all villagers and cannot be undone. You'll always be asked to confirm first.");
        warnBody.setWrapText(true);
        warnBody.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.70);");
        warnCard.getChildren().addAll(warnHead, warnBody);

        wrap.getChildren().addAll(headRow, statsCard, tipCard, warnCard);
        return wrap;
    }

    private HBox statRow(String icon, String label, Label valueLabel, String accent) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(38, 38);
        iconChip.setMinSize(38, 38);
        iconChip.setStyle("-fx-background-color: " + rgba(accent, 0.14) + "; -fx-background-radius: 12;");
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 15px;");
        iconChip.getChildren().add(ic);

        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");

        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);

        valueLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: " + accent + ";");

        row.getChildren().addAll(iconChip, lbl, grow, valueLabel);
        return row;
    }

    /* ============================================================
     *  HELPERS (kept identical to the other pages)
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
               "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.12), 24, 0.15, 0, 8);";
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