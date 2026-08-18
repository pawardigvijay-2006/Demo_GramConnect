package com.tech_fusion.view.gramsevak;
import com.tech_fusion.model.gramsevak.Complaint;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.scene.control.Label;

public class Complaintdetails {

    // ============================================================
    // COLORS  (same palette as Complaints.java)
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
    private static final String SAFFRON_MAIN = "#E07A1F";
    private static final String CONTEXT_TEAL = "#0E8C8C";
    private static final String AI_VIOLET = "#7C5CFC";
    private static final String DELAYED_RED = "#D94C38";

    private static final String SIDEBAR_TOP = "#CDEBD8";
    private static final String SIDEBAR_MID = "#BCE3CC";
    private static final String SIDEBAR_BOT = "#A9D8BD";

    private static final String FONT_FAMILY =
            "'Inter', 'Segoe UI', 'Arial', sans-serif";

    private Runnable backAction;
    private Complaint complaint;
    Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

    // ============================================================
    // MAIN PAGE
    // ============================================================
    //
    // Called like:
    //
    //   Dashboard.homeStage.setScene(
    //       new ComplaintDetails()
    //           .getComplaintDetailsScene(backAction, selectedComplaint)
    //   );
    //
    // "selectedComplaint" is whichever complaint the user clicked
    // View on — that's how C001 vs C002 shows different data on
    // this exact same page.
    // ============================================================

    public Scene getComplaintDetailsScene(
            Runnable backAction,
            Complaint complaint) {

        this.backAction = backAction;
        this.complaint = complaint;

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: " + BACKGROUND + ";"
        );

        root.setLeft(buildSidebar(backAction));
        root.setCenter(buildMainArea());

