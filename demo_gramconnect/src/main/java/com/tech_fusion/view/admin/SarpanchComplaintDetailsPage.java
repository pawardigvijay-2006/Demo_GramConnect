package com.tech_fusion.view.admin;

import com.tech_fusion.model.admin.OfficialComplaint;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
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
import javafx.stage.Stage;

import java.io.File;

/**
 * GramConnect - Official Complaint Review (Admin)
 * ------------------------------------------------------------------
 * Opened from ComplaintManagement's "Complaints Against Sarpanch -
 * Selected Village" panel via each row's "View Complaint" action.
 *
 * Shows the complaint's Title, Location, Official's Name,
 * Designation, and Description, plus "Mark Resolved" / "Mark
 * Rejected" actions that update the underlying {@link OfficialComplaint}
 * in place (see OfficialComplaintStore) and return to a freshly
 * refreshed Complaint Management screen.
 *
 * Same navigation pattern as every other admin page here: builds and
 * returns a Scene, swapped onto the shared {@code Dashboard.myStage}.
 */
public class SarpanchComplaintDetailsPage {

    private static final String FOREST_DEEP  = "#0B3D2E";
    private static final String FOREST_LIGHT = "#0F4736";
    private static final String SAFFRON_MAIN = "#E07A1F";
    private static final String CONTEXT_TEAL = "#0E8C8C";
    private static final String DELAYED_RED  = "#D94C38";
    private static final String NEUTRAL_GREY = "#6B7B74";
    private static final String FONT_FAMILY  = "'Inter', 'Segoe UI', 'Arial', sans-serif";

    private static final String BACKGROUND_IMAGE_PATH ="demo_gramconnect\\src\\main\\resources\\assets\\images\\WhatsApp Image 2026-08-10 at 11.55.38 PM.jpeg";

    private Label statusPill;
    private Button resolveBtn;
    private Button rejectBtn;
    private Label confirmationLabel;

    /**
     * @param complaint       the Official Complaint to review.
     * @param backToManagement returns to ComplaintManagement, rebuilt fresh
     *                         so the panel reflects any status change made here.
     */
    public Scene getComplaintDetailsScene(OfficialComplaint complaint, Runnable backToManagement) {
        BorderPane root = new BorderPane();
        root.setBackground(buildBackground());

        root.setTop(buildTopBar(backToManagement));

        ScrollPane scroller = new ScrollPane(buildCenterContent(complaint, backToManagement));
        scroller.setFitToWidth(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        root.setCenter(scroller);

        return new Scene(root, 1500, 820);
    }

    private Background buildBackground() {
        try {
            Image backgroundImage = new Image(new File(BACKGROUND_IMAGE_PATH).toURI().toString());
            return new Background(new BackgroundImage(backgroundImage,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true)));
        } catch (Exception ex) {
            return null;
        }
    }

    /* ============================================================
     *  TOP BAR — brand + breadcrumb + back action
     * ============================================================ */
    private HBox buildTopBar(Runnable backToManagement) {
        HBox topBar = new HBox(18);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPrefHeight(72);
        topBar.setPadding(new Insets(0, 32, 0, 32));
        topBar.setStyle(
                "-fx-background-color: rgba(255,255,255,0.92);" +
                "-fx-border-color: transparent transparent rgba(255,255,255,0.6) transparent;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 8, 0.1, 0, 2);"
        );

        Label brand = new Label("GramConnect");
        brand.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        Label crumbSep = new Label("/");
        crumbSep.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.35);");

