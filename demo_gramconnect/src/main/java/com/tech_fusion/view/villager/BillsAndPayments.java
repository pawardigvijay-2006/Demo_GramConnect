package com.tech_fusion.view.villager;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
 *
 * ============================================================
 * NEW IN THIS VERSION
 * ============================================================
 * - BillData is a small shared model (bill type, IDs, amounts, due
 *   date, status, UPI id) so "Pay Now" has real data to hand off.
 * - BILLS / PAYMENT_HISTORY are static, in-memory stores (same
 *   pattern as ComplaintsPage.COMPLAINTS) shared with PayBillPage,
 *   so a successful payment is reflected here immediately without
 *   re-fetching anything.
 * - Each "My Bills" card's "Pay Now" button now navigates to
 *   PayBillPage.getPayBillScene(bill, backAction, backToBills),
 *   passing the specific BillData for that card.
 * - markBillPaid(...) is called by PayBillPage after a successful
 *   Razorpay UPI payment: it flips the bill's status to PAID (so it
 *   drops out of "My Bills") and adds a row to Payment History.
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

        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        // =================================================================
        // SHARED DATA MODEL + STORE
        // Package-visible so PayBillPage can read/update it directly.
        // Replace with a real BillingService/DB call when one exists.
        // =================================================================
        static class BillData {
                String icon;
                String iconColor;
                String name;
                String billId;
                String consumerId;
                String consumerName;
                String billingPeriod;
                String dueDate;
                double amount;
                double lateFee;
                String status; // "DUE", "UPCOMING", "PAID"
                String statusColor;
                String upiId;

                BillData(String icon, String iconColor, String name, String billId, String consumerId,
                                String consumerName, String billingPeriod, String dueDate, double amount,
                                double lateFee, String status, String statusColor, String upiId) {
                        this.icon = icon;
                        this.iconColor = iconColor;
                        this.name = name;
                        this.billId = billId;
                        this.consumerId = consumerId;
                        this.consumerName = consumerName;
                        this.billingPeriod = billingPeriod;
                        this.dueDate = dueDate;
                        this.amount = amount;
                        this.lateFee = lateFee;
                        this.status = status;
                        this.statusColor = statusColor;
                        this.upiId = upiId;
                }
        }

        /** One row in the Payment History table. */
        static class PaymentRecord {
                String icon;
                String iconColor;
                String name;
                String reference;
                String date;
                double amount;

                PaymentRecord(String icon, String iconColor, String name, String reference, String date,
                                double amount) {
                        this.icon = icon;
                        this.iconColor = iconColor;
                        this.name = name;
                        this.reference = reference;
                        this.date = date;
                        this.amount = amount;
                }
        }

        // TODO: replace with BillingService.getBillsForVillager(userId) /
        // PaymentService.getPaymentHistory(userId).
        static final List<BillData> BILLS = new ArrayList<>();
        static final List<PaymentRecord> PAYMENT_HISTORY = new ArrayList<>();

        static {
                BILLS.add(new BillData(
                                "\uD83D\uDCA7", "#3A8CD6", "Water Bill",
                                "WB/2024/000123", "WCON-88213", "Ramesh Suresh Patil",
                                "Apr 2024 - May 2024", "25 May 2024",
                                240.00, 0.00, "DUE", DELAYED_RED, "gramconnect@upi"));
                BILLS.add(new BillData(
                                "\uD83C\uDFE0", FOREST_DEEP, "Property Tax",
                                "PT/2024/000789", "PCON-55210", "Ramesh Suresh Patil",
                                "FY 2024-25", "15 Jun 2024",
                                1200.00, 0.00, "DUE", DELAYED_RED, "gramconnect@upi"));

                PAYMENT_HISTORY.add(new PaymentRecord(
                                "\uD83D\uDCA7", "#3A8CD6", "Water Bill",
                                "WB/2024/000098", "10 Apr 2024", 220.00));
                PAYMENT_HISTORY.add(new PaymentRecord(
                                "\uD83C\uDFE0", FOREST_DEEP, "Property Tax",
                                "PT/2023/000654", "20 Jan 2024", 1150.00));
        }

        /**
         * Called by PayBillPage after a successful Razorpay UPI payment. Marks the
         * bill as PAID (so it drops out of "My Bills") and adds it to the top of
         * Payment History.
         */
        static void markBillPaid(BillData bill, String razorpayPaymentId, String paidOn) {
                bill.status = "PAID";
                PAYMENT_HISTORY.add(0, new PaymentRecord(
                                bill.icon, bill.iconColor, bill.name, bill.billId, paidOn, bill.amount + bill.lateFee));
        }

        // Instance state
        private Runnable backAction;

        /** Public entry point - returns the fully built Bills & Payments screen. */
        public Scene getBillsScene(Runnable backToDashboardAction) {
                this.backAction = backToDashboardAction;

                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BACKGROUND + ";");

                root.setLeft(buildSidebar(backToDashboardAction));
                root.setCenter(buildMainArea());

                return new Scene(root, 1500, 850);
        }

        // =================================================================
        // SIDEBAR - identical structure/colors to VillagerDashboard.java,
        // except "Bills & Payments" is the active row here, and
        // "Dashboard" calls the Runnable instead of homeStage.setScene(...)
        // directly (this class never touches a Stage at all).
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
                        VillagerDashboard.homeStage.setScene(new ProjectTransparency().getProjectScene(backToDashboardAction));
                });

                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", false);
                complaintsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new ComplaintsPage().getComplaintsPage(backToDashboardAction));
                });
                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", false);
                schemesNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new GovernmentSchemes().getSchemesScene(backToDashboardAction));
                });
                Label certificatesNav = navItem("\uD83D\uDCDC  Certificates", false);
                certificatesNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new Certificates().getCertificatesScene(backToDashboardAction));
                });
                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", true);
                billsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new BillsAndPayments().getBillsScene(backToDashboardAction));
                });

                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                announcementsNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new Announcements().getAnnouncementScene(backToDashboardAction));
                });
                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", false);
                gramSabhaNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new GramSabha().getGramSabhaScene(backToDashboardAction));
                });
                Label aiAssistantNav = navItem("\uD83E\uDD16  AI village assistant", false);
                aiAssistantNav.setOnMouseClicked(e -> {
                        VillagerDashboard.homeStage.setScene(new AIAssistant().getAiAssiatantScene(backToDashboardAction));
                });
                // TODO: wire these the same way once each page exposes its own
                // getXScene(Runnable backToDashboardAction) method.

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

        /** Same nav-row builder as VillagerDashboard.java: no handler attached here - callers attach their own. */
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
        // MAIN AREA (header on top, scrollable content below it)
        // =================================================================
        private BorderPane buildMainArea() {
                BorderPane main = new BorderPane();
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
        // MY BILLS (Water Bill + Property Tax only, skips anything already PAID)
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

                HBox billsRow = new HBox(18);
                boolean anyPending = false;
                for (BillData bill : BILLS) {
                        if (!"PAID".equals(bill.status)) {
                                billsRow.getChildren().add(billCard(bill));
                                anyPending = true;
                        }
                }
                if (!anyPending) {
                        Label allPaid = new Label("You're all caught up - no pending bills.");
                        allPaid.setStyle(
                                        "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-text-fill: "
                                                        + TEXT_SECONDARY + ";");
                        billsRow.getChildren().add(allPaid);
                }

                VBox card = new VBox(16, header, billsRow);
                card.setPadding(new Insets(24));
                card.setStyle(cardStyle(20));
                addHoverLift(card, 20);
                return card;
        }

        /** One "My Bills" card: icon chip, name, amount, due date, status pill, Pay Now button. */
        private VBox billCard(BillData bill) {

                Label iconLabel = new Label(bill.icon);
                iconLabel.setStyle("-fx-font-size: 18px;");
                StackPane iconChip = new StackPane(iconLabel);
                iconChip.setPrefSize(44, 44);
                iconChip.setMaxSize(44, 44);
                iconChip.setStyle("-fx-background-color: " + bill.iconColor + "; -fx-background-radius: 12;");

                Label nameLabel = new Label(bill.name);
                nameLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: 800;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                HBox top = new HBox(12, iconChip, nameLabel);
                top.setAlignment(Pos.CENTER_LEFT);

                Label amountLabel = new Label("\u20B9" + moneyFormat(bill.amount + bill.lateFee));
                amountLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 22px;" +
                                                "-fx-font-weight: 900;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                Label dueLabel = new Label("Due Date: " + bill.dueDate);
                dueLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-text-fill: " + TEXT_SECONDARY + ";");

                Label statusPill = new Label(bill.status);
                statusPill.setPadding(new Insets(4, 10, 4, 10));
                statusPill.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-background-color: " + rgba(bill.statusColor, 0.14) + ";" +
                                                "-fx-text-fill: " + bill.statusColor + ";" +
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

                // Navigation is conflict-free: PayBillPage gets the original Dashboard
                // backAction AND a dedicated "return to Bills & Payments" callback, so
                // it never has to guess which page to return to.
                payNow.setOnAction(e -> VillagerDashboard.homeStage.setScene(
                                new PayBillPage().getPayBillScene(
                                                bill,
                                                backAction,
                                                () -> VillagerDashboard.homeStage.setScene(
                                                                new BillsAndPayments().getBillsScene(backAction)))));

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

                int rowIndex = 1;
                for (PaymentRecord record : PAYMENT_HISTORY) {
                        addHistoryRow(table, rowIndex, record);
                        rowIndex++;
                }

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
        private void addHistoryRow(GridPane table, int rowIndex, PaymentRecord record) {

                Label iconLabel = new Label(record.icon);
                iconLabel.setStyle("-fx-font-size: 13px;");
                StackPane iconChip = new StackPane(iconLabel);
                iconChip.setPrefSize(28, 28);
                iconChip.setMaxSize(28, 28);
                iconChip.setStyle("-fx-background-color: " + record.iconColor + "; -fx-background-radius: 8;");

                Label nameLabel = new Label(record.name);
                nameLabel.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: 700;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");
                HBox typeCell = new HBox(8, iconChip, nameLabel);
                typeCell.setAlignment(Pos.CENTER_LEFT);

                Label refLabel = rowText(record.reference, TEXT_SECONDARY, 600);
                Label dateLabel = rowText(record.date, TEXT_SECONDARY, 600);
                Label amountLabel = rowText("\u20B9" + moneyFormat(record.amount), TEXT_PRIMARY, 800);

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
                downloadButton.setOnMouseClicked(e -> System.out.println("Download receipt: " + record.reference));

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

        private String moneyFormat(double amount) {
                return new java.text.DecimalFormat("#,##0.00").format(amount);
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