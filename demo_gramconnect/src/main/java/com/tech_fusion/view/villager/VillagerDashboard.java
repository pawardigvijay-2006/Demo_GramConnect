package com.tech_fusion.view.villager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * GramConnect - Villager Dashboard
 *
 * ============================================================
 * NAVIGATION NOTE (read this first!)
 * ============================================================
 * This file now follows the SAME navigation pattern as
 * SarpanchDashboard.java instead of the old handleNavigation(String)
 * switch-case method:
 *
 * - There is no more "one switch statement decides everything".
 * Instead, EACH sidebar nav item gets its own .setOnMouseClicked(...)
 * handler, wired individually right after the item is built - exactly
 * like SarpanchDashboard's projectTrackerNav/complaintNav.
 *
 * - "Dashboard" <-> "Project transparency" now swap the WHOLE Scene
 * on homeStage (via homeStage.setScene(...)), because
 * ProjectTransparency now builds its OWN sidebar + main area and
 * returns a full Scene from getProjectScene(Runnable). This mirrors
 * SarpanchDashboard.getDashboardScene()/back() exactly:
 * - getDashboardScene() builds root+scene once and caches it in
 * dashboardScene, so back() can return to it instantly without
 * rebuilding the whole dashboard.
 * - A Runnable ("backToDashboardAction") is handed to the child
 * page so IT can call back() when its own "Dashboard" nav item
 * is clicked, without needing to know anything about
 * VillagerDashboard's internals.
 *
 * - The other sidebar items (Complaints, Government schemes,
 * Certificates, Bills & Payments, Announcements, Gram Sabha, AI
 * village assistant) keep using the lighter-weight root.setCenter(...)
 * swap, since those pages don't have their own sidebar yet - only
 * the content area changes and the sidebar you're looking at right
 * now stays exactly where it is. Converting those to the same
 * full-Scene + Runnable pattern later is just a matter of giving
 * each one a getXScene(Runnable) method like ProjectTransparency's.
 * ============================================================
 */
public class VillagerDashboard extends Application {

        /** Shared Stage reference - every page that needs to change screens gets this. */
        public static Stage homeStage;

        /** The built Dashboard scene, cached so back() can return to it without rebuilding. */
        private Scene dashboardScene;

        private BorderPane root;
        private BorderPane mainArea;

        // ================= COLORS (borrowed from SarpanchDashboard.java) =================
        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String FOREST_LIGHT = "#0F4736";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String CONTEXT_TEAL = "#0E8C8C";
        private static final String AI_VIOLET = "#7C5CFC";
        private static final String DELAYED_RED = "#D94C38";

        // Light green sidebar gradient (same as Sarpanch's "requested change" palette)
        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        private static final String BACKGROUND = "#EFF5F1";

        private static final String TEXT_PRIMARY = FOREST_DEEP;
        private static final String TEXT_SECONDARY = "rgba(11,61,46,0.65)";

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        /*
         * JavaFX calls this method automatically when the app starts.
         * "primaryStage" is the actual OS window. We build ONE Scene via
         * getDashboardScene() (which caches itself into dashboardScene) and
         * show it - the same start()/getDashboardScene() split SarpanchDashboard
         * uses, so both dashboards can be swapped in/out the same way.
         */
        @Override
        public void start(Stage primaryStage) {

                homeStage = primaryStage;

                homeStage.setScene(getDashboardScene());

                homeStage.setTitle("GramConnect - Villager Dashboard");

                homeStage.setMinWidth(1280);
                homeStage.setMinHeight(800);

                homeStage.setMaximized(true);

                homeStage.show();
        }

