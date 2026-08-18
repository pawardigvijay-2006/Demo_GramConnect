package com.tech_fusion.view.sarpanch;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

/**
 * GramConnect - Sarpanch Complaint Details.
 *
 * Reached from a "View Details" click on {@link SarpanchComplaintsPage}'s
 * complaint cards. Shows the full complaint description and photo evidence
 * for a single {@link Complaint}, keeping the summary cards on the list page
 * lightweight.
 *
 * This page is purely structural/UI: {@link Complaint#getPhotoPath()} is not
 * yet populated by any intake channel, so when it's null this screen shows a
 * clean "no photo" placeholder. Once a real photo path is wired in (e.g. once
 * the Sarpanch and Villager complaint flows are connected), the image will
 * render here automatically with no further UI changes needed.
 *
 * Visual language, sidebar and top bar mirror {@link SarpanchComplaintsPage}
 * and the other Sarpanch pages; navigation follows the same Runnable-based
 * pattern used across those pages.
 */
public class SarpanchComplaintDetailPage {

    Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

    private static final String FOREST_DEEP   = "#0B3D2E";
    private static final String FOREST_LIGHT  = "#0F4736";
    private static final String SAFFRON_MAIN  = "#E07A1F";
    private static final String CONTEXT_TEAL  = "#0E8C8C";
    //private static final String AI_VIOLET     = "#7C5CFC";
    private static final String DELAYED_RED   = "#D94C38";
    private static final String SIDEBAR_TOP   = "#CDEBD8";
    private static final String SIDEBAR_MID   = "#Bce3cc";
    private static final String SIDEBAR_BOT   = "#A9D8BD";

    private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

    private static final String BACKGROUND_IMAGE_PATH =
        "/assets/images/BackgroundImage.png";

    private Runnable backToDashboardAction;
    private Runnable backToComplaintsAction;

    /**
     * Builds the Complaint Details scene for a single complaint and returns it.
     *
     * @param complaint               the complaint to show detail for
     * @param backToComplaintsAction  invoked to return to the Complaints list
     * @param backToDashboardAction   invoked when "Dashboard" is clicked in the sidebar
     */
    public Scene getComplaintDetailScene(Complaint complaint, Runnable backToComplaintsAction,
                                          Runnable backToDashboardAction) {
        this.backToDashboardAction = backToDashboardAction;
        this.backToComplaintsAction = backToComplaintsAction;

        BorderPane root = new BorderPane();
        Image backgroundImage = new Image(BACKGROUND_IMAGE_PATH);
        root.setBackground(new Background(new BackgroundImage(backgroundImage,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundPosition.CENTER,
                                                            new BackgroundSize(100, 100, true, true, false, true)
        )));

        root.setLeft(buildSidebar());

        BorderPane contentArea = new BorderPane();
        contentArea.setTop(buildTopBar());

        ScrollPane scroller = new ScrollPane(buildMainContent(complaint));
        scroller.setFitToWidth(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setStyle(
                "-fx-background: transparent; " +
                "-fx-background-color: transparent; " +
                "-fx-border-color: transparent;"
        );
        contentArea.setCenter(scroller);

        root.setCenter(contentArea);

        return new Scene(root, screenSize.getWidth(), screenSize.getHeight());
    }

    /* ============================================================
     *  SIDEBAR (identical to SarpanchComplaintsPage, "Complaints" active)
     * ============================================================ */
    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(288);
        sidebar.setMinWidth(288);
        sidebar.setStyle(
            "-fx-background-color: linear-gradient(to bottom, " + SIDEBAR_TOP + ", " + SIDEBAR_MID + ", " + SIDEBAR_BOT + ");" +
            "-fx-border-color: transparent rgba(11,61,46,0.10) transparent transparent;" +
            "-fx-border-width: 0 1 0 0;" +
            "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.20), 24, 0.2, 4, 0);"
        );

        HBox header = new HBox(14);
        header.setPadding(new Insets(24));
        header.setAlignment(Pos.CENTER_LEFT);

        StackPane avatar = new StackPane();
        Circle avatarCircle = new Circle(24);
        avatarCircle.setFill(Color.web(FOREST_DEEP));
        avatarCircle.setStroke(Color.web(SAFFRON_MAIN, 0.85));
        avatarCircle.setStrokeWidth(2.5);
        Label avatarInitials = new Label("SP");
        avatarInitials.setFont(Font.font("Inter", FontWeight.BOLD, 16));
        avatarInitials.setTextFill(Color.WHITE);
        avatar.getChildren().addAll(avatarCircle, avatarInitials);

        VBox nameBox = new VBox(2);
        Label name = new Label("Sarpanch Patil");
        name.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label role = new Label("Gram Panchayat");
        role.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.65); -fx-letter-spacing: 0.05em;");
        nameBox.getChildren().addAll(name, role);

