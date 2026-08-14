package com.tech_fusion.view.villager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * GramConnect - Report an Issue
 *
 * ============================================================
 * NAVIGATION NOTE
 * ============================================================
 * Same pattern as every other page: owns its own sidebar + header,
 * hands back a full Scene, reached with
 *   homeStage.setScene(new ReportProject().getReportScene(backToProjectDetailsAction));
 * from ProjectDetails' "Report an Issue" button. The breadcrumb's
 * "Project Details" crumb and the "Cancel" button both call
 * backToProjectDetailsAction.run() to go back without submitting -
 * this class never touches Stage or ProjectDetails directly.
 *
 * FLOW:
 * 1. The person picks "Fill Manually" (default) or "Generate with AI".
 * 2. AI mode: they type a short description of the problem, hit
 * "Generate Report", and Title/Description below get filled in for
 * them (mock generation for now - see the TODO on generateReport()).
 * Either way, both fields stay editable before sending.
 * 3. "Send Report" validates the fields aren't empty, then shows a
 * confirmation popup ("Report Submitted Successfully"). Closing that
 * popup navigates back to Project Details automatically.
 * ============================================================
 */
public class ReportProject {

        // ================= COLORS (same palette as ProjectTransparency1.java) =================
        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String DELAYED_RED = "#D94C38";

        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        private static final String LIGHT_GREEN = "#E8F7EA";
        private static final String SECONDARY = "#1976D2";
        private static final String LIGHT_BLUE = "#EAF5FC";

        private static final String BACKGROUND = "#EFF5F1";
        private static final String TEXT_PRIMARY = FOREST_DEEP;
        private static final String TEXT_SECONDARY = "rgba(11,61,46,0.65)";
        private static final String BORDER = "#D8E2DC";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        // Kept as fields so generateReport()/sendReport() can reach them without
        // threading parameters through every helper method.
        private TextField titleField;
        private TextArea descriptionArea;
        private TextField aiPromptField;
        private VBox aiSection;

        /**
         * Builds this page's full Scene.
         *
         * @param backToProjectDetailsAction what to run to return to Project
         *                                    Details - used by "Cancel", the
         *                                    breadcrumb, AND automatically
         *                                    after a report is submitted.
         */
        public Scene getReportScene(Runnable backToProjectDetailsAction) {
                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BACKGROUND + ";");

                root.setLeft(buildSidebar(backToProjectDetailsAction));
                root.setCenter(buildMainArea(backToProjectDetailsAction));

