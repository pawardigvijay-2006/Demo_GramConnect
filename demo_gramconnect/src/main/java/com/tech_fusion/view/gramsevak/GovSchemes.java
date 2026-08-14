package com.tech_fusion.view.gramsevak;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * GramConnect - Government Schemes page.
 * Redesigned to visually match the Sarpanch Dashboard (SarpanchDashboard.java):
 * same color palette, card style, typography and spacing.
 *
 * The old Scheme Directory TableView has been replaced with a scrollable
 * list of HBox/VBox scheme cards, as requested.
 */
public class GovSchemes {

        // ============================================================
        // Sarpanch Dashboard color palette (copied so this page matches it)
        // ============================================================
        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String FOREST_LIGHT = "#0F4736";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String CONTEXT_TEAL = "#0E8C8C";
        private static final String AI_VIOLET = "#7C5CFC";
        private static final String DELAYED_RED = "#D94C38";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        // ============================================================
        // MAIN ENTRY POINT
        // ============================================================
        public static VBox getSchemeContent(Runnable newSchemeAction) {

                VBox content = new VBox(24);
                content.setPadding(new Insets(32, 40, 48, 40));

                content.getChildren().addAll(
                                buildTitleRow(),
                                buildSummaryCards(),
                                buildMainArea());

                return content;
        }

        // ============================================================
        // TITLE ROW — "Scheme Management" + New Scheme button
        // ============================================================
        private static HBox buildTitleRow() {

                HBox titleRow = new HBox();
                titleRow.setAlignment(Pos.CENTER_LEFT);

                VBox titleBox = new VBox(6);

                Label title = new Label("Scheme Management");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";");

