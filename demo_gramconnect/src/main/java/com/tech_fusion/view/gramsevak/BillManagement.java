
package com.tech_fusion.view.gramsevak;

import com.tech_fusion.model.gramsevak.Bill;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BillManagement {

    public static VBox getBillManageContent(Runnable propertyBillAction ) //Runnable waterBillAction
     {

        VBox content = new VBox(20);
        content.setPadding(new Insets(25, 30, 30, 30));
        VBox.setVgrow(content, Priority.ALWAYS);
        content.setStyle("-fx-background-color: transparent;");
        // ============================================================
        // HEADER
        // ============================================================
        BorderPane titlePane = new BorderPane();

        VBox titleBox = new VBox(5);

        Text title = new Text("Bill Management");
        title.setStyle(
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #0B4F43;"
        );

        Text subtitle = new Text(
                "Manage and track utility and property bills for the village"
        );
        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-fill: #657180;"
        );

        titleBox.getChildren().addAll(title, subtitle);
        // ============================================================
        // GENERATE BUTTONS
        // ============================================================
        Button waterBillButton = new Button("+  Generate Water Bill");
        waterBillButton.setStyle(
                "-fx-background-color: #0B4F43;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 10 15 10 15;" +
                "-fx-cursor: hand;"
        );
        waterBillButton.setOnAction(event ->
                System.out.println("Generate Water Bill clicked")
        );

        Button propertyBillButton = new Button("+  Generate Property Bill");
        propertyBillButton.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: #0B4F43;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: #0B4F43;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 9 15 9 15;" +
                "-fx-cursor: hand;"
        );
        propertyBillButton.setOnAction(event ->{
                System.out.println("Generate Property Bill clicked");
                propertyBillAction.run();
     });

        HBox titleButtons = new HBox(10);
        titleButtons.setAlignment(Pos.CENTER_RIGHT);
        titleButtons.getChildren().addAll(
                waterBillButton,
                propertyBillButton
        );

        titlePane.setLeft(titleBox);
        titlePane.setRight(titleButtons);

        // ============================================================
        // SUMMARY CARDS
        // ============================================================

        HBox summaryCards = new HBox(15);

        VBox totalBills = createSummaryCard(
                "TOTAL BILLS",
                "128",
                "#0B4F43"
        );
        VBox paidBills = createSummaryCard(
                "PAID",
                "96",
                "#16803C"
        );
        VBox pendingBills = createSummaryCard(
                "PENDING",
                "24",
                "#E67E1F"
        );
        VBox overdueBills = createSummaryCard(
                "OVERDUE",
                "8",
                "#D93025"
        );
        HBox.setHgrow(totalBills, Priority.ALWAYS);
        HBox.setHgrow(paidBills, Priority.ALWAYS);
        HBox.setHgrow(pendingBills, Priority.ALWAYS);
        HBox.setHgrow(overdueBills, Priority.ALWAYS);

        summaryCards.getChildren().addAll(
                totalBills,
                paidBills,
                pendingBills,
                overdueBills
        );
        // ============================================================
        // SEARCH + FILTERS
        // ============================================================
        VBox filterCard = new VBox(12);
        filterCard.setPadding(new Insets(18));
        filterCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #E1E7E4;" +
                "-fx-border-radius: 14;"
        );

        Label filterTitle = new Label("Search & Filter Bills");
        filterTitle.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #0B4F43;"
        );

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText(
                "🔍  Search Citizen Name or House No."
        );

        ComboBox<String> areaComboBox = new ComboBox<>();
        areaComboBox.getItems().addAll(
                "All Areas",
                "Ward 1",
                "Ward 2",
                "Ward 3"
        );
        areaComboBox.setValue("All Areas");

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(
                "All Statuses",
                "Paid",
                "Pending",
                "Overdue"
        );
        statusComboBox.setValue("All Statuses");     

        Button searchButton = new Button("Search");
        searchButton.setStyle(
                "-fx-background-color: #159A9C;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 0 20 0 20;" +
                "-fx-cursor: hand;"
        );
        searchButton.setOnAction(event ->
                System.out.println("Search clicked")
        );


        HBox.setHgrow(searchField, Priority.ALWAYS);
        areaComboBox.setMaxWidth(Double.MAX_VALUE);
        statusComboBox.setMaxWidth(Double.MAX_VALUE);
        searchButton.setMaxWidth(Double.MAX_VALUE);

        searchField.setMinWidth(150);
        areaComboBox.setMinWidth(100);
        statusComboBox.setMinWidth(110);
        searchButton.setMinWidth(70);

        filterBox.getChildren().addAll(
                searchField,
                areaComboBox,
                statusComboBox,
                searchButton
        );

        filterCard.getChildren().addAll(
                filterTitle,
                filterBox
        );
        // ============================================================
        // BILL LIST CARD
        // ============================================================
        VBox billSection = new VBox(12);
        billSection.setPadding(new Insets(20));
        billSection.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: #E1E7E4;" +
                "-fx-border-radius: 16;"
        );

        BorderPane billHeader = new BorderPane();
        Label billTitle = new Label("Recent Bills");
        billTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #0B4F43;"
        );

        Label viewAll = new Label("View All →");
        viewAll.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #159A9C;" +
                "-fx-cursor: hand;"
        );

        billHeader.setLeft(billTitle);
        billHeader.setRight(viewAll);
        // ============================================================
        // BILL ITEMS
        // ============================================================
        VBox billList = new VBox(8);

        Bill bill1 = new Bill(
                "Ramesh Kumar",
                "H-102",
                "Water",
                450,
                "15 Oct 2026",
                "Paid"
        );
        Bill bill2 = new Bill(
                "Anita Sharma",
                "H-205",
                "Property",
                1200,
                "20 Oct 2026",
                "Pending"
        );
        Bill bill3 = new Bill(
                "Vikram Singh",
                "H-310",
                "Water",
                650,
                "10 Oct 2026",
                "Overdue"
        );
        Bill bill4 = new Bill(
                "Sunita Patil",
                "H-115",
                "Property",
                900,
                "25 Oct 2026",
                "Paid"
        );
        billList.getChildren().addAll(
                createBillItem(bill1, "#16803C"),
                createBillItem(bill2, "#E67E1F"),
                createBillItem(bill3, "#D93025"),
                createBillItem(bill4, "#16803C")
        );

        billSection.getChildren().addAll(
                billHeader,
                billList
        );
        // ============================================================
        // ADD EVERYTHING
        // ============================================================
        content.getChildren().addAll(
                titlePane,
                summaryCards,
                filterCard,
                billSection
        );
        return content;
    }
    // ============================================================
    // SUMMARY CARD
    // ============================================================
    private static VBox createSummaryCard(
            String titleText,
            String numberText,
            String accent) {

        VBox card = new VBox(8);

        card.setPadding(new Insets(15));

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #E1E7E4;" +
                "-fx-border-radius: 14;"
        );

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 11px;-fx-font-weight: bold;-fx-text-fill: #657180;");

        Label number = new Label(numberText);
        number.setStyle("-fx-font-size: 28px;-fx-font-weight: bold;-fx-text-fill: " + accent + ";");

        Label status = new Label("●  " + titleText);
        status.setStyle("-fx-font-size: 11px;-fx-font-weight: bold;-fx-text-fill: " + accent + ";");

        card.getChildren().addAll(
                title,
                number,
                status
        );
        return card;
    }
    // ============================================================
    // SINGLE BILL ITEM
    // ============================================================
    private static HBox createBillItem( Bill bill, String accent) {
        HBox item = new HBox(15);
        HBox.setHgrow(item, Priority.ALWAYS);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12));
        item.setStyle(
                "-fx-background-color: #F8FAF9;" +
                "-fx-background-radius: 10;"
        );
        // ------------------------------------------------------------
        // ICON
        // ------------------------------------------------------------
        VBox iconBox = new VBox();
        VBox.setVgrow(iconBox, Priority.ALWAYS);
        iconBox.setAlignment(Pos.CENTER);
        //iconBox.setPrefSize(45, 45);
        //iconBox.setMinSize(45, 45);
        iconBox.setStyle(
                "-fx-background-color: " + rgba(accent, 0.12) + ";" +
                "-fx-background-radius: 10;"
        );
        Label icon = new Label(
                bill.getBillType().equals("Water")
                        ? "💧"
                        : "🏠"
        );
        icon.setStyle("-fx-font-size: 18px;");

        iconBox.getChildren().add(icon);
        // ------------------------------------------------------------
        // CITIZEN DETAILS
        // ------------------------------------------------------------
        VBox citizenBox = new VBox(3);
        VBox.setVgrow(citizenBox, Priority.ALWAYS);

        Label citizen = new Label(bill.getCitizenname());
        citizen.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;-fx-text-fill: #285B5B;");

        Label house = new Label("House No: " + bill.getHousename());
        house.setStyle("-fx-font-size: 11px;-fx-text-fill: #7A8A87;");

        citizenBox.getChildren().addAll(
                citizen,
                house
        );
        // ------------------------------------------------------------
        // BILL TYPE
        // ------------------------------------------------------------
        VBox typeBox = new VBox(3);
        VBox.setVgrow(typeBox, Priority.ALWAYS);

        Label typeTitle = new Label("Bill Type");
        typeTitle.setStyle("-fx-font-size: 10px;-fx-text-fill: #7A8A87;");

        Label type = new Label(bill.getBillType());
        type.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;-fx-text-fill: #285B5B;");

        typeBox.getChildren().addAll(
                typeTitle,
                type
        );
        // ------------------------------------------------------------
        // AMOUNT
        // ------------------------------------------------------------
        VBox amountBox = new VBox(3);
        VBox.setVgrow(amountBox, Priority.ALWAYS);

        Label amountTitle = new Label("Amount");
        amountTitle.setStyle("-fx-font-size: 10px;-fx-text-fill: #7A8A87;");

        Label amount = new Label("₹" + bill.getAmount());
        amount.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;-fx-text-fill: #0B4F43;");

        amountBox.getChildren().addAll(
                amountTitle,
                amount
        );
        // ------------------------------------------------------------
        // DUE DATE
        // ------------------------------------------------------------
        VBox dateBox = new VBox(3);
        VBox.setVgrow(dateBox, Priority.ALWAYS);

        Label dateTitle = new Label("Due Date");
        dateTitle.setStyle( "-fx-font-size: 10px;-fx-text-fill: #7A8A87;");

        Label date = new Label(bill.getDueDate());
        date.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;-fx-text-fill: #285B5B;");

        dateBox.getChildren().addAll(
                dateTitle,
                date
        );
        // ------------------------------------------------------------
        // SPACER
        // ------------------------------------------------------------
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // ------------------------------------------------------------
        // STATUS
        // ------------------------------------------------------------

        Label status = new Label(bill.getStatus());
        status.setPadding(new Insets(5, 10, 5, 10));

        status.setStyle(
                "-fx-background-color: " +
                rgba(accent, 0.12) + ";" +
                "-fx-background-radius: 7;" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + accent + ";"
        );

        item.getChildren().addAll(
                iconBox,
                citizenBox,
                typeBox,
                amountBox,
                dateBox,
                spacer,
                status
        );

        return item;
    }
    // ============================================================
    // RGBA HELPER
    // ============================================================
    private static String rgba( String hex,double alpha) {

        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return "rgba(" +
                r + "," +
                g + "," +
                b + "," +
                alpha + ")";
    }

    public static Node getBillManageContent(Object object, Object object2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBillManageContent'");
    }
}
