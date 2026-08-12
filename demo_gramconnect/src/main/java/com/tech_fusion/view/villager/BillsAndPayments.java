package com.tech_fusion.view.villager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;

/**
 * GramConnect - Bills & Payments (Villager)
 *
 * ============================================================
 * Follows the same visual language as VillagerDashboard.java /
 * SarpanchDashboard.java: forest green + saffron accent palette,
 * translucent "glass card" panels with soft drop-shadow, rounded
 * pill status badges and a hover-lift effect on cards.
 *
 * Scope (per request): only the "My Bills" section restricted to
 * Water Bill + Property Tax, plus a "Payment History" section
 * listing recently paid bills with a download-receipt action.
 * Quick Actions / promo banner / Electricity / House Tax cards
 * from the reference screenshot are intentionally left out.
 *
 * Usage (same pattern as ProjectTransparency.getProjectBPane() and
 * ComplaintsPage.getComplaintsPage()):
 *
 *      root.setCenter(new BillsAndPayments().getBillsPane());
 * ============================================================
 */
public class BillsAndPayments {

        // ================= COLORS (shared with VillagerDashboard.java) =================
        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String FOREST_LIGHT = "#0F4736";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String CONTEXT_TEAL = "#0E8C8C";
        private static final String DELAYED_RED = "#D94C38";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        private static final String BACKGROUND = "#EFF5F1";

        private static final String TEXT_PRIMARY = FOREST_DEEP;
        private static final String TEXT_SECONDARY = "rgba(11,61,46,0.65)";

        /** Public entry point - returns the fully built Bills & Payments screen. */
        public BorderPane getBillsPane() {
                BorderPane main = new BorderPane();
                main.setStyle("-fx-background-color: " + BACKGROUND + ";");
                main.setTop(buildHeader());
                main.setCenter(buildScrollableContent());
                return main;
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

        private ScrollPane buildScrollableContent() {

                Label title = new Label("Bills & Payments");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 26px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                Label subtitle = new Label("Pay your bills securely and on time.");
                subtitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox heading = new VBox(4, title, subtitle);
                heading.setPadding(new Insets(24, 32, 0, 32));

                VBox content = new VBox(24);
                content.setPadding(new Insets(20, 32, 32, 32));
                content.setStyle("-fx-background-color: " + BACKGROUND + ";");

                content.getChildren().addAll(
                                heading,
                                buildMyBillsCard(),
                                buildPaymentHistoryCard());

                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
                return scrollPane;
        }

        // =================================================================
        // MY BILLS (Water Bill + Property Tax only)
        // =================================================================
        private VBox buildMyBillsCard() {

                Label title = new Label("My Bills");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                Label viewAll = new Label("View all bills \u2192");
                viewAll.setPadding(new Insets(6, 14, 6, 14));
                viewAll.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: " + rgba(CONTEXT_TEAL, 0.10) + ";" +
                                                "-fx-background-radius: 999;" +
                                                "-fx-text-fill: " + CONTEXT_TEAL + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-cursor: hand;");

                Region headerSpacer = new Region();
                HBox.setHgrow(headerSpacer, Priority.ALWAYS);

                HBox header = new HBox(title, headerSpacer, viewAll);
                header.setAlignment(Pos.CENTER_LEFT);

                HBox billsRow = new HBox(
                                18,
                                billCard("\uD83D\uDCA7", "#3A8CD6", "Water Bill", "\u20B9240.00", "Due Date: 25 May 2024", "DUE",
                                                DELAYED_RED),
                                billCard("\uD83C\uDFE0", FOREST_DEEP, "Property Tax", "\u20B91,200.00", "Due Date: 15 Jun 2024",
                                                "UPCOMING", CONTEXT_TEAL));

                VBox card = new VBox(16, header, billsRow);
                card.setPadding(new Insets(24));
                card.setStyle(cardStyle(20));
                addHoverLift(card, 20);
                return card;
        }

        /** One "My Bills" card: icon chip, name, amount, due date, status pill, Pay Now button. */
        private VBox billCard(String icon, String iconColor, String name, String amount, String dueDate,
                        String statusText, String statusColor) {

                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 18px;");
                StackPane iconChip = new StackPane(iconLabel);
                iconChip.setPrefSize(44, 44);
                iconChip.setMaxSize(44, 44);
                iconChip.setStyle("-fx-background-color: " + rgba(iconColor, 0.12) + "; -fx-background-radius: 12;");

                Label nameLabel = new Label(name);
                nameLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                HBox top = new HBox(12, iconChip, nameLabel);
                top.setAlignment(Pos.CENTER_LEFT);

                Label amountLabel = new Label(amount);
                amountLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 22px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                Label dueLabel = new Label(dueDate);
                dueLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");

                Label statusPill = new Label(statusText);
                statusPill.setPadding(new Insets(4, 10, 4, 10));
                statusPill.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: " + rgba(statusColor, 0.14) + ";" +
                                                "-fx-text-fill: " + statusColor + ";" +
                                                "-fx-background-radius: 999;" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: 800;");

                Button payNow = new Button("Pay Now");
                payNow.setMaxWidth(Double.MAX_VALUE);
                payNow.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", "
                                                + FOREST_DEEP + ");" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 10;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.30), 8, 0.1, 0, 3);");

