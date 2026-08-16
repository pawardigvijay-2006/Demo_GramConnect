package com.tech_fusion.view.villager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

/**
 * GramConnect - Project Transparency
 *
 * ============================================================
 * NAVIGATION NOTE (read this first!)
 * ============================================================
 * This page owns its OWN sidebar + header, exactly like
 * VillagerDashboard.java, and hands back a full Scene instead of just
 * a BorderPane. That's what lets VillagerDashboard swap the WHOLE
 * window over to this page with homeStage.setScene(...) and back
 * again.
 *
 * The pattern, step by step:
 * 1. VillagerDashboard's "Project transparency" nav item creates a
 * Runnable that knows how to get back to the dashboard:
 * Runnable backToDashboardAction = () -> back();
 * 2. It hands that Runnable straight into this page:
 * homeStage.setScene(new ProjectTransparency().getProjectScene(backToDashboardAction));
 * 3. This page never needs to import VillagerDashboard's internals -
 * it just wires its OWN "Dashboard" nav item to go there.
 *
 * BUGFIX NOTE (View Details <-> Project Details navigation):
 * getProjectScene(Runnable) now stores the Runnable it's given in the
 * backToDashboardAction FIELD below (not just a local parameter).
 * Previously, back() - called when returning here from
 * ProjectTransparency1 via the "View Details" -> "Back to Projects"
 * loop - rebuilt this page with getProjectScene(null), which silently
 * discarded the real "return to Dashboard" callback. Any further
 * navigation to a sibling page (Complaints, Schemes, ...) from that
 * rebuilt page then forwarded null instead of a working Runnable.
 * back() now reuses the stored field instead, so the callback
 * survives the round trip through ProjectTransparency1.
 *
 * Everything else here (5 stat cards, "All Projects" list with filter
 * tabs, Budget Overview bars, Recent Updates timeline, Transparency &
 * Accountability banner) is unchanged.
 * ============================================================
 */
public class ProjectTransparency {

        // ================= COLORS (same palette as VillagerDashboard.java) =================
        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String FOREST_LIGHT = "#0F4736";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String CONTEXT_TEAL = "#0E8C8C";
        private static final String DELAYED_RED = "#D94C38";

        // Light green sidebar gradient (identical to VillagerDashboard.java)
        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        // Extra accents used only on this page's cards/pills
        private static final String LIGHT_GREEN = "#E8F7EA";
        private static final String LIGHT_BLUE = "#EAF5FC";
        private static final String BLUE = "#2196F3";
        private static final String WARNING = "#F4A62A";
        private static final String LIGHT_YELLOW = "#FFF7E5";
        private static final String ERROR = "#D93025";
        private static final String LIGHT_RED = "#FDECEC";
        private static final String PURPLE = "#8B5CF6";
        private static final String LIGHT_PURPLE = "#F1EDFC";
        private static final String SECONDARY = "#1976D2";

        private static final String BACKGROUND = "#EFF5F1";
        private static final String TEXT_PRIMARY = FOREST_DEEP;
        private static final String TEXT_SECONDARY = "rgba(11,61,46,0.65)";
        private static final String BORDER = "#D8E2DC";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        /**
         * BUGFIX: remembers the Runnable this page was given, so back() (called
         * when returning from ProjectTransparency1) can rebuild this page
         * WITHOUT losing the real "return to Dashboard" callback. See
         * NAVIGATION NOTE above for the full story.
         */
        private Runnable backToDashboardAction;

        /**
         * Builds this page's full Scene: its own sidebar (with "Project
         * transparency" highlighted as active) on the left, header + scrollable
         * content on the right.
         *
         * @param backToDashboardAction what to run when this page's own
         *                               "Dashboard" nav item is clicked. Passed
         *                               in by whoever navigates here, so this
         *                               class stays decoupled from
         *                               VillagerDashboard.
         */
        public Scene getProjectScene(Runnable backToDashboardAction) {

                // BUGFIX: store it before building anything, so back() (near the
                // bottom of this file) can reuse the SAME callback later instead
                // of rebuilding this page with a null one.
                this.backToDashboardAction = backToDashboardAction;

                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BACKGROUND + ";");

                root.setLeft(buildSidebar(backToDashboardAction));
                root.setCenter(buildMainArea());

