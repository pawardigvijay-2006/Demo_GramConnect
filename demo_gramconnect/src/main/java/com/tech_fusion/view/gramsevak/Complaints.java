package com.tech_fusion.view.gramsevak;


import com.tech_fusion.model.gramsevak.Complaint;

//import com.example.model.ComplaintInfo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
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

public class Complaints {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

    // ============================================================
    // COLORS
    // ============================================================

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

    private static final String FOREST_DEEP = "#0B3D2E";
    private static final String FOREST_LIGHT = "#0F4736";
    private static final String SAFFRON_MAIN = "#E07A1F";
    private static final String CONTEXT_TEAL = "#0E8C8C";
    private static final String AI_VIOLET = "#7C5CFC";
    private static final String DELAYED_RED = "#D94C38";

    // Sidebar gradient
    private static final String SIDEBAR_TOP = "#CDEBD8";
    private static final String SIDEBAR_MID = "#BCE3CC";
    private static final String SIDEBAR_BOT = "#A9D8BD";

    private static final String FONT_FAMILY ="'Inter', 'Segoe UI', 'Arial', sans-serif";
    private Runnable backAction;
    // ============================================================
    // MAIN PAGE
    // ============================================================

    public Scene getComplaintScene(Runnable backAction) {

        this.backAction = backAction;

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: " + BACKGROUND + ";"
        );

        root.setLeft(buildSidebar(backAction));
        root.setCenter(buildMainArea());

