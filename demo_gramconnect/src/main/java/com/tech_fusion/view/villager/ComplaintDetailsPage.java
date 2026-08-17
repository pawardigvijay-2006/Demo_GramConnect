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
import javafx.scene.shape.Rectangle;

import com.tech_fusion.view.villager.ComplaintsPage.ComplaintData;

/**
 * GramConnect - Complaint Details
 *
 * ============================================================
 * NAVIGATION NOTE
 * ============================================================
 * Same pattern as ProjectTransparency1.java / ReportProject.java: this
 * page owns its own sidebar + header and hands back a full Scene, so
 * it's reached from a complaint row's eye-icon "View Details" block in
 * ComplaintsPage via:
 *
 *   homeStage.setScene(new ComplaintDetailsPage()
 *       .getComplaintDetailsScene(backToComplaintsAction, backToDashboardAction, complaint));
 *
 * Two Runnables come in, each with one job - this is what keeps
 * navigation between this page and ComplaintsPage conflict-free:
 * - backToComplaintsAction: rebuilds the Complaints list exactly as
 *   ComplaintsPage.openComplaintDetails(...) left it (same backAction
 *   chain back to the Dashboard). Used by the breadcrumb's "Complaints"
 *   crumb, the "Back to Complaints" button, AND the sidebar's own
 *   "Complaints" nav item, so all three behave identically.
 * - backToDashboardAction: used ONLY by the sidebar's "Dashboard" item
 *   (hardcoded straight to VillagerDashboard.getDashboardScene(), same
 *   as every other page) and forwarded to sibling pages (Project
 *   transparency, Schemes, ...) exactly like ComplaintsPage's own
 *   sidebar does - so navigating away from here behaves identically to
 *   navigating away from the complaints list.
 *
 * DATA NOTE: takes a ComplaintsPage.ComplaintData directly (that class
 * is package-visible), so there is exactly one source of truth for a
 * complaint's fields - no duplicated/out-of-sync data between the list
 * row and this details page.
 * ============================================================
 */
public class ComplaintDetailsPage {

        // ================= COLORS (same palette as ComplaintsPage.java) =================
        private static final String PRIMARY = "#005B1B";
        private static final String BACKGROUND = "#F4F8FB";
        private static final String TEXT_PRIMARY = "#10251A";
        private static final String TEXT_SECONDARY = "#66756C";
        private static final String BORDER = "#D8E2DC";
        private static final String SECONDARY = "#1976D2";
        private static final String LIGHT_BLUE = "#EAF5FC";

        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String DELAYED_RED = "#D94C38";

        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        /**
         * Builds this page's full Scene, exactly like ProjectTransparency1's
         * getProjectDetailsScene(Runnable).
         *
         * @param backToComplaintsAction returns to the Complaints list, in the
         *                               exact state ComplaintsPage left it in.
         * @param backToDashboardAction  returns to the real Dashboard - used
         *                               only by the sidebar's "Dashboard" item
         *                               and forwarded to sibling pages.
         * @param complaint              the complaint to display.
         */
        public Scene getComplaintDetailsScene(Runnable backToComplaintsAction, Runnable backToDashboardAction,
                        ComplaintData complaint) {
                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BACKGROUND + ";");

                root.setLeft(buildSidebar(backToComplaintsAction, backToDashboardAction));
                root.setCenter(buildMainArea(backToComplaintsAction, complaint));

