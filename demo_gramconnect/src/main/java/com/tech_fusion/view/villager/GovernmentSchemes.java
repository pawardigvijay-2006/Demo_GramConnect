package com.tech_fusion.view.villager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * GramConnect - Government Schemes
 *
 * ============================================================
 * THEME NOTE
 * ============================================================
 * This page reuses the EXACT same visual language as
 * VillagerDashboard.java: same color constants (forest green +
 * saffron accent), the same translucent "glass card" look with a
 * soft drop-shadow, the same hover-lift effect, and the same header
 * style (rounded pill search box, bell chip, avatar with saffron
 * ring). The cardStyle()/addHoverLift()/rgba() helpers below are
 * copied verbatim from VillagerDashboard.java so this file stays
 * self-contained and drops straight into the same package.
 *
 * Wired the same way as ProjectTransparency and ComplaintsPage:
 * VillagerDashboard's handleNavigation() would call
 *   root.setCenter(new GovernmentSchemes().getSchemesPane());
 * for the "Government schemes" sidebar item. getSchemesPane()
 * returns a full BorderPane (header + scrollable content) because
 * the sidebar navigation swaps out the whole center region, not
 * just the content below the header.
 *
 * ARCHITECTURE NOTE: The scheme list below is hardcoded mock data,
 * marked with a TODO showing where a real SchemeService would plug
 * in later.
 * ============================================================
 */
public class GovernmentSchemes{

    // ================= COLORS (copied from VillagerDashboard.java) =================
    private static final String FOREST_DEEP = "#0B3D2E";
    private static final String FOREST_LIGHT = "#0F4736";
    private static final String SAFFRON_MAIN = "#E07A1F";
    private static final String CONTEXT_TEAL = "#0E8C8C";
    private static final String DELAYED_RED = "#D94C38";

    private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

    private static final String BACKGROUND = "#EFF5F1";

    private static final String TEXT_PRIMARY = FOREST_DEEP;
    private static final String TEXT_SECONDARY = "rgba(11,61,46,0.65)";

    private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";
    
    

    private String selectedCategory = "All Schemes";

    // Kept only for standalone preview - the real app builds this pane inside
    // VillagerDashboard's existing root/sidebar via getSchemesPane().
//     @Override
//     public void start(Stage stage) {
//         BorderPane root = new BorderPane();
//         root.setStyle("-fx-background-color: " + BACKGROUND + ";");
//         root.setCenter(getSchemesPane());

//         Scene scene = new Scene(root, 1500, 850);
//         stage.setTitle("GramConnect - Government Schemes");
//         stage.setScene(scene);
//         stage.setMinWidth(1000);
//         stage.setMinHeight(700);
//         stage.show();
//     }

    /**
     * Public entry point used by VillagerDashboard's sidebar navigation:
     *   root.setCenter(new GovernmentSchemes().getSchemesPane());
     * Returns the full page (header on top, scrollable content below),
     * exactly like ProjectTransparency.getProjectBPane().
     */
    public Scene getSchemesScene(Runnable backAction) {
        
        BorderPane pane = new BorderPane();
        pane.setTop(buildHeader());
        pane.setCenter(buildScrollableContent());
        BorderPane root = new BorderPane();
        root.setLeft(buildSidebar(backAction));
        root.setCenter(pane);
        return new Scene(root, 1500, 850);
    }

    private VBox buildSidebar(Runnable backToDashboardAction) {
                VBox sidebar = new VBox();
                sidebar.setPrefWidth(230);
                sidebar.setMinWidth(230);
                sidebar.setStyle(
                                "-fx-background-color: linear-gradient(to bottom, " + SIDEBAR_TOP + ", " + SIDEBAR_MID
                                                + ", " + SIDEBAR_BOT + ");"
                                                + "-fx-border-color: transparent rgba(11,61,46,0.10) transparent transparent;"
                                                + "-fx-border-width: 0 1 0 0;"
                                                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.20), 24, 0.2, 4, 0);");

                Image logoImage = new Image("assets\\images\\gramconnect.png");
                ImageView logoIcon = new ImageView(logoImage);
                logoIcon.setFitWidth(60);
                logoIcon.setFitHeight(60);
                logoIcon.setPreserveRatio(true);
                logoIcon.setSmooth(true);

