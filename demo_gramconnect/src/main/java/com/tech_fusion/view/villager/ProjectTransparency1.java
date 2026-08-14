package com.tech_fusion.view.villager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

/**
 * GramConnect - Project Details
 *
 * ============================================================
 * NAVIGATION NOTE
 * ============================================================
 * Same pattern as ProjectTransparency.java: this page owns its own
 * sidebar + header and hands back a full Scene, so it's reached with
 *   homeStage.setScene(new ProjectTransparency1().getProjectDetailsScene(backToProjectsAction));
 * from a project card's "View Details" button in ProjectTransparency,
 * and returns via the "Back to Projects" button / breadcrumb calling
 * backToProjectsAction.run().
 *
 * NEW: "Report an Issue" button/card navigates to ReportProject.java,
 * the same way every other page-to-page jump works here - a Runnable
 * is built on the spot that knows how to rebuild THIS scene
 * (getProjectDetailsScene(backToProjectsAction) again) and is handed
 * to ReportProject so its "Cancel"/breadcrumb/post-submit flow can
 * come straight back to this exact project's details, not just any
 * generic "back" destination.
 *
 * SCOPE NOTE: per your request this only includes the image gallery,
 * project overview, start/end dates + location, project status
 * (donut), budget overview, budget utilization receipts, and now the
 * report-an-issue entry point - the Progress Timeline / Current
 * Progress / Quick Info / extra tabs from the reference screenshot
 * are left out.
 *
 * ARCHITECTURE NOTE: all data below (project fields, budget figures,
 * receipts) is hardcoded mock data for one project ("Village Road
 * Construction"), marked with TODO comments showing where a real
 * ProjectService.getProjectById(projectId) and
 * BudgetService.getUtilizationReceipts(projectId) would plug in.
 * ============================================================
 */
public class ProjectTransparency1 {

        // ================= COLORS (same palette as ProjectTransparency.java) =================
        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String DELAYED_RED = "#D94C38";

        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        private static final String LIGHT_GREEN = "#E8F7EA";
        private static final String SECONDARY = "#1976D2";
        private static final String LIGHT_BLUE = "#EAF5FC";
        private static final String WARNING = "#F4A62A";
        private static final String LIGHT_YELLOW = "#FFF7E5";
        private static final String LIGHT_RED = "#FDECEC";

        private static final String BACKGROUND = "#EFF5F1";
        private static final String TEXT_PRIMARY = FOREST_DEEP;
        private static final String TEXT_SECONDARY = "rgba(11,61,46,0.65)";
        private static final String BORDER = "#D8E2DC";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        /**
         * Builds this page's full Scene, exactly like ProjectTransparency's
         * getProjectScene(Runnable).
         *
         * @param backToProjectsAction what to run to get back to the
         *                             ProjectTransparency list - used by the
         *                             breadcrumb, the "Back to Projects"
         *                             button, AND the sidebar's own "Project
         *                             transparency" nav item.
         */
        public Scene getProjectDetailsScene(Runnable backToProjectsAction) {
                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BACKGROUND + ";");

                root.setLeft(buildSidebar(backToProjectsAction));
                root.setCenter(buildMainArea(backToProjectsAction));

