package com.tech_fusion.view.sarpanch;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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

/**
 * GramConnect - Create Announcements Page.
 *
 * Sub-page of AnnouncementsPage. Lets the Sarpanch compose a brand new,
 * customised announcement (title, category, message, priority, target
 * audience, publish date, attachment) with a live preview, then Save as
 * Draft or Publish it. Visual language, sidebar and top bar are kept
 * identical to AnnouncementsPage and every other page so it feels native
 * to the app. Navigation is bidirectional: it is opened from the
 * "Create Announcements" tile on AnnouncementsPage, and the breadcrumb /
 * "Cancel" action returns to AnnouncementsPage, exactly like the
 * Runnable-based back-navigation pattern used across the app.
 */
public class CreateAnnouncementsPage {

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

    /* live-preview bound labels */
    private Label previewTitle;
    private Label previewBody;
    private Label previewCategoryPill;
    private Label previewPriorityPill;
    private Label previewDate;

    private String selectedCategory = "General";
    private String selectedCategoryAccent = CONTEXT_TEAL;
    private String selectedPriority = "Normal";

    /**
     * Builds the Create Announcements scene and returns it.
     */
    public Scene getCreateAnnouncementsScene(Runnable backToAnnouncementsAction, Runnable backToDashboardAction) {
        this.backToAnnouncementsAction = backToAnnouncementsAction;
        this.backToDashboardAction = backToDashboardAction;

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
     *  SIDEBAR  (identical to the other pages; "Announcements" stays active
     *  since this is a sub-screen of the Announcements section)
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
createBtn.setOnMouseClicked(e -> {
            System.out.println("Create Project clicked");
            CreateProjectPage createProjectPage = new CreateProjectPage();
            Runnable backToProjectTrackerAction = () -> {
                ProjectTrackerPage projectTrackerPage = new ProjectTrackerPage();
                SarpanchDashboard.myStage.setTitle("GramConnect - Project Tracker");
                SarpanchDashboard.myStage.setScene(projectTrackerPage.getProjectTrackerScene(backToDashboardAction));
            };
            SarpanchDashboard.myStage.setTitle("GramConnect - Create Project");
            SarpanchDashboard.myStage.setScene(createProjectPage.getCreateProjectScene(backToProjectTrackerAction, backToDashboardAction));
        });

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
            buildFormAndPreviewRow()
        );
        return main;
    }

    /* ---------- Breadcrumb + page header + Cancel/back action ---------- */
    private VBox buildBreadcrumbAndHeader() {
        VBox wrap = new VBox(10);

        HBox crumbRow = new HBox(8);
        crumbRow.setAlignment(Pos.CENTER_LEFT);

        Label crumbBack = new Label("\u2190");
        crumbBack.setStyle("-fx-font-size: 13px; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-font-weight: 800;");

        Label crumbAnnouncements = new Label("Announcements");
        crumbAnnouncements.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700;" +
            "-fx-text-fill: " + CONTEXT_TEAL + "; -fx-underline: false;");
        crumbAnnouncements.setCursor(Cursor.HAND);

        HBox crumbLink = new HBox(6, crumbBack, crumbAnnouncements);
        crumbLink.setAlignment(Pos.CENTER_LEFT);
        crumbLink.setCursor(Cursor.HAND);
        crumbLink.setOnMouseClicked(e -> {
            System.out.println("Breadcrumb: Back to Announcements clicked");
            backToAnnouncementsAction.run();
        });

        Label crumbSep = new Label("/");
        crumbSep.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.35); -fx-font-weight: 700;");

        Label crumbCurrent = new Label("Create Announcement");
        crumbCurrent.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");

        crumbRow.getChildren().addAll(crumbLink, crumbSep, crumbCurrent);

        HBox headRow = new HBox(16);
        headRow.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(6);
        Label pageTitle = new Label("Create Announcement");
        pageTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label pageSub = new Label("Compose a new notice for the village — add a title, message, category and audience, then publish or save it as a draft.");
        pageSub.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 15px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.70);");
        pageSub.setWrapText(true);
        titleBox.getChildren().addAll(pageTitle, pageSub);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        Label cancelBtn = new Label("\u2715  Cancel");
        cancelBtn.setPadding(new Insets(12, 20, 12, 20));
        String cancelBase = "-fx-background-color: rgba(255,255,255,0.75); -fx-background-radius: 12;" +
            "-fx-border-color: rgba(11,61,46,0.15); -fx-border-radius: 12; -fx-border-width: 1;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 700;" +
            "-fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;";
        cancelBtn.setStyle(cancelBase);
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle(cancelBase.replace("0.75", "0.95")));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle(cancelBase));
        cancelBtn.setOnMouseClicked(e -> {
            System.out.println("Cancel clicked -> back to Announcements");
            backToAnnouncementsAction.run();
        });

        headRow.getChildren().addAll(titleBox, cancelBtn);

        wrap.getChildren().addAll(crumbRow, headRow);
        return wrap;
    }

    /* ---------- Two-column row: form (left) + live preview (right) ---------- */
    private HBox buildFormAndPreviewRow() {
        HBox row = new HBox(24);
        row.setAlignment(Pos.TOP_LEFT);

        VBox formCard = buildFormCard();
        HBox.setHgrow(formCard, Priority.ALWAYS);

        VBox previewColumn = buildPreviewColumn();
        previewColumn.setPrefWidth(380);
        previewColumn.setMinWidth(340);

        row.getChildren().addAll(formCard, previewColumn);
        return row;
    }

    /* ============================================================
     *  FORM CARD
     * ============================================================ */
    private VBox buildFormCard() {
        VBox card = new VBox(26);
        card.setPadding(new Insets(30));
        card.setStyle(cardStyle(20));

        card.getChildren().addAll(
            sectionLabel("\u270D", "Announcement Details", CONTEXT_TEAL),
            buildTitleField(),
            buildCategoryChips(),
            buildMessageField(),
            new Region() {{ setPrefHeight(1); setStyle("-fx-background-color: rgba(11,61,46,0.08);"); }},
            sectionLabel("\uD83C\uDFAF", "Audience & Priority", SAFFRON_MAIN),
            buildAudienceRow(),
            buildPriorityRow(),
            new Region() {{ setPrefHeight(1); setStyle("-fx-background-color: rgba(11,61,46,0.08);"); }},
            sectionLabel("\uD83D\uDCCE", "Schedule & Attachment", AI_VIOLET),
            buildScheduleRow(),
            buildAttachmentBox(),
            buildActionButtonsRow()
        );

        return card;
    }

    private HBox sectionLabel(String icon, String text, String accent) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        StackPane chip = new StackPane();
        chip.setPrefSize(32, 32);
        chip.setMinSize(32, 32);
        chip.setStyle("-fx-background-color: " + rgba(accent, 0.12) + "; -fx-background-radius: 10;");
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 14px; -fx-text-fill: " + accent + ";");
        chip.getChildren().add(ic);
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 17px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        row.getChildren().addAll(chip, lbl);
        return row;
    }

    private VBox fieldLabelBox(String text) {
        VBox box = new VBox(8);
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800;" +
            "-fx-text-fill: rgba(11,61,46,0.75); -fx-letter-spacing: 0.05em;");
        box.getChildren().add(lbl);
        return box;
    }

    private String inputStyle() {
        return "-fx-background-color: rgba(240,244,242,0.65); -fx-background-radius: 12;" +
               "-fx-border-color: rgba(11,61,46,0.14); -fx-border-radius: 12; -fx-border-width: 1;" +
               "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: " + FOREST_DEEP + ";" +
               "-fx-prompt-text-fill: rgba(11,61,46,0.38);";
    }

    private VBox buildTitleField() {
        VBox box = fieldLabelBox("ANNOUNCEMENT TITLE");
        TextField titleField = new TextField();
        titleField.setPromptText("e.g. Gram Sabha Meeting on 25th August");
        titleField.setPrefHeight(46);
        titleField.setStyle(inputStyle());
        titleField.textProperty().addListener((obs, oldV, newV) ->
            previewTitle.setText(newV == null || newV.isBlank() ? "Your announcement title" : newV));
        box.getChildren().add(titleField);
        return box;
    }

    private VBox buildCategoryChips() {
        VBox box = fieldLabelBox("CATEGORY");
        FlowPane chips = new FlowPane(10, 10);

        String[][] categories = {
            {"General", FOREST_DEEP},
            {"Gram Sabha", AI_VIOLET},
            {"Water Supply", CONTEXT_TEAL},
            {"Health", "#2E9E5B"},
            {"Infrastructure", SAFFRON_MAIN},
            {"Emergency", DELAYED_RED}
        };

        ToggleGroup catGroup = new ToggleGroup();
        for (String[] cat : categories) {
            ToggleButton chip = new ToggleButton(cat[0]);
            chip.setToggleGroup(catGroup);
            chip.setCursor(Cursor.HAND);
            String accent = cat[1];
            String off = "-fx-background-color: " + rgba(accent, 0.08) + "; -fx-background-radius: 999;" +
                "-fx-border-color: " + rgba(accent, 0.30) + "; -fx-border-radius: 999; -fx-border-width: 1;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 700; -fx-text-fill: " + accent + ";" +
                "-fx-padding: 8 16 8 16;";
            String on = "-fx-background-color: " + accent + "; -fx-background-radius: 999;" +
                "-fx-border-color: " + accent + "; -fx-border-radius: 999; -fx-border-width: 1;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800; -fx-text-fill: white;" +
                "-fx-padding: 8 16 8 16; -fx-effect: dropshadow(gaussian, " + rgba(accent, 0.45) + ", 10, 0.2, 0, 3);";
            chip.setStyle(off);
            chip.selectedProperty().addListener((obs, wasSel, isSel) -> chip.setStyle(isSel ? on : off));
            chip.setOnAction(e -> {
                if (chip.isSelected()) {
                    selectedCategory = cat[0];
                    selectedCategoryAccent = accent;
                    previewCategoryPill.setText(cat[0]);
                    previewCategoryPill.setStyle("-fx-background-color: " + rgba(accent, 0.14) + "; -fx-background-radius: 999;" +
                        "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: " + accent + ";" +
                        "-fx-padding: 5 12 5 12;");
                } else {
                    chip.setSelected(true); // keep exactly one selected
                }
            });
            if (cat[0].equals("General")) chip.setSelected(true);
            chips.getChildren().add(chip);
        }

        box.getChildren().add(chips);
        return box;
    }

    private VBox buildMessageField() {
        VBox box = fieldLabelBox("MESSAGE");
        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Write the full announcement message here. Be clear about the date, time, venue and any action villagers need to take...");
        messageArea.setWrapText(true);
        messageArea.setPrefRowCount(6);
        messageArea.setStyle(inputStyle());
        messageArea.textProperty().addListener((obs, oldV, newV) ->
            previewBody.setText(newV == null || newV.isBlank()
                ? "Your announcement message will appear here as you type, so you can see exactly how villagers will read it."
                : newV));

        HBox helperRow = new HBox();
        Label helper = new Label("Tip: keep it short and specific — date, time, venue, and what's expected of villagers.");
        helper.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.50);");
        helperRow.getChildren().add(helper);

        box.getChildren().addAll(messageArea, helperRow);
        return box;
    }

    private VBox buildAudienceRow() {
        VBox box = fieldLabelBox("TARGET AUDIENCE");
        FlowPane row = new FlowPane(20, 12);

        String[] audiences = {"All Villagers", "Ward 1", "Ward 2", "Ward 3", "Farmers Group", "Self-Help Groups"};
        for (String a : audiences) {
            CheckBox cb = new CheckBox(a);
            cb.setSelected(a.equals("All Villagers"));
            cb.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + FOREST_DEEP + ";" +
                "-fx-cursor: hand;");
            row.getChildren().add(cb);
        }
        box.getChildren().add(row);
        return box;
    }

    private VBox buildPriorityRow() {
        VBox box = fieldLabelBox("PRIORITY");
        HBox row = new HBox(10);

        String[][] priorities = {
            {"Low", CONTEXT_TEAL},
            {"Normal", FOREST_DEEP},
            {"High", SAFFRON_MAIN},
            {"Urgent", DELAYED_RED}
        };

        ToggleGroup prioGroup = new ToggleGroup();
        for (String[] p : priorities) {
            ToggleButton btn = new ToggleButton(p[0]);
            btn.setToggleGroup(prioGroup);
            btn.setCursor(Cursor.HAND);
            btn.setPrefWidth(110);
            String accent = p[1];
            String off = "-fx-background-color: rgba(240,244,242,0.7); -fx-background-radius: 10;" +
                "-fx-border-color: rgba(11,61,46,0.14); -fx-border-radius: 10; -fx-border-width: 1;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 700; -fx-text-fill: rgba(11,61,46,0.65);";
            String on = "-fx-background-color: " + accent + "; -fx-background-radius: 10;" +
                "-fx-border-color: " + accent + "; -fx-border-radius: 10; -fx-border-width: 1;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800; -fx-text-fill: white;" +
                "-fx-effect: dropshadow(gaussian, " + rgba(accent, 0.4) + ", 8, 0.2, 0, 2);";
            btn.setStyle(off);
            btn.selectedProperty().addListener((obs, wasSel, isSel) -> btn.setStyle(isSel ? on : off));
            btn.setOnAction(e -> {
                if (btn.isSelected()) {
                    selectedPriority = p[0];
                    previewPriorityPill.setText("\u26A1 " + p[0] + " priority");
                    previewPriorityPill.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 700; -fx-text-fill: " + accent + ";");
                } else {
                    btn.setSelected(true);
                }
            });
            if (p[0].equals("Normal")) btn.setSelected(true);
            row.getChildren().add(btn);
        }
        box.getChildren().add(row);
        return box;
    }

    private HBox buildScheduleRow() {
        HBox row = new HBox(20);

        VBox dateBox = fieldLabelBox("PUBLISH DATE");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefHeight(46);
        datePicker.setPrefWidth(220);
        datePicker.setStyle(inputStyle());
        datePicker.valueProperty().addListener((obs, oldV, newV) ->
            previewDate.setText(newV != null ? newV.toString() : LocalDate.now().toString()));
        dateBox.getChildren().add(datePicker);
        HBox.setHgrow(dateBox, Priority.ALWAYS);

        VBox languageBox = fieldLabelBox("LANGUAGE");
        ComboBox<String> languageCombo = new ComboBox<>();
        languageCombo.getItems().addAll("Marathi", "English", "Marathi + English");
        languageCombo.setValue("Marathi + English");
        languageCombo.setPrefHeight(46);
        languageCombo.setPrefWidth(220);
        languageCombo.setStyle(inputStyle());
        languageBox.getChildren().add(languageCombo);
        HBox.setHgrow(languageBox, Priority.ALWAYS);

        row.getChildren().addAll(dateBox, languageBox);
        return row;
    }

    private VBox buildAttachmentBox() {
        VBox box = fieldLabelBox("ATTACHMENT (OPTIONAL)");
        HBox dropZone = new HBox(14);
        dropZone.setAlignment(Pos.CENTER_LEFT);
        dropZone.setPadding(new Insets(18, 20, 18, 20));
        dropZone.setStyle(
            "-fx-background-color: " + rgba(AI_VIOLET, 0.06) + "; -fx-background-radius: 14;" +
            "-fx-border-color: " + rgba(AI_VIOLET, 0.35) + "; -fx-border-radius: 14; -fx-border-width: 1.4;" +
            "-fx-border-style: segments(6,4);"
        );
        dropZone.setCursor(Cursor.HAND);

        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(42, 42);
        iconChip.setMinSize(42, 42);
        iconChip.setStyle("-fx-background-color: " + rgba(AI_VIOLET, 0.14) + "; -fx-background-radius: 12;");
        Label ic = new Label("\uD83D\uDCCE");
        ic.setStyle("-fx-font-size: 17px; -fx-text-fill: " + AI_VIOLET + ";");
        iconChip.getChildren().add(ic);

        VBox textBox = new VBox(2);
        Label main = new Label("Click to upload a photo, poster or PDF");
        main.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");
        Label sub = new Label("PNG, JPG or PDF up to 10 MB");
        sub.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.55);");
        textBox.getChildren().addAll(main, sub);

        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);

        Label browsePill = new Label("Browse");
        browsePill.setPadding(new Insets(8, 16, 8, 16));
        browsePill.setStyle("-fx-background-color: " + AI_VIOLET + "; -fx-background-radius: 999;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: white;");

        dropZone.getChildren().addAll(iconChip, textBox, grow, browsePill);
        box.getChildren().add(dropZone);
        return box;
    }

    private HBox buildActionButtonsRow() {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_RIGHT);
        row.setPadding(new Insets(6, 0, 0, 0));

        Label saveDraftBtn = new Label("\uD83D\uDCDD  Save as Draft");
        saveDraftBtn.setPadding(new Insets(14, 22, 14, 22));
        String draftBase = "-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 12;" +
            "-fx-border-color: rgba(11,61,46,0.18); -fx-border-radius: 12; -fx-border-width: 1;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 800;" +
            "-fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;";
        saveDraftBtn.setStyle(draftBase);
        saveDraftBtn.setOnMouseEntered(e -> saveDraftBtn.setStyle(draftBase +
            "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.15), 10, 0.1, 0, 3); -fx-translate-y: -1;"));
        saveDraftBtn.setOnMouseExited(e -> saveDraftBtn.setStyle(draftBase));
        saveDraftBtn.setOnMouseClicked(e -> {
            System.out.println("[Create Announcements] Saved as draft: " + selectedCategory + " / " + selectedPriority);
            backToAnnouncementsAction.run();
        });

        Label publishBtn = new Label("\uD83D\uDCE2  Publish Announcement");
        publishBtn.setPadding(new Insets(14, 24, 14, 24));
        String publishBase = "-fx-background-color: linear-gradient(to right, " + CONTEXT_TEAL + ", " + FOREST_DEEP + ");" +
            "-fx-background-radius: 12; -fx-text-fill: white;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 800;" +
            "-fx-effect: dropshadow(gaussian, rgba(14,140,140,0.40), 12, 0.15, 0, 5); -fx-cursor: hand;";
        publishBtn.setStyle(publishBase);
        publishBtn.setOnMouseEntered(e -> publishBtn.setStyle(publishBase +
            "-fx-effect: dropshadow(gaussian, rgba(14,140,140,0.60), 16, 0.2, 0, 6); -fx-translate-y: -1;"));
        publishBtn.setOnMouseExited(e -> publishBtn.setStyle(publishBase));
        publishBtn.setOnMouseClicked(e -> {
            System.out.println("[Create Announcements] Published: " + selectedCategory + " / " + selectedPriority);
            backToAnnouncementsAction.run();
        });

        row.getChildren().addAll(saveDraftBtn, publishBtn);
        return row;
    }

    /* ============================================================
     *  LIVE PREVIEW COLUMN
     * ============================================================ */
    private VBox buildPreviewColumn() {
        VBox wrap = new VBox(18);

        HBox headRow = new HBox(10);
        headRow.setAlignment(Pos.CENTER_LEFT);
        Label eyeIcon = new Label("\uD83D\uDC41");
        eyeIcon.setStyle("-fx-font-size: 15px;");
        Label headLbl = new Label("Live Preview");
        headLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        headRow.getChildren().addAll(eyeIcon, headLbl);

        VBox previewCard = new VBox(16);
        previewCard.setPadding(new Insets(24));
        previewCard.setStyle(cardStyle(20));

        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);

        StackPane bullhorn = new StackPane();
        bullhorn.setPrefSize(38, 38);
        bullhorn.setMinSize(38, 38);
        bullhorn.setStyle("-fx-background-color: " + rgba(CONTEXT_TEAL, 0.14) + "; -fx-background-radius: 12;");
        Label bIcon = new Label("\uD83D\uDCE2");
        bIcon.setStyle("-fx-font-size: 16px; -fx-text-fill: " + CONTEXT_TEAL + ";");
        bullhorn.getChildren().add(bIcon);

        VBox metaBox = new VBox(4);
        previewCategoryPill = new Label(selectedCategory);
        previewCategoryPill.setStyle("-fx-background-color: " + rgba(selectedCategoryAccent, 0.14) + "; -fx-background-radius: 999;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: " + selectedCategoryAccent + ";" +
            "-fx-padding: 5 12 5 12;");
        previewCategoryPill.setMaxWidth(Region.USE_PREF_SIZE);
        metaBox.getChildren().add(previewCategoryPill);

        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);

        previewDate = new Label(LocalDate.now().toString());
        previewDate.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.55);");

        topRow.getChildren().addAll(bullhorn, metaBox, grow, previewDate);

        previewTitle = new Label("Your announcement title");
        previewTitle.setWrapText(true);
        previewTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 19px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        previewBody = new Label("Your announcement message will appear here as you type, so you can see exactly how villagers will read it.");
        previewBody.setWrapText(true);
        previewBody.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.72);" +
            "-fx-line-spacing: 3;");

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: rgba(11,61,46,0.10);");

        HBox bottomRow = new HBox();
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        previewPriorityPill = new Label("\u26A1 Normal priority");
        previewPriorityPill.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");
        Region gr2 = new Region();
        HBox.setHgrow(gr2, Priority.ALWAYS);
        Label byLine = new Label("\u2014 Sarpanch Patil");
        byLine.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.55); -fx-font-style: italic;");
        bottomRow.getChildren().addAll(previewPriorityPill, gr2, byLine);

        previewCard.getChildren().addAll(topRow, previewTitle, previewBody, divider, bottomRow);

        VBox tipCard = new VBox(10);
        tipCard.setPadding(new Insets(18));
        tipCard.setStyle(
            "-fx-background-color: " + rgba(SAFFRON_MAIN, 0.08) + "; -fx-background-radius: 16;" +
            "-fx-border-color: " + rgba(SAFFRON_MAIN, 0.25) + "; -fx-border-radius: 16; -fx-border-width: 1;"
        );
        Label tipHead = new Label("\uD83D\uDCA1  Good to know");
        tipHead.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: " + SAFFRON_MAIN + ";");
        Label tipBody = new Label("Published announcements are instantly visible to villagers on the Citizen app and are also read aloud at the next Gram Sabha, if marked Urgent.");
        tipBody.setWrapText(true);
        tipBody.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.70);");
        tipCard.getChildren().addAll(tipHead, tipBody);

        wrap.getChildren().addAll(headRow, previewCard, tipCard);
        return wrap;
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

    private String rgba(String hex, double alpha) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
    }
}