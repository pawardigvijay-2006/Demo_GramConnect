
package com.tech_fusion.view.gramsevak;

import com.tech_fusion.model.gramsevak.Document;

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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class DocumentsManage {

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

    private static final String FONT_FAMILY =
            "'Inter', 'Segoe UI', 'Arial', sans-serif";

    private static final String SAFFRON_MAIN = "#E07A1F";
    private static final String FOREST_DEEP = "#0B3D2E";

    // Sidebar gradient
    private static final String SIDEBAR_TOP = "#CDEBD8";
    private static final String SIDEBAR_MID = "#BCE3CC";
    private static final String SIDEBAR_BOT = "#A9D8BD";

    private static final String CONTEXT_TEAL = "#0E8C8C";
    private static final String DELAYED_RED = "#D94C38";

    private Runnable backAction;
     Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();



    // ============================================================
    // MAIN PAGE
    // ============================================================

    public Scene getDocumentPage(Runnable backAction) {

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
    // Same structure as BillManagement
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

        Image logoImage = new Image(
               "assets\\images\\gc logo.jpeg"
        );

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
                        + "-fx-font-weight: 900;"
        );


        Label subtitle = new Label("Village Governance");

        subtitle.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-text-fill: rgba(11,61,46,0.65);"
                        + "-fx-font-size: 9px;"
                        + "-fx-font-weight: 700;"
        );


        VBox logoTextBox = new VBox(
                0,
                logoText,
                subtitle
        );


        HBox logo = new HBox(
                8,
                logoIcon,
                logoTextBox
        );

        logo.setAlignment(Pos.CENTER_LEFT);


        VBox logoBox = new VBox(logo);

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


        // DOCUMENTS = ACTIVE
        Label documentNav =
                navItem("📄  Documents Management", true);


        documentNav.setOnMouseClicked(
                e -> Dashboard.homeStage.setScene(
                        new DocumentsManage()
                                .getDocumentPage(backAction)
                )
        );


        Label complaintNav =
                navItem("⚠  Complaints", false);

        complaintNav.setOnMouseClicked(
                e -> Dashboard.homeStage.setScene(
                        new Complaints()
                                .getComplaintScene(backAction)
                )
        );


        Label certificatesNav =
                navItem("📄  Government Schemes", false);

        certificatesNav.setOnMouseClicked(
                e -> Dashboard.homeStage.setScene(
                        new GovScheme()
                                .getSchemeScene(backAction)
                )
        );


        VBox navItems = new VBox(
                6,
                dashboardNav,
                billNav,
                documentNav,
                complaintNav,
                certificatesNav
        );

        navItems.setPadding(
                new Insets(16,12,16,12)
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

        Label item = new Label(text);

        item.setMaxWidth(
                Double.MAX_VALUE
        );

        item.setPadding(
                new Insets(14, 16, 14, 16)
        );


        // ========================================================
        // ACTIVE ITEM
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
        // NORMAL ITEM
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
    // TOP HEADER
    // Same as BillManagement
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
                "Search documents..."
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
                "-fx-font-family: " + FONT_FAMILY
                        + ";"
                        + "-fx-font-size: 13px;"
                        + "-fx-font-weight: 800;"
                        + "-fx-text-fill: " + TEXT_PRIMARY + ";"
        );


        Label role =
                new Label("Gram Sevak");

        role.setStyle(
                "-fx-font-family: " + FONT_FAMILY
                        + ";"
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
    // SCROLLABLE DOCUMENT CONTENT
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
        // PAGE TITLE
        // ========================================================

        VBox titleBox =
                new VBox(5);


        Text title =
                new Text("Document Management");

        title.setStyle(
                "-fx-font-size: 24px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-fill: #0B4F43;"
        );


        Text subtitle =
                new Text(
                        "Verify and process citizen document requests."
                );

        subtitle.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-fill: #657180;"
        );


        titleBox.getChildren().addAll(
                title,
                subtitle
        );


        // ========================================================
        // SUMMARY CARDS
        // ========================================================

        HBox summaryCards =
                new HBox(15);


        VBox pendingCard =
                createSummaryCard(
                        "PENDING VERIFICATION",
                        "42",
                        SAFFRON_MAIN
                );


        VBox approvedCard =
                createSummaryCard(
                        "APPROVED DOCUMENTS",
                        "128",
                        CONTEXT_TEAL
                );


        VBox rejectedCard =
                createSummaryCard(
                        "REJECTED REQUESTS",
                        "7",
                        DELAYED_RED
                );


        HBox.setHgrow(
                pendingCard,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                approvedCard,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                rejectedCard,
                Priority.ALWAYS
        );


        summaryCards.getChildren().addAll(
                pendingCard,
                approvedCard,
                rejectedCard
        );


        // ========================================================
        // SEARCH & FILTER DOCUMENTS
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
                        "Search & Filter Documents"
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
                "🔍  Search Citizen or Document"
        );


        // ========================================================
        // DOCUMENT TYPE
        // ========================================================

        ComboBox<String> typeComboBox =
                new ComboBox<>();

        typeComboBox.getItems().addAll(
                "All Types",
                "Income Certificate",
                "Birth Certificate",
                "Caste Certificate"
        );

        typeComboBox.setValue(
                "All Types"
        );


        // ========================================================
        // STATUS
        // ========================================================

        ComboBox<String> statusComboBox =
                new ComboBox<>();

        statusComboBox.getItems().addAll(
                "All Statuses",
                "Pending",
                "Approved",
                "Rejected"
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
                        "Document search clicked"
                )
        );


        HBox.setHgrow(
                searchField,
                Priority.ALWAYS
        );


        typeComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        statusComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        searchButton.setMaxWidth(
                Double.MAX_VALUE
        );


        searchField.setMinWidth(150);
        typeComboBox.setMinWidth(130);
        statusComboBox.setMinWidth(120);
        searchButton.setMinWidth(70);


        filterBox.getChildren().addAll(
                searchField,
                typeComboBox,
                statusComboBox,
                searchButton
        );


        filterCard.getChildren().addAll(
                filterTitle,
                filterBox
        );


        // ========================================================
        // VERIFICATION QUEUE
        // ========================================================

        VBox queueSection =
                new VBox(12);

        queueSection.setPadding(
                new Insets(20)
        );

        queueSection.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 16;"
                        + "-fx-border-color: #E1E7E4;"
                        + "-fx-border-radius: 16;"
        );


        // ========================================================
        // QUEUE HEADER
        // ========================================================

        BorderPane queueHeader =
                new BorderPane();


        Label queueTitle =
                new Label(
                        "Verification Queue"
                );

        queueTitle.setStyle(
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


        queueHeader.setLeft(
                queueTitle
        );

        queueHeader.setRight(
                viewAll
        );


        // ========================================================
        // DATABASE / API CONNECTION POINT
        // ========================================================
        //
        // These are temporary sample records.
        //
        // Later you can replace them with:
        //
        // List<DocumentInfo> documentList =
        //         DocumentService.getPendingRequests();
        //
        // Then:
        //
        // documentList.clear();
        //
        // for (DocumentInfo doc : documentList) {
        //
        //     documentListView.getChildren().add(
        //         createDocumentRequest(
        //             doc.getApplicantName(),
        //             doc.getDocumentType(),
        //             doc.getDateSubmitted(),
        //             doc.getStatus()
        //         )
        //     );
        // }
        //
        // The View / Approve / Reject buttons can later
        // call the appropriate backend/API methods.
        //
        // ========================================================


        VBox documentList =
                new VBox(8);


        documentList.getChildren().addAll(


        createDocumentRequest(new Document(
                "DOC001",
                "Ramesh Kadam",
                "Income Certificate",
                "12 Aug 2026",
                "Pending",
                "9876543210",
                "House No. 12, Suryapuri",
                "Suryapuri",
                "XXXX XXXX 4521",
                "Aadhaar Card",
                "ramesh_kadam_aadhaar.pdf",
                "12 Aug 2026",
                "PDF",
                "Pending"
        )),

        createDocumentRequest(new Document(
                "DOC002",
                "Sunita Pawar",
                "Birth Certificate",
                "10 Aug 2026",
                "Pending",
                "9823456712",
                "House No. 5, Suryapuri",
                "Suryapuri",
                "XXXX XXXX 7734",
                "Aadhaar Card",
                "sunita_pawar_aadhaar.pdf",
                "10 Aug 2026",
                "PDF",
                "Pending"
        )),

        createDocumentRequest(new Document(
                "DOC003",
                "Anil Deshmukh",
                "Caste Certificate",
                "08 Aug 2026",
                "Approved",
                "9812345678",
                "House No. 21, Suryapuri",
                "Suryapuri",
                "XXXX XXXX 9012",
                "Aadhaar Card",
                "anil_deshmukh_aadhaar.pdf",
                "08 Aug 2026",
                "PDF",
                "Approved"
        ))
);
        


        queueSection.getChildren().addAll(
                queueHeader,
                documentList
        );


        // ========================================================
        // ADD EVERYTHING
        // ========================================================

        content.getChildren().addAll(
                titleBox,
                summaryCards,
                filterCard,
                queueSection
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
    // Similar to BillManagement summary cards
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
    // SINGLE DOCUMENT REQUEST
    // ============================================================

  private HBox createDocumentRequest(Document document) {


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
        // DOCUMENT ICON
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


        String accent =
                getStatusColor(document.getStatus());


        iconBox.setStyle(
                "-fx-background-color: "
                        + rgba(accent, 0.12)
                        + ";"
                        + "-fx-background-radius: 10;"
        );


        Label icon =
                new Label("📄");

        icon.setStyle(
                "-fx-font-size: 18px;"
        );


        iconBox.getChildren().add(
                icon
        );


        // ========================================================
        // APPLICANT DETAILS
        // ========================================================

        VBox applicantBox =
                new VBox(3);

        applicantBox.setPrefWidth(
                220
        );


        Label applicant =
                new Label(
                        document.getApplicantName()
                );

        applicant.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #285B5B;"
        );


        Label applicantTitle =
                new Label(
                        "Applicant"
                );

        applicantTitle.setStyle(
                "-fx-font-size: 10px;"
                        + "-fx-text-fill: #7A8A87;"
        );


        applicantBox.getChildren().addAll(
                applicant,
                applicantTitle
        );


        // ========================================================
        // DOCUMENT TYPE
        // ========================================================

        VBox typeBox =
                new VBox(3);

        typeBox.setPrefWidth(
                190
        );


        Label typeTitle =
                new Label(
                        "Document Type"
                );

        typeTitle.setStyle(
                "-fx-font-size: 10px;"
                        + "-fx-text-fill: #7A8A87;"
        );


        Label type =
                new Label(
                        document.getDocumentType()
                );

        type.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #285B5B;"
        );


        typeBox.getChildren().addAll(
                typeTitle,
                type
        );


        // ========================================================
        // DATE SUBMITTED
        // ========================================================

        VBox dateBox =
                new VBox(3);

        dateBox.setPrefWidth(
                130
        );


        Label dateTitle =
                new Label(
                        "Submitted"
                );

        dateTitle.setStyle(
                "-fx-font-size: 10px;"
                        + "-fx-text-fill: #7A8A87;"
        );


        Label date =
                new Label(
                       document.getDateSubmitted()
                );

        date.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #285B5B;"
        );


        dateBox.getChildren().addAll(
                dateTitle,
                date
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
                new Label(document.getStatus());

        statusLabel.setPadding(
                new Insets(5, 10, 5, 10)
        );

        statusLabel.setStyle(
                "-fx-background-color: "
                        + rgba(accent, 0.12)
                        + ";"
                        + "-fx-background-radius: 7;"
                        + "-fx-font-size: 10px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + accent
                        + ";"
        );


        // ========================================================
        // ACTION BUTTONS
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


        Button approveButton =
                new Button("Approve");

        approveButton.setStyle(
                "-fx-background-color: #16803C;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-radius: 7;"
                        + "-fx-padding: 7 12 7 12;"
                        + "-fx-cursor: hand;"
        );


        Button rejectButton =
                new Button("Reject");

        rejectButton.setStyle(
                "-fx-background-color: #D93025;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-radius: 7;"
                        + "-fx-padding: 7 12 7 12;"
                        + "-fx-cursor: hand;"
        );


        // ========================================================
        // TEMPORARY BUTTON ACTIONS
        // ========================================================
        // AFTER
viewButton.setOnAction(
        event -> Dashboard.homeStage.setScene(
                new DocumentView()
                        .getDocumentViewScene(backAction, document)
        )
);

approveButton.setOnAction(
        event -> System.out.println(
                "Approve document: " + document.getApplicantName()
        )
);

rejectButton.setOnAction(
        event -> System.out.println(
                "Reject document: " + document.getApplicantName()
        )
);

        HBox actions =
                new HBox(
                        8,
                        viewButton,
                        approveButton,
                        rejectButton
                );

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );


        // ========================================================
        // ADD EVERYTHING TO DOCUMENT ITEM
        // ========================================================

        item.getChildren().addAll(
                iconBox,
                applicantBox,
                typeBox,
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

        switch (status) {

            case "Approved":
                return CONTEXT_TEAL;

            case "Rejected":
                return DELAYED_RED;

            default:
                return SAFFRON_MAIN;
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
}
