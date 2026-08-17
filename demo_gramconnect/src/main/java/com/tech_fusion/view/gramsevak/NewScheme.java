
package com.tech_fusion.view.gramsevak;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * GramConnect - Add New Government Scheme page.
 *
 * Opened when the Gram Sevak clicks "Generate/Add New Scheme" on the
 * Government Schemes page. Styled to match the rest of GramConnect
 * (same colors, card style, fonts, spacing as SarpanchDashboard /
 * GovSchemes / DocumentsManage).
 *
 * This class only returns page content - it does NOT create its own
 * Stage, Scene, or Application. The caller wraps getNewSchemeContent()
 * in a ScrollPane the same way it already does for GovSchemes, e.g.:
 *
 *     contentArea.setCenter(
 *         new ScrollPane(NewScheme.getNewSchemeContent())
 *     );
 */
public class NewScheme {

    // ============================================================
    // GramConnect color palette (same as the rest of the app)
    // ============================================================
    private static final String FOREST_DEEP  = "#0B3D2E";
    private static final String FOREST_LIGHT = "#0F4736";
    private static final String SAFFRON_MAIN = "#E07A1F";
    private static final String CONTEXT_TEAL = "#0E8C8C";
    private static final String DELAYED_RED  = "#D94C38";

    private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

    private GovScheme govScheme = new GovScheme();
    Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

    // ============================================================
    // MAIN ENTRY POINT
    // ============================================================
public Scene getNewSchemeScene(Runnable backAction) {

    BorderPane root = new BorderPane();

    root.setStyle(
            "-fx-background-color: #F4F8FB;"
    );

    // LEFT SIDEBAR
    root.setLeft(
            govScheme.buildSidebar(backAction)
    );

    // MAIN AREA
    BorderPane mainArea = new BorderPane();

    // TOP BAR
    mainArea.setTop(
            govScheme.buildHeader()
    );

    // CONTENT
    VBox mainContent = new VBox(24);

    mainContent.setPadding(
            new Insets(32, 40, 48, 40)
    );

    mainContent.getChildren().addAll(
            buildTopBar(backAction),
            buildSchemeInformationCard(),
            buildEligibilityDetailsCard(),
            buildRequiredDocumentsCard(),
            buildApplicationDetailsCard(),
            buildSchemeStatusCard(),
            buildActionButtons(backAction)
    );

    ScrollPane scrollPane = new ScrollPane(mainContent);

    scrollPane.setFitToWidth(true);

    scrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background: transparent;"
    );

    mainArea.setCenter(scrollPane);

    root.setCenter(mainArea);

