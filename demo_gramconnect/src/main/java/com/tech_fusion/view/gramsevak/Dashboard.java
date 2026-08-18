package com.tech_fusion.view.gramsevak;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Dashboard extends Application {

        /**
         * Shared Stage reference - every page that needs to change screens gets this.
         */
        public static Stage homeStage;

        /**
         * The built Dashboard scene, cached so back() can return to it without
         * rebuilding.
         */
        private Scene dashboardScene;

        private BorderPane root;
        private BorderPane mainArea;
        private StackPane dashboardLayer;
private VBox profileCard;

        // ================= COLORS (borrowed from SarpanchDashboard.java)
        // =================
        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String FOREST_LIGHT = "#0F4736";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String CONTEXT_TEAL = "#0E8C8C";
        private static final String AI_VIOLET = "#7C5CFC";
        private static final String DELAYED_RED = "#D94C38";

        // Light green sidebar gradient (same as Sarpanch's "requested change" palette)
        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        private static final String BACKGROUND = "#f8f8f8";

        private static final String TEXT_PRIMARY = FOREST_DEEP;
        private static final String TEXT_SECONDARY = "rgba(11,61,46,0.65)";
        private static final String BACKGROUND_IMAGE_PATH = "assets\\images\\backgroundfinalimage.png";

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        @Override
        public void start(Stage stage) throws Exception {
                homeStage = stage;
                homeStage.setScene(getDashboardScene());
                homeStage.setTitle("GramConnect -Gram Sevak Dashboard");
                homeStage.show();

        }

        public Scene getDashboardScene() {

                root = new BorderPane();
                Image backgroundImage = new Image(new File(BACKGROUND_IMAGE_PATH).toURI().toString());
                root.setBackground(new Background(new BackgroundImage(backgroundImage,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(100, 100, true, true, false, true))));

                root.setLeft(buildSidebar());

                // Main area
                mainArea = new BorderPane();
                mainArea.setTop(buildHeader());

                ScrollPane scroller = new ScrollPane(buildMainContent());
                scroller.setFitToWidth(true);
                scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                scroller.setStyle(
                                "-fx-background: transparent; " +
                                                "-fx-background-color: transparent; " +
                                                "-fx-border-color: transparent;");
                mainArea.setCenter(scroller);

                root.setCenter(mainArea);
                // StackPane allows us to place the profile card over the dashboard
dashboardLayer = new StackPane(root);

Scene scene = new Scene(
                dashboardLayer,
                screenSize.getWidth(),
                screenSize.getHeight());
                dashboardScene = scene;
                return scene;
        }

        /** Returns to the cached Dashboard scene without rebuilding it. */
        public void back() {
                homeStage.setTitle("GramConnect - Gram Sevak Dashboard");
                homeStage.setScene(dashboardScene);
        }

        // =================================================================
        private VBox buildSidebar() {
                // Step 1: create the empty container.
                VBox sidebar = new VBox();
                sidebar.setPrefWidth(288);
                sidebar.setMinWidth(288);
                sidebar.setStyle(
                                "-fx-background-color: linear-gradient(to bottom, " + SIDEBAR_TOP + ", " + SIDEBAR_MID
                                                + ", " + SIDEBAR_BOT + ");"
                                                + "-fx-border-color: transparent rgba(11,61,46,0.10) transparent transparent;"
                                                + "-fx-border-width: 0 1 0 0;"
                                                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.20), 24, 0.2, 4, 0);");

                // ---------------- Logo ----------------
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
                                                + "-fx-font-weight: 700;"
                                                + "-fx-letter-spacing: 0.05em;");

                VBox logoTextBox = new VBox(0, logoText, subtitle);
                HBox logo = new HBox(8, logoIcon, logoTextBox);
                logo.setAlignment(Pos.CENTER_LEFT);
                VBox logoBox = new VBox(logo);
                logoBox.setPadding(new Insets(24));

                // ---------------- Nav items ----------------
                // Each item is a local variable so we can attach its own click
                // handler right below, instead of routing every click through
                // one shared handleNavigation(String) switch statement.

                Label dashboardNav = navItem("\uD83C\uDFE0  Dashboard", true);
                dashboardNav.setOnMouseClicked(e -> back());

                Label billNav = navItem("\uD83C\uDFD7  Bill Management", false);
                billNav.setOnMouseClicked(e -> {
                        BillManagement billManagement = new BillManagement();
                        // This is the Runnable being asked for: it captures "how to get
                        // back to the dashboard" without ProjectTransparency needing to
                        // know anything about VillagerDashboard's fields or methods.
                        Runnable backToDashboardAction = () -> back();
                        homeStage.setScene(billManagement.getBillManagementPage(backToDashboardAction));
                        homeStage.setTitle("GramConnect - Bill Management");
                });

                Label documentNav = navItem("\uD83D\uDCAC  Documents Managemnet", false);
                documentNav.setOnMouseClicked(e -> {
                        Runnable backToDashboardAction = () -> back();
                        homeStage.setScene(new DocumentsManage().getDocumentPage(backToDashboardAction));
                        homeStage.setTitle("GramConnect - Documents");
                });

                Label complaintNav = navItem("\u26A0 Complaints", false);
                complaintNav.setOnMouseClicked(e -> {
                        Runnable backToDashboardAction = () -> back();
                        homeStage.setScene(new Complaints().getComplaintScene(backToDashboardAction));
                        homeStage.setTitle("GramConnect - Complaints");
                });

                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", false);
                schemesNav.setOnMouseClicked(e -> {
                        Runnable backToDashboardAction = () -> back();
                        homeStage.setScene(new GovScheme().getSchemeScene(backToDashboardAction));
                        homeStage.setTitle("GramConnect - Government Schemes");
                });

                VBox navItems = new VBox(6,
                                dashboardNav,
                                billNav,
                                documentNav,
                                complaintNav,
                                schemesNav);
                navItems.setPadding(new Insets(16, 12, 16, 12));
                VBox.setVgrow(navItems, Priority.ALWAYS); // let this section stretch to fill leftover space

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

        /**
         * Builds one clickable-looking sidebar row. Returns a single Label node
         * with NO click handler attached - the caller (buildSidebar() above)
         * attaches .setOnMouseClicked(...) itself right after construction, so
         * every nav item's destination is defined right next to the item
         * instead of in one faraway switch statement.
         */
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
                main.setCenter(buildMainContent());
                return main;
        }

        /**
         * Header restyled as a translucent glass bar with a rounded pill search box.
         */
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
                VBox nameBox = new VBox(name, role); // stack name above role

                Label chevron = new Label("\u25BE");
                chevron.setStyle("-fx-text-fill: " + TEXT_SECONDARY + ";");

                HBox profile = new HBox(8, avatar, nameBox, chevron);
                profile.setAlignment(Pos.CENTER_LEFT);
                profile.setPadding(new Insets(5, 8, 5, 8));
                profile.setStyle("-fx-background-color: transparent;"
                                                + "-fx-background-radius: 10;"
                                                + "-fx-cursor: hand;");

                // Open Profile page when the user clicks the name/avatar/chevron area
                profile.setOnMouseClicked(e ->{
                        showProfile();
                });

                // Hover effect
                profile.setOnMouseEntered(e -> profile.setStyle(
                                "-fx-background-color: rgba(11,61,46,0.08);"
                                                + "-fx-background-radius: 10;"
                                                + "-fx-cursor: hand;"));

                profile.setOnMouseExited(e -> profile.setStyle(
                                "-fx-background-color: transparent;"
                                                + "-fx-background-radius: 10;"
                                                + "-fx-cursor: hand;"));

                // Finally: put all pieces into the header, left to right.
                header.getChildren().addAll(searchBox, spacer, bellWithBadge, profile);

                return header;
        }
        private void showProfile() {

        // If profile is already visible, close it
        if (profileCard != null && dashboardLayer.getChildren().contains(profileCard)) {
                dashboardLayer.getChildren().remove(profileCard);
                return;
        }

        profileCard = new VBox(14);
        profileCard.setPrefWidth(310);
        profileCard.setMaxWidth(310);
        profileCard.setPadding(new Insets(18));
         // Let the card height depend only on its content
    profileCard.setMinHeight(Region.USE_PREF_SIZE);
    profileCard.setPrefHeight(Region.USE_COMPUTED_SIZE);
    profileCard.setMaxHeight(Region.USE_PREF_SIZE);

        profileCard.setStyle(
                        "-fx-background-color: white;"
                                        + "-fx-background-radius: 16;"
                                        + "-fx-border-color: rgba(11,61,46,0.10);"
                                        + "-fx-border-radius: 16;"
                                        + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.20), 18, 0.15, 0, 6);");

        // ============================================================
        // PROFILE HEADER
        // ============================================================

        HBox profileHeader = new HBox(12);
        profileHeader.setAlignment(Pos.CENTER_LEFT);

        StackPane profileAvatar = new StackPane();
        profileAvatar.setPrefSize(52, 52);
        profileAvatar.setMinSize(52, 52);
        profileAvatar.setMaxSize(52, 52);

        profileAvatar.setStyle(
                        "-fx-background-color: " + FOREST_DEEP + ";"
                                        + "-fx-background-radius: 30;"
                                        + "-fx-border-color: " + SAFFRON_MAIN + ";"
                                        + "-fx-border-width: 2;"
                                        + "-fx-border-radius: 30;");

        Label avatarText = new Label("AJ");
        avatarText.setStyle(
                        "-fx-text-fill: white;"
                                        + "-fx-font-family: " + FONT_FAMILY + ";"
                                        + "-fx-font-size: 15px;"
                                        + "-fx-font-weight: bold;");

        profileAvatar.getChildren().add(avatarText);

        VBox profileNameBox = new VBox(3);

        Label profileName = new Label("Amit Jadhav");
        profileName.setStyle(
                        "-fx-font-family: " + FONT_FAMILY + ";"
                                        + "-fx-font-size: 17px;"
                                        + "-fx-font-weight: 800;"
                                        + "-fx-text-fill: " + FOREST_DEEP + ";");

        Label profileRole = new Label("Gram Sevak");
        profileRole.setStyle(
                        "-fx-font-family: " + FONT_FAMILY + ";"
                                        + "-fx-font-size: 12px;"
                                        + "-fx-text-fill: " + TEXT_SECONDARY + ";");

        profileNameBox.getChildren().addAll(
                        profileName,
                        profileRole);

        profileHeader.getChildren().addAll(
                        profileAvatar,
                        profileNameBox);

        // ============================================================
        // DIVIDER
        // ============================================================

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setMaxWidth(Double.MAX_VALUE);
        divider.setStyle(
                        "-fx-background-color: rgba(11,61,46,0.10);");

        // ============================================================
        // PERSONAL INFORMATION
        // ============================================================

        Label sectionTitle = new Label("Personal Information");
        sectionTitle.setStyle(
                        "-fx-font-family: " + FONT_FAMILY + ";"
                                        + "-fx-font-size: 13px;"
                                        + "-fx-font-weight: 800;"
                                        + "-fx-text-fill: " + FOREST_DEEP + ";");

        VBox information = new VBox(12);

        information.getChildren().addAll(
                        profileInfoRow("👤", "Full Name", "Amit Jadhav"),
                        profileInfoRow("📱", "Mobile Number", "9876543210"),
                        profileInfoRow("✉", "Email Address", "amit@gmail.com"),
                        profileInfoRow("🏠", "Village / Gram Panchayat", "Suryapuri"),
                        profileInfoRow("👥", "Role", "Gram Sevak"));

        profileCard.getChildren().addAll(
                        profileHeader,
                        divider,
                        sectionTitle,
                        information);

        // ============================================================
        // ADD CARD TO DASHBOARD
        // ============================================================

        dashboardLayer.getChildren().add(profileCard);

        // Position at top-right, just below the header
        StackPane.setAlignment(profileCard, Pos.TOP_RIGHT);
        StackPane.setMargin(
                        profileCard,
                        new Insets(72, 24, 0, 0));
}
private HBox profileInfoRow(
                String icon,
                String labelText,
                String valueText) {

        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        StackPane iconBox = new StackPane();
        iconBox.setPrefSize(32, 32);
        iconBox.setMinSize(32, 32);
        iconBox.setMaxSize(32, 32);

        iconBox.setStyle(
                        "-fx-background-color: rgba(11,61,46,0.08);"
                                        + "-fx-background-radius: 9;");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 14px;");

        iconBox.getChildren().add(iconLabel);

        VBox textBox = new VBox(2);

        Label label = new Label(labelText);
        label.setStyle(
                        "-fx-font-family: " + FONT_FAMILY + ";"
                                        + "-fx-font-size: 10px;"
                                        + "-fx-font-weight: 600;"
                                        + "-fx-text-fill: rgba(11,61,46,0.55);");

        Label value = new Label(valueText);
        value.setWrapText(true);
        value.setStyle(
                        "-fx-font-family: " + FONT_FAMILY + ";"
                                        + "-fx-font-size: 12px;"
                                        + "-fx-font-weight: 700;"
                                        + "-fx-text-fill: " + FOREST_DEEP + ";");

        textBox.getChildren().addAll(
                        label,
                        value);

        row.getChildren().addAll(
                        iconBox,
                        textBox);

        return row;
}

        /*
         * ============================================================
         * MAIN CONTENT
         * ============================================================
         */
        private VBox buildMainContent() {
                VBox main = new VBox(32);
                Image bgImage = new Image(
        getClass()
              .getResource("/assets/images/BackgroundImage.png")
               .toExternalForm()
);

BackgroundImage backgroundImage =
        new BackgroundImage(
        bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        100,
                        100,
                        true,
                        true,
                        false,
                        true
                )
        );

