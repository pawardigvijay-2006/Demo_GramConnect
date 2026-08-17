package com.tech_fusion.view.villager;

import java.util.List;

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

import com.tech_fusion.view.villager.GramSabha.MeetingData;

/**
 * GramConnect - Gram Sabha Meeting Details
 *
 * Same non-Application pattern as the other pages: a plain class with
 * one public method that returns a Scene to swap onto the shared
 * VillagerDashboard.homeStage.
 *
 * Usage (from GramSabha's "View" link on a Previous Meetings row):
 * VillagerDashboard.homeStage.setScene(
 * new MeetingDetailPage().getMeetingDetailScene(meeting, backAction,
 * backToGramSabha));
 *
 * - backAction -> the usual "go to Dashboard" callback, unchanged.
 * - backToGramSabha -> re-opens GramSabha. Used by the sidebar's "Gram
 * Sabha" item and the breadcrumb "Gram Sabha" link - one callback,
 * no competing navigation paths.
 *
 * ============================================================
 * LAYOUT (matches the reference screenshot exactly)
 * ============================================================
 * - Breadcrumb: Gram Sabha > Meeting Details
 * - Title + status pill, with a single "Download Minutes (PDF)"
 *   button on the right (no separate Back button - the breadcrumb
 *   link covers that).
 * - One row: Date / Time / Venue.
 * - "About the Meeting" card - just the description.
 * - "Key Resolutions / Decisions" card - a numbered list.
 * - "Quick Summary" card - Total Registered Voters and Total
 *   Attendance, side by side with a vertical divider.
 *
 * Font sizes and colors follow the same readability rules used across
 * the app: dark forest-green text on light backgrounds for anything
 * the villager needs to read closely (body text is never below 12px),
 * bold high-contrast numerals for the Quick Summary stats, and the
 * status pill uses a saturated text color over a soft tint background
 * rather than low-contrast pastel-on-pastel.
 */
public class MeetingDetailPage {

        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String CONTEXT_TEAL = "#0E8C8C";
        private static final String DELAYED_RED = "#D94C38";
        private static final String ACCENT_GREEN = "#2E9E5B";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        private static final String BACKGROUND = "#EFF5F1";

        private static final String TEXT_PRIMARY = FOREST_DEEP;
        private static final String TEXT_SECONDARY = "rgba(11,61,46,0.65)";

        private static final String BORDER = "rgba(11,61,46,0.12)";

        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        public Scene getMeetingDetailScene(MeetingData meeting, Runnable backAction, Runnable backToGramSabha) {

                BorderPane pane = new BorderPane();
                pane.setTop(buildHeader());
                pane.setCenter(buildScrollableContent(meeting, backToGramSabha));

                BorderPane root = new BorderPane();
                root.setLeft(buildSidebar(backAction, backToGramSabha));
                root.setCenter(pane);

                return new Scene(root, 1500, 900);
        }

        // =================================================================
        // SIDEBAR - "Gram Sabha" stays active since this page is a sub-page
        // of Gram Sabha.
        // =================================================================
        private VBox buildSidebar(Runnable backToDashboardAction, Runnable backToGramSabha) {
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
                                                + "-fx-font-weight: 700;");

                VBox logoTextBox = new VBox(0, logoText, subtitle);
                HBox logo = new HBox(8, logoIcon, logoTextBox);
                logo.setAlignment(Pos.CENTER_LEFT);
                VBox logoBox = new VBox(logo);
                logoBox.setPadding(new Insets(18, 18, 22, 18));

                Label dashboardNav = navItem("\uD83C\uDFE0  Dashboard", false);
                dashboardNav.setOnMouseClicked(e -> backToDashboardAction.run());