        return new Scene(root, screenSize.getWidth(), screenSize.getHeight());
    }


    // ============================================================
    // SIDEBAR
    // ============================================================

    private VBox buildSidebar(Runnable backAction) {

        VBox sidebar = new VBox();

        sidebar.setPrefWidth(288);
        sidebar.setMinWidth(288);
        sidebar.setMaxWidth(288);

        sidebar.setStyle(
                "-fx-background-color: linear-gradient(to bottom, "
                        + SIDEBAR_TOP + ", "
                        + SIDEBAR_MID + ", "
                        + SIDEBAR_BOT + ");"
                        + "-fx-border-color: transparent rgba(11,61,46,0.10) "
                        + "transparent transparent;"
                        + "-fx-border-width: 0 1 0 0;"
                        + "-fx-effect: dropshadow(gaussian, "
                        + "rgba(11,61,46,0.20), 24, 0.2, 4, 0);"
        );


        // ========================================================
        // LOGO
        // ========================================================

        Image logoImage =
                new Image("assets\\images\\gc logo.jpeg");

        ImageView logoIcon =
                new ImageView(logoImage);

        logoIcon.setFitWidth(60);
        logoIcon.setFitHeight(60);
        logoIcon.setPreserveRatio(true);
        logoIcon.setSmooth(true);


        Label logoText =
                new Label("GramConnect");

        logoText.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-text-fill: " + FOREST_DEEP + ";"
                        + "-fx-font-size: 18px;"
                        + "-fx-font-weight: 900;"
        );


        Label subtitle =
                new Label("Village Governance");

        subtitle.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-text-fill: rgba(11,61,46,0.65);"
                        + "-fx-font-size: 9px;"
                        + "-fx-font-weight: 700;"
        );


        VBox logoTextBox =
                new VBox(
                        0,
                        logoText,
                        subtitle
                );


        HBox logo =
                new HBox(
                        8,
                        logoIcon,
                        logoTextBox
                );

        logo.setAlignment(
                Pos.CENTER_LEFT
        );
        VBox logoBox =
                new VBox(logo);

        logoBox.setPadding(
                new Insets(24)
        );
        // ========================================================
        // NAVIGATION
        // ========================================================
        Label dashboardNav =
                navItem("🏠  Dashboard", false);

        dashboardNav.setOnMouseClicked(
                e -> backAction.run()
        );
        Label billNav =
                navItem("🏗  Bill Management", false);

        billNav.setOnMouseClicked(
                e -> Dashboard.homeStage.setScene(
                        new BillManagement()
                                .getBillManagementPage(backAction)
                )
        );


        Label documentNav =
                navItem("📄  Documents Management", false);

        documentNav.setOnMouseClicked(
                e -> Dashboard.homeStage.setScene(
                        new DocumentsManage()
                                .getDocumentPage(backAction)
                )
        );


        // COMPLAINTS = ACTIVE

        Label complaintNav =
                navItem("⚠  Complaints", true);

        complaintNav.setOnMouseClicked(
                e -> Dashboard.homeStage.setScene(
                        new Complaints()
                                .getComplaintScene(backAction)
                )
        );


        Label certificatesNav =
                navItem(
                        "📄  Government Schemes",
                        false
                );

        certificatesNav.setOnMouseClicked(
                e -> Dashboard.homeStage.setScene(
                        new GovScheme()
                                .getSchemeScene(backAction)
                )
        );


        VBox navItems =
                new VBox(
                        6,
                        dashboardNav,
                        billNav,
                        documentNav,
                        complaintNav,
                        certificatesNav
                );

        navItems.setPadding(
                new Insets(16, 12, 16, 12)
        );

        VBox.setVgrow(
                navItems,
                Priority.ALWAYS
        );


         /* ----- CTA + footer links ----- */
        VBox footer = new VBox(10);
        footer.setPadding(new Insets(20, 24, 24, 24));

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: linear-gradient(to right, transparent, rgba(11,61,46,0.25), transparent);");


        VBox smallLinks = new VBox(4);
        smallLinks.setPadding(new Insets(8, 0, 0, 0));
        smallLinks.getChildren().addAll(
            footerLink("\u2699", "Settings"),
            footerLink("\u2753", "Support")
        );

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
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.65);");
        link.getChildren().addAll(ic, lbl);
        String base = "-fx-background-radius: 8; -fx-background-color: transparent; -fx-cursor: hand;";
        link.setStyle(base);
        link.setOnMouseEntered(e -> link.setStyle("-fx-background-radius: 8; -fx-background-color: rgba(255,255,255,0.45); -fx-cursor: hand;"));
        link.setOnMouseExited(e -> link.setStyle(base));
        return link;
}

    

    // ============================================================
    // NAV ITEM
    // ============================================================

    private Label navItem(
            String text,
            boolean active) {

        Label item =
                new Label(text);

        item.setMaxWidth(
                Double.MAX_VALUE
        );

        item.setPadding(
                new Insets(14, 16, 14, 16)
        );


        // ========================================================
        // ACTIVE
        // ========================================================

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
                            + "-fx-cursor: hand;"
            );

        }

        // ========================================================
        // NORMAL
        // ========================================================

        else {

            String baseStyle =
                    "-fx-font-family: " + FONT_FAMILY + ";"
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
                                    + "-fx-cursor: hand;"
                    )
            );


            item.setOnMouseExited(
                    e -> item.setStyle(baseStyle)
            );
        }

        return item;
    }


    // ============================================================
    // MAIN AREA
    // ============================================================

    private BorderPane buildMainArea() {

        BorderPane main =
                new BorderPane();

        main.setTop(
                buildHeader()
        );

        main.setCenter(
                buildScrollableContent()
        );

        return main;
    }


    // ============================================================
    // HEADER
    // ============================================================

    private HBox buildHeader() {

        HBox header =
                new HBox(16);

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setPadding(
                new Insets(14, 28, 14, 28)
        );

        header.setStyle(
                "-fx-background-color: rgba(255,255,255,0.92);"
                        + "-fx-border-color: transparent transparent "
                        + "rgba(255,255,255,0.6) transparent;"
                        + "-fx-effect: dropshadow(gaussian, "
                        + "rgba(11,61,46,0.08), 8, 0.1, 0, 2);"
        );


        // ========================================================
        // SEARCH
        // ========================================================

        HBox searchBox =
                new HBox(8);

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        searchBox.setPadding(
                new Insets(0, 16, 0, 16)
        );

        searchBox.setPrefHeight(38);
        searchBox.setPrefWidth(320);

        searchBox.setStyle(
                "-fx-background-color: " + BACKGROUND + ";"
                        + "-fx-background-radius: 20;"
                        + "-fx-border-color: rgba(11,61,46,0.10);"
                        + "-fx-border-radius: 20;"
                        + "-fx-border-width: 1;"
        );


        Label searchIcon =
                new Label("🔍");

        searchIcon.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-text-fill: rgba(11,61,46,0.5);"
        );


        TextField search =
                new TextField();

        search.setPromptText(
                "Search complaints..."
        );

        search.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-font-size: 12px;"
                        + "-fx-text-fill: " + FOREST_DEEP + ";"
                        + "-fx-prompt-text-fill: rgba(11,61,46,0.40);"
        );

        HBox.setHgrow(
                search,
                Priority.ALWAYS
        );


        searchBox.getChildren().addAll(
                searchIcon,
                search
        );


        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        // ========================================================
        // NOTIFICATION
        // ========================================================

        Label bellIcon =
                new Label("🔔");

        bellIcon.setStyle(
                "-fx-font-size: 15px;"
        );


        StackPane bell =
                new StackPane(bellIcon);

        bell.setPrefSize(38, 38);
        bell.setMaxSize(38, 38);

        bell.setStyle(
                "-fx-background-color: " + BACKGROUND + ";"
                        + "-fx-background-radius: 999;"
                        + "-fx-effect: dropshadow(gaussian, "
                        + "rgba(11,61,46,0.08), 4, 0.1, 0, 1);"
        );


        Label badge =
                new Label("3");

        badge.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-background-color: #D94C38;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 9px;"
                        + "-fx-font-weight: 800;"
                        + "-fx-background-radius: 999;"
                        + "-fx-padding: 1 5 1 5;"
        );


        StackPane bellWithBadge =
                new StackPane(
                        bell,
                        badge
                );

        StackPane.setAlignment(
                badge,
                Pos.TOP_RIGHT
        );


        // ========================================================
        // PROFILE
        // ========================================================

        StackPane avatar =
                new StackPane(
                        new Label("AJ")
                );

        avatar.setPrefSize(34, 34);
        avatar.setMaxSize(34, 34);

        avatar.setStyle(
                "-fx-background-color: " + FOREST_DEEP + ";"
                        + "-fx-background-radius: 18;"
                        + "-fx-border-color: " + SAFFRON_MAIN + ";"
                        + "-fx-border-width: 2;"
                        + "-fx-border-radius: 18;"
        );


        ((Label) avatar.getChildren().get(0))
                .setStyle(
                        "-fx-text-fill: white;"
                                + "-fx-font-size: 12px;"
                                + "-fx-font-weight: bold;"
                );


        Label name =
                new Label("Amit Jadhav");

        name.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-font-size: 13px;"
                        + "-fx-font-weight: 800;"
                        + "-fx-text-fill: " + TEXT_PRIMARY + ";"
        );


        Label role =
                new Label("Gram Sevak");

        role.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-font-size: 11px;"
                        + "-fx-text-fill: " + TEXT_SECONDARY + ";"
        );


        VBox nameBox =
                new VBox(
                        name,
                        role
                );


        Label chevron =
                new Label("▾");

        chevron.setStyle(
                "-fx-text-fill: " + TEXT_SECONDARY + ";"
        );


        HBox profile =
                new HBox(
                        8,
                        avatar,
                        nameBox,
                        chevron
                );

        profile.setAlignment(
                Pos.CENTER_LEFT
        );


        header.getChildren().addAll(
                searchBox,
                spacer,
                bellWithBadge,
                profile
        );

        return header;
    }


    // ============================================================
    // SCROLLABLE CONTENT
    // ============================================================

    private ScrollPane buildScrollableContent() {

        VBox content =
                new VBox(14);

        content.setPadding(
                new Insets(16, 24, 24, 24)
        );

        content.setFillWidth(true);

        content.setStyle(
                "-fx-background-color: transparent;"
        );


        // ========================================================
        // TITLE
        // ========================================================

        BorderPane titlePane =
                new BorderPane();


        VBox titleBox =
                new VBox(6);


        Text title =
                new Text("Complaint Management");

        title.setStyle(
                "-fx-font-size: 32px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-fill: #0B4F43;"
        );


        Text subtitle =
                new Text(
                        "Track, assign and resolve civic issues efficiently"
                );

        subtitle.setStyle(
                "-fx-font-size: 16px;"
                        + "-fx-fill: #657180;"
        );


        titleBox.getChildren().addAll(
                title,
                subtitle
        );


        // ========================================================
        // ADD COMPLAINT BUTTON
        // ========================================================

        Button addComplaintButton =
                new Button("+  Add Complaint");

        addComplaintButton.setStyle(
                "-fx-background-color: #0B4F43;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-radius: 8;"
                        + "-fx-padding: 10 15 10 15;"
                        + "-fx-cursor: hand;"
        );


        addComplaintButton.setOnAction(
                event -> System.out.println(
                        "Add Complaint clicked"
                )
        );


        titlePane.setLeft(
                titleBox
        );

        titlePane.setRight(
                addComplaintButton
        );


        // ========================================================
        // SUMMARY CARDS
        // ========================================================

        HBox summaryCards =
                new HBox(15);


        VBox totalActive =
                createSummaryCard(
                        "Total Active",
                        "142",
                        FOREST_DEEP
                );


        VBox newComplaints =
                createSummaryCard(
                        "New",
                        "28",
                        CONTEXT_TEAL
                );


        VBox inProgress =
                createSummaryCard(
                        "In Progress",
                        "84",
                        DELAYED_RED
                );


        VBox escalated =
                createSummaryCard(
                        "Escalated",
                        "12",
                        AI_VIOLET
                );


        HBox.setHgrow(
                totalActive,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                newComplaints,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                inProgress,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                escalated,
                Priority.ALWAYS
        );


        summaryCards.getChildren().addAll(
                totalActive,
                newComplaints,
                inProgress,
                escalated
        );


        // ========================================================
        // SEARCH & FILTER
        // ========================================================

        VBox filterCard =
                new VBox(12);

        filterCard.setPadding(
                new Insets(18)
        );

        filterCard.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 14;"
                        + "-fx-border-color: #E1E7E4;"
                        + "-fx-border-radius: 14;"
        );


        Label filterTitle =
                new Label(
                        "Search & Filter Complaints"
                );

        filterTitle.setStyle(
                "-fx-font-size: 15px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #0B4F43;"
        );


        HBox filterBox =
                new HBox(10);

        filterBox.setAlignment(
                Pos.CENTER_LEFT
        );


        TextField searchField =
                new TextField();

        searchField.setPromptText(
                "🔍  Search Complaint ID or Category"
        );


        // ========================================================
        // CATEGORY FILTER
        // ========================================================

        ComboBox<String> categoryComboBox =
                new ComboBox<>();

        categoryComboBox.getItems().addAll(
                "All Categories",
                "Water Supply",
                "Street Light",
                "Road Damage",
                "Garbage Collection"
        );

        categoryComboBox.setValue(
                "All Categories"
        );


        // ========================================================
        // STATUS FILTER
        // ========================================================

        ComboBox<String> statusComboBox =
                new ComboBox<>();

        statusComboBox.getItems().addAll(
                "All Statuses",
                "Pending",
                "In Progress",
                "Resolved",
                "Escalated"
        );

        statusComboBox.setValue(
                "All Statuses"
        );


        // ========================================================
        // SEARCH BUTTON
        // ========================================================

        Button searchButton =
                new Button("Search");

        searchButton.setStyle(
                "-fx-background-color: #159A9C;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-radius: 8;"
                        + "-fx-padding: 0 20 0 20;"
                        + "-fx-cursor: hand;"
        );


        searchButton.setOnAction(
                event -> System.out.println(
                        "Complaint search clicked"
                )
        );


        HBox.setHgrow(
                searchField,
                Priority.ALWAYS
        );


        categoryComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        statusComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        searchButton.setMaxWidth(
                Double.MAX_VALUE
        );


        searchField.setMinWidth(150);
        categoryComboBox.setMinWidth(130);
        statusComboBox.setMinWidth(120);
        searchButton.setMinWidth(70);


        filterBox.getChildren().addAll(
                searchField,
                categoryComboBox,
                statusComboBox,
                searchButton
        );


        filterCard.getChildren().addAll(
                filterTitle,
                filterBox
        );


        // ========================================================
        // COMPLAINT LIST SECTION
        // ========================================================

        VBox complaintSection =
                new VBox(12);

        complaintSection.setPadding(
                new Insets(20)
        );

        complaintSection.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 16;"
                        + "-fx-border-color: #E1E7E4;"
                        + "-fx-border-radius: 16;"
        );


        // ========================================================
        // LIST HEADER
        // ========================================================

        BorderPane complaintHeader =
                new BorderPane();


        Label recentTitle =
                new Label(
                        "Recent Complaints"
                );

        recentTitle.setStyle(
                "-fx-font-size: 18px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #0B4F43;"
        );


        Label viewAll =
                new Label(
                        "View All →"
                );

        viewAll.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #159A9C;"
                        + "-fx-cursor: hand;"
        );


        complaintHeader.setLeft(
                recentTitle
        );

        complaintHeader.setRight(
                viewAll
        );


        // ========================================================
        // COMPLAINT LIST
        // ========================================================

        VBox complaintList =
                new VBox(8);


        // ========================================================
        // DATABASE / API CONNECTION POINT
        // ========================================================
        //
        // These are temporary records.
        //
        // Later these can come from:
        //
        // ComplaintService.getComplaints()
        //
        // and each record can be converted using:
        //
        // createComplaintItem(...)
        //
        // ========================================================


        HBox complaint1 =
                createComplaintItem(
                        "C001",
                        "Water Supply",
                        "Pending",
                        "15 Jan 2026"
                );


        HBox complaint2 =
                createComplaintItem(
                        "C002",
                        "Street Light",
                        "In Progress",
                        "14 Oct 2026"
                );


        HBox complaint3 =
                createComplaintItem(
                        "C003",
                        "Road Damage",
                        "Resolved",
                        "12 Oct 2026"
                );


        HBox complaint4 =
                createComplaintItem(
                        "C004",
                        "Garbage Collection",
                        "Escalated",
                        "10 Oct 2026"
                );


        complaintList.getChildren().addAll(
                complaint1,
                complaint2,
                complaint3,
                complaint4
        );


        complaintSection.getChildren().addAll(
                complaintHeader,
                complaintList
        );


        // ========================================================
        // ADD EVERYTHING
        // ========================================================

        content.getChildren().addAll(
                titlePane,
                summaryCards,
                filterCard,
                complaintSection
        );


        // ========================================================
        // SCROLL PANE
        // ========================================================

        ScrollPane scrollPane =
                new ScrollPane(content);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color: transparent;"
        );


        return scrollPane;
    }


    // ============================================================
    // SUMMARY CARD
    // ============================================================

    private static VBox createSummaryCard(
            String titleText,
            String numberText,
            String accent) {

        VBox card =
                new VBox(8);

        card.setPadding(
                new Insets(15)
        );

        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 14;"
                        + "-fx-border-color: #E1E7E4;"
                        + "-fx-border-radius: 14;"
        );


        Label title =
                new Label(titleText);

        title.setStyle(
                "-fx-font-size: 11px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #657180;"
        );


        Label number =
                new Label(numberText);

        number.setStyle(
                "-fx-font-size: 28px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: " + accent + ";"
        );


        Label status =
                new Label(
                        "●  " + titleText
                );

        status.setStyle(
                "-fx-font-size: 11px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: " + accent + ";"
        );


        card.getChildren().addAll(
                title,
                number,
                status
        );


        return card;
    }


    // ============================================================
    // SINGLE COMPLAINT ITEM
    // ============================================================

    private HBox createComplaintItem(
            String id,
            String category,
            String status,
            String date) {


        HBox item =
                new HBox(15);

        HBox.setHgrow(
                item,
                Priority.ALWAYS
        );

        item.setAlignment(
                Pos.CENTER_LEFT
        );

        item.setPadding(
                new Insets(12)
        );

        item.setStyle(
                "-fx-background-color: #F8FAF9;"
                        + "-fx-background-radius: 10;"
        );


        // ========================================================
        // ICON
        // ========================================================

        VBox iconBox =
                new VBox();

        iconBox.setPrefSize(
                45,
                45
        );

        iconBox.setMinSize(
                45,
                45
        );

        iconBox.setAlignment(
                Pos.CENTER
        );


        String statusColor =
                getStatusColor(status);


        iconBox.setStyle(
                "-fx-background-color: "
                        + rgba(statusColor, 0.12)
                        + ";"
                        + "-fx-background-radius: 10;"
        );


        Label icon =
                new Label("⚠");

        icon.setStyle(
                "-fx-font-size: 18px;"
        );


        iconBox.getChildren().add(
                icon
        );


        // ========================================================
        // ID
        // ========================================================

        VBox idBox =
                new VBox(3);

        idBox.setPrefWidth(
                120
        );


        Label idTitle =
                new Label("Complaint ID");

        idTitle.setStyle(
                "-fx-font-size: 10px;"
                        + "-fx-text-fill: #7A8A87;"
        );


        Label idLabel =
                new Label(id);

        idLabel.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #285B5B;"
        );


        idBox.getChildren().addAll(
                idTitle,
                idLabel
        );


        // ========================================================
        // CATEGORY
        // ========================================================

        VBox categoryBox =
                new VBox(3);

        categoryBox.setPrefWidth(
                250
        );


        Label categoryTitle =
                new Label("Category");

        categoryTitle.setStyle(
                "-fx-font-size: 10px;"
                        + "-fx-text-fill: #7A8A87;"
        );


        Label categoryLabel =
                new Label(category);

        categoryLabel.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #285B5B;"
        );


        categoryBox.getChildren().addAll(
                categoryTitle,
                categoryLabel
        );


        // ========================================================
        // DATE
        // ========================================================

        VBox dateBox =
                new VBox(3);

        dateBox.setPrefWidth(
                150
        );


        Label dateTitle =
                new Label("Date");

        dateTitle.setStyle(
                "-fx-font-size: 10px;"
                        + "-fx-text-fill: #7A8A87;"
        );


        Label dateLabel =
                new Label(date);

        dateLabel.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #285B5B;"
        );


        dateBox.getChildren().addAll(
                dateTitle,
                dateLabel
        );


        // ========================================================
        // SPACER
        // ========================================================

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        // ========================================================
        // STATUS
        // ========================================================

        Label statusLabel =
                new Label(status);

        statusLabel.setPadding(
                new Insets(5, 10, 5, 10)
        );

        statusLabel.setStyle(
                "-fx-background-color:"
                        + rgba(statusColor, 0.12)
                        + ";"
                        + "-fx-background-radius:7;"
                        + "-fx-font-size:10px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:"
                        + statusColor
                        + ";"
        );


        // ========================================================
        // VIEW BUTTON
        // ========================================================

        Button viewButton =
                new Button("View");

        viewButton.setStyle(
                "-fx-background-color: #EAF5FC;"
                        + "-fx-text-fill: #1976D2;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-radius: 7;"
                        + "-fx-padding: 7 12 7 12;"
                        + "-fx-cursor: hand;"
        );


       // AFTER
