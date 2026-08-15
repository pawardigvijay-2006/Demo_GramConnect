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

import com.tech_fusion.view.villager.GovernmentSchemes.SchemeData;

/**
 * GramConnect - Scheme Detail (AI-generated info)
 *
 * Same non-Application pattern as ComplaintsPage / NewComplaintPage /
 * GovernmentSchemes: a plain class with one public method that returns
 * a Scene to swap onto the shared VillagerDashboard.homeStage.
 *
 * Usage (from GovernmentSchemes' "See Details" button):
 * VillagerDashboard.homeStage.setScene(
 * new SchemeDetailPage().getSchemeDetailScene(scheme, backAction, backToSchemes));
 *
 * - backAction -> the same "go to Dashboard" callback threaded through
 * every other page's sidebar, unchanged.
 * - backToSchemes -> re-opens GovernmentSchemes. Used by both the sidebar's
 * "Government schemes" item (kept highlighted/active here, same as the
 * reference design) and the "Government Schemes" breadcrumb link, so
 * there is exactly one way back to the list and no competing callback.
 */
public class SchemeDetailPage {

        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String FOREST_LIGHT = "#0F4736";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String CONTEXT_TEAL = "#0E8C8C";
        private static final String DELAYED_RED = "#D94C38";
        private static final String ACCENT_GREEN = "#2E9E5B";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        private static final String BACKGROUND = "#EFF5F1";

        private static final String TEXT_PRIMARY = FOREST_DEEP;
        private static final String TEXT_SECONDARY = "rgba(11,61,46,0.65)";

        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        public Scene getSchemeDetailScene(SchemeData scheme, Runnable backAction, Runnable backToSchemes) {

                BorderPane pane = new BorderPane();
                pane.setTop(buildHeader());
                pane.setCenter(buildScrollableContent(scheme, backToSchemes));

                BorderPane root = new BorderPane();
                root.setLeft(buildSidebar(backAction, backToSchemes));
                root.setCenter(pane);

                return new Scene(root, 1500, 850);
        }

        // =================================================================
        // SIDEBAR - identical to GovernmentSchemes' sidebar. "Government
        // schemes" stays active/highlighted since this page is reached from
        // (and returns to) the schemes list, matching the reference design.
        // =================================================================
        private VBox buildSidebar(Runnable backToDashboardAction, Runnable backToSchemes) {
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

                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", true);
                schemesNav.setOnMouseClicked(e -> backToSchemes.run());

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

                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", false);
                gramSabhaNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage.setScene(
                                                new GramSabha().getGramSabhaScene(backToDashboardAction)));

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
        // HEADER - identical style to GovernmentSchemes' header.
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

        // =================================================================
        // SCROLLABLE CONTENT
        // =================================================================
        private ScrollPane buildScrollableContent(SchemeData scheme, Runnable backToSchemes) {
                VBox content = new VBox(20);
                content.setPadding(new Insets(24, 32, 32, 32));
                content.setStyle("-fx-background-color: " + BACKGROUND + ";");

                content.getChildren().addAll(
                                buildBreadcrumb(scheme, backToSchemes),
                                buildTitleBlock(scheme),
                                buildAboutCard(scheme),
                                buildEligibilityAndDocsRow(scheme),
                                buildHowToApplyCard(),
                                buildApplyButton());

                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
                return scrollPane;
        }

        // ---- Breadcrumb: Services > Government Schemes > <Scheme Title> ----
        private HBox buildBreadcrumb(SchemeData scheme, Runnable backToSchemes) {
                Label services = new Label("Services");
                services.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + CONTEXT_TEAL + ";");

