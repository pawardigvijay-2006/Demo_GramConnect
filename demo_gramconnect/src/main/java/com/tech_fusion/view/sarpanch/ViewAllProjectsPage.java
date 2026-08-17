package com.tech_fusion.view.sarpanch;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

/** Page for browsing projects with photo evidence and transparent budget tracking. */
public class ViewAllProjectsPage extends ProjectTrackerActionPage {

    private static final String FOREST_DEEP  = "#0B3D2E";
    private static final String SAFFRON      = "#E07A1F";
    private static final String TEAL         = "#0E8C8C";
    private static final String RED          = "#D94C38";
    private static final String VIOLET       = "#7C5CFC";
    private static final String FONT         = "'Inter', 'Segoe UI', 'Arial', sans-serif";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public Scene getViewAllProjectsScene(Runnable backToProjectTrackerAction, Runnable backToDashboardAction) {
        return createActionScene(
            "View All Projects",
            "Browse projects, track fund utilisation, and view on-site progress photos.",
            buildAllProjects(),
            backToProjectTrackerAction,
            backToDashboardAction
        );
    }

    private VBox buildAllProjects() {
        VBox content = new VBox(22);
        content.getChildren().addAll(
            projectCard(sample("Ward 4 Road Metaling", "Main Street", "In Progress", SAFFRON, "\uD83D\uDEA7", 0.62,
                120000, 74400,
                List.of(new Spend("2 Aug 2026", "Road-base material", 40000),
                        new Spend("9 Aug 2026", "Labour wages - week 1", 21400),
                        new Spend("13 Aug 2026", "Equipment rental", 13000)))),
            projectCard(sample("Water Tank Renovation", "Near School Area", "Ready for Completion", TEAL, "\uD83D\uDEB0", 0.95,
                85000, 81200,
                List.of(new Spend("30 Jul 2026", "Cement and tank lining", 52000),
                        new Spend("6 Aug 2026", "Plumbing fittings", 19200),
                        new Spend("11 Aug 2026", "Final inspection labour", 10000)))),
            projectCard(sample("Primary School Repair", "Gram Panchayat Office", "Delayed", RED, "\uD83C\uDFEB", 0.38,
                250000, 96000,
                List.of(new Spend("28 Jul 2026", "Roof sheet purchase", 60000),
                        new Spend("8 Aug 2026", "Electrical repair", 36000))))
        );
        return content;
    }

    // ---------- project card ----------