main.setBackground(
        new Background(backgroundImage)
);
                main.setPadding(new Insets(32, 40, 48, 40));
               // main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");

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
                VBox kpi1 = kpiCard("#07352d", "💧", "PENDING BILLS", "5");
                VBox bottom1 = kpiBottom(kpi1);
                Label status1 = new Label("●  Pending");
                status1.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;-fx-text-fill: #ad610c;");
                bottom1.getChildren().add(status1);

                // 3. GOVERNMENT SCHEMES
                VBox kpi3 = kpiCard("#964901", "🏛", "GOVERNMENT SCHEMES", "8");
                VBox bottom3 = kpiBottom(kpi3);
                Label status3 = new Label("●  Active");
                status3.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;-fx-text-fill: #0c682d;");
                bottom3.getChildren().add(status3);

                // 4. OPEN COMPLAINTS
                VBox kpi4 = kpiCard("#7956F5", "⚠", "OPEN COMPLAINTS", "2");
                VBox bottom4 = kpiBottom(kpi4);
                Label status4 = new Label("●  Urgent");
                status4.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;-fx-text-fill: #DC2626;");
                bottom4.getChildren().add(status4);

                // 5. CERTIFICATES ISSUED
                VBox kpi5 = kpiCard("#073f35", "📜", "CERTIFICATES ISSUED", "12");
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
                HBox activity1 = createActivity("💧", "Water bill payment submitted", "Today, 10:30 AM", "#023a3c");

                HBox activity2 = createActivity("📄", "Certificate application submitted", "Yesterday, 4:15 PM",
                                "#210781");

                HBox activity3 = createActivity("⚠", "Complaint status updated", "Yesterday, 11:20 AM", "#763d07");

                HBox activity4 = createActivity("🏛", "Government scheme application", "2 days ago", "#0d473d");

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
