package com.tech_fusion.view.villager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * GramConnect - Announcements (Villager)
 *
 * ============================================================
 * Follows the same visual language as VillagerDashboard.java /
 * BillsAndPayments.java: forest green + saffron accent palette,
 * translucent "glass card" panels with soft drop-shadow, rounded
 * pill badges and a hover-lift effect on cards.
 *
 * Scope (per request): only the list of announcements. The
 * category tabs (All/General/Schemes/Meetings/Emergencies), the
 * right-hand "Filter Announcements" panel and the "Notice Board"
 * panel from the reference screenshot are intentionally left out.
 *
 * Usage (same pattern as ProjectTransparency.getProjectBPane() and
 * ComplaintsPage.getComplaintsPage()):
 *
 *      root.setCenter(new Announcements().getAnnouncementsPane());
 * ============================================================
 */
public class Announcements {

        // ================= COLORS (shared with VillagerDashboard.java) =================
        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String CONTEXT_TEAL = "#0E8C8C";
        private static final String AI_VIOLET = "#7C5CFC";
        private static final String DELAYED_RED = "#D94C38";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        private static final String BACKGROUND = "#EFF5F1";

        private static final String TEXT_PRIMARY = FOREST_DEEP;
        private static final String TEXT_SECONDARY = "rgba(11,61,46,0.65)";

         private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        /** Public entry point - returns the fully built Announcements screen. */
        public Scene getAnnouncementScene(Runnable backToDashboardAction) {

                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BACKGROUND + ";");

                root.setLeft(buildSidebar(backToDashboardAction));
                root.setCenter(buildMainArea());

