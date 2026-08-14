package com.tech_fusion.view.gramsevak;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class DocumentsManage {

    private static final String FOREST_DEEP  = "#0B3D2E";
    private static final String SAFFRON_MAIN = "#E07A1F";
    private static final String CONTEXT_TEAL = "#0E8C8C";
    private static final String DELAYED_RED  = "#D94C38";

    private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

    // ============================================================
    // MAIN ENTRY POINT
    // ============================================================
    public static VBox getDocumentManageContent() {

        VBox content = new VBox(24);
        content.setPadding(new Insets(32, 40, 48, 40));

        // =========================
        // TITLE
        // =========================
        VBox titleBox = new VBox(6);

        Label title = new Label("Document Management");
        title.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 28px;" +
                "-fx-font-weight: 900;" +
                "-fx-text-fill: " + FOREST_DEEP + ";");

        Label subtitle = new Label("Verify and process citizen document requests.");
        subtitle.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 500;" +
                "-fx-text-fill: rgba(11,61,46,0.70);");

        titleBox.getChildren().addAll(title, subtitle);

        // =========================
        // SUMMARY CARDS
        // =========================
        HBox cards = new HBox(24);

        VBox pendingCard = createSummaryCard(FOREST_DEEP, "\uD83D\uDCC4", "PENDING VERIFICATION", "42");
        VBox approvedCard = createSummaryCard(CONTEXT_TEAL, "\u2714", "APPROVED DOCUMENTS", "128");
        VBox rejectedCard = createSummaryCard(DELAYED_RED, "\u2716", "REJECTED REQUESTS", "7");

        HBox.setHgrow(pendingCard, Priority.ALWAYS);
        HBox.setHgrow(approvedCard, Priority.ALWAYS);
        HBox.setHgrow(rejectedCard, Priority.ALWAYS);

        cards.getChildren().addAll(pendingCard, approvedCard, rejectedCard);

        // =========================
        // VERIFICATION QUEUE
        // =========================
        VBox queueBox = new VBox(20);
        queueBox.setPadding(new Insets(32));
        queueBox.setStyle(cardStyle(24));

        // ----- Queue header: icon + title + filters -----
        HBox queueHeading = new HBox(12);
        queueHeading.setAlignment(Pos.CENTER_LEFT);

        StackPane headIconChip = new StackPane();
        headIconChip.setPrefSize(40, 40);
        headIconChip.setMinSize(40, 40);
        headIconChip.setStyle("-fx-background-color: rgba(11,61,46,0.10); -fx-background-radius: 999;");
        Label headIcon = new Label("\uD83D\uDCC4");
        headIcon.setStyle("-fx-font-size: 16px; -fx-text-fill: " + FOREST_DEEP + ";");
        headIconChip.getChildren().add(headIcon);

        Label queueTitle = new Label("Verification Queue");
        queueTitle.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: 900;" +
                "-fx-text-fill: " + FOREST_DEEP + ";");

        Region headSpacer = new Region();
        HBox.setHgrow(headSpacer, Priority.ALWAYS);

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(
                "All Types",
                "Income Certificate",
                "Birth Certificate",
                "Caste Certificate");
        typeComboBox.setValue("All Types");
        styleComboBox(typeComboBox);

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(
                "Status: Pending",
                "Approved",
                "Rejected");
        statusComboBox.setValue("Status: Pending");
        styleComboBox(statusComboBox);

        HBox filters = new HBox(10, typeComboBox, statusComboBox);
        filters.setAlignment(Pos.CENTER_LEFT);

        queueHeading.getChildren().addAll(headIconChip, queueTitle, headSpacer, filters);

        // ----- Document request list (replaces the old TableView) -----
        VBox requestList = new VBox(14);

        /*
         * ============================================================
         * DATABASE / API CONNECTION POINT
         * ============================================================
         * The 3 sample cards below are placeholders only, so the page
         * has something to show right now.
         *
         * When the backend is ready:
         *   1. Fetch the real requests, e.g.:
         *        List<DocumentInfo> documentList = DocumentService.getPendingRequests();
         *   2. Clear the placeholder cards:
         *        requestList.getChildren().clear();
         *   3. Loop through documentList and build a card for each one:
         *        for (DocumentInfo doc : documentList) {
         *            requestList.getChildren().add(
         *                createDocumentRequest(
         *                    doc.getApplicantName(),
         *                    doc.getDocumentType(),
         *                    doc.getDateSubmitted(),
         *                    doc.getStatus()
         *                )
         *            );
         *        }
         *
         * The View / Approve / Reject buttons inside createDocumentRequest(...)
         * are also where you will later attach setOnAction(...) handlers that
         * call your API (e.g. approve a specific request by its ID).
         * ============================================================
         */
        requestList.getChildren().addAll(
                createDocumentRequest("Ramesh Kadam", "Income Certificate", "12 Aug 2026", "Pending"),
                createDocumentRequest("Sunita Pawar", "Birth Certificate", "10 Aug 2026", "Pending"),
                createDocumentRequest("Anil Deshmukh", "Caste Certificate", "08 Aug 2026", "Approved"));

        ScrollPane scrollPane = new ScrollPane(requestList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(360);
        scrollPane.setStyle(
                "-fx-background: transparent;" +
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;");

        queueBox.getChildren().addAll(queueHeading, scrollPane);

        content.getChildren().addAll(titleBox, cards, queueBox);

        return content;
    }

    // ============================================================
    // Summary card — same look as the Sarpanch Dashboard KPI cards
    // (colored top strip, icon chip, label, big number)
    // ============================================================
    private static VBox createSummaryCard(String accent, String icon, String labelText, String statText) {

        VBox card = new VBox();
        card.setMinHeight(150);
        card.setStyle(cardStyle(16));

        Region strip = new Region();
        strip.setPrefHeight(6);
        strip.setStyle("-fx-background-color: " + accent + "; -fx-background-radius: 16 16 0 0;");

        VBox inner = new VBox(16);
        inner.setPadding(new Insets(20, 24, 24, 24));

        HBox head = new HBox(12);
        head.setAlignment(Pos.CENTER_LEFT);

        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(40, 40);
        iconChip.setMinSize(40, 40);
        iconChip.setStyle("-fx-background-color: " + rgba(accent, 0.12) + "; -fx-background-radius: 12;");
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 16px; -fx-text-fill: " + accent + ";");
        iconChip.getChildren().add(ic);

        Label lbl = new Label(labelText);
        lbl.setWrapText(true);
        lbl.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 800;" +
                "-fx-text-fill: rgba(11,61,46,0.80);" +
                "-fx-letter-spacing: 0.06em;");

        head.getChildren().addAll(iconChip, lbl);

        Label stat = new Label(statText);
        stat.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 34px;" +
                "-fx-font-weight: 900;" +
                "-fx-text-fill: " + FOREST_DEEP + ";");

        inner.getChildren().addAll(head, stat);
        card.getChildren().addAll(strip, inner);

        return card;
    }

    // ============================================================
    // Document request card — one horizontal card per request.
    // This replaces a single TableView row.
    // ============================================================
    private static HBox createDocumentRequest(String applicantName, String documentType,
                                                String dateSubmitted, String status) {

        HBox card = new HBox(20);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(18, 20, 18, 20));
        card.setStyle(
                "-fx-background-color: rgba(255,255,255,0.7);" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: rgba(11,61,46,0.08);" +
                "-fx-border-radius: 14;" +
                "-fx-border-width: 1;");

        // ----- Applicant name + document type -----
        VBox details = new VBox(4);
        details.setPrefWidth(260);

        Label nameLbl = new Label(applicantName);
        nameLbl.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: 800;" +
                "-fx-text-fill: " + FOREST_DEEP + ";");

        Label typeLbl = new Label(documentType);
        typeLbl.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 500;" +
                "-fx-text-fill: rgba(11,61,46,0.65);");

        details.getChildren().addAll(nameLbl, typeLbl);

        // ----- Date submitted -----
        Label dateLbl = new Label(dateSubmitted);
        dateLbl.setPrefWidth(130);
        dateLbl.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: rgba(11,61,46,0.70);");

        // ----- Status pill (same style as the dashboard's status pills) -----
        String statusColor = getStatusColor(status);

        HBox pill = new HBox(6);
        pill.setAlignment(Pos.CENTER);
        pill.setPadding(new Insets(6, 14, 6, 14));
        pill.setMaxWidth(Region.USE_PREF_SIZE);
        pill.setStyle("-fx-background-color: " + statusColor + "; -fx-background-radius: 999;");

        Circle pillDot = new Circle(3, Color.WHITE);
        Label pillLbl = new Label(status);
        pillLbl.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: white;");
        pill.getChildren().addAll(pillDot, pillLbl);

        HBox statusWrap = new HBox(pill);
        statusWrap.setPrefWidth(130);
        statusWrap.setAlignment(Pos.CENTER_LEFT);

        // ----- Spacer pushes the buttons to the right -----
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // ----- Action buttons -----
        Button viewBtn = new Button("View");
        styleActionButton(viewBtn, "rgba(11,61,46,0.10)", FOREST_DEEP);

        Button approveBtn = new Button("Approve");
        styleActionButton(approveBtn, CONTEXT_TEAL, "white");

        Button rejectBtn = new Button("Reject");
        styleActionButton(rejectBtn, DELAYED_RED, "white");

        // NOTE: attach your database/API calls here later, e.g.
        // approveBtn.setOnAction(e -> DocumentService.approve(requestId));
        HBox actions = new HBox(8, viewBtn, approveBtn, rejectBtn);
        actions.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(details, dateLbl, statusWrap, spacer, actions);

        return card;
    }

    // ============================================================
    // Small style helpers
    // ============================================================
    private static void styleActionButton(Button button, String bgColor, String textColor) {
        String base =
                "-fx-background-color: " + bgColor + ";" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: " + textColor + ";" +
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 700;" +
                "-fx-padding: 8 14 8 14;" +
                "-fx-cursor: hand;";
        button.setStyle(base);
        button.setOnMouseEntered(e -> button.setStyle(base + "-fx-opacity: 0.85;"));
        button.setOnMouseExited(e -> button.setStyle(base));
    }

    private static void styleComboBox(ComboBox<String> comboBox) {
        comboBox.setStyle(
                "-fx-background-color: rgba(255,255,255,0.7);" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(11,61,46,0.10);" +
                "-fx-border-radius: 10;" +
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 12px;");
    }

    private static String getStatusColor(String status) {
        switch (status) {
            case "Approved":
                return CONTEXT_TEAL;
            case "Rejected":
                return DELAYED_RED;
            default:
                return SAFFRON_MAIN; // Pending, or anything else
        }
    }

    /** Same glass-panel card style used across the Sarpanch Dashboard. */
    private static String cardStyle(int radius) {
        return "-fx-background-color: rgba(255,255,255,0.88);" +
               "-fx-background-radius: " + radius + ";" +
               "-fx-border-color: rgba(255,255,255,0.5);" +
               "-fx-border-radius: " + radius + ";" +
               "-fx-border-width: 1;" +
               "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.06), 16, 0.1, 0, 4);";
    }

    /** Convert #RRGGBB hex to an rgba(r,g,b,a) CSS string. */
    private static String rgba(String hex, double alpha) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
    }
}
