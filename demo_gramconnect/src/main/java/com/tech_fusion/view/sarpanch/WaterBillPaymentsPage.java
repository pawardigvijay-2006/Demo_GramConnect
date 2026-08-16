package com.tech_fusion.view.sarpanch;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * GramConnect - Water Bill Payments Page.
 *
 * Opened from the "Water Bill Payments" action card on CitizenServicesPage.
 * Shows all water bill revenue collected from villagers, broken down
 * individually per household / billing period, along with summary stats,
 * a status filter and a search box. Visual language, sidebar and top bar
 * match CitizenServicesPage exactly, and navigation follows the same
 * Runnable-based pattern used across the app.
 */
public class WaterBillPaymentsPage {

    /* ---------- Color palette (kept identical to the other pages) ---------- */
    private static final String FOREST_DEEP   = "#0B3D2E";
    private static final String FOREST_LIGHT  = "#0F4736";
    private static final String SAFFRON_MAIN  = "#E07A1F";
    private static final String CONTEXT_TEAL  = "#0E8C8C";
    //private static final String AI_VIOLET     = "#7C5CFC";
    private static final String DELAYED_RED   = "#D94C38";
    private static final String SIDEBAR_TOP   = "#CDEBD8";
    private static final String SIDEBAR_MID   = "#Bce3cc";
    private static final String SIDEBAR_BOT   = "#A9D8BD";

    private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

    private static final String BACKGROUND_IMAGE_PATH =
        "/assets/images/BackgroundImage.png";

    private Runnable backToCitizenServicesAction;
    private Runnable backToDashboardAction;

    /* ============================================================
     *  DATA MODEL
     * ============================================================ */
    public static class WaterBillRecord {
        private final SimpleStringProperty villagerName;
        private final SimpleStringProperty householdId;
        private final SimpleStringProperty ward;
        private final SimpleStringProperty billingPeriod;
        private final SimpleStringProperty unitsConsumed;
        private final SimpleStringProperty amount;
        private final SimpleStringProperty status;
        private final SimpleStringProperty paymentDate;

        public WaterBillRecord(String villagerName, String householdId, String ward, String billingPeriod,
                                String unitsConsumed, String amount, String status, String paymentDate) {
            this.villagerName = new SimpleStringProperty(villagerName);
            this.householdId = new SimpleStringProperty(householdId);
            this.ward = new SimpleStringProperty(ward);
            this.billingPeriod = new SimpleStringProperty(billingPeriod);
            this.unitsConsumed = new SimpleStringProperty(unitsConsumed);
            this.amount = new SimpleStringProperty(amount);
            this.status = new SimpleStringProperty(status);
            this.paymentDate = new SimpleStringProperty(paymentDate);
        }

        public String getVillagerName() { return villagerName.get(); }
        public String getHouseholdId() { return householdId.get(); }
        public String getWard() { return ward.get(); }
        public String getBillingPeriod() { return billingPeriod.get(); }
        public String getUnitsConsumed() { return unitsConsumed.get(); }
        public String getAmount() { return amount.get(); }
        public String getStatus() { return status.get(); }
        public String getPaymentDate() { return paymentDate.get(); }