        return new Scene(root,  screenSize.getWidth(), screenSize.getHeight());
    }

    // ============================================================
    // SIDEBAR  (identical structure to Complaints.java,
    // "Complaints" stays the active nav item)
    // ============================================================

    private VBox buildSidebar(Runnable backAction) {

        VBox sidebar = new VBox();

        sidebar.setPrefWidth(230);
        sidebar.setMinWidth(230);
        sidebar.setMaxWidth(230);

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
                        + "-fx-font-weight: 900;"
        );

        Label subtitle = new Label("Village Governance");
        subtitle.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-text-fill: rgba(11,61,46,0.65);"
                        + "-fx-font-size: 9px;"
                        + "-fx-font-weight: 700;"
        );

        VBox logoTextBox = new VBox(0, logoText, subtitle);

        HBox logo = new HBox(8, logoIcon, logoTextBox);
        logo.setAlignment(Pos.CENTER_LEFT);

        VBox logoBox = new VBox(logo);
        logoBox.setPadding(new Insets(18, 18, 22, 18));

        // ========================================================
        // NAVIGATION
        // ========================================================

        Label dashboardNav = navItem("🏠  Dashboard", false);
        dashboardNav.setOnMouseClicked(e -> backAction.run());

        Label billNav = navItem("🏗  Bill Management", false);
        billNav.setOnMouseClicked(
                e -> Dashboard.homeStage.setScene(
                        new BillManagement()
                                .getBillManagementPage(backAction)
                )
        );

        Label documentNav = navItem("📄  Documents Management", false);
        documentNav.setOnMouseClicked(
                e -> Dashboard.homeStage.setScene(
                        new DocumentsManage()
                                .getDocumentPage(backAction)
                )
        );

        // COMPLAINTS = ACTIVE (this page belongs to the Complaints section)

        Label complaintNav = navItem("⚠  Complaints", true);
        complaintNav.setOnMouseClicked(
                e -> Dashboard.homeStage.setScene(
                        new Complaints()
                                .getComplaintScene(backAction)
                )
        );

        Label certificatesNav = navItem("📄  Government Schemes", false);
        certificatesNav.setOnMouseClicked(
                e -> Dashboard.homeStage.setScene(
                        new GovScheme()
                                .getSchemeScene(backAction)
                )
        );

        VBox navItems = new VBox(
                4,
                dashboardNav,
                billNav,
                documentNav,
                complaintNav,
                certificatesNav
        );

        navItems.setPadding(new Insets(0, 10, 0, 10));
        VBox.setVgrow(navItems, Priority.ALWAYS);

        Label emergency = new Label("⚠  Emergency assistance");
        emergency.setWrapText(true);
        emergency.setPadding(new Insets(10, 12, 10, 12));
        emergency.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-text-fill: " + DELAYED_RED + ";"
                        + "-fx-font-size: 12px;"
                        + "-fx-font-weight: 700;"
                        + "-fx-background-color: rgba(217,76,56,0.12);"
                        + "-fx-background-radius: 10;"
        );

        VBox emergencyBox = new VBox(emergency);
        emergencyBox.setPadding(new Insets(12, 16, 18, 18));

        sidebar.getChildren().addAll(logoBox, navItems, emergencyBox);

        return sidebar;
    }

    // ============================================================
    // NAV ITEM  (identical to Complaints.java)
    // ============================================================

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
                            + "-fx-cursor: hand;"
            );

        } else {

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

            item.setOnMouseExited(e -> item.setStyle(baseStyle));
        }

        return item;
    }

    // ============================================================
    // MAIN AREA
    // ============================================================

    private BorderPane buildMainArea() {

        BorderPane main = new BorderPane();
        main.setTop(buildHeader());
        main.setCenter(buildScrollableContent());
        return main;
    }

    // ============================================================
    // HEADER  (identical to Complaints.java)
    // ============================================================

    private HBox buildHeader() {

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 28, 14, 28));

        header.setStyle(
                "-fx-background-color: rgba(255,255,255,0.92);"
                        + "-fx-border-color: transparent transparent "
                        + "rgba(255,255,255,0.6) transparent;"
                        + "-fx-effect: dropshadow(gaussian, "
                        + "rgba(11,61,46,0.08), 8, 0.1, 0, 2);"
        );

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
                        + "-fx-border-width: 1;"
        );

        Label searchIcon = new Label("🔍");
        searchIcon.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-text-fill: rgba(11,61,46,0.5);"
        );

        TextField search = new TextField();
        search.setPromptText("Search complaints...");
        search.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-font-size: 12px;"
                        + "-fx-text-fill: " + FOREST_DEEP + ";"
                        + "-fx-prompt-text-fill: rgba(11,61,46,0.40);"
        );

        HBox.setHgrow(search, Priority.ALWAYS);
        searchBox.getChildren().addAll(searchIcon, search);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label bellIcon = new Label("🔔");
        bellIcon.setStyle("-fx-font-size: 15px;");

        StackPane bell = new StackPane(bellIcon);
        bell.setPrefSize(38, 38);
        bell.setMaxSize(38, 38);
        bell.setStyle(
                "-fx-background-color: " + BACKGROUND + ";"
                        + "-fx-background-radius: 999;"
                        + "-fx-effect: dropshadow(gaussian, "
                        + "rgba(11,61,46,0.08), 4, 0.1, 0, 1);"
        );

        Label badge = new Label("3");
        badge.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-background-color: #D94C38;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 9px;"
                        + "-fx-font-weight: 800;"
                        + "-fx-background-radius: 999;"
                        + "-fx-padding: 1 5 1 5;"
        );

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
                        + "-fx-border-radius: 18;"
        );

        ((Label) avatar.getChildren().get(0)).setStyle(
                "-fx-text-fill: white;"
                        + "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;"
        );

        Label name = new Label("Amit Jadhav");
        name.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-font-size: 13px;"
                        + "-fx-font-weight: 800;"
                        + "-fx-text-fill: " + TEXT_PRIMARY + ";"
        );

        Label role = new Label("Gram Sevak");
        role.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";"
                        + "-fx-font-size: 11px;"
                        + "-fx-text-fill: " + TEXT_SECONDARY + ";"
        );

        VBox nameBox = new VBox(name, role);

        Label chevron = new Label("▾");
        chevron.setStyle("-fx-text-fill: " + TEXT_SECONDARY + ";");

        HBox profile = new HBox(8, avatar, nameBox, chevron);
        profile.setAlignment(Pos.CENTER_LEFT);

        header.getChildren().addAll(searchBox, spacer, bellWithBadge, profile);

        return header;
    }

    // ============================================================
    // SCROLLABLE CONTENT
    // ============================================================

    private ScrollPane buildScrollableContent() {

        VBox content = new VBox(14);
        content.setPadding(new Insets(16, 24, 24, 24));
        content.setFillWidth(true);
        content.setStyle("-fx-background-color: transparent;");

        // ========================================================
        // BACK BUTTON
        // ========================================================

        Button backButton = new Button("←  Back to Complaints");

        backButton.setStyle(
                "-fx-background-color: white;"
                        + "-fx-text-fill: #0B4F43;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: #E1E7E4;"
                        + "-fx-border-radius: 8;"
                        + "-fx-padding: 8 14 8 14;"
                        + "-fx-cursor: hand;"
        );

        backButton.setOnAction(
                event -> Dashboard.homeStage.setScene(
                        new Complaints()
                                .getComplaintScene(backAction)
                )
        );

        // ========================================================
        // TITLE
        // ========================================================

        VBox titleBox = new VBox(5);

        Text title = new Text("Complaint Details");
        title.setStyle(
                "-fx-font-size: 24px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-fill: #0B4F43;"
        );

        Text subtitle = new Text(
                "View complete information and manage this civic complaint"
        );
        subtitle.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-fill: #657180;"
        );

        titleBox.getChildren().addAll(title, subtitle);

        // ========================================================
        // BASIC INFO CARD
        // ========================================================

        VBox basicInfoCard = new VBox(12);
        basicInfoCard.setPadding(new Insets(20));
        basicInfoCard.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 16;"
                        + "-fx-border-color: #E1E7E4;"
                        + "-fx-border-radius: 16;"
        );

        HBox basicInfoRow = new HBox(30);
        basicInfoRow.setAlignment(Pos.CENTER_LEFT);

        basicInfoRow.getChildren().addAll(
                infoBlock("Complaint ID", complaint.getId()),
                infoBlock("Category", complaint.getCategory()),
                infoBlock("Date Submitted", complaint.getDateSubmitted())
        );

        String statusColor = getStatusColor(complaint.getStatus());

        VBox statusBlock = new VBox(3);
        Label statusTitle = new Label("Current Status");
        statusTitle.setStyle(
                "-fx-font-size: 10px;"
                        + "-fx-text-fill: #7A8A87;"
        );

        Label statusValue = new Label(complaint.getStatus());
        statusValue.setPadding(new Insets(5, 10, 5, 10));
        statusValue.setStyle(
                "-fx-background-color:" + rgba(statusColor, 0.12) + ";"
                        + "-fx-background-radius:7;"
                        + "-fx-font-size:12px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:" + statusColor + ";"
        );

        statusBlock.getChildren().addAll(statusTitle, statusValue);
        basicInfoRow.getChildren().add(statusBlock);

        basicInfoCard.getChildren().add(basicInfoRow);

        // ========================================================
        // DETAILED INFORMATION CARD
        // ========================================================

        VBox detailsCard = new VBox(14);
        detailsCard.setPadding(new Insets(20));
        detailsCard.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 16;"
                        + "-fx-border-color: #E1E7E4;"
                        + "-fx-border-radius: 16;"
        );

        Label detailsTitle = new Label("Complaint Information");
        detailsTitle.setStyle(
                "-fx-font-size: 16px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #0B4F43;"
        );

        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(14);

        int row = 0;
        grid.add(gridLabel("Complaint ID"), 0, row);
        grid.add(gridValue(complaint.getId()), 1, row++);

        grid.add(gridLabel("Complaint Title"), 0, row);
        grid.add(gridValue(complaint.getSubject()), 1, row++);

        grid.add(gridLabel("Submitted By"), 0, row);
        grid.add(gridValue(complaint.getSubmittedBy()), 1, row++);

        grid.add(gridLabel("Contact Information"), 0, row);
        grid.add(gridValue(complaint.getContact()), 1, row++);

        grid.add(gridLabel("Location"), 0, row);
        grid.add(gridValue(complaint.getLocation()), 1, row++);

        grid.add(gridLabel("Date Submitted"), 0, row);
        grid.add(gridValue(complaint.getDateSubmitted()), 1, row++);

        grid.add(gridLabel("Last Updated"), 0, row);
        grid.add(gridValue(complaint.getLastUpdated()), 1, row++);

        detailsCard.getChildren().addAll(detailsTitle, grid);

        // ========================================================
        // DESCRIPTION CARD
        // ========================================================

        VBox descriptionCard = new VBox(10);
        descriptionCard.setPadding(new Insets(20));
        descriptionCard.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 16;"
                        + "-fx-border-color: #E1E7E4;"
                        + "-fx-border-radius: 16;"
        );

        Label descriptionTitle = new Label("Complaint Description");
        descriptionTitle.setStyle(
                "-fx-font-size: 16px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #0B4F43;"
        );

        Label descriptionText = new Label(complaint.getDescription());
        descriptionText.setWrapText(true);
        descriptionText.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-text-fill: " + TEXT_PRIMARY + ";"
        );

        descriptionCard.getChildren().addAll(descriptionTitle, descriptionText);

        // ========================================================
        // ATTACHMENTS CARD
        // ========================================================

        VBox attachmentsCard = new VBox(10);
        attachmentsCard.setPadding(new Insets(20));
        attachmentsCard.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 16;"
                        + "-fx-border-color: #E1E7E4;"
                        + "-fx-border-radius: 16;"
        );

        Label attachmentsTitle = new Label("Attachments");
        attachmentsTitle.setStyle(
                "-fx-font-size: 16px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #0B4F43;"
        );

        
        HBox attachmentBox = new HBox(10);
