package com.tech_fusion.view.villager;

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

/**
 * GramConnect - Gram Sabha (Villager)
 *
 * ============================================================
 * Follows the same visual language as VillagerDashboard.java /
 * BillsAndPayments.java / Announcements.java: forest green +
 * saffron accent palette, translucent "glass card" panels with
 * soft drop-shadow, rounded pill badges and a hover-lift effect
 * on cards.
 *
 * Scope (per request): everything in the reference screenshot
 * EXCEPT the "Documents" panel, the "Resolutions Passed" stat
 * card, and the "Resolutions" column in the Previous Meetings
 * table.
 *
 * Usage (same pattern as ProjectTransparency.getProjectBPane() and
 * ComplaintsPage.getComplaintsPage()):
 *
 *      root.setCenter(new GramSabha().getGramSabhaPane());
 * ============================================================
 */
public class GramSabha {

        // ================= COLORS (shared with VillagerDashboard.java) =================
        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String CONTEXT_TEAL = "#0E8C8C";
        private static final String AI_VIOLET = "#7C5CFC";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        private static final String BACKGROUND = "#EFF5F1";

        private static final String TEXT_PRIMARY = FOREST_DEEP;
        private static final String TEXT_SECONDARY = "rgba(11,61,46,0.65)";

        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";
        private static final String DELAYED_RED = "#D94C38";


