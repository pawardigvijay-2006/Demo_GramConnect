package com.tech_fusion.view.villager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * GramConnect - Villager Dashboard
 *
 * ============================================================
 * HOW JAVAFX LAYOUT WORKS (read this first!)
 * ============================================================
 *
 * Every screen is built out of small pieces (Label, TextField, ProgressBar...)
 * that get grouped together inside "containers" (VBox, HBox, StackPane...).
 *
 * The trick you already noticed is this constructor pattern:
 *
 * VBox box = new VBox(label1, label2, label3);
 *
 * That line means: "Create a VBox, and immediately put label1, label2, and
 * label3 inside it, stacked vertically, in that order." VBox = Vertical Box.
 * HBox works the same way but lays children out side-by-side (Horizontal Box).
 *
 * You will see this pattern everywhere below:
 * 1. First we build the small pieces (a Label for a title, a Label for a
 * value, etc).
 * 2. Then we pass those pieces into a VBox or HBox to group them.
 * 3. That group (which is itself just a "Node") gets passed into the NEXT
 * container up, and so on, until everything is nested inside one
 * root node (a BorderPane) that gets shown in the Scene.
 *
 * So the whole screen is really just one big tree of nodes:
 *
 * BorderPane (root)
 * |-- left: sidebar (VBox containing Labels)
 * `-- center: main area (BorderPane)
 * |-- top: header (HBox containing a TextField, a bell Label, a profile HBox)
 * `-- center: scrollable content (VBox containing all the cards)
 *
 * Nothing here is magic - it is just Java objects being created and handed
 * to each other's constructors or added via .getChildren().addAll(...).
 * ============================================================
 *
 * ARCHITECTURE NOTE: This is a single, self-contained file (one Application,
 * one Stage, one Scene, no FXML, no external CSS) so it's easy to read start
 * to finish. All data below (projects, bills, complaints) is hardcoded mock
 * data - marked with TODO comments showing where a real Service class would
 * plug in later.
 */
public class VillagerDashboard extends Application {

        // ================= COLORS =================
        // Keeping every color as a named constant means we type the hex code
        // once and reuse the name everywhere - easier to read and easier to
        // change the whole theme later.

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

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        /**
         * JavaFX calls this method automatically when the app starts.
         * "primaryStage" is the actual OS window. We build ONE root node
         * (a BorderPane), put it inside ONE Scene, and show it in the Stage.
         */
        @Override
        public void start(Stage primaryStage) {

                // BorderPane splits the window into 5 zones: top, bottom, left,
                // right, center. We only use "left" (sidebar) and "center" (everything else).
                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BACKGROUND + ";");

                root.setLeft(buildSidebar()); // put the sidebar VBox on the left
                root.setCenter(buildMainArea()); // put the header + content on the right

                // One Scene wraps the root node. 1600x1000 is the starting window size.
                Scene scene = new Scene(root, 1500, 850);