                Label projectsNav = navItem("\uD83C\uDFD7  Project transparency", false);
                projectsNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage.setScene(
                                                new ProjectTransparency().getProjectScene(backToDashboardAction)));

                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", false);
                complaintsNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage.setScene(
                                                new ComplaintsPage().getComplaintsPage(backToDashboardAction)));

                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", false);
                schemesNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage.setScene(
                                                new GovernmentSchemes().getSchemesScene(backToDashboardAction)));

                Label certificatesNav = navItem("\uD83D\uDCDC  Certificates", false);
                certificatesNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage.setScene(
                                                new Certificates().getCertificatesScene(backToDashboardAction)));

                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", false);
                billsNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage.setScene(
                                                new BillsAndPayments().getBillsScene(backToDashboardAction)));

                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                announcementsNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage.setScene(
                                                new Announcements().getAnnouncementScene(backToDashboardAction)));

                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", true);
                gramSabhaNav.setOnMouseClicked(e -> backToGramSabha.run());

                Label aiAssistantNav = navItem("\uD83E\uDD16  AI village assistant", false);
                aiAssistantNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage.setScene(
                                                new AIAssistant().getAiAssiatantScene(backToDashboardAction)));

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
        // HEADER
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

        // =================================================================
        // CONTENT
        // =================================================================
        private ScrollPane buildScrollableContent(MeetingData meeting, Runnable backToGramSabha) {
                VBox content = new VBox(18);
                content.setPadding(new Insets(24, 32, 32, 32));
                content.setStyle("-fx-background-color: " + BACKGROUND + ";");

                content.getChildren().addAll(
                                buildBreadcrumb(backToGramSabha),
                                buildTitleRow(meeting),
                                buildMetaRow(meeting),
                                buildAboutCard(meeting),
                                buildResolutionsCard(meeting),
                                buildQuickSummaryCard(meeting));

                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
                return scrollPane;
        }

        // ---- Breadcrumb: Gram Sabha > Meeting Details ----
        private HBox buildBreadcrumb(Runnable backToGramSabha) {
                Label gramSabhaLink = new Label("Gram Sabha");
                gramSabhaLink.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + CONTEXT_TEAL + "; -fx-cursor: hand;");
                gramSabhaLink.setOnMouseClicked(e -> backToGramSabha.run());

                Label separator = new Label("\u203A");
                separator.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                Label current = new Label("Meeting Details");
                current.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                HBox crumb = new HBox(6, gramSabhaLink, separator, current);
                crumb.setAlignment(Pos.CENTER_LEFT);
                return crumb;
        }

        // ---- Title row: title + status pill on the left, Download Minutes on the right ----
        private HBox buildTitleRow(MeetingData meeting) {
                Label title = new Label("Gram Sabha Meeting \u2013 " + meeting.date);
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                Label statusPill = new Label(meeting.status);
                statusPill.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: " + rgba(ACCENT_GREEN, 0.16) + ";"
                                                + "-fx-text-fill: " + ACCENT_GREEN + ";"
                                                + "-fx-font-size: 11px; -fx-font-weight: 800;"
                                                + "-fx-background-radius: 999; -fx-padding: 4 12 4 12;");

                HBox titleGroup = new HBox(12, title, statusPill);
                titleGroup.setAlignment(Pos.CENTER_LEFT);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button downloadBtn = new Button("\u2B07  Download Minutes (PDF)");
                downloadBtn.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: white;"
                                                + "-fx-text-fill: " + ACCENT_GREEN + ";"
                                                + "-fx-font-size: 13px; -fx-font-weight: 800;"
                                                + "-fx-border-color: " + ACCENT_GREEN + "; -fx-border-radius: 8;"
                                                + "-fx-border-width: 1.5;"
                                                + "-fx-background-radius: 8; -fx-padding: 11 18 11 18; -fx-cursor: hand;");
                // TODO: wire to MinutesService.downloadMinutes(meeting) once a real
                // backend/document store exists.

                HBox row = new HBox(titleGroup, spacer, downloadBtn);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- Date / Time / Venue meta row ----
        private HBox buildMetaRow(MeetingData meeting) {
                HBox row = new HBox(28,
                                metaItem("\uD83D\uDCC6", "Date:", meeting.date + " (" + meeting.dayLabel + ")"),
                                metaItem("\uD83D\uDD52", "Time:", meeting.timeRange),
                                metaItem("\uD83D\uDCCD", "Venue:", meeting.venue));
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        private HBox metaItem(String icon, String label, String value) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + ACCENT_GREEN + ";");
                Label labelLabel = new Label(label);
                labelLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label valueLabel = new Label(value);
                valueLabel.setWrapText(true);
                valueLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                HBox row = new HBox(6, iconLabel, labelLabel, valueLabel);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- About the Meeting ----
        private VBox buildAboutCard(MeetingData meeting) {
                Label header = new Label("About the Meeting");
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 16px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                Label aboutText = new Label(meeting.aboutText);
                aboutText.setWrapText(true);
                aboutText.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: "
                                                + TEXT_PRIMARY + "; -fx-line-spacing: 3;");

                VBox card = new VBox(12, header, aboutText);
                card.setPadding(new Insets(24));
                card.setStyle(cardStyle(14));
                return card;
        }

        // ---- Key Resolutions / Decisions ----
        private VBox buildResolutionsCard(MeetingData meeting) {
                Label header = new Label("Key Resolutions / Decisions");
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 16px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                VBox list = new VBox(0);
                List<String> resolutions = meeting.resolutions;
                for (int i = 0; i < resolutions.size(); i++) {
                        boolean isLast = (i == resolutions.size() - 1);
                        list.getChildren().add(resolutionRow(i + 1, resolutions.get(i), isLast));
                }

                VBox card = new VBox(16, header, list);
                card.setPadding(new Insets(24));
                card.setStyle(cardStyle(14));
                return card;
        }

        private VBox resolutionRow(int number, String text, boolean isLast) {
                Label numberLabel = new Label(number + ".");
                numberLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                numberLabel.setMinWidth(22);

                Label textLabel = new Label(text);
                textLabel.setWrapText(true);
                textLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                HBox.setHgrow(textLabel, Priority.ALWAYS);

                HBox row = new HBox(10, numberLabel, textLabel);
                row.setAlignment(Pos.TOP_LEFT);
                row.setPadding(new Insets(12, 0, 12, 0));

                VBox wrapper = new VBox(row);
                if (!isLast) {
                        Region divider = new Region();
                        divider.setPrefHeight(1);
                        divider.setStyle("-fx-background-color: " + BORDER + ";");
                        wrapper.getChildren().add(divider);
                }
                return wrapper;
        }

        // ---- Quick Summary: two stats side by side with a vertical divider ----
        private VBox buildQuickSummaryCard(MeetingData meeting) {
                Label header = new Label("Quick Summary");
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 16px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                String attendancePercentText = String.format("%.1f", meeting.attendancePercent) + "%";

                VBox votersStat = summaryStat("\uD83D\uDC65", "Total Registered Voters",
                                String.valueOf(meeting.totalRegisteredVoters));
                VBox attendanceStat = summaryStat("\uD83D\uDC65", "Total Attendance",
                                meeting.totalAttendance + " (" + attendancePercentText + ")");

                Region verticalDivider = new Region();
                verticalDivider.setPrefWidth(1);
                verticalDivider.setMaxHeight(Double.MAX_VALUE);
                verticalDivider.setStyle("-fx-background-color: " + BORDER + ";");

                HBox statsRow = new HBox(48, votersStat, verticalDivider, attendanceStat);
                statsRow.setAlignment(Pos.CENTER);

                VBox card = new VBox(20, header, statsRow);
                card.setPadding(new Insets(24));
                card.setStyle(cardStyle(14));
                return card;
        }

        private VBox summaryStat(String icon, String label, String value) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + ACCENT_GREEN + ";");
                Label labelLabel = new Label(label);
                labelLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");
                HBox topRow = new HBox(6, iconLabel, labelLabel);
                topRow.setAlignment(Pos.CENTER_LEFT);

                Label valueLabel = new Label(value);
                valueLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                VBox box = new VBox(8, topRow, valueLabel);
                box.setAlignment(Pos.CENTER_LEFT);
                return box;
        }

        // =================================================================
        // HELPERS
        // =================================================================
        private String cardStyle(int radius) {
                return "-fx-background-color: white;"
                                + "-fx-background-radius: " + radius + ";"
                                + "-fx-border-color: " + BORDER + ";"
                                + "-fx-border-radius: " + radius + ";"
                                + "-fx-border-width: 1;"
                                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.06), 16, 0.1, 0, 4);";
        }

        private String rgba(String hex, double alpha) {
                int r = Integer.parseInt(hex.substring(1, 3), 16);
                int g = Integer.parseInt(hex.substring(3, 5), 16);
                int b = Integer.parseInt(hex.substring(5, 7), 16);
                return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
        }
}