package com.tech_fusion.view.gramsevak;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class PropertyTaxBill {

    // ============================================================
    // COLOR PALETTE - SAME AS NEW DASHBOARD
    // ============================================================

    private static final String FOREST_DEEP = "#0B3D2E";
    private static final String FOREST_LIGHT = "#0F4736";
    private static final String SAFFRON_MAIN = "#E07A1F";
    private static final String CONTEXT_TEAL = "#0E8C8C";

    private static final String CARD_BG = "rgba(255,255,255,0.90)";
    private static final String PAGE_BG = "rgba(240,244,242,0.52)";
    private static final String BORDER = "#E5E7EB";
    private static final String FIELD_BG = "#F8FAF9";
    private static final String TEXT_DARK = "#285B5B";
    private static final String TEXT_LIGHT = "#7A8A87";

    // ============================================================
    // MAIN METHOD
    // ============================================================

    public static Node getPropertyTaxBillContent(Runnable backAction) {

        VBox main = new VBox(22);

        main.setPadding(new Insets(28, 35, 40, 35));

        main.setStyle(
                "-fx-background-color: " + PAGE_BG + ";"
        );

        // ========================================================
        // TOP TITLE SECTION
        // ========================================================

        VBox heading = new VBox(5);

        Label back = new Label("←  Back to Bill Management");

        back.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + CONTEXT_TEAL + ";" +
                "-fx-cursor: hand;"
        );

        // This can later be connected to BillManagement
        back.setOnMouseClicked(e -> {
            System.out.println("Back to Bill Management clicked");
            backAction.run();

        });

        Label title = new Label("Generate Property Tax Bill");

        title.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: 900;" +
                "-fx-text-fill: " + FOREST_DEEP + ";"
        );

        Label subtitle = new Label(
                "Create and manage property tax bills securely."
        );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 500;" +
                "-fx-text-fill: " + TEXT_LIGHT + ";"
        );

        heading.getChildren().addAll(
                back,
                title,
                subtitle
        );

        // ========================================================
        // LEFT SIDE
        // ========================================================

        VBox leftColumn = new VBox(18);

        VBox ownerInformation = buildOwnerInformation();
        VBox propertyDetails = buildPropertyDetails();
        VBox taxAssessment = buildTaxAssessment();

        leftColumn.getChildren().addAll(
                ownerInformation,
                propertyDetails,
                taxAssessment
        );

        // ========================================================
        // RIGHT SIDE
        // ========================================================

        VBox rightColumn = new VBox(18);

        VBox calculationSummary = buildCalculationSummary();
        VBox paymentStatus = buildPaymentStatus();

        rightColumn.getChildren().addAll(
                calculationSummary,
                paymentStatus
        );

        // ========================================================
        // TWO COLUMN LAYOUT
        // ========================================================

        HBox columns = new HBox(20);

        HBox.setHgrow(leftColumn, Priority.ALWAYS);
        HBox.setHgrow(rightColumn, Priority.ALWAYS);

        leftColumn.setMaxWidth(Double.MAX_VALUE);
        rightColumn.setMaxWidth(Double.MAX_VALUE);

        // Left side gets more space
        leftColumn.prefWidthProperty().bind(
                columns.widthProperty().multiply(0.65)
        );

        rightColumn.prefWidthProperty().bind(
                columns.widthProperty().multiply(0.35)
        );

        columns.getChildren().addAll(
                leftColumn,
                rightColumn
        );

        // ========================================================
        // ADD EVERYTHING
        // ========================================================

        main.getChildren().addAll(
                heading,
                columns
        );

        return main;
    }

    // ============================================================
    // OWNER INFORMATION
    // ============================================================

    private static VBox buildOwnerInformation() {

        VBox card = createCard();

        Label title = sectionTitle("👤", "Owner Information");

        GridPane grid = createGrid();

        TextField propertyId = createTextField(
                "Enter Property ID"
        );

        TextField ownerName = createTextField(
                "Full Legal Name"
        );

        TextField mobile = createTextField(
                "+91"
        );

        TextField email = createTextField(
                "owner@example.com"
        );

        TextField address = createTextField(
                "Complete property address"
        );

        ComboBox<String> ward = createComboBox(
                "Select Ward"
        );

        addField(grid, "Property ID", propertyId, 0, 0);
        addField(grid, "Owner Name", ownerName, 1, 0);

        addField(grid, "Mobile Number", mobile, 0, 1);
        addField(grid, "Email Address", email, 1, 1);

        addFieldFullWidth(
                grid,
                "Address",
                address,
                2
        );

        addFieldFullWidth(
                grid,
                "Ward / Area",
                ward,
                3
        );

        card.getChildren().addAll(
                title,
                grid
        );

        return card;
    }

    // ============================================================
    // PROPERTY DETAILS
    // ============================================================

    private static VBox buildPropertyDetails() {

        VBox card = createCard();

        Label title = sectionTitle(
                "🏠",
                "Property Details"
        );

        GridPane grid = createGrid();

        ComboBox<String> propertyType =
                createComboBox("Select Property Type");

        ComboBox<String> usageType =
                createComboBox("Select Usage");

        TextField propertyNumber =
                createTextField("PROP-2024-002");

        TextField surveyNumber =
                createTextField("e.g. 125/1A");

        TextField propertyArea =
                createTextField("125.50");

        TextField builtUpArea =
                createTextField("110.00");

        TextField totalFloors =
                createTextField("2");

        TextField constructionYear =
                createTextField("2018");

        addField(
                grid,
                "Property Type",
                propertyType,
                0,
                0
        );

        addField(
                grid,
                "Usage Type",
                usageType,
                1,
                0
        );

        addField(
                grid,
                "Property Number",
                propertyNumber,
                0,
                1
        );

        addField(
                grid,
                "Survey / Plot Number",
                surveyNumber,
                1,
                1
        );

        addField(
                grid,
                "Property Area (sq. ft.)",
                propertyArea,
                0,
                2
        );

        addField(
                grid,
                "Built-up Area (sq. ft.)",
                builtUpArea,
                1,
                2
        );

        addField(
                grid,
                "Total Floors",
                totalFloors,
                0,
                3
        );

        addField(
                grid,
                "Year of Construction",
                constructionYear,
                1,
                3
        );

        card.getChildren().addAll(
                title,
                grid
        );

        return card;
    }

    // ============================================================
    // TAX ASSESSMENT DETAILS
    // ============================================================

    private static VBox buildTaxAssessment() {

        VBox card = createCard();

        Label title = sectionTitle(
                "₹",
                "Tax Assessment Details"
        );

        GridPane grid = createGrid();

        TextField assessmentYear =
                createTextField("2024 - 2025");

        TextField assessmentValue =
                createTextField("0.00");

        TextField baseTax =
                createTextField("0.00");

        TextField sanitationTax =
                createTextField("0.00");

        TextField educationCess =
                createTextField("0.00");

        TextField previousArrears =
                createTextField("0.00");

        TextField lateFee =
                createTextField("0.00");

        TextField discount =
                createTextField("0.00");

        TextArea remarks =
                new TextArea();

        remarks.setPromptText(
                "Optional notes"
        );

        remarks.setPrefHeight(55);

        remarks.setStyle(
                "-fx-background-color: " + FIELD_BG + ";" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-font-size: 12px;"
        );

        addField(
                grid,
                "Assessment Year",
                assessmentYear,
                0,
                0
        );

        addField(
                grid,
                "Assessment Value (₹)",
                assessmentValue,
                1,
                0
        );

        addField(
                grid,
                "Base Property Tax (₹)",
                baseTax,
                0,
                1
        );

        addField(
                grid,
                "Water / Sanitation Tax (₹)",
                sanitationTax,
                1,
                1
        );

        addField(
                grid,
                "Education Cess (₹)",
                educationCess,
                0,
                2
        );

        addField(
                grid,
                "Previous Arrears (₹)",
                previousArrears,
                1,
                2
        );

        addField(
                grid,
                "Late Fee / Penalty (₹)",
                lateFee,
                0,
                3
        );

        addField(
                grid,
                "Discount / Rebate (₹)",
                discount,
                1,
                3
        );

        addFieldFullWidth(
                grid,
                "Other Charges / Remarks",
                remarks,
                4
        );

        card.getChildren().addAll(
                title,
                grid
        );

        return card;
    }

    // ============================================================
    // CALCULATION SUMMARY
    // ============================================================

    private static VBox buildCalculationSummary() {

        VBox card = new VBox(16);

        card.setPadding(
                new Insets(20)
        );

        card.setStyle(
                "-fx-background-color: " + FOREST_DEEP + ";" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.18), 15, 0.15, 0, 4);"
        );

        Label title = new Label(
                "Calculation Summary"
        );

        title.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );

        VBox values = new VBox(12);

        HBox baseTax =
                summaryRow("Base Tax", "₹0.00");

        HBox additionalTax =
                summaryRow("Additional Taxes", "₹0.00");

        HBox arrears =
                summaryRow("Arrears & Penalties", "₹0.00");

        HBox discount =
                summaryRow("Total Discounts", "₹0.00");

        Region divider = new Region();

        divider.setPrefHeight(1);

        divider.setStyle(
                "-fx-background-color: rgba(255,255,255,0.25);"
        );

        values.getChildren().addAll(
                baseTax,
                additionalTax,
                arrears,
                discount,
                divider
        );

        // ========================================================
        // TOTAL
        // ========================================================

        Label totalLabel = new Label(
                "TOTAL DUE"
        );

        totalLabel.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: rgba(255,255,255,0.75);"
        );

        Label totalAmount = new Label(
                "₹ 0.00"
        );

        totalAmount.setStyle(
                "-fx-font-size: 32px;" +
                "-fx-font-weight: 900;" +
                "-fx-text-fill: white;"
        );

        VBox totalBox = new VBox(3);

        totalBox.setAlignment(
                Pos.CENTER
        );

        totalBox.getChildren().addAll(
                totalLabel,
                totalAmount
        );

        // ========================================================
        // BUTTONS
        // ========================================================

        Button generate =
                new Button("Generate Bill");

        generate.setMaxWidth(
                Double.MAX_VALUE
        );

        generate.setStyle(
                "-fx-background-color: #A9E6B5;" +
                "-fx-text-fill: " + FOREST_DEEP + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 11 15;" +
                "-fx-cursor: hand;"
        );

        generate.setOnAction(e -> {
            System.out.println(
                    "Generate Bill clicked"
            );
        });

        Button save =
                new Button("Save Draft");

        Button print =
                new Button("Print");

        styleSecondaryButton(save);
        styleSecondaryButton(print);

        HBox secondaryButtons =
                new HBox(8);

        HBox.setHgrow(save, Priority.ALWAYS);
        HBox.setHgrow(print, Priority.ALWAYS);

        save.setMaxWidth(
                Double.MAX_VALUE
        );

        print.setMaxWidth(
                Double.MAX_VALUE
        );

        secondaryButtons.getChildren().addAll(
                save,
                print
        );

        Button cancel =
                new Button("Cancel");

        cancel.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: rgba(255,255,255,0.75);" +
                "-fx-font-size: 11px;" +
                "-fx-cursor: hand;"
        );

        card.getChildren().addAll(
                title,
                values,
                totalBox,
                generate,
                secondaryButtons,
                cancel
        );

        return card;
    }

    // ============================================================
    // PAYMENT STATUS
    // ============================================================

    private static VBox buildPaymentStatus() {

        VBox card = createCard();

        Label title =
                sectionTitle(
                        "●",
                        "Payment Status"
                );

        GridPane grid =
                createGrid();

        DatePicker dueDate =
                new DatePicker();

        dueDate.setPromptText(
                "Select date"
        );

        styleControl(dueDate);

        ComboBox<String> status =
                createComboBox(
                        "Select Status"
                );

        status.getItems().addAll(
                "Pending",
                "Paid",
                "Overdue"
        );

        TextField referenceId =
                createTextField(
                        "e.g. TXN-12345"
                );

        addFieldFullWidth(
                grid,
                "Payment Due Date",
                dueDate,
                0
        );

        addFieldFullWidth(
                grid,
                "Current Status",
                status,
                1
        );

        addFieldFullWidth(
                grid,
                "Reference ID (Optional)",
                referenceId,
                2
        );

        card.getChildren().addAll(
                title,
                grid
        );

        return card;
    }

    // ============================================================
    // CREATE CARD
    // ============================================================

    private static VBox createCard() {

        VBox card =
                new VBox(15);

        card.setPadding(
                new Insets(20)
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: 1;" +
                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.07), 14, 0.1, 0, 3);"
        );

        return card;
    }

    // ============================================================
    // SECTION TITLE
    // ============================================================

    private static Label sectionTitle(
            String icon,
            String text) {

        Label title =
                new Label(icon + "  " + text);

        title.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + FOREST_DEEP + ";"
        );

        return title;
    }

    // ============================================================
    // GRID
    // ============================================================

    private static GridPane createGrid() {

        GridPane grid =
                new GridPane();

        grid.setHgap(12);
        grid.setVgap(12);

        ColumnConstraints col1 =
                new ColumnConstraints();

        ColumnConstraints col2 =
                new ColumnConstraints();

        col1.setPercentWidth(50);
        col2.setPercentWidth(50);

        col1.setHgrow(Priority.ALWAYS);
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(
                col1,
                col2
        );

        return grid;
    }

    // ============================================================
    // ADD NORMAL FIELD
    // ============================================================

    private static void addField(
            GridPane grid,
            String labelText,
            Node control,
            int column,
            int row) {

        VBox box =
                new VBox(5);

        Label label =
                new Label(labelText);

        label.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + TEXT_DARK + ";"
        );

        control.maxWidth(
                Double.MAX_VALUE
        );

        box.getChildren().addAll(
                label,
                control
        );

        grid.add(
                box,
                column,
                row
        );
    }

    // ============================================================
    // ADD FULL WIDTH FIELD
    // ============================================================

    private static void addFieldFullWidth(
            GridPane grid,
            String labelText,
            Node control,
            int row) {

        VBox box =
                new VBox(5);

        Label label =
                new Label(labelText);

        label.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + TEXT_DARK + ";"
        );

        control.maxWidth(
                Double.MAX_VALUE
        );

        box.getChildren().addAll(
                label,
                control
        );

        grid.add(
                box,
                0,
                row,
                2,
                1
        );
    }

    // ============================================================
    // TEXT FIELD
    // ============================================================

    private static TextField createTextField(
            String prompt) {

        TextField field =
                new TextField();

        field.setPromptText(
                prompt
        );

        styleControl(field);

        return field;
    }

    // ============================================================
    // COMBO BOX
    // ============================================================

    private static ComboBox<String> createComboBox(
            String prompt) {

        ComboBox<String> combo =
                new ComboBox<>();

        combo.setPromptText(
                prompt
        );

        combo.setMaxWidth(
                Double.MAX_VALUE
        );

        combo.setPrefHeight(
                36
        );

        combo.getItems().addAll(
                "Residential",
                "Commercial",
                "Industrial"
        );

        styleControl(combo);

        return combo;
    }

    // ============================================================
    // CONTROL STYLE
    // ============================================================

    private static void styleControl(
            Control control) {

        control.setStyle(
                "-fx-background-color: " + FIELD_BG + ";" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-font-size: 12px;" +
                "-fx-text-fill: " + FOREST_DEEP + ";"
        );

        control.setPrefHeight(
                36
        );
    }

    // ============================================================
    // SUMMARY ROW
    // ============================================================

    private static HBox summaryRow(
            String name,
            String amount) {

        HBox row =
                new HBox();

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        Label label =
                new Label(name);

        label.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: rgba(255,255,255,0.75);"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Label value =
                new Label(amount);

        value.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );

        row.getChildren().addAll(
                label,
                spacer,
                value
        );

        return row;
    }

    // ============================================================
    // SECONDARY BUTTON
    // ============================================================

    private static void styleSecondaryButton(
            Button button) {

        button.setStyle(
                "-fx-background-color: rgba(255,255,255,0.12);" +
                "-fx-text-fill: white;" +
                "-fx-border-color: rgba(255,255,255,0.25);" +
                "-fx-border-radius: 7;" +
                "-fx-background-radius: 7;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );
    }
}