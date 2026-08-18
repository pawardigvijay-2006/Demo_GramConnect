package com.tech_fusion.view.gramsevak;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;




import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GenerateWaterBillPage extends Application {

    /* =========================================================
       COLORS
       ========================================================= */

    private static final String FOREST_DEEP = "#0B3D2E";
    private static final String FOREST_LIGHT = "#0F4736";

    // NORMAL GREEN FOR GENERATE BILL BUTTON
    private static final String NORMAL_GREEN = "#247C0C";

    private static final String SAFFRON_MAIN = "#E07A1F";

    private static final String BACKGROUND = "#F5F8F6";
    private static final String TEXT_PRIMARY = "#27352F";
    private static final String TEXT_SECONDARY = "#65736D";

    // SARPANCH SIDEBAR COLORS
    private static final String SIDEBAR_TOP = "#CDEBD8";
    private static final String SIDEBAR_MID = "#C2E7D0";
    private static final String SIDEBAR_BOT = "#B7E0C7";

    // BILL CALCULATION GREEN
    private static final String BILL_GREEN = "#E7F5EC";
    private static final String BILL_GREEN_BORDER = "#A8D5B8";

    private static final String FONT_FAMILY =
            "'Inter', 'Segoe UI', 'Arial', sans-serif";

    private Scene waterBillScene;

    /*
     * SCREEN SIZE
     */
    private final Rectangle2D screenSize =
            Screen.getPrimary().getVisualBounds();


    /* =========================================================
       MAIN SCENE
       ========================================================= */

    public Scene getWaterBillScene(Runnable backToDashboardAction) {

        BorderPane root = new BorderPane();

        // =====================================================
        // BACKGROUND IMAGE
        // =====================================================

        try {

            Image bgImage = new Image(
                    getClass()
                            .getResource(
                                    "/assets/images/BackgroundImage.png"
                            )
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

            root.setBackground(
                    new Background(backgroundImage)
            );

        } catch (Exception e) {

            root.setStyle(
                    "-fx-background-color: #F5F8F6;"
            );
        }


        // =====================================================
        // SIDEBAR
        // =====================================================

        root.setLeft(
                buildSidebar(backToDashboardAction)
        );


        // =====================================================
        // RIGHT CONTENT AREA
        // =====================================================

        BorderPane contentArea = new BorderPane();

        contentArea.setStyle(
                "-fx-background-color: transparent;"
        );


        // TOP HEADER
        contentArea.setTop(
                buildTopBar()
        );


        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane(
                        buildMainContent(
                                backToDashboardAction
                        )
                );

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-border-color: transparent;"
        );

        contentArea.setCenter(
                scrollPane
        );

        root.setCenter(
                contentArea
        );


        // =====================================================
        // RESPONSIVE SCENE
        // =====================================================

        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();

        waterBillScene = new Scene(
                root,
                screenWidth,
                screenHeight
        );

        return waterBillScene;
    }


    /* =========================================================
       SIDEBAR
       ========================================================= */

    private VBox buildSidebar(
            Runnable backAction
    ) {

        VBox sidebar = new VBox();

        sidebar.setPrefWidth(288);
        sidebar.setMinWidth(250);
        sidebar.setMaxWidth(288);

        sidebar.setStyle(
                "-fx-background-color: linear-gradient(to bottom, "
                        + SIDEBAR_TOP + ", "
                        + SIDEBAR_MID + ", "
                        + SIDEBAR_BOT + ");"
                        +
                "-fx-border-color: transparent "
                        + "rgba(11,61,46,0.10) "
                        + "transparent transparent;"
                        +
                "-fx-border-width: 0 1 0 0;"
                        +
                "-fx-effect: dropshadow(gaussian, "
                        + "rgba(11,61,46,0.20), "
                        + "24, 0.2, 4, 0);"
        );


        /* =====================================================
           HEADER
           ===================================================== */

        HBox header = new HBox(14);

        header.setPadding(
                new Insets(24)
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );


        StackPane avatar =
                new StackPane();

        Label avatarLabel =
                new Label("AJ");

        avatarLabel.setStyle(
                "-fx-background-color: "
                        + FOREST_DEEP + ";" +
                "-fx-background-radius: 50%;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-alignment: center;" +
                "-fx-min-width: 48px;" +
                "-fx-min-height: 48px;"
        );

        avatar.getChildren().add(
                avatarLabel
        );


        VBox nameBox =
                new VBox(2);


        Label name =
                new Label("Gram Sevak");

        name.setStyle(
                "-fx-font-family: "
                        + FONT_FAMILY + ";" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: 900;" +
                "-fx-text-fill: "
                        + FOREST_DEEP + ";"
        );


        Label role =
                new Label("Gram Panchayat");

        role.setStyle(
                "-fx-font-family: "
                        + FONT_FAMILY + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: rgba(11,61,46,0.65);"
        );


        nameBox.getChildren().addAll(
                name,
                role
        );


        header.getChildren().addAll(
                avatar,
                nameBox
        );


        /* =====================================================
           NAVIGATION
           ===================================================== */

        VBox nav =
                new VBox(6);

        nav.setPadding(
                new Insets(16, 12, 16, 12)
        );


        HBox dashboard =
                navItem(
                        "▦",
                        "Dashboard",
                        false
                );


        HBox billManagement =
                navItem(
                        "▣",
                        "Bill Management",
                        true
                );


        HBox documentManagement =
                navItem(
                        "▤",
                        "Document Management",
                        false
                );


        HBox complaints =
                navItem(
                        "⚠",
                        "Complaints",
                        false
                );


        HBox governmentSchemes =
                navItem(
                        "▣",
                        "Government Schemes",
                        false
                );


        dashboard.setOnMouseClicked(
                e -> {

                    if (backAction != null) {
                        backAction.run();
                    }

                }
        );


        nav.getChildren().addAll(
                dashboard,
                billManagement,
                documentManagement,
                complaints,
                governmentSchemes
        );


        VBox.setVgrow(
                nav,
                Priority.ALWAYS
        );


        /* =====================================================
           FOOTER
           ===================================================== */

        VBox footer =
                new VBox(10);

        footer.setPadding(
                new Insets(
                        20,
                        24,
                        24,
                        24
                )
        );


        Region divider =
                new Region();

        divider.setPrefHeight(1);

        divider.setStyle(
                "-fx-background-color: "
                        + "rgba(11,61,46,0.20);"
        );


        Label createProject =
                new Label(
                        "+   Create Project"
                );

        createProject.setMaxWidth(
                Double.MAX_VALUE
        );

        createProject.setAlignment(
                Pos.CENTER
        );

        createProject.setPadding(
                new Insets(14)
        );

        createProject.setStyle(
                "-fx-background-color: "
                        + FOREST_DEEP + ";" +
                "-fx-background-radius: 12;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 700;" +
                "-fx-cursor: hand;"
        );


        Label support =
                new Label(
                        "?   Support"
                );

        support.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: "
                        + FOREST_DEEP + ";"
        );


        Label settingsLabel =
                new Label(
                        "⚙   Settings"
                );

        settingsLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: rgba(11,61,46,0.70);" +
                "-fx-cursor: hand;"
        );


        footer.getChildren().addAll(
                divider,
                settingsLabel,
                support
        );


        sidebar.getChildren().addAll(
                header,
                nav,
                footer
        );

        return sidebar;
    }


    /* =========================================================
       NAV ITEM
       ========================================================= */

    private HBox navItem(
            String icon,
            String text,
            boolean active
    ) {

        HBox item =
                new HBox(14);

        item.setAlignment(
                Pos.CENTER_LEFT
        );

        item.setPadding(
                new Insets(
                        14,
                        16,
                        14,
                        16
                )
        );

        item.setMaxWidth(
                Double.MAX_VALUE
        );


        Label iconLabel =
                new Label(icon);

        iconLabel.setStyle(
                "-fx-font-size: 17px;" +
                "-fx-text-fill: " +
                (active
                        ? SAFFRON_MAIN
                        : FOREST_DEEP)
                        + ";"
        );


        Label textLabel =
                new Label(text);

        textLabel.setStyle(
                "-fx-font-family: "
                        + FONT_FAMILY + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: "
                        + (active ? "800" : "600")
                        + ";" +
                "-fx-text-fill: "
                        + (active
                        ? SAFFRON_MAIN
                        : "rgba(11,61,46,0.80)")
                        + ";"
        );


        item.getChildren().addAll(
                iconLabel,
                textLabel
        );


        if (active) {

            item.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.78);" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-color: " + SAFFRON_MAIN + ";" +
                    "-fx-border-width: 0 0 0 4;" +
                    "-fx-border-radius: 12;"
            );

        } else {

            item.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;"
            );


            item.setOnMouseEntered(
                    e -> item.setStyle(
                            "-fx-background-color: "
                                    + "rgba(255,255,255,0.45);" +
                            "-fx-background-radius: 10;"
                    )
            );


            item.setOnMouseExited(
                    e -> item.setStyle(
                            "-fx-background-color: transparent;" +
                            "-fx-background-radius: 10;"
                    )
            );
        }


        return item;
    }


    /* =========================================================
       TOP HEADER
       ========================================================= */

    private HBox buildTopBar() {

        HBox header =
                new HBox(16);

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setPadding(
                new Insets(
                        14,
                        28,
                        14,
                        28
                )
        );

        header.setStyle(
                "-fx-background-color: rgba(255,255,255,0.92);" +
                "-fx-border-color: transparent transparent " +
                "rgba(255,255,255,0.6) transparent;" +
                "-fx-effect: dropshadow(gaussian, " +
                "rgba(11,61,46,0.08), " +
                "8, 0.1, 0, 2);"
        );


        /* =====================================================
           SEARCH BOX
           ===================================================== */

        HBox searchBox =
                new HBox(8);

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        searchBox.setPadding(
                new Insets(0, 16, 0, 16)
        );

        searchBox.setPrefWidth(300);
        searchBox.setPrefHeight(38);

        searchBox.setStyle(
                "-fx-background-color: "
                        + BACKGROUND + ";" +
                "-fx-background-radius: 20;" +
                "-fx-border-color: rgba(11,61,46,0.10);" +
                "-fx-border-radius: 20;" +
                "-fx-border-width: 1;"
        );


        Label searchIcon =
                new Label("\uD83D\uDD0D");

        searchIcon.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: rgba(11,61,46,0.60);"
        );


        TextField search =
                new TextField();

        search.setPromptText(
                "Search projects, schemes, services"
        );

        search.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 12px;" +
                "-fx-text-fill: " + FOREST_DEEP + ";" +
                "-fx-prompt-text-fill: rgba(11,61,46,0.40);"
        );

        HBox.setHgrow(
                search,
                Priority.ALWAYS
        );


        searchBox.getChildren().addAll(
                searchIcon,
                search
        );


        /* =====================================================
           SPACER
           ===================================================== */

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        /* =====================================================
           NOTIFICATION
           ===================================================== */

        Label bellIcon =
                new Label("\uD83D\uDD14");

        bellIcon.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-text-fill: "
                        + FOREST_DEEP + ";"
        );


        StackPane bell =
                new StackPane(
                        bellIcon
                );

        bell.setPrefSize(38, 38);
        bell.setMaxSize(38, 38);

        bell.setStyle(
                "-fx-background-color: "
                        + BACKGROUND + ";" +
                "-fx-background-radius: 999;" +
                "-fx-effect: dropshadow(gaussian, " +
                "rgba(11,61,46,0.08), " +
                "4, 0.1, 0, 1);"
        );


        Label badge =
                new Label("3");

        badge.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-background-color: #D94C38;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 9px;" +
                "-fx-font-weight: 800;" +
                "-fx-background-radius: 999;" +
                "-fx-padding: 1 5 1 5;"
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


        /* =====================================================
           PROFILE AVATAR
           ===================================================== */

        Label avatarLabel =
                new Label("AJ");

        avatarLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;"
        );


        StackPane avatar =
                new StackPane(
                        avatarLabel
                );

        avatar.setPrefSize(34, 34);
        avatar.setMaxSize(34, 34);

        avatar.setStyle(
                "-fx-background-color: "
                        + FOREST_DEEP + ";" +
                "-fx-background-radius: 18;" +
                "-fx-border-color: "
                        + SAFFRON_MAIN + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 18;"
        );


        /* =====================================================
           PROFILE NAME
           ===================================================== */

        Label name =
                new Label(
                        "Amit Jadhav"
                );

        name.setStyle(
                "-fx-font-family: "
                        + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 800;" +
                "-fx-text-fill: "
                        + TEXT_PRIMARY + ";"
        );


        Label role =
                new Label(
                        "Gram Sevak"
                );

        role.setStyle(
                "-fx-font-family: "
                        + FONT_FAMILY + ";" +
                "-fx-font-size: 11px;" +
                "-fx-text-fill: "
                        + TEXT_SECONDARY + ";"
        );


        VBox nameBox =
                new VBox(
                        1,
                        name,
                        role
                );


        Label chevron =
                new Label("\u25BE");

        chevron.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY + ";" +
                "-fx-font-size: 11px;"
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

        profile.setPadding(
                new Insets(5, 8, 5, 8)
        );

        profile.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
        );


        profile.setOnMouseClicked(
                e -> showProfile()
        );


        profile.setOnMouseEntered(
                e -> profile.setStyle(
                        "-fx-background-color: "
                                + "rgba(11,61,46,0.08);" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
                )
        );


        profile.setOnMouseExited(
                e -> profile.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
                )
        );


        header.getChildren().addAll(
                searchBox,
                spacer,
                bellWithBadge,
                profile
        );


        return header;
    }


    /* =========================================================
       PROFILE
       ========================================================= */

    private void showProfile() {

        showMessage(
                "Profile",
                "Name: Amit Jadhav\n" +
                "Role: Gram Sevak\n" +
                "Department: Gram Panchayat"
        );
    }


    /* =========================================================
       MAIN CONTENT
       ========================================================= */

    private VBox buildMainContent(
            Runnable backToDashboardAction
    ) {

        VBox main =
                new VBox(20);

        main.setPadding(
                new Insets(
                        28,
                        38,
                        45,
                        38
                )
        );

        main.setStyle(
                "-fx-background-color: transparent;"
        );


        /* =====================================================
           PAGE HEADER
           ===================================================== */

        HBox pageHeader =
                new HBox();

        pageHeader.setAlignment(
                Pos.CENTER_LEFT
        );


        VBox titleBox =
                new VBox(5);


        Label back =
                new Label(
                        "‹  Back to Bill Management"
                );

        back.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #65736D;" +
                "-fx-cursor: hand;"
        );


        back.setOnMouseClicked(
                e -> {

                    if (backToDashboardAction != null) {
                        backToDashboardAction.run();
                    }

                }
        );


        Label title =
                new Label(
                        "Generate Water Bill"
                );

        title.setStyle(
                "-fx-font-family: "
                        + FONT_FAMILY + ";" +
                "-fx-font-size: 30px;" +
                "-fx-font-weight: 900;" +
                "-fx-text-fill: "
                        + FOREST_DEEP + ";"
        );


        Label subtitle =
                new Label(
                        "Create and manage water bills for village residents"
                );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #65736D;"
        );


        titleBox.getChildren().addAll(
                back,
                title,
                subtitle
        );


        Region headerSpacer =
                new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );


        Button cancel =
                new Button("Cancel");

        cancel.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #CBD5D0;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 9 18;" +
                "-fx-cursor: hand;"
        );


        Button saveDraft =
                new Button("Save Draft");

        saveDraft.setStyle(
                "-fx-background-color: "
                        + FOREST_DEEP + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 10 18;" +
                "-fx-cursor: hand;"
        );


        pageHeader.getChildren().addAll(
                titleBox,
                headerSpacer,
                cancel,
                saveDraft
        );


        /* =====================================================
           BODY
           ===================================================== */

        HBox body =
                new HBox(18);

        body.setFillHeight(true);


        VBox left =
                new VBox(18);

        VBox right =
                new VBox(18);


        HBox.setHgrow(
                left,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                right,
                Priority.ALWAYS
        );


        left.setMinWidth(0);
        right.setMinWidth(0);

        left.setPrefWidth(650);
        right.setPrefWidth(350);


        left.getChildren().addAll(
                buildConsumerInformation(),
                buildConnectionDetails()
        );


        right.getChildren().addAll(
                buildBillCalculation(),
                buildPaymentInformation()
        );


        body.getChildren().addAll(
                left,
                right
        );


        /* =====================================================
           BOTTOM BUTTONS
           ===================================================== */

        HBox buttons =
                new HBox(14);

        buttons.setAlignment(
                Pos.CENTER_RIGHT
        );


        // =====================================================
        // GENERATE BILL BUTTON - NORMAL GREEN
        // =====================================================

        Button generate =
                new Button(
                        "▣  Generate Bill"
                );

        generate.setPrefWidth(230);
        generate.setPrefHeight(46);

        generate.setStyle(
                "-fx-background-color: "
                        + FOREST_DEEP + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );


        Button print =
                new Button(
                        "⇩  Print / Download Bill"
                );

        print.setPrefWidth(230);
        print.setPrefHeight(46);

        print.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: "
                        + FOREST_DEEP + ";" +
                "-fx-border-color: #BFCBC5;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );


        generate.setOnAction(
                e -> showMessage(
                        "Water Bill",
                        "Water bill generated successfully!"
                )
        );


        saveDraft.setOnAction(
                e -> showMessage(
                        "Saved",
                        "Water bill saved as draft."
                )
        );


        cancel.setOnAction(
                e -> {

                    if (backToDashboardAction != null) {
                        backToDashboardAction.run();
                    }

                }
        );


        print.setOnAction(
                e -> showMessage(
                        "Print",
                        "Bill is ready for printing/downloading."
                )
        );


        buttons.getChildren().addAll(
                print,
                generate
        );


        main.getChildren().addAll(
                pageHeader,
                body,
                buttons
        );


        return main;
    }


    /* =========================================================
       CONSUMER INFORMATION
       ========================================================= */

    private VBox buildConsumerInformation() {

        VBox box =
                sectionBox(
                        "♙",
                        "Consumer Information"
                );


        GridPane grid =
                createGrid();


        TextField consumerId =
                input(
                        "Enter Consumer ID"
                );


        TextField citizenName =
                input(
                        "Enter full name"
                );


        TextField mobile =
                input(
                        "+91 XXXXX XXXXX"
                );


        ComboBox<String> connectionType =
                combo(
                        "Residential",
                        "Commercial",
                        "Institutional"
                );


        TextField address =
                input(
                        "Enter complete address"
                );


        TextField propertyNo =
                input(
                        "e.g. H.No. 42"
                );


        ComboBox<String> ward =
                combo(
                        "Select Ward",
                        "Ward 1",
                        "Ward 2",
                        "Ward 3",
                        "Ward 4"
                );


        addField(
                grid,
                0,
                0,
                "Consumer ID *",
                consumerId
        );


        addField(
                grid,
                1,
                0,
                "Citizen Name *",
                citizenName
        );


        addField(
                grid,
                0,
                1,
                "Mobile Number",
                mobile
        );


        addField(
                grid,
                1,
                1,
                "Connection Type",
                connectionType
        );


        VBox addressBox =
                fieldBlock(
                        "Address",
                        address
                );


        grid.add(
                addressBox,
                0,
                2,
                2,
                1
        );


        addField(
                grid,
                0,
                3,
                "House / Property Number",
                propertyNo
        );


        addField(
                grid,
                1,
                3,
                "Ward / Village Area",
                ward
        );


        box.getChildren().add(
                grid
        );


        return box;
    }


    /* =========================================================
       WATER CONNECTION DETAILS
       ========================================================= */

    private VBox buildConnectionDetails() {

        VBox box =
                sectionBox(
                        "♧",
                        "Water Connection Details"
                );


        GridPane grid =
                createGrid();


        TextField meterNumber =
                input(
                        "Enter meter number"
                );


        ComboBox<String> billingPeriod =
                combo(
                        "August 2026",
                        "July 2026",
                        "June 2026",
                        "May 2026"
                );


        TextField previousReading =
                input("0");


        TextField currentReading =
                input("0");


        TextField unitsConsumed =
                input("0 Units");

        unitsConsumed.setEditable(false);


        ComboBox<String> connectionStatus =
                combo(
                        "Active",
                        "Inactive",
                        "Disconnected"
                );


        addField(
                grid,
                0,
                0,
                "Meter Number",
                meterNumber
        );


        addField(
                grid,
                1,
                0,
                "Billing Period",
                billingPeriod
        );


        addField(
                grid,
                0,
                1,
                "Previous Meter Reading",
                previousReading
        );


        addField(
                grid,
                1,
                1,
                "Current Meter Reading",
                currentReading
        );


        addField(
                grid,
                0,
                2,
                "Units Consumed",
                unitsConsumed
        );


        addField(
                grid,
                1,
                2,
                "Connection Status",
                connectionStatus
        );


        Runnable calculateUnits =
                () -> {

                    try {

                        double previous =
                                Double.parseDouble(
                                        previousReading.getText()
                                );


                        double current =
                                Double.parseDouble(
                                        currentReading.getText()
                                );


                        double units =
                                Math.max(
                                        0,
                                        current - previous
                                );


                        unitsConsumed.setText(
                                String.format(
                                        "%.0f Units",
                                        units
                                )
                        );


                    } catch (Exception ignored) {

                        unitsConsumed.setText(
                                "0 Units"
                        );
                    }
                };


        previousReading.textProperty()
                .addListener(
                        (obs, oldVal, newVal) ->
                                calculateUnits.run()
                );


        currentReading.textProperty()
                .addListener(
                        (obs, oldVal, newVal) ->
                                calculateUnits.run()
                );


        box.getChildren().add(
                grid
        );


        return box;
    }


    /* =========================================================
       BILL CALCULATION
       ========================================================= */

    private VBox buildBillCalculation() {

        VBox box =
                sectionBox(
                        "▣",
                        "Bill Calculation"
                );


        // =====================================================
        // FOREST GREEN BILL CALCULATION CARD
        // =====================================================

        box.setStyle(
                "-fx-background-color: "
                        + FOREST_DEEP + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: "
                        + BILL_GREEN_BORDER + ";" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, " +
                "rgba(15,64,49,0.18), " +
                "10, 0.1, 0, 3);"
        );


        // =====================================================
        // HEADING - WHITE
        // =====================================================

        if (box.getChildren().get(0) instanceof HBox) {

            HBox heading =
                    (HBox) box.getChildren().get(0);

            for (Node node : heading.getChildren()) {

                if (node instanceof Label) {

                    ((Label) node).setStyle(
                            "-fx-font-size: 18px;" +
                            "-fx-font-weight: 800;" +
                            "-fx-text-fill: white;"
                    );
                }
            }
        }


        GridPane grid =
                new GridPane();

        grid.setVgap(14);
        grid.setHgap(10);


        TextField basic =
                moneyField("100.00");


        TextField usage =
                moneyField("0.00");


        TextField meter =
                moneyField("20.00");


        TextField maintenance =
                moneyField("15.00");


        TextField late =
                moneyField("0.00");


        TextField discount =
                moneyField("0.00");


        addMoneyRow(
                grid,
                0,
                "Basic Water Charge",
                basic
        );


        addMoneyRow(
                grid,
                1,
                "Usage Charge",
                usage
        );


        addMoneyRow(
                grid,
                2,
                "Meter Charge",
                meter
        );


        addMoneyRow(
                grid,
                3,
                "Maintenance Charge",
                maintenance
        );


        addMoneyRow(
                grid,
                4,
                "Late Fee",
                late
        );


        addMoneyRow(
                grid,
                5,
                "Discount / Subsidy",
                discount
        );


        // =====================================================
        // MAKE ALL BILL LABELS WHITE
        // =====================================================

        for (Node node : grid.getChildren()) {

            if (node instanceof Label) {

                ((Label) node).setStyle(
                        "-fx-font-size: 12px;" +
                        "-fx-text-fill: white;"
                );
            }


            if (node instanceof HBox) {

                HBox amountBox =
                        (HBox) node;

                for (Node child :
                        amountBox.getChildren()) {

                    if (child instanceof Label) {

                        ((Label) child).setStyle(
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: white;"
                        );
                    }
                }
            }
        }


        Separator separator =
                new Separator();

        separator.setStyle(
                "-fx-background-color: rgba(255,255,255,0.35);"
        );


        VBox totalBox =
                new VBox(3);

        totalBox.setPadding(
                new Insets(
                        15,
                        8,
                        5,
                        8
                )
        );


        Label totalLabel =
                new Label(
                        "Total Payable"
                );

        totalLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );


        Label total =
                new Label(
                        "₹135.00"
                );

        total.setStyle(
                "-fx-font-size: 25px;" +
                "-fx-font-weight: 900;" +
                "-fx-text-fill: white;"
        );


        totalBox.getChildren().addAll(
                totalLabel,
                total
        );


        box.getChildren().addAll(
                grid,
                separator,
                totalBox
        );


        // =====================================================
        // CALCULATE TOTAL
        // =====================================================

        Runnable calculateTotal =
                () -> {

                    try {

                        double b =
                                Double.parseDouble(
                                        basic.getText()
                                );


                        double u =
                                Double.parseDouble(
                                        usage.getText()
                                );


                        double m =
                                Double.parseDouble(
                                        meter.getText()
                                );


                        double maint =
                                Double.parseDouble(
                                        maintenance.getText()
                                );


                        double lateFee =
                                Double.parseDouble(
                                        late.getText()
                                );


                        double dis =
                                Double.parseDouble(
                                        discount.getText()
                                );


                        double finalAmount =
                                b
                                + u
                                + m
                                + maint
                                + lateFee
                                - dis;


                        total.setText(
                                String.format(
                                        "₹%.2f",
                                        Math.max(
                                                0,
                                                finalAmount
                                        )
                                )
                        );


                    } catch (Exception ignored) {

                    }
                };


        basic.textProperty()
                .addListener(
                        (a, b, c) ->
                                calculateTotal.run()
                );


        usage.textProperty()
                .addListener(
                        (a, b, c) ->
                                calculateTotal.run()
                );


        meter.textProperty()
                .addListener(
                        (a, b, c) ->
                                calculateTotal.run()
                );


        maintenance.textProperty()
                .addListener(
                        (a, b, c) ->
                                calculateTotal.run()
                );


        late.textProperty()
                .addListener(
                        (a, b, c) ->
                                calculateTotal.run()
                );


        discount.textProperty()
                .addListener(
                        (a, b, c) ->
                                calculateTotal.run()
                );


        return box;
    }


    /* =========================================================
       PAYMENT INFORMATION
       ========================================================= */

    private VBox buildPaymentInformation() {

        VBox box =
                sectionBox(
                        "▣",
                        "Payment Info"
                );


        VBox content =
                new VBox(14);


        ComboBox<String> paymentStatus =
                combo(
                        "Unpaid",
                        "Paid",
                        "Partially Paid"
                );


        DatePicker dueDate =
                new DatePicker();

        dueDate.setPromptText(
                "mm/dd/yyyy"
        );

        dueDate.setMaxWidth(
                Double.MAX_VALUE
        );


        TextField referenceId =
                input(
                        "Optional if unpaid"
                );


        content.getChildren().addAll(

                fieldBlock(
                        "Payment Status",
                        paymentStatus
                ),

                fieldBlock(
                        "Due Date",
                        dueDate
                ),

                fieldBlock(
                        "Payment Reference ID",
                        referenceId
                )
        );


        box.getChildren().add(
                content
        );


        return box;
    }


    /* =========================================================
       SECTION BOX
       ========================================================= */

    private VBox sectionBox(
            String icon,
            String title
    ) {

        VBox box =
                new VBox(18);

        box.setPadding(
                new Insets(20)
        );


        box.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #DCE5E0;" +
                "-fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, " +
                "rgba(11,61,46,0.05), " +
                "8, 0.1, 0, 2);"
        );


        HBox heading =
                new HBox(10);

        heading.setAlignment(
                Pos.CENTER_LEFT
        );


        Label iconLabel =
                new Label(icon);

        iconLabel.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-text-fill: "
                        + FOREST_DEEP + ";"
        );


        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 17px;" +
                "-fx-font-weight: 800;" +
                "-fx-text-fill: #27352F;"
        );


        heading.getChildren().addAll(
                iconLabel,
                titleLabel
        );


        box.getChildren().add(
                heading
        );


        return box;
    }


    /* =========================================================
       GRID
       ========================================================= */

    private GridPane createGrid() {

        GridPane grid =
                new GridPane();

        grid.setHgap(18);
        grid.setVgap(15);


        ColumnConstraints c1 =
                new ColumnConstraints();

        c1.setPercentWidth(50);

        c1.setHgrow(
                Priority.ALWAYS
        );


        ColumnConstraints c2 =
                new ColumnConstraints();

        c2.setPercentWidth(50);

        c2.setHgrow(
                Priority.ALWAYS
        );


        grid.getColumnConstraints()
                .addAll(
                        c1,
                        c2
                );


        return grid;
    }


    /* =========================================================
       ADD FIELD
       ========================================================= */

    private void addField(
            GridPane grid,
            int col,
            int row,
            String label,
            Control control
    ) {

        VBox box =
                new VBox(6);


        Label l =
                new Label(label);

        l.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #596861;"
        );


        control.setMaxWidth(
                Double.MAX_VALUE
        );


        box.getChildren().addAll(
                l,
                control
        );


        grid.add(
                box,
                col,
                row
        );
    }


    /* =========================================================
       MONEY ROW
       ========================================================= */

    private void addMoneyRow(
            GridPane grid,
            int row,
            String label,
            TextField field
    ) {

        Label name =
                new Label(label);

        name.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: white;"
        );


        Label rupee =
                new Label("₹");

        rupee.setStyle(
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );


        HBox amount =
                new HBox(5);

        amount.setAlignment(
                Pos.CENTER_RIGHT
        );


        amount.getChildren().addAll(
                rupee,
                field
        );


        grid.add(
                name,
                0,
                row
        );


        grid.add(
                amount,
                1,
                row
        );
    }


    /* =========================================================
       FIELD BLOCK
       ========================================================= */

    private VBox fieldBlock(
            String label,
            Control control
    ) {

        VBox box =
                new VBox(6);


        Label l =
                new Label(label);

        l.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #596861;"
        );


        control.setMaxWidth(
                Double.MAX_VALUE
        );


        box.getChildren().addAll(
                l,
                control
        );


        return box;
    }


    /* =========================================================
       INPUT
       ========================================================= */

    private TextField input(
            String prompt
    ) {

        TextField field =
                new TextField();


        field.setPromptText(
                prompt
        );


        field.setPrefHeight(38);


        field.setStyle(
                "-fx-background-color: #FAFCFB;" +
                "-fx-border-color: #D6E0DB;" +
                "-fx-border-radius: 7;" +
                "-fx-background-radius: 7;" +
                "-fx-padding: 8 10;" +
                "-fx-font-size: 12px;"
        );


        return field;
    }


    /* =========================================================
       MONEY FIELD
       ========================================================= */

    private TextField moneyField(
            String value
    ) {

        TextField field =
                input(value);


        field.setPrefWidth(85);


        field.setAlignment(
                Pos.CENTER_RIGHT
        );


        field.setText(value);


        return field;
    }


    /* =========================================================
       COMBO BOX
       ========================================================= */

    private ComboBox<String> combo(
            String... values
    ) {

        ComboBox<String> combo =
                new ComboBox<>();


        combo.getItems().addAll(
                values
        );


        combo.getSelectionModel()
                .selectFirst();


        combo.setPrefHeight(38);


        combo.setMaxWidth(
                Double.MAX_VALUE
        );


        return combo;
    }


    /* =========================================================
       MESSAGE
       ========================================================= */

    private void showMessage(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);


        alert.showAndWait();
    }


    /* =========================================================
       START
       ========================================================= */

    @Override
    public void start(Stage stage) {

        stage.setTitle(
                "Generate Water Bill - Gram Sevak Portal"
        );


        Scene scene =
                getWaterBillScene(
                        () -> {

                            System.out.println(
                                    "Back to Dashboard"
                            );

                        }
                );


        stage.setScene(scene);


        // =====================================================
        // RESPONSIVE LAPTOP SCREEN
        // =====================================================

        stage.setX(
                screenSize.getMinX()
        );

        stage.setY(
                screenSize.getMinY()
        );

        stage.setWidth(
                screenSize.getWidth()
        );

        stage.setHeight(
                screenSize.getHeight()
        );


        stage.setMaximized(true);


        stage.show();
    }


    /* =========================================================
       MAIN
       ========================================================= */

    public static void main(String[] args) {

        launch(args);
    }
}