                return new Scene(root, 1500, 850);
        }

        // =================================================================
        // SIDEBAR (same structure as ProjectTransparency1.java; "Project
        // transparency" stays active since this page lives under that
        // section, and now returns to Project Details, not the project list)
        // =================================================================
        private VBox buildSidebar(Runnable backToProjectDetailsAction) {
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
                projectsNav.setOnMouseClicked(e -> backToProjectDetailsAction.run());

                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", false);
                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", false);
                Label certificatesNav = navItem("\uD83D\uDCDC  Certificates", false);
                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", false);
                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", false);
                Label aiAssistantNav = navItem("\uD83E\uDD16  AI village assistant", false);
                // TODO: wire these once every page shares one consistent
                // "backToDashboardAction" instead of page-specific Runnables.

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
        private BorderPane buildMainArea(Runnable backToProjectDetailsAction) {
                BorderPane main = new BorderPane();
                main.setTop(buildHeader());
                main.setCenter(buildScrollableContent(backToProjectDetailsAction));
                return main;
        }

        /** Identical header as the rest of the app. */
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
                search.setPromptText("Search...");
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

                HBox profile = new HBox(8, avatar, nameBox);
                profile.setAlignment(Pos.CENTER_LEFT);

                header.getChildren().addAll(searchBox, spacer, bell, profile);
                return header;
        }

        private ScrollPane buildScrollableContent(Runnable backToProjectDetailsAction) {
                VBox content = new VBox(18);
                content.setPadding(new Insets(18, 24, 28, 24));
                content.setMaxWidth(760);

                content.getChildren().addAll(
                                buildBreadcrumbRow(backToProjectDetailsAction),
                                buildTitleRow(backToProjectDetailsAction),
                                buildFormCard(backToProjectDetailsAction));

                VBox wrapper = new VBox(content);
                wrapper.setAlignment(Pos.TOP_CENTER);
                wrapper.setPadding(new Insets(0, 0, 0, 0));

                ScrollPane scrollPane = new ScrollPane(wrapper);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent;");
                return scrollPane;
        }

        // ---- Breadcrumb ----
        private HBox buildBreadcrumbRow(Runnable backToProjectDetailsAction) {
                Label crumbTransparency = new Label("Project Transparency");
                crumbTransparency.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                Label sep1 = new Label("\u203A");
                sep1.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                Label crumbDetails = new Label("Project Details");
                crumbDetails.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; "
                                                + "-fx-text-fill: " + SECONDARY + "; -fx-cursor: hand;");
                crumbDetails.setOnMouseClicked(e -> backToProjectDetailsAction.run());

                Label sep2 = new Label("\u203A");
                sep2.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                Label crumbCurrent = new Label("Report an Issue");
                crumbCurrent.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; "
                                                + "-fx-text-fill: " + SECONDARY + ";");

                HBox row = new HBox(6, crumbTransparency, sep1, crumbDetails, sep2, crumbCurrent);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- Title row ----
        private HBox buildTitleRow(Runnable backToProjectDetailsAction) {
                Label title = new Label("Report an Issue");
                title.setStyle("-fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 24px; -fx-font-weight: bold;");
                Label subtitle = new Label("Village Road Construction \u00B7 Main Street, Suryapuri");
                subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                VBox titleBox = new VBox(2, title, subtitle);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button cancelBtn = new Button("Cancel");
                cancelBtn.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";" +
                                                "-fx-border-color: " + BORDER + ";" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-padding: 10 16 10 16;" +
                                                "-fx-cursor: hand;");
                cancelBtn.setOnAction(e -> backToProjectDetailsAction.run());

                HBox row = new HBox(titleBox, spacer, cancelBtn);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // =================================================================
        // FORM CARD: mode toggle, AI-generate section, title/description
        // fields, Send Report button
        // =================================================================
        private VBox buildFormCard(Runnable backToProjectDetailsAction) {

                // ---- Mode toggle ----
                ToggleGroup modeGroup = new ToggleGroup();

                ToggleButton manualToggle = new ToggleButton("\u270D  Fill Manually");
                manualToggle.setToggleGroup(modeGroup);
                manualToggle.setSelected(true);

                ToggleButton aiToggle = new ToggleButton("\u2728  Generate with AI");
                aiToggle.setToggleGroup(modeGroup);

                styleModeToggle(manualToggle);
                styleModeToggle(aiToggle);

                HBox modeRow = new HBox(10, manualToggle, aiToggle);
                modeRow.setAlignment(Pos.CENTER_LEFT);

                // ---- AI-assisted section (hidden until "Generate with AI" is picked) ----
                Label aiLabel = new Label("Briefly describe the problem");
                aiLabel.setStyle(
                                "-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_SECONDARY + ";");

                aiPromptField = new TextField();
                aiPromptField.setPromptText("e.g. large potholes forming near the school gate");
                aiPromptField.setStyle(
                                "-fx-background-color: " + LIGHT_BLUE + "; -fx-background-radius: 8; "
                                                + "-fx-padding: 10; -fx-font-size: 12px;");

                Button generateBtn = new Button("\u2728  Generate Report");
                generateBtn.setStyle(
                                "-fx-background-color: " + SECONDARY + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-padding: 9 16 9 16;" +
                                                "-fx-cursor: hand;");
                generateBtn.setOnAction(e -> generateReport());

                Label aiHint = new Label(
                                "AI will draft a title and description for you - review and edit before sending.");
                aiHint.setWrapText(true);
                aiHint.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                aiSection = new VBox(8, aiLabel, aiPromptField, generateBtn, aiHint);
                aiSection.setPadding(new Insets(14));
                aiSection.setStyle("-fx-background-color: " + LIGHT_BLUE + "; -fx-background-radius: 10; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 10; -fx-border-width: 1;");
                aiSection.setVisible(false);
                aiSection.setManaged(false);

                manualToggle.setOnAction(e -> {
                        aiSection.setVisible(false);
                        aiSection.setManaged(false);
                });
                aiToggle.setOnAction(e -> {
                        aiSection.setVisible(true);
                        aiSection.setManaged(true);
                });

                // ---- Title + description fields (shared by both modes) ----
                Label titleLabel = new Label("Report Title");
                titleLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_SECONDARY + ";");
                titleField = new TextField();
                titleField.setPromptText("Short summary of the issue");
                titleField.setStyle("-fx-background-color: " + BACKGROUND + "; -fx-background-radius: 8; "
                                + "-fx-padding: 10; -fx-font-size: 12px;");

                Label descLabel = new Label("Description");
                descLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_SECONDARY + ";");
                descriptionArea = new TextArea();
                descriptionArea.setPromptText("Describe what you noticed, where, and when...");
                descriptionArea.setWrapText(true);
                descriptionArea.setPrefRowCount(6);
                descriptionArea.setStyle("-fx-background-color: " + BACKGROUND + "; -fx-background-radius: 8; "
                                + "-fx-font-size: 12px;");

                Label errorLabel = new Label();
                errorLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + DELAYED_RED + "; -fx-font-weight: bold;");
                errorLabel.setVisible(false);
                errorLabel.setManaged(false);

                // ---- Send Report button ----
                Button sendBtn = new Button("\uD83D\uDCE4  Send Report");
                sendBtn.setStyle(
                                "-fx-background-color: " + FOREST_DEEP + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 12 22 12 22;" +
                                                "-fx-cursor: hand;");
                sendBtn.setOnAction(e -> {
                        if (titleField.getText() == null || titleField.getText().isBlank()
                                        || descriptionArea.getText() == null || descriptionArea.getText().isBlank()) {
                                errorLabel.setText("Please fill in both a title and a description before sending.");
                                errorLabel.setVisible(true);
                                errorLabel.setManaged(true);
                                return;
                        }
                        errorLabel.setVisible(false);
                        errorLabel.setManaged(false);
                        sendReport(backToProjectDetailsAction);
                });

                VBox card = new VBox(16,
                                modeRow, aiSection, titleLabel, titleField, descLabel, descriptionArea,
                                errorLabel, sendBtn);
                card.setPadding(new Insets(24));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 14; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 14; -fx-border-width: 1;");
                return card;
        }

        private void styleModeToggle(ToggleButton toggle) {
                String selectedStyle = "-fx-background-color: " + FOREST_DEEP + "; -fx-text-fill: white; "
                                + "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8; "
                                + "-fx-padding: 9 16 9 16; -fx-cursor: hand;";
                String unselectedStyle = "-fx-background-color: white; -fx-text-fill: " + TEXT_SECONDARY + "; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; "
                                + "-fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 9 16 9 16; -fx-cursor: hand;";

                toggle.setStyle(toggle.isSelected() ? selectedStyle : unselectedStyle);
                toggle.selectedProperty().addListener((obs, was, isNow) ->
                                toggle.setStyle(isNow ? selectedStyle : unselectedStyle));
        }

        /**
         * Mock AI report generation - fills the title/description fields from a
         * short prompt so the flow is fully demonstrable end to end.
         * TODO: replace with a real AIReportService.generateReport(prompt) call
         * to Gemini, running on a background thread the same way
         * AIAnalysisService is meant to for project photo analysis.
         */
        private void generateReport() {
                String prompt = aiPromptField.getText();
                if (prompt == null || prompt.isBlank()) {
                        return;
                }
                String trimmed = prompt.trim();
                String capitalized = trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1);

                titleField.setText("Issue reported: " + capitalized);
                descriptionArea.setText(
                                "While observing the Village Road Construction project, I noticed the following: "
                                                + trimmed + ". This is affecting residents in the area and I would "
                                                + "request the Gram Panchayat to look into it and take appropriate "
                                                + "action at the earliest.");
        }

        /** Shows the "Report Submitted" popup, then returns to Project Details. */
        private void sendReport(Runnable backToProjectDetailsAction) {
                // TODO: replace with ComplaintService.submitProjectReport(projectId, title, description)

                Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                confirmation.setTitle("GramConnect");
                confirmation.setHeaderText("Report Submitted Successfully!");
                confirmation.setContentText(
                                "Your report has been sent to the Gram Panchayat. "
                                                + "You'll be notified once it has been reviewed.");
                confirmation.getButtonTypes().setAll(ButtonType.OK);
                confirmation.showAndWait();

                backToProjectDetailsAction.run();
        }
}
