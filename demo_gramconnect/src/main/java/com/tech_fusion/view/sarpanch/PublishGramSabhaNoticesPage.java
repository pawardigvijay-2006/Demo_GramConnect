package com.tech_fusion.view.sarpanch;

import java.io.File;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
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
 * GramConnect - Publish Gram Sabha Notices Page.
 *
 * Sub-page of AnnouncementsPage. Lets the Sarpanch publish two kinds of
 * Gram Sabha notices: a "Meeting Announcement" (upcoming meeting, agenda,
 * venue) or a "Meeting Summary" (minutes / resolutions after a meeting has
 * been held) — with a live preview and a glance at the upcoming Gram Sabha
 * schedule. Visual language, sidebar and top bar are kept identical to
 * AnnouncementsPage / CreateAnnouncementsPage / EditAnnouncementsPage and
 * every other page. Navigation is bidirectional: opened from the
 * "Publish Gram Sabha Notices" tile on AnnouncementsPage, and the
 * breadcrumb / "Cancel" action returns to AnnouncementsPage, following the
 * same Runnable-based back-navigation pattern used across the app.
 */
public class PublishGramSabhaNoticesPage {

    /* ---------- Color palette (kept identical to the other pages) ---------- */
    private static final String FOREST_DEEP   = "#0B3D2E";
    private static final String FOREST_LIGHT  = "#0F4736";
    private static final String SAFFRON_MAIN  = "#E07A1F";
    private static final String CONTEXT_TEAL  = "#0E8C8C";
    private static final String AI_VIOLET     = "#7C5CFC";
    //private static final String DELAYED_RED   = "#D94C38";
    private static final String SIDEBAR_TOP   = "#CDEBD8";
    private static final String SIDEBAR_MID   = "#Bce3cc";
    private static final String SIDEBAR_BOT   = "#A9D8BD";

    private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

    private static final String BACKGROUND_IMAGE_PATH =
        "C:/Users/Ashish/Downloads/Background File of each Page/Background Image.png";

    /** Navigates back to AnnouncementsPage (passed in from AnnouncementsPage). */
    private Runnable backToAnnouncementsAction;
    /** Navigates back to the Dashboard (threaded through so the sidebar still works here). */
    private Runnable backToDashboardAction;

    /** true = "Meeting Announcement" notice type selected, false = "Meeting Summary". */
    private boolean isAnnouncementType = true;

    /* live-preview bound nodes */
    private Label previewTypePill;
    private Label previewTitle;
    private Label previewMetaLine;
    private VBox previewListBox;
    private Label previewFooterNote;

    /* dynamic agenda / resolutions list backing the form + preview */
    private final ObservableList<String> listItems = FXCollections.observableArrayList();

    /* re-renderable regions so switching notice type swaps the form in place */
    private VBox formFieldsContainer;
    private VBox typeToggleRow;

    /** Builds the Publish Gram Sabha Notices scene and returns it. */
    public Scene getPublishGramSabhaNoticesScene(Runnable backToAnnouncementsAction, Runnable backToDashboardAction) {
        this.backToAnnouncementsAction = backToAnnouncementsAction;
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
        crumbBack.setStyle("-fx-font-size: 13px; -fx-text-fill: " + AI_VIOLET + "; -fx-font-weight: 800;");

        Label crumbAnnouncements = new Label("Announcements");
        crumbAnnouncements.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700;" +
            "-fx-text-fill: " + AI_VIOLET + ";");

        HBox crumbLink = new HBox(6, crumbBack, crumbAnnouncements);
        crumbLink.setAlignment(Pos.CENTER_LEFT);
        crumbLink.setCursor(Cursor.HAND);
        crumbLink.setOnMouseClicked(e -> {
            System.out.println("Breadcrumb: Back to Announcements clicked");
            backToAnnouncementsAction.run();
        });