                return new Scene(root, 1500, 850);
        }

        // =================================================================
        // SIDEBAR (identical to ComplaintsPage.java's, "Complaints" active
        // since this page lives under that section)
        // =================================================================
        private VBox buildSidebar(Runnable backToComplaintsAction, Runnable backToDashboardAction) {
                VBox sidebar = new VBox();
                sidebar.setPrefWidth(230);
                sidebar.setMinWidth(230);
                sidebar.setMaxWidth(230);
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

                // BUGFIX-PATTERN: goes straight to the real Dashboard, like every
                // other page's sidebar - never backToComplaintsAction.
                Label dashboardNav = navItem("\uD83C\uDFE0  Dashboard", false);
                dashboardNav.setOnMouseClicked(e -> backToDashboardAction.run());

                Label projectsNav = navItem("\uD83C\uDFD7  Project transparency", false);
                projectsNav.setOnMouseClicked(e -> VillagerDashboard.homeStage
                                .setScene(new ProjectTransparency().getProjectScene(backToDashboardAction)));

                // Reuses the SAME backToComplaintsAction the breadcrumb and "Back
                // to Complaints" button use, so all three paths back behave
                // identically.
                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", true);
                complaintsNav.setOnMouseClicked(e -> backToComplaintsAction.run());

                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", false);
                schemesNav.setOnMouseClicked(e -> VillagerDashboard.homeStage
                                .setScene(new GovernmentSchemes().getSchemesScene(backToDashboardAction)));

                Label certificatesNav = navItem("\uD83D\uDCDC  Certificates", false);
                certificatesNav.setOnMouseClicked(e -> VillagerDashboard.homeStage
                                .setScene(new Certificates().getCertificatesScene(backToDashboardAction)));

                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", false);
                billsNav.setOnMouseClicked(e -> VillagerDashboard.homeStage
                                .setScene(new BillsAndPayments().getBillsScene(backToDashboardAction)));

                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                announcementsNav.setOnMouseClicked(e -> VillagerDashboard.homeStage
                                .setScene(new Announcements().getAnnouncementScene(backToDashboardAction)));

                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", false);
                gramSabhaNav.setOnMouseClicked(e -> VillagerDashboard.homeStage
                                .setScene(new GramSabha().getGramSabhaScene(backToDashboardAction)));

                Label aiAssistantNav = navItem("\uD83E\uDD16  AI village assistant", false);
                aiAssistantNav.setOnMouseClicked(e -> VillagerDashboard.homeStage
                                .setScene(new AIAssistant().getAiAssiatantScene(backToDashboardAction)));

                VBox navItems = new VBox(4,
                                dashboardNav, projectsNav, complaintsNav, schemesNav,
                                certificatesNav, billsNav, announcementsNav, gramSabhaNav, aiAssistantNav);
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
        // MAIN AREA
        // =================================================================
        private BorderPane buildMainArea(Runnable backToComplaintsAction, ComplaintData complaint) {
                BorderPane main = new BorderPane();
                main.setTop(buildHeader());
                main.setCenter(buildScrollableContent(backToComplaintsAction, complaint));
                return main;
        }

        /** Identical header (search + bell + profile) as ComplaintsPage.java. */
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

        private ScrollPane buildScrollableContent(Runnable backToComplaintsAction, ComplaintData complaint) {
                VBox content = new VBox(18);
                content.setPadding(new Insets(18, 26, 28, 26));

                content.getChildren().addAll(
                                buildBreadcrumbRow(backToComplaintsAction),
                                buildTitleRow(backToComplaintsAction),
                                buildDetailsCard(complaint),
                                buildSubmittedFooter(complaint));

                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent;");
                return scrollPane;
        }

        // ---- Breadcrumb: Complaints > Complaint Details ----
        private HBox buildBreadcrumbRow(Runnable backToComplaintsAction) {
                Label crumbList = new Label("Complaints");
                crumbList.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; "
                                                + "-fx-text-fill: " + SECONDARY + "; -fx-cursor: hand;");
                crumbList.setOnMouseClicked(e -> backToComplaintsAction.run());

                Label sep = new Label("\u203A");
                sep.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                Label crumbCurrent = new Label("Complaint Details");
                crumbCurrent.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; "
                                                + "-fx-text-fill: " + TEXT_PRIMARY + ";");

                HBox row = new HBox(6, crumbList, sep, crumbCurrent);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- Title row: heading on the left, "Back to Complaints" on the right ----
        private HBox buildTitleRow(Runnable backToComplaintsAction) {
                Label title = new Label("Complaint Details");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-text-fill: " + TEXT_PRIMARY
                                                + "; -fx-font-size: 24px; -fx-font-weight: 900;");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button backBtn = new Button("\u2190  Back to Complaints");
                backBtn.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: " + PRIMARY + ";" +
                                                "-fx-border-color: " + PRIMARY + ";" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-padding: 10 16 10 16;" +
                                                "-fx-cursor: hand;");
                backBtn.setOnAction(e -> backToComplaintsAction.run());

                HBox row = new HBox(title, spacer, backBtn);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // =================================================================
        // DETAILS CARD: left = Title/Location/Description, right = photo +
        // status pill + status icon
        // =================================================================
        private HBox buildDetailsCard(ComplaintData complaint) {

                VBox leftColumn = new VBox(0,
                                fieldRow("\uD83D\uDCA7", complaint.iconBg, complaint.iconColor, "Title",
                                                boldValue(complaint.title)),
                                fieldDivider(),
                                fieldRow("\uD83D\uDCCD", "#E8F5E9", PRIMARY, "Location",
                                                boldValue(complaint.location)),
                                fieldDivider(),
                                fieldRow("\uD83D\uDCC4", "#E3F2FD", SECONDARY, "Description",
                                                wrappedValue(complaint.description)));
                leftColumn.setPadding(new Insets(24));
                HBox.setHgrow(leftColumn, Priority.ALWAYS);

                VBox rightColumn = buildImageColumn(complaint);
                rightColumn.setPadding(new Insets(24));
                rightColumn.setPrefWidth(560);
                rightColumn.setMinWidth(420);

                HBox card = new HBox(leftColumn, rightColumn);
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return card;
        }

        /** One field: small icon chip + label on top, value node underneath. */
        private VBox fieldRow(String icon, String iconBg, String iconColor, String label, javafx.scene.Node valueNode) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + iconColor + ";");
                StackPane iconCircle = new StackPane(iconLabel);
                iconCircle.setPrefSize(30, 30);
                iconCircle.setMaxSize(30, 30);
                iconCircle.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 15;");

                Label labelText = new Label(label);
                labelText.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                HBox header = new HBox(10, iconCircle, labelText);
                header.setAlignment(Pos.CENTER_LEFT);

                VBox box = new VBox(10, header, valueNode);
                box.setPadding(new Insets(16, 0, 16, 0));
                return box;
        }

        private Label boldValue(String text) {
                Label label = new Label(text);
                label.setWrapText(true);
                label.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 17px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + "; -fx-padding: 0 0 0 40;");
                return label;
        }

        private Label wrappedValue(String text) {
                Label label = new Label(text);
                label.setWrapText(true);
                label.setMaxWidth(420);
                label.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: " + TEXT_PRIMARY
                                                + "; -fx-padding: 0 0 0 40;");
                return label;
        }

        private Region fieldDivider() {
                Region divider = new Region();
                divider.setPrefHeight(1);
                divider.setStyle("-fx-background-color: " + BORDER + ";");
                return divider;
        }

        /** Right column: "Image Uploaded by Complainant" header + status pill/icon, then the photo. */
        private VBox buildImageColumn(ComplaintData complaint) {
                Label headerIcon = new Label("\uD83D\uDDBC");
                headerIcon.setStyle("-fx-font-size: 14px; -fx-text-fill: " + PRIMARY + ";");

                Label headerText = new Label("Image Uploaded by Complainant");
                headerText.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                HBox headerLeft = new HBox(8, headerIcon, headerText);
                headerLeft.setAlignment(Pos.CENTER_LEFT);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // NEW: a small colored icon showing this specific complaint's
                // status, right next to its status pill.
                Label statusIcon = new Label(statusIconFor(complaint.status));
                statusIcon.setStyle("-fx-font-size: 13px; -fx-text-fill: " + complaint.pillFg + ";");

                Label statusPill = new Label(complaint.status);
                statusPill.setStyle("-fx-background-color: " + complaint.pillBg + "; -fx-text-fill: " + complaint.pillFg
                                + "; -fx-background-radius: 10; -fx-padding: 4 12 4 12; -fx-font-size: 11px; -fx-font-weight: bold;");

                HBox statusGroup = new HBox(6, statusIcon, statusPill);
                statusGroup.setAlignment(Pos.CENTER_LEFT);

                HBox header = new HBox(headerLeft, spacer, statusGroup);
                header.setAlignment(Pos.CENTER_LEFT);

                ImageView image = new ImageView(new Image(complaint.imagePath));
                image.setFitWidth(500);
                image.setFitHeight(360);
                image.setPreserveRatio(false);
                Rectangle clip = new Rectangle(500, 360);
                clip.setArcWidth(12);
                clip.setArcHeight(12);
                image.setClip(clip);

                VBox column = new VBox(16, header, image);
                return column;
        }

        /** Maps a complaint's status text to a small representative icon. */
        private String statusIconFor(String status) {
                switch (status) {
                        case "Resolved":
                                return "\u2705"; // check mark
                        case "Pending":
                                return "\u23F3"; // hourglass
                        case "Action Required":
                                return "\u26A0"; // warning triangle
                        case "Assigned":
                                return "\uD83D\uDCCC"; // pushpin
                        default:
                                return "\u2139"; // info
                }
        }

        // ---- Footer: "Submitted on <date> at <time>" with flanking divider lines ----
        private HBox buildSubmittedFooter(ComplaintData complaint) {
                Region lineLeft = new Region();
                lineLeft.setPrefHeight(1);
                lineLeft.setStyle("-fx-background-color: " + BORDER + ";");
                HBox.setHgrow(lineLeft, Priority.ALWAYS);

                // TODO: store the real submission time on ComplaintData instead of
                // this placeholder - the reference design shows a specific time.
                Label submitted = new Label("\uD83D\uDCC5  Submitted on " + complaint.date + " at 09:15 AM");
                submitted.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY
                                                + ";");

                Region lineRight = new Region();
                lineRight.setPrefHeight(1);
                lineRight.setStyle("-fx-background-color: " + BORDER + ";");
                HBox.setHgrow(lineRight, Priority.ALWAYS);

                HBox row = new HBox(16, lineLeft, submitted, lineRight);
                row.setAlignment(Pos.CENTER);
                row.setPadding(new Insets(8, 40, 8, 40));
                return row;
        }
}