        /** Public entry point - returns the fully built Bills & Payments screen. */
        public Scene getGramSabhaScene(Runnable backToDashboardAction) {

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
                        VillagerDashboard.homeStage.setScene(new ProjectTransparency().getProjectScene(backToDashboardAction));
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
                        VillagerDashboard.homeStage.setScene(new ComplaintsPage().getComplaintsPage(backToDashboardAction));
                });

                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                announcementsNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new Announcements().getAnnouncementScene(backToDashboardAction));
                });
                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", true);
                
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
                VBox content = new VBox(20);
                content.setPadding(new Insets(20, 32, 32, 32));
                content.setStyle("-fx-background-color: " + BACKGROUND + ";");

                Label title = new Label("Gram Sabha");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 26px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                Label subtitle = new Label("Participate in Gram Sabha meetings and stay informed about village decisions.");
                subtitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox heading = new VBox(4, title, subtitle);
                heading.setPadding(new Insets(24, 32, 0, 32));

                HBox mainRow = new HBox(20, buildLeftColumn(), buildRightColumn());

                content.getChildren().addAll(heading, buildStatCardsRow(), mainRow);

                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
                return scrollPane;
        }

        // =================================================================
        // STAT CARDS ROW (Resolutions Passed card removed)
        // =================================================================
        private HBox buildStatCardsRow() {
                return new HBox(18,
                                statCard("\uD83D\uDC65", FOREST_DEEP, "Total Meetings", "12", "This Year"),
                                statCard("\uD83D\uDCC5", "#3A8CD6", "Upcoming Meeting", "15 Jun 2024", "Sunday, 11:00 AM"),
                                statCard("\uD83D\uDC64", AI_VIOLET, "Your Attendance", "8 / 12", "Meetings"));
        }

        private VBox statCard(String icon, String iconColor, String label, String value, String caption) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 18px;");
                StackPane iconChip = new StackPane(iconLabel);
                iconChip.setPrefSize(46, 46);
                iconChip.setMaxSize(46, 46);
                iconChip.setStyle("-fx-background-color: " + iconColor + "; -fx-background-radius: 12;");

                Label labelLabel = new Label(label);
                labelLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");

                Label valueLabel = new Label(value);
                valueLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 21px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + iconColor + ";");

                Label captionLabel = new Label(caption);
                captionLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox textBox = new VBox(2, labelLabel, valueLabel, captionLabel);

                HBox top = new HBox(14, iconChip, textBox);
                top.setAlignment(Pos.CENTER_LEFT);

                VBox card = new VBox(top);
                card.setPadding(new Insets(18));
                HBox.setHgrow(card, Priority.ALWAYS);
                card.setStyle(cardStyle(16));
                addHoverLift(card, 16);
                return card;
        }

        // =================================================================
        // LEFT COLUMN - Upcoming Meeting + Previous Meetings
        // =================================================================
        private VBox buildLeftColumn() {
                VBox column = new VBox(20, buildUpcomingMeetingCard(), buildPreviousMeetingsCard());
                HBox.setHgrow(column, Priority.ALWAYS);
                return column;
        }

        private VBox buildUpcomingMeetingCard() {
                Label sectionTitle = new Label("Upcoming Gram Sabha Meeting");
                sectionTitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";");

                Label iconLabel = new Label("\uD83D\uDCC5");
                iconLabel.setStyle("-fx-font-size: 20px;");
                StackPane iconChip = new StackPane(iconLabel);
                iconChip.setPrefSize(48, 48);
                iconChip.setMaxSize(48, 48);
                iconChip.setStyle("-fx-background-color: " + FOREST_DEEP + "; -fx-background-radius: 12;");

                Label meetingTitle = new Label("Gram Sabha Meeting \u2013 June 2024");
                meetingTitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                Label upcomingPill = new Label("Upcoming");
                upcomingPill.setPadding(new Insets(4, 12, 4, 12));
                upcomingPill.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: " + rgba(FOREST_DEEP, 0.12) + ";" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";" +
                                                "-fx-background-radius: 999;" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: 800;");

                Region titleSpacer = new Region();
                HBox.setHgrow(titleSpacer, Priority.ALWAYS);

                HBox titleRow = new HBox(10, iconChip, meetingTitle, titleSpacer, upcomingPill);
                titleRow.setAlignment(Pos.CENTER_LEFT);

                VBox metaBox = new VBox(8,
                                metaRow("\uD83D\uDCC6", "Date:", "15 June 2024 (Sunday)"),
                                metaRow("\uD83D\uDD52", "Time:", "11:00 AM"),
                                metaRow("\uD83D\uDCCD", "Venue:", "Gram Panchayat Office, Suryapuri"));
                metaBox.setPadding(new Insets(0, 0, 0, 60));

                Region divider = new Region();
                divider.setPrefHeight(1);
                divider.setStyle("-fx-background-color: rgba(11,61,46,0.10);");

                Label agendaLabel = new Label("Agenda:");
                agendaLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                VBox agendaItems = new VBox(4,
                                agendaItem("Discussion on village development works"),
                                agendaItem("Review of ongoing projects"),
                                agendaItem("Approval of new proposals"),
                                agendaItem("Financial report presentation"));

                VBox agendaBox = new VBox(8, agendaLabel, agendaItems);
                agendaBox.setPadding(new Insets(0, 0, 0, 60));

                Button viewAgenda = new Button("View Agenda");
                viewAgenda.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-border-color: " + FOREST_DEEP + ";" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-border-width: 1.5;" +
                                                "-fx-padding: 8 18 8 18;" +
                                                "-fx-cursor: hand;");
                HBox agendaButtonRow = new HBox(viewAgenda);
                agendaButtonRow.setAlignment(Pos.CENTER_RIGHT);
                agendaButtonRow.setPadding(new Insets(0, 0, 0, 60));

                VBox meetingDetails = new VBox(14, titleRow, metaBox, divider, agendaBox, agendaButtonRow);
                meetingDetails.setPadding(new Insets(18));
                meetingDetails.setStyle(
                                "-fx-background-color: " + rgba(FOREST_DEEP, 0.05) + ";" +
                                                "-fx-background-radius: 14;");

                VBox card = new VBox(16, sectionTitle, meetingDetails);
                card.setPadding(new Insets(24));
                card.setStyle(cardStyle(20));
                addHoverLift(card, 20);
                return card;
        }

        private HBox metaRow(String icon, String label, String value) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                Label labelLabel = new Label(label);
                labelLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");
                Label valueLabel = new Label(value);
                valueLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");
                HBox row = new HBox(6, iconLabel, labelLabel, valueLabel);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        private HBox agendaItem(String text) {
                Label bullet = new Label("\u2022");
                bullet.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                Label textLabel = new Label(text);
                textLabel.setWrapText(true);
                textLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");
                HBox row = new HBox(6, bullet, textLabel);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // =================================================================
        // PREVIOUS MEETINGS (Resolutions column removed)
        // =================================================================
        private VBox buildPreviousMeetingsCard() {
                Label title = new Label("Previous Meetings");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";");

                GridPane table = new GridPane();
                table.setHgap(12);
                table.setVgap(4);

                ColumnConstraints colDate = new ColumnConstraints();
                colDate.setPercentWidth(20);
                ColumnConstraints colAgenda = new ColumnConstraints();
                colAgenda.setPercentWidth(45);
                ColumnConstraints colAttendance = new ColumnConstraints();
                colAttendance.setPercentWidth(17);
                ColumnConstraints colMinutes = new ColumnConstraints();
                colMinutes.setPercentWidth(18);
                table.getColumnConstraints().addAll(colDate, colAgenda, colAttendance, colMinutes);

                table.addRow(0,
                                tableHeaderLabel("Meeting Date"),
                                tableHeaderLabel("Agenda"),
                                tableHeaderLabel("Attendance"),
                                tableHeaderLabel("Minutes"));

                // TODO: replace with GramSabhaService.getPreviousMeetings(villageId)
                addMeetingRow(table, 1, "12 May 2024", "Budget Approval & Water Management", "45 / 60");
                addMeetingRow(table, 2, "10 Mar 2024", "Village Road & Drainage Work", "42 / 60");
                addMeetingRow(table, 3, "14 Jan 2024", "New Scheme Proposals", "40 / 60");

                Label viewAll = new Label("View All Meetings \u2192");
                viewAll.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";" +
                                                "-fx-cursor: hand;");
                HBox viewAllRow = new HBox(viewAll);
                viewAllRow.setAlignment(Pos.CENTER);
                viewAllRow.setPadding(new Insets(8, 0, 0, 0));

                VBox card = new VBox(16, title, table, viewAllRow);
                card.setPadding(new Insets(24));
                card.setStyle(cardStyle(20));
                addHoverLift(card, 20);
                return card;
        }

        private Label tableHeaderLabel(String text) {
                Label label = new Label(text);
                label.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";" +
                                                "-fx-letter-spacing: 0.02em;");
                return label;
        }

        private void addMeetingRow(GridPane table, int rowIndex, String date, String agenda, String attendance) {
                Label dateIcon = new Label("\uD83D\uDCC5");
                dateIcon.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                Label dateLabel = new Label(date);
                dateLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");
                HBox dateCell = new HBox(6, dateIcon, dateLabel);
                dateCell.setAlignment(Pos.CENTER_LEFT);

                Label agendaLabel = new Label(agenda);
                agendaLabel.setWrapText(true);
                agendaLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                Label attendanceLabel = new Label(attendance);
                attendanceLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-text-fill: #3A8CD6;");

                Label viewIcon = new Label("\uD83D\uDCC4");
                viewIcon.setStyle("-fx-font-size: 11px;");
                Label viewLabel = new Label("View");
                viewLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";" +
                                                "-fx-cursor: hand;");
                HBox viewCell = new HBox(4, viewIcon, viewLabel);
                viewCell.setAlignment(Pos.CENTER_LEFT);
                // TODO: wire to GramSabhaService.viewMinutes(meetingDate)

                Insets pad = new Insets(12, 4, 12, 4);
                for (var node : new javafx.scene.Node[] { dateCell, agendaLabel, attendanceLabel, viewCell }) {
                        GridPane.setMargin(node, pad);
                }

                table.addRow(rowIndex, dateCell, agendaLabel, attendanceLabel, viewCell);
        }

        // =================================================================
        // RIGHT COLUMN - About Gram Sabha only (Documents panel removed)
        // =================================================================
        private VBox buildRightColumn() {
                VBox column = new VBox(20, buildAboutCard());
                column.setPrefWidth(320);
                column.setMinWidth(300);
                return column;
        }

        private VBox buildAboutCard() {
                Label title = new Label("About Gram Sabha");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";");

                Label iconLabel = new Label("\uD83D\uDC65");
                iconLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                StackPane iconCircle = new StackPane(iconLabel);
                iconCircle.setPrefSize(56, 56);
                iconCircle.setMaxSize(56, 56);
                iconCircle.setStyle("-fx-background-color: rgba(11,61,46,0.06); -fx-background-radius: 999;");
                HBox iconRow = new HBox(iconCircle);
                iconRow.setAlignment(Pos.CENTER_LEFT);

                Label body = new Label(
                                "Gram Sabha is the foundation of democracy in rural areas. Your participation is important for the development and well-being of our village.");
                body.setWrapText(true);
                body.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");

                Button learnMore = new Button("Learn More");
                learnMore.setMaxWidth(Double.MAX_VALUE);
                learnMore.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-border-color: " + FOREST_DEEP + ";" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-border-width: 1.5;" +
                                                "-fx-padding: 10;" +
                                                "-fx-cursor: hand;");

                VBox card = new VBox(14, title, iconRow, body, learnMore);
                card.setPadding(new Insets(24));
                card.setStyle(cardStyle(20));
                addHoverLift(card, 20);
                return card;
        }

        // =================================================================
        // HELPERS (shared look with VillagerDashboard.java / other pages)
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
}