    return new Scene(root,  screenSize.getWidth(), screenSize.getHeight());
}
    // ============================================================
    // TOP BAR — Back button + Title + Subtitle
    // ============================================================
    private static VBox buildTopBar(Runnable backAction) {

        VBox topBar = new VBox(14);

        // ----- Back button -----
        Button backBtn = new Button("\u2190  Back to Government Schemes");
        String backBase =
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + FOREST_DEEP + ";" +
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 700;" +
                "-fx-padding: 4 0 4 0;" +
                "-fx-cursor: hand;";
        backBtn.setStyle(backBase);
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(backBase + "-fx-underline: true;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(backBase));

        // Later: replace this with code that switches contentArea back
        // to GovSchemes.getSchemeContent().
        backBtn.setOnAction(e -> {
                System.out.println("Back to Government Schemes clicked");
                backAction.run();

    });

        // ----- Title + subtitle -----
        Label title = new Label("Add New Government Scheme");
        title.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 28px;" +
                "-fx-font-weight: 900;" +
                "-fx-text-fill: " + FOREST_DEEP + ";");

        Label subtitle = new Label("Create and publish a new scheme for citizens");
        subtitle.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 500;" +
                "-fx-text-fill: rgba(11,61,46,0.70);");

        topBar.getChildren().addAll(backBtn, title, subtitle);
        return topBar;
    }

    // ============================================================
    // SECTION 1 — SCHEME INFORMATION
    // ============================================================
    private static VBox buildSchemeInformationCard() {

        VBox card = createSectionCard("\uD83D\uDCDD", "Scheme Information");

        // Scheme Name
        TextField schemeNameField = new TextField();
        schemeNameField.setPromptText("e.g., PM-Kisan Samman Nidhi");
        styleTextField(schemeNameField);
        card.getChildren().add(fieldGroup("Scheme Name *", schemeNameField));

        // Department + Domain/Category (side by side)
        ComboBox<String> departmentBox = new ComboBox<>();
        departmentBox.getItems().addAll(
                "Ministry of Agriculture",
                "Ministry of Rural Development",
                "Ministry of Education",
                "Ministry of Health",
                "State Government",
                "Gram Panchayat");
        departmentBox.setPromptText("Select Department");
        styleComboBox(departmentBox);

        ComboBox<String> domainBox = new ComboBox<>();
        domainBox.getItems().addAll(
                "Agriculture",
                "Education",
                "Health",
                "Housing",
                "Employment",
                "Women & Child Development",
                "Social Welfare",
                "Rural Development",
                "Other");
        domainBox.setPromptText("Select Category");
        styleComboBox(domainBox);

        card.getChildren().add(
                fieldRow(
                        fieldGroup("Department *", departmentBox),
                        fieldGroup("Domain / Category *", domainBox)));

        // Scheme Type
        ComboBox<String> schemeTypeBox = new ComboBox<>();
        schemeTypeBox.getItems().addAll(
                "Central Government",
                "State Government",
                "Local Government");
        schemeTypeBox.setPromptText("Select Scheme Type");
        styleComboBox(schemeTypeBox);
        card.getChildren().add(fieldGroup("Scheme Type", schemeTypeBox));

        // Scheme Description
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Provide a detailed overview of the scheme...");
        styleTextArea(descriptionArea, 90);
        card.getChildren().add(fieldGroup("Scheme Description", descriptionArea));

        // Scheme Benefits
        TextArea benefitsArea = new TextArea();
        benefitsArea.setPromptText("List the key benefits for beneficiaries...");
        styleTextArea(benefitsArea, 90);
        card.getChildren().add(fieldGroup("Scheme Benefits", benefitsArea));

        return card;
    }

    // ============================================================
    // SECTION 2 — ELIGIBILITY DETAILS
    // ============================================================
    private static VBox buildEligibilityDetailsCard() {

        VBox card = createSectionCard("\u2714", "Eligibility Details");

        // Eligibility Criteria
        TextArea eligibilityArea = new TextArea();
        eligibilityArea.setPromptText("Who is eligible for this scheme?");
        styleTextArea(eligibilityArea, 80);
        card.getChildren().add(fieldGroup("Eligibility Criteria", eligibilityArea));

        // Minimum Age + Maximum Age
        TextField minAgeField = new TextField();
        minAgeField.setPromptText("e.g., 18");
        styleTextField(minAgeField);

        TextField maxAgeField = new TextField();
        maxAgeField.setPromptText("e.g., 60");
        styleTextField(maxAgeField);

        card.getChildren().add(
                fieldRow(
                        fieldGroup("Minimum Age", minAgeField),
                        fieldGroup("Maximum Age", maxAgeField)));

        // Income Limit + Target Beneficiaries
        TextField incomeLimitField = new TextField();
        incomeLimitField.setPromptText("e.g., Below \u20B92.5 Lakhs");
        styleTextField(incomeLimitField);

        ComboBox<String> targetBeneficiariesBox = new ComboBox<>();
        targetBeneficiariesBox.getItems().addAll(
                "Farmers",
                "Women",
                "Students",
                "Senior Citizens",
                "Low Income Families",
                "Persons with Disabilities",
                "Rural Households",
                "General",
                "Other");
        targetBeneficiariesBox.setPromptText("Select Target Beneficiaries");
        styleComboBox(targetBeneficiariesBox);

        card.getChildren().add(
                fieldRow(
                        fieldGroup("Income Limit", incomeLimitField),
                        fieldGroup("Target Beneficiaries", targetBeneficiariesBox)));

        // Additional Eligibility Criteria
        TextArea additionalEligibilityArea = new TextArea();
        additionalEligibilityArea.setPromptText("Any other eligibility conditions...");
        styleTextArea(additionalEligibilityArea, 80);
        card.getChildren().add(fieldGroup("Additional Eligibility Criteria", additionalEligibilityArea));

        return card;
    }

    // ============================================================
    // SECTION 3 — REQUIRED DOCUMENTS (no TableView)
    // ============================================================
    private static VBox buildRequiredDocumentsCard() {

        VBox card = createSectionCard("\uD83D\uDCC4", "Required Documents");

        // ----- List of document items -----
        VBox documentsList = new VBox(10);
        documentsList.getChildren().addAll(
                createDocumentItem("Aadhaar Card"),
                createDocumentItem("PAN Card"),
                createDocumentItem("Ration Card"),
                createDocumentItem("Income Certificate"));

        card.getChildren().add(documentsList);

        // ----- Row to add a new document -----
        TextField newDocumentField = new TextField();
        newDocumentField.setPromptText("Enter document name");
        styleTextField(newDocumentField);
        HBox.setHgrow(newDocumentField, Priority.ALWAYS);

        Button addDocumentBtn = new Button("+ Add Document");
        styleSmallButton(addDocumentBtn, "rgba(11,61,46,0.10)", FOREST_DEEP);

        // Simple beginner-friendly approach: read the text field, and if
        // it is not empty, add a new document item to the list above.
        addDocumentBtn.setOnAction(e -> {
            String documentName = newDocumentField.getText().trim();
            if (!documentName.isEmpty()) {
                documentsList.getChildren().add(createDocumentItem(documentName));
                newDocumentField.clear();
            }
        });

        HBox addDocumentRow = new HBox(10, newDocumentField, addDocumentBtn);
        addDocumentRow.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().add(addDocumentRow);
        return card;
    }

    /** Builds one required-document item: a CheckBox + Label inside a rounded row. */
    private static HBox createDocumentItem(String documentName) {

        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12, 16, 12, 16));
        item.setStyle(
                "-fx-background-color: rgba(255,255,255,0.6);" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(11,61,46,0.08);" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;");

        CheckBox checkBox = new CheckBox();

        Label nameLbl = new Label(documentName);
        nameLbl.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: " + FOREST_DEEP + ";");

        item.getChildren().addAll(checkBox, nameLbl);
        return item;
    }

    // ============================================================
    // SECTION 4 — APPLICATION DETAILS
    // ============================================================
    private static VBox buildApplicationDetailsCard() {

        VBox card = createSectionCard("\uD83D\uDCCB", "Application Details");

        // Application Process
        TextArea processArea = new TextArea();
        processArea.setPromptText("Provide step-by-step instructions for applicants...");
        styleTextArea(processArea, 90);
        card.getChildren().add(fieldGroup("Application Process", processArea));

        // Application Mode + Application Deadline
        ComboBox<String> applicationModeBox = new ComboBox<>();
        applicationModeBox.getItems().addAll("Online", "Offline", "Both");
        applicationModeBox.setPromptText("Select Application Mode");
        styleComboBox(applicationModeBox);

        DatePicker deadlinePicker = new DatePicker();
        styleDatePicker(deadlinePicker);

        card.getChildren().add(
                fieldRow(
                        fieldGroup("Application Mode", applicationModeBox),
                        fieldGroup("Application Deadline", deadlinePicker)));

        // Official Application Link
        TextField linkField = new TextField();
        linkField.setPromptText("https://...");
        styleTextField(linkField);
        card.getChildren().add(fieldGroup("Official Application Link", linkField));

        // Helpline / Contact Information
        TextField helplineField = new TextField();
        helplineField.setPromptText("Phone number or email address");
        styleTextField(helplineField);
        card.getChildren().add(fieldGroup("Helpline / Contact Information", helplineField));

        return card;
    }

    // ============================================================
    // SECTION 5 — SCHEME STATUS
    // ============================================================
    private static VBox buildSchemeStatusCard() {

        VBox card = createSectionCard("\u2699", "Scheme Status");

        // Scheme Status
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Draft", "Active", "Closed");
        statusBox.setValue("Draft");
        styleComboBox(statusBox);
        card.getChildren().add(fieldGroup("Scheme Status", statusBox));

        // Publish to Citizens
        VBox publishGroup = new VBox(4);

        CheckBox publishCheckBox = new CheckBox("Publish to Citizens");
        publishCheckBox.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: " + FOREST_DEEP + ";");

        Label publishHint = new Label("Make this scheme visible on the citizen portal.");
        publishHint.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 500;" +
                "-fx-text-fill: rgba(11,61,46,0.60);");

        publishGroup.getChildren().addAll(publishCheckBox, publishHint);
        card.getChildren().add(publishGroup);

        return card;
    }

    // ============================================================
    // BOTTOM ACTION BUTTONS
    // ============================================================
   private static HBox buildActionButtons(Runnable backAction) {

        HBox actionsRow = new HBox(14);
        actionsRow.setAlignment(Pos.CENTER_RIGHT);

        // ----- Cancel: white background, green border, forest text -----
        Button cancelBtn = new Button("Cancel");
        String cancelBase =
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " + FOREST_DEEP + ";" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1.5;" +
                "-fx-text-fill: " + FOREST_DEEP + ";" +
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 700;" +
                "-fx-padding: 12 26 12 26;" +
                "-fx-cursor: hand;";
        cancelBtn.setStyle(cancelBase);
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle(cancelBase + "-fx-background-color: rgba(11,61,46,0.06);"));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle(cancelBase));
        cancelBtn.setOnAction(e -> backAction.run());

        // ----- Save as Draft: saffron accent -----
        Button saveDraftBtn = new Button("Save as Draft");
        String saveDraftBase =
                "-fx-background-color: " + SAFFRON_MAIN + ";" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 700;" +
                "-fx-padding: 12 26 12 26;" +
                "-fx-effect: dropshadow(gaussian, rgba(224,122,31,0.35), 8, 0.1, 0, 3);" +
                "-fx-cursor: hand;";
        saveDraftBtn.setStyle(saveDraftBase);
        saveDraftBtn.setOnMouseEntered(e -> saveDraftBtn.setStyle(saveDraftBase + "-fx-opacity: 0.9;"));
        saveDraftBtn.setOnMouseExited(e -> saveDraftBtn.setStyle(saveDraftBase));
        saveDraftBtn.setOnAction(e -> System.out.println("Save as Draft clicked"));

        // ----- Publish Scheme: forest/deep green accent -----
        Button publishBtn = new Button("Publish Scheme");
        String publishBase =
                "-fx-background-color: linear-gradient(to right, " + FOREST_LIGHT + ", " + FOREST_DEEP + ");" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 700;" +
                "-fx-padding: 12 26 12 26;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.35), 10, 0.1, 0, 4);" +
                "-fx-cursor: hand;";
        publishBtn.setStyle(publishBase);
        publishBtn.setOnMouseEntered(e -> publishBtn.setStyle(publishBase +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.55), 15, 0.2, 0, 5); -fx-translate-y: -1;"));
        publishBtn.setOnMouseExited(e -> publishBtn.setStyle(publishBase));
        publishBtn.setOnAction(e -> System.out.println("Publish Scheme clicked"));

        actionsRow.getChildren().addAll(cancelBtn, saveDraftBtn, publishBtn);
        return actionsRow;
    }

    // ============================================================
    // Layout helpers
    // ============================================================

    /** Creates a white rounded section card with an icon + title header already added. */
    private static VBox createSectionCard(String icon, String titleText) {

        VBox card = new VBox(18);
        card.setPadding(new Insets(28));
        card.setStyle(cardStyle(20));

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(38, 38);
        iconChip.setMinSize(38, 38);
        iconChip.setStyle("-fx-background-color: rgba(11,61,46,0.10); -fx-background-radius: 999;");
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 15px; -fx-text-fill: " + FOREST_DEEP + ";");
        iconChip.getChildren().add(ic);

        Label title = new Label(titleText);
        title.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: 900;" +
                "-fx-text-fill: " + FOREST_DEEP + ";");

        header.getChildren().addAll(iconChip, title);
        card.getChildren().add(header);
        return card;
    }

    /** Wraps a label + input control into one labeled field group. */
    private static VBox fieldGroup(String labelText, Region control) {
        VBox group = new VBox(6);
        control.setMaxWidth(Double.MAX_VALUE);
        group.getChildren().addAll(createFieldLabel(labelText), control);
        return group;
    }

    /** Places two field groups side by side, sharing the row width equally. */
    private static HBox fieldRow(VBox left, VBox right) {
        HBox row = new HBox(20, left, right);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        return row;
    }

    private static Label createFieldLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle(
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: rgba(11,61,46,0.85);");
        return lbl;
    }

    // ============================================================
    // Control styling helpers
    // ============================================================
    private static void styleTextField(TextField textField) {
        textField.setStyle(
                "-fx-background-color: rgba(255,255,255,0.7);" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(11,61,46,0.10);" +
                "-fx-border-radius: 10;" +
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + FOREST_DEEP + ";" +
                "-fx-padding: 10 12 10 12;");
    }

    private static void styleTextArea(TextArea textArea, double prefHeight) {
        textArea.setPrefHeight(prefHeight);
        textArea.setWrapText(true);
        textArea.setStyle(
                "-fx-background-color: rgba(255,255,255,0.7);" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(11,61,46,0.10);" +
                "-fx-border-radius: 10;" +
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + FOREST_DEEP + ";");
    }

    private static void styleComboBox(ComboBox<String> comboBox) {
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setStyle(
                "-fx-background-color: rgba(255,255,255,0.7);" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(11,61,46,0.10);" +
                "-fx-border-radius: 10;" +
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;");
    }

    private static void styleDatePicker(DatePicker datePicker) {
        datePicker.setMaxWidth(Double.MAX_VALUE);
        datePicker.setStyle(
                "-fx-background-color: rgba(255,255,255,0.7);" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(11,61,46,0.10);" +
                "-fx-border-radius: 10;" +
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 13px;");
    }

    private static void styleSmallButton(Button button, String bgColor, String textColor) {
        String base =
                "-fx-background-color: " + bgColor + ";" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: " + textColor + ";" +
                "-fx-font-family: " + FONT_FAMILY + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 700;" +
                "-fx-padding: 9 16 9 16;" +
                "-fx-cursor: hand;";
        button.setStyle(base);
        button.setOnMouseEntered(e -> button.setStyle(base + "-fx-opacity: 0.85;"));
        button.setOnMouseExited(e -> button.setStyle(base));
    }

    /** Same glass-panel card style used across GramConnect. */
    private static String cardStyle(int radius) {
        return "-fx-background-color: rgba(255,255,255,0.88);" +
               "-fx-background-radius: " + radius + ";" +
               "-fx-border-color: rgba(255,255,255,0.5);" +
               "-fx-border-radius: " + radius + ";" +
               "-fx-border-width: 1;" +
               "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.06), 16, 0.1, 0, 4);";
    }
}
