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
 * Sabha" item, the breadcrumb link, and "Back to Meetings" - one
 * callback, no competing navigation paths.
 *
 * Font sizes and colors follow the same readability rules used across
 * the app: dark forest-green text on light backgrounds for anything
 * the villager needs to read closely (body text is never below 12px),
 * bold high-contrast numerals for the Quick Summary stats, and status
 * pills use a saturated text color over a soft tint background rather
 * than low-contrast pastel-on-pastel.
 */
public class MeetingDetailPage {

        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String CONTEXT_TEAL = "#0E8C8C";
        private static final String AI_VIOLET = "#7C5CFC";
        private static final String DELAYED_RED = "#D94C38";
        private static final String ACCENT_GREEN = "#2E9E5B";
        private static final String MALE_BLUE = "#3A8CD6";

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
                                buildTitleRow(meeting, backToGramSabha),
                                buildMetaRow(meeting),
                                buildThreeCardRow(meeting),
                                buildQuickSummaryCard(meeting),
                                buildSignedNote(meeting));

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

        // ---- Title row: title + status pill on the left, action buttons on the right
        // ----
        private HBox buildTitleRow(MeetingData meeting, Runnable backToGramSabha) {
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
                                                + "-fx-text-fill: " + TEXT_PRIMARY + ";"
                                                + "-fx-font-size: 12px; -fx-font-weight: 700;"
                                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 8;"
                                                + "-fx-background-radius: 8; -fx-padding: 9 16 9 16; -fx-cursor: hand;");
                // TODO: wire to MinutesService.downloadMinutes(meeting) once a real
                // backend/document store exists.

                Button backBtn = new Button("\u2190  Back to Meetings");
                backBtn.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: white;"
                                                + "-fx-text-fill: " + TEXT_PRIMARY + ";"
                                                + "-fx-font-size: 12px; -fx-font-weight: 700;"
                                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 8;"
                                                + "-fx-background-radius: 8; -fx-padding: 9 16 9 16; -fx-cursor: hand;");
                backBtn.setOnAction(e -> backToGramSabha.run());

                HBox actions = new HBox(10, downloadBtn, backBtn);
                actions.setAlignment(Pos.CENTER_RIGHT);

                HBox row = new HBox(titleGroup, spacer, actions);
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
                iconLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                Label labelLabel = new Label(label);
                labelLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label valueLabel = new Label(value);
                valueLabel.setWrapText(true);
                valueLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");
                HBox row = new HBox(6, iconLabel, labelLabel, valueLabel);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- About / Agenda / Highlights, three cards side by side ----
        private HBox buildThreeCardRow(MeetingData meeting) {

                VBox aboutCard = buildAboutCard(meeting);
                VBox agendaCard = buildAgendaCard(meeting);
                VBox highlightsCard = buildHighlightsCard(meeting);

                // About section should occupy most of the available width
                aboutCard.setPrefWidth(620);
                aboutCard.setMinWidth(560);
                aboutCard.setMaxWidth(Double.MAX_VALUE);

                // Agenda and Highlights should remain compact but readable
                agendaCard.setPrefWidth(300);
                agendaCard.setMinWidth(270);
                agendaCard.setMaxWidth(340);

                highlightsCard.setPrefWidth(300);
                highlightsCard.setMinWidth(270);
                highlightsCard.setMaxWidth(340);

                HBox row = new HBox(
                                18,
                                aboutCard,
                                agendaCard,
                                highlightsCard);

                row.setAlignment(Pos.TOP_LEFT);

                // Allow About card to consume remaining width
                HBox.setHgrow(aboutCard, Priority.ALWAYS);

                return row;
        }

        private VBox buildAboutCard(MeetingData meeting) {
                Label header = new Label("About the Meeting");
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 15px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                Label aboutText = new Label(meeting.aboutText);
                aboutText.setWrapText(true);
                aboutText.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_SECONDARY + "; -fx-line-spacing: 2;");

                Region divider = new Region();
                divider.setPrefHeight(1);
                divider.setStyle("-fx-background-color: " + BORDER + ";");

                VBox infoList = new VBox(10,
                                infoRow("\uD83D\uDC64", "Presided By", meeting.presidedBy),
                                infoRow("\uD83C\uDFDB", "Organized By", meeting.organizedBy),
                                infoRow("\uD83D\uDC65", "Meeting Type", meeting.meetingType));

                Region divider2 = new Region();
                divider2.setPrefHeight(1);
                divider2.setStyle("-fx-background-color: " + BORDER + ";");

                Label quorumIcon = new Label("\u2705");
                quorumIcon.setStyle("-fx-font-size: 12px;");
                Label quorumLabel = new Label("Quorum Status");
                quorumLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label quorumValue = new Label(meeting.quorumStatus);
                quorumValue.setWrapText(true);
                quorumValue.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");
                VBox quorumTextBox = new VBox(2, quorumLabel, quorumValue);
                HBox quorumRow = new HBox(8, quorumIcon, quorumTextBox);
                quorumRow.setAlignment(Pos.CENTER_LEFT);

                VBox card = new VBox(14, header, aboutText, divider, infoList, divider2, quorumRow);
                card.setPadding(new Insets(20));
                card.setStyle(cardStyle(14));
                return card;
        }

        private HBox infoRow(String icon, String label, String value) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                Label labelLabel = new Label(label);
                labelLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label valueLabel = new Label(value);
                valueLabel.setWrapText(true);
                valueLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");
                VBox textBox = new VBox(2, labelLabel, valueLabel);
                HBox row = new HBox(8, iconLabel, textBox);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        private VBox buildAgendaCard(MeetingData meeting) {
                Label header = new Label("Agenda of the Meeting");
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 15px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                VBox list = new VBox(12);
                int number = 1;
                for (String item : meeting.agenda) {
                        list.getChildren().add(numberedAgendaItem(number, item));
                        number++;
                }

                VBox card = new VBox(14, header, list);
                card.setPadding(new Insets(20));
                card.setStyle(cardStyle(14));
                return card;
        }

        private HBox numberedAgendaItem(int number, String text) {
                Label circle = new Label(String.valueOf(number));
                circle.setMinSize(22, 22);
                circle.setMaxSize(22, 22);
                circle.setAlignment(Pos.CENTER);
                circle.setStyle(
                                "-fx-background-color: " + rgba(CONTEXT_TEAL, 0.16) + ";"
                                                + "-fx-text-fill: " + CONTEXT_TEAL + ";"
                                                + "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-font-size: 11px; -fx-font-weight: 800;"
                                                + "-fx-background-radius: 999;");

                Label textLabel = new Label(text);
                textLabel.setWrapText(true);
                textLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                HBox row = new HBox(10, circle, textLabel);
                row.setAlignment(Pos.TOP_LEFT);
                return row;
        }

        private VBox buildHighlightsCard(MeetingData meeting) {
                Label header = new Label("Meeting Highlights");
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 15px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                VBox list = new VBox(12);
                for (String item : meeting.highlights) {
                        list.getChildren().add(checklistItem(item));
                }

                VBox card = new VBox(14, header, list);
                card.setPadding(new Insets(20));
                card.setStyle(cardStyle(14));
                return card;
        }

        private HBox checklistItem(String text) {
                Label check = new Label("\u2705");
                check.setStyle("-fx-font-size: 12px;");
                Label textLabel = new Label(text);
                textLabel.setWrapText(true);
                textLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                HBox row = new HBox(8, check, textLabel);
                row.setAlignment(Pos.TOP_LEFT);
                return row;
        }

        // ---- Quick Summary ----
        private VBox buildQuickSummaryCard(MeetingData meeting) {
                Label header = new Label("Quick Summary");
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 15px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                String attendancePercentText = String.format("%.1f", meeting.attendancePercent) + "%";

                HBox statsRow = new HBox(32,
                                summaryStat("\uD83D\uDC65", FOREST_DEEP, "Total Registered Voters",
                                                String.valueOf(meeting.totalRegisteredVoters)),
                                summaryStat("\uD83D\uDC64", CONTEXT_TEAL, "Total Attendance",
                                                meeting.totalAttendance + " (" + attendancePercentText + ")"),
                                summaryStat("\uD83D\uDEB9", MALE_BLUE, "Male", String.valueOf(meeting.male)),
                                summaryStat("\u26A4", AI_VIOLET, "Female", String.valueOf(meeting.female)),
                                summaryStat("\uD83D\uDD52", TEXT_PRIMARY, "Meeting Duration", meeting.duration));
                for (var node : statsRow.getChildren()) {
                        HBox.setHgrow(node, Priority.ALWAYS);
                }

                VBox card = new VBox(16, header, statsRow);
                card.setPadding(new Insets(22));
                card.setStyle(cardStyle(14));
                return card;
        }

        private VBox summaryStat(String icon, String iconColor, String label, String value) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + iconColor + ";");
                Label labelLabel = new Label(label);
                labelLabel.setWrapText(true);
                labelLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");
                HBox topRow = new HBox(6, iconLabel, labelLabel);
                topRow.setAlignment(Pos.CENTER_LEFT);

                Label valueLabel = new Label(value);
                valueLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                return new VBox(6, topRow, valueLabel);
        }

        // ---- Digitally signed note ----
        private HBox buildSignedNote(MeetingData meeting) {
                Label infoIcon = new Label("\u2139");
                infoIcon.setStyle("-fx-font-size: 13px; -fx-text-fill: " + CONTEXT_TEAL + ";");
                Label infoText = new Label(
                                "These minutes are approved and digitally signed by the authorized officials.");
                infoText.setWrapText(true);
                infoText.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                HBox leftGroup = new HBox(8, infoIcon, infoText);
                leftGroup.setAlignment(Pos.CENTER_LEFT);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label signedBadge = new Label("\u2705  Digitally Signed");
                signedBadge.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: "
                                                + ACCENT_GREEN + ";");

                Label signedOnLabel = new Label("Signed on:");
                signedOnLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label signedOnValue = new Label(meeting.signedOn);
                signedOnValue.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                HBox rightGroup = new HBox(16, signedBadge, new HBox(4, signedOnLabel, signedOnValue));
                rightGroup.setAlignment(Pos.CENTER_RIGHT);
                ((HBox) rightGroup.getChildren().get(1)).setAlignment(Pos.CENTER_LEFT);

                HBox row = new HBox(leftGroup, spacer, rightGroup);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(14, 18, 14, 18));
                row.setStyle(
                                "-fx-background-color: " + rgba(CONTEXT_TEAL, 0.07) + ";"
                                                + "-fx-background-radius: 12;");
                return row;
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