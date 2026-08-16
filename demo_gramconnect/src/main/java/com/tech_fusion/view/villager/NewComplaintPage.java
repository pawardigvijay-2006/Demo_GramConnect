package com.tech_fusion.view.villager;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

/**
 * GramConnect - Register New Complaint
 *
 * Same pattern as ComplaintsPage.java: not an Application, just a class
 * with one public method that returns a Scene to swap onto the shared
 * VillagerDashboard.homeStage.
 *
 * Usage:
 * VillagerDashboard.homeStage.setScene(
 * new NewComplaintPage().getNewComplaintScene(backAction, onSubmittedGoBack));
 *
 * - backAction -> same "go to Dashboard" callback threaded through every
 * other page's sidebar, unchanged.
 * - onSubmittedGoBack -> called after the user dismisses the "Complaint
 * Submitted" popup; normally this just re-opens ComplaintsPage so the
 * new complaint shows up in the list.
 */
public class NewComplaintPage {

        private static final String PRIMARY = "#005B1B";
        private static final String LIGHT_GREEN = "#E8F7EA";
        private static final String BACKGROUND = "#F4F8FB";
        private static final String TEXT_PRIMARY = "#10251A";
        private static final String TEXT_SECONDARY = "#66756C";
        private static final String BORDER = "#D8E2DC";
        private static final String ERROR = "#D93025";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String FOREST_DEEP = "#0B3D2E";

        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";
        private static final String DELAYED_RED = "#D94C38";

        // Form fields (built once, referenced from the submit handler)
        private TextField titleField;
        private TextField locationField;
        private TextArea descriptionField;
        private Label photoNameLabel;
        private File selectedPhoto;
        private Label titleError;
        private Label locationError;
        private Label descriptionError;

        public Scene getNewComplaintScene(Runnable backAction, Runnable onSubmittedGoBack) {
                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BACKGROUND + ";");

                root.setLeft(buildSidebar(backAction));
                root.setCenter(buildMainArea(backAction, onSubmittedGoBack));

