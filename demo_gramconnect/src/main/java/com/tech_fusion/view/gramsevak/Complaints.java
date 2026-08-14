package com.tech_fusion.view.gramsevak;

//import com.example.model.ComplaintInfo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Complaints {
        /* ---------- Color palette (from the HTML template) ---------- */
        private static final String FOREST_DEEP = "#0B3D2E";
        private static final String FOREST_LIGHT = "#0F4736";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String CONTEXT_TEAL = "#0E8C8C";
        private static final String AI_VIOLET = "#7C5CFC";
        private static final String DELAYED_RED = "#D94C38";
        // Light green sidebar colors (requested change)
        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#Bce3cc";
        private static final String SIDEBAR_BOT = "#A9D8BD";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";

        public static VBox getComplaintManagement() {
                BorderPane titlBorderPane = new BorderPane();
                VBox content = new VBox(10);
                content.setPadding(new Insets(32, 40, 48, 40));

                Text title = new Text("Complaint Management");
                title.setStyle("-fx-font-size:20px; -fx-fill:black; -fx-font-weight:bold");

                Text subtitle = new Text("Track, assign and resolve civic issues efficiently");
                subtitle.setStyle("-fx-font-size:11px; -fx-fill:black");

                VBox titleBox = new VBox(5);
                titleBox.getChildren().addAll(title, subtitle);

                titlBorderPane.setLeft(title);

                // =========================
                // SUMMARY CARDS
                // =========================

                HBox cards = new HBox(24);

                VBox card1 = createSummaryCard(FOREST_DEEP, "◉", "142", "Total Active");
                VBox card2 = createSummaryCard(CONTEXT_TEAL, "✦", "28", "New");
                VBox card3 = createSummaryCard(DELAYED_RED, "↻", "84", "In Progress");
                VBox card4 = createSummaryCard("#7956F5", "⚠", "12", "Escalated");

                HBox.setHgrow(card1, Priority.ALWAYS);
                HBox.setHgrow(card2, Priority.ALWAYS);
                HBox.setHgrow(card3, Priority.ALWAYS);
                HBox.setHgrow(card4, Priority.ALWAYS);

                cards.getChildren().addAll(card1, card2, card3, card4);

                // =========================
                // MAIN AREA
                // =========================

                HBox mainArea = new HBox(15);
                mainArea.setMaxWidth(Double.MAX_VALUE);

                // =========================
                // COMPLAINT TABLE
                // =========================

                VBox complaintBox = new VBox();
                complaintBox.setSpacing(8);
                complaintBox.setPadding(new Insets(10));

                complaintBox.setMaxWidth(Double.MAX_VALUE);

                HBox.setHgrow(complaintBox, Priority.ALWAYS);
                complaintBox.setStyle(
                                "-fx-background-color:white;" +
                                                "-fx-border-color:#D7DCE5;" +
                                                "-fx-border-radius:8;" +
                                                "-fx-background-radius:8;");
                mainArea.setMaxWidth(Double.MAX_VALUE);
                VBox.setVgrow(complaintBox, Priority.ALWAYS);

                // ============================================================
                // HEADING
                // ============================================================

                BorderPane complaintHeading = new BorderPane();

                Label recentTitle = new Label("Recent Complaints");

                recentTitle.setStyle(
                                "-fx-font-size:14px;" +
                                                "-fx-font-weight:bold;");

                complaintHeading.setLeft(recentTitle);

                complaintHeading.setPadding(
                                new Insets(12));

                // ============================================================
                // COMPLAINT LIST
                // ============================================================

                VBox complaintList = new VBox(8);
                complaintList.setPadding(
                                new Insets(0, 12, 12, 12));
                complaintList.setMaxWidth(Double.MAX_VALUE);
                VBox.setVgrow(complaintList, Priority.ALWAYS);

                // Complaint 1
                HBox complaint1 = createComplaintItem("C001", "Water Supply", "Pending", "15 Jan 2026");

                // Complaint 2
                HBox complaint2 = createComplaintItem("C002", "Street Light", "In Progress", "14 Oct 2026");

                // Complaint 3
                HBox complaint3 = createComplaintItem("C003", "Road Damage", "Resolved", "12 Oct 2026");

                // Complaint 4
                HBox complaint4 = createComplaintItem("C004", "Garbage Collection", "Escalated", "10 Oct 2026");

                complaintList.getChildren().addAll(
                                complaint1,
                                complaint2,
                                complaint3,
                                complaint4);

                // ============================================================
                // ADD EVERYTHING
                // ============================================================

                complaintBox.getChildren().addAll(
                                complaintHeading,
                                complaintList);

                mainArea.getChildren().add(
                                complaintBox);
                VBox.setVgrow(mainArea, Priority.ALWAYS);

                content.getChildren().addAll(
                                titlBorderPane,
                                cards,
                                mainArea);

                return content;
        }

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

        private static HBox createComplaintItem(
                        String id,
                        String category,
                        String status,
                        String date) {

                HBox item = new HBox(20);
                item.setAlignment(Pos.CENTER_LEFT);
                item.setPadding(new Insets(12));
                item.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(item, Priority.ALWAYS);
                item.setStyle("-fx-background-color:#F8FAF9;-fx-background-radius:8;");

                // ========================================================
                // ID
                // ========================================================

                VBox idBox = new VBox(3);

                Label idTitle = new Label("ID");

                idTitle.setStyle(
                                "-fx-font-size:10px;" +
                                                "-fx-text-fill:#7A8A87;");

                Label idLabel = new Label(id);

                idLabel.setStyle(
                                "-fx-font-size:13px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:#285B5B;");

                idBox.getChildren().addAll(
                                idTitle,
                                idLabel);

                // ========================================================
                // CATEGORY
                // ========================================================

                VBox categoryBox = new VBox(3);

                Label categoryTitle = new Label("Category");

                categoryTitle.setStyle(
                                "-fx-font-size:10px;" +
                                                "-fx-text-fill:#7A8A87;");

                Label categoryLabel = new Label(category);

                categoryLabel.setStyle(
                                "-fx-font-size:13px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:#285B5B;");

                categoryBox.getChildren().addAll(
                                categoryTitle,
                                categoryLabel);

                // ========================================================
                // DATE
                // ========================================================

                VBox dateBox = new VBox(3);

                Label dateTitle = new Label("Date");
                dateTitle.setStyle("-fx-font-size:10px;-fx-text-fill:#7A8A87;");

                Label dateLabel = new Label(date);
                dateLabel.setStyle("-fx-font-size:12px;-fx-font-weight:bold;-fx-text-fill:#285B5B;");

                dateBox.getChildren().addAll(
                                dateTitle,
                                dateLabel);

                // ========================================================
                // SPACER
                // ========================================================
                Region spacer = new Region();
                HBox.setHgrow(spacer,
                                Priority.ALWAYS);

                // ========================================================
                // STATUS
                // ========================================================

                Label statusLabel = new Label(status);
                statusLabel.setPadding(new Insets(5, 10, 5, 10));
                String statusColor;

                if (status.equals("Pending")) {
                        statusColor = "#E67E1F";
                } else if (status.equals("In Progress")) {
                        statusColor = "#159A9C";
                } else if (status.equals("Resolved")) {
                        statusColor = "#16803C";
                } else {
                        statusColor = "#D93025";
                }

                statusLabel.setStyle(
                                "-fx-background-color:" + statusColor + "22;" +
                                                "-fx-background-radius:7;" +
                                                "-fx-font-size:10px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:" + statusColor + ";");

                idBox.setPrefWidth(180);
                categoryBox.setPrefWidth(350);
                dateBox.setPrefWidth(250);

                // ========================================================
                // ADD TO HBOX
                // ========================================================

                item.getChildren().addAll(
                                idBox,
                                categoryBox,
                                dateBox,
                                spacer,
                                statusLabel);

                return item;
        }

        // helpers
        private static String cardStyle(int radius) {
                return "-fx-background-color: rgba(255,255,255,0.88);" +
                                "-fx-background-radius: " + radius + ";" +
                                "-fx-border-color: rgba(255,255,255,0.5);" +
                                "-fx-border-radius: " + radius + ";" +
                                "-fx-border-width: 1;" +
                                "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.06), 16, 0.1, 0, 4);";
        }

        private static String rgba(String hex, double alpha) {
                int r = Integer.parseInt(hex.substring(1, 3), 16);
                int g = Integer.parseInt(hex.substring(3, 5), 16);
                int b = Integer.parseInt(hex.substring(5, 7), 16);
                return "rgba(" + r + "," + g + "," + b + "," + alpha + ")";
        }
}
