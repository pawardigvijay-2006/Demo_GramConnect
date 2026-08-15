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
 * GramConnect - Certificates
 *
 * ============================================================
 * THEME NOTE
 * ============================================================
 * Reuses the EXACT same visual language as VillagerDashboard.java
 * and GovernmentSchemes.java: same forest-green + saffron color
 * constants, the same translucent "glass card" look with a soft
 * drop-shadow, the same hover-lift effect, and the same header style
 * (rounded pill search box, bell chip with a badge, avatar with a
 * saffron ring). cardStyle()/addHoverLift()/rgba() are copied
 * verbatim so this file stays self-contained.
 *
 * NEW: each "Apply Now" button now navigates to CertificateApply.java,
 * the same way every other page-to-page jump in this app works - a
 * Runnable is built on the spot that knows how to rebuild THIS exact
 * scene (getCertificatesScene(backToDashboardAction) again), and is
 * handed to CertificateApply as "backToCertificatesAction" so its
 * breadcrumb/back button/post-submit flow always lands back on the
 * Certificates page, not some generic destination.
 *
 * Per your earlier request, this page only offers the 4 certificate
 * types you asked for - Birth, Death, Marriage, and Residential -
 * instead of all 8 shown in the original reference screenshot.
 *
 * ARCHITECTURE NOTE: The application table below is hardcoded mock
 * data, marked with a TODO showing where a real CertificateService
 * would plug in later.
 * ============================================================
 */
public class Certificates {

        // ================= COLORS (copied from VillagerDashboard.java) =================
        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String FOREST_LIGHT = "#0F4736";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String CONTEXT_TEAL = "#0E8C8C";
        private static final String AI_VIOLET = "#7C5CFC";
        private static final String ROSE_PINK = "#D6336C";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        private static final String BACKGROUND = "#EFF5F1";

        private static final String TEXT_PRIMARY = FOREST_DEEP;
        private static final String TEXT_SECONDARY = "rgba(11,61,46,0.65)";

        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";
        private static final String DELAYED_RED = "#D94C38";

        /**
         * Public entry point used by VillagerDashboard's sidebar navigation:
         * root.setCenter(new Certificates().getCertificatesScene(backToDashboardAction));
         * Returns the full page (sidebar + header + scrollable content).
         */
        public Scene getCertificatesScene(Runnable backToDashboardAction) {

                BorderPane pane = new BorderPane();
                pane.setTop(buildHeader());
                pane.setCenter(buildScrollableContent(backToDashboardAction));

                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BACKGROUND + ";");

                root.setLeft(buildSidebar(backToDashboardAction));
                root.setCenter(pane);