        /**
         * Builds (or rebuilds) the Dashboard scene and returns it. Public so any
         * child page's "Dashboard" nav item can jump straight back here via a
         * Runnable, the same way ProjectTrackerPage calls back into
         * SarpanchDashboard.
         */
        public Scene getDashboardScene() {

                root = new BorderPane();

                root.setStyle(
                                "-fx-background-color: " + BACKGROUND + ";");

                // Sidebar remains fixed for every page that swaps content via
                // root.setCenter(...) instead of a full Scene swap.
                root.setLeft(buildSidebar());

                // Main area
                mainArea = buildMainArea();

                root.setCenter(mainArea);

                dashboardScene = new Scene(root, 1500, 850);

                return dashboardScene;
        }

        /** Returns to the cached Dashboard scene without rebuilding it. */
        public void back() {
                homeStage.setTitle("GramConnect - Villager Dashboard");
                homeStage.setScene(dashboardScene);
        }

        // =================================================================
        // SIDEBAR - light-green gradient background, saffron active-state
        // accent. Every nav item below is built as a local variable and gets
        // its own .setOnMouseClicked(...) handler right after construction -
        // no shared switch statement.
        // =================================================================
        private VBox buildSidebar() {
                // Step 1: create the empty container.
                VBox sidebar = new VBox();
                sidebar.setPrefWidth(230);
                sidebar.setMinWidth(230);
                sidebar.setStyle(
                                "-fx-background-color: linear-gradient(to bottom, " + SIDEBAR_TOP + ", " + SIDEBAR_MID
                                                + ", " + SIDEBAR_BOT + ");"
                                                + "-fx-border-color: transparent rgba(11,61,46,0.10) transparent transparent;"
                                                + "-fx-border-width: 0 1 0 0;"
                                                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.20), 24, 0.2, 4, 0);");

                // ---------------- Logo ----------------
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
                // Each item is a local variable so we can attach its own click
                // handler right below, instead of routing every click through
                // one shared handleNavigation(String) switch statement.

                Label dashboardNav = navItem("\uD83C\uDFE0  Dashboard", true);
                dashboardNav.setOnMouseClicked(e -> back());

                Label projectsNav = navItem("\uD83C\uDFD7  Project transparency", false);
                projectsNav.setOnMouseClicked(e -> {
                        ProjectTransparency projectTransparency = new ProjectTransparency();
                        // This is the Runnable being asked for: it captures "how to get
                        // back to the dashboard" without ProjectTransparency needing to
                        // know anything about VillagerDashboard's fields or methods.
                        Runnable backToDashboardAction = () -> back();
                        homeStage.setScene(projectTransparency.getProjectScene(backToDashboardAction));
                        homeStage.setTitle("GramConnect - Project Transparency");
                });

                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", false);
                complaintsNav.setOnMouseClicked(e -> {
                        Runnable backToDashboardAction = () -> back();
                        homeStage.setScene(new ComplaintsPage().getComplaintsPage(backToDashboardAction));
                        homeStage.setTitle("GramConnect - Complaints");
                });

                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", false);
                schemesNav.setOnMouseClicked(e -> {
                        Runnable backToDashboardAction = () -> back();
                        homeStage.setScene(new GovernmentSchemes().getSchemesScene(backToDashboardAction));
                        homeStage.setTitle("GramConnect - Government Schemes");
                });

                Label certificatesNav = navItem("\uD83D\uDCDC  Certificates", false);
                certificatesNav.setOnMouseClicked(e -> {
                        Runnable backToDashboardAction = () -> back();
                        homeStage.setScene(new Certificates().getCertificatesScene(backToDashboardAction));
                        homeStage.setTitle("GramConnect - Certificates");
                });

                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", false);
                billsNav.setOnMouseClicked(e -> {
                        Runnable backToDashboardAction = () -> back();
                        homeStage.setScene(new BillsAndPayments().getBillsScene(backToDashboardAction));
                        homeStage.setTitle("GramConnect - Bills & Payments");
                });

                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                announcementsNav.setOnMouseClicked(e -> {
                        Runnable backToDashboardAction = () -> back();
                        homeStage.setScene(new Announcements().getAnnouncementScene(backToDashboardAction));
                        homeStage.setTitle("GramConnect - Announcements");
                });

                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", false);
                gramSabhaNav.setOnMouseClicked(e -> {
                        Runnable backToDashboardAction = () -> back();
                        homeStage.setScene(new GramSabha().getGramSabhaScene(backToDashboardAction));
                        homeStage.setTitle("GramConnect - Gram Sabha");
                });

