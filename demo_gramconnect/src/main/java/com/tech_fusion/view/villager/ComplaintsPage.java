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
 * GramConnect - Complaints Management
 *
 * ============================================================
 * DIFFERENCE FROM THE OTHER PAGES IN THIS PROJECT
 * ============================================================
 * VillagerDashboard.java and ProjectTransparency.java are each a full
 * javafx.application.Application (they build their own Stage/Scene and
 * call primaryStage.show()).
 *
 * This class is NOT an Application. It is a plain class with one public
 * method:
 *
 * public BorderPane getComplaintsPage(Stage homepageStage)
 *
 * ...which builds and returns just the root BorderPane (sidebar + main
 * area), so a navigation controller can plug it straight into the
 * single shared "homepageStage" like this:
 *
 * homepageStage.getScene().setRoot(new
 * ComplaintsPage().getComplaintsPage(homepageStage));
 *
 * or, if no Scene exists yet:
 *
 * homepageStage.setScene(new Scene(new
 * ComplaintsPage().getComplaintsPage(homepageStage), 1500, 850));
 *
 * The Stage reference is passed in (and threaded through the sidebar)
 * so that clicking a different nav item (Dashboard, Project transparency,
 * etc.) can swap the root on that SAME stage instead of opening a new
 * window - that's the whole point of a single-Stage, multi-page app.
 * Everything else (sidebar, header, colors, card styles) is copy-pasted
 * from VillagerDashboard.java / ProjectTransparency.java on purpose, so
 * all three screens look and feel identical.
 * ============================================================
 */
public class ComplaintsPage {

        // ================= COLORS (same palette as the other pages) =================
        private static final String PRIMARY = "#005B1B";
        private static final String PRIMARY_DARK = "#004D16";

        private static final String ACCENT_GREEN = "#68D66F";
        private static final String LIGHT_GREEN = "#E8F7EA";

        private static final String BLUE = "#2196F3";
        private static final String LIGHT_BLUE = "#EAF5FC";

        private static final String WARNING = "#F4A62A";
        private static final String LIGHT_YELLOW = "#FFF7E5";

        private static final String ERROR = "#D93025";
        private static final String LIGHT_RED = "#FDECEC";

        private static final String BACKGROUND = "#F4F8FB";

        private static final String TEXT_PRIMARY = "#10251A";
        private static final String TEXT_SECONDARY = "#66756C";

        private static final String BORDER = "#D8E2DC";
        private static final String SECONDARY = "#1976D2";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String FOREST_DEEP = "#0B3D2E";

        // Light green sidebar gradient (identical to VillagerDashboard.java)
        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";
        private static final String DELAYED_RED = "#D94C38";

        /**
         * Builds the whole Complaints Management screen and returns it as a
         * single BorderPane, ready to be set as a Scene's root on the shared
         * homepageStage.
         */
        public Scene getComplaintsPage(Runnable backAction) {
                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BACKGROUND + ";");

                root.setLeft(buildSidebar(backAction));
                root.setCenter(buildMainArea());

