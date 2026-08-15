package com.tech_fusion.view.gramsevak;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class NewDashboard extends Application {
        /* ---------- Color palette (from the HTML template) ---------- */
        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String FOREST_LIGHT = "#0F4736";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String CONTEXT_TEAL = "#0E8C8C";
        private static final String AI_VIOLET = "#7C5CFC";
        private static final String DELAYED_RED = "#D94C38";
        // Light green sidebar colors (requested change)
        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#Bce3cc";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        private static final String BACKGROUND_IMAGE_PATH = "D:/Downloads/backgroundfinalimage.png/";
        BorderPane contentArea;
        private HBox activeNavItem;

        @Override
        public void start(Stage stage) throws Exception {
                BorderPane root = new BorderPane();
                Image backgroundImage = new Image(new File(BACKGROUND_IMAGE_PATH).toURI().toString());
                root.setBackground(new Background(new BackgroundImage(backgroundImage,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(100, 100, true, true, false, true))));
                root.setLeft(buildSidebar());

                contentArea = new BorderPane();
                contentArea.setTop(buildTopBar());

                ScrollPane scroller = createPage(buildMainContent());

                scroller.setStyle(
                                "-fx-background: transparent; " +
                                                "-fx-background-color: transparent; " +
                                                "-fx-border-color: transparent;");
                contentArea.setCenter(scroller);
                root.setCenter(contentArea);

                Scene scene = new Scene(root, 1300, 800);
                stage.setTitle("GramConnect - Gram Sevak");
                stage.setScene(scene);
                stage.show();
        }

        private ScrollPane createPage(Node page) {

                ScrollPane scrollPane = new ScrollPane(page);

                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(false);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scrollPane.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scrollPane.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-background: transparent;" +
                                                "-fx-border-color: transparent;");

                return scrollPane;
        }

        /*
         * ============================================================
         * SIDEBAR (Light Green version + Announcements section)
         * ============================================================
         */
        private VBox buildSidebar() {
                VBox sidebar = new VBox();
                sidebar.setPrefWidth(288);
                sidebar.setMinWidth(250);
                sidebar.setMaxWidth(288);
                sidebar.setStyle(
                                "-fx-background-color: linear-gradient(to bottom, " + SIDEBAR_TOP + ", " + SIDEBAR_MID
                                                + ", "
                                                + SIDEBAR_BOT + ");" +
                                                "-fx-border-color: transparent rgba(11,61,46,0.10) transparent transparent;"
                                                +
                                                "-fx-border-width: 0 1 0 0;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.20), 24, 0.2, 4, 0);");
                /* ----- Header: avatar + name ----- */
                HBox header = new HBox(14);
                header.setPadding(new Insets(24));
                header.setAlignment(Pos.CENTER_LEFT);

                StackPane avatar = new StackPane();
                Circle avatarCircle = new Circle(24);
                avatarCircle.setFill(Color.web(FOREST_DEEP));
                avatarCircle.setStroke(Color.web(SAFFRON_MAIN, 0.85));
                avatarCircle.setStrokeWidth(2.5);
                Label avatarInitials = new Label("AJ");
                avatarInitials.setFont(Font.font("Inter", FontWeight.BOLD, 16));
                avatarInitials.setTextFill(Color.WHITE);
                avatar.getChildren().addAll(avatarCircle, avatarInitials);

                VBox nameBox = new VBox(2);
                Label name = new Label("Gram Sevak");
                name.setStyle("-fx-font-family: " + FONT_FAMILY
                                + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: "
                                + FOREST_DEEP + ";");
                Label role = new Label("Gram Panchayat");
                role.setStyle("-fx-font-family: " + FONT_FAMILY
                                + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.65); -fx-letter-spacing: 0.05em;");
                nameBox.getChildren().addAll(name, role);

                header.getChildren().addAll(avatar, nameBox);

                /* ----- Navigation items ----- */
                VBox nav = new VBox(6);
                nav.setPadding(new Insets(16, 12, 16, 12));
                nav.getChildren().addAll(
                                navItem("\u25A6", "Dashboard", true, () -> {
                                        contentArea.setCenter(createPage(buildMainContent()));
                                }),
                                navItem("\uD83D\uDDC2", "Bill Management", false, () -> {
                                        contentArea.setCenter(createPage(BillManagement.getBillManageContent()));
                                }),
                                navItem("\uD83D\uDCB0", "Document Management", false, () -> {
                                        contentArea.setCenter(createPage(DocumentsManage.getDocumentManageContent()));
                                }),
                                navItem("\u26A0", "Complaints", false, () -> {
                                        contentArea.setCenter(createPage(Complaints.getComplaintManagement()));
                                }),
                                navItem("📄", "Government Schemes", false, () -> {
                                        contentArea.setCenter(createPage( GovSchemes.getSchemeContent(() -> {
                                         contentArea.setCenter(new ScrollPane(NewScheme.getNewSchemeContent()));
                                                                        })));
                                })
                // navItem("\uD83D\uDCE2", "Announcements", false) // NEW SECTION
                );
                VBox.setVgrow(nav, Priority.ALWAYS);

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

                sidebar.getChildren().addAll(header, nav, footer);
                return sidebar;
        }

        private HBox navItem(String icon, String text, boolean active, Runnable action) {
                HBox item = new HBox(14);
                item.setAlignment(Pos.CENTER_LEFT);
                item.setPadding(new Insets(14, 16, 14, 16));
                item.setMaxWidth(Double.MAX_VALUE);

                Label ic = new Label(icon);
                ic.setStyle("-fx-font-size: 17px; -fx-text-fill: " + (active ? SAFFRON_MAIN : FOREST_DEEP) + ";");

                Label lbl = new Label(text);
                lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px;" +
                                "-fx-font-weight: " + (active ? "800" : "600") + ";" +
                                "-fx-text-fill: " + (active ? SAFFRON_MAIN : "rgba(11,61,46,0.80)") + ";" +
                                "-fx-letter-spacing: 0.05em;");

                item.getChildren().addAll(ic, lbl);
                // Navigation across pages
                item.setOnMouseClicked(e -> {
                        action.run();
                });

                if (active) {
                        // active pill with saffron indicator bar on the left
                        Region bar = new Region();
                        bar.setPrefWidth(6);
                        bar.setMinWidth(6);
                        bar.setStyle("-fx-background-color: " + SAFFRON_MAIN + "; -fx-background-radius: 8 0 0 8;" +
                                        "-fx-effect: dropshadow(gaussian, rgba(224,122,31,0.6), 8, 0.3, 0, 0);");
                        HBox wrap = new HBox(bar, item);
                        HBox.setHgrow(item, Priority.ALWAYS);
                        wrap.setStyle("-fx-background-color: rgba(255,255,255,0.65); -fx-background-radius: 10;" +
                                        "-fx-effect: innershadow(gaussian, rgba(11,61,46,0.10), 6, 0.2, 0, 1);");
                        wrap.setMaxWidth(Double.MAX_VALUE);
                        return wrap;
                } else {
                        String base = "-fx-background-radius: 10; -fx-background-color: transparent; -fx-cursor: hand;";
                        item.setStyle(base);
                        item.setOnMouseEntered(e -> item.setStyle(
                                        "-fx-background-radius: 10; -fx-background-color: rgba(255,255,255,0.45); -fx-cursor: hand;"));
                        item.setOnMouseExited(e -> item.setStyle(base));
                        return item;
                }
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
                link.setOnMouseEntered(e -> link
                                .setStyle("-fx-background-radius: 8; -fx-background-color: rgba(255,255,255,0.45); -fx-cursor: hand;"));
                link.setOnMouseExited(e -> link.setStyle(base));
                return link;
        }

        /*
         * ============================================================
         * TOP NAVIGATION BAR
         * ============================================================
         */
        private HBox buildTopBar() {
                HBox topBar = new HBox(24);
                topBar.setAlignment(Pos.CENTER_LEFT);
                topBar.setPrefHeight(72);
                topBar.setPadding(new Insets(0, 32, 0, 32));
                topBar.setStyle(
                                "-fx-background-color: rgba(255,255,255,0.92);" +
                                                "-fx-border-color: transparent transparent rgba(255,255,255,0.6) transparent;"
                                                +
                                                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 8, 0.1, 0, 2);");
                Image projectLogo = new Image("assets\\images\\gramconnect.png");
                ImageView imgView = new ImageView(projectLogo);
                imgView.setFitHeight(50);
                imgView.setFitWidth(60);

                /* Search box */
                HBox searchBox = new HBox(8);
                searchBox.setAlignment(Pos.CENTER_LEFT);
                searchBox.setPadding(new Insets(0, 16, 0, 16));
                searchBox.setPrefWidth(480);
                searchBox.setPrefHeight(42);
                searchBox.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 12;" +
                                "-fx-border-color: rgba(11,61,46,0.10); -fx-border-radius: 12; -fx-border-width: 1;");
                Label searchIcon = new Label("\uD83D\uDD0D");
                searchIcon.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.5);");
                TextField searchField = new TextField();
                searchField.setPromptText("Search anything...");
                searchField.setStyle("-fx-background-color: transparent; -fx-font-family: " + FONT_FAMILY + ";" +
                                "-fx-font-size: 14px; -fx-text-fill: " + FOREST_DEEP
                                + "; -fx-prompt-text-fill: rgba(11,61,46,0.40);");
                HBox.setHgrow(searchField, Priority.ALWAYS);
                searchBox.getChildren().addAll(searchIcon, searchField);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                /* Notification bell with saffron dot */
                StackPane bell = new StackPane();
                Label bellIcon = new Label("\uD83D\uDD14");
                bellIcon.setStyle("-fx-font-size: 16px;");
                StackPane bellBtn = new StackPane(bellIcon);
                bellBtn.setPrefSize(42, 42);
                bellBtn.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 50;" +
                                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 4, 0.1, 0, 1); -fx-cursor: hand;");
                Circle dot = new Circle(5, Color.web(SAFFRON_MAIN));
                dot.setStroke(Color.WHITE);
                dot.setStrokeWidth(2);
                StackPane.setAlignment(dot, Pos.TOP_RIGHT);
                StackPane.setMargin(dot, new Insets(7, 7, 0, 0));
                bell.getChildren().addAll(bellBtn, dot);
                Region vDivider = new Region();
                vDivider.setPrefSize(1, 32);
                vDivider.setStyle("-fx-background-color: rgba(11,61,46,0.10);");

                /* Profile / language chip */
                HBox profile = new HBox(10);
                profile.setAlignment(Pos.CENTER_LEFT);
                profile.setPadding(new Insets(6, 12, 6, 6));
                profile.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 12;" +
                                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 4, 0.1, 0, 1); -fx-cursor: hand;");
                Circle pAvatar = new Circle(16, Color.web(CONTEXT_TEAL));
                pAvatar.setStroke(Color.WHITE);
                pAvatar.setStrokeWidth(2);
                Label lang = new Label("\u092E\u0930\u093E\u0920\u0940");
                lang.setStyle("-fx-font-family: " + FONT_FAMILY
                                + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: "
                                + FOREST_DEEP + ";");
                Label chevron = new Label("\u25BE");
                chevron.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.7);");
                profile.getChildren().addAll(pAvatar, lang, chevron);

                topBar.getChildren().addAll(imgView, searchBox, spacer, bell, vDivider, profile);
                return topBar;
        }

        /*
         * ============================================================
         * MAIN CONTENT
         * ============================================================
         */
        private VBox buildMainContent() {
                VBox main = new VBox(32);
                main.setPadding(new Insets(32, 40, 48, 40));
                main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");

                /* Welcome section */
                VBox welcome = new VBox(6);
                Label welcomeTitle = new Label("Welcome back, Gram Sevak");
                welcomeTitle.setStyle("-fx-font-family: " + FONT_FAMILY
                                + "; -fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
                Label welcomeSub = new Label(
                                "Gram Sevak Dashboard \u2013 Manage complaints,bills, documents and schemes efficiently.");
                welcomeSub.setStyle("-fx-font-family: " + FONT_FAMILY
                                + "; -fx-font-size: 16px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.70);");
                welcomeSub.setWrapText(true);
                welcome.getChildren().addAll(welcomeTitle, welcomeSub);

                // DATE
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | hh:mm a");
                String dateTime = now.format(formatter);
                Label date = new Label(dateTime);
                date.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;-fx-text-fill: #0B4F43;");

                BorderPane greeting = new BorderPane();
                greeting.setLeft(welcome);
                greeting.setRight(date);

                main.getChildren().addAll(
                                greeting,
                                buildKpiRow(),
                                buildRecentActivity(),
                                buildChartsRow()
                // add features of dashbord
                );
                return main;
        }

        /*
         * ============================================================
         * KPI CARDS ROW
         * ============================================================
         */
        private HBox buildKpiRow() {

                HBox row = new HBox(20);
                row.setAlignment(Pos.TOP_LEFT);

                // 1. PENDING BILLS
                VBox kpi1 = kpiCard("#0B4F43", "💧", "PENDING BILLS", "5");
                VBox bottom1 = kpiBottom(kpi1);
                Label status1 = new Label("●  Pending");
                status1.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;-fx-text-fill: #D97706;");
                bottom1.getChildren().add(status1);

                // 3. GOVERNMENT SCHEMES
                VBox kpi3 = kpiCard("#E67E1F", "🏛", "GOVERNMENT SCHEMES", "8");
                VBox bottom3 = kpiBottom(kpi3);
                Label status3 = new Label("●  Active");
                status3.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;-fx-text-fill: #16803C;");
                bottom3.getChildren().add(status3);

                // 4. OPEN COMPLAINTS
                VBox kpi4 = kpiCard("#7956F5", "⚠", "OPEN COMPLAINTS", "2");
                VBox bottom4 = kpiBottom(kpi4);
                Label status4 = new Label("●  Urgent");
                status4.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;-fx-text-fill: #DC2626;");
                bottom4.getChildren().add(status4);

                // 5. CERTIFICATES ISSUED
                VBox kpi5 = kpiCard("#0B4F43", "📜", "CERTIFICATES ISSUED", "12");
                VBox bottom5 = kpiBottom(kpi5);
                Label status5 = new Label("●  Active");
                status5.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;-fx-text-fill: #16803C;");
                bottom5.getChildren().add(status5);

                // Make all cards expand equally
                HBox.setHgrow(kpi1, Priority.ALWAYS);
                HBox.setHgrow(kpi3, Priority.ALWAYS);
                HBox.setHgrow(kpi4, Priority.ALWAYS);
                HBox.setHgrow(kpi5, Priority.ALWAYS);

                row.getChildren().addAll(kpi1, kpi3, kpi4, kpi5);
                return row;
        }

        /*
         * ============================================================
         * BASE KPI CARD
         * ============================================================
         */
        private VBox kpiCard(String accent, String icon, String labelText, String statText) {
                VBox card = new VBox();
                card.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(card, Priority.ALWAYS);
                card.setStyle(
                                "-fx-background-color: white;-fx-background-radius: 16;-fx-border-radius: 16;-fx-border-color: #E5E7EB;");

                // COLOURED TOP STRIP
                Region strip = new Region();
                strip.setPrefHeight(6);
                strip.setMinHeight(6);
                strip.setStyle("-fx-background-color: " + accent + ";" + "-fx-background-radius: 16 16 0 0;");

                // INNER CONTENT
                VBox inner = new VBox(20);
                inner.setPadding(new Insets(18, 20, 20, 20));
                VBox.setVgrow(inner, Priority.ALWAYS);

                // -------------------------
                // ICON + TITLE
                // -------------------------
                HBox head = new HBox(12);
                head.setAlignment(Pos.CENTER_LEFT);

                // Icon background
                StackPane iconChip = new StackPane();
                iconChip.setPrefSize(48, 48);
                iconChip.setMinSize(48, 48);
                iconChip.setStyle("-fx-background-color: " + rgba(accent, 0.12) + ";" + "-fx-background-radius: 12;");

                // Icon
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 18px;fx-text-fill: " + accent + ";");
                iconChip.getChildren().add(iconLabel);

                // Title
                Label titleLabel = new Label(labelText);
                titleLabel.setWrapText(true);
                titleLabel.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;-fx-text-fill: #285B5B;");
                head.getChildren().addAll(iconChip, titleLabel);

                // SPACE
                Region grow = new Region();
                VBox.setVgrow(grow, Priority.ALWAYS);

                // BOTTOM CONTENT
                VBox bottom = new VBox(10);

                // Large number
                Label stat = new Label(statText);

                stat.setStyle("-fx-font-size: 40px;-fx-font-weight: bold;-fx-text-fill: #0B4F43;");
                bottom.getChildren().add(stat);
                // Add everything
                inner.getChildren().addAll(
                                head,
                                grow,
                                bottom);
                card.getChildren().addAll(
                                strip,
                                inner);
                return card;
        }

        /*
         * ============================================================
         * GET KPI BOTTOM SECTION
         * ============================================================
         */
        private VBox kpiBottom(VBox kpiCard) {

                VBox inner = (VBox) kpiCard.getChildren().get(1);

                return (VBox) inner.getChildren()
                                .get(inner.getChildren().size() - 1);
        }

        /*
         * ============================================================
         * RECENT ACTIVITY
         * ============================================================
         */
        private VBox buildRecentActivity() {

                VBox card = new VBox(16);

                card.setPadding(new Insets(20, 24, 20, 24));
                card.setMaxWidth(Double.MAX_VALUE);

                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 16;" +
                                                "-fx-border-color: #E5E7EB;" +
                                                "-fx-border-radius: 16;");
                // -------------------------
                // HEADER
                // -------------------------
                HBox header = new HBox();
                header.setAlignment(Pos.CENTER_LEFT);

                Label title = new Label("Recent Activity");
                title.setStyle(
                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #0B4F43;");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label viewAll = new Label("View All");
                viewAll.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #159A9C;" +
                                                "-fx-cursor: hand;");

                header.getChildren().addAll(
                                title,
                                spacer,
                                viewAll);
                // -------------------------
                // ACTIVITY ITEMS
                // -------------------------
                HBox activity1 = createActivity("💧", "Water bill payment submitted", "Today, 10:30 AM", "#159A9C");

                HBox activity2 = createActivity("📄", "Certificate application submitted", "Yesterday, 4:15 PM",
                                "#7956F5");

                HBox activity3 = createActivity("⚠", "Complaint status updated", "Yesterday, 11:20 AM", "#E67E1F");

                HBox activity4 = createActivity("🏛", "Government scheme application", "2 days ago", "#0B4F43");

                VBox activityList = new VBox(10);
                activityList.getChildren().addAll(
                                activity1,
                                activity2,
                                activity3,
                                activity4);
                card.getChildren().addAll(
                                header,
                                activityList);
                return card;
        }

        /*
         * ============================================================
         * SINGLE ACTIVITY ITEM
         * ============================================================
         */
        private HBox createActivity(String icon, String activityText, String timeText, String accent) {

                HBox item = new HBox(14);
                item.setAlignment(Pos.CENTER_LEFT);
                item.setPadding(new Insets(10, 12, 10, 12));
                item.setStyle("-fx-background-color: #F8FAF9;-fx-background-radius: 10;");

                // -------------------------
                // ICON
                // -------------------------
                StackPane iconBox = new StackPane();
                iconBox.setPrefSize(40, 40);
                iconBox.setMinSize(40, 40);
                iconBox.setStyle(
                                "-fx-background-color: " + rgba(accent, 0.12) + ";" + "-fx-background-radius: 10;");

                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-font-size: 16px;");

                iconBox.getChildren().add(iconLabel);

                // -------------------------
                // TEXT
                // -------------------------
                VBox textBox = new VBox(3);

                Label activity = new Label(activityText);
                activity.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;-fx-text-fill: #285B5B;");

                Label time = new Label(timeText);
                time.setStyle(
                                "-fx-font-size: 11px;" +
                                                "-fx-text-fill: #7A8A87;");

                textBox.getChildren().addAll(
                                activity,
                                time);

                item.getChildren().addAll(
                                iconBox,
                                textBox);

                return item;
        }

        /*
         * ============================================================
         * CHARTS ROW (Tax Collection + Pending Notices)
         * ============================================================
         */
        private HBox buildChartsRow() {
                HBox row = new HBox(24);
                VBox pendingnotice = buildPendingNotices();
                VBox tax = buildTaxCard();
                HBox.setHgrow(pendingnotice, Priority.ALWAYS);
                HBox.setHgrow(tax, Priority.ALWAYS);
                pendingnotice.setPrefWidth(600);
                tax.setPrefWidth(600);
                row.getChildren().addAll(pendingnotice, tax); // add pendingnotice later
                return row;
        }

        /* ----- Project Progress Overview (donut chart) ----- */
        private VBox buildTaxCard() {
                VBox card = new VBox(28);
                card.setPadding(new Insets(32));
                card.setStyle(cardStyle(24));

                Label title = new Label("Tax Collection Goal");
                title.setStyle("-fx-font-family: " + FONT_FAMILY
                                + "; -fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

                HBox body = new HBox(40);
                body.setAlignment(Pos.CENTER_LEFT);

                /* Donut chart drawn on Canvas: 25% green, 50% saffron, 17% empty, 8% red */
                StackPane donut = new StackPane();
                Canvas canvas = new Canvas(192, 192);
                drawDonut(canvas.getGraphicsContext2D());
                VBox center = new VBox(2);
                center.setAlignment(Pos.CENTER);
                Label pct = new Label("73%");
                pct.setStyle("-fx-font-family: " + FONT_FAMILY
                                + "; -fx-font-size: 30px; -fx-font-weight: 900; -fx-text-fill: "
                                + FOREST_DEEP + ";");
                center.getChildren().addAll(pct);
                donut.getChildren().addAll(canvas, center);
                /* Legend */
                VBox legend = new VBox(14);
                legend.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(legend, Priority.ALWAYS);
                legend.getChildren().addAll(
                                legendRow(FOREST_DEEP, "Water Tax", "13 (48%)", false),
                                legendRow(SAFFRON_MAIN, "Property Tax", "8 (40%)", false),
                                legendRow(DELAYED_RED, "Other", "2 (2%)", true));
                body.getChildren().addAll(donut, legend);
                card.getChildren().addAll(title, body);
                addHoverLift(card, 24);
                return card;
        }

        private void drawDonut(GraphicsContext g) {
                double cx = 96, cy = 96, r = 76, stroke = 30;
                g.setLineWidth(stroke);
                g.setLineCap(StrokeLineCap.BUTT);
                // Track (Not Started - light)
                g.setStroke(Color.web(FOREST_DEEP, 0.07));
                g.strokeArc(cx - r, cy - r, 2 * r, 2 * r, 0, 360, ArcType.OPEN);
                // Completed 25% (green) - start at top (90deg), clockwise
                g.setStroke(Color.web(FOREST_DEEP));
                g.strokeArc(cx - r, cy - r, 2 * r, 2 * r, 90, -90, ArcType.OPEN);
                // In Progress 50% (saffron)
                g.setStroke(Color.web(SAFFRON_MAIN, 0.95));
                g.strokeArc(cx - r, cy - r, 2 * r, 2 * r, 0, -180, ArcType.OPEN);
                // Delayed 8% (red)
                g.setStroke(Color.web(DELAYED_RED, 0.95));
                g.strokeArc(cx - r, cy - r, 2 * r, 2 * r, 180, -29, ArcType.OPEN);
        }

        private HBox legendRow(String color, String label, String value, boolean delayed) {
                HBox rowBox = new HBox(12);
                rowBox.setAlignment(Pos.CENTER_LEFT);
                rowBox.setPadding(new Insets(8, 10, 8, 10));
                if (delayed) {
                        rowBox.setStyle("-fx-background-color: rgba(217,76,56,0.10); -fx-background-radius: 8;" +
                                        "-fx-border-color: rgba(217,76,56,0.20); -fx-border-radius: 8;");
                }
                Circle dotC = new Circle(7);
                dotC.setStyle("-fx-fill: " + color + ";");
                Label lbl = new Label(label);
                lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 600;" +
                                "-fx-text-fill: " + (delayed ? DELAYED_RED : FOREST_DEEP) + ";");
                Region grow = new Region();
                HBox.setHgrow(grow, Priority.ALWAYS);
                Label val = new Label(value);
                val.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 800;" +
                                "-fx-text-fill: " + (delayed ? DELAYED_RED : FOREST_DEEP) + ";");
                rowBox.getChildren().addAll(dotC, lbl, grow, val);
                return rowBox;
        }

        /*
         * ============================================================
         * PENDING NOTICES
         * ============================================================
         */
        private VBox buildPendingNotices() {

                VBox pending = new VBox(12);

                pending.setPadding(new Insets(18, 20, 18, 20));
                pending.setMaxWidth(Double.MAX_VALUE);
                pending.setStyle(
                                "-fx-background-color: white;-fx-background-radius: 16;-fx-border-color: #E5E7EB;-fx-border-radius: 16;");

                // -------------------------
                // HEADER
                // -------------------------
                BorderPane heading = new BorderPane();

                Label title = new Label("Pending Notices");
                title.setStyle("-fx-font-size: 18px;-fx-font-weight: bold;-fx-text-fill: #0B4F43;");

                Button viewAll = new Button("View All →");
                viewAll.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #159A9C;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");
                viewAll.setOnAction(e -> System.out.println("View All Pending Notices clicked"));

                heading.setLeft(title);
                heading.setRight(viewAll);

                // -------------------------
                // NOTICE ITEMS
                // -------------------------

                HBox notice1 = buildVillagerNotice("💧", "Water Bill Payment", "₹450 pending", "URGENT", "#D93025");

                HBox notice2 = buildVillagerNotice("🏠", "Property Tax", "₹1,200 pending", "PENDING", "#E67E1F");

                HBox notice3 = buildVillagerNotice("📄", "Certificate Application", "Document verification required",
                                "REVIEW",
                                "#7956F5");

                VBox noticeList = new VBox(8);
                noticeList.getChildren().addAll(
                                notice1,
                                notice2,
                                notice3);

                pending.getChildren().addAll(
                                heading,
                                noticeList);

                return pending;
        }

        /*
         * ============================================================
         * SINGLE VILLAGER NOTICE
         * ============================================================
         */
        private HBox buildVillagerNotice(
                        String icon,
                        String titleText,
                        String detailText,
                        String statusText,
                        String accent) {

                HBox notice = new HBox(12);

                notice.setAlignment(Pos.CENTER_LEFT);
                notice.setPadding(new Insets(10, 12, 10, 12));
                notice.setStyle(
                                "-fx-background-color: #F8FAF9;" +
                                                "-fx-background-radius: 10;");

                // -------------------------
                // ICON
                // -------------------------
                StackPane iconBox = new StackPane();
                iconBox.setPrefSize(40, 40);
                iconBox.setMinSize(40, 40);
                iconBox.setStyle(
                                "-fx-background-color: " +
                                                rgba(accent, 0.12) +
                                                ";" +
                                                "-fx-background-radius: 10;");

                Label iconLabel = new Label(icon);
                iconLabel.setStyle(
                                "-fx-font-size: 16px;");

                iconBox.getChildren().add(iconLabel);

                // -------------------------
                // NOTICE TEXT
                // -------------------------
                VBox textBox = new VBox(3);

                Label title = new Label(titleText);

                title.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #285B5B;");

                Label detail = new Label(detailText);

                detail.setWrapText(true);

                detail.setStyle(
                                "-fx-font-size: 11px;" +
                                                "-fx-text-fill: #7A8A87;");

                textBox.getChildren().addAll(
                                title,
                                detail);

                // -------------------------
                // STATUS
                // -------------------------
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label status = new Label(statusText);
                status.setPadding(
                                new Insets(5, 9, 5, 9));
                status.setStyle(
                                "-fx-background-color: " + rgba(accent, 0.12) + ";" + "-fx-background-radius: 6;" +
                                                "-fx-font-size: 10px;" + "-fx-font-weight: bold;" + "-fx-text-fill: "
                                                + accent + ";");

                notice.getChildren().addAll(
                                iconBox,
                                textBox,
                                spacer,
                                status);

                return notice;
        }
        /*
         * ============================================================
         * HELPERS
         * ============================================================
         */

        /** Glass-panel style shared by all cards. */
        private String cardStyle(int radius) {
                return "-fx-background-color: rgba(255,255,255,0.88);" +
                                "-fx-background-radius: " + radius + ";" +
                                "-fx-border-color: rgba(255,255,255,0.5);" +
                                "-fx-border-radius: " + radius + ";" +
                                "-fx-border-width: 1;" +
                                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.06), 16, 0.1, 0, 4);";
        }

        /** Hover lift effect matching the HTML .stat-card-shadow:hover. */
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

        /** Convert #RRGGBB hex to rgba(r,g,b,a) CSS string. */
        private String rgba(String hex, double alpha) {
                int r = Integer.parseInt(hex.substring(1, 3), 16);
                int g = Integer.parseInt(hex.substring(3, 5), 16);
                int b = Integer.parseInt(hex.substring(5, 7), 16);
                return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
        }
}