                Label aiAssistantNav = navItem("\uD83E\uDD16  AI village assistant", false);
                aiAssistantNav.setOnMouseClicked(e -> {
                        Runnable backToDashboardAction = () -> back();
                        homeStage.setScene(new AIAssistant().getAiAssiatantScene(backToDashboardAction));
                        homeStage.setTitle("GramConnect - AI Village Assistant");
                });

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
                VBox.setVgrow(navItems, Priority.ALWAYS); // let this section stretch to fill leftover space

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

        /**
         * Builds one clickable-looking sidebar row. Returns a single Label node
         * with NO click handler attached - the caller (buildSidebar() above)
         * attaches .setOnMouseClicked(...) itself right after construction, so
         * every nav item's destination is defined right next to the item
         * instead of in one faraway switch statement.
         */
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
        // MAIN AREA (header on top, scrollable content below it)
        // =================================================================
        private BorderPane buildMainArea() {
                BorderPane main = new BorderPane();
                main.setTop(buildHeader());
                main.setCenter(buildScrollableContent());
                return main;
        }

        /** Header restyled as a translucent glass bar with a rounded pill search box. */
        private HBox buildHeader() {
                // HBox lays its children left-to-right. Spacing "16" = 16px gap between them.
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
                searchBox.setPrefWidth(300);
                searchBox.setPrefHeight(38);
                searchBox.setStyle(
                                "-fx-background-color: " + BACKGROUND + ";"
                                                + "-fx-background-radius: 20;"
                                                + "-fx-border-color: rgba(11,61,46,0.10);"
                                                + "-fx-border-radius: 20;"
                                                + "-fx-border-width: 1;");
                Label searchIcon = new Label("\uD83D\uDD0D");
                searchIcon.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.5);");
                TextField search = new TextField();
                search.setPromptText("Search projects, schemes, services");
                search.setStyle(
                                "-fx-background-color: transparent;"
                                                + "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-font-size: 12px;"
                                                + "-fx-text-fill: " + FOREST_DEEP + ";"
                                                + "-fx-prompt-text-fill: rgba(11,61,46,0.40);");
                HBox.setHgrow(search, Priority.ALWAYS);
                searchBox.getChildren().addAll(searchIcon, search);

