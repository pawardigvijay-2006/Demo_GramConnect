package com.tech_fusion.view.sarpanch;

import java.io.File;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * GramConnect - Create Project Page.
 *
 * Reached via the "+ Create Project" call-to-action in the sidebar footer,
 * available from every main page. Lets the Sarpanch draft a new project
 * proposal - budget, duration and plan - with a live preview, then submit
 * it to the BDO for review and fund allocation. Visual language, sidebar
 * and top bar match Publish Gram Sabha Notices / Announcements and every
 * other page in the app.
 */
public class CreateProjectPage {

    private static final String FOREST_DEEP  = "#0B3D2E";
    private static final String FOREST_LIGHT = "#0F4736";
    private static final String SAFFRON_MAIN = "#E07A1F";
    private static final String CONTEXT_TEAL = "#0E8C8C";
    private static final String AI_VIOLET    = "#7C5CFC";
    private static final String DELAYED_RED  = "#D94C38";
    private static final String SIDEBAR_TOP  = "#CDEBD8";
    private static final String SIDEBAR_MID  = "#BCE3CC";
    private static final String SIDEBAR_BOT  = "#A9D8BD";

    private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";
    private static final String BACKGROUND_IMAGE_PATH =
        "C:/Users/Ashish/Downloads/Background File of each Page/Background Image.png";

    private Runnable backToProjectTrackerAction;
    private Runnable backToDashboardAction;

    /** Low / Medium / High. */
    private String selectedPriority = "Medium";

    /* live-preview bound nodes */
    private Label previewName;
    private Label previewCategoryPill;
    private Label previewPriorityPill;
    private Label previewLocation;
    private Label previewBudget;
    private Label previewDuration;
    private Label previewPlanSnippet;