                return new Scene(root, 1500, 850);
        }

        // =================================================================
        // SIDEBAR - identical to ComplaintsPage, "Complaints" stays active
        // since this is a sub-page of Complaints.
        // =================================================================
        private VBox buildSidebar(Runnable backAction) {

                VBox sidebar = new VBox();
                sidebar.setPrefWidth(230);
                sidebar.setMinWidth(230);
                sidebar.setMaxWidth(230);

                sidebar.setStyle(
                                "-fx-background-color: linear-gradient(to bottom, "
                                                + SIDEBAR_TOP + ", "
                                                + SIDEBAR_MID + ", "
                                                + SIDEBAR_BOT + ");"
                                                + "-fx-border-color: transparent rgba(11,61,46,0.10) "
                                                + "transparent transparent;"
                                                + "-fx-border-width: 0 1 0 0;"
                                                + "-fx-effect: dropshadow(gaussian, "
                                                + "rgba(11,61,46,0.20), 24, 0.2, 4, 0);");

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
                dashboardNav.setOnMouseClicked(e -> backAction.run());

                Label projectsNav = navItem("\uD83C\uDFD7  Project transparency", false);
                projectsNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage.setScene(
                                                new ProjectTransparency().getProjectScene(backAction)));

                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", true);
                complaintsNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage.setScene(
                                                new ComplaintsPage().getComplaintsPage(backAction)));

                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", false);
                schemesNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage
                                                .setScene(new GovernmentSchemes().getSchemesScene(backAction)));

                Label certificatesNav = navItem("\uD83D\uDCDC  Certificates", false);
                certificatesNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage
                                                .setScene(new Certificates().getCertificatesScene(backAction)));

                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", false);
                billsNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage
                                                .setScene(new BillsAndPayments().getBillsScene(backAction)));

                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                announcementsNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage
                                                .setScene(new Announcements().getAnnouncementScene(backAction)));

                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", false);
                gramSabhaNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage
                                                .setScene(new GramSabha().getGramSabhaScene(backAction)));

                Label aiAssistantNav = navItem("\uD83E\uDD16  AI village assistant", false);
                aiAssistantNav.setOnMouseClicked(
                                e -> VillagerDashboard.homeStage
                                                .setScene(new AIAssistant().getAiAssiatantScene(backAction)));

                VBox navItems = new VBox(
                                4,
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
                        String baseStyle = "-fx-font-family: " + FONT_FAMILY + ";"
                                        + "-fx-text-fill: rgba(11,61,46,0.80);"
                                        + "-fx-font-size: 13px;"
                                        + "-fx-font-weight: 700;"
                                        + "-fx-cursor: hand;";
                        item.setStyle(baseStyle);
                        item.setOnMouseEntered(
                                        e -> item.setStyle(
                                                        "-fx-font-family: " + FONT_FAMILY + ";"
                                                                        + "-fx-background-color: rgba(255,255,255,0.45);"
                                                                        + "-fx-text-fill: " + FOREST_DEEP + ";"
                                                                        + "-fx-font-weight: 700;"
                                                                        + "-fx-font-size: 13px;"
                                                                        + "-fx-background-radius: 8;"
                                                                        + "-fx-cursor: hand;"));
                        item.setOnMouseExited(e -> item.setStyle(baseStyle));
                }

                return item;
        }

        // =================================================================
        // MAIN AREA
        // =================================================================
        private BorderPane buildMainArea(Runnable backAction, Runnable onSubmittedGoBack) {
                BorderPane main = new BorderPane();
                main.setTop(buildHeader());
                main.setCenter(buildScrollableContent(backAction, onSubmittedGoBack));
                return main;
        }

        private HBox buildHeader() {
                // HBox lays its children left-to-right. Spacing "16" = 16px gap between them.
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
                searchBox.setPrefWidth(300);
                searchBox.setPrefHeight(38);
                searchBox.setStyle(
                                "-fx-background-color: " + BACKGROUND + ";"
                                                + "-fx-background-radius: 20;"
                                                + "-fx-border-color: rgba(11,61,46,0.10);"
                                                + "-fx-border-radius: 20;"
                                                + "-fx-border-width: 1;");
                Label searchIcon = new Label("\uD83D\uDD0D");
                searchIcon.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.5);");
                TextField search = new TextField();
                search.setPromptText("Search projects, schemes, services");
                search.setStyle(
                                "-fx-background-color: transparent;"
                                                + "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-font-size: 12px;"
                                                + "-fx-text-fill: " + FOREST_DEEP + ";"
                                                + "-fx-prompt-text-fill: rgba(11,61,46,0.40);");
                HBox.setHgrow(search, Priority.ALWAYS);
                searchBox.getChildren().addAll(searchIcon, search);

                // An empty Region set to grow fills up leftover space - this is how
                // we "push" the bell + profile to the right edge of the header.
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

                // StackPane layers its children on top of each other (centered by
                // default) - here it's used for the "RP" avatar bubble, matching
                // Sarpanch's forest-green circle + saffron ring look.
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
                VBox nameBox = new VBox(name, role); // stack name above role

                Label chevron = new Label("\u25BE");
                chevron.setStyle("-fx-text-fill: " + TEXT_SECONDARY + ";");

                HBox profile = new HBox(8, avatar, nameBox, chevron); // avatar + name side by side
                profile.setAlignment(Pos.CENTER_LEFT);

                // Finally: put all pieces into the header, left to right.
                header.getChildren().addAll(searchBox, spacer, bellWithBadge, profile);
                return header;
        }

        private ScrollPane buildScrollableContent(Runnable backAction, Runnable onSubmittedGoBack) {
                VBox content = new VBox(18);
                content.setPadding(new Insets(20, 28, 28, 28));
                content.setMaxWidth(720);
                content.setFillWidth(true);

                content.getChildren().addAll(
                                buildPageTitle(),
                                buildForm(onSubmittedGoBack));

                VBox centered = new VBox(content);
                centered.setAlignment(Pos.TOP_CENTER);

                ScrollPane scrollPane = new ScrollPane(centered);
                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(false);
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                scrollPane.setPannable(true);
                scrollPane.setStyle(
                                "-fx-background-color: transparent;"
                                                + "-fx-background-insets: 0;"
                                                + "-fx-padding: 0;");

                return scrollPane;
        }

        private VBox buildPageTitle() {
                Label title = new Label("Register New Complaint");
                title.setStyle("-fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 22px; -fx-font-weight: bold;");

                Label subtitle = new Label("Tell us what's wrong - we'll route it to the right department.");
                subtitle.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 12px;");

                return new VBox(2, title, subtitle);
        }

        // =================================================================
        // FORM: title, location, description, upload photo, submit
        // =================================================================
        private VBox buildForm(Runnable onSubmittedGoBack) {

                VBox card = new VBox(18);
                card.setPadding(new Insets(24));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 14; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 14; -fx-border-width: 1;");

                // ---- Complaint title ----
                Label titleLabel = fieldLabel("Complaint Title");
                titleField = new TextField();
                titleField.setPromptText("e.g. Broken water pipe near temple");
                titleField.setStyle(fieldStyle());
                titleError = errorLabel();
                VBox titleBox = new VBox(6, titleLabel, titleField, titleError);

                // ---- Location area ----
                Label locationLabel = fieldLabel("Location / Area");
                locationField = new TextField();
                locationField.setPromptText("e.g. Temple Square, Zone 4");
                locationField.setStyle(fieldStyle());
                locationError = errorLabel();
                VBox locationBox = new VBox(6, locationLabel, locationField, locationError);

                // ---- Description ----
                Label descriptionLabel = fieldLabel("Description");
                descriptionField = new TextArea();
                descriptionField.setPromptText("Describe the issue in detail...");
                descriptionField.setWrapText(true);
                descriptionField.setPrefRowCount(5);
                descriptionField.setStyle(fieldStyle());
                descriptionError = errorLabel();
                VBox descriptionBox = new VBox(6, descriptionLabel, descriptionField, descriptionError);

                // ---- Upload photo ----
                Label photoLabel = fieldLabel("Upload Photo (optional)");

                Button uploadBtn = new Button("\uD83D\uDCF7  Choose Photo");
                uploadBtn.setStyle(secondaryButtonStyle());

                photoNameLabel = new Label("No file selected");
                photoNameLabel.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 12px;");

                uploadBtn.setOnAction(e -> {
                        FileChooser chooser = new FileChooser();
                        chooser.setTitle("Select a photo");
                        chooser.getExtensionFilters().add(
                                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
                        File file = chooser.showOpenDialog(VillagerDashboard.homeStage);
                        if (file != null) {
                                selectedPhoto = file;
                                photoNameLabel.setText(file.getName());
                                photoNameLabel.setStyle("-fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 12px; -fx-font-weight: 600;");
                        }
                });

                HBox uploadRow = new HBox(12, uploadBtn, photoNameLabel);
                uploadRow.setAlignment(Pos.CENTER_LEFT);
                VBox photoBox = new VBox(6, photoLabel, uploadRow);

                // ---- Submit / Cancel ----
                Button submitBtn = new Button("Submit Complaint");
                submitBtn.setStyle(primaryButtonStyle());

                Button cancelBtn = new Button("Cancel");
                cancelBtn.setStyle(secondaryButtonStyle());
                cancelBtn.setOnAction(e -> onSubmittedGoBack.run());

                submitBtn.setOnAction(e -> handleSubmit(onSubmittedGoBack));

                HBox actionsRow = new HBox(10, submitBtn, cancelBtn);
                actionsRow.setAlignment(Pos.CENTER_LEFT);

                card.getChildren().addAll(titleBox, locationBox, descriptionBox, photoBox, actionsRow);
                return card;
        }

        private void handleSubmit(Runnable onSubmittedGoBack) {
                String title = titleField.getText() == null ? "" : titleField.getText().trim();
                String location = locationField.getText() == null ? "" : locationField.getText().trim();
                String description = descriptionField.getText() == null ? "" : descriptionField.getText().trim();

                boolean valid = true;

                if (title.isEmpty()) {
                        titleError.setText("Complaint title is required.");
                        valid = false;
                } else {
                        titleError.setText("");
                }

                if (location.isEmpty()) {
                        locationError.setText("Location / area is required.");
                        valid = false;
                } else {
                        locationError.setText("");
                }

                if (description.isEmpty()) {
                        descriptionError.setText("A short description is required.");
                        valid = false;
                } else {
                        descriptionError.setText("");
                }

                if (!valid) {
                        return;
                }

                // Save into the shared in-memory store (replace with a real
                // ComplaintService/DB call when the backend exists). Photo
                // upload is captured as a local file reference for now.
                ComplaintsPage.addComplaint(title, location, description);

                Alert confirm = new Alert(AlertType.INFORMATION);
                confirm.setTitle("Complaint Submitted");
                confirm.setHeaderText(null);
                confirm.setContentText("Your complaint has been submitted successfully. "
                                + "You can track its status under \"My Complaints\".");
                confirm.getButtonTypes().setAll(ButtonType.OK);
                confirm.showAndWait();

                onSubmittedGoBack.run();
        }

        // ---- small style helpers ----
        private Label fieldLabel(String text) {
                Label label = new Label(text);
                label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                return label;
        }

        private Label errorLabel() {
                Label label = new Label("");
                label.setStyle("-fx-font-size: 11px; -fx-text-fill: " + ERROR + ";");
                return label;
        }

        private String fieldStyle() {
                return "-fx-background-color: " + BACKGROUND + ";"
                                + "-fx-font-family: " + FONT_FAMILY + ";"
                                + "-fx-font-size: 12px;"
                                + "-fx-text-fill: " + TEXT_PRIMARY + ";"
                                + "-fx-prompt-text-fill: rgba(11,61,46,0.40);"
                                + "-fx-background-radius: 8;"
                                + "-fx-border-color: " + BORDER + ";"
                                + "-fx-border-radius: 8;"
                                + "-fx-border-width: 1;"
                                + "-fx-padding: 8;";
        }

        private String primaryButtonStyle() {
                return "-fx-background-color: " + PRIMARY + ";" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 13px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;" +
                                "-fx-padding: 12 22 12 22;" +
                                "-fx-cursor: hand;";
        }

        private String secondaryButtonStyle() {
                return "-fx-background-color: " + BACKGROUND + ";" +
                                "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                                "-fx-font-size: 12px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;" +
                                "-fx-border-color: " + BORDER + ";" +
                                "-fx-border-width: 1;" +
                                "-fx-border-radius: 8;" +
                                "-fx-padding: 10 18 10 18;" +
                                "-fx-cursor: hand;";
        }
}