                return new Scene(root, 1500, 850);
        }

        // =================================================================
        // SIDEBAR (same structure as the other pages - "Complaints" is now
        // the active row). Each nav item is wired to swap the root on the
        // SAME homepageStage - fill in the TODOs with your real page
        // classes once they exist.
        // =================================================================
        private VBox buildSidebar(Runnable backAction) {

                VBox sidebar = new VBox();

                sidebar.setPrefWidth(230);
                sidebar.setMinWidth(230);
                sidebar.setMaxWidth(230);

                // EXACT SAME SIDEBAR STYLE AS PROJECT TRANSPARENCY
                sidebar.setStyle(
                                "-fx-background-color: linear-gradient(to bottom, "
                                                + SIDEBAR_TOP + ", "
                                                + SIDEBAR_MID + ", "
                                                + SIDEBAR_BOT + ");"
                                                + "-fx-border-color: transparent rgba(11,61,46,0.10) "
                                                + "transparent transparent;"
                                                + "-fx-border-width: 0 1 0 0;"
                                                + "-fx-effect: dropshadow(gaussian, "
                                                + "rgba(11,61,46,0.20), 24, 0.2, 4, 0);");

                // =====================================================
                // LOGO
                // =====================================================

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
                                                + "-fx-font-weight: 700;");

                VBox logoTextBox = new VBox(0, logoText, subtitle);

                HBox logo = new HBox(8, logoIcon, logoTextBox);

                logo.setAlignment(Pos.CENTER_LEFT);

                VBox logoBox = new VBox(logo);

                logoBox.setPadding(
                                new Insets(18, 18, 22, 18));

                // =====================================================
                // NAVIGATION
                // Complaints is ACTIVE
                // =====================================================

                Label dashboardNav = navItem("\uD83C\uDFE0  Dashboard", false);

                dashboardNav.setOnMouseClicked(
                                e -> {
                                        backAction.run();
                                });

                Label projectsNav = navItem("\uD83C\uDFD7  Project transparency", false);
                projectsNav.setOnMouseClicked(
                                e -> {
                                        // backAction.run();
                                        VillagerDashboard.homeStage.setScene(
                                                        new ProjectTransparency().getProjectScene(backAction));
                                });

                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", true);

                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", false);
                schemesNav.setOnMouseClicked(
                                e -> {
                                        // backAction.run();
                                        VillagerDashboard.homeStage
                                                        .setScene(new GovernmentSchemes().getSchemesScene(backAction));
                                });

                Label certificatesNav = navItem("\uD83D\uDCDC  Certificates", false);
                certificatesNav.setOnMouseClicked(
                                e -> {
                                        // backAction.run();
                                        VillagerDashboard.homeStage
                                                        .setScene(new Certificates().getCertificatesScene(backAction));
                                });

                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", false);
                billsNav.setOnMouseClicked(
                                e -> {
                                        // backAction.run();
                                        VillagerDashboard.homeStage
                                                        .setScene(new BillsAndPayments().getBillsScene(backAction));
                                });

                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                announcementsNav.setOnMouseClicked(
                                e -> {
                                        // backAction.run();
                                        VillagerDashboard.homeStage
                                                        .setScene(new Announcements().getAnnouncementScene(backAction));
                                });

                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", false);
                gramSabhaNav.setOnMouseClicked(
                                e -> {
                                        // backAction.run();
                                        VillagerDashboard.homeStage
                                                        .setScene(new GramSabha().getGramSabhaScene(backAction));
                                });

                Label aiAssistantNav = navItem("\uD83E\uDD16  AI village assistant", false);
                aiAssistantNav.setOnMouseClicked(
                                e -> {
                                        // backAction.run();
                                        VillagerDashboard.homeStage
                                                        .setScene(new AIAssistant().getAiAssiatantScene(backAction));
                                });

                VBox navItems = new VBox(
                                4,
                                dashboardNav,
                                projectsNav,
                                complaintsNav,
                                schemesNav,
                                certificatesNav,
                                billsNav,
                                announcementsNav,
                                gramSabhaNav,
                                aiAssistantNav);

                navItems.setPadding(
                                new Insets(0, 10, 0, 10));

                VBox.setVgrow(
                                navItems,
                                Priority.ALWAYS);

                // =====================================================
                // EMERGENCY ASSISTANCE
                // =====================================================

                Label emergency = new Label("\u26A0  Emergency assistance");

                emergency.setWrapText(true);

                emergency.setPadding(
                                new Insets(10, 12, 10, 12));

                emergency.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-text-fill: " + DELAYED_RED + ";"
                                                + "-fx-font-size: 12px;"
                                                + "-fx-font-weight: 700;"
                                                + "-fx-background-color: rgba(217,76,56,0.12);"
                                                + "-fx-background-radius: 10;");

                VBox emergencyBox = new VBox(emergency);

                emergencyBox.setPadding(
                                new Insets(12, 16, 18, 18));

                sidebar.getChildren().addAll(
                                logoBox,
                                navItems,
                                emergencyBox);

                return sidebar;
        }

        private Label navItem(String text, boolean active) {

                Label item = new Label(text);

                item.setMaxWidth(
                                Double.MAX_VALUE);

                item.setPadding(
                                new Insets(10, 14, 10, 14));

                // =====================================================
                // ACTIVE ITEM
                // EXACT SAME STYLE AS PROJECT TRANSPARENCY
                // =====================================================

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

                }

                // =====================================================
                // INACTIVE ITEM
                // =====================================================

                else {

                        String baseStyle = "-fx-font-family: " + FONT_FAMILY + ";"
                                        + "-fx-text-fill: rgba(11,61,46,0.80);"
                                        + "-fx-font-size: 13px;"
                                        + "-fx-font-weight: 700;"
                                        + "-fx-cursor: hand;";

                        item.setStyle(baseStyle);

                        // ---------------- Hover ----------------

                        item.setOnMouseEntered(
                                        e -> item.setStyle(
                                                        "-fx-font-family: " + FONT_FAMILY + ";"
                                                                        + "-fx-background-color: rgba(255,255,255,0.45);"
                                                                        + "-fx-text-fill: " + FOREST_DEEP + ";"
                                                                        + "-fx-font-weight: 700;"
                                                                        + "-fx-font-size: 13px;"
                                                                        + "-fx-background-radius: 8;"
                                                                        + "-fx-cursor: hand;"));

                        // ---------------- Mouse Exit ----------------

                        item.setOnMouseExited(
                                        e -> item.setStyle(baseStyle));
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

        /**
         * Same header pattern as the other pages, with two additions to match
         * the reference screenshot: a red "unread" dot on the bell, and a
         * round "?" help button next to the avatar photo.
         */
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
                searchBox.setPrefWidth(320);
                searchBox.setStyle(
                                "-fx-background-color: " + BACKGROUND + ";"
                                                + "-fx-background-radius: 20;"
                                                + "-fx-border-color: rgba(11,61,46,0.10);"
                                                + "-fx-border-radius: 20;"
                                                + "-fx-border-width: 1;");
                Label searchIcon = new Label("\uD83D\uDD0D");
                searchIcon.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.5);");
                TextField search = new TextField();
                search.setPromptText("Search certificates...");
                search.setStyle(
                                "-fx-background-color: transparent;"
                                                + "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-font-size: 12px;"
                                                + "-fx-text-fill: " + FOREST_DEEP + ";"
                                                + "-fx-prompt-text-fill: rgba(11,61,46,0.40);");
                HBox.setHgrow(search, Priority.ALWAYS);
                searchBox.getChildren().addAll(searchIcon, search);

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
                VBox nameBox = new VBox(name, role);

                Label chevron = new Label("\u25BE");
                chevron.setStyle("-fx-text-fill: " + TEXT_SECONDARY + ";");

                HBox profile = new HBox(8, avatar, nameBox, chevron);
                profile.setAlignment(Pos.CENTER_LEFT);

                header.getChildren().addAll(searchBox, spacer, bellWithBadge, profile);
                return header;
        }

        private ScrollPane buildScrollableContent() {

                VBox content = new VBox(14);

                content.setPadding(new Insets(16, 24, 24, 24));

                content.setFillWidth(true);

                content.getChildren().addAll(
                                buildPageTitle(),
                                buildStatCardsRow(),
                                buildRegisterButton(),
                                buildRecentSubmissionsHeader(),
                                buildComplaintsList(),
                                buildShowMoreLink());

                ScrollPane scrollPane = new ScrollPane(content);

                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(false);

                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scrollPane.setPannable(true);

                scrollPane.setStyle(
                                "-fx-background-color: transparent;"
                                                + "-fx-background-insets: 0;"
                                                + "-fx-padding: 0;");

                return scrollPane;
        }

        // ---- Page title ----
        private VBox buildPageTitle() {
                Label title = new Label("Complaints Management");
                title.setStyle("-fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 24px; -fx-font-weight: bold;");

                Label subtitle = new Label("Track, monitor, and resolve citizen grievances for community development.");
                subtitle.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 12px;");

                return new VBox(2, title, subtitle);
        }

        // ---- Stat cards ----
        private HBox buildStatCardsRow() {
                HBox row = new HBox(14,
                                statCard("\uD83D\uDCCB", LIGHT_GREEN, PRIMARY, "12", "Cumulative reports",
                                                "Total", TEXT_SECONDARY, BACKGROUND, false),
                                statCard("\u2705", LIGHT_GREEN, PRIMARY, "8", "Action completed",
                                                "Resolved", PRIMARY, LIGHT_GREEN, false),
                                statCard("\uD83D\uDD0D", LIGHT_BLUE, SECONDARY, "2", "Under investigation",
                                                "Processing", SECONDARY, LIGHT_BLUE, false),
                                statCard("\u26A0", LIGHT_RED, ERROR, "2", "Action required now",
                                                "Critical", ERROR, LIGHT_RED, true));
                return row;
        }

        /**
         * One stat card: icon chip top-left, status pill top-right, big
         * number, small caption - plus a large, faint watermark of the same
         * icon in the bottom-right corner (matches the reference design).
         * The "Critical" card additionally gets a thick colored left border.
         */
        private VBox statCard(String icon, String iconBg, String iconColor, String value, String caption,
                        String pillText, String pillFg, String pillBg, boolean criticalAccent) {

                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-text-fill: " + iconColor + "; -fx-font-size: 15px;");
                StackPane iconCircle = new StackPane(iconLabel);
                iconCircle.setPrefSize(50, 50);
                iconCircle.setMaxSize(80, 80);
                iconCircle.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 9;");

                Label pill = new Label(pillText);
                pill.setStyle("-fx-background-color: " + pillBg + "; -fx-text-fill: " + pillFg + "; "
                                + "-fx-background-radius: 10; -fx-padding: 3 10 3 10; -fx-font-size: 10px; -fx-font-weight: bold;");

                Region topSpacer = new Region();
                HBox.setHgrow(topSpacer, Priority.ALWAYS);
                HBox topRow = new HBox(iconCircle, topSpacer, pill);
                topRow.setAlignment(Pos.CENTER_LEFT);

                Label valueLabel = new Label(value);
                valueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label captionLabel = new Label(caption);
                captionLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                VBox textBlock = new VBox(4, valueLabel, captionLabel);

                // Faint oversized watermark icon in the bottom-right corner.
                Label watermark = new Label(icon);
                watermark.setStyle("-fx-font-size: 46px; -fx-opacity: 0.08;");
                StackPane.setAlignment(watermark, Pos.BOTTOM_RIGHT);

                VBox foreground = new VBox(14, topRow, textBlock);
                foreground.setPadding(new Insets(16));

                StackPane cardStack = new StackPane(watermark, foreground);
                cardStack.setAlignment(Pos.TOP_LEFT);
                cardStack.setMinWidth(280);
                cardStack.setPrefWidth(280);
                HBox.setHgrow(cardStack, Priority.ALWAYS);

                String borderStyle = criticalAccent
                                ? "-fx-border-color: " + ERROR + " " + BORDER + " " + BORDER + " " + BORDER + "; "
                                                + "-fx-border-width: 0 1 1 4;"
                                : "-fx-border-color: " + BORDER + "; -fx-border-width: 1;";

                cardStack.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-radius: 12; " + borderStyle);
                HBox.setHgrow(cardStack, Priority.ALWAYS);
                return new VBox(cardStack);
        }

        // ---- "Register New Complaint" button ----
        private HBox buildRegisterButton() {
                Button register = new Button("+  Register New Complaint");
                register.setStyle(
                                "-fx-background-color: " + PRIMARY + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 12 22 12 22;" +
                                                "-fx-cursor: hand;");
                // TODO: wire this up to open a real "New Complaint" form/dialog.

                HBox row = new HBox(register);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- "Recent Submissions" header (title + Filter/Sort buttons) ----
        private HBox buildRecentSubmissionsHeader() {
                Label title = new Label("Recent Submissions");
                title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button filter = new Button("\u2630  Filter");
                filter.setStyle(headerButtonStyle());

                Button sort = new Button("\u21C5  Sort");
                sort.setStyle(headerButtonStyle());
                // TODO: wire filter/sort up to real ComplaintService queries.

                HBox row = new HBox(10, title, spacer, filter, sort);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        private String headerButtonStyle() {
                return "-fx-background-color: " + BACKGROUND + ";" +
                                "-fx-text-fill: " + TEXT_SECONDARY + ";" +
                                "-fx-font-size: 11px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 7;" +
                                "-fx-padding: 8 14 8 14;" +
                                "-fx-cursor: hand;";
        }

        // ---- Complaint list ----
        private VBox buildComplaintsList() {
                // TODO: replace with ComplaintService.getRecentComplaints(villageId)
                VBox list = new VBox(12,
                                complaintRow("\uD83D\uDCA7", LIGHT_BLUE, SECONDARY,
                                                "Broken Water Pipe near Temple", "#CMP-2025-0012",
                                                "Oct 24, 2024", "Temple Square, Zone 4",
                                                "Assigned", LIGHT_BLUE, SECONDARY),
                                complaintRow("\u2195", LIGHT_YELLOW, "#C2703D",
                                                "Pot hole on Main Road", "#CMP-2025-0015",
                                                "Oct 22, 2024", "Highway Junction",
                                                "Pending", LIGHT_YELLOW, "#C2703D"),
                                complaintRow("\uD83D\uDCA1", LIGHT_YELLOW, WARNING,
                                                "Non-functional Street Light", "#CMP-2025-0008",
                                                "Oct 15, 2024", "Gandhi Street",
                                                "Resolved", LIGHT_GREEN, PRIMARY),
                                complaintRow("\uD83D\uDDD1", LIGHT_RED, ERROR,
                                                "Waste Accumulation near School", "#CMP-2025-0021",
                                                "Oct 25, 2024", "Primary School, North Wing",
                                                "Action Required", LIGHT_RED, ERROR));
                return list;
        }

        /**
         * One complaint row: category icon on the left, title + complaint ID
         * + status pill on the top line, date + location on the second line,
         * and a "View Details ->" link underneath.
         */
        private VBox complaintRow(String icon, String iconBg, String iconColor, String title, String complaintId,
                        String date, String location, String status, String pillBg, String pillFg) {

                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-text-fill: " + iconColor + "; -fx-font-size: 15px;");
                StackPane iconCircle = new StackPane(iconLabel);
                iconCircle.setPrefSize(40, 40);
                iconCircle.setMaxSize(40, 40);
                iconCircle.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 20;");

                Label titleLabel = new Label(title);
                titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label dateLabel = new Label("\uD83D\uDCC5  " + date);
                dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label locationLabel = new Label("\uD83D\uDCCD  " + location);
                locationLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_PRIMARY + ";");

                HBox metaRow = new HBox(14, dateLabel, locationLabel);
                metaRow.setAlignment(Pos.CENTER_LEFT);

                Label viewDetails = new Label("View Details  \u2192");
                viewDetails.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + "; "
                                + "-fx-cursor: hand;");
                // TODO: wire this up to open a real ComplaintDetailView(complaintId)

                VBox leftText = new VBox(5, titleLabel, metaRow, viewDetails);
                HBox.setHgrow(leftText, Priority.ALWAYS);

                Label idLabel = new Label(complaintId);
                idLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label statusPill = new Label(status);
                statusPill.setStyle("-fx-background-color: " + pillBg + "; -fx-text-fill: " + pillFg + "; "
                                + "-fx-background-radius: 10; -fx-padding: 4 12 4 12; -fx-font-size: 10px; -fx-font-weight: bold;");

                VBox rightBlock = new VBox(8, idLabel, statusPill);
                rightBlock.setAlignment(Pos.CENTER_RIGHT);

                HBox row = new HBox(14, iconCircle, leftText, rightBlock);
                row.setAlignment(Pos.TOP_LEFT);
                row.setPadding(new Insets(16));
                row.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");

                return new VBox(row);
        }

        // ---- "Show N more complaints" link ----
        private HBox buildShowMoreLink() {
                Label showMore = new Label("Show 24 more complaints");
                showMore.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + "; "
                                + "-fx-cursor: hand;");
                // TODO: wire this up to real pagination in ComplaintService.

                HBox row = new HBox(showMore);
                row.setAlignment(Pos.CENTER);
                row.setPadding(new Insets(6, 0, 10, 0));
                return row;
        }

}