                return new Scene(root, 1500, 850);
        }

        // =================================================================
        // SIDEBAR - identical structure/colors to VillagerDashboard.java,
        // except "Project transparency" is the active row here, and
        // "Dashboard" calls the Runnable instead of homeStage.setScene(...)
        // directly (this class never touches a Stage at all).
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
                        VillagerDashboard.homeStage.setScene(new ComplaintsPage().getComplaintsPage(backToDashboardAction));
                });

                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", false);
                complaintsNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new ComplaintsPage().getComplaintsPage(backToDashboardAction));
                });
                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", false);
                schemesNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new GovernmentSchemes().getSchemesScene(backToDashboardAction));
                });
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
                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", true);
                
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
                Label title = new Label("Announcements");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 26px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                Label subtitle = new Label("Stay updated with the latest news and important announcements from your Gram Panchayat.");
                subtitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox heading = new VBox(4, title, subtitle);
                heading.setPadding(new Insets(24, 32, 0, 32));

                VBox content = new VBox(24);
                content.setPadding(new Insets(20, 32, 32, 32));
                content.setStyle("-fx-background-color: " + BACKGROUND + ";");

                content.getChildren().addAll(heading, buildAnnouncementsListCard());

                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
                return scrollPane;
        }

        // =================================================================
        // ANNOUNCEMENTS LIST
        // =================================================================
        private VBox buildAnnouncementsListCard() {

                // TODO: replace with AnnouncementService.getAllAnnouncements(villageId)
                VBox list = new VBox(0,
                                announcementRow(
                                                "\uD83D\uDCE2", "#3A8CD6",
                                                "Gram Sabha Meeting on 15 June 2024",
                                                "Meetings", "#3A8CD6",
                                                "All villagers are informed that the Gram Sabha meeting will be held on 15th June 2024 at 11:00 AM in the Panchayat Office. Your presence is requested.",
                                                "10 Jun 2024", true),
                                announcementRow(
                                                "\uD83C\uDF31", FOREST_DEEP,
                                                "Applications Open for PM Kisan Yojana",
                                                "Schemes", FOREST_DEEP,
                                                "Applications for PM Kisan Samman Nidhi Yojana are now open. Eligible farmers can apply online or contact the Gram Panchayat office for assistance.",
                                                "08 Jun 2024", true),
                                announcementRow(
                                                "\u26A0", SAFFRON_MAIN,
                                                "Road Construction Work Update",
                                                "General", SAFFRON_MAIN,
                                                "The road construction work from Suryapuri to Kalgaon is in progress. Please cooperate and avoid unnecessary travel on the route.",
                                                "05 Jun 2024", true),
                                announcementRow(
                                                "\u26A0", DELAYED_RED,
                                                "Heatwave Alert \u2013 Stay Safe",
                                                "Emergencies", DELAYED_RED,
                                                "IMD has issued a heatwave alert for the next few days. Stay hydrated, avoid direct sunlight and take necessary precautions.",
                                                "03 Jun 2024", true));

                VBox card = new VBox(list);
                card.setStyle(cardStyle(20));
                addHoverLift(card, 20);
                return card;
        }

        /**
         * One announcement row: icon chip, title + category pill + "New" pill on
         * top, description below, published date at the bottom, and the list
         * date pinned to the right - matching the reference screenshot layout.
         */
        private VBox announcementRow(String icon, String iconColor, String title, String categoryText,
                        String categoryColor, String description, String date, boolean isNew) {

                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 22px;");
                StackPane iconChip = new StackPane(iconLabel);
                iconChip.setPrefSize(56, 56);
                iconChip.setMaxSize(56, 56);
                iconChip.setStyle("-fx-background-color: " + rgba(iconColor, 0.12) + "; -fx-background-radius: 14;");

                Label titleLabel = new Label(title);
                titleLabel.setWrapText(true);
                titleLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                Label newPill = new Label("New");
                newPill.setPadding(new Insets(3, 10, 3, 10));
                newPill.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: " + rgba(FOREST_DEEP, 0.12) + ";" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";" +
                                                "-fx-background-radius: 999;" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: 800;");

                Region titleSpacer = new Region();
                HBox.setHgrow(titleSpacer, Priority.ALWAYS);

                Label dateLabel = new Label(date);
                dateLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");

                HBox titleRow = new HBox(10, titleLabel, titleSpacer);
                if (isNew) {
                        titleRow.getChildren().add(1, newPill);
                }
                titleRow.getChildren().add(dateLabel);
                titleRow.setAlignment(Pos.CENTER_LEFT);

                Label categoryPill = new Label(categoryText);
                categoryPill.setPadding(new Insets(3, 10, 3, 10));
                categoryPill.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: " + rgba(categoryColor, 0.12) + ";" +
                                                "-fx-text-fill: " + categoryColor + ";" +
                                                "-fx-background-radius: 999;" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: 800;");
                HBox categoryRow = new HBox(categoryPill);
                categoryRow.setAlignment(Pos.CENTER_LEFT);

                Label descriptionLabel = new Label(description);
                descriptionLabel.setWrapText(true);
                descriptionLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");

                Label publishedIcon = new Label("\uD83D\uDDD3");
                publishedIcon.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                Label publishedLabel = new Label("Published on: " + date);
                publishedLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");
                HBox publishedRow = new HBox(4, publishedIcon, publishedLabel);
                publishedRow.setAlignment(Pos.CENTER_LEFT);

                VBox details = new VBox(8, titleRow, categoryRow, descriptionLabel, publishedRow);
                HBox.setHgrow(details, Priority.ALWAYS);

                HBox row = new HBox(16, iconChip, details);
                row.setAlignment(Pos.TOP_LEFT);
                row.setPadding(new Insets(20, 24, 20, 24));

                // subtle divider under every row except handled via border on the VBox wrapper
                VBox wrapper = new VBox(row);
                wrapper.setStyle("-fx-border-color: transparent transparent rgba(11,61,46,0.08) transparent;"
                                + "-fx-border-width: 0 0 1 0;");

                return wrapper;
        }

        // =================================================================
        // HELPERS (shared look with VillagerDashboard.java / BillsAndPayments.java)
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