                // An empty Region set to grow fills up leftover space - this is how
                // we "push" the bell + profile to the right edge of the header.
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // Bell with a small red badge showing unread notification count.
                Label bellIcon = new Label("\uD83D\uDD14");
                bellIcon.setStyle("-fx-font-size: 15px;");
                StackPane bell = new StackPane(bellIcon);
                bell.setPrefSize(38, 38);
                bell.setMaxSize(38, 38);
                bell.setStyle(
                                "-fx-background-color: " + BACKGROUND + ";"
                                                + "-fx-background-radius: 999;"
                                                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 4, 0.1, 0, 1);");
                Label badge = new Label("3");
                badge.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: #D94C38;"
                                                + "-fx-text-fill: white;"
                                                + "-fx-font-size: 9px; -fx-font-weight: 800;"
                                                + "-fx-background-radius: 999;"
                                                + "-fx-padding: 1 5 1 5;");
                StackPane bellWithBadge = new StackPane(bell, badge);
                StackPane.setAlignment(badge, Pos.TOP_RIGHT);

                // StackPane layers its children on top of each other (centered by
                // default) - here it's used for the "RP" avatar bubble, matching
                // Sarpanch's forest-green circle + saffron ring look.
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

                Label name = new Label("Ramesh Patil");
                name.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label role = new Label("Villager, Suryapuri");
                role.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");
                VBox nameBox = new VBox(name, role); // stack name above role

                Label chevron = new Label("\u25BE");
                chevron.setStyle("-fx-text-fill: " + TEXT_SECONDARY + ";");

                HBox profile = new HBox(8, avatar, nameBox, chevron); // avatar + name side by side
                profile.setAlignment(Pos.CENTER_LEFT);

                // Finally: put all pieces into the header, left to right.
                header.getChildren().addAll(searchBox, spacer, bellWithBadge, profile);
                return header;
        }

        private ScrollPane buildScrollableContent() {
                // This VBox stacks 4 big sections vertically, 18px apart:
                // 1. greeting text
                // 2. the row of 4 stat cards
                // 3. the projects list + bills card, side by side
                // 4. the complaints/announcement/gram sabha row
                VBox content = new VBox(24);
                content.setPadding(new Insets(24, 32, 32, 32));
                content.setStyle("-fx-background-color: " + BACKGROUND + ";");

                content.getChildren().addAll(
                                buildGreeting(),
                                buildStatCardsRow(),
                                buildProjectsAndBudgetRow(),
                                buildComplaintsAnnouncementsGramSabhaRow(),
                                buildVillageStatusBar());

                // Wrapping the VBox in a ScrollPane means if the window is too
                // short, the user can scroll instead of content getting clipped.
                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true); // content stretches to match the scroll pane's width
                scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
                return scrollPane;
        }

        // ---- Greeting ----
        private VBox buildGreeting() {
                Label eyebrow = new Label("NAMASTE, RAMESH");
                eyebrow.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-text-fill: " + SAFFRON_MAIN
                                                + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-letter-spacing: 0.08em;");
                Label title = new Label("Here is what is happening in Suryapuri today");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-text-fill: " + TEXT_PRIMARY
                                                + "; -fx-font-size: 26px; -fx-font-weight: 900;");
                return new VBox(4, eyebrow, title);
        }

        // ---- Stat cards ----
        private HBox buildStatCardsRow() {
                HBox row = new HBox(18,
                                statCard(CONTEXT_TEAL, "\uD83C\uDFD7", "8", "Ongoing projects"),
                                statCard(FOREST_DEEP, "\uD83D\uDCCA", "72%", "Average completion"),
                                statCard(SAFFRON_MAIN, "\uD83D\uDCAC", "2", "Open complaints"),
                                statCard(DELAYED_RED, "\uD83D\uDCB3", "\u20B91,350", "Pending bills"));
                return row;
        }

        /** Builds one KPI-style stat card: colored top strip, icon chip, big value, caption. */
        private VBox statCard(String accent, String icon, String value, String label) {
                VBox card = new VBox();
                HBox.setHgrow(card, Priority.ALWAYS);
                card.setStyle(cardStyle(16));

                Region strip = new Region();
                strip.setPrefHeight(5);
                strip.setStyle("-fx-background-color: " + accent + "; -fx-background-radius: 16 16 0 0;");

                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: " + accent + ";");
                StackPane iconCircle = new StackPane(iconLabel);
                iconCircle.setPrefSize(40, 40);
                iconCircle.setMaxSize(40, 40);
                iconCircle.setStyle("-fx-background-color: " + rgba(accent, 0.12) + "; -fx-background-radius: 12;");

                Label valueLabel = new Label(value);
                valueLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                Label textLabel = new Label(label);
                textLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                VBox inner = new VBox(10, iconCircle, valueLabel, textLabel);
                inner.setPadding(new Insets(16, 16, 18, 16));

                card.getChildren().addAll(strip, inner);
                addHoverLift(card, 16);
                return card;
        }

        // ---- Projects list + My Bills card ----
        private HBox buildProjectsAndBudgetRow() {
                return new HBox(18, buildProjectsCard(), buildMyBillsCard());
        }

        private VBox buildProjectsCard() {

                Label title = new Label("Recent projects");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                Label viewAll = new Label("View all \u2192");
                viewAll.setPadding(new Insets(6, 14, 6, 14));
                viewAll.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: " + rgba(CONTEXT_TEAL, 0.10) + ";" +
                                                "-fx-background-radius: 999;" +
                                                "-fx-text-fill: " + CONTEXT_TEAL + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-cursor: hand;");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox header = new HBox(title, spacer, viewAll);
                header.setAlignment(Pos.CENTER_LEFT);

                VBox list = new VBox(20,
                                projectRow("assets\\images\\road.jpg", "Village road construction",
                                                "Main Street, Suryapuri", 78, "On Track"),
                                projectRow("assets\\images\\water_tank.jpg", "Water tank renovation",
                                                "Near school area", 45, "In Progress"),
                                projectRow("assets\\images\\street_light.jpg", "Street light installation",
                                                "All village roads", 25, "Delayed"));

                VBox card = new VBox(16, header, list);
                card.setPadding(new Insets(24));
                card.setPrefWidth(600);
                card.setStyle(cardStyle(20));
                HBox.setHgrow(card, Priority.ALWAYS);
                addHoverLift(card, 20);
                return card;
        }

        /**
         * Builds one project row: name + status pill on top, location below, a
         * rounded colored progress bar at the bottom.
         */
        private HBox projectRow(String imagePath, String name, String location, int percent, String status) {

                ImageView projectImage = new ImageView(new Image(imagePath));
                projectImage.setFitWidth(118);
                projectImage.setFitHeight(78);
                projectImage.setPreserveRatio(false);

                Rectangle clip = new Rectangle(118, 78);
                clip.setArcWidth(12);
                clip.setArcHeight(12);
                projectImage.setClip(clip);

                Label nameLabel = new Label(name);
                nameLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                String pillColor;
                String barColor;
                switch (status) {
                        case "Delayed":
                                pillColor = DELAYED_RED;
                                barColor = DELAYED_RED;
                                break;
                        case "In Progress":
                                pillColor = SAFFRON_MAIN;
                                barColor = SAFFRON_MAIN;
                                break;
                        default:
                                pillColor = FOREST_DEEP;
                                barColor = FOREST_DEEP;
                }

                Label statusPill = new Label(status);
                statusPill.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: " + pillColor + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-background-radius: 999;" +
                                                "-fx-padding: 4 12 4 12;" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-effect: dropshadow(gaussian, " + rgba(pillColor, 0.25) + ", 6, 0.2, 0, 2);");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                HBox topRow = new HBox(8, nameLabel, spacer, statusPill);
                topRow.setAlignment(Pos.CENTER_LEFT);

                Label locationLabel = new Label(location);
                locationLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");

                StackPane bar = progressBar(percent / 100.0, barColor, 7);
                HBox.setHgrow(bar, Priority.ALWAYS);

                Label percentLabel = new Label(percent + "%");
                percentLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");

                HBox progressRow = new HBox(8, bar, percentLabel);
                progressRow.setAlignment(Pos.CENTER_LEFT);

                VBox projectDetails = new VBox(6, topRow, locationLabel, progressRow);
                projectDetails.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(projectDetails, Priority.ALWAYS);

                HBox projectRow = new HBox(14, projectImage, projectDetails);
                projectRow.setAlignment(Pos.CENTER_LEFT);
                return projectRow;
        }

        /** Builds the "My Bills" card: a title, 3 bill rows, and a "Pay All Dues" button. */
        private VBox buildMyBillsCard() {

                Label title = new Label("My Bills");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                HBox water = billRow("Water Bill", "Sept 2024", "\u20B9240.00", "DUE", DELAYED_RED);
                HBox property = billRow("Property Tax", "Annual 2024", "\u20B91,200.00", "PAID", FOREST_DEEP);
                HBox electricity = billRow("Electricity", "Sept 2024", "\u20B9650.00", "DUE", DELAYED_RED);

                javafx.scene.control.Button payButton = new javafx.scene.control.Button("Pay All Dues");
                payButton.setMaxWidth(Double.MAX_VALUE);
                payButton.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", "
                                                + FOREST_DEEP + ");" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 10;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.35), 8, 0.1, 0, 3);");

                VBox card = new VBox(12, title, water, property, electricity, payButton);
                card.setPadding(new Insets(20));
                card.setPrefWidth(340);
                card.setStyle(cardStyle(18));
                addHoverLift(card, 18);
                return card;
        }

        private HBox billRow(String name, String date, String amount, String status, String statusColor) {
                Label nameLabel = new Label(name);
                nameLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label dateLabel = new Label(date);
                dateLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 9px; -fx-text-fill: " + TEXT_SECONDARY
                                                + ";");

                Label amountLabel = new Label(amount);
                amountLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label statusLabel = new Label(status);
                statusLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 8px; -fx-font-weight: 800; -fx-text-fill: "
                                                + statusColor + ";");

                VBox left = new VBox(2, nameLabel, dateLabel);
                VBox right = new VBox(2, amountLabel, statusLabel);
                right.setAlignment(Pos.CENTER_RIGHT);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox row = new HBox(left, spacer, right);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(12));
                row.setStyle(
                                "-fx-background-color: rgba(255,255,255,0.6);" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: rgba(255,255,255,0.7);" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-border-width: 1;");
                return row;
        }

        // ---- Complaints / Announcement / Gram Sabha row ----
        private HBox buildComplaintsAnnouncementsGramSabhaRow() {
                return new HBox(18, buildComplaintsCard(), buildAnnouncementCard(), buildGramSabhaCard());
        }

        private VBox buildComplaintsCard() {
                Label title = new Label("Complaint status");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                // TODO: replace with ComplaintService.getComplaintsForUser(userId)
                VBox list = new VBox(12,
                                complaintRow("Water leakage in street", "In Progress", SAFFRON_MAIN),
                                complaintRow("Street light not working", "Resolved", FOREST_DEEP));

                VBox card = new VBox(14, title, list);
                card.setPadding(new Insets(20));
                card.setPrefWidth(300);
                card.setStyle(cardStyle(18));
                HBox.setHgrow(card, Priority.ALWAYS);
                addHoverLift(card, 18);
                return card;
        }

        private HBox complaintRow(String title, String status, String pillColor) {
                Label titleLabel = new Label(title);
                titleLabel.setWrapText(true);
                titleLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY
                                                + ";");

                Label statusLabel = new Label(status);
                statusLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: " + pillColor + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-background-radius: 999;" +
                                                "-fx-padding: 3 10 3 10;" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: 700;");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                HBox row = new HBox(titleLabel, spacer, statusLabel);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        private VBox buildAnnouncementCard() {
                Label title = new Label("Latest announcement");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                Label headline = new Label("Water supply schedule change");
                headline.setWrapText(true);
                headline.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label body = new Label("Supply interrupted in Ward 3 on 25 May.");
                body.setWrapText(true);
                body.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY
                                                + ";");

                VBox card = new VBox(10, title, headline, body);
                card.setPadding(new Insets(20));
                card.setPrefWidth(300);
                card.setStyle(cardStyle(18));
                HBox.setHgrow(card, Priority.ALWAYS);
                addHoverLift(card, 18);
                return card;
        }

        private VBox buildGramSabhaCard() {
                Label title = new Label("Upcoming Gram Sabha");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                Label dateLabel = new Label("30");
                dateLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-text-fill: " + CONTEXT_TEAL
                                                + "; -fx-font-size: 15px; -fx-font-weight: 900;");
                StackPane dateBox = new StackPane(dateLabel);
                dateBox.setPrefSize(42, 42);
                dateBox.setMaxSize(42, 42);
                dateBox.setStyle("-fx-background-color: " + rgba(CONTEXT_TEAL, 0.12) + "; -fx-background-radius: 10;");

                Label eventTitle = new Label("11:00 AM, Panchayat Bhavan");
                eventTitle.setWrapText(true);
                eventTitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label eventSub = new Label("Village development discussion");
                eventSub.setWrapText(true);
                eventSub.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY
                                                + ";");
                VBox eventBox = new VBox(2, eventTitle, eventSub);

                HBox body = new HBox(12, dateBox, eventBox);
                body.setAlignment(Pos.CENTER_LEFT);

                VBox card = new VBox(12, title, body);
                card.setPadding(new Insets(20));
                card.setPrefWidth(300);
                card.setStyle(cardStyle(18));
                HBox.setHgrow(card, Priority.ALWAYS);
                addHoverLift(card, 18);
                return card;
        }

        // ================================================================
        // VILLAGE STATUS BAR
        // ================================================================
        private HBox buildVillageStatusBar() {

                Label sun = new Label("\u2600");
                sun.setStyle("-fx-font-size: 30px; -fx-text-fill: white;");

                Label temperature = new Label("32\u00B0C");
                temperature.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: white;");

                Label weather = new Label("Sunny Skies | Ganeshpur, MH");
                weather.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 9px;" +
                                                "-fx-text-fill: rgba(255,255,255,0.75);");

                VBox weatherText = new VBox(1, temperature, weather);
                weatherText.setAlignment(Pos.CENTER_LEFT);

                HBox weatherBox = new HBox(10, sun, weatherText);
                weatherBox.setAlignment(Pos.CENTER_LEFT);

                HBox power = statusChip("\u26A1", "Power Status", "All Good \u2022 No scheduled cuts");
                HBox health = statusChip("\u271A", "Health Camp", "Free Checkup @ PHC \u2022 10 AM");

                Label today = new Label("Today in Village");
                today.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-letter-spacing: 0.06em;");

                VBox weatherSection = new VBox(8, today, weatherBox);
                weatherSection.setAlignment(Pos.CENTER_LEFT);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox cards = new HBox(12, power, health);
                cards.setAlignment(Pos.CENTER_RIGHT);

                HBox bottom = new HBox(20, weatherSection, spacer, cards);
                bottom.setAlignment(Pos.CENTER_LEFT);
                bottom.setPadding(new Insets(16, 22, 16, 22));
                bottom.setMinHeight(104);
                bottom.setPrefHeight(104);
                bottom.setMaxWidth(Double.MAX_VALUE);
                bottom.setStyle(
                                "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");"
                                                + "-fx-background-radius: 16;"
                                                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.35), 16, 0.15, 0, 6);");

                return bottom;
        }

        /** One translucent status chip (used for Power Status + Health Camp). */
        private HBox statusChip(String icon, String title, String text) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

                Label titleLabel = new Label(title);
                titleLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-text-fill: white;");

                Label textLabel = new Label(text);
                textLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 9px;" +
                                                "-fx-text-fill: rgba(255,255,255,0.75);");

                VBox content = new VBox(2, titleLabel, textLabel);
                content.setAlignment(Pos.CENTER_LEFT);

                HBox chip = new HBox(8, iconLabel, content);
                chip.setAlignment(Pos.CENTER_LEFT);
                chip.setPadding(new Insets(10, 14, 10, 14));
                chip.setPrefWidth(250);
                chip.setStyle(
                                "-fx-background-color: rgba(255,255,255,0.12);" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: rgba(255,255,255,0.15);" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-border-width: 1;");
                return chip;
        }

        // =================================================================
        // HELPERS (shared glass-panel look, hover lift, rgba conversion,
        // rounded progress bar - identical to SarpanchDashboard.java)
        // =================================================================

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
}