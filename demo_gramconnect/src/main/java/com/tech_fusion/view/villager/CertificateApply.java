package com.tech_fusion.view.villager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.util.Random;

/**
 * GramConnect - Certificate Application
 *
 * ============================================================
 * NAVIGATION NOTE
 * ============================================================
 * Same pattern as every other page: owns its own sidebar + header,
 * hands back a full Scene, reached from one of Certificates.java's
 * "Apply Now" buttons with
 *   homeStage.setScene(new CertificateApply().getApplyScene(
 *           backToCertificatesAction, "Residential Certificate", "..."));
 * The breadcrumb's "Certificates" crumb, the "Back to Certificates"
 * button, AND the confirmation popup's OK button all call
 * backToCertificatesAction.run() to return - this class never
 * touches Stage or Certificates directly.
 *
 * This one form is reused for all 4 certificate types (Residential,
 * Birth, Death, Marriage) - only the certificate name/description
 * shown in the breadcrumb, title, and "About" card change per type,
 * since the applicant-info + document-upload fields are the same
 * shape for all of them in this mock. A real build would likely swap
 * in a type-specific field set from CertificateService.getFormFields(type).
 *
 * ARCHITECTURE NOTE: "Upload File" opens a real javafx.stage.FileChooser
 * and just remembers the chosen file's name (no actual upload yet) -
 * marked with a TODO for CertificateService.uploadDocument(...). The
 * final "Preview & Submit" click validates the mandatory fields, then
 * shows a confirmation popup with a mock application ID - marked with
 * a TODO for CertificateService.submitApplication(...).
 * ============================================================
 */
public class CertificateApply {

        // ================= COLORS (same palette as Certificates.java) =================
        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String DELAYED_RED = "#D94C38";
        private static final String CONTEXT_TEAL = "#0E8C8C";

        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        private static final String LIGHT_GREEN = "#E8F7EA";
        private static final String LIGHT_AMBER = "#FEF3C7";
        private static final String AMBER = "#D97706";

        private static final String BACKGROUND = "#EFF5F1";
        private static final String TEXT_PRIMARY = FOREST_DEEP;
        private static final String TEXT_SECONDARY = "rgba(11,61,46,0.65)";
        private static final String BORDER = "#D8E2DC";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        // Kept as fields so validation/reset/submit can reach them without
        // threading a dozen parameters through every helper method.
        private TextField fullNameField;
        private TextField fatherNameField;
        private DatePicker dobPicker;
        private ComboBox<String> genderBox;
        private TextField mobileField;
        private TextField emailField;
        private TextArea addressArea;
        private TextField aadhaarField;
        private TextField occupationField;
        private TextField incomeField;
        private Label docStatusAadhaar;
        private Label docStatusAddress;
        private Label docStatusPhoto;
        private Label docStatusOther;
        private Label validationError;

        /**
         * Builds this page's full Scene.
         *
         * @param backToCertificatesAction what to run to return to the
         *                                  Certificates list - used by the
         *                                  breadcrumb, "Back to Certificates",
         *                                  and automatically after a
         *                                  successful submit.
         * @param certificateName           e.g. "Residential Certificate"
         * @param certificateDescription    one-line description shown in the
         *                                   "About" card
         */
        public Scene getApplyScene(Runnable backToCertificatesAction, String certificateName,
                        String certificateDescription) {
                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BACKGROUND + ";");

                root.setLeft(buildSidebar(backToCertificatesAction));
                root.setCenter(buildMainArea(backToCertificatesAction, certificateName, certificateDescription));