        Label crumb = new Label("Complaint Management / Official Complaint");
        crumb.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: rgba(11,61,46,0.55);");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button back = new Button("\u2190  Back to Complaint Management");
        back.setStyle(
                "-fx-background-color: rgba(11,61,46,0.06); -fx-background-radius: 10;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + FOREST_DEEP + "; -fx-padding: 10 16 10 16; -fx-cursor: hand;"
        );
        back.setOnMouseEntered(e -> back.setStyle(
                "-fx-background-color: rgba(11,61,46,0.12); -fx-background-radius: 10;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + FOREST_DEEP + "; -fx-padding: 10 16 10 16; -fx-cursor: hand;"));
        back.setOnMouseExited(e -> back.setStyle(
                "-fx-background-color: rgba(11,61,46,0.06); -fx-background-radius: 10;" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + FOREST_DEEP + "; -fx-padding: 10 16 10 16; -fx-cursor: hand;"));
        back.setOnAction(e -> backToManagement.run());

        topBar.getChildren().addAll(brand, crumbSep, crumb, spacer, back);
        return topBar;
    }

    /* ============================================================
     *  CENTER CONTENT — centered review card
     * ============================================================ */
    private VBox buildCenterContent(OfficialComplaint complaint, Runnable backToManagement) {
        VBox outer = new VBox();
        outer.setAlignment(Pos.TOP_CENTER);
        outer.setPadding(new Insets(40, 32, 56, 32));

        VBox card = new VBox(0);
        card.setMaxWidth(760);
        card.setPrefWidth(760);
        card.setStyle(
                "-fx-background-color: rgba(255,255,255,0.94);" +
                "-fx-background-radius: 24;" +
                "-fx-border-color: rgba(255,255,255,0.6);" +
                "-fx-border-radius: 24;" +
                "-fx-border-width: 1;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.16), 32, 0.15, 0, 12);"
        );

        Region accentStrip = new Region();
        accentStrip.setPrefHeight(8);
        accentStrip.setStyle(
                "-fx-background-color: linear-gradient(to right, " + SAFFRON_MAIN + ", " + CONTEXT_TEAL + ");" +
                "-fx-background-radius: 24 24 0 0;"
        );

        VBox body = new VBox(26);
        body.setPadding(new Insets(30, 34, 34, 34));

        body.getChildren().addAll(
                buildHeaderBlock(complaint),
                buildInfoGrid(complaint),
                buildDescriptionBlock(complaint),
                buildActionsBlock(complaint, backToManagement)
        );

        card.getChildren().addAll(accentStrip, body);
        outer.getChildren().add(card);
        return outer;
    }

    private VBox buildHeaderBlock(OfficialComplaint complaint) {
        HBox idRow = new HBox(10);
        idRow.setAlignment(Pos.CENTER_LEFT);

        Label idLabel = new Label(complaint.getId());
        idLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12px; -fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.45); -fx-letter-spacing: 0.06em;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        statusPill = new Label();
        statusPill.setPadding(new Insets(5, 14, 5, 14));
        statusPill.setMaxWidth(Region.USE_PREF_SIZE);
        applyStatusStyle(complaint.getStatus());

        idRow.getChildren().addAll(idLabel, spacer, statusPill);

        Label titleLabel = new Label(complaint.getTitle());
        titleLabel.setWrapText(true);
        titleLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 26px; -fx-font-weight: 900; -fx-text-fill: " + FOREST_DEEP + ";");

        Label subtitle = new Label("Official Complaint against the Sarpanch");
        subtitle.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: rgba(11,61,46,0.55);");

        return new VBox(8, idRow, titleLabel, subtitle);
    }

    private HBox buildInfoGrid(OfficialComplaint complaint) {
        HBox row = new HBox(16);
        VBox locationCard = infoTile("\uD83D\uDCCD", CONTEXT_TEAL, "LOCATION", complaint.getLocation());
        String officialCombined = complaint.getOfficialName()
                + ((complaint.getDesignation() == null || complaint.getDesignation().trim().isEmpty())
                        ? "" : " \u2014 " + complaint.getDesignation());
        VBox officialCard = infoTile("\uD83E\uDDD1\u200D\uD83D\uDCBC", SAFFRON_MAIN, "OFFICIAL", officialCombined);

        HBox.setHgrow(locationCard, Priority.ALWAYS);
        HBox.setHgrow(officialCard, Priority.ALWAYS);

        row.getChildren().addAll(locationCard, officialCard);
        return row;
    }

    private VBox infoTile(String icon, String accent, String label, String value) {
        VBox tile = new VBox(10);
        tile.setPadding(new Insets(16));
        tile.setStyle(
                "-fx-background-color: " + rgba(accent, 0.06) + ";" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: " + rgba(accent, 0.16) + ";" +
                "-fx-border-radius: 14;" +
                "-fx-border-width: 1;"
        );

        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(34, 34);
        iconChip.setMinSize(34, 34);
        iconChip.setStyle("-fx-background-color: " + rgba(accent, 0.16) + "; -fx-background-radius: 10;");
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 14px; -fx-text-fill: " + accent + ";");
        iconChip.getChildren().add(ic);

        Label labelText = new Label(label);
        labelText.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 10.5px; -fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.55); -fx-letter-spacing: 0.07em;");

        Label valueText = new Label((value == null || value.trim().isEmpty()) ? "\u2014" : value);
        valueText.setWrapText(true);
        valueText.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14.5px; -fx-font-weight: 800; -fx-text-fill: " + FOREST_DEEP + ";");