    public Scene getCreateProjectScene(Runnable backToProjectTrackerAction, Runnable backToDashboardAction) {
        this.backToProjectTrackerAction = backToProjectTrackerAction;
        this.backToDashboardAction = backToDashboardAction;

        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundImage(
            new Image(new File(BACKGROUND_IMAGE_PATH).toURI().toString()),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, false, true)
        )));
        root.setLeft(buildSidebar());

        BorderPane contentArea = new BorderPane();
        contentArea.setTop(buildTopBar());
        ScrollPane scroller = new ScrollPane(buildMainContent());
        scroller.setFitToWidth(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        contentArea.setCenter(scroller);
        root.setCenter(contentArea);

        return new Scene(root, 1300, 800);
    }

    /* ============================================================ SIDEBAR ============================================================ */
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
        Label initials = new Label("SP");
        initials.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: white;");
        avatar.getChildren().addAll(avatarCircle, initials);
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
        dashboardNav.setOnMouseClicked(e -> backToDashboardAction.run());

        HBox projectTrackerNav = navItem("\uD83D\uDDC2", "Project Tracker", true);
        projectTrackerNav.setOnMouseClicked(e -> backToProjectTrackerAction.run());

        HBox complaintsNav = navItem("\u26A0", "Complaints", false);
        complaintsNav.setOnMouseClicked(e -> {
            SarpanchComplaintsPage complaintsPage = new SarpanchComplaintsPage();
            SarpanchDashboard.myStage.setTitle("GramConnect - Complaints");
            SarpanchDashboard.myStage.setScene(complaintsPage.getComplaintsScene(backToDashboardAction));
        });

        HBox citizenServicesNav = navItem("\uD83D\uDCC4", "Citizen Services", false);
        citizenServicesNav.setOnMouseClicked(e -> {
            CitizenServicesPage citizenServicesPage = new CitizenServicesPage();
            SarpanchDashboard.myStage.setTitle("GramConnect - Citizen Services");
            SarpanchDashboard.myStage.setScene(citizenServicesPage.getCitizenServicesScene(backToDashboardAction));
        });

        HBox announcementsNav = navItem("\uD83D\uDCE2", "Announcements", false);
        announcementsNav.setOnMouseClicked(e -> {
            AnnouncementsPage announcementsPage = new AnnouncementsPage();
            SarpanchDashboard.myStage.setTitle("GramConnect - Announcements");
            SarpanchDashboard.myStage.setScene(announcementsPage.getAnnouncementsScene(backToDashboardAction));
        });

        nav.getChildren().addAll(dashboardNav, projectTrackerNav, complaintsNav, citizenServicesNav, announcementsNav);
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
        String createBase = "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
            "-fx-background-radius: 12; -fx-text-fill: white; -fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 700;" +
            "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.35), 10, 0.1, 0, 4); -fx-cursor: hand;";
        createBtn.setStyle(createBase);
        createBtn.setOnMouseEntered(e -> createBtn.setStyle(createBase + "-fx-translate-y: -1;"));
        createBtn.setOnMouseExited(e -> createBtn.setStyle(createBase));

        VBox smallLinks = new VBox(4);
        smallLinks.setPadding(new Insets(8, 0, 0, 0));
        smallLinks.getChildren().addAll(footerLink("\u2699", "Settings"), footerLink("\u2753", "Support"));

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
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: " + (active ? "800" : "600") +
            "; -fx-text-fill: " + (active ? SAFFRON_MAIN : "rgba(11,61,46,0.80)") + "; -fx-letter-spacing: 0.05em;");
        item.getChildren().addAll(ic, lbl);
        item.setCursor(Cursor.HAND);
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
        }
        String base = "-fx-background-radius: 10; -fx-background-color: transparent; -fx-cursor: hand;";
        item.setStyle(base);
        item.setOnMouseEntered(e -> item.setStyle("-fx-background-radius: 10; -fx-background-color: rgba(255,255,255,0.45); -fx-cursor: hand;"));
        item.setOnMouseExited(e -> item.setStyle(base));
        return item;
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

    /* ============================================================ TOP BAR ============================================================ */
    private HBox buildTopBar() {
        HBox topBar = new HBox(24);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(72);
        topBar.setPadding(new Insets(0, 32, 0, 32));
        topBar.setStyle("-fx-background-color: rgba(255,255,255,0.92);" +
            "-fx-border-color: transparent transparent rgba(255,255,255,0.6) transparent;" +
            "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 8, 0.1, 0, 2);");

        ImageView logo = new ImageView(new Image("assets/images/ProjectLogo.png"));
        logo.setFitHeight(50);
        logo.setFitWidth(60);

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
        StackPane bellBtn = new StackPane(new Label("\uD83D\uDD14"));
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

        topBar.getChildren().addAll(logo, searchBox, spacer, bell, vDivider, profile);
        return topBar;
    }

    /* ============================================================ MAIN CONTENT ============================================================ */
    private VBox buildMainContent() {
        VBox main = new VBox(28);
        main.setPadding(new Insets(32, 40, 48, 40));
        main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");
        main.getChildren().addAll(buildBreadcrumbAndHeader(), buildFormAndPreviewRow());
        return main;
    }

    private VBox buildBreadcrumbAndHeader() {
        VBox wrap = new VBox(10);

        HBox crumbRow = new HBox(8);
        crumbRow.setAlignment(Pos.CENTER_LEFT);
        Label crumbBack = new Label("\u2190");
        crumbBack.setStyle("-fx-font-size: 13px; -fx-text-fill: " + SAFFRON_MAIN + "; -fx-font-weight: 800;");
        Label crumbTracker = new Label("Project Tracker");
        crumbTracker.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + SAFFRON_MAIN + ";");
        HBox crumbLink = new HBox(6, crumbBack, crumbTracker);
        crumbLink.setAlignment(Pos.CENTER_LEFT);
        crumbLink.setCursor(Cursor.HAND);
        crumbLink.setOnMouseClicked(e -> backToProjectTrackerAction.run());
        Label crumbSep = new Label("/");
        crumbSep.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.35); -fx-font-weight: 700;");
        Label crumbCurrent = new Label("Create Project");
        crumbCurrent.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        crumbRow.getChildren().addAll(crumbLink, crumbSep, crumbCurrent);

        HBox headRow = new HBox(16);
        headRow.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(6);
        Label pageTitle = new Label("Create New Project");
        pageTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label pageSub = new Label("Draft a project proposal with budget and duration, then submit it for BDO review and fund allocation.");
        pageSub.setWrapText(true);
        pageSub.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 15px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.70);");
        titleBox.getChildren().addAll(pageTitle, pageSub);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        Label cancelBtn = new Label("\u2715  Cancel");
        cancelBtn.setPadding(new Insets(12, 20, 12, 20));
        String cancelBase = "-fx-background-color: rgba(255,255,255,0.75); -fx-background-radius: 12;" +
            "-fx-border-color: rgba(11,61,46,0.15); -fx-border-radius: 12; -fx-border-width: 1;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;";
        cancelBtn.setStyle(cancelBase);
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle(cancelBase.replace("0.75", "0.95")));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle(cancelBase));
        cancelBtn.setOnMouseClicked(e -> backToProjectTrackerAction.run());

        headRow.getChildren().addAll(titleBox, cancelBtn);
        wrap.getChildren().addAll(crumbRow, headRow);
        return wrap;
    }

    private HBox buildFormAndPreviewRow() {
        HBox row = new HBox(24);
        row.setAlignment(Pos.TOP_LEFT);

        VBox previewColumn = buildPreviewColumn();
        previewColumn.setPrefWidth(380);
        previewColumn.setMinWidth(340);

        VBox formCard = buildFormCard();
        HBox.setHgrow(formCard, Priority.ALWAYS);

        row.getChildren().addAll(formCard, previewColumn);
        return row;
    }

    /* ============================================================ FORM CARD ============================================================ */
    private VBox buildFormCard() {
        VBox card = new VBox(26);
        card.setPadding(new Insets(30));
        card.setStyle(cardStyle(20));

        // ---- Section: Project Details ----
        TextField nameInput = new TextField();
        nameInput.setPromptText("e.g. Ward 3 Community Hall Repair");
        nameInput.setPrefHeight(46);
        nameInput.setStyle(inputStyle());
        nameInput.textProperty().addListener((obs, o, n) ->
            previewName.setText(n == null || n.isBlank() ? "Your project name" : n));

        VBox categoryBox = fieldLabelBox("CATEGORY");
        ComboBox<String> category = new ComboBox<>();
        category.getItems().addAll("Roads", "Water Supply", "Electricity", "Sanitation", "Education", "Other");
        category.setPromptText("Select category");
        category.setPrefHeight(46);
        category.setMaxWidth(Double.MAX_VALUE);
        category.setStyle(inputStyle());
        category.valueProperty().addListener((obs, o, n) -> {
            previewCategoryPill.setText(n == null ? "Category" : n);
            previewCategoryPill.setVisible(n != null);
            previewCategoryPill.setManaged(n != null);
        });
        categoryBox.getChildren().add(category);

        VBox locationBox = fieldLabelBox("LOCATION / WARD");
        TextField locationInput = new TextField();
        locationInput.setPromptText("Village / Ward");
        locationInput.setPrefHeight(46);
        locationInput.setStyle(inputStyle());
        locationInput.textProperty().addListener((obs, o, n) ->
            previewLocation.setText("\uD83D\uDCCD " + (n == null || n.isBlank() ? "Location not set" : n)));
        locationBox.getChildren().add(locationInput);

        HBox catLocRow = new HBox(20, categoryBox, locationBox);
        HBox.setHgrow(categoryBox, Priority.ALWAYS);
        HBox.setHgrow(locationBox, Priority.ALWAYS);

        VBox priorityBox = fieldLabelBox("PRIORITY");
        priorityBox.getChildren().add(buildPriorityToggle());

        // ---- Section: Budget & Duration ----
        VBox budgetBox = fieldLabelBox("EXPECTED BUDGET (Rs.)");
        TextField budgetInput = new TextField();
        budgetInput.setPromptText("e.g. 150000");
        budgetInput.setPrefHeight(46);
        budgetInput.setStyle(inputStyle());
        budgetInput.textProperty().addListener((obs, o, n) ->
            previewBudget.setText(n == null || n.isBlank() ? "Rs. --" : "Rs. " + n));
        budgetBox.getChildren().add(budgetInput);
        HBox.setHgrow(budgetBox, Priority.ALWAYS);

        VBox durationBox = fieldLabelBox("EXPECTED DURATION");
        HBox durationRow = new HBox(10);
        TextField durationValue = new TextField();
        durationValue.setPromptText("e.g. 3");
        durationValue.setPrefHeight(46);
        durationValue.setPrefWidth(90);
        durationValue.setStyle(inputStyle());
        ComboBox<String> durationUnit = new ComboBox<>();
        durationUnit.getItems().addAll("Days", "Weeks", "Months");
        durationUnit.setValue("Months");
        durationUnit.setPrefHeight(46);
        durationUnit.setStyle(inputStyle());
        Runnable updateDuration = () -> previewDuration.setText("\u23F1 " +
            (durationValue.getText() == null || durationValue.getText().isBlank() ? "--" : durationValue.getText())
            + " " + durationUnit.getValue());
        durationValue.textProperty().addListener((obs, o, n) -> updateDuration.run());
        durationUnit.valueProperty().addListener((obs, o, n) -> updateDuration.run());
        durationRow.getChildren().addAll(durationValue, durationUnit);
        durationBox.getChildren().add(durationRow);
        HBox.setHgrow(durationBox, Priority.ALWAYS);

        HBox budgetDurationRow = new HBox(20, budgetBox, durationBox);

        // ---- Section: Project Plan ----
        VBox planBox = fieldLabelBox("PROJECT PLAN / STRUCTURE");
        TextArea planInput = new TextArea();
        planInput.setPromptText("Describe the scope of work, key milestones, materials needed, and expected community benefit.");
        planInput.setPrefRowCount(5);
        planInput.setWrapText(true);
        planInput.setStyle(inputStyle());
        planInput.textProperty().addListener((obs, o, n) -> previewPlanSnippet.setText(
            n == null || n.isBlank() ? "Project plan will appear here." :
            (n.length() > 160 ? n.substring(0, 160) + "..." : n)));
        planBox.getChildren().add(planInput);

        Label feedback = new Label();
        feedback.setWrapText(true);
        feedback.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + CONTEXT_TEAL + ";");

        HBox buttonsRow = new HBox(14);
        buttonsRow.setAlignment(Pos.CENTER_RIGHT);
        buttonsRow.setPadding(new Insets(6, 0, 0, 0));

        Label saveDraftBtn = new Label("\uD83D\uDCDD  Save as Draft");
        saveDraftBtn.setPadding(new Insets(14, 22, 14, 22));
        String draftBase = "-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 12;" +
            "-fx-border-color: rgba(11,61,46,0.18); -fx-border-radius: 12; -fx-border-width: 1;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;";
        saveDraftBtn.setStyle(draftBase);
        saveDraftBtn.setOnMouseEntered(e -> saveDraftBtn.setStyle(draftBase + "-fx-translate-y: -1;"));
        saveDraftBtn.setOnMouseExited(e -> saveDraftBtn.setStyle(draftBase));
        saveDraftBtn.setOnMouseClicked(e -> {
            feedback.setStyle(feedback.getStyle().replace(DELAYED_RED, CONTEXT_TEAL));
            feedback.setText("Draft saved. You can finish this proposal later.");
        });

        Label submitBtn = new Label("\uD83D\uDCE4  Submit for BDO Approval");
        submitBtn.setPadding(new Insets(14, 24, 14, 24));
        String submitBase = "-fx-background-color: linear-gradient(to right, " + SAFFRON_MAIN + ", " + FOREST_DEEP + ");" +
            "-fx-background-radius: 12; -fx-text-fill: white; -fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 800;" +
            "-fx-effect: dropshadow(gaussian, " + rgba(SAFFRON_MAIN, 0.40) + ", 12, 0.15, 0, 5); -fx-cursor: hand;";
        submitBtn.setStyle(submitBase);
        submitBtn.setOnMouseEntered(e -> submitBtn.setStyle(submitBase + "-fx-translate-y: -1;"));
        submitBtn.setOnMouseExited(e -> submitBtn.setStyle(submitBase));
        submitBtn.setOnMouseClicked(e -> {
            boolean valid = !nameInput.getText().isBlank() && category.getValue() != null
                && !locationInput.getText().isBlank() && !budgetInput.getText().isBlank()
                && !durationValue.getText().isBlank() && !planInput.getText().isBlank();
            if (!valid) {
                feedback.setStyle(feedback.getStyle().replace(CONTEXT_TEAL, DELAYED_RED));
                feedback.setText("Please fill in all fields before submitting.");
                return;
            }
            feedback.setStyle(feedback.getStyle().replace(DELAYED_RED, CONTEXT_TEAL));
            feedback.setText("\u2713  \"" + nameInput.getText() + "\" submitted to the BDO for review and fund allocation.");
        });

        buttonsRow.getChildren().addAll(saveDraftBtn, submitBtn);

        card.getChildren().addAll(
            sectionLabel("\uD83D\uDCCB", "Project Details", AI_VIOLET),
            fieldLabelBox("PROJECT NAME"),
            nameInput,
            catLocRow,
            priorityBox,
            divider(),
            sectionLabel("\uD83D\uDCB0", "Budget & Timeline", SAFFRON_MAIN),
            budgetDurationRow,
            divider(),
            sectionLabel("\uD83D\uDCDD", "Project Plan", CONTEXT_TEAL),
            planBox,
            buttonsRow,
            feedback
        );
        return card;
    }

    private HBox buildPriorityToggle() {
        HBox row = new HBox(10);
        String[][] options = {{"Low", CONTEXT_TEAL}, {"Medium", SAFFRON_MAIN}, {"High", DELAYED_RED}};
        Label[] pills = new Label[options.length];
        for (int i = 0; i < options.length; i++) {
            String level = options[i][0];
            String accent = options[i][1];
            Label pill = new Label(level);
            pill.setPadding(new Insets(10, 20, 10, 20));
            pill.setCursor(Cursor.HAND);
            pill.setStyle(priorityPillStyle(accent, level.equals(selectedPriority)));
            pill.setOnMouseClicked(e -> {
                selectedPriority = level;
                previewPriorityPill.setText(level + " Priority");
                previewPriorityPill.setStyle(previewPillStyle(accent));
                for (int j = 0; j < options.length; j++) {
                    pills[j].setStyle(priorityPillStyle(options[j][1], options[j][0].equals(selectedPriority)));
                }
            });
            pills[i] = pill;
            row.getChildren().add(pill);
        }
        return row;
    }

    private String priorityPillStyle(String accent, boolean selected) {
        if (selected) {
            return "-fx-background-color: " + accent + "; -fx-background-radius: 999;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: white;" +
                "-fx-effect: dropshadow(gaussian, " + rgba(accent, 0.40) + ", 10, 0.2, 0, 3); -fx-cursor: hand;";
        }
        return "-fx-background-color: rgba(240,244,242,0.65); -fx-background-radius: 999;" +
            "-fx-border-color: rgba(11,61,46,0.16); -fx-border-radius: 999; -fx-border-width: 1;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + "; -fx-cursor: hand;";
    }

    /* ============================================================ LIVE PREVIEW COLUMN ============================================================ */
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
        iconChip.setStyle("-fx-background-color: " + rgba(SAFFRON_MAIN, 0.14) + "; -fx-background-radius: 12;");
        Label bIcon = new Label("\uD83C\uDFD7");
        bIcon.setStyle("-fx-font-size: 16px; -fx-text-fill: " + SAFFRON_MAIN + ";");
        iconChip.getChildren().add(bIcon);
        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);
        previewCategoryPill = new Label("Category");
        previewCategoryPill.setPadding(new Insets(5, 12, 5, 12));
        previewCategoryPill.setStyle(previewPillStyle(AI_VIOLET));
        previewCategoryPill.setVisible(false);
        previewCategoryPill.setManaged(false);
        topRow.getChildren().addAll(iconChip, grow, previewCategoryPill);

        previewName = new Label("Your project name");
        previewName.setWrapText(true);
        previewName.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 18px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        previewLocation = new Label("\uD83D\uDCCD Location not set");
        previewLocation.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 700; -fx-text-fill: rgba(11,61,46,0.65);");

        previewPriorityPill = new Label("Medium Priority");
        previewPriorityPill.setMaxWidth(Region.USE_PREF_SIZE);
        previewPriorityPill.setPadding(new Insets(5, 12, 5, 12));
        previewPriorityPill.setStyle(previewPillStyle(SAFFRON_MAIN));

        Region div1 = divider();

        HBox statsRow = new HBox(12);
        VBox budgetStat = statTile("BUDGET", CONTEXT_TEAL);
        previewBudget = (Label) budgetStat.getChildren().get(1);
        VBox durationStat = statTile("DURATION", AI_VIOLET);
        previewDuration = (Label) durationStat.getChildren().get(1);
        HBox.setHgrow(budgetStat, Priority.ALWAYS);
        HBox.setHgrow(durationStat, Priority.ALWAYS);
        statsRow.getChildren().addAll(budgetStat, durationStat);
        previewBudget.setText("Rs. --");
        previewDuration.setText("\u23F1 --");

        Label planHeading = new Label("Project Plan");
        planHeading.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: rgba(11,61,46,0.70); -fx-letter-spacing: 0.05em;");
        previewPlanSnippet = new Label("Project plan will appear here.");
        previewPlanSnippet.setWrapText(true);
        previewPlanSnippet.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.75);");

        Label footerNote = new Label("\uD83D\uDCE4 Will be sent to the BDO for review and fund allocation.");
        footerNote.setWrapText(true);
        footerNote.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.55); -fx-font-style: italic;");

        previewCard.getChildren().addAll(topRow, previewName, previewLocation, previewPriorityPill,
            div1, statsRow, planHeading, previewPlanSnippet, footerNote);

        VBox approvalCard = buildApprovalProcessCard();

        wrap.getChildren().addAll(headRow, previewCard, approvalCard);
        return wrap;
    }

    private VBox statTile(String label, String accent) {
        VBox tile = new VBox(4);
        tile.setPadding(new Insets(12, 14, 12, 14));
        tile.setStyle("-fx-background-color: " + rgba(accent, 0.08) + "; -fx-background-radius: 12;");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 10px; -fx-font-weight: 800; -fx-text-fill: rgba(11,61,46,0.55); -fx-letter-spacing: 0.05em;");
        Label val = new Label("--");
        val.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 15px; -fx-font-weight: 900; -fx-text-fill: " + accent + ";");
        tile.getChildren().addAll(lbl, val);
        return tile;
    }

    /** Small card showing what happens after submission - Submitted -> BDO Review -> Approved & Funded. */
    private VBox buildApprovalProcessCard() {
        VBox card = new VBox(14);
        card.setPadding(new Insets(20));
        card.setStyle(cardStyle(20));

        HBox head = new HBox(10);
        head.setAlignment(Pos.CENTER_LEFT);
        Label ic = new Label("\uD83D\uDCCC");
        ic.setStyle("-fx-font-size: 15px;");
        Label lbl = new Label("What Happens Next");
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
        head.getChildren().addAll(ic, lbl);

        String[][] steps = {
            {"1", "Submitted for Review", "Your proposal is sent to the BDO office.", CONTEXT_TEAL},
            {"2", "BDO Verification", "Budget, duration and plan are reviewed.", SAFFRON_MAIN},
            {"3", "Approved & Funded", "Funds are allocated and the project starts.", AI_VIOLET}
        };

        VBox list = new VBox(12);
        for (String[] s : steps) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.TOP_LEFT);
            Label num = new Label(s[0]);
            num.setMinSize(26, 26);
            num.setPrefSize(26, 26);
            num.setAlignment(Pos.CENTER);
            num.setStyle("-fx-background-color: " + rgba(s[3], 0.16) + "; -fx-background-radius: 999;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: " + s[3] + ";");
            VBox textBox = new VBox(2);
            Label title = new Label(s[1]);
            title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
            Label desc = new Label(s[2]);
            desc.setWrapText(true);
            desc.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.60);");
            textBox.getChildren().addAll(title, desc);
            HBox.setHgrow(textBox, Priority.ALWAYS);
            row.getChildren().addAll(num, textBox);
            list.getChildren().add(row);
        }

        card.getChildren().addAll(head, list);
        return card;
    }

    /* ============================================================ HELPERS ============================================================ */
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

    private Region divider() {
        Region d = new Region();
        d.setPrefHeight(1);
        d.setStyle("-fx-background-color: rgba(11,61,46,0.08);");
        return d;
    }

    private String inputStyle() {
        return "-fx-background-color: rgba(240,244,242,0.65); -fx-background-radius: 12;" +
               "-fx-border-color: rgba(11,61,46,0.14); -fx-border-radius: 12; -fx-border-width: 1;" +
               "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: " + FOREST_DEEP + ";" +
               "-fx-prompt-text-fill: rgba(11,61,46,0.38);";
    }

    private String previewPillStyle(String accent) {
        return "-fx-background-color: " + rgba(accent, 0.16) + "; -fx-background-radius: 999;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: " + accent + ";" +
            "-fx-padding: 5 12 5 12;";
    }

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