                return new Scene(root, 1500, 850);
        }

        // =================================================================
        // SIDEBAR ("Certificates" stays active since this page lives under
        // that section)
        // =================================================================
        private VBox buildSidebar(Runnable backToCertificatesAction) {
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

                Label projectsNav = navItem("\uD83C\uDFD7  Project Transparency", false);
                projectsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new ProjectTransparency().getProjectScene(backToCertificatesAction));
                });

                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", false);
                complaintsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new ComplaintsPage().getComplaintsPage(backToCertificatesAction));
                });
                Label schemesNav = navItem("\uD83C\uDF81  Government Schemes", false);
                schemesNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new GovernmentSchemes().getSchemesScene(backToCertificatesAction));
                });
                Label certificatesNav = navItem("\uD83D\uDCDC  Certificates", true);
                certificatesNav.setOnMouseClicked(e -> backToCertificatesAction.run());

                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", false);
                billsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new BillsAndPayments().getBillsScene(backToCertificatesAction));
                });
                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                announcementsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new Announcements().getAnnouncementScene(backToCertificatesAction));
                });
                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", false);
                gramSabhaNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new GramSabha().getGramSabhaScene(backToCertificatesAction));
                });
                Label aiAssistantNav = navItem("\uD83E\uDD16  AI Village Assistant", false);
                aiAssistantNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage
                                        .setScene(new AIAssistant().getAiAssiatantScene(backToCertificatesAction));
                });

                VBox navItems = new VBox(4,
                                dashboardNav, projectsNav, complaintsNav, schemesNav,
                                certificatesNav, billsNav, announcementsNav, gramSabhaNav, aiAssistantNav);
                navItems.setPadding(new Insets(0, 10, 0, 10));
                VBox.setVgrow(navItems, Priority.ALWAYS);

                Label emergency = new Label("\u26A0  Emergency Assistance");
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
        private BorderPane buildMainArea(Runnable backToCertificatesAction, String certificateName,
                        String certificateDescription) {
                BorderPane main = new BorderPane();
                main.setTop(buildHeader());
                main.setCenter(buildScrollableContent(backToCertificatesAction, certificateName, certificateDescription));
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

        private ScrollPane buildScrollableContent(Runnable backToCertificatesAction, String certificateName,
                        String certificateDescription) {
                VBox content = new VBox(18);
                content.setPadding(new Insets(20, 26, 30, 26));

                content.getChildren().addAll(
                                buildBreadcrumbRow(backToCertificatesAction, certificateName),
                                buildTitleRow(backToCertificatesAction, certificateName),
                                buildMainRow(backToCertificatesAction, certificateName, certificateDescription));

                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent;");
                return scrollPane;
        }

        // ---- Breadcrumb ----
        private HBox buildBreadcrumbRow(Runnable backToCertificatesAction, String certificateName) {
                Label crumbCertificates = new Label("Certificates");
                crumbCertificates.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + CONTEXT_TEAL + "; -fx-cursor: hand;");
                crumbCertificates.setOnMouseClicked(e -> backToCertificatesAction.run());

                Label sep1 = new Label("\u203A");
                sep1.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                Label crumbType = new Label(certificateName);
                crumbType.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                + TEXT_SECONDARY + ";");

                Label sep2 = new Label("\u203A");
                sep2.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                Label crumbApply = new Label("Apply");
                crumbApply.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 700; "
                                                + "-fx-text-fill: " + TEXT_PRIMARY + ";");

                HBox row = new HBox(6, crumbCertificates, sep1, crumbType, sep2, crumbApply);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- Title row ----
        private HBox buildTitleRow(Runnable backToCertificatesAction, String certificateName) {
                Label title = new Label("Apply for " + certificateName);
                title.setStyle("-fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 24px; -fx-font-weight: bold;");
                Label subtitle = new Label("Fill in the details below and upload the required documents to apply.");
                subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                VBox titleBox = new VBox(4, title, subtitle);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button backBtn = new Button("\u2190  Back to Certificates");
                backBtn.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                                                "-fx-border-color: " + BORDER + ";" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-padding: 10 16 10 16;" +
                                                "-fx-cursor: hand;");
                backBtn.setOnAction(e -> backToCertificatesAction.run());

                HBox row = new HBox(titleBox, spacer, backBtn);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- Main row: form (left) + info cards (right) ----
        private HBox buildMainRow(Runnable backToCertificatesAction, String certificateName,
                        String certificateDescription) {
                HBox row = new HBox(18,
                                buildFormColumn(backToCertificatesAction),
                                buildInfoColumn(certificateName, certificateDescription));
                return row;
        }

        // =================================================================
        // LEFT: Applicant Information + Required Documents + actions
        // =================================================================
        private VBox buildFormColumn(Runnable backToCertificatesAction) {
                VBox applicantCard = buildApplicantInformationCard();
                VBox documentsCard = buildRequiredDocumentsCard();
                HBox actionsRow = buildActionsRow(backToCertificatesAction);

                VBox column = new VBox(18, applicantCard, documentsCard, actionsRow);
                HBox.setHgrow(column, Priority.ALWAYS);
                return column;
        }

        private VBox buildApplicantInformationCard() {
                Label sectionTitle = sectionHeader("\uD83D\uDC64", "Applicant Information");

                fullNameField = new TextField();
                fullNameField.setStyle(fieldStyle());
                fatherNameField = new TextField();
                fatherNameField.setStyle(fieldStyle());

                dobPicker = new DatePicker();
                dobPicker.setPromptText("dd/mm/yyyy");
                dobPicker.setMaxWidth(Double.MAX_VALUE);
                dobPicker.setStyle(fieldStyle());

                genderBox = new ComboBox<>();
                genderBox.getItems().addAll("Male", "Female", "Other");
                genderBox.setPromptText("Select gender");
                genderBox.setMaxWidth(Double.MAX_VALUE);
                genderBox.setStyle(fieldStyle());

                mobileField = new TextField();
                mobileField.setStyle(fieldStyle());
                emailField = new TextField();
                emailField.setStyle(fieldStyle());

                Label addressLabel = requiredLabel("Address");
                addressArea = new TextArea();
                addressArea.setPromptText("House no., street, village, taluka, district, PIN");
                addressArea.setWrapText(true);
                addressArea.setPrefRowCount(2);
                addressArea.setStyle(fieldStyle());
                VBox addressBox = new VBox(6, addressLabel, addressArea);

                aadhaarField = new TextField();
                aadhaarField.setStyle(fieldStyle());
                occupationField = new TextField();
                occupationField.setStyle(fieldStyle());
                incomeField = new TextField();
                incomeField.setStyle(fieldStyle());

                GridPane row1 = threeColumnRow(
                                fieldBox("Full Name", fullNameField, true),
                                fieldBox("Father's / Husband's Name", fatherNameField, true),
                                fieldBox("Date of Birth", dobPicker, true));
                GridPane row2 = threeColumnRow(
                                fieldBox("Gender", genderBox, true),
                                fieldBox("Mobile Number", mobileField, true),
                                fieldBox("Email (Optional)", emailField, false));
                GridPane row3 = threeColumnRow(
                                fieldBox("Aadhaar Number", aadhaarField, true),
                                fieldBox("Occupation", occupationField, true),
                                fieldBox("Annual Income (\u20B9)", incomeField, true));

                VBox card = new VBox(18, sectionTitle, row1, row2, addressBox, row3);
                card.setPadding(new Insets(22));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return card;
        }

        private VBox buildRequiredDocumentsCard() {
                Label sectionTitle = sectionHeader("\uD83D\uDCCE", "Required Documents");
                Label hint = new Label("Please upload clear and valid documents.");
                hint.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                docStatusAadhaar = new Label();
                docStatusAddress = new Label();
                docStatusPhoto = new Label();
                docStatusOther = new Label();

                VBox rows = new VBox(12,
                                documentRow("Aadhaar Card", "Upload front side of Aadhaar card", true,
                                                "JPG, PNG, PDF (Max. 2MB)", docStatusAadhaar),
                                documentRow("Address Proof", "Ration Card / Voter ID / Electricity Bill", true,
                                                "JPG, PNG, PDF (Max. 2MB)", docStatusAddress),
                                documentRow("Passport Size Photo", "Recent passport size color photo", true,
                                                "JPG, PNG (Max. 2MB)", docStatusPhoto),
                                documentRow("Other Supporting Document (If any)", "Any additional document", false,
                                                "JPG, PNG, PDF (Max. 2MB)", docStatusOther));

                VBox card = new VBox(14, sectionTitle, hint, rows);
                card.setPadding(new Insets(22));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return card;
        }

        private HBox documentRow(String title, String description, boolean required, String formats,
                        Label statusLabel) {
                Label iconLabel = new Label("\uD83D\uDCC4");
                iconLabel.setStyle("-fx-text-fill: " + FOREST_DEEP + "; -fx-font-size: 14px;");
                StackPane iconChip = new StackPane(iconLabel);
                iconChip.setPrefSize(34, 34);
                iconChip.setMaxSize(34, 34);
                iconChip.setStyle("-fx-background-color: " + LIGHT_GREEN + "; -fx-background-radius: 9;");

                Label titleLabel = new Label(title + (required ? " *" : ""));
                titleLabel.setWrapText(true);
                titleLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label descLabel = new Label(description);
                descLabel.setWrapText(true);
                descLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                statusLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: " + FOREST_DEEP + "; -fx-font-weight: bold;");

                VBox textBox = new VBox(2, titleLabel, descLabel, statusLabel);
                HBox.setHgrow(textBox, Priority.ALWAYS);

                Button uploadBtn = new Button("\uD83D\uDCC1  Upload File");
                uploadBtn.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                                                "-fx-border-color: " + BORDER + ";" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-padding: 8 14 8 14;" +
                                                "-fx-cursor: hand;");
                uploadBtn.setOnAction(e -> {
                        // TODO: replace with CertificateService.uploadDocument(applicationId, file)
                        FileChooser chooser = new FileChooser();
                        chooser.setTitle("Select " + title);
                        chooser.getExtensionFilters().add(
                                        new FileChooser.ExtensionFilter("Documents", "*.jpg", "*.jpeg", "*.png",
                                                        "*.pdf"));
                        java.io.File file = chooser.showOpenDialog(uploadBtn.getScene().getWindow());
                        if (file != null) {
                                statusLabel.setText("\u2713  " + file.getName());
                        }
                });

                Label formatsLabel = new Label(formats);
                formatsLabel.setWrapText(true);
                formatsLabel.setMaxWidth(150);
                formatsLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox uploadBox = new VBox(4, uploadBtn, formatsLabel);
                uploadBox.setAlignment(Pos.CENTER_RIGHT);

                HBox row = new HBox(12, iconChip, textBox, uploadBox);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- Reset / Preview & Submit actions ----
        private HBox buildActionsRow(Runnable backToCertificatesAction) {
                Label mandatoryNote = new Label("* Mandatory fields");
                mandatoryNote.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                validationError = new Label();
                validationError.setStyle("-fx-font-size: 11px; -fx-text-fill: " + DELAYED_RED + "; -fx-font-weight: bold;");
                validationError.setVisible(false);
                validationError.setManaged(false);

                VBox leftBox = new VBox(4, mandatoryNote, validationError);
                HBox.setHgrow(leftBox, Priority.ALWAYS);

                Button resetBtn = new Button("\u21BB  Reset");
                resetBtn.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                                                "-fx-border-color: " + BORDER + ";" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-padding: 11 20 11 20;" +
                                                "-fx-cursor: hand;");
                resetBtn.setOnAction(e -> resetForm());

                Button submitBtn = new Button("Preview & Submit  \u2192");
                submitBtn.setStyle(
                                "-fx-background-color: " + FOREST_DEEP + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 11 22 11 22;" +
                                                "-fx-cursor: hand;");
                submitBtn.setOnAction(e -> submitApplication(backToCertificatesAction));

                HBox row = new HBox(12, leftBox, resetBtn, submitBtn);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // =================================================================
        // RIGHT: About / Documents Guidelines / How it works / Important Note
        // =================================================================
        private VBox buildInfoColumn(String certificateName, String certificateDescription) {
                VBox column = new VBox(16,
                                buildAboutCard(certificateName, certificateDescription),
                                buildGuidelinesCard(),
                                buildHowItWorksCard(),
                                buildImportantNoteCard());
                column.setPrefWidth(340);
                column.setMinWidth(340);
                return column;
        }

        private VBox buildAboutCard(String certificateName, String certificateDescription) {
                Label title = sectionHeader("\uD83C\uDFE0", "About " + certificateName);
                Label body = new Label(certificateDescription);
                body.setWrapText(true);
                body.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox card = new VBox(10, title, body);
                card.setPadding(new Insets(18));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return card;
        }

        private VBox buildGuidelinesCard() {
                Label title = sectionHeader("\uD83D\uDCCB", "Documents Guidelines");

                String[] guidelines = {
                                "All documents must be clear and readable",
                                "File size should be less than 2MB",
                                "Accepted formats: JPG, PNG, PDF",
                                "Document should be self-attested"
                };

                VBox list = new VBox(10);
                for (String g : guidelines) {
                        list.getChildren().add(checklistRow(g, FOREST_DEEP));
                }

                VBox card = new VBox(12, title, list);
                card.setPadding(new Insets(18));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return card;
        }

        private HBox checklistRow(String text, String iconColor) {
                Label icon = new Label("\uD83D\uDFE9");
                icon.setStyle("-fx-text-fill: " + iconColor + "; -fx-font-size: 10px;");
                Label label = new Label(text);
                label.setWrapText(true);
                label.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_PRIMARY + ";");
                HBox row = new HBox(8, icon, label);
                row.setAlignment(Pos.TOP_LEFT);
                return row;
        }

        private VBox buildHowItWorksCard() {
                Label title = sectionHeader("\u2753", "How it works?");

                VBox steps = new VBox(16,
                                howItWorksStep("1", "Fill Application", "Enter your details and upload documents"),
                                howItWorksStep("2", "Submit Application", "Review and submit your application"),
                                howItWorksStep("3", "Verification", "Application will be verified by authorities"),
                                howItWorksStep("4", "Receive Certificate", "Download your certificate once approved"));

                VBox card = new VBox(14, title, steps);
                card.setPadding(new Insets(18));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return card;
        }

        private HBox howItWorksStep(String number, String title, String description) {
                Label numberLabel = new Label(number);
                numberLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + FOREST_DEEP + ";");
                StackPane numberCircle = new StackPane(numberLabel);
                numberCircle.setPrefSize(28, 28);
                numberCircle.setMaxSize(28, 28);
                numberCircle.setStyle("-fx-background-color: " + LIGHT_GREEN + "; -fx-background-radius: 14;");

                Label titleLabel = new Label(title);
                titleLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label descLabel = new Label(description);
                descLabel.setWrapText(true);
                descLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                VBox textBox = new VBox(2, titleLabel, descLabel);

                HBox row = new HBox(10, numberCircle, textBox);
                row.setAlignment(Pos.TOP_LEFT);
                return row;
        }

        private VBox buildImportantNoteCard() {
                Label title = sectionHeader("\uD83D\uDCA1", "Important Note");

                VBox list = new VBox(8,
                                checklistRow("Ensure all information is correct before submission.", FOREST_DEEP),
                                checklistRow("You will be notified about the status via SMS.", FOREST_DEEP));

                VBox card = new VBox(10, title, list);
                card.setPadding(new Insets(18));
                card.setStyle("-fx-background-color: " + LIGHT_AMBER + "; -fx-background-radius: 12; "
                                + "-fx-border-color: " + AMBER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                return card;
        }

        // =================================================================
        // Small shared helpers for the form
        // =================================================================
        private Label sectionHeader(String icon, String text) {
                Label label = new Label(icon + "  " + text);
                label.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                return label;
        }

        private Label requiredLabel(String text) {
                Label label = new Label(text + " *");
                label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_SECONDARY + ";");
                return label;
        }

        private String fieldStyle() {
                return "-fx-background-color: " + BACKGROUND + "; -fx-background-radius: 8; "
                                + "-fx-padding: 8; -fx-font-size: 12px;";
        }

        private VBox fieldBox(String labelText, javafx.scene.control.Control control, boolean required) {
                Label label = required ? requiredLabel(labelText) : new Label(labelText);
                if (!required) {
                        label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_SECONDARY + ";");
                }
                control.setMaxWidth(Double.MAX_VALUE);
                return new VBox(6, label, control);
        }

        /** Lays 3 already-built field boxes into one evenly-spaced row. */
        private GridPane threeColumnRow(Region col1, Region col2, Region col3) {
                GridPane grid = new GridPane();
                grid.setHgap(24);
                for (int i = 0; i < 3; i++) {
                        ColumnConstraints cc = new ColumnConstraints();
                        cc.setPercentWidth(33.3);
                        grid.getColumnConstraints().add(cc);
                }
                col1.setMaxWidth(Double.MAX_VALUE);
                col2.setMaxWidth(Double.MAX_VALUE);
                col3.setMaxWidth(Double.MAX_VALUE);
                grid.add(col1, 0, 0);
                grid.add(col2, 1, 0);
                grid.add(col3, 2, 0);
                return grid;
        }

        private void resetForm() {
                fullNameField.clear();
                fatherNameField.clear();
                dobPicker.setValue(null);
                genderBox.setValue(null);
                mobileField.clear();
                emailField.clear();
                addressArea.clear();
                aadhaarField.clear();
                occupationField.clear();
                incomeField.clear();
                docStatusAadhaar.setText("");
                docStatusAddress.setText("");
                docStatusPhoto.setText("");
                docStatusOther.setText("");
                validationError.setVisible(false);
                validationError.setManaged(false);
        }

        /**
         * Validates the mandatory fields, then shows the "Application Submitted"
         * confirmation popup and returns to Certificates once it's dismissed.
         */
        private void submitApplication(Runnable backToCertificatesAction) {
                boolean missingRequired =
                                fullNameField.getText() == null || fullNameField.getText().isBlank()
                                                || fatherNameField.getText() == null || fatherNameField.getText().isBlank()
                                                || dobPicker.getValue() == null
                                                || genderBox.getValue() == null
                                                || mobileField.getText() == null || mobileField.getText().isBlank()
                                                || addressArea.getText() == null || addressArea.getText().isBlank()
                                                || aadhaarField.getText() == null || aadhaarField.getText().isBlank()
                                                || occupationField.getText() == null || occupationField.getText().isBlank()
                                                || incomeField.getText() == null || incomeField.getText().isBlank();

                if (missingRequired) {
                        validationError.setText("Please fill in all mandatory fields marked with *.");
                        validationError.setVisible(true);
                        validationError.setManaged(true);
                        return;
                }

                validationError.setVisible(false);
                validationError.setManaged(false);

                // TODO: replace with CertificateService.submitApplication(applicant, documents)
                String applicationId = "GC/2026/" + (100000 + new Random().nextInt(899999));

                Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                confirmation.setTitle("GramConnect");
                confirmation.setHeaderText("Application Submitted Successfully!");
                confirmation.setContentText(
                                "Your application ID is " + applicationId + ". "
                                                + "You can track its status from 'My Applications' on the "
                                                + "Certificates page, and you'll be notified via SMS once it's reviewed.");
                confirmation.getButtonTypes().setAll(ButtonType.OK);
                confirmation.showAndWait();

                backToCertificatesAction.run();
        }
}
