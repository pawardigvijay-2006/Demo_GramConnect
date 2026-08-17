package com.tech_fusion.view.gramsevak;

import com.tech_fusion.model.gramsevak.Bill;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class BillManagement {
        // ================= COLORS (same palette as the other pages) =================
        private static final String PRIMARY = "#005B1B";
        private static final String PRIMARY_DARK = "#004D16";

        private static final String ACCENT_GREEN = "#68D66F";
        private static final String LIGHT_GREEN = "#E8F7EA";

        private static final String BLUE = "#2196F3";
        private static final String LIGHT_BLUE = "#EAF5FC";

        private static final String WARNING = "#F4A62A";
        private static final String LIGHT_YELLOW = "#FFF7E5";

        private static final String ERROR = "#D93025";
        private static final String LIGHT_RED = "#FDECEC";

        private static final String BACKGROUND = "#F4F8FB";

        private static final String TEXT_PRIMARY = "#10251A";
        private static final String TEXT_SECONDARY = "#66756C";

        private static final String BORDER = "#D8E2DC";
        private static final String SECONDARY = "#1976D2";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String FOREST_DEEP = "#0B3D2E";

        // Light green sidebar gradient (identical to VillagerDashboard.java)
        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";
        private static final String DELAYED_RED = "#D94C38";

        private Runnable backAction;
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        public Scene getBillManagementPage(Runnable backAction) {
                this.backAction = backAction;

                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BACKGROUND + ";");

                root.setLeft(buildSidebar(backAction));
                root.setCenter(buildMainArea());

                return new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        }

        public VBox buildSidebar(Runnable backAction) {

                VBox sidebar = new VBox();

                sidebar.setPrefWidth(288);
                sidebar.setMinWidth(288);
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

                Image logoImage = new Image("assets\\images\\gc logo.jpeg");
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
                logoBox.setPadding(new Insets(24));

                Label dashboardNav = navItem("\uD83C\uDFE0  Dashboard", false);
                dashboardNav.setOnMouseClicked(e -> backAction.run());

                Label billNav = navItem("\uD83C\uDFD7  Bill Management", true);
                billNav.setOnMouseClicked(e -> Dashboard.homeStage.setScene(
                                new BillManagement().getBillManagementPage(backAction)));

                Label documentNav = navItem("\uD83D\uDCAC  Documents Managemnet", false);
                documentNav.setOnMouseClicked(e -> Dashboard.homeStage.setScene(
                                new DocumentsManage().getDocumentPage(backAction)));

                Label complaintNav = navItem("\u26A0 Complaints", false);
                complaintNav.setOnMouseClicked(e -> Dashboard.homeStage
                                .setScene(new Complaints().getComplaintScene(backAction)));

                Label schemeNav = navItem("📄 Government Schemes", false);
                schemeNav.setOnMouseClicked(e -> Dashboard.homeStage
                                .setScene(new GovScheme().getSchemeScene(backAction)));

                VBox navItems = new VBox(
                                6,
                                dashboardNav,
                                billNav,
                                documentNav,
                                complaintNav,
                                schemeNav);

                navItems.setPadding(new Insets(16, 12, 16, 12));
                VBox.setVgrow(navItems, Priority.ALWAYS);

                /* ----- CTA + footer links ----- */
                VBox footer = new VBox(10);
                footer.setPadding(new Insets(20, 24, 24, 24));

                Region divider = new Region();
                divider.setPrefHeight(1);
                divider.setStyle(
                                "-fx-background-color: linear-gradient(to right, transparent, rgba(11,61,46,0.25), transparent);");

                VBox smallLinks = new VBox(4);
                smallLinks.setPadding(new Insets(8, 0, 0, 0));
                smallLinks.getChildren().addAll(
                                footerLink("\u2699", "Settings"),
                                footerLink("\u2753", "Support"));

                footer.getChildren().addAll(divider, smallLinks);
                sidebar.getChildren().addAll(logoBox, navItems, footer);
                return sidebar;
        }

        private HBox footerLink(String icon, String text) {
                HBox link = new HBox(10);
                link.setAlignment(Pos.CENTER_LEFT);
                link.setPadding(new Insets(8, 16, 8, 16));
                Label ic = new Label(icon);
                ic.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.65);");
                Label lbl = new Label(text);
                lbl.setStyle("-fx-font-family: " + FONT_FAMILY
                                + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.65);");
                link.getChildren().addAll(ic, lbl);
                String base = "-fx-background-radius: 8; -fx-background-color: transparent; -fx-cursor: hand;";
                link.setStyle(base);
                link.setOnMouseEntered(e -> link.setStyle(
                                "-fx-background-radius: 8; -fx-background-color: rgba(255,255,255,0.45); -fx-cursor: hand;"));
                link.setOnMouseExited(e -> link.setStyle(base));
                return link;
        }

        private Label navItem(String text, boolean active) {

                Label item = new Label(text);
                item.setMaxWidth(Double.MAX_VALUE);
                item.setPadding(new Insets(14, 16, 14, 16));

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
        // MAIN AREA (header on top, scrollable content below it)
        // =================================================================
        private BorderPane buildMainArea() {
                BorderPane main = new BorderPane();
                main.setTop(buildHeader());
                main.setCenter(buildScrollableContent());
                return main;
        }

        public HBox buildHeader() {
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

                StackPane avatar = new StackPane(new Label("AJ"));
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

                Label name = new Label("Amit Jadhav");
                name.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label role = new Label("Gram Sevak");
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

                VBox content = new VBox(14);
                content.setPadding(new Insets(16, 24, 24, 24));
                content.setFillWidth(true);
                content.setStyle("-fx-background-color: transparent;");
                // ============================================================
                // HEADER
                // ============================================================
                BorderPane titlePane = new BorderPane();

                VBox titleBox = new VBox(6);

                Text title = new Text("Bill Management");
                title.setStyle("-fx-font-size: 32px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #0B4F43;");
                Text subtitle = new Text(
                                "Manage and track utility and property bills for the village");
                subtitle.setStyle(
                                "-fx-font-size: 16px;" +
                                                "-fx-fill: #657180;");

                titleBox.getChildren().addAll(title, subtitle);
                // ============================================================
                // GENERATE BUTTONS
                // ============================================================
                Button waterBillButton = new Button("+  Generate Water Bill");
                waterBillButton.setStyle(
                                "-fx-background-color: #0B4F43;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 10 15 10 15;" +
                                                "-fx-cursor: hand;");
                waterBillButton.setOnAction(event -> System.out.println("Generate Water Bill clicked"));

                Button propertyBillButton = new Button("+  Generate Property Bill");
                propertyBillButton.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: #0B4F43;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-border-color: #0B4F43;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 9 15 9 15;" +
                                                "-fx-cursor: hand;");

                

                propertyBillButton.setOnAction(event -> {
                        Runnable backToBillManagement = () -> {
                                Dashboard.homeStage.setScene(getBillManagementPage(backAction));
                                Dashboard.homeStage.setTitle("GramConnect - Bill Management");
                        };

                        Scene propertyTaxScene = new PropertyTaxBill().getPropertyTaxBillScene(backToBillManagement);
                        Dashboard.homeStage.setScene(propertyTaxScene);
                        Dashboard.homeStage.setTitle("GramConnect - Property Tax Bill");
                });

                HBox titleButtons = new HBox(10);
                titleButtons.setAlignment(Pos.CENTER_RIGHT);
                titleButtons.getChildren().addAll(
                                waterBillButton,
                                propertyBillButton);

                titlePane.setLeft(titleBox);
                titlePane.setRight(titleButtons);

                // ============================================================
                // SUMMARY CARDS
                // ============================================================

                HBox summaryCards = new HBox(15);

                VBox totalBills = createSummaryCard(
                                "TOTAL BILLS",
                                "128",
                                "#0B4F43");
                VBox paidBills = createSummaryCard(
                                "PAID",
                                "96",
                                "#16803C");
                VBox pendingBills = createSummaryCard(
                                "PENDING",
                                "24",
                                "#E67E1F");
                VBox overdueBills = createSummaryCard(
                                "OVERDUE",
                                "8",
                                "#D93025");
                HBox.setHgrow(totalBills, Priority.ALWAYS);
                HBox.setHgrow(paidBills, Priority.ALWAYS);
                HBox.setHgrow(pendingBills, Priority.ALWAYS);
                HBox.setHgrow(overdueBills, Priority.ALWAYS);

                summaryCards.getChildren().addAll(
                                totalBills,
                                paidBills,
                                pendingBills,
                                overdueBills);
                // ============================================================
                // SEARCH + FILTERS
                // ============================================================
                VBox filterCard = new VBox(12);
                filterCard.setPadding(new Insets(18));
                filterCard.setStyle("-fx-background-color: white;" +
                                "-fx-background-radius: 14;" +
                                "-fx-border-color: #E1E7E4;" +
                                "-fx-border-radius: 14;");

                Label filterTitle = new Label("Search & Filter Bills");
                filterTitle.setStyle("-fx-font-size: 15px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: #0B4F43;");

                HBox filterBox = new HBox(10);
                filterBox.setAlignment(Pos.CENTER_LEFT);

                TextField searchField = new TextField();
                searchField.setPromptText("🔍  Search Citizen Name or House No.");

                ComboBox<String> areaComboBox = new ComboBox<>();
                areaComboBox.getItems().addAll(
                                "All Areas",
                                "Ward 1",
                                "Ward 2",
                                "Ward 3");
                areaComboBox.setValue("All Areas");

                ComboBox<String> statusComboBox = new ComboBox<>();
                statusComboBox.getItems().addAll(
                                "All Statuses",
                                "Paid",
                                "Pending",
                                "Overdue");
                statusComboBox.setValue("All Statuses");

                Button searchButton = new Button("Search");
                searchButton.setStyle("-fx-background-color: #159A9C;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;" +
                                "-fx-padding: 0 20 0 20;" +
                                "-fx-cursor: hand;");
                searchButton.setOnAction(event -> System.out.println("Search clicked"));

                HBox.setHgrow(searchField, Priority.ALWAYS);
                areaComboBox.setMaxWidth(Double.MAX_VALUE);
                statusComboBox.setMaxWidth(Double.MAX_VALUE);
                searchButton.setMaxWidth(Double.MAX_VALUE);

                searchField.setMinWidth(150);
                areaComboBox.setMinWidth(100);
                statusComboBox.setMinWidth(110);
                searchButton.setMinWidth(70);

                filterBox.getChildren().addAll(
                                searchField,
                                areaComboBox,
                                statusComboBox,
                                searchButton);

                filterCard.getChildren().addAll(
                                filterTitle,
                                filterBox);
                // ============================================================
                // BILL LIST CARD
                // ============================================================
                VBox billSection = new VBox(12);
                billSection.setPadding(new Insets(20));
                billSection.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 16;" +
                                                "-fx-border-color: #E1E7E4;" +
                                                "-fx-border-radius: 16;");

                BorderPane billHeader = new BorderPane();
                Label billTitle = new Label("Recent Bills");
                billTitle.setStyle(
                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #0B4F43;");

                Label viewAll = new Label("View All →");
                viewAll.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #159A9C;" +
                                                "-fx-cursor: hand;");

                billHeader.setLeft(billTitle);
                billHeader.setRight(viewAll);
                // ============================================================
                // BILL ITEMS
                // ============================================================
                VBox billList = new VBox(8);
                Bill bill1 = new Bill(
                                "Ramesh Kumar",
                                "H-102",
                                "Water",
                                450,
                                "15 Oct 2026",
                                "Paid");
                Bill bill2 = new Bill(
                                "Anita Sharma",
                                "H-205",
                                "Property",
                                1200,
                                "20 Oct 2026",
                                "Pending");
                Bill bill3 = new Bill(
                                "Vikram Singh",
                                "H-310",
                                "Water",
                                650,
                                "10 Oct 2026",
                                "Overdue");
                Bill bill4 = new Bill(
                                "Sunita Patil",
                                "H-115",
                                "Property",
                                900,
                                "25 Oct 2026",
                                "Paid");
                billList.getChildren().addAll(
                                createBillItem(bill1, "#16803C"),
                                createBillItem(bill2, "#E67E1F"),
                                createBillItem(bill3, "#D93025"),
                                createBillItem(bill4, "#16803C"));

                billSection.getChildren().addAll(
                                billHeader,
                                billList);
                // ============================================================
                // ADD EVERYTHING
                // ============================================================
                content.getChildren().addAll(
                                titlePane,
                                summaryCards,
                                filterCard,
                                billSection);
                // ============================================================
                // SCROLL PANE
                // ============================================================
                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent;");
                return scrollPane;
        }

        // ============================================================
        // SUMMARY CARD
        // ============================================================
        private static VBox createSummaryCard(
                        String titleText,
                        String numberText,
                        String accent) {

                VBox card = new VBox(8);
                card.setPadding(new Insets(15));
                card.setStyle("-fx-background-color: white;" +
                                "-fx-background-radius: 14;" +
                                "-fx-border-color: #E1E7E4;" +
                                "-fx-border-radius: 14;");

                Label title = new Label(titleText);
                title.setStyle("-fx-font-size: 11px;-fx-font-weight: bold;-fx-text-fill: #657180;");

                Label number = new Label(numberText);
                number.setStyle("-fx-font-size: 28px;-fx-font-weight: bold;-fx-text-fill: " + accent + ";");

                Label status = new Label("●  " + titleText);
                status.setStyle("-fx-font-size: 11px;-fx-font-weight: bold;-fx-text-fill: " + accent + ";");

                card.getChildren().addAll(
                                title,
                                number,
                                status);
                return card;
        }

        // ============================================================
        // SINGLE BILL ITEM
        // ============================================================
        private static HBox createBillItem(Bill bill, String accent) {
                HBox item = new HBox(15);
                HBox.setHgrow(item, Priority.ALWAYS);
                item.setAlignment(Pos.CENTER_LEFT);
                item.setPadding(new Insets(12));
                item.setStyle("-fx-background-color: #F8FAF9;" + "-fx-background-radius: 10;");
                // ------------------------------------------------------------
                // ICON
                // ------------------------------------------------------------
                VBox iconBox = new VBox();
                VBox.setVgrow(iconBox, Priority.ALWAYS);
                iconBox.setAlignment(Pos.CENTER);
                // iconBox.setPrefSize(45, 45);
                // iconBox.setMinSize(45, 45);
                iconBox.setStyle(
                                "-fx-background-color: " + rgba(accent, 0.12) + ";" +
                                                "-fx-background-radius: 10;");
                Label icon = new Label(
                                bill.getBillType().equals("Water")
                                                ? "💧"
                                                : "🏠");
                icon.setStyle("-fx-font-size: 18px;");

                iconBox.getChildren().add(icon);
                // ------------------------------------------------------------
                // CITIZEN DETAILS
                // ------------------------------------------------------------
                VBox citizenBox = new VBox(3);
                VBox.setVgrow(citizenBox, Priority.ALWAYS);

                Label citizen = new Label(bill.getCitizenname());
                citizen.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;-fx-text-fill: #285B5B;");

                Label house = new Label("House No: " + bill.getHousename());
                house.setStyle("-fx-font-size: 11px;-fx-text-fill: #7A8A87;");

                citizenBox.getChildren().addAll(
                                citizen,
                                house);
                // ------------------------------------------------------------
                // BILL TYPE
                // ------------------------------------------------------------
                VBox typeBox = new VBox(3);
                VBox.setVgrow(typeBox, Priority.ALWAYS);

                Label typeTitle = new Label("Bill Type");
                typeTitle.setStyle("-fx-font-size: 10px;-fx-text-fill: #7A8A87;");

                Label type = new Label(bill.getBillType());
                type.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;-fx-text-fill: #285B5B;");

                typeBox.getChildren().addAll(
                                typeTitle,
                                type);
                // ------------------------------------------------------------
                // AMOUNT
                // ------------------------------------------------------------
                VBox amountBox = new VBox(3);
                VBox.setVgrow(amountBox, Priority.ALWAYS);

                Label amountTitle = new Label("Amount");
                amountTitle.setStyle("-fx-font-size: 10px;-fx-text-fill: #7A8A87;");

                Label amount = new Label("₹" + bill.getAmount());
                amount.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;-fx-text-fill: #0B4F43;");

                amountBox.getChildren().addAll(
                                amountTitle,
                                amount);
                // ------------------------------------------------------------
                // DUE DATE
                // ------------------------------------------------------------
                VBox dateBox = new VBox(3);
                VBox.setVgrow(dateBox, Priority.ALWAYS);

                Label dateTitle = new Label("Due Date");
                dateTitle.setStyle("-fx-font-size: 10px;-fx-text-fill: #7A8A87;");

                Label date = new Label(bill.getDueDate());
                date.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;-fx-text-fill: #285B5B;");

                dateBox.getChildren().addAll(dateTitle, date);
                // ------------------------------------------------------------
                // SPACER
                // ------------------------------------------------------------
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // ------------------------------------------------------------
                // STATUS
                // ------------------------------------------------------------

                Label status = new Label(bill.getStatus());
                status.setPadding(new Insets(5, 10, 5, 10));

                status.setStyle(
                                "-fx-background-color: " +
                                                rgba(accent, 0.12) + ";" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: " + accent + ";");

                item.getChildren().addAll(
                                iconBox,
                                citizenBox,
                                typeBox,
                                amountBox,
                                dateBox,
                                spacer,
                                status);

                return item;
        }

        // ============================================================
        // RGBA HELPER
        // ============================================================
        private static String rgba(String hex, double alpha) {

                int r = Integer.parseInt(hex.substring(1, 3), 16);
                int g = Integer.parseInt(hex.substring(3, 5), 16);
                int b = Integer.parseInt(hex.substring(5, 7), 16);
                return "rgba(" +
                                r + "," +
                                g + "," +
                                b + "," +
                                alpha + ")";
        }

}