                return new Scene(root, 1500, 850);
        }

        // =================================================================
        // SIDEBAR - identical structure/colors to VillagerDashboard.java,
        // except "Project transparency" is the active row here, and
        // "Dashboard" navigates directly to the real Dashboard scene
        // (this class never touches a Stage anywhere else).
        // =================================================================
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

                Label dashboardNav = navItem("\uD83C\uDFE0  Dashboard", false);
                dashboardNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new VillagerDashboard().getDashboardScene());
                });

                Label projectsNav = navItem("\uD83C\uDFD7  Project transparency", true);
                // Already on this page - no handler needed.

                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", false);
                complaintsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new ComplaintsPage().getComplaintsPage(backToDashboardAction));
                });
                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", false);
                schemesNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new GovernmentSchemes().getSchemesScene(backToDashboardAction));
                });
                Label certificatesNav = navItem("\uD83D\uDCDC  Certificates", false);
                certificatesNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new Certificates().getCertificatesScene(backToDashboardAction));
                });
                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", false);
                billsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new BillsAndPayments().getBillsScene(backToDashboardAction));
                });
                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                announcementsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new Announcements().getAnnouncementScene(backToDashboardAction));
                });
                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", false);
                gramSabhaNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new GramSabha().getGramSabhaScene(backToDashboardAction));
                });
                Label aiAssistantNav = navItem("\uD83E\uDD16  AI village assistant", false);
                aiAssistantNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new AIAssistant().getAiAssiatantScene(backToDashboardAction));
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

        /** Same nav-row builder as VillagerDashboard.java: no handler attached here - callers attach their own. */
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

        /** Identical header (search + bell + profile) as VillagerDashboard.java. */
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
                search.setPromptText("Search projects...");
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
                VBox content = new VBox(18);
                content.setPadding(new Insets(18, 24, 28, 24));

                content.getChildren().addAll(
                                buildPageTitleRow(),
                                buildStatCardsRow(),
                                buildProjectsAndSidebarRow(),
                                buildTransparencyBanner());

                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent;");
                return scrollPane;
        }

        // ---- Page title row ----
        private HBox buildPageTitleRow() {
                Label title = new Label("Project Transparency");
                title.setStyle("-fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 24px; -fx-font-weight: bold;");

                Label subtitle = new Label("Real-time status of village development projects");
                subtitle.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 12px;");

                VBox titleBox = new VBox(2, title, subtitle);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button downloadReport = new Button("\u2B07  Download Report");
                downloadReport.setStyle(
                                "-fx-background-color: " + FOREST_DEEP + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-padding: 10 16 10 16;" +
                                                "-fx-cursor: hand;");
                // TODO: wire this up to a real ReportService.exportProjectReport(villageId)

                HBox row = new HBox(titleBox, spacer, downloadReport);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- Stat cards (5 across the top) ----
        private HBox buildStatCardsRow() {
                HBox row = new HBox(14,
                                statCard("\uD83D\uDCCA", LIGHT_GREEN, FOREST_DEEP, "12", "Total Projects",
                                                "All development projects"),
                                statCard("\uD83D\uDCC8", LIGHT_BLUE, SECONDARY, "8", "In Progress",
                                                "Projects under construction"),
                                statCard("\u2705", LIGHT_YELLOW, WARNING, "3", "Completed",
                                                "Successfully finished"),
                                statCard("\u23F0", LIGHT_RED, ERROR, "1", "Delayed",
                                                "Behind schedule"),
                                statCard("\u20B9", LIGHT_PURPLE, PURPLE, "\u20B918.45L", "Total Budget",
                                                "Allocated for projects"));
                return row;
        }

        private VBox statCard(String icon, String iconBg, String iconColor, String value, String label,
                        String caption) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-text-fill: " + iconColor + "; -fx-font-size: 14px;");

                StackPane iconCircle = new StackPane(iconLabel);
                iconCircle.setPrefSize(38, 38);
                iconCircle.setMaxSize(38, 38);
                iconCircle.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 9;");

                Label valueLabel = new Label(value);
                valueLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label textLabel = new Label(label);
                textLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label captionLabel = new Label(caption);
                captionLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox card = new VBox(6, iconCircle, valueLabel, textLabel, captionLabel);
                card.setPadding(new Insets(14));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                HBox.setHgrow(card, Priority.ALWAYS);
                return card;
        }

        // ---- "All Projects" (left) + "Budget Overview" / "Recent Updates" (right) ----
        private HBox buildProjectsAndSidebarRow() {
                HBox row = new HBox(16, buildAllProjectsCard(), buildRightColumn());
                return row;
        }

        // =================================================================
        // ALL PROJECTS CARD
        // =================================================================
        private VBox buildAllProjectsCard() {

                Label title = new Label("All Projects");
                title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                HBox tabs = new HBox(6,
                                filterTab("All Projects", true),
                                filterTab("In Progress", false),
                                filterTab("Completed", false),
                                filterTab("Delayed", false));

                Region tabSpacer = new Region();
                HBox.setHgrow(tabSpacer, Priority.ALWAYS);

                ComboBox<String> categoryFilter = new ComboBox<>();
                categoryFilter.getItems().addAll("All Categories", "Roads", "Water", "Electricity");
                categoryFilter.setValue("All Categories");
                categoryFilter.setStyle("-fx-font-size: 11px;");

                TextField searchProject = new TextField();
                searchProject.setPromptText("Search project...");
                searchProject.setPrefWidth(160);
                searchProject.setStyle("-fx-background-color: " + BACKGROUND + "; -fx-background-radius: 6; "
                                + "-fx-font-size: 11px; -fx-padding: 6 10 6 10;");

                HBox filterRow = new HBox(8, tabs, tabSpacer, categoryFilter, searchProject);
                filterRow.setAlignment(Pos.CENTER_LEFT);

                // TODO: replace with ProjectService.getProjectsForVillage(villageId)
                VBox list = new VBox(16,
                                projectCard(
                                                "assets\\images\\road.jpg",
                                                "Village Road Construction",
                                                "On Track",
                                                "Main Street, Suryapuri",
                                                "Construction of main village road with proper drainage and street lighting.",
                                                "\u20B96,25,000", "15 Mar 2026", "15 Sep 2026", 78),
                                projectCard(
                                                "assets\\images\\water_tank.jpg",
                                                "Water Tank Renovation",
                                                "In Progress",
                                                "Near School Area",
                                                "Renovation of old water tank and improvement of water storage capacity.",
                                                "\u20B93,80,000", "10 Apr 2026", "10 Aug 2026", 45),
                                projectCard(
                                                "assets\\images\\street_light.jpg",
                                                "Street Light Installation",
                                                "Delayed",
                                                "All Village Roads",
                                                "Installation of LED street lights on all village roads for better visibility and safety.",
                                                "\u20B92,40,000", "20 Feb 2026", "20 Aug 2026", 25));

                VBox card = new VBox(16, title, filterRow, list);
                card.setPadding(new Insets(18));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                HBox.setHgrow(card, Priority.ALWAYS);
                return card;
        }

        private Label filterTab(String text, boolean active) {
                Label tab = new Label(text);
                if (active) {
                        tab.setStyle("-fx-background-color: " + FOREST_DEEP + "; -fx-text-fill: white; "
                                        + "-fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; "
                                        + "-fx-padding: 7 14 7 14; -fx-cursor: hand;");
                } else {
                        tab.setStyle("-fx-background-color: white; -fx-text-fill: " + TEXT_SECONDARY + "; "
                                        + "-fx-border-color: " + BORDER
                                        + "; -fx-border-radius: 6; -fx-background-radius: 6; "
                                        + "-fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 7 14 7 14; -fx-cursor: hand;");
                }
                return tab;
        }

        private VBox projectCard(String imagePath, String name, String status, String location, String description,
                        String budget, String startDate, String endDate, int percent) {

                ImageView image = new ImageView(new Image(imagePath));
                image.setFitWidth(140);
                image.setFitHeight(100);
                image.setPreserveRatio(false);
                Rectangle clip = new Rectangle(140, 100);
                clip.setArcWidth(12);
                clip.setArcHeight(12);
                image.setClip(clip);

                String pillBg, pillFg, accent;
                switch (status) {
                        case "Delayed":
                                pillBg = "#FFEBEE";
                                pillFg = "#791F1F";
                                accent = ERROR;
                                break;
                        case "In Progress":
                                pillBg = "#FFF3E0";
                                pillFg = "#854F0B";
                                accent = SECONDARY;
                                break;
                        default:
                                pillBg = "#E8F5E9";
                                pillFg = "#1B5E20";
                                accent = FOREST_DEEP;
                }

                Label nameLabel = new Label(name);
                nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label statusPill = new Label(status);
                statusPill.setStyle("-fx-background-color: " + pillBg + "; -fx-text-fill: " + pillFg + "; "
                                + "-fx-background-radius: 10; -fx-padding: 3 10 3 10; -fx-font-size: 10px; -fx-font-weight: bold;");

                HBox nameRow = new HBox(8, nameLabel, statusPill);
                nameRow.setAlignment(Pos.CENTER_LEFT);

                Label locationLabel = new Label(location);
                locationLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                Label descriptionLabel = new Label(description);
                descriptionLabel.setWrapText(true);
                descriptionLabel.setMaxWidth(420);
                descriptionLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                Label budgetLabel = new Label(budget);
                budgetLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                Label startLabel = new Label("Start: " + startDate);
                startLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                Label endLabel = new Label("End: " + endDate);
                endLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                HBox metaRow = new HBox(16, budgetLabel, startLabel, endLabel);
                metaRow.setAlignment(Pos.CENTER_LEFT);

                VBox details = new VBox(6, nameRow, locationLabel, descriptionLabel, metaRow);
                details.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(details, Priority.ALWAYS);

                Label percentLabel = new Label(percent + "%");
                percentLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + accent + ";");

                Label completeLabel = new Label("Complete");
                completeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                ProgressBar bar = new ProgressBar(percent / 100.0);
                bar.setPrefWidth(140);
                bar.setPrefHeight(6);
                bar.setStyle("-fx-accent: " + accent + ";");

                Button viewDetails = new Button("View Details");
                viewDetails.setPrefWidth(140);
                viewDetails.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";" +
                                                "-fx-border-color: " + FOREST_DEEP + ";" +
                                                "-fx-border-radius: 6;" +
                                                "-fx-background-radius: 6;" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-padding: 7;" +
                                                "-fx-cursor: hand;");
                // TODO: wire this up to open a real ProjectDetailView(projectId)

                // BUGFIX: "backAction" calls back() on THIS instance, and back()
                // (below) rebuilds this page using the backToDashboardAction
                // FIELD - captured earlier in getProjectScene(...) - instead of
                // discarding it with getProjectScene(null). This is what keeps
                // the "Dashboard" callback alive across a View Details -> Back
                // to Projects round trip.
                viewDetails.setOnAction(e -> {
                        Runnable backAction = () -> back();
                        VillagerDashboard.homeStage.setScene(new ProjectTransparency1().getProjectDetailsScene(backAction));
                });

                VBox rightBlock = new VBox(6, percentLabel, completeLabel, bar, viewDetails);
                rightBlock.setAlignment(Pos.CENTER_RIGHT);
                rightBlock.setPrefWidth(140);

                HBox row = new HBox(16, image, details, rightBlock);
                row.setAlignment(Pos.TOP_LEFT);
                row.setPadding(new Insets(14));
                row.setStyle("-fx-background-color: " + BACKGROUND + "; -fx-background-radius: 10; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 10; -fx-border-width: 1;");

                return new VBox(row);
        }

        // =================================================================
        // RIGHT COLUMN: Budget Overview + Recent Updates
        // =================================================================
        private VBox buildRightColumn() {
                VBox column = new VBox(16, buildBudgetOverviewCard(), buildRecentUpdatesCard());
                column.setPrefWidth(330);
                column.setMinWidth(330);
                return column;
        }

        private VBox buildBudgetOverviewCard() {
                Label title = new Label("Budget Overview");
                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label totalLabel = new Label("Total Allocated: \u20B918,45,000");
                totalLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox bars = new VBox(16,
                                budgetBar(FOREST_DEEP, "Spent", "\u20B98,45,000", 45),
                                budgetBar(SECONDARY, "In Progress", "\u20B96,75,000", 37),
                                budgetBar(WARNING, "Remaining", "\u20B93,25,000", 18));

                VBox card = new VBox(14, title, totalLabel, bars);
                card.setPadding(new Insets(18));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return card;
        }

        private VBox budgetBar(String color, String label, String amount, int percent) {
                Circle dot = new Circle(4, Color.web(color));

                Label labelText = new Label(label);
                labelText.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                HBox labelRow = new HBox(6, dot, labelText);
                labelRow.setAlignment(Pos.CENTER_LEFT);

                Region topSpacer = new Region();
                HBox.setHgrow(topSpacer, Priority.ALWAYS);

                Label amountText = new Label(amount + "  (" + percent + "%)");
                amountText.setStyle(
                                "-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_SECONDARY + ";");

                HBox topRow = new HBox(labelRow, topSpacer, amountText);
                topRow.setAlignment(Pos.CENTER_LEFT);

                double trackWidth = 280;

                Region track = new Region();
                track.setPrefSize(trackWidth, 9);
                track.setMaxSize(trackWidth, 9);
                track.setStyle("-fx-background-color: " + BACKGROUND + "; -fx-background-radius: 5;");

                Region fill = new Region();
                fill.setPrefSize(trackWidth * (percent / 100.0), 9);
                fill.setMaxSize(trackWidth * (percent / 100.0), 9);
                fill.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 5;");

                StackPane barStack = new StackPane(track, fill);
                barStack.setAlignment(Pos.CENTER_LEFT);

                return new VBox(6, topRow, barStack);
        }

        private VBox buildRecentUpdatesCard() {
                Label title = new Label("Recent Updates");
                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                // TODO: replace with UpdateService.getRecentUpdates(villageId)
                VBox list = new VBox(14,
                                updateRow("\u2705", LIGHT_GREEN, FOREST_DEEP, "Village road construction",
                                                "Road paving completed on Main Street", "2 days ago"),
                                updateRow("\u23F0", LIGHT_YELLOW, WARNING, "Water tank renovation",
                                                "Tank cleaning and painting in progress", "5 days ago"),
                                updateRow("\u23F0", LIGHT_RED, ERROR, "Street light installation",
                                                "Material delivery delayed due to rain", "1 week ago"));

                Button viewAll = new Button("View All Updates");
                viewAll.setMaxWidth(Double.MAX_VALUE);
                viewAll.setStyle(
                                "-fx-background-color: " + FOREST_DEEP + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6;" +
                                                "-fx-padding: 9;" +
                                                "-fx-cursor: hand;");

                VBox card = new VBox(14, title, list, viewAll);
                card.setPadding(new Insets(18));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return card;
        }

        private HBox updateRow(String icon, String iconBg, String iconColor, String title, String description,
                        String time) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-text-fill: " + iconColor + "; -fx-font-size: 12px;");
                StackPane iconCircle = new StackPane(iconLabel);
                iconCircle.setPrefSize(26, 26);
                iconCircle.setMaxSize(26, 26);
                iconCircle.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 13;");

                Label titleLabel = new Label(title);
                titleLabel.setWrapText(true);
                titleLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label descLabel = new Label(description);
                descLabel.setWrapText(true);
                descLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                Label timeLabel = new Label(time);
                timeLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox textBox = new VBox(2, titleLabel, descLabel, timeLabel);
                HBox.setHgrow(textBox, Priority.ALWAYS);

                HBox row = new HBox(10, iconCircle, textBox);
                row.setAlignment(Pos.TOP_LEFT);
                return row;
        }

        // =================================================================
        // TRANSPARENCY & ACCOUNTABILITY BANNER (bottom strip)
        // =================================================================
        private HBox buildTransparencyBanner() {
                Label shield = new Label("\uD83D\uDEE1");
                shield.setStyle("-fx-text-fill: " + FOREST_DEEP + "; -fx-font-size: 20px;");
                StackPane shieldCircle = new StackPane(shield);
                shieldCircle.setPrefSize(38, 38);
                shieldCircle.setMaxSize(38, 38);
                shieldCircle.setStyle("-fx-background-color: white; -fx-background-radius: 19;");

                Label title = new Label("Transparency & Accountability");
                title.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label body = new Label(
                                "All project information is updated regularly by the Sarpanch and Gram Panchayat. "
                                                + "For any queries or discrepancies, please contact your Gram Sevak.");
                body.setWrapText(true);
                body.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox textBox = new VBox(2, title, body);
                HBox.setHgrow(textBox, Priority.ALWAYS);

                HBox banner = new HBox(12, shieldCircle, textBox);
                banner.setAlignment(Pos.CENTER_LEFT);
                banner.setPadding(new Insets(16, 20, 16, 20));
                banner.setStyle("-fx-background-color: " + LIGHT_GREEN + "; -fx-background-radius: 12; "
                                + "-fx-border-color: " + FOREST_DEEP
                                + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return banner;
        }

        /**
         * BUGFIX: previously called getProjectScene(null), which discarded the
         * real backToDashboardAction this page was originally opened with.
         * Now reuses the value captured in the field during
         * getProjectScene(...), so the "Dashboard" callback keeps working even
         * after a View Details -> Back to Projects round trip.
         */
        private void back() {
                VillagerDashboard.homeStage.setTitle("GramConnect - Project Transparency");
                VillagerDashboard.homeStage.setScene(getProjectScene(backToDashboardAction));
        }
}