                Label separator1 = new Label("\u203A");
                separator1.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                Label schemesLink = new Label("Government Schemes");
                schemesLink.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + CONTEXT_TEAL + "; -fx-cursor: hand;");
                schemesLink.setOnMouseClicked(e -> backToSchemes.run());

                Label separator2 = new Label("\u203A");
                separator2.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                Label current = new Label(scheme.title);
                current.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                HBox crumb = new HBox(6, services, separator1, schemesLink, separator2, current);
                crumb.setAlignment(Pos.CENTER_LEFT);
                return crumb;
        }

        // ---- Title + category badge + short description ----
        private VBox buildTitleBlock(SchemeData scheme) {
                Label title = new Label(scheme.title);
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 26px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                Label badge = new Label(scheme.badge);
                badge.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: rgba(46,158,91,0.14);"
                                                + "-fx-text-fill: " + ACCENT_GREEN + ";"
                                                + "-fx-font-size: 11px; -fx-font-weight: 800;"
                                                + "-fx-background-radius: 999;"
                                                + "-fx-padding: 4 12 4 12;");

                HBox titleRow = new HBox(12, title, badge);
                titleRow.setAlignment(Pos.CENTER_LEFT);

                Label subtitle = new Label(scheme.shortDescription);
                subtitle.setWrapText(true);
                subtitle.setMaxWidth(900);
                subtitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                return new VBox(6, titleRow, subtitle);
        }

        // ---- "About this Scheme" card: AI badge, about text + key benefits on the left, illustration on the right ----
        private VBox buildAboutCard(SchemeData scheme) {

                Label aboutTitle = new Label("About this Scheme");
                aboutTitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 15px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                Label aiBadge = new Label("\u2728  AI-Powered Information");
                aiBadge.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: rgba(224,122,31,0.14);"
                                                + "-fx-text-fill: " + SAFFRON_MAIN + ";"
                                                + "-fx-font-size: 10px; -fx-font-weight: 800;"
                                                + "-fx-background-radius: 999;"
                                                + "-fx-padding: 4 10 4 10;");

                Region titleSpacer = new Region();
                HBox.setHgrow(titleSpacer, Priority.ALWAYS);
                HBox aboutHeaderRow = new HBox(10, aboutTitle, titleSpacer, aiBadge);
                aboutHeaderRow.setAlignment(Pos.CENTER_LEFT);

                Label aboutText = new Label(scheme.aboutText);
                aboutText.setWrapText(true);
                aboutText.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                Label benefitsTitle = new Label("Key Benefits");
                benefitsTitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                VBox benefitsList = new VBox(6);
                for (String benefit : scheme.benefits) {
                        benefitsList.getChildren().add(checklistItem(benefit));
                }

                VBox leftColumn = new VBox(14, aboutHeaderRow, aboutText, benefitsTitle, benefitsList);
                HBox.setHgrow(leftColumn, Priority.ALWAYS);

                // Illustration placeholder - a soft gradient panel with the
                // scheme's icon, standing in for a real illustration asset.
                Label illustrationIcon = new Label(scheme.icon);
                illustrationIcon.setStyle("-fx-font-size: 56px;");
                StackPane illustration = new StackPane(illustrationIcon);
                illustration.setPrefSize(220, 220);
                illustration.setMinSize(220, 220);
                illustration.setMaxSize(220, 220);
                illustration.setStyle(
                                "-fx-background-color: linear-gradient(to bottom right, rgba(46,158,91,0.16), rgba(11,61,46,0.10));"
                                                + "-fx-background-radius: 16;"
                                                + "-fx-border-color: rgba(11,61,46,0.10);"
                                                + "-fx-border-radius: 16;"
                                                + "-fx-border-width: 1;");

                HBox row = new HBox(24, leftColumn, illustration);
                row.setAlignment(Pos.TOP_LEFT);
                row.setPadding(new Insets(22));
                row.setStyle(cardStyle(14));
                return new VBox(row);
        }

        private HBox checklistItem(String text) {
                Label check = new Label("\u2705");
                check.setStyle("-fx-font-size: 11px;");
                Label label = new Label(text);
                label.setWrapText(true);
                label.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                HBox row = new HBox(8, check, label);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- Eligibility + Documents Required, side by side ----
        private HBox buildEligibilityAndDocsRow(SchemeData scheme) {

                VBox eligibilityCard = infoListCard("\uD83D\uDC64  Eligibility", scheme.eligibility, false);
                VBox documentsCard = infoListCard("\uD83D\uDCC4  Documents Required", scheme.documents, true);

                HBox.setHgrow(eligibilityCard, Priority.ALWAYS);
                HBox.setHgrow(documentsCard, Priority.ALWAYS);

                HBox row = new HBox(16, eligibilityCard, documentsCard);
                return row;
        }

        private VBox infoListCard(String headerText, List<String> items, boolean numbered) {
                Label header = new Label(headerText);
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                VBox list = new VBox(8);
                for (String item : items) {
                        Label bullet = new Label(numbered ? "\u2022" : "\u2022");
                        bullet.setStyle("-fx-text-fill: " + ACCENT_GREEN + "; -fx-font-size: 12px; -fx-font-weight: 900;");
                        Label text = new Label(item);
                        text.setWrapText(true);
                        text.setStyle(
                                        "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                        + TEXT_SECONDARY + ";");
                        HBox itemRow = new HBox(8, bullet, text);
                        itemRow.setAlignment(Pos.TOP_LEFT);
                        list.getChildren().add(itemRow);
                }

                VBox card = new VBox(12, header, list);
                card.setPadding(new Insets(20));
                card.setStyle(cardStyle(14));
                return card;
        }

        // ---- How to Apply: 4 numbered steps ----
        private VBox buildHowToApplyCard() {
                Label header = new Label("\uD83D\uDCCB  How to Apply");
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                HBox stepsRow = new HBox(16,
                                stepBlock(1, "Click on 'Apply on\nOfficial Portal' button."),
                                stepBlock(2, "You will be redirected\nto official scheme portal."),
                                stepBlock(3, "Fill the application form\non the portal."),
                                stepBlock(4, "Submit required documents\nand complete registration."));

                for (int i = 0; i < stepsRow.getChildren().size(); i++) {
                        HBox.setHgrow(stepsRow.getChildren().get(i), Priority.ALWAYS);
                }

                VBox card = new VBox(14, header, stepsRow);
                card.setPadding(new Insets(20));
                card.setStyle(cardStyle(14));
                return card;
        }

        private VBox stepBlock(int number, String text) {
                Label circle = new Label(String.valueOf(number));
                circle.setMinSize(24, 24);
                circle.setMaxSize(24, 24);
                circle.setAlignment(Pos.CENTER);
                circle.setStyle(
                                "-fx-background-color: " + ACCENT_GREEN + ";"
                                                + "-fx-text-fill: white;"
                                                + "-fx-font-size: 11px; -fx-font-weight: 800;"
                                                + "-fx-background-radius: 999;");

                Label label = new Label(text);
                label.setWrapText(true);
                label.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                VBox block = new VBox(8, circle, label);
                return block;
        }

        // ---- Apply on Official Portal CTA ----
        private VBox buildApplyButton() {
                Button applyBtn = new Button("\u2197  Apply on Official Portal");
                applyBtn.setMaxWidth(Double.MAX_VALUE);
                applyBtn.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT
                                                + ", " + FOREST_DEEP + ");"
                                                + "-fx-text-fill: white;"
                                                + "-fx-font-size: 13px; -fx-font-weight: 800;"
                                                + "-fx-background-radius: 10; -fx-padding: 14 0 14 0;"
                                                + "-fx-cursor: hand;"
                                                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.30), 10, 0.1, 0, 3);");
                // TODO: wire this up to HostServices.showDocument(scheme.officialPortalUrl)
                // once each SchemeData carries a real portal URL.

                Label lockNote = new Label("\uD83D\uDD12  You will be redirected to the official government portal");
                lockNote.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                VBox box = new VBox(8, applyBtn, lockNote);
                box.setAlignment(Pos.CENTER);
                return box;
        }

        // =================================================================
        // HELPERS (same glass-card look as GovernmentSchemes.java)
        // =================================================================
        private String cardStyle(int radius) {
                return "-fx-background-color: rgba(255,255,255,0.92);"
                                + "-fx-background-radius: " + radius + ";"
                                + "-fx-border-color: rgba(255,255,255,0.5);"
                                + "-fx-border-radius: " + radius + ";"
                                + "-fx-border-width: 1;"
                                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.06), 16, 0.1, 0, 4);";
        }
}