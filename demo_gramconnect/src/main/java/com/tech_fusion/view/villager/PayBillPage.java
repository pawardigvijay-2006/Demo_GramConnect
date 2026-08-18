package com.tech_fusion.view.villager;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import com.tech_fusion.controller.Razorpay;
import com.tech_fusion.view.villager.BillsAndPayments.BillData;

/**
 * GramConnect - Pay Bill
 *
 * Same non-Application pattern as the other pages: a plain class with
 * one public method that returns a Scene to swap onto the shared
 * VillagerDashboard.homeStage.
 *
 * Usage (from BillsAndPayments' "Pay Now" button):
 * VillagerDashboard.homeStage.setScene(
 * new PayBillPage().getPayBillScene(bill, backAction, backToBills));
 *
 * - backAction -> the usual "go to Dashboard" callback, unchanged.
 * - backToBills -> re-opens BillsAndPayments. Used by the sidebar's
 * "Bills & Payments" item, the breadcrumb link, "Back to Bills",
 * "Cancel", and after a successful payment - one callback, no
 * competing navigation paths.
 *
 * Payment is handled by RazorpayUpiCheckout, which creates a Razorpay
 * payment link, opens it in the villager's browser, and reports
 * success or failure back here once the payment completes.
 */
public class PayBillPage {

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

        private static final String BORDER = "rgba(11,61,46,0.12)";

        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        private static final DecimalFormat MONEY = new DecimalFormat("#,##0.00");

        public Scene getPayBillScene(BillData bill, Runnable backAction, Runnable backToBills) {

                BorderPane pane = new BorderPane();
                pane.setTop(buildHeader());
                pane.setCenter(buildScrollableContent(bill, backAction, backToBills));

                BorderPane root = new BorderPane();
                root.setLeft(buildSidebar(backAction, backToBills));
                root.setCenter(pane);

                return new Scene(root, 1500, 900);
        }

        // =================================================================
        // SIDEBAR - "Bills & Payments" stays active since this page is a
        // sub-page of Bills & Payments.
        // =================================================================
        private VBox buildSidebar(Runnable backToDashboardAction, Runnable backToBills) {
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

                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", true);
                billsNav.setOnMouseClicked(e -> backToBills.run());

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
        private ScrollPane buildScrollableContent(BillData bill, Runnable backAction, Runnable backToBills) {
                VBox content = new VBox(20);
                content.setPadding(new Insets(24, 32, 32, 32));
                content.setStyle("-fx-background-color: " + BACKGROUND + ";");

                content.getChildren().addAll(
                                buildBreadcrumbAndBack(backToBills),
                                buildTitleBlock(bill),
                                buildTwoColumnLayout(bill, backToBills));

                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
                return scrollPane;
        }

        private HBox buildBreadcrumbAndBack(Runnable backToBills) {
                Label billsLink = new Label("Bills & Payments");
                billsLink.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + CONTEXT_TEAL + "; -fx-cursor: hand;");
                billsLink.setOnMouseClicked(e -> backToBills.run());

                Label separator = new Label("\u203A");
                separator.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                Label current = new Label("Pay Bill");
                current.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                HBox crumb = new HBox(6, billsLink, separator, current);
                crumb.setAlignment(Pos.CENTER_LEFT);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button backToBillsBtn = new Button("\u2190  Back to Bills");
                backToBillsBtn.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: white;"
                                                + "-fx-text-fill: " + TEXT_PRIMARY + ";"
                                                + "-fx-font-size: 12px; -fx-font-weight: 700;"
                                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 8;"
                                                + "-fx-background-radius: 8; -fx-padding: 8 14 8 14; -fx-cursor: hand;");
                backToBillsBtn.setOnAction(e -> backToBills.run());

                HBox row = new HBox(crumb, spacer, backToBillsBtn);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        private VBox buildTitleBlock(BillData bill) {
                Label title = new Label("Pay " + bill.name);
                title.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 26px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                Label subtitle = new Label("Complete your payment securely using your preferred payment method.");
                subtitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                return new VBox(4, title, subtitle);
        }