        /** Numeric amount, stripped of currency symbol/commas, used for summary totals. */
        public double getAmountValue() {
            try {
                return Double.parseDouble(amount.get().replace("\u20B9", "").replace(",", "").trim());
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
    }

    /** Sample water bill revenue records, one row per villager/household per billing period. */
    private List<WaterBillRecord> loadRecords() {
        List<WaterBillRecord> records = new ArrayList<>();
        records.add(new WaterBillRecord("Ramesh Kadam", "HH-101", "Ward 1", "Jul 2026", "12 KL", "\u20B9480", "Paid", "05 Aug 2026"));
        records.add(new WaterBillRecord("Sunita Jadhav", "HH-102", "Ward 1", "Jul 2026", "9 KL", "\u20B9360", "Paid", "03 Aug 2026"));
        records.add(new WaterBillRecord("Vitthal More", "HH-103", "Ward 2", "Jul 2026", "15 KL", "\u20B9600", "Pending", "\u2014"));
        records.add(new WaterBillRecord("Anita Pawar", "HH-104", "Ward 2", "Jul 2026", "7 KL", "\u20B9280", "Paid", "07 Aug 2026"));
        records.add(new WaterBillRecord("Ganesh Shinde", "HH-105", "Ward 3", "Jul 2026", "11 KL", "\u20B9440", "Overdue", "\u2014"));
        records.add(new WaterBillRecord("Kavita Deshmukh", "HH-106", "Ward 3", "Jul 2026", "10 KL", "\u20B9400", "Paid", "02 Aug 2026"));
        records.add(new WaterBillRecord("Prakash Bhosale", "HH-107", "Ward 1", "Jun 2026", "13 KL", "\u20B9520", "Paid", "06 Jul 2026"));
        records.add(new WaterBillRecord("Meera Kale", "HH-108", "Ward 4", "Jul 2026", "8 KL", "\u20B9320", "Pending", "\u2014"));
        records.add(new WaterBillRecord("Suresh Gaikwad", "HH-109", "Ward 4", "Jul 2026", "14 KL", "\u20B9560", "Paid", "08 Aug 2026"));
        records.add(new WaterBillRecord("Lata Patil", "HH-110", "Ward 2", "Jun 2026", "9 KL", "\u20B9360", "Paid", "04 Jul 2026"));
        records.add(new WaterBillRecord("Dinesh Wagh", "HH-111", "Ward 5", "Jul 2026", "16 KL", "\u20B9640", "Overdue", "\u2014"));
        records.add(new WaterBillRecord("Sarika Chavan", "HH-112", "Ward 5", "Jul 2026", "10 KL", "\u20B9400", "Paid", "05 Aug 2026"));
        return records;
    }

    /**
     * Builds the Water Bill Payments scene and returns it.
     */
    public Scene getWaterBillPaymentsScene(Runnable backToCitizenServicesAction, Runnable backToDashboardAction) {
        this.backToCitizenServicesAction = backToCitizenServicesAction;
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
     *  SIDEBAR  (identical to CitizenServicesPage, "Citizen Services" active)
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
        dashboardNav.setOnMouseClicked(e -> backToDashboardAction.run());

        HBox projectTrackerNav = navItem("\uD83D\uDDC2", "Project Tracker", false);
        projectTrackerNav.setOnMouseClicked(e -> {
            ProjectTrackerPage projectTrackerPage = new ProjectTrackerPage();
            SarpanchDashboard.myStage.setScene(projectTrackerPage.getProjectTrackerScene(backToDashboardAction));
        });

        HBox complaintsNav = navItem("\u26A0", "Complaints", false);
        complaintsNav.setOnMouseClicked(e -> {
            SarpanchComplaintsPage complaintsPage = new SarpanchComplaintsPage();
            SarpanchDashboard.myStage.setTitle("GramConnect - Complaints");
            SarpanchDashboard.myStage.setScene(complaintsPage.getComplaintsScene(backToDashboardAction));
        });

        HBox citizenServicesNav = navItem("\uD83D\uDCC4", "Citizen Services", true);
        citizenServicesNav.setOnMouseClicked(e -> backToCitizenServicesAction.run());

        HBox announcementsNav = navItem("\uD83D\uDCE2", "Announcements", false);
        announcementsNav.setOnMouseClicked(e -> {
            AnnouncementsPage announcementsPage = new AnnouncementsPage();
            SarpanchDashboard.myStage.setTitle("GramConnect - Announcements");
            SarpanchDashboard.myStage.setScene(announcementsPage.getAnnouncementsScene(backToDashboardAction));
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
            wrap.setCursor(javafx.scene.Cursor.HAND);
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

        Image projectLogo = new Image("/assets/images/ProjectLogo.png");
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
        searchField.setPromptText("Search citizen requests...");
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
    private ObservableList<WaterBillRecord> allRecords;
    private FilteredList<WaterBillRecord> filteredRecords;

    private VBox buildMainContent() {
        VBox main = new VBox(28);
        main.setPadding(new Insets(32, 40, 48, 40));
        main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");

        HBox breadcrumb = new HBox(8);
        breadcrumb.setAlignment(Pos.CENTER_LEFT);
        Label backArrow = new Label("\u2190 Citizen Services");
        backArrow.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700;" +
            "-fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");
        backArrow.setOnMouseClicked(e -> backToCitizenServicesAction.run());
        breadcrumb.getChildren().add(backArrow);

        VBox welcome = new VBox(6);
        Label welcomeTitle = new Label("Water Bill Payments");
        welcomeTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label welcomeSub = new Label("All water bill revenue collected from villagers, broken down by household and billing period.");
        welcomeSub.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.70);");
        welcomeSub.setWrapText(true);
        welcome.getChildren().addAll(welcomeTitle, welcomeSub);

        List<WaterBillRecord> records = loadRecords();
        allRecords = FXCollections.observableArrayList(records);
        filteredRecords = new FilteredList<>(allRecords, r -> true);

        double totalCollected = 0;
        int paidCount = 0, pendingCount = 0, overdueCount = 0;
        for (WaterBillRecord r : records) {
            if (null != r.getStatus()) switch (r.getStatus()) {
                case "Paid" -> {
                    totalCollected += r.getAmountValue();
                    paidCount++;
                }
                case "Pending" -> pendingCount++;
                case "Overdue" -> overdueCount++;
                default -> {
                }
            }
        }

        HBox statsRow = new HBox(24);
        statsRow.setAlignment(Pos.TOP_LEFT);
        VBox s1 = kpiCard(CONTEXT_TEAL, "\u20B9", "TOTAL REVENUE COLLECTED", "\u20B9" + String.format("%,.0f", totalCollected));
        VBox s2 = kpiCard(FOREST_DEEP, "\uD83D\uDCC4", "TOTAL RECORDS", String.valueOf(records.size()));
        VBox s3 = kpiCard(SAFFRON_MAIN, "\u2713", "PAID", String.valueOf(paidCount));
        VBox s4 = kpiCard(DELAYED_RED, "!", "PENDING / OVERDUE", String.valueOf(pendingCount + overdueCount));
        HBox.setHgrow(s1, Priority.ALWAYS);
        HBox.setHgrow(s2, Priority.ALWAYS);
        HBox.setHgrow(s3, Priority.ALWAYS);
        HBox.setHgrow(s4, Priority.ALWAYS);
        statsRow.getChildren().addAll(s1, s2, s3, s4);

        HBox toolsRow = buildToolsRow();
        TableView<WaterBillRecord> table = buildTable();

        main.getChildren().addAll(breadcrumb, welcome, statsRow, toolsRow, table);
        return main;
    }

    private HBox buildToolsRow() {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(4, 0, 4, 0));

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 16, 0, 16));
        searchBox.setPrefWidth(340);
        searchBox.setPrefHeight(42);
        searchBox.setStyle(cardStyle(12));
        Label searchIcon = new Label("\uD83D\uDD0D");
        searchIcon.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.5);");
        TextField searchField = new TextField();
        searchField.setPromptText("Search by villager or household ID...");
        searchField.setStyle("-fx-background-color: transparent; -fx-font-family: " + FONT_FAMILY + ";" +
            "-fx-font-size: 14px; -fx-text-fill: " + FOREST_DEEP + "; -fx-prompt-text-fill: rgba(11,61,46,0.40);");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBox.getChildren().addAll(searchIcon, searchField);