                Label subtitle = new Label("Manage government schemes and citizen eligibility.");
                subtitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: 500;" +
                                                "-fx-text-fill: rgba(11,61,46,0.70);");

                titleBox.getChildren().addAll(title, subtitle);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button newSchemeBtn = new Button("+   New Scheme");
                String base = "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP
                                + ");" +
                                "-fx-background-radius: 12;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: 700;" +
                                "-fx-padding: 12 22 12 22;" +
                                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.35), 10, 0.1, 0, 4);" +
                                "-fx-cursor: hand;";
                newSchemeBtn.setStyle(base);
                newSchemeBtn.setOnMouseEntered(e -> newSchemeBtn.setStyle(base +
                                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.55), 15, 0.2, 0, 5); -fx-translate-y: -1;"));
                newSchemeBtn.setOnMouseExited(e -> newSchemeBtn.setStyle(base));

                newSchemeBtn.setOnAction(e -> {
                               // newSchemeAction.run();
                });

                // Future purpose: open a form to add a new scheme (name, category,
                // eligibility, required documents, description, status, etc.)

                titleRow.getChildren().addAll(titleBox, spacer, newSchemeBtn);
                return titleRow;
        }

        // ============================================================
        // SUMMARY CARDS
        // ============================================================
        private static HBox buildSummaryCards() {

                HBox cards = new HBox(24);

                VBox totalCard = createSummaryCard(FOREST_DEEP, "\uD83D\uDCCB", "TOTAL SCHEMES", "42");
                VBox activeCard = createSummaryCard(CONTEXT_TEAL, "\u2714", "ACTIVE SCHEMES", "36");
                VBox beneficiariesCard = createSummaryCard(AI_VIOLET, "\uD83D\uDC65", "TOTAL BENEFICIARIES", "1,204");
                VBox pendingCard = createSummaryCard(SAFFRON_MAIN, "\u23F3", "PENDING APPLICATIONS", "84");

                HBox.setHgrow(totalCard, Priority.ALWAYS);
                HBox.setHgrow(activeCard, Priority.ALWAYS);
                HBox.setHgrow(beneficiariesCard, Priority.ALWAYS);
                HBox.setHgrow(pendingCard, Priority.ALWAYS);

                cards.getChildren().addAll(totalCard, activeCard, beneficiariesCard, pendingCard);
                return cards;
        }

        private static VBox createSummaryCard(String accent, String icon, String labelText, String statText) {

                VBox card = new VBox();
                card.setMinHeight(150);
                card.setStyle(cardStyle(16));

                Region strip = new Region();
                strip.setPrefHeight(6);
                strip.setStyle("-fx-background-color: " + accent + "; -fx-background-radius: 16 16 0 0;");

                VBox inner = new VBox(16);
                inner.setPadding(new Insets(20, 24, 24, 24));

                HBox head = new HBox(12);
                head.setAlignment(Pos.CENTER_LEFT);

                StackPane iconChip = new StackPane();
                iconChip.setPrefSize(40, 40);
                iconChip.setMinSize(40, 40);
                iconChip.setStyle("-fx-background-color: " + rgba(accent, 0.12) + "; -fx-background-radius: 12;");
                Label ic = new Label(icon);
                ic.setStyle("-fx-font-size: 16px; -fx-text-fill: " + accent + ";");
                iconChip.getChildren().add(ic);

                Label lbl = new Label(labelText);
                lbl.setWrapText(true);
                lbl.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-text-fill: rgba(11,61,46,0.80);" +
                                                "-fx-letter-spacing: 0.06em;");

                head.getChildren().addAll(iconChip, lbl);

                Label stat = new Label(statText);
                stat.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 34px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";");

                inner.getChildren().addAll(head, stat);
                card.getChildren().addAll(strip, inner);

                return card;
        }

        // ============================================================
        // MAIN AREA — Scheme Directory (left, wide) + Check Eligibility /
        // Recent Approvals (right, narrower)
        // ============================================================
        private static HBox buildMainArea() {

                HBox mainArea = new HBox(24);

                VBox schemeDirectory = buildSchemeDirectory();
                HBox.setHgrow(schemeDirectory, Priority.ALWAYS);

                VBox rightSide = new VBox(24);
                rightSide.setPrefWidth(340);
                rightSide.setMinWidth(300);

                rightSide.getChildren().addAll(
                                buildCheckEligibility(),
                                buildRecentApprovals());

                mainArea.getChildren().addAll(schemeDirectory, rightSide);
                return mainArea;
        }

        // ============================================================
        // SCHEME DIRECTORY (replaces the old TableView)
        // ============================================================
        private static VBox buildSchemeDirectory() {

                VBox card = new VBox(20);
                card.setPadding(new Insets(32));
                card.setStyle(cardStyle(24));

                // ----- Header: icon + title + category filter -----
                HBox header = new HBox(12);
                header.setAlignment(Pos.CENTER_LEFT);

                StackPane iconChip = new StackPane();
                iconChip.setPrefSize(40, 40);
                iconChip.setMinSize(40, 40);
                iconChip.setStyle("-fx-background-color: rgba(11,61,46,0.10); -fx-background-radius: 999;");
                Label headIcon = new Label("\uD83D\uDCCB");
                headIcon.setStyle("-fx-font-size: 16px; -fx-text-fill: " + FOREST_DEEP + ";");
                iconChip.getChildren().add(headIcon);

                Label headerTitle = new Label("Scheme Directory");
                headerTitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";");

                Region headSpacer = new Region();
                HBox.setHgrow(headSpacer, Priority.ALWAYS);

                ComboBox<String> categoryComboBox = new ComboBox<>();
                categoryComboBox.getItems().addAll(
                                "All Categories",
                                "Agriculture",
                                "Infrastructure",
                                "Education",
                                "Women & Child");
                categoryComboBox.setValue("All Categories");
                styleComboBox(categoryComboBox);

                header.getChildren().addAll(iconChip, headerTitle, headSpacer, categoryComboBox);

                // ----- Scheme card list -----
                VBox schemeList = new VBox(14);

                /*
                 * ============================================================
                 * DATABASE / API CONNECTION POINT
                 * ============================================================
                 * The sample cards below are placeholders only, so the page has
                 * something to show right now.
                 *
                 * When the backend is ready:
                 * 1. Fetch the real schemes, e.g.:
                 * List<Scheme> schemeData = SchemeService.getAllSchemes();
                 * 2. Clear the placeholder cards:
                 * schemeList.getChildren().clear();
                 * 3. Loop through schemeData and build a card for each one:
                 * for (Scheme scheme : schemeData) {
                 * schemeList.getChildren().add(
                 * createSchemeCard(
                 * scheme.getName(),
                 * scheme.getCategory(),
                 * scheme.getStatus(),
                 * String.valueOf(scheme.getApplications())
                 * )
                 * );
                 * }
                 *
                 * The "View Details" button inside createSchemeCard(...) is also
                 * where you will later attach a setOnAction(...) handler to open
                 * the full details of that specific scheme.
                 * ============================================================
                 */
                schemeList.getChildren().addAll(
                                createSchemeCard("PM-KISAN", "Agriculture", "Active", "128"),
                                createSchemeCard("PMAY-Gramin", "Infrastructure", "Active", "96"),
                                createSchemeCard("Beti Bachao Beti Padhao", "Women & Child", "Active", "74"),
                                createSchemeCard("Scholarship Scheme", "Education", "Active", "156"),
                                createSchemeCard("Rural Housing Scheme", "Infrastructure", "Active", "82"),
                                createSchemeCard("Women Empowerment Scheme", "Women & Child", "Active", "63"),
                                createSchemeCard("Farmer Support Scheme", "Agriculture", "Inactive", "45"));

                ScrollPane scrollPane = new ScrollPane(schemeList);
                scrollPane.setFitToWidth(true);
                scrollPane.setPrefHeight(460);
                scrollPane.setStyle(
                                "-fx-background: transparent;" +
                                                "-fx-background-color: transparent;" +
                                                "-fx-border-color: transparent;");

                card.getChildren().addAll(header, scrollPane);
                return card;
        }

        /**
         * Builds one scheme card for the Scheme Directory.
         * Replaces a single TableView row.
         */
        private static VBox createSchemeCard(String schemeName, String category, String status, String applications) {

                VBox card = new VBox(10);
                card.setPadding(new Insets(18, 20, 18, 20));
                card.setStyle(
                                "-fx-background-color: rgba(255,255,255,0.7);" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: rgba(11,61,46,0.08);" +
                                                "-fx-border-radius: 14;" +
                                                "-fx-border-width: 1;");

                // ----- Top row: scheme name + status pill -----
                HBox topRow = new HBox();
                topRow.setAlignment(Pos.CENTER_LEFT);

                Label nameLbl = new Label(schemeName);
                nameLbl.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";");

                Region topSpacer = new Region();
                HBox.setHgrow(topSpacer, Priority.ALWAYS);

                String statusColor = "Active".equals(status) ? CONTEXT_TEAL : DELAYED_RED;
                HBox pill = new HBox(6);
                pill.setAlignment(Pos.CENTER);
                pill.setPadding(new Insets(6, 14, 6, 14));
                pill.setMaxWidth(Region.USE_PREF_SIZE);
                pill.setStyle("-fx-background-color: " + statusColor + "; -fx-background-radius: 999;");
                Circle pillDot = new Circle(3, Color.WHITE);
                Label pillLbl = new Label(status);
                pillLbl.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-text-fill: white;");
                pill.getChildren().addAll(pillDot, pillLbl);

                topRow.getChildren().addAll(nameLbl, topSpacer, pill);

                // ----- Category -----
                Label categoryLbl = new Label(category);
                categoryLbl.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: 500;" +
                                                "-fx-text-fill: rgba(11,61,46,0.65);");

                // ----- Bottom row: applications count + View Details button -----
                HBox bottomRow = new HBox();
                bottomRow.setAlignment(Pos.CENTER_LEFT);

                Label applicationsLbl = new Label("Applications: " + applications);
                applicationsLbl.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: 600;" +
                                                "-fx-text-fill: rgba(11,61,46,0.70);");

                Region bottomSpacer = new Region();
                HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

                Button viewDetailsBtn = new Button("View Details");
                styleSmallButton(viewDetailsBtn, "rgba(11,61,46,0.10)", FOREST_DEEP);

                bottomRow.getChildren().addAll(applicationsLbl, bottomSpacer, viewDetailsBtn);

                card.getChildren().addAll(topRow, categoryLbl, bottomRow);
                return card;
        }

        // ============================================================
        // CHECK ELIGIBILITY
        // ============================================================
        private static VBox buildCheckEligibility() {

                VBox card = new VBox(16);
                card.setPadding(new Insets(28));
                card.setStyle(cardStyle(24));

                Label title = new Label("Check Eligibility");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";");

                Label description = new Label("Verify a citizen's eligibility for a scheme using their Aadhaar ID.");
                description.setWrapText(true);
                description.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: 500;" +
                                                "-fx-text-fill: rgba(11,61,46,0.65);");

                TextField aadhaarField = new TextField();
                aadhaarField.setPromptText("Enter Aadhaar ID");
                styleTextField(aadhaarField);

                ComboBox<String> schemeCategoryComboBox = new ComboBox<>();
                schemeCategoryComboBox.getItems().addAll(
                                "Agriculture",
                                "Infrastructure",
                                "Education",
                                "Women & Child");
                schemeCategoryComboBox.setValue("Agriculture");
                schemeCategoryComboBox.setMaxWidth(Double.MAX_VALUE);
                styleComboBox(schemeCategoryComboBox);

                Button verifyBtn = new Button("Verify Citizen");
                verifyBtn.setMaxWidth(Double.MAX_VALUE);
                String base = "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP
                                + ");" +
                                "-fx-background-radius: 10;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                "-fx-font-size: 13px;" +
                                "-fx-font-weight: 700;" +
                                "-fx-padding: 10 16 10 16;" +
                                "-fx-cursor: hand;";
                verifyBtn.setStyle(base);
                verifyBtn.setOnMouseEntered(e -> verifyBtn.setStyle(base + "-fx-opacity: 0.9;"));
                verifyBtn.setOnMouseExited(e -> verifyBtn.setStyle(base));

                // Actual Aadhaar verification will be wired up later.
                verifyBtn.setOnAction(e -> System.out.println("Verify Citizen clicked"));

                card.getChildren().addAll(title, description, aadhaarField, schemeCategoryComboBox, verifyBtn);
                return card;
        }

        // ============================================================
        // RECENT APPROVALS
        // ============================================================
        private static VBox buildRecentApprovals() {

                VBox card = new VBox(16);
                card.setPadding(new Insets(28));
                card.setStyle(cardStyle(24));

                Label title = new Label("Recent Approvals");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";");

                VBox approvalList = new VBox(10);

                HBox approval1 = createApproval("Ramesh Kumar", "PM-KISAN");
                HBox approval2 = createApproval("Anita Sharma", "Scholarship Scheme");
                HBox approval3 = createApproval("Vikram Singh", "PMAY-Gramin");

                approvalList.getChildren().addAll(approval1, approval2, approval3);

                card.getChildren().addAll(title, approvalList);
                return card;
        }

        /**
         * Builds one recent-approval row: citizen name + scheme name + Approved pill.
         */
        private static HBox createApproval(String citizenName, String schemeName) {

                HBox row = new HBox(12);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(12, 14, 12, 14));
                row.setStyle(
                                "-fx-background-color: rgba(255,255,255,0.6);" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-border-color: rgba(255,255,255,0.7);" +
                                                "-fx-border-radius: 12;" +
                                                "-fx-border-width: 1;");

                VBox details = new VBox(2);
                Label nameLbl = new Label(citizenName);
                nameLbl.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";");
                Label schemeLbl = new Label(schemeName);
                schemeLbl.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 500;" +
                                                "-fx-text-fill: rgba(11,61,46,0.65);");
                details.getChildren().addAll(nameLbl, schemeLbl);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox pill = new HBox(6);
                pill.setAlignment(Pos.CENTER);
                pill.setPadding(new Insets(5, 12, 5, 12));
                pill.setMaxWidth(Region.USE_PREF_SIZE);
                pill.setStyle("-fx-background-color: " + CONTEXT_TEAL + "; -fx-background-radius: 999;");
                Circle pillDot = new Circle(3, Color.WHITE);
                Label pillLbl = new Label("Approved");
                pillLbl.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-text-fill: white;");
                pill.getChildren().addAll(pillDot, pillLbl);

                row.getChildren().addAll(details, spacer, pill);
                return row;
        }

        // ============================================================
        // Small style helpers
        // ============================================================
        private static void styleSmallButton(Button button, String bgColor, String textColor) {
                String base = "-fx-background-color: " + bgColor + ";" +
                                "-fx-background-radius: 8;" +
                                "-fx-text-fill: " + textColor + ";" +
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                "-fx-font-size: 12px;" +
                                "-fx-font-weight: 700;" +
                                "-fx-padding: 8 14 8 14;" +
                                "-fx-cursor: hand;";
                button.setStyle(base);
                button.setOnMouseEntered(e -> button.setStyle(base + "-fx-opacity: 0.85;"));
                button.setOnMouseExited(e -> button.setStyle(base));
        }

        private static void styleComboBox(ComboBox<String> comboBox) {
                comboBox.setStyle(
                                "-fx-background-color: rgba(255,255,255,0.7);" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: rgba(11,61,46,0.10);" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;");
        }

        private static void styleTextField(TextField textField) {
                textField.setStyle(
                                "-fx-background-color: rgba(255,255,255,0.7);" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: rgba(11,61,46,0.10);" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";" +
                                                "-fx-padding: 10 12 10 12;");
        }

        /** Same glass-panel card style used across the Sarpanch Dashboard. */
        private static String cardStyle(int radius) {
                return "-fx-background-color: rgba(255,255,255,0.88);" +
                                "-fx-background-radius: " + radius + ";" +
                                "-fx-border-color: rgba(255,255,255,0.5);" +
                                "-fx-border-radius: " + radius + ";" +
                                "-fx-border-width: 1;" +
                                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.06), 16, 0.1, 0, 4);";
        }

        /** Convert #RRGGBB hex to an rgba(r,g,b,a) CSS string. */
        private static String rgba(String hex, double alpha) {
                int r = Integer.parseInt(hex.substring(1, 3), 16);
                int g = Integer.parseInt(hex.substring(3, 5), 16);
                int b = Integer.parseInt(hex.substring(5, 7), 16);
                return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
        }
}