    private VBox projectCard(ProjectInfo p) {
        VBox card = new VBox(18);
        card.setPadding(new Insets(24));
        card.setStyle(cardStyle(20));

        // header: icon chip + name/location + status pill
        HBox header = new HBox(14);
        header.setAlignment(Pos.CENTER_LEFT);
        StackPane iconChip = new StackPane();
        iconChip.setPrefSize(42, 42);
        iconChip.setMinSize(42, 42);
        iconChip.setStyle("-fx-background-color: " + rgba(p.color, 0.14) + "; -fx-background-radius: 13;");
        Label ic = new Label(p.icon);
        ic.setStyle("-fx-font-size: 18px;");
        iconChip.getChildren().add(ic);

        VBox titleBox = new VBox(2);
        Label name = label(p.name, 18, 900, FOREST_DEEP);
        Label loc = label("\uD83D\uDCCD " + p.location, 12, 600, "rgba(11,61,46,0.6)");
        titleBox.getChildren().addAll(name, loc);
        Region grow = new Region();
        HBox.setHgrow(grow, Priority.ALWAYS);
        header.getChildren().addAll(iconChip, titleBox, grow, pill(p.status, p.color));

        // completion row: bar + live percent
        Label pctLabel = label(pct(p.progress), 13, 900, p.color);
        HBox progressRow = new HBox(10);
        progressRow.setAlignment(Pos.CENTER_LEFT);
        ProgressBar bar = new ProgressBar(p.progress);
        bar.setMaxWidth(Double.MAX_VALUE);
        bar.setStyle("-fx-accent: " + p.color + ";");
        HBox.setHgrow(bar, Priority.ALWAYS);
        progressRow.getChildren().addAll(bar, pctLabel);
        Label progressCaption = label("PROJECT COMPLETION", 10, 800, "rgba(11,61,46,0.55)");

        // budget summary tiles
        Label spentLabel = label(rs(p.spent), 16, 900, RED);
        Label remainingLabel = label(rs(p.allocated - p.spent), 16, 900, TEAL);
        HBox budgetRow = new HBox(12,
            statTile("ALLOCATED", label(rs(p.allocated), 16, 900, FOREST_DEEP)),
            statTile("SPENT SO FAR", spentLabel),
            statTile("REMAINING BALANCE", remainingLabel)
        );
        ProgressBar spendBar = new ProgressBar(p.allocated <= 0 ? 0 : p.spent / p.allocated);
        spendBar.setMaxWidth(Double.MAX_VALUE);
        spendBar.setStyle("-fx-accent: " + RED + ";");

        // expenditure history
        VBox historyList = new VBox(6);
        for (Spend s : p.history) historyList.getChildren().add(historyRow(s));
        HBox historyTitle = sectionLabel("\uD83E\uDDFE", "EXPENDITURE HISTORY");

        TextField descField = new TextField();
        descField.setPromptText("What was this expense for?");
        descField.setStyle(fieldStyle());
        descField.setPrefWidth(260);
        TextField amountField = new TextField();
        amountField.setPromptText("Amount (Rs.)");
        amountField.setStyle(fieldStyle());
        amountField.setPrefWidth(120);
        Button addExpense = actionButton("+ Record Expense", FOREST_DEEP);
        Label budgetFeedback = feedback();
        addExpense.setOnAction(e -> {
            try {
                double amt = Double.parseDouble(amountField.getText().trim());
                if (descField.getText().isBlank() || amt <= 0) throw new NumberFormatException();
                p.spent += amt;
                Spend entry = new Spend(LocalDate.now().format(DATE_FMT), descField.getText().trim(), amt);
                p.history.add(entry);
                historyList.getChildren().add(0, historyRow(entry));
                spentLabel.setText(rs(p.spent));
                remainingLabel.setText(rs(p.allocated - p.spent));
                spendBar.setProgress(p.allocated <= 0 ? 0 : p.spent / p.allocated);
                descField.clear(); amountField.clear();
                budgetFeedback.setText("Expense recorded and added to the public record.");
            } catch (NumberFormatException ex) {
                budgetFeedback.setText("Enter a valid description and amount.");
            }
        });
        HBox expenseForm = new HBox(10, descField, amountField, addExpense);
        expenseForm.setAlignment(Pos.CENTER_LEFT);

        // progress photo gallery
        HBox photoTitle = sectionLabel("\uD83D\uDCF7", "PROGRESS PHOTOS");
        HBox gallery = new HBox(10);
        gallery.setAlignment(Pos.CENTER_LEFT);
        for (Image img : p.photos) gallery.getChildren().add(thumbnail(img));
        Button upload = new Button("+ Upload Photo");
        upload.setStyle("-fx-background-color: " + rgba(FOREST_DEEP, 0.06) + "; -fx-background-radius: 10; -fx-border-color: " + rgba(FOREST_DEEP, 0.2) + ";"
            + "-fx-border-style: dashed; -fx-border-radius: 10; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 700;"
            + "-fx-text-fill: " + FOREST_DEEP + "; -fx-pref-width: 90; -fx-pref-height: 70; -fx-cursor: hand;");
        upload.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Upload Site Progress Photo");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
            File file = chooser.showOpenDialog(upload.getScene() == null ? null : upload.getScene().getWindow());
            if (file != null) {
                Image img = new Image(file.toURI().toString());
                p.photos.add(img);
                gallery.getChildren().add(gallery.getChildren().size() - 1, thumbnail(img));
            }
        });
        gallery.getChildren().add(upload);
        ScrollPane photoScroll = new ScrollPane(gallery);
        photoScroll.setFitToHeight(true);
        photoScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        photoScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        photoScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        photoScroll.setPrefHeight(90);

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + rgba(FOREST_DEEP, 0.1) + ";");

        card.getChildren().addAll(header, progressCaption, progressRow, divider, budgetRow, spendBar,
            historyTitle, historyList, expenseForm, budgetFeedback,
            photoTitle, photoScroll);
        return card;
    }

    // ---------- small builders ----------

    private HBox historyRow(Spend s) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 10, 8, 10));
        row.setStyle("-fx-background-color: " + rgba(FOREST_DEEP, 0.04) + "; -fx-background-radius: 8;");
        Label date = label(s.date(), 11, 700, "rgba(11,61,46,0.55)");
        date.setPrefWidth(80);
        Label desc = label(s.description(), 13, 600, FOREST_DEEP);
        HBox.setHgrow(desc, Priority.ALWAYS);
        Label amt = label(rs(s.amount()), 13, 800, RED);
        row.getChildren().addAll(date, desc, amt);
        return row;
    }

    private VBox thumbnail(Image img) {
        ImageView view = new ImageView(img);
        view.setFitWidth(90);
        view.setFitHeight(70);
        view.setPreserveRatio(false);
        Rectangle clip = new Rectangle(90, 70);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        view.setClip(clip);
        VBox box = new VBox(view);
        box.setStyle("-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.2), 6, 0.1, 0, 2);");
        return box;
    }

    private VBox statTile(String label, Label value) {
        VBox tile = new VBox(4);
        tile.setPadding(new Insets(12, 16, 12, 16));
        tile.setStyle("-fx-background-color: " + rgba(FOREST_DEEP, 0.05) + "; -fx-background-radius: 12;");
        HBox.setHgrow(tile, Priority.ALWAYS);
        tile.setMaxWidth(Double.MAX_VALUE);
        tile.getChildren().addAll(label(label, 10, 800, "rgba(11,61,46,0.55)"), value);
        return tile;
    }

    private HBox sectionLabel(String icon, String text) {
        HBox row = new HBox(6);
        row.setAlignment(Pos.CENTER_LEFT);
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 11px;");
        row.getChildren().addAll(ic, label(text, 11, 800, "rgba(11,61,46,0.6)"));
        return row;
    }

    private Label pill(String text, String color) {
        Label pill = label(text, 11, 800, "white");
        pill.setPadding(new Insets(5, 12, 5, 12));
        pill.setStyle(pill.getStyle() + "-fx-background-color: " + color + "; -fx-background-radius: 999;");
        return pill;
    }

    private Label label(String text, int size, int weight, String color) {
        Label l = new Label(text);
        l.setWrapText(true);
        l.setStyle("-fx-font-family: " + FONT + "; -fx-font-size: " + size + "px; -fx-font-weight: " + weight + "; -fx-text-fill: " + color + ";");
        return l;
    }

    private Label feedback() {
        return label("", 12, 700, TEAL);
    }

    private Button actionButton(String text, String color) {
        Button b = new Button(text);
        b.setPadding(new Insets(9, 16, 9, 16));
        b.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 9; -fx-text-fill: white; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-font-weight: 800; -fx-cursor: hand;");
        return b;
    }

    private String fieldStyle() {
        return "-fx-background-color: white; -fx-background-radius: 9; -fx-border-color: " + rgba(FOREST_DEEP, 0.16) + "; -fx-border-radius: 9; -fx-padding: 8 12 8 12; -fx-font-family: " + FONT + "; -fx-font-size: 12px; -fx-text-fill: " + FOREST_DEEP + ";";
    }

    private String cardStyle(int radius) {
        return "-fx-background-color: rgba(255,255,255,0.92);" +
               "-fx-background-radius: " + radius + ";" +
               "-fx-border-color: rgba(255,255,255,0.6);" +
               "-fx-border-radius: " + radius + ";" +
               "-fx-border-width: 1;" +
               "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.10), 16, 0.1, 0, 4);";
    }

    private String rgba(String hex, double alpha) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
    }

    private String rs(double amount) {
        return String.format("Rs. %,.0f", amount);
    }

    private String pct(double progress) {
        return String.format("%.0f%%", progress * 100);
    }

    // ---------- data holders ----------

    private ProjectInfo sample(String name, String location, String status, String color, String icon, double progress,
                                double allocated, double spent, List<Spend> history) {
        ProjectInfo p = new ProjectInfo();
        p.name = name; p.location = location; p.status = status; p.color = color; p.icon = icon; p.progress = progress;
        p.allocated = allocated; p.spent = spent;
        p.history = new ArrayList<>(history);
        p.photos = FXCollections.observableArrayList();
        return p;
    }

    private static class ProjectInfo {
        String name, location, status, color, icon;
        double progress, allocated, spent;
        List<Spend> history;
        ObservableList<Image> photos;
    }

    private record Spend(String date, String description, double amount) {}
}