                Label logoText = new Label("GramConnect");
                logoText.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-text-fill: " + FOREST_DEEP + ";"
                                                + "-fx-font-size: 18px;"
                                                + "-fx-font-weight: 900;");

                Label subtitle = new Label("Village Governance");
                subtitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-text-fill: rgba(11,61,46,0.65);"
                                                + "-fx-font-size: 9px;"
                                                + "-fx-font-weight: 700;"
                                                + "-fx-letter-spacing: 0.05em;");

                VBox logoTextBox = new VBox(0, logoText, subtitle);
                HBox logo = new HBox(8, logoIcon, logoTextBox);
                logo.setAlignment(Pos.CENTER_LEFT);
                VBox logoBox = new VBox(logo);
                logoBox.setPadding(new Insets(18, 18, 22, 18));

                // ---------------- Nav items ----------------
                // "Dashboard" is the only item that needs to actually navigate
                // anywhere from this page - it just calls the Runnable it was
                // handed. The rest are inactive placeholders for now, same as
                // they'd be on any page other than their own.

                Label dashboardNav = navItem("\uD83C\uDFE0  Dashboard", false);
                dashboardNav.setOnMouseClicked(e -> {
                        backToDashboardAction.run();
                });

                Label projectsNav = navItem("\uD83C\uDFD7  Project transparency", false);
                projectsNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new ProjectTransparency().getProjectScene(backToDashboardAction));
                });

                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", false);
                complaintsNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new ComplaintsPage().getComplaintsPage(backToDashboardAction));
                });
                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", true);
                
                Label certificatesNav = navItem("\uD83D\uDCDC  Certificates", false);
                certificatesNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new Certificates().getCertificatesScene(backToDashboardAction));
                });
                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", false);
                billsNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new BillsAndPayments().getBillsScene(backToDashboardAction));
                });
                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                announcementsNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new Announcements().getAnnouncementScene(backToDashboardAction));
                });
                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", false);
                gramSabhaNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new GramSabha().getGramSabhaScene(backToDashboardAction));
                });
                Label aiAssistantNav = navItem("\uD83E\uDD16  AI village assistant", false);
                aiAssistantNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new AIAssistant().getAiAssiatantScene(backToDashboardAction));
                });
                // TODO: wire these the same way once each page exposes its own
                // getXScene(Runnable backToDashboardAction) method.

                VBox navItems = new VBox(4,
                                dashboardNav,
                                projectsNav,
                                complaintsNav,
                                schemesNav,
                                certificatesNav,
                                billsNav,
                                announcementsNav,
                                gramSabhaNav,
                                aiAssistantNav);
                navItems.setPadding(new Insets(0, 10, 0, 10));
                VBox.setVgrow(navItems, Priority.ALWAYS);

                Label emergency = new Label("\u26A0  Emergency assistance");
                emergency.setWrapText(true);
                emergency.setPadding(new Insets(10, 12, 10, 12));
                emergency.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-text-fill: " + DELAYED_RED + ";"
                                                + "-fx-font-size: 12px;"
                                                + "-fx-font-weight: 700;"
                                                + "-fx-background-color: rgba(217,76,56,0.12);"
                                                + "-fx-background-radius: 10;");
                VBox emergencyBox = new VBox(emergency);
                emergencyBox.setPadding(new Insets(12, 16, 18, 18));

                sidebar.getChildren().addAll(logoBox, navItems, emergencyBox);
                return sidebar;
        }

        private Label navItem(String text, boolean active) {
                Label item = new Label(text);
                item.setMaxWidth(Double.MAX_VALUE);
                item.setPadding(new Insets(10, 14, 10, 14));

                if (active) {
                        item.setStyle(
                                        "-fx-font-family: " + FONT_FAMILY + ";"
                                                        + "-fx-background-color: rgba(255,255,255,0.65);"
                                                        + "-fx-text-fill: " + SAFFRON_MAIN + ";"
                                                        + "-fx-font-weight: 800;"
                                                        + "-fx-font-size: 13px;"
                                                        + "-fx-background-radius: 8;"
                                                        + "-fx-border-color: " + SAFFRON_MAIN
                                                        + " transparent transparent transparent;"
                                                        + "-fx-border-width: 0 0 0 4;"
                                                        + "-fx-border-radius: 8;"
                                                        + "-fx-cursor: hand;");
                } else {
                        String base = "-fx-font-family: " + FONT_FAMILY + ";"
                                        + "-fx-text-fill: rgba(11,61,46,0.80);"
                                        + "-fx-font-size: 13px;"
                                        + "-fx-font-weight: 700;"
                                        + "-fx-cursor: hand;";
                        item.setStyle(base);

                        item.setOnMouseEntered(e -> item.setStyle(
                                        "-fx-font-family: " + FONT_FAMILY + ";"
                                                        + "-fx-background-color: rgba(255,255,255,0.45);"
                                                        + "-fx-text-fill: " + FOREST_DEEP + ";"
                                                        + "-fx-font-weight: 700;"
                                                        + "-fx-font-size: 13px;"
                                                        + "-fx-background-radius: 8;"
                                                        + "-fx-cursor: hand;"));

                        item.setOnMouseExited(e -> item.setStyle(base));
                }

                return item;
        }

    // =================================================================
    // HEADER (same glass-bar style as VillagerDashboard, plus a language
    // globe icon between the search box and the notification bell)
    // =================================================================
    private HBox buildHeader() {
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 28, 14, 28));
        header.setStyle(
                "-fx-background-color: rgba(255,255,255,0.92);"
                        + "-fx-border-color: transparent transparent rgba(255,255,255,0.6) transparent;"
                        + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 8, 0.1, 0, 2);");

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 16, 0, 16));
        searchBox.setPrefHeight(38);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        searchBox.setStyle(
                "-fx-background-color: " + BACKGROUND + ";"
                        + "-fx-background-radius: 20;"
                        + "-fx-border-color: rgba(11,61,46,0.10);"
                        + "-fx-border-radius: 20;"
                        + "-fx-border-width: 1;");
        Label searchIcon = new Label("\uD83D\uDD0D");
        searchIcon.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.5);");
        TextField search = new TextField();
        search.setPromptText("Search schemes, departments, or benefits...");
        search.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-font-size: 12px;"
                        + "-fx-text-fill: " + FOREST_DEEP + ";"
                        + "-fx-prompt-text-fill: rgba(11,61,46,0.40);");
        HBox.setHgrow(search, Priority.ALWAYS);
        searchBox.getChildren().addAll(searchIcon, search);

        StackPane globe = new StackPane(new Label("\uD83C\uDF10"));
        globe.setPrefSize(34, 34);
        globe.setMaxSize(34, 34);
        ((Label) globe.getChildren().get(0)).setStyle("-fx-font-size: 15px;");
        globe.setStyle("-fx-background-color: " + BACKGROUND + "; -fx-background-radius: 999;");

        StackPane bell = new StackPane(new Label("\uD83D\uDD14"));
        bell.setPrefSize(38, 38);
        bell.setMaxSize(38, 38);
        ((Label) bell.getChildren().get(0)).setStyle("-fx-font-size: 15px;");
        bell.setStyle(
                "-fx-background-color: " + BACKGROUND + ";"
                        + "-fx-background-radius: 999;"
                        + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 4, 0.1, 0, 1);");

        StackPane avatar = new StackPane(new Label("RP"));
        avatar.setPrefSize(34, 34);
        avatar.setMaxSize(34, 34);
        avatar.setStyle(
                "-fx-background-color: " + FOREST_DEEP + ";"
                        + "-fx-background-radius: 18;"
                        + "-fx-border-color: " + SAFFRON_MAIN + ";"
                        + "-fx-border-width: 2;"
                        + "-fx-border-radius: 18;");
        ((Label) avatar.getChildren().get(0))
                .setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");

        Label name = new Label("Ramesh Patel");
        name.setStyle(
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: "
                        + TEXT_PRIMARY + ";");

        HBox profile = new HBox(8, avatar, name);
        profile.setAlignment(Pos.CENTER_LEFT);

        header.getChildren().addAll(searchBox, globe, bell, profile);
        return header;
    }

    // =================================================================
    // SCROLLABLE CONTENT
    // =================================================================
    private ScrollPane buildScrollableContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(24, 32, 32, 32));
        content.setStyle("-fx-background-color: " + BACKGROUND + ";");

        content.getChildren().addAll(
                buildBreadcrumb(),
                buildTitleRow(),
                buildCategoryTabs(),
                buildSectionHeader(),
                buildSchemeList());

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scrollPane;
    }

    // ---- Breadcrumb ----
    private HBox buildBreadcrumb() {
        Label services = new Label("Services");
        services.setStyle(
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: " + CONTEXT_TEAL + ";");
        Label separator = new Label("\u203A");
        separator.setStyle(
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");
        Label current = new Label("Government Schemes");
        current.setStyle(
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: "
                        + TEXT_PRIMARY + ";");

        HBox crumb = new HBox(6, services, separator, current);
        crumb.setAlignment(Pos.CENTER_LEFT);
        return crumb;
    }

    // ---- Title row: heading + subtitle on the left, Filters/My Applications on the right ----
    private HBox buildTitleRow() {
        Label title = new Label("Government Schemes");
        title.setStyle(
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 28px; -fx-font-weight: 900; -fx-text-fill: "
                        + TEXT_PRIMARY + ";");
        Label subtitle = new Label(
                "Explore and apply for central and state government welfare schemes tailored to your profile and community needs.");
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(560);
        subtitle.setStyle(
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_SECONDARY + ";");
        VBox textBox = new VBox(6, title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button filtersBtn = new Button("\u2261  Filters");
        filtersBtn.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-background-color: white;"
                        + "-fx-text-fill: " + TEXT_PRIMARY + ";"
                        + "-fx-font-size: 12px; -fx-font-weight: 700;"
                        + "-fx-border-color: rgba(11,61,46,0.15); -fx-border-radius: 8;"
                        + "-fx-background-radius: 8; -fx-padding: 9 16 9 16; -fx-cursor: hand;");

        Button myApplicationsBtn = new Button("\u21BB  My Applications");
        myApplicationsBtn.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 12px; -fx-font-weight: 800;"
                        + "-fx-background-radius: 8; -fx-padding: 9 16 9 16; -fx-cursor: hand;"
                        + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.30), 8, 0.1, 0, 3);");

        HBox buttonRow = new HBox(10, filtersBtn, myApplicationsBtn);
        buttonRow.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(textBox, spacer, buttonRow);
        row.setAlignment(Pos.TOP_LEFT);
        return row;
    }

    // ---- Category filter tabs ----
    private HBox buildCategoryTabs() {
        HBox tabs = new HBox(10,
                categoryTab("All Schemes"),
                categoryTab("Agriculture"),
                categoryTab("Health"),
                categoryTab("Education"),
                categoryTab("Housing"));
        return tabs;
    }

    private Label categoryTab(String text) {
        boolean active = text.equals(selectedCategory);
        Label tab = new Label(text);
        tab.setPadding(new Insets(8, 18, 8, 18));
        if (active) {
            tab.setStyle(
                    "-fx-font-family: " + FONT_FAMILY + ";"
                            + "-fx-background-color: " + FOREST_DEEP + ";"
                            + "-fx-text-fill: white;"
                            + "-fx-font-size: 12px; -fx-font-weight: 800;"
                            + "-fx-background-radius: 999;");
        } else {
            tab.setStyle(
                    "-fx-font-family: " + FONT_FAMILY + ";"
                            + "-fx-background-color: white;"
                            + "-fx-text-fill: " + TEXT_PRIMARY + ";"
                            + "-fx-font-size: 12px; -fx-font-weight: 700;"
                            + "-fx-background-radius: 999;"
                            + "-fx-border-color: rgba(11,61,46,0.15); -fx-border-radius: 999;"
                            + "-fx-cursor: hand;");
        }
        return tab;
    }

    // ---- Section header: icon chip + category title + active-count caption ----
    private HBox buildSectionHeader() {
        Label icon = new Label("\uD83D\uDE9C");
        icon.setStyle("-fx-font-size: 18px;");
        StackPane iconChip = new StackPane(icon);
        iconChip.setPrefSize(36, 36);
        iconChip.setMaxSize(36, 36);
        iconChip.setStyle("-fx-background-color: " + rgba(FOREST_DEEP, 0.10) + "; -fx-background-radius: 10;");

        Label title = new Label("Popular Schemes");
        title.setStyle(
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-font-weight: 900; -fx-text-fill: "
                        + TEXT_PRIMARY + ";");
        Label caption = new Label("14 Active Schemes");
        caption.setStyle(
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");
        VBox textBox = new VBox(2, title, caption);

        HBox row = new HBox(12, iconChip, textBox);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    // ---- Scheme list ----
    private VBox buildSchemeList() {
        // TODO: replace with SchemeService.getSchemesForCategory(villageId, "Agriculture")
        VBox list = new VBox(16,
                schemeCard("Soil Health Card Scheme",
                        "Provides farmers with information on nutrient status of their soil along with "
                                + "recommendations on appropriate dosage of nutrients to be applied for improving "
                                + "soil health and its fertility."),
                schemeCard("PM Krishi Sinchayee Yojana",
                        "Focused on creating sources of assured irrigation, also creating protective irrigation "
                                + "by harnessing rain water at micro level through 'Jal Sanchay' and 'Jal Sinchan'."),
                schemeCard("Kusum Scheme",
                        "Aimed at providing energy security to farmers and de-dieselizing the farm sector by "
                                + "installing solar pumps and other renewable power plants.")
        );
        return list;
    }

    /** One scheme row: title + description on the left, two stacked buttons on the right. */
    private HBox schemeCard(String title, String description) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 15px; -fx-font-weight: 900; -fx-text-fill: "
                        + TEXT_PRIMARY + ";");

        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(560);
        descLabel.setStyle(
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: " + CONTEXT_TEAL + ";");

        VBox textColumn = new VBox(8, titleLabel, descLabel);
        HBox.setHgrow(textColumn, Priority.ALWAYS);

        Button viewDocsBtn = new Button("View Documents Required");
        viewDocsBtn.setMaxWidth(Double.MAX_VALUE);
        viewDocsBtn.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-background-color: white;"
                        + "-fx-text-fill: " + TEXT_PRIMARY + ";"
                        + "-fx-font-size: 11px; -fx-font-weight: 700;"
                        + "-fx-border-color: rgba(11,61,46,0.15); -fx-border-radius: 8;"
                        + "-fx-background-radius: 8; -fx-padding: 8 14 8 14; -fx-cursor: hand;");

        Button applyBtn = new Button("Apply Now");
        applyBtn.setMaxWidth(Double.MAX_VALUE);
        applyBtn.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 11px; -fx-font-weight: 800;"
                        + "-fx-background-radius: 8; -fx-padding: 8 14 8 14; -fx-cursor: hand;"
                        + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.25), 6, 0.1, 0, 2);");

        VBox buttonColumn = new VBox(8, viewDocsBtn, applyBtn);
        buttonColumn.setPrefWidth(210);
        buttonColumn.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(20, textColumn, buttonColumn);
        row.setAlignment(Pos.TOP_LEFT);
        row.setPadding(new Insets(20));
        row.setStyle(cardStyle(14));
        addHoverLift(row, 14);
        return row;
    }

    // =================================================================
    // HELPERS (copied verbatim from VillagerDashboard.java so this file
    // stays self-contained and every card matches the same glass-panel
    // look and hover behavior)
    // =================================================================

    /** Glass-panel style shared by every card on this screen. */
    private String cardStyle(int radius) {
        return "-fx-background-color: rgba(255,255,255,0.88);"
                + "-fx-background-radius: " + radius + ";"
                + "-fx-border-color: rgba(255,255,255,0.5);"
                + "-fx-border-radius: " + radius + ";"
                + "-fx-border-width: 1;"
                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.06), 16, 0.1, 0, 4);";
    }

    /** Hover lift effect matching VillagerDashboard's card hover behavior. */
    private void addHoverLift(Region card, int radius) {
        String base = cardStyle(radius);
        String hover = "-fx-background-color: rgba(255,255,255,0.92);"
                + "-fx-background-radius: " + radius + ";"
                + "-fx-border-color: rgba(255,255,255,0.6);"
                + "-fx-border-radius: " + radius + ";"
                + "-fx-border-width: 1;"
                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.12), 24, 0.15, 0, 8);"
                + "-fx-translate-y: -2;";
        card.setOnMouseEntered(e -> card.setStyle(hover));
        card.setOnMouseExited(e -> card.setStyle(base));
    }

    /** Convert #RRGGBB hex to an rgba(r,g,b,a) CSS string. */
    private String rgba(String hex, double alpha) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
    }

}