        private HBox buildTwoColumnLayout(BillData bill, Runnable backToBills) {
                VBox leftColumn = new VBox(20,
                                buildBillDetailsCard(bill),
                                buildPaymentMethodCard(bill),
                                buildNoteAndActionsRow(bill, backToBills));
                leftColumn.setPrefWidth(760);
                HBox.setHgrow(leftColumn, Priority.ALWAYS);

                VBox rightColumn = new VBox(20,
                                buildPaymentSummaryCard(bill),
                                buildSecurityCard(),
                                buildNeedHelpCard());
                rightColumn.setPrefWidth(380);
                rightColumn.setMinWidth(340);

                HBox row = new HBox(20, leftColumn, rightColumn);
                return row;
        }

        // ---- 1. Bill Details ----
        private VBox buildBillDetailsCard(BillData bill) {
                Label icon = new Label(bill.icon);
                icon.setStyle("-fx-font-size: 16px;");
                StackPane iconChip = new StackPane(icon);
                iconChip.setPrefSize(34, 34);
                iconChip.setMaxSize(34, 34);
                iconChip.setStyle("-fx-background-color: " + bill.iconColor + "; -fx-background-radius: 10;");

                Label header = new Label("1. Bill Details");
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 15px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                HBox headerRow = new HBox(10, iconChip, header);
                headerRow.setAlignment(Pos.CENTER_LEFT);

                GridPane grid = new GridPane();
                grid.setHgap(28);
                grid.setVgap(14);
                grid.add(detailField("Bill Type", bill.name), 0, 0);
                grid.add(detailField("Bill ID / Reference", bill.billId), 1, 0);
                grid.add(detailField("Consumer Name", bill.consumerName), 2, 0);
                grid.add(detailField("Consumer ID", bill.consumerId), 0, 1);
                grid.add(detailField("Billing Period", bill.billingPeriod), 1, 1);
                grid.add(detailField("Due Date", bill.dueDate), 2, 1);
                HBox.setHgrow(grid, Priority.ALWAYS);

                Label amountLabel = new Label("Amount Payable");
                amountLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: "
                                                + ACCENT_GREEN + "; -fx-font-weight: 700;");
                Label amountValue = new Label("\u20B9" + MONEY.format(bill.amount));
                amountValue.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label statusPill = new Label(bill.status);
                statusPill.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: " + rgba(bill.statusColor, 0.14) + ";"
                                                + "-fx-text-fill: " + bill.statusColor + ";"
                                                + "-fx-font-size: 10px; -fx-font-weight: 800;"
                                                + "-fx-background-radius: 999; -fx-padding: 3 10 3 10;");

                VBox amountBox = new VBox(8, amountLabel, amountValue, statusPill);
                amountBox.setPadding(new Insets(16));
                amountBox.setPrefWidth(180);
                amountBox.setStyle(
                                "-fx-background-color: " + rgba(ACCENT_GREEN, 0.08) + ";"
                                                + "-fx-background-radius: 12;");

                HBox bodyRow = new HBox(24, grid, amountBox);
                bodyRow.setAlignment(Pos.TOP_LEFT);

                VBox card = new VBox(18, headerRow, bodyRow);
                card.setPadding(new Insets(22));
                card.setStyle(cardStyle(14));
                return card;
        }

        private VBox detailField(String label, String value) {
                Label labelNode = new Label(label);
                labelNode.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");
                Label valueNode = new Label(value);
                valueNode.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                return new VBox(4, labelNode, valueNode);
        }

        // ---- 2. Payment Method - UPI via Razorpay only ----
        private VBox buildPaymentMethodCard(BillData bill) {
                Label header = new Label("2. Payment Method");
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 15px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                Label razorpayBadge = new Label("\u26A1  Powered by Razorpay");
                razorpayBadge.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: " + rgba("#3D3D9E", 0.10) + ";"
                                                + "-fx-text-fill: #3D3D9E;"
                                                + "-fx-font-size: 10px; -fx-font-weight: 800;"
                                                + "-fx-background-radius: 999; -fx-padding: 4 10 4 10;");

                Region headerSpacer = new Region();
                HBox.setHgrow(headerSpacer, Priority.ALWAYS);
                HBox headerRow = new HBox(10, header, headerSpacer, razorpayBadge);
                headerRow.setAlignment(Pos.CENTER_LEFT);

                // Single, non-toggleable UPI option - shown as an "always selected" info
                // row instead of a picker, since it's the only method this app supports.
                Label upiIcon = new Label("\uD83D\uDCF2");
                upiIcon.setStyle("-fx-font-size: 18px;");
                StackPane upiIconChip = new StackPane(upiIcon);
                upiIconChip.setPrefSize(38, 38);
                upiIconChip.setMaxSize(38, 38);
                upiIconChip.setStyle(
                                "-fx-background-color: " + rgba(ACCENT_GREEN, 0.14) + "; -fx-background-radius: 10;");

                Label upiTitle = new Label("UPI Payment");
                upiTitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label upiSubtitle = new Label("Pay using any UPI app - Google Pay, PhonePe, Paytm & more");
                upiSubtitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");
                VBox upiTextBox = new VBox(2, upiTitle, upiSubtitle);

                Region optionSpacer = new Region();
                HBox.setHgrow(optionSpacer, Priority.ALWAYS);

                Label selectedCheck = new Label("\u2705  Selected");
                selectedCheck.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: "
                                                + ACCENT_GREEN + ";");

                HBox optionRow = new HBox(14, upiIconChip, upiTextBox, optionSpacer, selectedCheck);
                optionRow.setAlignment(Pos.CENTER_LEFT);
                optionRow.setPadding(new Insets(14, 16, 14, 16));
                optionRow.setStyle(
                                "-fx-background-color: " + rgba(ACCENT_GREEN, 0.06) + ";"
                                                + "-fx-border-color: " + ACCENT_GREEN + ";"
                                                + "-fx-border-width: 1.5;"
                                                + "-fx-border-radius: 10;"
                                                + "-fx-background-radius: 10;");

                VBox upiPanel = buildUpiPanel(bill);

                VBox card = new VBox(16, headerRow, optionRow, upiPanel);
                card.setPadding(new Insets(22));
                card.setStyle(cardStyle(14));
                return card;
        }

        private VBox buildUpiPanel(BillData bill) {
                Label subtitle = new Label(
                                "Scan the QR code with any UPI app, or tap Pay Now to open Razorpay checkout");
                subtitle.setWrapText(true);
                subtitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                // Placeholder QR - a real UPI QR would come from Razorpay's order/UPI intent
                // response. Swap this StackPane for an ImageView once you generate one
                // (e.g. via a QR library on your backend) from the order's UPI intent URL.
                ImageView qrImageView = new ImageView(
                                new Image("assets\\images\\IMG_20260815_113239.png"));

                qrImageView.setFitHeight(150);
                qrImageView.setFitWidth(150);
                qrImageView.setPreserveRatio(false);

                // Rounded corners
                Rectangle clip = new Rectangle(150, 150);
                clip.setArcWidth(10);
                clip.setArcHeight(10);

                qrImageView.setClip(clip);

                Label qrGlyph = new Label("\u2637");
                qrGlyph.setStyle("-fx-font-size: 64px; -fx-text-fill: " + TEXT_PRIMARY + ";");
                StackPane qrBox = new StackPane(qrImageView);
                qrBox.setPrefSize(150, 150);
                qrBox.setMaxSize(150, 150);
                qrBox.setStyle(
                                "-fx-background-color: white;"
                                                + "-fx-border-color: " + BORDER + "; -fx-border-width: 1;"
                                                + "-fx-background-radius: 8; -fx-border-radius: 8;");

                Label upiIdLabel = new Label("UPI ID");
                upiIdLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");
                Label upiIdValue = new Label(bill.upiId);
                upiIdValue.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                VBox upiIdBox = new VBox(4, upiIdLabel, upiIdValue);

                Label secureNote = new Label("\uD83D\uDD12  Handled entirely by Razorpay's secure checkout");
                secureNote.setWrapText(true);
                secureNote.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 10px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                VBox rightBox = new VBox(14, upiIdBox, secureNote);
                rightBox.setAlignment(Pos.TOP_LEFT);

                HBox qrRow = new HBox(28, qrBox, rightBox);
                qrRow.setAlignment(Pos.CENTER_LEFT);

                VBox panel = new VBox(10, subtitle, qrRow);
                panel.setPadding(new Insets(18));
                panel.setStyle(
                                "-fx-background-color: " + rgba(ACCENT_GREEN, 0.05) + ";"
                                                + "-fx-background-radius: 12;");
                return panel;
        }

        // ---- Note + Cancel/Pay Now ----
        private VBox buildNoteAndActionsRow(BillData bill, Runnable backToBills) {
                Label noteIcon = new Label("\u2139");
                noteIcon.setStyle("-fx-text-fill: " + CONTEXT_TEAL + "; -fx-font-size: 13px;");
                Label noteText = new Label(
                                "Note: Once payment is successful, it may take up to 5 minutes to update in the system.");
                noteText.setWrapText(true);
                noteText.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                HBox noteRow = new HBox(8, noteIcon, noteText);
                noteRow.setAlignment(Pos.CENTER_LEFT);
                noteRow.setPadding(new Insets(12, 16, 12, 16));
                noteRow.setStyle(
                                "-fx-background-color: " + rgba(CONTEXT_TEAL, 0.08) + ";"
                                                + "-fx-background-radius: 10;");

                Button cancelBtn = new Button("Cancel");
                cancelBtn.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: white;"
                                                + "-fx-text-fill: " + TEXT_PRIMARY + ";"
                                                + "-fx-font-size: 13px; -fx-font-weight: 700;"
                                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 8;"
                                                + "-fx-background-radius: 8; -fx-padding: 12 26 12 26; -fx-cursor: hand;");
                cancelBtn.setOnAction(e -> backToBills.run());

              

                Button payNowBtn = new Button("\uD83D\uDD12  Pay Now \u20B9" + MONEY.format(totalAmount(bill)));
                payNowBtn.setMaxWidth(Double.MAX_VALUE);
                payNowBtn.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT
                                                + ", " + FOREST_DEEP + ");"
                                                + "-fx-text-fill: white;"
                                                + "-fx-font-size: 13px; -fx-font-weight: 800;"
                                                + "-fx-background-radius: 8; -fx-padding: 12 26 12 26; -fx-cursor: hand;"
                                                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.30), 8, 0.1, 0, 3);");
                HBox.setHgrow(payNowBtn, Priority.ALWAYS);

                Razorpay razorpay = new Razorpay();

                payNowBtn.setOnAction(e -> handlePayNow(bill, backToBills));

                System.out.println("Razorpay payment clicked");

                Parent razorpayparent = razorpay.getView();

                HBox actionsRow = new HBox(12, cancelBtn, payNowBtn);
                actionsRow.setAlignment(Pos.CENTER_LEFT);

                return new VBox(14, noteRow, actionsRow,razorpayparent);
        }

        private double totalAmount(BillData bill) {
                return bill.amount + bill.lateFee;
        }

        /**
         * Opens the Razorpay UPI checkout dialog. On success, records the payment
         * (moves the bill into Payment History), shows a "Payment Successful"
         * popup, and returns to Bills & Payments. On failure/cancel, shows a
         * simple notice and leaves the user on this page to retry.
         */
        private void handlePayNow(BillData bill, Runnable backToBills) {
                
                RazorpayUpiCheckout.openUpiCheckout(
                                VillagerDashboard.homeStage,
                                totalAmount(bill),
                                "Ramesh Suresh Patil",
                                "9999999999",
                                bill.name,
                                new RazorpayUpiCheckout.PaymentCallback() {
                                        @Override
                                        public void onSuccess(String razorpayPaymentId) {
                                                String paidOn = LocalDate.now()
                                                                .format(DateTimeFormatter.ofPattern("d MMM yyyy"));
                                                BillsAndPayments.markBillPaid(bill, razorpayPaymentId, paidOn);

                                                Alert success = new Alert(AlertType.INFORMATION);
                                                success.setTitle("Payment Successful");
                                                success.setHeaderText(null);
                                                success.setContentText(
                                                                "Your payment of \u20B9"
                                                                                + MONEY.format(totalAmount(bill))
                                                                                + " for " + bill.name
                                                                                + " was successful.\n\nPayment ID: "
                                                                                + razorpayPaymentId
                                                                                + "\nPaid on: " + paidOn);
                                                success.getButtonTypes().setAll(ButtonType.OK);
                                                success.showAndWait();

                                                backToBills.run();
                                        }

                                        @Override
                                        public void onFailure(String reason) {
                                                Alert failed = new Alert(AlertType.WARNING);
                                                failed.setTitle("Payment Not Completed");
                                                failed.setHeaderText(null);
                                                failed.setContentText(reason == null
                                                                ? "Your payment could not be completed. Please try again."
                                                                : reason);
                                                failed.getButtonTypes().setAll(ButtonType.OK);
                                                failed.showAndWait();
                                        }
                                });
        }

        // ---- 3. Payment Summary ----
        private VBox buildPaymentSummaryCard(BillData bill) {
                Label header = new Label("3. Payment Summary");
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 15px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");

                HBox billAmountRow = summaryRow("Bill Amount", "\u20B9" + MONEY.format(bill.amount), false);
                HBox lateFeeRow = summaryRow("Late Fee", "\u20B9" + MONEY.format(bill.lateFee), false);

                Region divider = new Region();
                divider.setPrefHeight(1);
                divider.setStyle("-fx-background-color: " + BORDER + ";");

                HBox totalRow = summaryRow("Total Amount", "\u20B9" + MONEY.format(totalAmount(bill)), true);

                Label secureIcon = new Label("\uD83D\uDEE1");
                secureIcon.setStyle("-fx-font-size: 12px; -fx-text-fill: " + ACCENT_GREEN + ";");
                Label secureText = new Label("Your payment is secure and encrypted");
                secureText.setWrapText(true);
                secureText.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: "
                                                + ACCENT_GREEN + ";");
                HBox secureRow = new HBox(8, secureIcon, secureText);
                secureRow.setAlignment(Pos.CENTER_LEFT);
                secureRow.setPadding(new Insets(10, 12, 10, 12));
                secureRow.setStyle(
                                "-fx-background-color: " + rgba(ACCENT_GREEN, 0.08) + ";"
                                                + "-fx-background-radius: 8;");

                VBox card = new VBox(14, header, billAmountRow, lateFeeRow, divider, totalRow, secureRow);
                card.setPadding(new Insets(22));
                card.setStyle(cardStyle(14));
                return card;
        }

        private HBox summaryRow(String label, String value, boolean emphasize) {
                Label labelNode = new Label(label);
                labelNode.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: "
                                                + (emphasize ? "14px" : "12px") + "; -fx-font-weight: "
                                                + (emphasize ? "800" : "600") + "; -fx-text-fill: "
                                                + (emphasize ? TEXT_PRIMARY : TEXT_SECONDARY) + ";");

                Label valueNode = new Label(value);
                valueNode.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: "
                                                + (emphasize ? "16px" : "12px") + "; -fx-font-weight: "
                                                + (emphasize ? "900" : "700") + "; -fx-text-fill: "
                                                + (emphasize ? ACCENT_GREEN : TEXT_PRIMARY) + ";");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox row = new HBox(labelNode, spacer, valueNode);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- Security & Assurance ----
        private VBox buildSecurityCard() {
                Label icon = new Label("\uD83D\uDEE1");
                icon.setStyle("-fx-font-size: 15px; -fx-text-fill: " + ACCENT_GREEN + ";");
                Label header = new Label("Security & Assurance");
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 14px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                HBox headerRow = new HBox(8, icon, header);
                headerRow.setAlignment(Pos.CENTER_LEFT);

                VBox list = new VBox(10,
                                securityItem("100% Secure Payments"),
                                securityItem("Your data is protected"),
                                securityItem("Instant Payment Confirmation"),
                                securityItem("Official Receipt Provided"));

                VBox card = new VBox(14, headerRow, list);
                card.setPadding(new Insets(22));
                card.setStyle(cardStyle(14));
                return card;
        }

        private HBox securityItem(String text) {
                Label check = new Label("\u2714");
                check.setStyle("-fx-font-size: 11px; -fx-text-fill: " + ACCENT_GREEN + ";");
                Label label = new Label(text);
                label.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                HBox row = new HBox(8, check, label);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        // ---- Need Help ----
        private VBox buildNeedHelpCard() {
                Label icon = new Label("\uD83C\uDFA7");
                icon.setStyle("-fx-font-size: 15px; -fx-text-fill: " + FOREST_DEEP + ";");
                Label header = new Label("Need Help?");
                header.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 14px; -fx-font-weight: 900; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                HBox headerRow = new HBox(8, icon, header);
                headerRow.setAlignment(Pos.CENTER_LEFT);

                Label body = new Label("If you face any issue with payment, please contact");
                body.setWrapText(true);
                body.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                Label phone = new Label("1800-123-4567");
                phone.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 14px; -fx-font-weight: 900; -fx-text-fill: "
                                                + FOREST_DEEP + ";");
                Label tollFree = new Label(" (Toll Free)");
                tollFree.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");
                HBox phoneRow = new HBox(4, phone, tollFree);
                phoneRow.setAlignment(Pos.CENTER_LEFT);

                Label hours = new Label("Mon - Sat: 9:00 AM to 6:00 PM");
                hours.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");

                VBox card = new VBox(10, headerRow, body, phoneRow, hours);
                card.setPadding(new Insets(22));
                card.setStyle(cardStyle(14));
                return card;
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