                return new Scene(root, 1500, 850);
        }

        // =================================================================
        // SIDEBAR
        // =================================================================
        private VBox buildSidebar(Runnable backToProjectsAction) {
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
                        VillagerDashboard.homeStage.setScene(new VillagerDashboard().getDashboardScene());
                });

                Label projectsNav = navItem("\uD83C\uDFD7  Project transparency", true);
                projectsNav.setOnMouseClicked(e -> backToProjectsAction.run());

                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", false);
                complaintsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new ComplaintsPage().getComplaintsPage(backToProjectsAction));
                });
                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", false);
                schemesNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new GovernmentSchemes().getSchemesScene(backToProjectsAction));
                });
                Label certificatesNav = navItem("\uD83D\uDCDC  Certificates", false);
                certificatesNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new Certificates().getCertificatesScene(backToProjectsAction));
                });
                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", false);
                billsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new BillsAndPayments().getBillsScene(backToProjectsAction));
                });
                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                announcementsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new Announcements().getAnnouncementScene(backToProjectsAction));
                });
                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", false);
                gramSabhaNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new GramSabha().getGramSabhaScene(backToProjectsAction));
                });
                Label aiAssistantNav = navItem("\uD83E\uDD16  AI village assistant", false);
                aiAssistantNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new AIAssistant().getAiAssiatantScene(backToProjectsAction));
                });

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
        private BorderPane buildMainArea(Runnable backToProjectsAction) {
                BorderPane main = new BorderPane();
                main.setTop(buildHeader());
                main.setCenter(buildScrollableContent(backToProjectsAction));
                return main;
        }

        /** Identical header (search + bell + profile) as ProjectTransparency.java. */
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

        private ScrollPane buildScrollableContent(Runnable backToProjectsAction) {
                VBox content = new VBox(20);
                content.setPadding(new Insets(20, 24, 30, 24));

                content.getChildren().addAll(
                                buildBreadcrumbRow(backToProjectsAction),
                                buildTitleRow(backToProjectsAction),
                                buildMetaRow(),
                                buildMainContentRow(backToProjectsAction));

                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent;");
                return scrollPane;
        }

        // ---- Breadcrumb ----
        private HBox buildBreadcrumbRow(Runnable backToProjectsAction) {
                Label crumbRoot = new Label("Project Transparency");
                crumbRoot.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; "
                                                + "-fx-text-fill: " + SECONDARY + "; -fx-cursor: hand;");
                crumbRoot.setOnMouseClicked(e -> backToProjectsAction.run());

                Label separator = new Label("\u203A");
                separator.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                Label crumbCurrent = new Label("Project Details");
                crumbCurrent.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; "
                                                + "-fx-text-fill: " + SECONDARY + ";");

                HBox row = new HBox(6, crumbRoot, separator, crumbCurrent);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- Title row: name + status pill on the left, "Back to Projects" on the right ----
        private HBox buildTitleRow(Runnable backToProjectsAction) {
                Label title = new Label("Village Road Construction");
                title.setStyle("-fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 26px; -fx-font-weight: bold;");

                Label statusPill = new Label("On Track");
                statusPill.setStyle("-fx-background-color: " + LIGHT_GREEN + "; -fx-text-fill: " + FOREST_DEEP + "; "
                                + "-fx-background-radius: 10; -fx-padding: 4 12 4 12; -fx-font-size: 11px; -fx-font-weight: bold;");

                HBox titleGroup = new HBox(12, title, statusPill);
                titleGroup.setAlignment(Pos.CENTER_LEFT);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button backBtn = new Button("\u2190  Back to Projects");
                backBtn.setStyle(
                                "-fx-background-color: " + FOREST_DEEP + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-padding: 10 16 10 16;" +
                                                "-fx-cursor: hand;");
                backBtn.setOnAction(e -> backToProjectsAction.run());

                HBox row = new HBox(titleGroup, spacer, backBtn);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- Meta row: location + type ----
        private HBox buildMetaRow() {
                Label location = new Label("\uD83D\uDCCD  Main Street, Suryapuri");
                location.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                Label type = new Label("\uD83C\uDFD7  Road Infrastructure");
                type.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                HBox row = new HBox(20, location, type);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- Main content: left column (gallery + overview + report card) / right column (status + budget + receipts) ----
        private HBox buildMainContentRow(Runnable backToProjectsAction) {
                HBox row = new HBox(18, buildLeftColumn(backToProjectsAction), buildRightColumn());
                return row;
        }

        // =================================================================
        // LEFT COLUMN: image gallery + project overview + report-an-issue
        // =================================================================
        private VBox buildLeftColumn(Runnable backToProjectsAction) {
                VBox column = new VBox(20,
                                buildImageGalleryCard(),
                                buildOverviewCard(),
                                buildReportCard(backToProjectsAction));
                HBox.setHgrow(column, Priority.ALWAYS);
                return column;
        }

        private VBox buildImageGalleryCard() {
                // TODO: replace with real photo URLs from Firebase Storage /
                // ProjectService.getProjectPhotos(projectId)
                String[] photoPaths = {
                                "assets\\images\\road.jpg",
                                "assets\\images\\street_light.jpg",
                                "assets\\images\\street_light.jpg",
                                "assets\\images\\street_light.jpg"
                };

                ImageView mainImage = new ImageView(new Image(photoPaths[0]));
                mainImage.setFitWidth(760);
                mainImage.setFitHeight(360);
                mainImage.setPreserveRatio(false);
                Rectangle mainClip = new Rectangle(760, 360);
                mainClip.setArcWidth(14);
                mainClip.setArcHeight(14);
                mainImage.setClip(mainClip);

                HBox thumbRow = new HBox(10);
                thumbRow.setAlignment(Pos.CENTER_LEFT);
                for (int i = 0; i < photoPaths.length; i++) {
                        ImageView thumb = new ImageView(new Image(photoPaths[i]));
                        thumb.setFitWidth(90);
                        thumb.setFitHeight(64);
                        thumb.setPreserveRatio(false);
                        Rectangle clip = new Rectangle(90, 64);
                        clip.setArcWidth(8);
                        clip.setArcHeight(8);
                        thumb.setClip(clip);

                        StackPane thumbFrame = new StackPane(thumb);
                        boolean selected = (i == photoPaths.length - 1);
                        thumbFrame.setStyle(
                                        selected
                                                        ? "-fx-border-color: " + FOREST_DEEP + "; -fx-border-width: 2; -fx-border-radius: 8;"
                                                        : "-fx-border-color: transparent; -fx-border-width: 2;");
                        thumbRow.getChildren().add(thumbFrame);
                }

                VBox card = new VBox(14, mainImage, thumbRow);
                card.setPadding(new Insets(18));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return card;
        }

        private VBox buildOverviewCard() {
                Label title = new Label("Project Overview");
                title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label description = new Label(
                                "Construction of main village road with proper drainage and street lighting to "
                                                + "improve connectivity and living standards.");
                description.setWrapText(true);
                description.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                // TODO: replace with ProjectService.getProjectById(projectId)
                GridPane grid = new GridPane();
                grid.setHgap(28);
                grid.setVgap(18);
                grid.add(overviewField("Project ID", "PRJ-2025-001"), 0, 0);
                grid.add(overviewField("Scheme", "Gram Panchayat Development Plan"), 1, 0);
                grid.add(overviewField("Executing Agency", "Gram Panchayat Suryapuri"), 2, 0);
                grid.add(overviewField("Start Date", "15 Mar 2026"), 0, 1);
                grid.add(overviewField("Expected End Date", "15 Sep 2026"), 1, 1);
                grid.add(overviewField("Project Type", "Infrastructure"), 2, 1);
                grid.add(overviewField("Beneficiaries", "1,250 Villagers"), 0, 2);
                grid.add(overviewField("Ward/Area", "Ward No. 2"), 1, 2);
                grid.add(overviewField("Status", "On Track"), 2, 2);

                VBox card = new VBox(16, title, description, grid);
                card.setPadding(new Insets(20));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return card;
        }

        private VBox overviewField(String label, String value) {
                Label labelText = new Label(label.toUpperCase());
                labelText.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_SECONDARY
                                + "; -fx-letter-spacing: 0.05em;");
                Label valueText = new Label(value);
                valueText.setWrapText(true);
                valueText.setMaxWidth(220);
                valueText.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                return new VBox(4, labelText, valueText);
        }

        /**
         * NEW: "Report an Issue" call-to-action card. Also helps fill the
         * empty space that used to sit below the Overview card on wide
         * windows, since it stretches the same width as the gallery/overview
         * cards above it.
         */
        private VBox buildReportCard(Runnable backToProjectsAction) {
                Label icon = new Label("\uD83D\uDEA9");
                icon.setStyle("-fx-font-size: 22px;");
                StackPane iconCircle = new StackPane(icon);
                iconCircle.setPrefSize(50, 50);
                iconCircle.setMaxSize(50, 50);
                iconCircle.setStyle("-fx-background-color: " + LIGHT_RED + "; -fx-background-radius: 25;");

                Label title = new Label("Notice a problem with this project?");
                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label body = new Label(
                                "Poor quality work, delays, safety concerns, or anything else - let the Gram "
                                                + "Panchayat know so it can be looked into.");
                body.setWrapText(true);
                body.setMaxWidth(420);
                body.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox textBox = new VBox(6, title, body);
                HBox.setHgrow(textBox, Priority.ALWAYS);

                Button reportBtn = new Button("\uD83D\uDEA9  Report an Issue");
                reportBtn.setStyle(
                                "-fx-background-color: " + DELAYED_RED + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 12 20 12 20;" +
                                                "-fx-cursor: hand;");
                // The Runnable below rebuilds THIS exact project's Details scene,
                // so ReportProject's "Cancel"/breadcrumb/post-submit navigation
                // lands back here rather than at some generic dashboard.
                reportBtn.setOnAction(e -> {
                        Runnable backToProjectDetailsAction = () -> VillagerDashboard.homeStage
                                        .setScene(getProjectDetailsScene(backToProjectsAction));
                        ReportProject reportProject = new ReportProject();
                        VillagerDashboard.homeStage.setScene(reportProject.getReportScene(backToProjectDetailsAction));
                });

                HBox row = new HBox(18, iconCircle, textBox, reportBtn);
                row.setAlignment(Pos.CENTER_LEFT);

                VBox card = new VBox(row);
                card.setPadding(new Insets(24));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + LIGHT_RED + "; -fx-border-radius: 12; -fx-border-width: 1;");
                VBox.setVgrow(card, Priority.ALWAYS);
                return card;
        }

        // =================================================================
        // RIGHT COLUMN: Project Status donut + Budget Overview + Bill Receipts
        // =================================================================
        private VBox buildRightColumn() {
                VBox column = new VBox(20, buildProjectStatusCard(), buildBudgetOverviewCard(), buildReceiptsCard());
                column.setPrefWidth(360);
                column.setMinWidth(360);
                return column;
        }

        private VBox buildProjectStatusCard() {
                Label title = new Label("Project Status");
                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                int completed = 78;
                int inProgress = 0;
                int remaining = 22;

                StackPane donut = new StackPane();
                Canvas canvas = new Canvas(150, 150);
                drawDonut(canvas.getGraphicsContext2D(), completed, inProgress);
                VBox center = new VBox(2);
                center.setAlignment(Pos.CENTER);
                Label pct = new Label(completed + "%");
                pct.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label pctSub = new Label("Complete");
                pctSub.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                center.getChildren().addAll(pct, pctSub);
                donut.getChildren().addAll(canvas, center);

                VBox legend = new VBox(10,
                                statusLegendRow(FOREST_DEEP, "Completed", completed + "%"),
                                statusLegendRow(SECONDARY, "In Progress", inProgress + "%"),
                                statusLegendRow(WARNING, "Remaining", remaining + "%"));
                HBox.setHgrow(legend, Priority.ALWAYS);

                HBox body = new HBox(20, donut, legend);
                body.setAlignment(Pos.CENTER_LEFT);

                Label banner = new Label("\u2713  Project is on track and in good progress.");
                banner.setWrapText(true);
                banner.setStyle("-fx-background-color: " + LIGHT_GREEN + "; -fx-text-fill: " + FOREST_DEEP + "; "
                                + "-fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 11px; -fx-font-weight: bold;");

                VBox card = new VBox(16, title, body, banner);
                card.setPadding(new Insets(20));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return card;
        }

        private void drawDonut(GraphicsContext g, int completedPct, int inProgressPct) {
                double cx = 75, cy = 75, r = 60, stroke = 20;
                g.setLineWidth(stroke);
                g.setLineCap(StrokeLineCap.BUTT);
                g.setStroke(Color.web(WARNING, 0.35));
                g.strokeArc(cx - r, cy - r, 2 * r, 2 * r, 0, 360, ArcType.OPEN);
                if (inProgressPct > 0) {
                        g.setStroke(Color.web(SECONDARY));
                        g.strokeArc(cx - r, cy - r, 2 * r, 2 * r, 90 - (completedPct * 3.6), -(inProgressPct * 3.6),
                                        ArcType.OPEN);
                }
                g.setStroke(Color.web(FOREST_DEEP));
                g.strokeArc(cx - r, cy - r, 2 * r, 2 * r, 90, -(completedPct * 3.6), ArcType.OPEN);
        }

        private HBox statusLegendRow(String color, String label, String value) {
                Circle dot = new Circle(4, Color.web(color));
                Label labelText = new Label(label);
                labelText.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                Label valueText = new Label(value);
                valueText.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                HBox row = new HBox(8, dot, labelText, spacer, valueText);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        private VBox buildBudgetOverviewCard() {
                Label title = new Label("Budget Overview");
                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label totalLabel = new Label("Total Allocated");
                totalLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                Label totalValue = new Label("\u20B96,25,000");
                totalValue.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + FOREST_DEEP + ";");
                VBox totalBox = new VBox(2, totalLabel, totalValue);

                VBox bars = new VBox(16,
                                budgetBar(FOREST_DEEP, "Spent", "\u20B94,87,500", 78),
                                budgetBar(SECONDARY, "In Progress", "\u20B91,00,000", 16),
                                budgetBar(WARNING, "Remaining", "\u20B937,500", 6));

                VBox card = new VBox(16, title, totalBox, bars);
                card.setPadding(new Insets(20));
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
                amountText.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_SECONDARY + ";");
                HBox topRow = new HBox(labelRow, topSpacer, amountText);
                topRow.setAlignment(Pos.CENTER_LEFT);

                double trackWidth = 310;
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

        // =================================================================
        // Budget Utilization Receipts
        // =================================================================
        private VBox buildReceiptsCard() {
                Label title = new Label("Budget Utilization Receipts");
                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label subtitle = new Label("Bills uploaded by the Gram Panchayat for this project's spending.");
                subtitle.setWrapText(true);
                subtitle.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox list = new VBox(12,
                                receiptRow("Cement & Gravel Purchase", "\u20B91,85,000", "20 Mar 2026"),
                                receiptRow("Labour Wages - March", "\u20B91,20,000", "05 Apr 2026"),
                                receiptRow("Road Roller Rental", "\u20B995,000", "18 Apr 2026"),
                                receiptRow("Drainage Pipes Purchase", "\u20B987,500", "02 May 2026"));

                VBox card = new VBox(14, title, subtitle, list);
                card.setPadding(new Insets(20));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return card;
        }

        private VBox receiptRow(String description, String amount, String date) {
                Label iconLabel = new Label("\uD83E\uDDFE");
                iconLabel.setStyle("-fx-text-fill: " + FOREST_DEEP + "; -fx-font-size: 14px;");
                StackPane iconChip = new StackPane(iconLabel);
                iconChip.setPrefSize(34, 34);
                iconChip.setMaxSize(34, 34);
                iconChip.setStyle("-fx-background-color: " + LIGHT_GREEN + "; -fx-background-radius: 9;");

                Label descLabel = new Label(description);
                descLabel.setWrapText(true);
                descLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label dateLabel = new Label("Uploaded " + date);
                dateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                VBox textBox = new VBox(2, descLabel, dateLabel);
                HBox.setHgrow(textBox, Priority.ALWAYS);

                Label amountLabel = new Label(amount);
                amountLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                HBox topRow = new HBox(10, iconChip, textBox, amountLabel);
                topRow.setAlignment(Pos.CENTER_LEFT);

                Button viewReceiptBtn = new Button("\uD83D\uDCC4  View Receipt");
                viewReceiptBtn.setMaxWidth(Double.MAX_VALUE);
                viewReceiptBtn.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";" +
                                                "-fx-border-color: " + FOREST_DEEP + ";" +
                                                "-fx-border-radius: 6;" +
                                                "-fx-background-radius: 6;" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-padding: 7;" +
                                                "-fx-cursor: hand;");
                // TODO: open the actual uploaded receipt image/PDF (Firebase Storage URL)

                VBox row = new VBox(8, topRow, viewReceiptBtn);
                row.setPadding(new Insets(12));
                row.setStyle("-fx-background-color: " + BACKGROUND + "; -fx-background-radius: 8; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 8; -fx-border-width: 1;");
                return row;
        }
}