attachmentBox.setAlignment(Pos.CENTER_LEFT);
attachmentBox.setPadding(new Insets(10));

Label imageIcon = new Label("🖼");
imageIcon.setStyle(
        "-fx-font-size: 22px;"
        + "-fx-text-fill: #1976D2;"
);

VBox attachmentInfo = new VBox(3);

Label attachmentName = new Label("Complaint Photo");
attachmentName.setStyle(
        "-fx-font-size: 13px;"
        + "-fx-font-weight: bold;"
        + "-fx-text-fill: #285B5B;"
);

Label attachmentText = new Label("Image attached");
attachmentText.setStyle(
        "-fx-font-size: 11px;"
        + "-fx-text-fill: #7A8A87;"
);

attachmentInfo.getChildren().addAll(
        attachmentName,
        attachmentText
);

Region attachmentSpacer = new Region();
HBox.setHgrow(
        attachmentSpacer,
        Priority.ALWAYS
);

Button viewPhotoButton = new Button("View Photo");
viewPhotoButton.setStyle(
        "-fx-background-color: #EAF5FC;"
        + "-fx-text-fill: #1976D2;"
        + "-fx-font-weight: bold;"
        + "-fx-background-radius: 7;"
        + "-fx-padding: 7 14 7 14;"
        + "-fx-cursor: hand;"
);