        header.getChildren().addAll(avatar, nameBox);

        VBox nav = new VBox(6);
        nav.setPadding(new Insets(16, 12, 16, 12));

        HBox dashboardNav = navItem("\u25A6", "Dashboard", false);
        dashboardNav.setOnMouseClicked(e -> backToDashboardAction.run());

        HBox projectTrackerNav = navItem("\uD83D\uDDC2", "Project Tracker", false);
        projectTrackerNav.setOnMouseClicked(e -> {
            ProjectTrackerPage projectTrackerPage = new ProjectTrackerPage();
            SarpanchDashboard.myStage.setScene(projectTrackerPage.getProjectTrackerScene(backToDashboardAction));
        });

        HBox citizenServicesNav = navItem("\uD83D\uDCC4", "Bills & Payments", false);
        citizenServicesNav.setOnMouseClicked(e -> {
            CitizenServicesPage citizenServicesPage = new CitizenServicesPage();
            SarpanchDashboard.myStage.setTitle("GramConnect ");
            SarpanchDashboard.myStage.setScene(citizenServicesPage.getCitizenServicesScene(backToDashboardAction));
        });

        HBox complaintsNav = navItem("\u26A0", "Complaints", true);
        complaintsNav.setOnMouseClicked(e -> backToComplaintsAction.run());

        HBox announcementsNav = navItem("\uD83D\uDCE2", "Announcements", false);
        announcementsNav.setOnMouseClicked(e -> {
            AnnouncementsPage announcementsPage = new AnnouncementsPage();
            SarpanchDashboard.myStage.setTitle("GramConnect - Announcements");
            SarpanchDashboard.myStage.setScene(announcementsPage.getAnnouncementsScene(backToDashboardAction));
        });

        nav.getChildren().addAll(
            dashboardNav,
            projectTrackerNav,
            complaintsNav,
            citizenServicesNav,
            announcementsNav
        );
        VBox.setVgrow(nav, Priority.ALWAYS);