                return new Scene(root, 1500, 850);
        }

        // =================================================================
        // SIDEBAR
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

                Label dashboardNav = navItem("\uD83C\uDFE0  Dashboard", false);
                dashboardNav.setOnMouseClicked(e -> {
                        backToDashboardAction.run();
                });

                Label projectsNav = navItem("\uD83C\uDFD7  Project transparency", false);
                projectsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new ProjectTransparency().getProjectScene(backToDashboardAction));
                });

                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", false);
                complaintsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new ComplaintsPage().getComplaintsPage(backToDashboardAction));
                });
                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", false);
                schemesNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new GovernmentSchemes().getSchemesScene(backToDashboardAction));
                });
                Label certificatesNav = navItem("\uD83D\uDCDC  Certificates", true);

                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", false);
                billsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new BillsAndPayments().getBillsScene(backToDashboardAction));
                });
                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                announcementsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new Announcements().getAnnouncementScene(backToDashboardAction));
                });
                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", false);
                gramSabhaNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new GramSabha().getGramSabhaScene(backToDashboardAction));
                });
                Label aiAssistantNav = navItem("\uD83E\uDD16  AI village assistant", false);
                aiAssistantNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new AIAssistant().getAiAssiatantScene(backToDashboardAction));
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
        // SCROLLABLE CONTENT
        // =================================================================
        private ScrollPane buildScrollableContent(Runnable backToDashboardAction) {
                VBox content = new VBox(20);
                content.setPadding(new Insets(24, 32, 32, 32));
                content.setStyle("-fx-background-color: " + BACKGROUND + ";");

                content.getChildren().addAll(
                                buildTitleRow(),
                                buildSectionLabel("Apply for Certificate"),
                                buildCertificateGrid(backToDashboardAction),
                                buildMyApplicationsCard());

                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
                return scrollPane;
        }

        private HBox buildTitleRow() {
                Label title = new Label("Certificates");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 28px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label subtitle = new Label("Apply for official certificates online and track your application status.");
                subtitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");
                VBox textBox = new VBox(6, title, subtitle);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button howItWorksBtn = new Button("\u2753  How it works?");
                howItWorksBtn.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: white;"
                                                + "-fx-text-fill: " + FOREST_DEEP + ";"
                                                + "-fx-font-size: 12px; -fx-font-weight: 700;"
                                                + "-fx-border-color: rgba(11,61,46,0.15); -fx-border-radius: 8;"
                                                + "-fx-background-radius: 8; -fx-padding: 9 16 9 16; -fx-cursor: hand;");

                HBox row = new HBox(textBox, spacer, howItWorksBtn);
                row.setAlignment(Pos.TOP_LEFT);
                return row;
        }

        private Label buildSectionLabel(String text) {
                Label label = new Label(text);
                label.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 16px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                return label;
        }

        // ---- Certificate grid: only Residential, Birth, Death, Marriage ----
        private HBox buildCertificateGrid(Runnable backToDashboardAction) {
                // Rebuilds THIS exact Certificates scene - handed to CertificateApply
                // as "backToCertificatesAction" so its back/breadcrumb/post-submit
                // navigation always returns here, not to the dashboard.
                Runnable backToCertificatesAction = () -> VillagerDashboard.homeStage
                                .setScene(getCertificatesScene(backToDashboardAction));

                // TODO: replace with CertificateService.getAvailableCertificateTypes()
                HBox row = new HBox(16,
                                certificateCard("\uD83C\uDFE0", FOREST_DEEP, "Residential Certificate",
                                                "Proof of residence issued for your village address.",
                                                backToCertificatesAction),
                                certificateCard("\uD83D\uDC76", ROSE_PINK, "Birth Certificate",
                                                "Official certificate of birth issued by Gram Panchayat.",
                                                backToCertificatesAction),
                                certificateCard("\uD83D\uDD6F", SAFFRON_MAIN, "Death Certificate",
                                                "Certificate issued for the registered death.",
                                                backToCertificatesAction),
                                certificateCard("\uD83D\uDC8D", AI_VIOLET, "Marriage Certificate",
                                                "Official proof of marriage registered with the Gram Panchayat.",
                                                backToCertificatesAction));
                return row;
        }

        /**
         * One certificate card: colored icon chip, title, description, and an
         * "Apply Now" button that now navigates to CertificateApply.
         */
        private VBox certificateCard(String icon, String accent, String title, String description,
                        Runnable backToCertificatesAction) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 20px;");
                StackPane iconChip = new StackPane(iconLabel);
                iconChip.setPrefSize(52, 52);
                iconChip.setMaxSize(52, 52);
                iconChip.setStyle("-fx-background-color: " + accent + "; -fx-background-radius: 14;");

                Label titleLabel = new Label(title);
                titleLabel.setWrapText(true);
                titleLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 14px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                titleLabel.setAlignment(Pos.CENTER);
                titleLabel.setMaxWidth(Double.MAX_VALUE);

                Label descLabel = new Label(description);
                descLabel.setWrapText(true);
                descLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";"
                                                + "-fx-text-alignment: center;");
                descLabel.setAlignment(Pos.CENTER);
                descLabel.setMaxWidth(Double.MAX_VALUE);

                Button applyBtn = new Button("Apply Now");
                applyBtn.setMaxWidth(Double.MAX_VALUE);
                applyBtn.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: white;"
                                                + "-fx-text-fill: " + FOREST_DEEP + ";"
                                                + "-fx-font-size: 11px; -fx-font-weight: 800;"
                                                + "-fx-border-color: " + FOREST_DEEP + "; -fx-border-radius: 8;"
                                                + "-fx-background-radius: 8; -fx-padding: 8; -fx-cursor: hand;");
                applyBtn.setOnAction(e -> {
                        CertificateApply certificateApply = new CertificateApply();
                        VillagerDashboard.homeStage.setScene(
                                        certificateApply.getApplyScene(backToCertificatesAction, title, description));
                });

                VBox card = new VBox(10, iconChip, titleLabel, descLabel, applyBtn);
                card.setAlignment(Pos.TOP_CENTER);
                card.setPadding(new Insets(20));
                card.setPrefWidth(220);
                card.setStyle(cardStyle(16));
                HBox.setHgrow(card, Priority.ALWAYS);
                addHoverLift(card, 16);
                return card;
        }

        // ---- My Applications table ----
        private VBox buildMyApplicationsCard() {
                Label title = buildSectionLabel("My Applications");

                Label viewAll = new Label("View All \u2192");
                viewAll.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: "
                                                + FOREST_DEEP + "; -fx-cursor: hand;");

                Region headerSpacer = new Region();
                HBox.setHgrow(headerSpacer, Priority.ALWAYS);
                HBox headerRow = new HBox(title, headerSpacer, viewAll);
                headerRow.setAlignment(Pos.CENTER_LEFT);

                HBox columnHeader = new HBox(
                                tableCell("Certificate Type", 220, true),
                                tableCell("Application ID", 160, true),
                                tableCell("Date Applied", 140, true),
                                tableCell("Status", 120, true),
                                tableCell("Action", 120, true));
                columnHeader.setPadding(new Insets(0, 0, 10, 0));
                columnHeader.setStyle(
                                "-fx-border-color: transparent transparent rgba(11,61,46,0.12) transparent; -fx-border-width: 0 0 1 0;");

                // TODO: replace with CertificateService.getApplicationsForUser(userId)
                VBox rows = new VBox(0,
                                applicationRow("\uD83C\uDFE0", FOREST_DEEP, "Residential Certificate", "GC/2024/000123",
                                                "10 May 2024", "Pending", "View Status"),
                                applicationRow("\uD83D\uDC76", ROSE_PINK, "Birth Certificate", "GC/2024/000098",
                                                "02 May 2024", "Approved", "Download"),
                                applicationRow("\uD83D\uDC8D", AI_VIOLET, "Marriage Certificate", "GC/2024/000076",
                                                "25 Apr 2024", "Pending", "View Status"));

                VBox card = new VBox(14, headerRow, columnHeader, rows);
                card.setPadding(new Insets(20));
                card.setStyle(cardStyle(16));
                return card;
        }

        private Label tableCell(String text, double width, boolean isHeader) {
                Label cell = new Label(text);
                cell.setPrefWidth(width);
                if (isHeader) {
                        cell.setStyle(
                                        "-fx-font-family: " + FONT_FAMILY
                                                        + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: "
                                                        + TEXT_SECONDARY + ";");
                } else {
                        cell.setStyle(
                                        "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                        + TEXT_PRIMARY + ";");
                }
                return cell;
        }

        private HBox applicationRow(String icon, String accent, String certType, String appId,
                        String dateApplied, String status, String actionText) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 12px;");
                StackPane iconChip = new StackPane(iconLabel);
                iconChip.setPrefSize(24, 24);
                iconChip.setMaxSize(24, 24);
                iconChip.setStyle("-fx-background-color: " + accent + "; -fx-background-radius: 7;");

                Label certLabel = new Label(certType);
                certLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                HBox certCell = new HBox(8, iconChip, certLabel);
                certCell.setAlignment(Pos.CENTER_LEFT);
                certCell.setPrefWidth(220);

                boolean approved = status.equals("Approved");
                Label statusPill = new Label(status);
                statusPill.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: "
                                                + (approved ? rgba(FOREST_DEEP, 0.12) : rgba(SAFFRON_MAIN, 0.15)) + ";"
                                                + "-fx-text-fill: " + (approved ? FOREST_DEEP : SAFFRON_MAIN) + ";"
                                                + "-fx-background-radius: 999; -fx-padding: 3 12 3 12;"
                                                + "-fx-font-size: 10px; -fx-font-weight: 700;");
                HBox statusCell = new HBox(statusPill);
                statusCell.setPrefWidth(120);
                statusCell.setAlignment(Pos.CENTER_LEFT);

                Label actionLabel = new Label(actionText + (actionText.equals("Download") ? "  \u2B07" : ""));
                actionLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: "
                                                + FOREST_DEEP + "; -fx-cursor: hand;");
                HBox actionCell = new HBox(actionLabel);
                actionCell.setPrefWidth(120);
                actionCell.setAlignment(Pos.CENTER_LEFT);

                HBox row = new HBox(
                                certCell,
                                tableCell(appId, 160, false),
                                tableCell(dateApplied, 140, false),
                                statusCell,
                                actionCell);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(12, 0, 12, 0));
                row.setStyle("-fx-border-color: transparent transparent rgba(11,61,46,0.06) transparent; -fx-border-width: 0 0 1 0;");
                return row;
        }

        // =================================================================
        // HELPERS (copied verbatim from VillagerDashboard.java so this file
        // stays self-contained and every card matches the same glass-panel
        // look and hover behavior)
        // =================================================================

        private String cardStyle(int radius) {
                return "-fx-background-color: rgba(255,255,255,0.88);"
                                + "-fx-background-radius: " + radius + ";"
                                + "-fx-border-color: rgba(255,255,255,0.5);"
                                + "-fx-border-radius: " + radius + ";"
                                + "-fx-border-width: 1;"
                                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.06), 16, 0.1, 0, 4);";
        }

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

        private String rgba(String hex, double alpha) {
                int r = Integer.parseInt(hex.substring(1, 3), 16);
                int g = Integer.parseInt(hex.substring(3, 5), 16);
                int b = Integer.parseInt(hex.substring(5, 7), 16);
                return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
        }
}