package com.tech_fusion.view.sarpanch;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Shared layout and UI helpers for the individual Project Tracker action pages.
 */
public abstract class ProjectTrackerActionPage {

    private static final String FOREST_DEEP = "#0B3D2E";
    private static final String FOREST_LIGHT = "#0F4736";
    private static final String SAFFRON = "#E07A1F";
    private static final String TEAL = "#0E8C8C";
    private static final String VIOLET = "#7C5CFC";
    private static final String RED = "#D94C38";
    private static final String FONT = "'Inter', 'Segoe UI', Arial, sans-serif";
    private static final String SIDEBAR_TOP = "#CDEBD8";
    private static final String SIDEBAR_MID = "#BCE3CC";
    private static final String SIDEBAR_BOT = "#A9D8BD";
    private static final String BACKGROUND_IMAGE_PATH =
        "C:/Users/Ashish/Downloads/Background File of each Page/Background Image.png";

    private Runnable backToProjectTrackerAction;
    private Runnable backToDashboardAction;

    protected Scene createActionScene(String title, String subtitle, VBox actionContent,
                                      Runnable backToProjectTrackerAction, Runnable backToDashboardAction) {
        this.backToProjectTrackerAction = backToProjectTrackerAction;
        this.backToDashboardAction = backToDashboardAction;

        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundImage(
            new javafx.scene.image.Image(new File(BACKGROUND_IMAGE_PATH).toURI().toString()),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(100, 100, true, true, false, true)
        )));
        root.setLeft(buildSidebar());

        BorderPane page = new BorderPane();
        page.setTop(buildTopBar());
        ScrollPane scroller = new ScrollPane(buildContent(title, subtitle, actionContent));
        scroller.setFitToWidth(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        page.setCenter(scroller);
        root.setCenter(page);
        return new Scene(root, 1300, 800);
    }

    /** Matches the complete Dashboard sidebar; only Project Tracker is active. */
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
        avatarCircle.setStroke(Color.web(SAFFRON, 0.85));
        avatarCircle.setStrokeWidth(2.5);
        Label initials = new Label("SP");
        initials.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: white;");
        avatar.getChildren().addAll(avatarCircle, initials);
        VBox nameBox = new VBox(2);
        Label name = new Label("Sarpanch Patil");
        name.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label role = new Label("Gram Panchayat");
        role.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.65); -fx-letter-spacing: 0.05em;");
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
            System.out.println("Complaints clicked");
            SarpanchComplaintsPage complaintsPage = new SarpanchComplaintsPage();
            SarpanchDashboard.myStage.setTitle("GramConnect - Complaints");
            SarpanchDashboard.myStage.setScene(complaintsPage.getComplaintsScene(backToDashboardAction));
        });
        nav.getChildren().addAll(
            dashboardNav,
            projectTrackerNav,
            complaintsNav,
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
        String createBase = "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
            "-fx-background-radius: 12; -fx-text-fill: white; -fx-font-family: " + FONT + "; -fx-font-size: 14px; -fx-font-weight: 700;" +
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

    /** Matches the Dashboard and Project Tracker top navigation bar. */
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
        searchField.setStyle("-fx-background-color: transparent; -fx-font-family: " + FONT + ";" +
            "-fx-font-size: 14px; -fx-text-fill: " + FOREST_DEEP + "; -fx-prompt-text-fill: rgba(11,61,46,0.40);");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBox.getChildren().addAll(searchIcon, searchField);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        StackPane bell = new StackPane();
        StackPane bellButton = new StackPane(new Label("\uD83D\uDD14"));
        bellButton.setPrefSize(42, 42);
        bellButton.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 50; -fx-cursor: hand;");
        Circle notificationDot = new Circle(5, Color.web(SAFFRON));
        notificationDot.setStroke(Color.WHITE);
        notificationDot.setStrokeWidth(2);
        StackPane.setAlignment(notificationDot, Pos.TOP_RIGHT);
        StackPane.setMargin(notificationDot, new Insets(7, 7, 0, 0));
        bell.getChildren().addAll(bellButton, notificationDot);

        Region divider = new Region();
        divider.setPrefSize(1, 32);
        divider.setStyle("-fx-background-color: rgba(11,61,46,0.10);");
        HBox profile = new HBox(10);
        profile.setAlignment(Pos.CENTER_LEFT);
        profile.setPadding(new Insets(6, 12, 6, 6));
        profile.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 4, 0.1, 0, 1); -fx-cursor: hand;");
        Circle profileAvatar = new Circle(16, Color.web(TEAL));
        profileAvatar.setStroke(Color.WHITE);
        profileAvatar.setStrokeWidth(2);
        Label language = new Label("\u092E\u0930\u093E\u0920\u0940");
        language.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");
        Label chevron = new Label("\u25BE");
        chevron.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.7);");
        profile.getChildren().addAll(profileAvatar, language, chevron);

        topBar.getChildren().addAll(logo, searchBox, spacer, bell, divider, profile);
        return topBar;
    }

    private VBox buildContent(String titleText, String subtitleText, VBox actionContent) {
        VBox content = new VBox(24);
        content.setPadding(new Insets(32, 40, 48, 40));
        content.setStyle("-fx-background-color: rgba(240,244,242,0.56);");
        HBox pageHeader = new HBox(16);
        pageHeader.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(6);
        Label title = new Label(titleText);
        title.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label subtitle = mutedLabel(subtitleText);
        subtitle.setStyle(subtitle.getStyle() + "-fx-font-size: 16px;");
        titleBox.getChildren().addAll(title, subtitle);
        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);
        Button back = actionButton("\u2190 Back to Project Tracker", FOREST_DEEP);
        back.setOnAction(e -> backToProjectTrackerAction.run());
        pageHeader.getChildren().addAll(titleBox, grow, back);
        content.getChildren().addAll(pageHeader, actionContent);
        return content;
    }

    protected VBox buildViewAllProjects() {
        VBox content = new VBox(24);
        VBox projectsCard = card();
        projectsCard.getChildren().add(heading("All Village Projects", "Select a project to edit its information or approve its completion."));
        GridPane table = projectTable();
        addProject(table, 1, "Ward 4 Road Metaling", "Main Street", "Rs. 1,20,000", "In Progress", SAFFRON);
        addProject(table, 2, "Water Tank Renovation", "Near School Area", "Rs. 85,000", "Ready for Completion", TEAL);
        addProject(table, 3, "Primary School Repair", "Gram Panchayat Office", "Rs. 2,50,000", "Delayed", RED);
        projectsCard.getChildren().add(table);

        VBox editor = card();
        editor.getChildren().add(heading("Make Required Changes", "The Sarpanch can correct project details before saving or approving completion."));
        ComboBox<String> project = projectSelector("Water Tank Renovation");
        TextField name = textField("Water Tank Renovation");
        TextField location = textField("Near School Area");
        TextField department = textField("Water Supply");
        TextField budget = textField("Rs. 85,000");
        TextField target = textField("20 August 2026");
        TextArea notes = new TextArea("Repair complete. Final inspection photographs attached.");
        notes.setPrefRowCount(3);
        notes.setWrapText(true);
        notes.setStyle(fieldStyle());
        project.setOnAction(e -> populateProject(project.getValue(), name, location, department, budget, target));

        GridPane fields = form();
        addField(fields, "PROJECT NAME", name, 0, 0);
        addField(fields, "LOCATION", location, 1, 0);
        addField(fields, "DEPARTMENT", department, 0, 1);
        addField(fields, "APPROVED BUDGET", budget, 1, 1);
        addField(fields, "TARGET COMPLETION", target, 0, 2);
        addField(fields, "PROJECT NOTES", notes, 1, 2);
        Label feedback = feedbackLabel();
        Button save = actionButton("Save Required Changes", TEAL);
        save.setOnAction(e -> feedback.setText("Changes saved for " + name.getText() + "."));
        Button approve = actionButton("Approve Project Completion", SAFFRON);
        approve.setOnAction(e -> feedback.setText("Project completion approved for " + name.getText() + "."));
        editor.getChildren().addAll(labeled("SELECT PROJECT", project), fields, new HBox(12, save, approve), feedback);
        content.getChildren().addAll(projectsCard, editor);
        return content;
    }

    protected VBox buildAllocateBudget() {
        VBox content = card();
        content.getChildren().add(heading("New Budget Allocation", "Allocate funds only against an approved project."));
        ComboBox<String> project = projectSelector("Ward 4 Road Metaling");
        ComboBox<String> category = new ComboBox<>();
        category.getItems().addAll("Materials", "Labour", "Equipment", "Contingency");
        category.setValue("Materials");
        category.setMaxWidth(Double.MAX_VALUE);
        category.setStyle(fieldStyle());
        TextField amount = textField("Rs. 50,000");
        GridPane fields = form();
        addField(fields, "PROJECT", project, 0, 0);
        addField(fields, "CATEGORY", category, 1, 0);
        addField(fields, "AMOUNT", amount, 0, 1);
        addField(fields, "AVAILABLE BALANCE", readOnlyField("Rs. 1,35,000"), 1, 1);
        TextArea reason = new TextArea();
        reason.setPromptText("Allocation note or supporting reason");
        reason.setPrefRowCount(3);
        reason.setStyle(fieldStyle());
        Label feedback = feedbackLabel();
        Button allocate = actionButton("Allocate Budget", SAFFRON);
        allocate.setOnAction(e -> feedback.setText(amount.getText() + " allocated to " + project.getValue() + "."));
        content.getChildren().addAll(fields, labeled("ALLOCATION NOTE", reason), allocate, feedback);
        return content;
    }

    protected VBox buildBudgetChanges() {
        VBox content = card();
        content.getChildren().add(heading("Pending Budget Change Requests", "Approve a valid revision or return it for clarification."));
        Label feedback = feedbackLabel();
        content.getChildren().addAll(
            budgetRequest("Ward 4 Road Metaling", "Increase materials by Rs. 15,000", "Road-base material price revision", feedback),
            budgetRequest("Primary School Repair", "Move Rs. 10,000 to labour", "Additional electrical repair work", feedback),
            feedback
        );
        return content;
    }

    protected VBox buildProgressMonitor() {
        VBox content = card();
        content.getChildren().add(heading("Project Progress Monitor", "Review progress and record a follow-up for the project team."));
        content.getChildren().addAll(
            progress("Ward 4 Road Metaling", .62, "In Progress", SAFFRON),
            progress("Water Tank Renovation", .95, "Inspection Pending", TEAL),
            progress("Primary School Repair", .38, "Delayed by 8 days", RED)
        );
        TextArea update = new TextArea();
        update.setPromptText("Record a progress update, action owner, or delay reason");
        update.setPrefRowCount(3);
        update.setStyle(fieldStyle());
        Label feedback = feedbackLabel();
        Button save = actionButton("Save Progress Update", FOREST_DEEP);
        save.setOnAction(e -> feedback.setText("Progress update recorded."));
        content.getChildren().addAll(labeled("FOLLOW-UP UPDATE", update), save, feedback);
        return content;
    }

    protected VBox buildAiAnalysis() {
        VBox content = card();
        content.getChildren().add(heading("AI Project Analysis", "Use these insights as decision support before taking an action."));
        content.getChildren().addAll(
            insight("HIGH RISK", "Primary School Repair may miss its target date because two milestones remain incomplete.", RED),
            insight("BUDGET WATCH", "Ward 4 Road Metaling has used 62% of its material budget at 62% completion.", SAFFRON),
            insight("POSITIVE SIGNAL", "Water Tank Renovation evidence is consistent with the reported 95% completion.", TEAL)
        );
        Label feedback = feedbackLabel();
        Button acknowledge = actionButton("Acknowledge Insights", VIOLET);
        acknowledge.setOnAction(e -> feedback.setText("AI insights marked as reviewed."));
        content.getChildren().addAll(acknowledge, feedback);
        return content;
    }

    protected VBox buildGpsVerification() {
        VBox content = card();
        content.getChildren().add(heading("GPS and Timestamp Verification", "Compare submitted site evidence with the approved project location."));
        ComboBox<String> evidence = new ComboBox<>();
        evidence.getItems().addAll("Water Tank Renovation - Site photo #12", "Ward 4 Road Metaling - Site photo #29");
        evidence.setValue("Water Tank Renovation - Site photo #12");
        evidence.setMaxWidth(Double.MAX_VALUE);
        evidence.setStyle(fieldStyle());
        GridPane details = form();
        addField(details, "REPORTED COORDINATES", readOnlyField("18.5204 N, 73.8567 E"), 0, 0);
        addField(details, "PROJECT LOCATION", readOnlyField("Near School Area"), 1, 0);
        addField(details, "CAPTURED ON", readOnlyField("12 August 2026, 10:42 AM"), 0, 1);
        addField(details, "DISTANCE FROM SITE", readOnlyField("18 metres - within allowed range"), 1, 1);
        Label feedback = feedbackLabel();
        Button verify = actionButton("Verify Evidence", TEAL);
        verify.setOnAction(e -> feedback.setText("GPS and timestamp evidence verified."));
        content.getChildren().addAll(labeled("SELECT EVIDENCE", evidence), details, verify, feedback);
        return content;
    }

    private GridPane projectTable() {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(0);
        for (double width : new double[] {35, 25, 18, 22}) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(width);
            grid.getColumnConstraints().add(column);
        }
        String[] headings = {"PROJECT", "LOCATION", "BUDGET", "STATUS"};
        for (int i = 0; i < headings.length; i++) {
            Label label = smallLabel(headings[i]);
            label.setPadding(new Insets(0, 8, 10, 8));
            grid.add(label, i, 0);
        }
        return grid;
    }

    private void addProject(GridPane grid, int row, String name, String location, String budget, String status, String color) {
        Label project = valueLabel(name);
        Label village = valueLabel(location);
        Label money = valueLabel(budget);
        Label state = new Label(status);
        state.setPadding(new Insets(5, 10, 5, 10));
        state.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 999; -fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: white;");
        project.setPadding(new Insets(14, 8, 14, 8));
        village.setPadding(new Insets(14, 8, 14, 8));
        money.setPadding(new Insets(14, 8, 14, 8));
        grid.add(project, 0, row);
        grid.add(village, 1, row);
        grid.add(money, 2, row);
        grid.add(state, 3, row);
    }

    private void populateProject(String project, TextField name, TextField location, TextField department,
                                 TextField budget, TextField target) {
        name.setText(project);
        if (null == project) {
            location.setText("Near School Area"); department.setText("Water Supply"); budget.setText("Rs. 85,000"); target.setText("20 August 2026");
        } else switch (project) {
            case "Ward 4 Road Metaling" -> {
                location.setText("Main Street");
                department.setText("Rural Development");
                budget.setText("Rs. 1,20,000");
                target.setText("15 September 2026");
            }
            case "Primary School Repair" -> {
                location.setText("Gram Panchayat Office");
                department.setText("Infrastructure");
                budget.setText("Rs. 2,50,000");
                target.setText("10 September 2026");
            }
            default -> {
                location.setText("Near School Area");
                department.setText("Water Supply");
                budget.setText("Rs. 85,000");
                target.setText("20 August 2026");
            }
        }
    }

    private VBox budgetRequest(String project, String request, String reason, Label feedback) {
        VBox row = new VBox(8);
        row.setPadding(new Insets(16));
        row.setStyle("-fx-background-color: rgba(11,61,46,0.05); -fx-background-radius: 12;");
        Button approve = actionButton("Approve", TEAL);
        Button returnForChanges = actionButton("Return for changes", RED);
        approve.setOnAction(e -> feedback.setText("Budget change approved for " + project + "."));
        returnForChanges.setOnAction(e -> feedback.setText("Budget change returned for clarification."));
        row.getChildren().addAll(valueLabel(project + " - " + request), mutedLabel(reason), new HBox(10, approve, returnForChanges));
        return row;
    }

    private VBox progress(String name, double percent, String status, String color) {
        VBox row = new VBox(8);
        row.setPadding(new Insets(14, 0, 14, 0));
        HBox header = new HBox(12);
        Label project = valueLabel(name);
        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);
        Label state = new Label(status);
        state.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: " + color + ";");
        header.getChildren().addAll(project, grow, state);
        ProgressBar bar = new ProgressBar(percent);
        bar.setMaxWidth(Double.MAX_VALUE);
        bar.setPrefHeight(12);
        bar.setStyle("-fx-accent: " + color + ";");
        row.getChildren().addAll(header, bar);
        return row;
    }

    private VBox insight(String title, String message, String color) {
        VBox row = new VBox(6);
        row.setPadding(new Insets(16));
        row.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: " + color + "; -fx-border-radius: 12; -fx-border-width: 0 0 0 4;");
        Label heading = new Label(title);
        heading.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 900; -fx-text-fill: " + color + ";");
        row.getChildren().addAll(heading, mutedLabel(message));
        return row;
    }

    private VBox card() {
        VBox card = new VBox(18);
        card.setPadding(new Insets(28));
        card.setStyle("-fx-background-color: rgba(255,255,255,0.92); -fx-background-radius: 20; -fx-border-color: white; -fx-border-radius: 20; -fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 16, 0.1, 0, 4);");
        return card;
    }

    private VBox heading(String title, String description) {
        VBox box = new VBox(4);
        Label name = new Label(title);
        name.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 21px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        box.getChildren().addAll(name, mutedLabel(description));
        return box;
    }

    private HBox navItem(String icon, String text, boolean active) {
        HBox item = new HBox(14);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(14, 16, 14, 16));
        item.setMaxWidth(Double.MAX_VALUE);
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 17px; -fx-text-fill: " + (active ? SAFFRON : FOREST_DEEP) + ";");
        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 14px; -fx-font-weight: " +
            (active ? "800" : "600") + "; -fx-text-fill: " + (active ? SAFFRON : "rgba(11,61,46,0.80)") + "; -fx-letter-spacing: 0.05em;");
        item.getChildren().addAll(iconLabel, textLabel);
        if (active) {
            Region activeBar = new Region();
            activeBar.setPrefWidth(6);
            activeBar.setMinWidth(6);
            activeBar.setStyle("-fx-background-color: " + SAFFRON + "; -fx-background-radius: 8 0 0 8;" +
                "-fx-effect: dropshadow(gaussian, rgba(224,122,31,0.6), 8, 0.3, 0, 0);");
            HBox wrap = new HBox(activeBar, item);
            HBox.setHgrow(item, Priority.ALWAYS);
            wrap.setMaxWidth(Double.MAX_VALUE);
            wrap.setStyle("-fx-background-color: rgba(255,255,255,0.65); -fx-background-radius: 10;" +
                "-fx-effect: innershadow(gaussian, rgba(11,61,46,0.10), 6, 0.2, 0, 1);");
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
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.65);");
        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.65);");
        link.getChildren().addAll(iconLabel, textLabel);
        String base = "-fx-background-radius: 8; -fx-background-color: transparent; -fx-cursor: hand;";
        link.setStyle(base);
        link.setOnMouseEntered(e -> link.setStyle("-fx-background-radius: 8; -fx-background-color: rgba(255,255,255,0.45); -fx-cursor: hand;"));
        link.setOnMouseExited(e -> link.setStyle(base));
        return link;
    }

    private GridPane form() {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(14);
        ColumnConstraints left = new ColumnConstraints(); left.setPercentWidth(50);
        ColumnConstraints right = new ColumnConstraints(); right.setPercentWidth(50);
        grid.getColumnConstraints().addAll(left, right);
        return grid;
    }

    private void addField(GridPane grid, String label, Node field, int column, int row) {
        grid.add(labeled(label, field), column, row);
    }

    private VBox labeled(String label, Node field) {
        VBox box = new VBox(6);
        box.getChildren().addAll(smallLabel(label), field);
        return box;
    }

    private ComboBox<String> projectSelector(String selected) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("Ward 4 Road Metaling", "Water Tank Renovation", "Primary School Repair");
        combo.setValue(selected);
        combo.setMaxWidth(Double.MAX_VALUE);
        combo.setStyle(fieldStyle());
        return combo;
    }

    private TextField textField(String value) {
        TextField field = new TextField(value);
        field.setStyle(fieldStyle());
        return field;
    }

    private TextField readOnlyField(String value) {
        TextField field = textField(value);
        field.setEditable(false);
        field.setStyle(fieldStyle() + "-fx-background-color: rgba(11,61,46,0.05);");
        return field;
    }

    private String fieldStyle() {
        return "-fx-background-color: white; -fx-background-radius: 9; -fx-border-color: rgba(11,61,46,0.16); -fx-border-radius: 9; -fx-padding: 9 12 9 12; -fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-text-fill: " + FOREST_DEEP + ";";
    }

    private Label smallLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: rgba(11,61,46,0.62);");
        return label;
    }

    private Label valueLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");
        return label;
    }

    private Label mutedLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-text-fill: rgba(11,61,46,0.68);");
        return label;
    }

    private Label feedbackLabel() {
        Label label = new Label();
        label.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + TEAL + ";");
        return label;
    }

    private Button actionButton(String text, String color) {
        Button button = new Button(text);
        button.setPadding(new Insets(10, 16, 10, 16));
        button.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 9; -fx-text-fill: white; -fx-font-family: " + FONT + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-cursor: hand;");
        return button;
    }
}