        sidebar.getChildren().addAll(header, nav);
        return sidebar;
    }

    private HBox navItem(String icon, String text, boolean active) {
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
        item.setStyle("-fx-cursor: hand;");

        if (active) {
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
            item.setOnMouseEntered(e -> item.setStyle("-fx-background-radius: 10; -fx-background-color: rgba(255,255,255,0.45); -fx-cursor: hand;"));
            item.setOnMouseExited(e -> item.setStyle(base));
            return item;
        }
    }

    /* ============================================================
     *  TOP NAVIGATION BAR (identical to SarpanchComplaintsPage)
     * ============================================================ */
    private HBox buildTopBar() {
        HBox topBar = new HBox(24);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(72);
        topBar.setPadding(new Insets(0, 32, 0, 32));
        topBar.setStyle(
            "-fx-background-color: rgba(255,255,255,0.92);" +
            "-fx-border-color: transparent transparent rgba(255,255,255,0.6) transparent;" +
            "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 8, 0.1, 0, 2);"
        );

        Image projectLogo = new Image("assets/images/ProjectLogo.png");
        ImageView imgView = new ImageView(projectLogo);
        imgView.setFitHeight(50);
        imgView.setFitWidth(60);

        Label crumb = new Label("Complaints  /  Complaint Details");
        crumb.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: rgba(11,61,46,0.55);");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

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

        topBar.getChildren().addAll(imgView, crumb, spacer, bell);
        return topBar;
    }

    /* ============================================================
     *  MAIN CONTENT
     * ============================================================ */
    private VBox buildMainContent(Complaint c) {
        VBox main = new VBox(28);
        main.setPadding(new Insets(32, 40, 48, 40));
        main.setStyle("-fx-background-color: rgba(240,244,242,0.52);");

        Label backLink = new Label("\u2190   Back to Complaints");
        backLink.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: " + CONTEXT_TEAL + "; -fx-cursor: hand;");
        backLink.setOnMouseClicked(e -> backToComplaintsAction.run());

        main.getChildren().addAll(backLink, buildHeaderCard(c), buildDetailGrid(c), buildActionCard(c));
        return main;
    }

    /** Remark box + status actions, moved here from the complaint card. */
    private VBox buildActionCard(Complaint c) {
        VBox card = card();
        card.setSpacing(12);

        Label heading = new Label("\u2699  Take Action");
        heading.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 17px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        javafx.scene.control.TextField remarkField = new javafx.scene.control.TextField();
        remarkField.setPromptText("Add a note for this complaint (visible to the villager)...");
        remarkField.setStyle("-fx-background-color: white; -fx-background-radius: 9; -fx-border-color: rgba(11,61,46,0.16); -fx-border-radius: 9;" +
            " -fx-padding: 9 12 9 12; -fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-text-fill: " + FOREST_DEEP + ";");
        HBox.setHgrow(remarkField, Priority.ALWAYS);

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_LEFT);
        if ("Pending".equalsIgnoreCase(c.getStatus())) {
            actions.getChildren().add(actionButton("Mark In Progress", CONTEXT_TEAL, () -> apply(c, "In Progress", remarkField)));
            actions.getChildren().add(actionButton("Resolve", FOREST_DEEP, () -> apply(c, "Resolved", remarkField)));
            actions.getChildren().add(actionButton("Reject", DELAYED_RED, () -> apply(c, "Rejected", remarkField)));
        } else if ("In Progress".equalsIgnoreCase(c.getStatus())) {
            actions.getChildren().add(actionButton("Resolve", FOREST_DEEP, () -> apply(c, "Resolved", remarkField)));
            actions.getChildren().add(actionButton("Reject", DELAYED_RED, () -> apply(c, "Rejected", remarkField)));
        } else {
            actions.getChildren().add(actionButton("Reopen", SAFFRON_MAIN, () -> apply(c, "Pending", remarkField)));
        }

        card.getChildren().addAll(heading, remarkField, actions);
        return card;
    }

    private void apply(Complaint c, String newStatus, javafx.scene.control.TextField remarkField) {
        ComplaintStore.updateStatus(c.getId(), newStatus, remarkField.getText());
        SarpanchDashboard.myStage.setScene(getComplaintDetailScene(c, backToComplaintsAction, backToDashboardAction));
    }

    private javafx.scene.control.Button actionButton(String text, String color, Runnable onClick) {
        javafx.scene.control.Button button = new javafx.scene.control.Button(text);
        button.setPadding(new Insets(9, 16, 9, 16));
        button.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 9; -fx-text-fill: white;" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800; -fx-cursor: hand;");
        button.setOnAction(e -> onClick.run());
        return button;
    }

    /** Hero header: title, status, and at-a-glance who/where/when. */
    private VBox buildHeaderCard(Complaint c) {
        VBox card = card();
        card.setSpacing(18);

        HBox top = new HBox(16);
        top.setAlignment(Pos.CENTER_LEFT);

        StackPane iconCircle = new StackPane();
        iconCircle.setPrefSize(56, 56);
        iconCircle.setMinSize(56, 56);
        iconCircle.setStyle("-fx-background-color: linear-gradient(to bottom right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
            "-fx-background-radius: 999;");
        Label iconLabel = new Label(categoryIcon(c.getCategory()));
        iconLabel.setStyle("-fx-font-size: 22px;");
        iconCircle.getChildren().add(iconLabel);

        VBox titleBox = new VBox(4);
        Label title = new Label(c.getTitle());
        title.setWrapText(true);
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 26px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label idLine = new Label(c.getId() + "   \u00B7   " + c.getCategory());
        idLine.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 700; -fx-text-fill: rgba(11,61,46,0.55);");
        titleBox.getChildren().addAll(title, idLine);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);

        Label statusPill = new Label(c.getStatus());
        statusPill.setPadding(new Insets(8, 18, 8, 18));
        String statusColor = statusColor(c.getStatus());
        statusPill.setStyle("-fx-background-color: " + statusColor + "; -fx-background-radius: 999;" +
            "-fx-effect: dropshadow(gaussian, " + rgba(statusColor, 0.3) + ", 8, 0.2, 0, 3);" +
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800; -fx-text-fill: white;");

        top.getChildren().addAll(iconCircle, titleBox, grow, statusPill);

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: rgba(11,61,46,0.10);");

        HBox metaRow = new HBox(28);
        metaRow.setAlignment(Pos.CENTER_LEFT);
        metaRow.getChildren().addAll(
            metaBlock("\uD83D\uDC64", "Filed By", c.getVillagerName()),
            metaBlock("\uD83D\uDCCD", "Location", c.getVillage()),
            metaBlock("\uD83D\uDCC5", "Date Posted", c.getFormattedDate()),
            metaBlock("\uD83D\uDCDE", "Contact", c.getContactNumber() == null || c.getContactNumber().isBlank() ? "Not provided" : c.getContactNumber())
        );

        card.getChildren().addAll(top, divider, metaRow);
        return card;
    }

    private VBox metaBlock(String icon, String label, String value) {
        VBox block = new VBox(4);
        Label lbl = new Label(icon + "  " + label.toUpperCase());
        lbl.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 10.5px; -fx-font-weight: 800;" +
            "-fx-text-fill: rgba(11,61,46,0.50); -fx-letter-spacing: 0.06em;");
        Label val = new Label(value);
        val.setWrapText(true);
        val.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: " + FOREST_DEEP + ";");
        block.getChildren().addAll(lbl, val);
        return block;
    }

    /** Two-column layout: description on the left, photo evidence on the right. */
    private HBox buildDetailGrid(Complaint c) {
        HBox grid = new HBox(24);

        VBox descriptionCard = card();
        descriptionCard.setSpacing(14);
        Label descHeading = new Label("\uD83D\uDCDD  Description");
        descHeading.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 17px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        Label descBody = new Label(c.getDescription());
        descBody.setWrapText(true);
        descBody.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14.5px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.80); -fx-line-spacing: 4;");
        descriptionCard.getChildren().addAll(descHeading, descBody);

        if (c.getOfficerRemark() != null && !c.getOfficerRemark().isBlank()) {
            Region divider = new Region();
            divider.setPrefHeight(1);
            divider.setStyle("-fx-background-color: rgba(11,61,46,0.10);");
            Label remarkHeading = new Label("\uD83D\uDCC4  Officer Remark");
            remarkHeading.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");
            Label remarkBody = new Label(c.getOfficerRemark());
            remarkBody.setWrapText(true);
            remarkBody.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 500; -fx-text-fill: rgba(11,61,46,0.72);");
            descriptionCard.getChildren().addAll(divider, remarkHeading, remarkBody);
        }

        HBox.setHgrow(descriptionCard, Priority.ALWAYS);

        VBox photoCard = card();
        photoCard.setSpacing(14);
        photoCard.setPrefWidth(420);
        photoCard.setMinWidth(360);
        Label photoHeading = new Label("\uD83D\uDCF7  Photo Evidence");
        photoHeading.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 17px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");
        photoCard.getChildren().addAll(photoHeading, buildPhotoPane(c));

        grid.getChildren().addAll(descriptionCard, photoCard);
        return grid;
    }

    /**
     * Renders the attached photo when {@link Complaint#getPhotoPath()} points at a
     * real, readable file, otherwise shows an attractive placeholder. No photo
     * upload/storage is wired up yet on the Sarpanch side; this simply makes the
     * page ready to display one the moment a real path is set.
     */
    private StackPane buildPhotoPane(Complaint c) {
        StackPane pane = new StackPane();
        pane.setPrefHeight(320);
        pane.setMinHeight(320);

        String path = c.getPhotoPath();
        boolean hasPhoto = path != null && !path.isBlank() && new File(path).isFile();

        if (hasPhoto) {
            Image photo = new Image("file:" + path, 420, 320, true, true, true);
            ImageView view = new ImageView(photo);
            view.setFitWidth(420);
            view.setFitHeight(320);
            view.setPreserveRatio(false);
            view.setSmooth(true);
            view.setStyle("-fx-background-radius: 16;");
            StackPane clip = new StackPane(view);
            clip.setStyle("-fx-background-radius: 16; -fx-border-radius: 16;");
            pane.getChildren().add(clip);
        } else {
            pane.setStyle(
                "-fx-background-color: rgba(11,61,46,0.05);" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: rgba(11,61,46,0.18);" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: 1.5;" +
                "-fx-border-style: segments(8,6);"
            );
            VBox empty = new VBox(10);
            empty.setAlignment(Pos.CENTER);
            Label camera = new Label("\uD83D\uDCF7");
            camera.setStyle("-fx-font-size: 32px; -fx-opacity: 0.55;");
            Label emptyTitle = new Label("No Photo Attached");
            emptyTitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13.5px; -fx-font-weight: 800; -fx-text-fill: rgba(11,61,46,0.55);");
            Label emptySub = new Label("Photo evidence will appear here once submitted.");
            emptySub.setWrapText(true);
            emptySub.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.40);");
            emptySub.setMaxWidth(240);
            emptySub.setAlignment(Pos.CENTER);
            empty.getChildren().addAll(camera, emptyTitle, emptySub);
            pane.getChildren().add(empty);
        }

        return pane;
    }

    private String categoryIcon(String category) {
        if (category == null) return "\uD83D\uDCC1";
        return switch (category) {
            case "Water Supply" -> "\uD83D\uDCA7";
            case "Roads" -> "\uD83D\uDEE3";
            case "Electricity" -> "\uD83D\uDCA1";
            case "Sanitation" -> "\uD83E\uDDF9";
            case "Education" -> "\uD83C\uDF93";
            default -> "\uD83D\uDCC1";
        };
    }

    private String statusColor(String status) {
        if (status == null) return CONTEXT_TEAL;
        return switch (status) {
            case "Pending" -> SAFFRON_MAIN;
            case "In Progress" -> CONTEXT_TEAL;
            case "Resolved" -> FOREST_DEEP;
            case "Rejected" -> DELAYED_RED;
            default -> CONTEXT_TEAL;
        };
    }

    /* ============================================================
     *  HELPERS (kept identical to SarpanchComplaintsPage)
     * ============================================================ */
    private VBox card() {
        VBox card = new VBox(14);
        card.setPadding(new Insets(24));
        card.setStyle(cardStyle(20));
        return card;
    }

    private String cardStyle(int radius) {
        return "-fx-background-color: rgba(255,255,255,0.90);" +
               "-fx-background-radius: " + radius + ";" +
               "-fx-border-color: rgba(255,255,255,0.5);" +
               "-fx-border-radius: " + radius + ";" +
               "-fx-border-width: 1;" +
               "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 18, 0.1, 0, 5);";
    }

    private String rgba(String hex, double alpha) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
    }
}