viewButton.setOnAction(
        event -> {

            Complaint selectedComplaint =
                    buildTempComplaint(id, category, status, date);

            Dashboard.homeStage.setScene(
                    new Complaintdetails()
                            .getComplaintDetailsScene(
                                    backAction,
                                    selectedComplaint
                            )
            );
        }
);


        // ========================================================
        // ACTION BOX
        // ========================================================

        HBox actions =
                new HBox(
                        8,
                        viewButton
                );

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );


        // ========================================================
        // ADD EVERYTHING
        // ========================================================

        item.getChildren().addAll(
                iconBox,
                idBox,
                categoryBox,
                dateBox,
                spacer,
                statusLabel,
                actions
        );


        return item;
    }


    // ============================================================
    // STATUS COLOR
    // ============================================================

    private static String getStatusColor(
            String status) {

        if (status.equals("Pending")) {

            return WARNING;

        } else if (status.equals("In Progress")) {

            return CONTEXT_TEAL;

        } else if (status.equals("Resolved")) {

            return "#16803C";

        } else {

            return DELAYED_RED;
        }
    }


    // ============================================================
    // RGBA HELPER
    // ============================================================

    private static String rgba(
            String hex,
            double alpha) {

        int r =
                Integer.parseInt(
                        hex.substring(1, 3),
                        16
                );

        int g =
                Integer.parseInt(
                        hex.substring(3, 5),
                        16
                );

        int b =
                Integer.parseInt(
                        hex.substring(5, 7),
                        16
                );


        return "rgba("
                + r + ","
                + g + ","
                + b + ","
                + alpha
                + ")";
    }
    // ============================================================
// TEMP DATA BUILDER
// ============================================================
//
// Later this becomes something like:
//
//     ComplaintService.getComplaintById(id)
//
// and everything downstream (ComplaintDetails) stays the same,
// because it only ever reads from a Complaint object.
// ============================================================

private Complaint buildTempComplaint(
        String id,
        String category,
        String status,
        String date) {

    return new Complaint(
            id,
            category,
            "Irregular " + category,
            "There has been no regular " + category.toLowerCase()
                    + " service reported in this area. Residents "
                    + "have raised this as an ongoing concern.",
            "Ramesh Patil",
            "9876543210",
            "Suryapuri",
            date,
            date,
            status,
            "High",
            "Water Department",
            "Not Assigned"
    );
}
}