                primaryStage.setTitle("GramConnect - Villager Dashboard");
                primaryStage.setScene(scene);
                primaryStage.setMinWidth(1280);
                primaryStage.setMinHeight(800);
                primaryStage.show();
        }

        // =================================================================
        // SIDEBAR
        // =================================================================
        private VBox buildSidebar() {
                // Step 1: create the empty container.
                VBox sidebar = new VBox();
                sidebar.setPrefWidth(230);
                sidebar.setMinWidth(230);
                sidebar.setStyle("-fx-background-color: " + PRIMARY_DARK + ";");

                // The logo is just a Label, wrapped in its own tiny VBox so we can
                // give it padding without affecting the rest of the sidebar.
                Label logo = new Label("\uD83C\uDF3F  GramConnect");
                logo.setStyle("-fx-text-fill: white; -fx-font-size: 17px; -fx-font-weight: bold;");
                VBox logoBox = new VBox(logo); // <-- one child passed straight into VBox

                logoBox.setPadding(new Insets(20, 18, 22, 18));

                // Step 2: build each nav row as a Label (see navItem() below),
                // then pass all 8 of them into one VBox at once. This VBox lays
                // them out top-to-bottom, 4px apart (the "4" is the spacing).
                VBox navItems = new VBox(4,
                                navItem("\uD83C\uDFE0  Dashboard", true),
                                navItem("\uD83C\uDFD7  Project transparency", false),
                                navItem("\uD83D\uDCAC  Complaints", false),
                                navItem("\uD83C\uDF81  Government schemes", false),
                                navItem("\uD83D\uDCDC  Certificates", false),
                                navItem("\uD83D\uDCB3  Bills & Payments", false),
                                navItem("\uD83D\uDCE2  Announcements", false),
                                navItem("\uD83D\uDC65  Gram Sabha", false),
                                navItem("\uD83E\uDD16  AI village assistant", false));
                navItems.setPadding(new Insets(0, 10, 0, 10));
                VBox.setVgrow(navItems, Priority.ALWAYS); // let this section stretch to fill leftover space

                Label emergency = new Label("\u26A0  Emergency assistance");
                emergency.setWrapText(true);
                emergency.setStyle("-fx-text-fill: #FFCC80; -fx-font-size: 13px;");
                VBox emergencyBox = new VBox(emergency);
                emergencyBox.setPadding(new Insets(12, 16, 18, 18));

                // Step 3: the sidebar itself is filled using .getChildren().addAll(...)
                // instead of the constructor - this does the exact same thing as
                // passing them into "new VBox(...)", just written after the fact.
                sidebar.getChildren().addAll(logoBox, navItems, emergencyBox);
                return sidebar;
        }

        /** Builds one clickable-looking sidebar row. Returns a single Label node. */
        private Label navItem(String text, boolean active) {
                Label item = new Label(text);
                item.setMaxWidth(Double.MAX_VALUE);
                item.setPadding(new Insets(10, 14, 10, 18));
                if (active) {
                        item.setStyle("-fx-background-color: " + LIGHT_BLUE + "; -fx-text-fill: "+ PRIMARY
                                        + "; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8;");
                } else {
                        item.setStyle("-fx-text-fill: #C8E6C9; -fx-font-size: 13px; -fx-cursor: hand;");
                        // Simple hover effect: swap the style on mouse enter/exit.
                        item.setOnMouseEntered(e -> item.setStyle(
                                        "-fx-text-fill: white; -fx-font-size: 13px; -fx-cursor: hand; "
                                                        + "-fx-background-color: " + PRIMARY
                                                        + "; -fx-background-radius: 8;"));
                        item.setOnMouseExited(e -> item.setStyle(
                                        "-fx-text-fill: #C8E6C9; -fx-font-size: 13px; -fx-cursor: hand;"));
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
                // HBox lays its children left-to-right. Spacing "16" = 16px gap between them.
                HBox header = new HBox(16);
                header.setAlignment(Pos.CENTER_LEFT);
                header.setPadding(new Insets(14, 28, 14, 28));
                header.setStyle("-fx-background-color: white; -fx-border-color: transparent transparent "
                                + BORDER + " transparent; -fx-border-width: 0 0 1 0;");

                TextField search = new TextField();
                search.setPromptText("Search projects, schemes, services");
                search.setPrefWidth(280);
                search.setStyle("-fx-background-color: " + BACKGROUND + "; -fx-background-radius: 20; "
                                + "-fx-padding: 8 16 8 16; -fx-font-size: 12px;");

                // An empty Region set to grow fills up leftover space - this is how
                // we "push" the bell + profile to the right edge of the header.
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label bell = new Label("\uD83D\uDD14");
                bell.setStyle("-fx-background-color: " + BACKGROUND + "; -fx-background-radius: 20; "
                                + "-fx-padding: 8; -fx-font-size: 15px;");

                // StackPane layers its children on top of each other (centered by
                // default) - here it's just used to give the "RP" label a round
                // colored background, like an avatar bubble.
                StackPane avatar = new StackPane(new Label("RP"));
                avatar.setPrefSize(34, 34);
                avatar.setMaxSize(34, 34);
                avatar.setStyle("-fx-background-color: " + SECONDARY + "; -fx-background-radius: 18;");
                ((Label) avatar.getChildren().get(0))
                                .setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");

                Label name = new Label("Ramesh Patil");
                name.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label role = new Label("Villager, Suryapuri");
                role.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                VBox nameBox = new VBox(name, role); // stack name above role

                HBox profile = new HBox(8, avatar, nameBox); // avatar + name side by side
                profile.setAlignment(Pos.CENTER_LEFT);

                // Finally: put all 4 pieces into the header, left to right.
                header.getChildren().addAll(search, spacer, bell, profile);
                return header;
        }

        private ScrollPane buildScrollableContent() {
                // This VBox stacks 4 big sections vertically, 18px apart:
                // 1. greeting text
                // 2. the row of 4 stat cards
                // 3. the projects list + bills card, side by side
                // 4. the complaints/announcement/gram sabha row
                VBox content = new VBox(18);
                content.setPadding(new Insets(18, 24, 28, 24));

                content.getChildren().addAll(
                                buildGreeting(),
                                buildStatCardsRow(),
                                buildProjectsAndBudgetRow(),
                                buildComplaintsAnnouncementsGramSabhaRow(),
                                buildVillageStatusBar());

                // Wrapping the VBox in a ScrollPane means if the window is too
                // short, the user can scroll instead of content getting clipped.
                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true); // content stretches to match the scroll pane's width
                scrollPane.setStyle("-fx-background-color: transparent;");
                return scrollPane;
        }

        // ---- Greeting ----
        private VBox buildGreeting() {
                Label eyebrow = new Label("NAMASTE, RAMESH");
                eyebrow.setStyle("-fx-text-fill: " + PRIMARY + "; -fx-font-size: 12px; -fx-font-weight: bold;");
                Label title = new Label("Here is what is happening in Suryapuri today");
                title.setStyle("-fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 22px; -fx-font-weight: bold;");
                // "new VBox(2, eyebrow, title)" = a VBox with 2px spacing, containing
                // these two Labels stacked vertically.
                return new VBox(2, eyebrow, title);
        }

        // ---- Stat cards ----
        private HBox buildStatCardsRow() {
                // Build 4 cards (see statCard() below) and pass all 4 into one HBox
                // at once, 14px apart, laid out side by side.
                HBox row = new HBox(14,
                                statCard("\uD83C\uDFD7", "#E3F2FD", SECONDARY, "8", "Ongoing projects"),
                                statCard("\uD83D\uDCCA", "#E8F5E9", PRIMARY, "72%", "Average completion"),
                                statCard("\uD83D\uDCAC", "#FFF3E0", WARNING, "2", "Open complaints"),
                                statCard("\uD83D\uDCB3", "#FFEBEE", ERROR, "\u20B91,350", "Pending bills"));
                return row;
        }

        /** Builds one small stat card: an icon circle, a big number, and a caption. */
        private VBox statCard(String icon, String iconBg, String iconColor, String value, String label) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-text-fill: " + iconColor + ";");

                // The icon circle: a StackPane with one Label centered inside it,
                // sized to 34x34 with rounded corners so it reads as a "chip".
                StackPane iconCircle = new StackPane(iconLabel);
                iconCircle.setPrefSize(34, 34);
                iconCircle.setMaxSize(34, 34);
                iconCircle.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 8;");

                Label valueLabel = new Label(value);
                valueLabel.setStyle("-fx-font-size: 21px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label textLabel = new Label(label);
                textLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                // The whole card is just: icon circle, then value, then label,
                // stacked vertically with 8px gaps, inside a padded white box.
                VBox card = new VBox(8, iconCircle, valueLabel, textLabel);
                card.setPadding(new Insets(14));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                HBox.setHgrow(card, Priority.ALWAYS); // let the 4 cards share the row's width evenly
                return card;
        }

        // ---- Projects list + My Bills card ----
        private HBox buildProjectsAndBudgetRow() {
                // Two big cards, side by side.
                return new HBox(16, buildProjectsCard(), buildMyBillsCard());
        }

        private VBox buildProjectsCard() {

    Label title = new Label("Recent projects");
    title.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";"
    );

    Label viewAll = new Label("View all \u2192");
    viewAll.setStyle(
            "-fx-text-fill: " + SECONDARY + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;"
    );

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox header = new HBox(
            title,
            spacer,
            viewAll
    );

    header.setAlignment(Pos.CENTER_LEFT);


    // -------------------------------------------------
    // PROJECT LIST
    // -------------------------------------------------

    VBox list = new VBox(
            18,

            projectRow(
                    "assets\\images\\road.jpg",
                    "Village road construction",
                    "Main Street, Suryapuri",
                    78,
                    "On Track"
            ),

            projectRow(
                    "assets\\images\\water_tank.jpg",
                    "Water tank renovation",
                    "Near school area",
                    45,
                    "In Progress"
            ),

            projectRow(
                    "assets\\images\\street_light.jpg",
                    "Street light installation",
                    "All village roads",
                    25,
                    "Delayed"
            )
    );


    // -------------------------------------------------
    // CARD
    // -------------------------------------------------

    VBox card = new VBox(
            14,
            header,
            list
    );

    card.setPadding(new Insets(18));

        card.setPrefWidth(580);

    card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 1;"
    );

    HBox.setHgrow(card, Priority.ALWAYS);

    return card;
}

        /**
         * Builds one project row: name + status pill on top, location below, progress
         * bar at the bottom.
         */
        private HBox projectRow(
        String imagePath,
        String name,
        String location,
        int percent,
        String status) {


    // =================================================
    // PROJECT IMAGE
    // =================================================

    ImageView projectImage = new ImageView(
            new Image(imagePath)
    );

    projectImage.setFitWidth(118);
    projectImage.setFitHeight(78);

    projectImage.setPreserveRatio(false);


    // Rounded corners for image
    Rectangle clip = new Rectangle(
            118,
            78
    );

    clip.setArcWidth(12);
    clip.setArcHeight(12);

    projectImage.setClip(clip);


    // =================================================
    // PROJECT NAME
    // =================================================

    Label nameLabel = new Label(name);

    nameLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";"
    );


    // =================================================
    // STATUS COLORS
    // =================================================

    String pillBg;
    String pillFg;
    String barColor;

    switch (status) {

        case "Delayed":

            pillBg = "#FFEBEE";
            pillFg = "#791F1F";
            barColor = ERROR;

            break;


        case "In Progress":

            pillBg = "#FFF3E0";
            pillFg = "#854F0B";
            barColor = SECONDARY;

            break;


        default:

            pillBg = "#E8F5E9";
            pillFg = "#1B5E20";
            barColor = PRIMARY;
    }


    // =================================================
    // STATUS PILL
    // =================================================

    Label statusPill = new Label(status);

    statusPill.setStyle(
            "-fx-background-color: " + pillBg + ";" +
            "-fx-text-fill: " + pillFg + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 3 10 3 10;" +
            "-fx-font-size: 10px;" +
            "-fx-font-weight: bold;"
    );


    // =================================================
    // TOP ROW
    // =================================================

    Region spacer = new Region();

    HBox.setHgrow(
            spacer,
            Priority.ALWAYS
    );

    HBox topRow = new HBox(
            nameLabel,
            spacer,
            statusPill
    );

    topRow.setAlignment(
            Pos.CENTER_LEFT
    );


    // =================================================
    // LOCATION
    // =================================================

    Label locationLabel = new Label(location);

    locationLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + TEXT_SECONDARY + ";"
    );


    // =================================================
    // PROGRESS BAR
    // =================================================

    ProgressBar bar = new ProgressBar(
            percent / 100.0
    );

    bar.setPrefWidth(400);
    bar.setPrefHeight(7);

    bar.setStyle(
            "-fx-accent: " + barColor + ";"
    );


    // =================================================
    // PERCENTAGE
    // =================================================

    Label percentLabel = new Label(
            percent + "%"
    );

    percentLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + TEXT_SECONDARY + ";"
    );


    HBox progressRow = new HBox(
            8,
            bar,
            percentLabel
    );

    progressRow.setAlignment(
            Pos.CENTER_LEFT
    );


    // =================================================
    // PROJECT DETAILS
    // =================================================

    VBox projectDetails = new VBox(
            4,
            topRow,
            locationLabel,
            progressRow
    );

    projectDetails.setAlignment(
            Pos.CENTER_LEFT
    );

    HBox.setHgrow(
            projectDetails,
            Priority.ALWAYS
    );


    // =================================================
    // FINAL PROJECT ROW
    // =================================================

    HBox projectRow = new HBox(
            14,
            projectImage,
            projectDetails
    );

    projectRow.setAlignment(
            Pos.CENTER_LEFT
    );


    return projectRow;
}
        /**
         * Builds the "My Bills" card: a title, 3 bill rows, and a "Pay All Dues"
         * button.
         */
        private VBox buildMyBillsCard() {

                Label title = new Label("My Bills");
                title.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: " + TEXT_PRIMARY + ";");

                // Each bill row below follows the same recipe:
                // 1. a left-side VBox with the bill name + date
                // 2. a right-side VBox with the amount + DUE/PAID status
                // 3. a spacer Region in between so the right side sticks to the edge
                // 4. all 3 passed into one HBox = the finished row

                // Water Bill
                Label waterTitle = new Label("Water Bill");
                waterTitle.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label waterDate = new Label("Sept 2024");
                waterDate.setStyle("-fx-font-size: 9px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                Label waterAmount = new Label("\u20B9240.00");
                waterAmount.setStyle(
                                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label waterStatus = new Label("DUE");
                waterStatus.setStyle("-fx-font-size: 8px; -fx-font-weight: bold; -fx-text-fill: " + ERROR + ";");

                VBox waterLeft = new VBox(2, waterTitle, waterDate);
                VBox waterRight = new VBox(2, waterAmount, waterStatus);
                Region waterSpacer = new Region();
                HBox.setHgrow(waterSpacer, Priority.ALWAYS);

                HBox water = new HBox(waterLeft, waterSpacer, waterRight);
                water.setAlignment(Pos.CENTER_LEFT);
                water.setPadding(new Insets(10));
                water.setStyle("-fx-background-color: " + LIGHT_BLUE + "; -fx-background-radius: 7;");

                // Property Tax
                Label propertyTitle = new Label("Property Tax");
                propertyTitle.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label propertyDate = new Label("Annual 2024");
                propertyDate.setStyle("-fx-font-size: 9px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                Label propertyAmount = new Label("\u20B91,200.00");
                propertyAmount.setStyle(
                                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label propertyStatus = new Label("PAID");
                propertyStatus.setStyle("-fx-font-size: 8px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";");

                VBox propertyLeft = new VBox(2, propertyTitle, propertyDate);
                VBox propertyRight = new VBox(2, propertyAmount, propertyStatus);
                Region propertySpacer = new Region();
                HBox.setHgrow(propertySpacer, Priority.ALWAYS);

                HBox property = new HBox(propertyLeft, propertySpacer, propertyRight);
                property.setAlignment(Pos.CENTER_LEFT);
                property.setPadding(new Insets(10));
                property.setStyle(
                                "-fx-background-color: "+ LIGHT_BLUE +";"+
                                                "-fx-border-color: " + BORDER + ";" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;");

                // Electricity
                Label electricityTitle = new Label("Electricity");
                electricityTitle.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label electricityDate = new Label("Sept 2024");
                electricityDate.setStyle("-fx-font-size: 9px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                Label electricityAmount = new Label("\u20B9650.00");
                electricityAmount.setStyle(
                                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label electricityStatus = new Label("DUE");
                electricityStatus.setStyle("-fx-font-size: 8px; -fx-font-weight: bold; -fx-text-fill: " + ERROR + ";");

                VBox electricityLeft = new VBox(2, electricityTitle, electricityDate);
                VBox electricityRight = new VBox(2, electricityAmount, electricityStatus);
                Region electricitySpacer = new Region();
                HBox.setHgrow(electricitySpacer, Priority.ALWAYS);

                HBox electricity = new HBox(electricityLeft, electricitySpacer, electricityRight);
                electricity.setAlignment(Pos.CENTER_LEFT);
                electricity.setPadding(new Insets(10));
                electricity.setStyle("-fx-background-color: " + LIGHT_BLUE + "; -fx-background-radius: 7;");

                // Pay all button
                javafx.scene.control.Button payButton = new javafx.scene.control.Button("Pay All Dues");
                payButton.setMaxWidth(Double.MAX_VALUE);
                payButton.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: " + PRIMARY + ";" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-border-color: " + PRIMARY + ";" +
                                                "-fx-border-radius: 6;" +
                                                "-fx-background-radius: 6;" +
                                                "-fx-padding: 8;");

                // The whole card: title, then the 3 bill rows, then the button,
                // all stacked vertically 10px apart.
                VBox card = new VBox(10, title, water, property, electricity, payButton);
                card.setPadding(new Insets(16));
                card.setPrefWidth(330);
                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: " + BORDER + ";" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-border-width: 1;");

                return card;
        }

        // ---- Complaints / Announcement / Gram Sabha row ----
        private HBox buildComplaintsAnnouncementsGramSabhaRow() {
                // Three cards side by side.
                return new HBox(16, buildComplaintsCard(), buildAnnouncementCard(), buildGramSabhaCard());
        }

        private VBox buildComplaintsCard() {
                Label title = new Label("Complaint status");
                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                // TODO: replace with ComplaintService.getComplaintsForUser(userId)
                VBox list = new VBox(10,
                                complaintRow("Water leakage in street", "In Progress"),
                                complaintRow("Street light not working", "Resolved"));

                VBox card = new VBox(12, title, list);
                card.setPadding(new Insets(18));
                card.setPrefWidth(300);
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                HBox.setHgrow(card, Priority.ALWAYS);
                return card;
        }

        /** One complaint row: title on the left, status pill pushed to the right. */
        private HBox complaintRow(String title, String status) {
                Label titleLabel = new Label(title);
                titleLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                String pillBg = status.equals("Resolved") ? "#E8F5E9" : "#FFF3E0";
                String pillFg = status.equals("Resolved") ? "#1B5E20" : "#854F0B";
                Label statusLabel = new Label(status);
                statusLabel.setStyle("-fx-background-color: " + pillBg + "; -fx-text-fill: " + pillFg + "; "
                                + "-fx-background-radius: 10; -fx-padding: 3 10 3 10; -fx-font-size: 10px; -fx-font-weight: bold;");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                HBox row = new HBox(titleLabel, spacer, statusLabel);
                row.setAlignment(Pos.CENTER_LEFT);
                return row;
        }

        private VBox buildAnnouncementCard() {
                Label title = new Label("Latest announcement");
                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                // TODO: replace with AnnouncementService.getRecentAnnouncements(villageId)
                Label headline = new Label("Water supply schedule change");
                headline.setWrapText(true);
                headline.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label body = new Label("Supply interrupted in Ward 3 on 25 May.");
                body.setWrapText(true);
                body.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox card = new VBox(8, title, headline, body);
                card.setPadding(new Insets(18));
                card.setPrefWidth(300);
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                HBox.setHgrow(card, Priority.ALWAYS);
                return card;
        }

        private VBox buildGramSabhaCard() {
                Label title = new Label("Upcoming Gram Sabha");
                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label dateLabel = new Label("30");
                dateLabel.setStyle("-fx-text-fill: " + SECONDARY + "; -fx-font-size: 14px; -fx-font-weight: bold;");
                StackPane dateBox = new StackPane(dateLabel);
                dateBox.setPrefSize(40, 40);
                dateBox.setMaxSize(40, 40);
                dateBox.setStyle("-fx-background-color: #E3F2FD; -fx-background-radius: 8;");

                Label eventTitle = new Label("11:00 AM, Panchayat Bhavan");
                eventTitle.setWrapText(true);
                eventTitle.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label eventSub = new Label("Village development discussion");
                eventSub.setWrapText(true);
                eventSub.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                VBox eventBox = new VBox(eventTitle, eventSub);

                HBox body = new HBox(10, dateBox, eventBox);
                body.setAlignment(Pos.CENTER_LEFT);

                VBox card = new VBox(10, title, body);
                card.setPadding(new Insets(18));
                card.setPrefWidth(300);
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                HBox.setHgrow(card, Priority.ALWAYS);
                return card;
        }

        // ================================================================
        // VILLAGE STATUS BAR
        // ================================================================

        private HBox buildVillageStatusBar() {

                // ============================================================
                // TEMPERATURE / WEATHER
                // ============================================================

                Label sun = new Label("☀");
                sun.setStyle(
                                "-fx-font-size: 30px;" +
                                                "-fx-text-fill: white;");

                Label temperature = new Label("32°C");
                temperature.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: white;");

                Label weather = new Label("Sunny Skies | Ganeshpur, MH");
                weather.setStyle(
                                "-fx-font-size: 9px;" +
                                                "-fx-text-fill: #D4E9D7;");

                VBox weatherText = new VBox(
                                1,
                                temperature,
                                weather);

                weatherText.setAlignment(Pos.CENTER_LEFT);

                HBox weatherBox = new HBox(
                                10,
                                sun,
                                weatherText);

                weatherBox.setAlignment(Pos.CENTER_LEFT);

                // ============================================================
                // POWER STATUS
                // ============================================================

                Label powerIcon = new Label("⚡");
                powerIcon.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-text-fill: white;");

                Label powerTitle = new Label("Power Status");
                powerTitle.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: white;");

                Label powerText = new Label("All Good • No scheduled cuts");
                powerText.setStyle(
                                "-fx-font-size: 9px;" +
                                                "-fx-text-fill: #D4E9D7;");

                VBox powerContent = new VBox(
                                2,
                                powerTitle,
                                powerText);

                powerContent.setAlignment(Pos.CENTER_LEFT);

                HBox power = new HBox(
                                8,
                                powerIcon,
                                powerContent);

                power.setAlignment(Pos.CENTER_LEFT);

                power.setPadding(
                                new Insets(10, 14, 10, 14));

                power.setPrefWidth(250);

                power.setStyle(
                                "-fx-background-color: rgba(255,255,255,0.10);" +
                                                "-fx-background-radius: 6;");

                // ============================================================
                // HEALTH CAMP
                // ============================================================

                Label healthIcon = new Label("✚");
                healthIcon.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-text-fill: white;");

                Label healthTitle = new Label("Health Camp");
                healthTitle.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: white;");

                Label healthText = new Label("Free Checkup @ PHC • 10 AM");
                healthText.setStyle(
                                "-fx-font-size: 9px;" +
                                                "-fx-text-fill: #D4E9D7;");

                VBox healthContent = new VBox(
                                2,
                                healthTitle,
                                healthText);

                healthContent.setAlignment(Pos.CENTER_LEFT);

                HBox health = new HBox(
                                8,
                                healthIcon,
                                healthContent);

                health.setAlignment(Pos.CENTER_LEFT);

                health.setPadding(
                                new Insets(10, 14, 10, 14));

                health.setPrefWidth(250);

                health.setStyle(
                                "-fx-background-color: rgba(255,255,255,0.10);" +
                                                "-fx-background-radius: 6;");

                // ============================================================
                // "TODAY IN VILLAGE"
                // ============================================================

                Label today = new Label("Today in Village");

                today.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: white;");

                VBox weatherSection = new VBox(
                                8,
                                today,
                                weatherBox);

                weatherSection.setAlignment(Pos.CENTER_LEFT);

                // ============================================================
                // SPACER
                // ============================================================

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                // ============================================================
                // POWER + HEALTH CARDS
                // ============================================================

                HBox cards = new HBox(
                                12,
                                power,
                                health);

                cards.setAlignment(Pos.CENTER_RIGHT);

                // ============================================================
                // MAIN STATUS BAR
                // ============================================================

                HBox bottom = new HBox(
                                20,
                                weatherSection,
                                spacer,
                                cards);

                bottom.setAlignment(
                                Pos.CENTER_LEFT);

                bottom.setPadding(
                                new Insets(
                                                14,
                                                18,
                                                14,
                                                18));

                bottom.setMinHeight(100);

                bottom.setPrefHeight(100);

                bottom.setMaxWidth(
                                Double.MAX_VALUE);

                bottom.setStyle(
                                "-fx-background-color: " + PRIMARY + ";" +
                                                "-fx-background-radius: 9;");

                return bottom;
        }

        /** Entry point - JavaFX's launch() eventually calls start() above. */
        public static void main(String[] args) {
                launch(args);
        }
}