        tile.getChildren().addAll(iconChip, labelText, valueText);
        return tile;
    }

    private VBox buildDescriptionBlock(OfficialComplaint complaint) {
        Label label = new Label("DESCRIPTION");
        label.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 10.5px; -fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.55); -fx-letter-spacing: 0.07em;");

        Label text = new Label(complaint.getDescription());
        text.setWrapText(true);
        text.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: rgba(11,61,46,0.82); -fx-line-spacing: 3;");

        VBox box = new VBox(10, label, text);
        box.setPadding(new Insets(16));
        box.setStyle(
                "-fx-background-color: rgba(11,61,46,0.03);" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: rgba(11,61,46,0.08);" +
                "-fx-border-radius: 14;" +
                "-fx-border-width: 1;"
        );
        return box;
    }

    private VBox buildActionsBlock(OfficialComplaint complaint, Runnable backToManagement) {
        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: rgba(11,61,46,0.10);");

        resolveBtn = new Button("\u2705  Mark Resolved");
        resolveBtn.setStyle(actionButtonStyle(CONTEXT_TEAL, true));
        resolveBtn.setOnAction(e -> updateStatus(complaint, OfficialComplaint.Status.RESOLVED));
        addButtonHover(resolveBtn, CONTEXT_TEAL, true);

        rejectBtn = new Button("\u2716  Mark Rejected");
        rejectBtn.setStyle(actionButtonStyle(DELAYED_RED, true));
        rejectBtn.setOnAction(e -> updateStatus(complaint, OfficialComplaint.Status.REJECTED));
        addButtonHover(rejectBtn, DELAYED_RED, true);

        Button backBtn = new Button("Back to Complaint Management");
        backBtn.setStyle(actionButtonStyle(FOREST_DEEP, false));
        backBtn.setOnAction(e -> backToManagement.run());
        addButtonHover(backBtn, FOREST_DEEP, false);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox actionsRow = new HBox(12, resolveBtn, rejectBtn, spacer, backBtn);
        actionsRow.setAlignment(Pos.CENTER_LEFT);

        confirmationLabel = new Label();
        confirmationLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 700; -fx-text-fill: " + CONTEXT_TEAL + ";");
        confirmationLabel.setManaged(false);
        confirmationLabel.setVisible(false);

        VBox block = new VBox(16, divider, actionsRow, confirmationLabel);
        return block;
    }

    private void updateStatus(OfficialComplaint complaint, OfficialComplaint.Status newStatus) {
        complaint.setStatus(newStatus);
        applyStatusStyle(newStatus);

        confirmationLabel.setManaged(true);
        confirmationLabel.setVisible(true);
        confirmationLabel.setText(newStatus == OfficialComplaint.Status.RESOLVED
                ? "\u2713 Marked as Resolved. This will update on the Complaint Management panel."
                : "\u2713 Marked as Rejected. This will update on the Complaint Management panel.");
        confirmationLabel.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 700;" +
                "-fx-text-fill: " + (newStatus == OfficialComplaint.Status.RESOLVED ? CONTEXT_TEAL : DELAYED_RED) + ";");
    }

    private void applyStatusStyle(OfficialComplaint.Status status) {
        String color;
        String text;
        switch (status) {
            case RESOLVED:
                color = CONTEXT_TEAL;
                text = "Resolved";
                break;
            case REJECTED:
                color = NEUTRAL_GREY;
                text = "Rejected";
                break;
            default:
                color = SAFFRON_MAIN;
                text = "Pending";
                break;
        }
        statusPill.setText(text);
        statusPill.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11.5px; -fx-font-weight: 800;" +
                "-fx-text-fill: " + color + "; -fx-background-color: " + rgba(color, 0.14) + "; -fx-background-radius: 999;");
    }

    private String actionButtonStyle(String accent, boolean filled) {
        if (filled) {
            return "-fx-background-color: " + accent + "; -fx-text-fill: white;" +
                    "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800;" +
                    "-fx-background-radius: 10; -fx-padding: 12 20 12 20; -fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, " + rgba(accent, 0.35) + ", 10, 0.2, 0, 3);";
        }
        return "-fx-background-color: transparent; -fx-text-fill: " + accent + ";" +
                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800;" +
                "-fx-border-color: " + rgba(accent, 0.30) + "; -fx-border-width: 1.5; -fx-border-radius: 10;" +
                "-fx-background-radius: 10; -fx-padding: 11 18 11 18; -fx-cursor: hand;";
    }

    private void addButtonHover(Button btn, String accent, boolean filled) {
        String base = actionButtonStyle(accent, filled);
        String hover = filled
                ? "-fx-background-color: derive(" + accent + ", -10%); -fx-text-fill: white;" +
                        "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 13px; -fx-font-weight: 800;" +
                        "-fx-background-radius: 10; -fx-padding: 12 20 12 20; -fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, " + rgba(accent, 0.45) + ", 14, 0.25, 0, 4);"
                : "-fx-background-color: " + rgba(accent, 0.06) + "; -fx-text-fill: " + accent + ";" +
                        "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 12.5px; -fx-font-weight: 800;" +
                        "-fx-border-color: " + rgba(accent, 0.45) + "; -fx-border-width: 1.5; -fx-border-radius: 10;" +
                        "-fx-background-radius: 10; -fx-padding: 11 18 11 18; -fx-cursor: hand;";
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(base));
    }

    private String rgba(String hex, double alpha) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
    }
}