        Label crumbSep = new Label("/");
        crumbSep.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.35); -fx-font-weight: 700;");

        Label crumbCurrent = new Label("Publish Gram Sabha Notices");
        crumbCurrent.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");

        crumbRow.getChildren().addAll(crumbLink, crumbSep, crumbCurrent);

        HBox headRow = new HBox(16);
        headRow.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(6);
        Label pageTitle = new Label("Publish Gram Sabha Notices");
        pageTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label pageSub = new Label("Publish a Gram Sabha Meeting Announcement to invite villagers, or a Meeting Summary to record what was decided.");
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

    /* ---------- Two-column row: form (left) + live preview & schedule (right) ---------- */
    private HBox buildFormAndPreviewRow() {
    HBox row = new HBox(24);
    row.setAlignment(Pos.TOP_LEFT);

    // Build preview FIRST because renderFormFields()
    // immediately updates these preview fields.
    VBox previewColumn = buildPreviewColumn();
    previewColumn.setPrefWidth(380);
    previewColumn.setMinWidth(340);

    // Build form AFTER preview nodes exist.
    VBox formCard = buildFormCard();
    HBox.setHgrow(formCard, Priority.ALWAYS);

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

        typeToggleRow = buildTypeToggleRow();

        formFieldsContainer = new VBox(26);
        renderFormFields();

        card.getChildren().addAll(
            sectionLabel("\uD83C\uDFDB", "Notice Type", AI_VIOLET),
            typeToggleRow,
            new Region() {{ setPrefHeight(1); setStyle("-fx-background-color: rgba(11,61,46,0.08);"); }},
            formFieldsContainer
        );

        return card;
    }

    /** The two big "Meeting Announcement" / "Meeting Summary" selector cards. */
    private VBox buildTypeToggleRow() {
        VBox wrap = new VBox();
        HBox row = new HBox(16);

        VBox announcementCard = typeCard("\uD83D\uDCE3", "Meeting Announcement",
            "Invite villagers to an upcoming Gram Sabha meeting with agenda and venue.", AI_VIOLET, true);
        VBox summaryCard = typeCard("\uD83D\uDCCB", "Meeting Summary",
            "Publish the minutes, resolutions and decisions from a meeting already held.", CONTEXT_TEAL, false);

        HBox.setHgrow(announcementCard, Priority.ALWAYS);
        HBox.setHgrow(summaryCard, Priority.ALWAYS);

        announcementCard.setOnMouseClicked(e -> {
            isAnnouncementType = true;
            listItems.clear();
            restyleTypeCards(announcementCard, summaryCard);
            renderFormFields();
        });
        summaryCard.setOnMouseClicked(e -> {
            isAnnouncementType = false;
            listItems.clear();
            restyleTypeCards(summaryCard, announcementCard);
            renderFormFields();
        });

        row.getChildren().addAll(announcementCard, summaryCard);
        wrap.getChildren().add(row);
        return wrap;
    }

    private VBox typeCard(String icon, String title, String desc, String accent, boolean selected) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(18));
        card.setCursor(Cursor.HAND);
        card.setUserData(accent);
        applyTypeCardStyle(card, accent, selected);

        HBox head = new HBox(10);
        head.setAlignment(Pos.CENTER_LEFT);
        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(36, 36);
        iconChip.setMinSize(36, 36);
        iconChip.setStyle("-fx-background-color: " + rgba(accent, selected ? 0.22 : 0.12) + "; -fx-background-radius: 10;");
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 15px; -fx-text-fill: " + accent + ";");
        iconChip.getChildren().add(ic);
        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        head.getChildren().addAll(iconChip, titleLbl);

        Label descLbl = new Label(desc);
        descLbl.setWrapText(true);
        descLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.62);");

        card.getChildren().addAll(head, descLbl);
        return card;
    }

    private void applyTypeCardStyle(VBox card, String accent, boolean selected) {
        if (selected) {
            card.setStyle(
                "-fx-background-color: " + rgba(accent, 0.10) + "; -fx-background-radius: 16;" +
                "-fx-border-color: " + accent + "; -fx-border-radius: 16; -fx-border-width: 2;" +
                "-fx-effect: dropshadow(gaussian, " + rgba(accent, 0.30) + ", 14, 0.2, 0, 5);"
            );
        } else {
            card.setStyle(
                "-fx-background-color: rgba(240,244,242,0.55); -fx-background-radius: 16;" +
                "-fx-border-color: rgba(11,61,46,0.12); -fx-border-radius: 16; -fx-border-width: 1.4;"
            );
        }
    }

    private void restyleTypeCards(VBox selectedCard, VBox unselectedCard) {
        applyTypeCardStyle(selectedCard, (String) selectedCard.getUserData(), true);
        applyTypeCardStyle(unselectedCard, (String) unselectedCard.getUserData(), false);
    }

    /** Rebuilds the fields under the notice-type toggle based on isAnnouncementType. */
    private void renderFormFields() {
        formFieldsContainer.getChildren().clear();

        String accent = isAnnouncementType ? AI_VIOLET : CONTEXT_TEAL;

        VBox titleField = fieldLabelBox(isAnnouncementType ? "MEETING AGENDA TITLE" : "MEETING SUMMARY TITLE");
        TextField titleInput = new TextField();
        titleInput.setPromptText(isAnnouncementType
            ? "e.g. Gram Sabha Meeting - Village Development Plan Review"
            : "e.g. Minutes of Gram Sabha held on 10th August");
        titleInput.setPrefHeight(46);
        titleInput.setStyle(inputStyle());
        titleInput.textProperty().addListener((obs, oldV, newV) ->
            previewTitle.setText(newV == null || newV.isBlank() ? "Gram Sabha notice title" : newV));

        HBox scheduleRow = new HBox(20);
        VBox dateBox = fieldLabelBox(isAnnouncementType ? "MEETING DATE" : "DATE HELD");
        DatePicker datePicker = new DatePicker(LocalDate.now().plusDays(isAnnouncementType ? 7 : 0));
        datePicker.setPrefHeight(46);
        datePicker.setStyle(inputStyle());
        HBox.setHgrow(dateBox, Priority.ALWAYS);
        dateBox.getChildren().add(datePicker);

        VBox timeBox = fieldLabelBox("TIME");
        ComboBox<String> timeCombo = new ComboBox<>();
        timeCombo.getItems().addAll("9:00 AM", "10:00 AM", "11:00 AM", "3:00 PM", "4:00 PM", "5:00 PM");
        timeCombo.setValue("10:00 AM");
        timeCombo.setPrefHeight(46);
        timeCombo.setPrefWidth(160);
        timeCombo.setStyle(inputStyle());
        timeBox.getChildren().add(timeCombo);

        Runnable updateMeta = () -> previewMetaLine.setText(
            "\uD83D\uDCC5 " + (datePicker.getValue() != null ? datePicker.getValue().toString() : LocalDate.now().toString())
            + "   \u23F0 " + timeCombo.getValue()
        );
        datePicker.valueProperty().addListener((obs, oldV, newV) -> updateMeta.run());
        timeCombo.valueProperty().addListener((obs, oldV, newV) -> updateMeta.run());

        scheduleRow.getChildren().addAll(dateBox, timeBox);

        VBox venueField = fieldLabelBox("VENUE");
        TextField venueInput = new TextField("Gram Panchayat Bhavan, Main Hall");
        venueInput.setPrefHeight(46);
        venueInput.setStyle(inputStyle());
        venueField.getChildren().add(venueInput);

        formFieldsContainer.getChildren().addAll(titleField, titleInput, scheduleRow, venueField);

        if (isAnnouncementType) {
            formFieldsContainer.getChildren().addAll(
                buildDynamicListField("AGENDA ITEMS", "e.g. Approval of last meeting's minutes", AI_VIOLET,
                    "Agenda item " + (listItems.size() + 1)),
                buildReminderRow()
            );
            // seed a couple of sensible defaults the first time this type is rendered
            if (listItems.isEmpty()) {
                listItems.addAll("Approval of previous Gram Sabha minutes", "Review of ongoing village development works");
            }
        } else {
            formFieldsContainer.getChildren().addAll(
                buildAttendeesRow(),
                buildDynamicListField("KEY RESOLUTIONS / DECISIONS", "e.g. Approved budget for new water tank", CONTEXT_TEAL,
                    "Resolution " + (listItems.size() + 1))
            );
            if (listItems.isEmpty()) {
                listItems.add("Approved construction of new water tank near Ward 3");
            }
        }

        formFieldsContainer.getChildren().add(buildActionButtonsRow());

        updateMeta.run();
        refreshPreviewList();
        previewTypePill.setText(isAnnouncementType ? "Meeting Announcement" : "Meeting Summary");
        previewTypePill.setStyle("-fx-background-color: " + rgba(accent, 0.16) + "; -fx-background-radius: 999;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: " + accent + ";" +
            "-fx-padding: 5 12 5 12;");
        previewFooterNote.setText(isAnnouncementType
            ? "\uD83D\uDCE2 Will notify all villagers instantly once published."
            : "\uD83D\uDCCB Archived under Gram Sabha records once published.");
    }

    private HBox buildAttendeesRow() {
        HBox row = new HBox(20);
        VBox attendeesBox = fieldLabelBox("ATTENDEES PRESENT");
        Spinner<Integer> attendeesSpinner = new Spinner<>();
        attendeesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5000, 120, 5));
        attendeesSpinner.setEditable(true);
        attendeesSpinner.setPrefHeight(46);
        attendeesSpinner.setPrefWidth(160);
        attendeesSpinner.setStyle(inputStyle());
        attendeesBox.getChildren().add(attendeesSpinner);

        VBox nextMeetingBox = fieldLabelBox("NEXT MEETING DATE (OPTIONAL)");
        DatePicker nextDatePicker = new DatePicker();
        nextDatePicker.setPromptText("Not yet scheduled");
        nextDatePicker.setPrefHeight(46);
        nextDatePicker.setStyle(inputStyle());
        HBox.setHgrow(nextMeetingBox, Priority.ALWAYS);
        nextMeetingBox.getChildren().add(nextDatePicker);

        row.getChildren().addAll(attendeesBox, nextMeetingBox);
        return row;
    }

    private VBox buildReminderRow() {
        VBox box = new VBox(10);
        CheckBox reminderCheck = new CheckBox("Send a reminder to villagers 3 days before the meeting");
        reminderCheck.setSelected(true);
        reminderCheck.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;");
        CheckBox quorumCheck = new CheckBox("Include quorum requirement note in the notice");
        quorumCheck.setSelected(true);
        quorumCheck.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;");
        box.getChildren().addAll(reminderCheck, quorumCheck);
        return box;
    }

    /** A labeled add/remove chip-list field, shared by Agenda Items and Key Resolutions. */
    private VBox buildDynamicListField(String label, String promptText, String accent, String placeholderIfEmpty) {
        VBox box = fieldLabelBox(label);

        FlowPane chipsPane = new FlowPane(8, 8);
        Runnable renderChips = () -> {
            chipsPane.getChildren().clear();
            for (String entry : listItems) {
                chipsPane.getChildren().add(removableChip(entry, accent, chipsPane));
            }
        };

        HBox addRow = new HBox(10);
        addRow.setAlignment(Pos.CENTER_LEFT);
        TextField addField = new TextField();
        addField.setPromptText(promptText);
        addField.setPrefHeight(42);
        addField.setStyle(inputStyle());
        HBox.setHgrow(addField, Priority.ALWAYS);

        Runnable addAction = () -> {
            String text = addField.getText();
            if (text != null && !text.isBlank()) {
                listItems.add(text.trim());
                addField.clear();
                renderChips.run();
                refreshPreviewList();
            }
        };

        Label addBtn = new Label("+  Add");
        addBtn.setPadding(new Insets(10, 18, 10, 18));
        String addBase = "-fx-background-color: " + accent + "; -fx-background-radius: 10;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800; -fx-text-fill: white;" +
            "-fx-cursor: hand;";
        addBtn.setStyle(addBase);
        addBtn.setOnMouseClicked(e -> addAction.run());
        addField.setOnAction(e -> addAction.run());

        addRow.getChildren().addAll(addField, addBtn);

        renderChips.run();
        listItems.addListener((javafx.collections.ListChangeListener<String>) c -> renderChips.run());

        box.getChildren().addAll(chipsPane, addRow);
        return box;
    }

    private Label removableChip(String text, String accent, FlowPane parent) {
        Label chip = new Label(text + "   \u2715");
        chip.setPadding(new Insets(8, 14, 8, 14));
        chip.setStyle("-fx-background-color: " + rgba(accent, 0.12) + "; -fx-background-radius: 999;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: " + accent + ";" +
            "-fx-cursor: hand;");
        chip.setOnMouseClicked(e -> {
            listItems.remove(text);
            refreshPreviewList();
        });
        return chip;
    }

    private HBox buildActionButtonsRow() {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_RIGHT);
        row.setPadding(new Insets(10, 0, 0, 0));

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
            System.out.println("[Publish Gram Sabha Notices] Saved as draft: " + (isAnnouncementType ? "Meeting Announcement" : "Meeting Summary"));
            backToAnnouncementsAction.run();
        });

        String accent = isAnnouncementType ? AI_VIOLET : CONTEXT_TEAL;
        Label publishBtn = new Label("\uD83D\uDCE2  Publish Notice");
        publishBtn.setPadding(new Insets(14, 24, 14, 24));
        String publishBase = "-fx-background-color: linear-gradient(to right, " + accent + ", " + FOREST_DEEP + ");" +
            "-fx-background-radius: 12; -fx-text-fill: white;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 800;" +
            "-fx-effect: dropshadow(gaussian, " + rgba(accent, 0.40) + ", 12, 0.15, 0, 5); -fx-cursor: hand;";
        publishBtn.setStyle(publishBase);
        publishBtn.setOnMouseEntered(e -> publishBtn.setStyle(publishBase +
            "-fx-effect: dropshadow(gaussian, " + rgba(accent, 0.60) + ", 16, 0.2, 0, 6); -fx-translate-y: -1;"));
        publishBtn.setOnMouseExited(e -> publishBtn.setStyle(publishBase));
        publishBtn.setOnMouseClicked(e -> {
            System.out.println("[Publish Gram Sabha Notices] Published: " + (isAnnouncementType ? "Meeting Announcement" : "Meeting Summary"));
            backToAnnouncementsAction.run();
        });

        row.getChildren().addAll(saveDraftBtn, publishBtn);
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

    /* ============================================================
     *  LIVE PREVIEW + SCHEDULE COLUMN
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

        VBox previewCard = new VBox(14);
        previewCard.setPadding(new Insets(24));
        previewCard.setStyle(cardStyle(20));

        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);
        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(38, 38);
        iconChip.setMinSize(38, 38);
        iconChip.setStyle("-fx-background-color: " + rgba(AI_VIOLET, 0.14) + "; -fx-background-radius: 12;");
        Label bIcon = new Label("\uD83C\uDFDB");
        bIcon.setStyle("-fx-font-size: 16px; -fx-text-fill: " + AI_VIOLET + ";");
        iconChip.getChildren().add(bIcon);

        previewTypePill = new Label("Meeting Announcement");
        previewTypePill.setMaxWidth(Region.USE_PREF_SIZE);

        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);

        topRow.getChildren().addAll(iconChip, previewTypePill, grow);

        previewTitle = new Label("Gram Sabha notice title");
        previewTitle.setWrapText(true);
        previewTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 18px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        previewMetaLine = new Label("\uD83D\uDCC5 Date   \u23F0 Time");
        previewMetaLine.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 700; -fx-text-fill: rgba(11,61,46,0.65);");

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: rgba(11,61,46,0.10);");

        Label listHeading = new Label("Agenda");
        listHeading.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: rgba(11,61,46,0.70); -fx-letter-spacing: 0.05em;");

        previewListBox = new VBox(8);

        previewFooterNote = new Label("\uD83D\uDCE2 Will notify all villagers instantly once published.");
        previewFooterNote.setWrapText(true);
        previewFooterNote.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.55); -fx-font-style: italic;");

        previewCard.getChildren().addAll(topRow, previewTitle, previewMetaLine, divider, listHeading, previewListBox, previewFooterNote);

        VBox scheduleCard = buildUpcomingScheduleCard();

        wrap.getChildren().addAll(headRow, previewCard, scheduleCard);
        return wrap;
    }

    private void refreshPreviewList() {
        previewListBox.getChildren().clear();
        String accent = isAnnouncementType ? AI_VIOLET : CONTEXT_TEAL;
        if (listItems.isEmpty()) {
            Label empty = new Label(isAnnouncementType ? "No agenda items added yet." : "No resolutions added yet.");
            empty.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.45); -fx-font-style: italic;");
            previewListBox.getChildren().add(empty);
            return;
        }
        int i = 1;
        for (String entry : listItems) {
            HBox row = new HBox(8);
            row.setAlignment(Pos.TOP_LEFT);
            Label bullet = new Label(String.valueOf(i));
            bullet.setMinWidth(20);
            bullet.setAlignment(Pos.CENTER);
            bullet.setStyle("-fx-background-color: " + rgba(accent, 0.16) + "; -fx-background-radius: 999;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 10.5px; -fx-font-weight: 800; -fx-text-fill: " + accent + ";" +
                "-fx-padding: 2 6 2 6;");
            Label text = new Label(entry);
            text.setWrapText(true);
            text.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.75);");
            HBox.setHgrow(text, Priority.ALWAYS);
            row.getChildren().addAll(bullet, text);
            previewListBox.getChildren().add(row);
            i++;
        }
    }

    /** Small at-a-glance card listing the next few scheduled Gram Sabha meetings (mock data). */
    private VBox buildUpcomingScheduleCard() {
        VBox card = new VBox(14);
        card.setPadding(new Insets(20));
        card.setStyle(cardStyle(20));

        HBox head = new HBox(10);
        head.setAlignment(Pos.CENTER_LEFT);
        Label ic = new Label("\uD83D\uDDD3");
        ic.setStyle("-fx-font-size: 15px;");
        Label lbl = new Label("Upcoming Gram Sabha Schedule");
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        head.getChildren().addAll(ic, lbl);

        String[][] schedule = {
            {"25 Aug 2026", "Village Development Plan Review", SAFFRON_MAIN},
            {"14 Sep 2026", "Annual Budget Discussion", AI_VIOLET},
            {"05 Oct 2026", "Water & Sanitation Follow-up", CONTEXT_TEAL}
        };

        VBox list = new VBox(10);
        for (String[] s : schedule) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            Label dateChip = new Label(s[0]);
            dateChip.setPadding(new Insets(6, 10, 6, 10));
            dateChip.setStyle("-fx-background-color: " + rgba(s[2], 0.14) + "; -fx-background-radius: 8;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: " + s[2] + ";");
            Label desc = new Label(s[1]);
            desc.setWrapText(true);
            desc.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.72);");
            HBox.setHgrow(desc, Priority.ALWAYS);
            row.getChildren().addAll(dateChip, desc);
            list.getChildren().add(row);
        }

        card.getChildren().addAll(head, list);
        return card;
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