                VBox card = new VBox(10, top, amountLabel, dueLabel, statusPill, payNow);
                card.setPadding(new Insets(18));
                card.setPrefWidth(280);
                HBox.setHgrow(card, Priority.ALWAYS);
                card.setStyle(cardStyle(16));
                addHoverLift(card, 16);
                return card;
        }

        // =================================================================
        // PAYMENT HISTORY (recently paid bills + download receipt)
        // =================================================================
        private VBox buildPaymentHistoryCard() {

                Label title = new Label("Payment History");
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                Label viewAll = new Label("View all history \u2192");
                viewAll.setPadding(new Insets(6, 14, 6, 14));
                viewAll.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: " + rgba(CONTEXT_TEAL, 0.10) + ";" +
                                                "-fx-background-radius: 999;" +
                                                "-fx-text-fill: " + CONTEXT_TEAL + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-cursor: hand;");

                Region headerSpacer = new Region();
                HBox.setHgrow(headerSpacer, Priority.ALWAYS);

                HBox header = new HBox(title, headerSpacer, viewAll);
                header.setAlignment(Pos.CENTER_LEFT);

                GridPane table = new GridPane();
                table.setHgap(12);
                table.setVgap(4);

                ColumnConstraints colType = new ColumnConstraints();
                colType.setPercentWidth(26);
                ColumnConstraints colRef = new ColumnConstraints();
                colRef.setPercentWidth(20);
                ColumnConstraints colDate = new ColumnConstraints();
                colDate.setPercentWidth(16);
                ColumnConstraints colAmount = new ColumnConstraints();
                colAmount.setPercentWidth(16);
                ColumnConstraints colStatus = new ColumnConstraints();
                colStatus.setPercentWidth(12);
                ColumnConstraints colReceipt = new ColumnConstraints();
                colReceipt.setPercentWidth(10);
                table.getColumnConstraints().addAll(colType, colRef, colDate, colAmount, colStatus, colReceipt);

                // Header row
                table.addRow(0,
                                tableHeaderLabel("Bill Type"),
                                tableHeaderLabel("Bill ID / Reference"),
                                tableHeaderLabel("Date"),
                                tableHeaderLabel("Amount"),
                                tableHeaderLabel("Status"),
                                tableHeaderLabel("Receipt"));

                // TODO: replace with PaymentService.getPaymentHistory(userId), restricted here
                // to Water Bill + Property Tax as requested.
                addHistoryRow(table, 1, "\uD83D\uDCA7", "#3A8CD6", "Water Bill", "WB/2024/000123", "10 May 2024",
                                "\u20B9240.00");
                addHistoryRow(table, 2, "\uD83C\uDFE0", FOREST_DEEP, "Property Tax", "PT/2024/000789", "20 Apr 2024",
                                "\u20B91,200.00");

                VBox card = new VBox(16, header, table);
                card.setPadding(new Insets(24));
                card.setStyle(cardStyle(20));
                addHoverLift(card, 20);
                return card;
        }

        private Label tableHeaderLabel(String text) {
                Label label = new Label(text);
                label.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";" +
                                                "-fx-letter-spacing: 0.02em;");
                return label;
        }

        /** Adds one "paid bill" row (icon+name, ref, date, amount, PAID pill, download receipt button). */
        private void addHistoryRow(GridPane table, int rowIndex, String icon, String iconColor, String name,
                        String reference, String date, String amount) {

                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 13px;");
                StackPane iconChip = new StackPane(iconLabel);
                iconChip.setPrefSize(28, 28);
                iconChip.setMaxSize(28, 28);
                iconChip.setStyle("-fx-background-color: " + rgba(iconColor, 0.12) + "; -fx-background-radius: 8;");

                Label nameLabel = new Label(name);
                nameLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");
                HBox typeCell = new HBox(8, iconChip, nameLabel);
                typeCell.setAlignment(Pos.CENTER_LEFT);

                Label refLabel = rowText(reference, TEXT_SECONDARY, 600);
                Label dateLabel = rowText(date, TEXT_SECONDARY, 600);
                Label amountLabel = rowText(amount, TEXT_PRIMARY, 800);

                Label statusPill = new Label("Paid");
                statusPill.setPadding(new Insets(3, 10, 3, 10));
                statusPill.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: " + rgba(FOREST_DEEP, 0.12) + ";" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";" +
                                                "-fx-background-radius: 999;" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: 800;");

                Label downloadIcon = new Label("\u2B07");
                downloadIcon.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: " + FOREST_DEEP + ";" +
                                                "-fx-cursor: hand;");
                StackPane downloadButton = new StackPane(downloadIcon);
                downloadButton.setPrefSize(30, 30);
                downloadButton.setMaxSize(30, 30);
                downloadButton.setStyle(
                                "-fx-background-color: " + rgba(FOREST_DEEP, 0.08) + ";" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-cursor: hand;");
                // TODO: wire to ReceiptService.downloadReceipt(reference)
                downloadButton.setOnMouseClicked(e -> System.out.println("Download receipt: " + reference));

                Insets pad = new Insets(12, 4, 12, 4);
                for (var node : new javafx.scene.Node[] { typeCell, refLabel, dateLabel, amountLabel, statusPill,
                                downloadButton }) {
                        GridPane.setMargin(node, pad);
                }

                table.addRow(rowIndex, typeCell, refLabel, dateLabel, amountLabel, statusPill, downloadButton);

                // subtle row divider
                Region divider = new Region();
                divider.setPrefHeight(1);
                divider.setStyle("-fx-background-color: rgba(11,61,46,0.08);");
                GridPane.setColumnSpan(divider, 6);
                GridPane.setMargin(divider, new Insets(0, 0, 0, 0));
        }

        private Label rowText(String text, String color, int weight) {
                Label label = new Label(text);
                label.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: " + weight + ";" +
                                                "-fx-text-fill: " + color + ";");
                return label;
        }

        // =================================================================
        // HELPERS (shared look with VillagerDashboard.java / SarpanchDashboard.java)
        // =================================================================

        private String cardStyle(int radius) {
                return "-fx-background-color: rgba(255,255,255,0.88);" +
                                "-fx-background-radius: " + radius + ";" +
                                "-fx-border-color: rgba(255,255,255,0.5);" +
                                "-fx-border-radius: " + radius + ";" +
                                "-fx-border-width: 1;" +
                                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.06), 16, 0.1, 0, 4);";
        }

        private void addHoverLift(Region card, int radius) {
                String base = cardStyle(radius);
                String hover = "-fx-background-color: rgba(255,255,255,0.92);" +
                                "-fx-background-radius: " + radius + ";" +
                                "-fx-border-color: rgba(255,255,255,0.6);" +
                                "-fx-border-radius: " + radius + ";" +
                                "-fx-border-width: 1;" +
                                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.12), 24, 0.15, 0, 8);" +
                                "-fx-translate-y: -2;";
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