viewPhotoButton.setOnAction(e -> {
    // Open complaint image
});

attachmentBox.getChildren().addAll(
        imageIcon,
        attachmentInfo,
        attachmentSpacer,
        viewPhotoButton
);

        attachmentsCard.getChildren().addAll(attachmentsTitle, attachmentBox);

        // ========================================================
        // ADD EVERYTHING
        // ========================================================

        content.getChildren().addAll(
                backButton,
                titleBox,
                basicInfoCard,
                detailsCard,
                descriptionCard,
                attachmentsCard
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        return scrollPane;
    }

    // ============================================================
    // SMALL HELPERS
    // ============================================================

    private VBox infoBlock(String titleText, String valueText) {

        VBox block = new VBox(3);

        Label title = new Label(titleText);
        title.setStyle(
                "-fx-font-size: 10px;"
                        + "-fx-text-fill: #7A8A87;"
        );

        Label value = new Label(valueText);
        value.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #285B5B;"
        );

        block.getChildren().addAll(title, value);
        return block;
    }

    private Label gridLabel(String text) {

        Label label = new Label(text);
        label.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-text-fill: #7A8A87;"
                        + "-fx-font-weight: bold;"
        );

        return label;
    }

    private Label gridValue(String text) {

        Label label = new Label(text);
        label.setWrapText(true);
        label.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-text-fill: " + TEXT_PRIMARY + ";"
        );

        return label;
    }

    // ============================================================
    // STATUS COLOR  (same logic as Complaints.java)
    // ============================================================

    private static String getStatusColor(String status) {

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
    // RGBA HELPER  (same as Complaints.java)
    // ============================================================

    private static String rgba(String hex, double alpha) {

        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);

        return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
    }
}