        Label filterLbl = new Label("Status:");
        filterLbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");

        ComboBox<String> statusFilter = new ComboBox<>(FXCollections.observableArrayList("All", "Paid", "Pending", "Overdue"));
        statusFilter.setValue("All");
        statusFilter.setPrefHeight(42);
        statusFilter.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-background-radius: 10;");

        searchField.textProperty().addListener((obs, oldV, newV) -> applyFilter(searchField.getText(), statusFilter.getValue()));
        statusFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilter(searchField.getText(), statusFilter.getValue()));

        row.getChildren().addAll(searchBox, filterLbl, statusFilter);
        return row;
    }

    private void applyFilter(String query, String status) {
        String q = query == null ? "" : query.trim().toLowerCase();
        filteredRecords.setPredicate(r -> {
            boolean matchesQuery = q.isEmpty()
                || r.getVillagerName().toLowerCase().contains(q)
                || r.getHouseholdId().toLowerCase().contains(q)
                || r.getWard().toLowerCase().contains(q);
            boolean matchesStatus = "All".equals(status) || r.getStatus().equals(status);
            return matchesQuery && matchesStatus;
        });
    }

    @SuppressWarnings("unchecked")
    private TableView<WaterBillRecord> buildTable() {
        TableView<WaterBillRecord> table = new TableView<>();
        table.setItems(filteredRecords);
        table.setStyle(cardStyle(16) + " -fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px;");
        table.setPrefHeight(480);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<WaterBillRecord, String> nameCol = new TableColumn<>("Villager");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("villagerName"));

        TableColumn<WaterBillRecord, String> hhCol = new TableColumn<>("Household ID");
        hhCol.setCellValueFactory(new PropertyValueFactory<>("householdId"));

        TableColumn<WaterBillRecord, String> wardCol = new TableColumn<>("Ward");
        wardCol.setCellValueFactory(new PropertyValueFactory<>("ward"));

        TableColumn<WaterBillRecord, String> periodCol = new TableColumn<>("Billing Period");
        periodCol.setCellValueFactory(new PropertyValueFactory<>("billingPeriod"));

        TableColumn<WaterBillRecord, String> unitsCol = new TableColumn<>("Units Consumed");
        unitsCol.setCellValueFactory(new PropertyValueFactory<>("unitsConsumed"));

        TableColumn<WaterBillRecord, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<WaterBillRecord, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new javafx.scene.control.TableCell<WaterBillRecord, String>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) { setText(null); setStyle(""); return; }
                setText(value);
                String color = "Paid".equals(value) ? CONTEXT_TEAL : "Pending".equals(value) ? SAFFRON_MAIN : DELAYED_RED;
                setStyle("-fx-text-fill: " + color + "; -fx-font-weight: 800;");
            }
        });

        TableColumn<WaterBillRecord, String> dateCol = new TableColumn<>("Payment Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        table.getColumns().addAll(nameCol, hhCol, wardCol, periodCol, unitsCol, amountCol, statusCol, dateCol);
        return table;
    }

    /* ============================================================
     *  KPI CARD (same visual language as CitizenServicesPage)
     * ============================================================ */
    private VBox kpiCard(String accent, String icon, String labelText, String statText) {
        VBox card = new VBox();
        card.setPrefWidth(300);
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
        stat.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 28px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        inner.getChildren().addAll(head, stat);
        card.getChildren().addAll(strip, inner);
        return card;
    }

    /* ============================================================
     *  HELPERS
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