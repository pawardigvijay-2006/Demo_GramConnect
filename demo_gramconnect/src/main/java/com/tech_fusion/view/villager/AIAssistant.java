package com.tech_fusion.view.villager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * GramConnect - AI Assistant (Villager)
 *
 * ============================================================
 * Uses the same palette / header pattern as ProjectTransparency.java
 * (forest-green PRIMARY, light tint backgrounds per accent, white
 * cards with a thin BORDER and no drop-shadow / hover-lift - unlike
 * the "glass card" look used on VillagerDashboard.java and the other
 * villager pages).
 *
 * Sections (matching the reference screenshot):
 * 1. Page title ("AI Assistant" + sparkle icon) + subtitle.
 * 2. Hero greeting card (robot illustration placeholder, greeting
 * text, "How can I help you today?").
 * 3. "Try asking me something" - 6 suggestion chips in a 3-column grid.
 * 4. "Quick Information" - 4 stat cards (Active Projects, Schemes
 * Available, Next Gram Sabha, Documents Available).
 * 5. "Ask me anything..." chat panel with one assistant message
 * bubble and a message input row (mic + send button).
 *
 * ARCHITECTURE NOTE: same as ProjectTransparency.java - one
 * self-contained file, no FXML, no external CSS. Mock data is
 * marked with TODO comments showing where a real Service class
 * would plug in later.
 *
 * Usage (same pattern as ProjectTransparency.getProjectBPane()):
 *
 *      root.setCenter(new AIAssistant().getAssistantPane());
 * ============================================================
 */
public class AIAssistant {

        // ================= COLORS (shared with ProjectTransparency.java) =================
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

        private static final String PURPLE = "#8B5CF6";
        private static final String LIGHT_PURPLE = "#F1EDFC";

        private static final String BACKGROUND = "#F4F8FB";

        private static final String TEXT_PRIMARY = "#10251A";
        private static final String TEXT_SECONDARY = "#66756C";

        private static final String BORDER = "#D8E2DC";
        private static final String SECONDARY = "#1976D2";

        private static final String FONT_FAMILY = "'Inter', 'Segoe UI', 'Arial', sans-serif";
        private static final String SAFFRON_MAIN = "#E07A1F";
        private static final String FOREST_DEEP = "#0B3D2E";

        private static final String SIDEBAR_TOP = "#CDEBD8";
        private static final String SIDEBAR_MID = "#BCE3CC";
        private static final String SIDEBAR_BOT = "#A9D8BD";
        private static final String DELAYED_RED = "#D94C38";

        /** Public entry point - returns the fully built Bills & Payments screen. */
        public Scene getAiAssiatantScene(Runnable backToDashboardAction) {

                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BACKGROUND + ";");

                root.setLeft(buildSidebar(backToDashboardAction));
                root.setCenter(buildMainArea());

                return new Scene(root, 1500, 850);
        }

        // =================================================================
        // SIDEBAR - identical structure/colors to VillagerDashboard.java,
        // except "Project transparency" is the active row here, and
        // "Dashboard" calls the Runnable instead of homeStage.setScene(...)
        // directly (this class never touches a Stage at all).
        // =================================================================
        private VBox buildSidebar(Runnable backToDashboardAction) {
                VBox sidebar = new VBox();
                sidebar.setPrefWidth(230);
                sidebar.setMinWidth(230);
                sidebar.setStyle(
                                "-fx-background-color: linear-gradient(to bottom, " + SIDEBAR_TOP + ", " + SIDEBAR_MID
                                                + ", " + SIDEBAR_BOT + ");"
                                                + "-fx-border-color: transparent rgba(11,61,46,0.10) transparent transparent;"
                                                + "-fx-border-width: 0 1 0 0;"
                                                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.20), 24, 0.2, 4, 0);");

                Image logoImage = new Image("assets\\images\\gramconnect.png");
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
                                                + "-fx-font-weight: 900;");

                Label subtitle = new Label("Village Governance");
                subtitle.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-text-fill: rgba(11,61,46,0.65);"
                                                + "-fx-font-size: 9px;"
                                                + "-fx-font-weight: 700;"
                                                + "-fx-letter-spacing: 0.05em;");

                VBox logoTextBox = new VBox(0, logoText, subtitle);
                HBox logo = new HBox(8, logoIcon, logoTextBox);
                logo.setAlignment(Pos.CENTER_LEFT);
                VBox logoBox = new VBox(logo);
                logoBox.setPadding(new Insets(18, 18, 22, 18));

                // ---------------- Nav items ----------------
                // "Dashboard" is the only item that needs to actually navigate
                // anywhere from this page - it just calls the Runnable it was
                // handed. The rest are inactive placeholders for now, same as
                // they'd be on any page other than their own.

                Label dashboardNav = navItem("\uD83C\uDFE0  Dashboard", false);
                dashboardNav.setOnMouseClicked(e -> {
                        backToDashboardAction.run();
                });

                Label projectsNav = navItem("\uD83C\uDFD7  Project transparency", false);
                projectsNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new ProjectTransparency().getProjectScene(backToDashboardAction));
                });

                Label complaintsNav = navItem("\uD83D\uDCAC  Complaints", false);
                complaintsNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new ComplaintsPage().getComplaintsPage(backToDashboardAction));
                });
                Label schemesNav = navItem("\uD83C\uDF81  Government schemes", false);
                schemesNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new GovernmentSchemes().getSchemesScene(backToDashboardAction));
                });
                Label certificatesNav = navItem("\uD83D\uDCDC  Certificates", false);
                certificatesNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new Certificates().getCertificatesScene(backToDashboardAction));
                });
                Label billsNav = navItem("\uD83D\uDCB3  Bills & Payments", false);
                billsNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new BillsAndPayments().getBillsScene(backToDashboardAction));
                });
                Label announcementsNav = navItem("\uD83D\uDCE2  Announcements", false);
                announcementsNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new Announcements().getAnnouncementScene(backToDashboardAction));
                });
                Label gramSabhaNav = navItem("\uD83D\uDC65  Gram Sabha", false);
                gramSabhaNav.setOnMouseClicked(e ->{
                        // Runnable backToProjectTransparency = () -> back();
                        VillagerDashboard.homeStage.setScene(new GramSabha().getGramSabhaScene(backToDashboardAction));
                });
                Label aiAssistantNav = navItem("\uD83E\uDD16  AI village assistant", true);
                
                // TODO: wire these the same way once each page exposes its own
                // getXScene(Runnable backToDashboardAction) method.

                VBox navItems = new VBox(4,
                                dashboardNav,
                                projectsNav,
                                complaintsNav,
                                schemesNav,
                                certificatesNav,
                                billsNav,
                                announcementsNav,
                                gramSabhaNav,
                                aiAssistantNav);
                navItems.setPadding(new Insets(0, 10, 0, 10));
                VBox.setVgrow(navItems, Priority.ALWAYS);

                Label emergency = new Label("\u26A0  Emergency assistance");
                emergency.setWrapText(true);
                emergency.setPadding(new Insets(10, 12, 10, 12));
                emergency.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-text-fill: " + DELAYED_RED + ";"
                                                + "-fx-font-size: 12px;"
                                                + "-fx-font-weight: 700;"
                                                + "-fx-background-color: rgba(217,76,56,0.12);"
                                                + "-fx-background-radius: 10;");
                VBox emergencyBox = new VBox(emergency);
                emergencyBox.setPadding(new Insets(12, 16, 18, 18));

                sidebar.getChildren().addAll(logoBox, navItems, emergencyBox);
                return sidebar;
        }

        /** Same nav-row builder as VillagerDashboard.java: no handler attached here - callers attach their own. */
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
                                                        + "-fx-cursor: hand;");
                } else {
                        String base = "-fx-font-family: " + FONT_FAMILY + ";"
                                        + "-fx-text-fill: rgba(11,61,46,0.80);"
                                        + "-fx-font-size: 13px;"
                                        + "-fx-font-weight: 700;"
                                        + "-fx-cursor: hand;";
                        item.setStyle(base);

                        item.setOnMouseEntered(e -> item.setStyle(
                                        "-fx-font-family: " + FONT_FAMILY + ";"
                                                        + "-fx-background-color: rgba(255,255,255,0.45);"
                                                        + "-fx-text-fill: " + FOREST_DEEP + ";"
                                                        + "-fx-font-weight: 700;"
                                                        + "-fx-font-size: 13px;"
                                                        + "-fx-background-radius: 8;"
                                                        + "-fx-cursor: hand;"));

                        item.setOnMouseExited(e -> item.setStyle(base));
                }

                return item;
        }

        // =================================================================
        // MAIN AREA (header on top, scrollable content below it)
        // =================================================================
        private BorderPane buildMainArea() {
                BorderPane main = new BorderPane();
                main.setTop(buildHeader());
                main.setCenter(buildScrollableContent());
                return main;
        }
        // =================================================================
        // TOPBAR (identical pattern to ProjectTransparency.buildHeader())
        // =================================================================
        private HBox buildHeader() {
                HBox header = new HBox(16);
                header.setAlignment(Pos.CENTER_LEFT);
                header.setPadding(new Insets(14, 28, 14, 28));
                header.setStyle(
                                "-fx-background-color: rgba(255,255,255,0.92);"
                                                + "-fx-border-color: transparent transparent rgba(255,255,255,0.6) transparent;"
                                                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 8, 0.1, 0, 2);");

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
                                                + "-fx-border-width: 1;");
                Label searchIcon = new Label("\uD83D\uDD0D");
                searchIcon.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(11,61,46,0.5);");
                TextField search = new TextField();
                search.setPromptText("Search anything...");
                search.setStyle(
                                "-fx-background-color: transparent;"
                                                + "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-font-size: 12px;"
                                                + "-fx-text-fill: " + FOREST_DEEP + ";"
                                                + "-fx-prompt-text-fill: rgba(11,61,46,0.40);");
                HBox.setHgrow(search, Priority.ALWAYS);
                searchBox.getChildren().addAll(searchIcon, search);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label bellIcon = new Label("\uD83D\uDD14");
                bellIcon.setStyle("-fx-font-size: 15px;");
                StackPane bell = new StackPane(bellIcon);
                bell.setPrefSize(38, 38);
                bell.setMaxSize(38, 38);
                bell.setStyle(
                                "-fx-background-color: " + BACKGROUND + ";"
                                                + "-fx-background-radius: 999;"
                                                + "-fx-effect: dropshadow(gaussian, rgba(11,61,46,0.08), 4, 0.1, 0, 1);");
                Label badge = new Label("3");
                badge.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-background-color: " + ERROR + ";"
                                                + "-fx-text-fill: white;"
                                                + "-fx-font-size: 9px; -fx-font-weight: 800;"
                                                + "-fx-background-radius: 999;"
                                                + "-fx-padding: 1 5 1 5;");
                StackPane bellWithBadge = new StackPane(bell, badge);
                StackPane.setAlignment(badge, Pos.TOP_RIGHT);

                StackPane avatar = new StackPane(new Label("RP"));
                avatar.setPrefSize(34, 34);
                avatar.setMaxSize(34, 34);
                avatar.setStyle(
                                "-fx-background-color: " + FOREST_DEEP + ";"
                                                + "-fx-background-radius: 18;"
                                                + "-fx-border-color: " + SAFFRON_MAIN + ";"
                                                + "-fx-border-width: 2;"
                                                + "-fx-border-radius: 18;");
                ((Label) avatar.getChildren().get(0))
                                .setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");

                Label name = new Label("Ramesh Patil");
                name.setStyle(
                                "-fx-font-family: " + FONT_FAMILY
                                                + "; -fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: "
                                                + TEXT_PRIMARY + ";");
                Label role = new Label("Villager, Suryapuri");
                role.setStyle(
                                "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 11px; -fx-text-fill: "
                                                + TEXT_SECONDARY + ";");
                VBox nameBox = new VBox(name, role);

                Label chevron = new Label("\u25BE");
                chevron.setStyle("-fx-text-fill: " + TEXT_SECONDARY + ";");

                HBox profile = new HBox(8, avatar, nameBox, chevron);
                profile.setAlignment(Pos.CENTER_LEFT);

                header.getChildren().addAll(searchBox, spacer, bellWithBadge, profile);
                return header;
        }

        // =================================================================
        // SCROLLABLE CONTENT
        // =================================================================
        private ScrollPane buildScrollableContent() {
                VBox content = new VBox(18);
                content.setPadding(new Insets(18, 24, 28, 24));

                content.getChildren().addAll(
                                buildPageTitleRow(),
                                buildHeroCard(),
                                buildSuggestionsSection(),
                                buildQuickInfoSection(),
                                buildChatSection());

                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent;");
                return scrollPane;
        }

        // ---- Page title row ----
        private VBox buildPageTitleRow() {
                Label sparkle = new Label("\u2726");
                sparkle.setStyle("-fx-font-size: 20px; -fx-text-fill: " + PRIMARY + ";");

                Label title = new Label("AI Assistant");
                title.setStyle("-fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 24px; -fx-font-weight: bold;");

                HBox titleRow = new HBox(8, sparkle, title);
                titleRow.setAlignment(Pos.CENTER_LEFT);

                Label subtitle = new Label(
                                "Your smart village assistant. Ask anything about schemes, projects, documents, payments and more.");
                subtitle.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 12px;");

                return new VBox(2, titleRow, subtitle);
        }

        // =================================================================
        // HERO GREETING CARD
        // =================================================================
        private HBox buildHeroCard() {
                Label robotIcon = new Label("\uD83E\uDD16");
                robotIcon.setStyle("-fx-font-size: 46px;");
                StackPane robotCircle = new StackPane(robotIcon);
                robotCircle.setPrefSize(110, 110);
                robotCircle.setMaxSize(110, 110);
                robotCircle.setStyle("-fx-background-color: white; -fx-background-radius: 20; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 20; -fx-border-width: 1;");

                Label greeting = new Label("Hello, Ramesh Patil! \uD83D\uDC4B");
                greeting.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label body = new Label(
                                "I'm your AI Assistant. I can help you with information about government schemes, project status, bills, certificates and more.");
                body.setWrapText(true);
                body.setMaxWidth(560);
                body.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                Label prompt = new Label("How can I help you today?");
                prompt.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";");

                VBox textBox = new VBox(8, greeting, body, prompt);
                HBox.setHgrow(textBox, Priority.ALWAYS);

                // Simple illustration placeholder standing in for the village
                // scene on the right of the hero card in the reference design.
                Label sceneIcon = new Label("\uD83C\uDFD8");
                sceneIcon.setStyle("-fx-font-size: 60px; -fx-opacity: 0.5;");
                StackPane sceneBox = new StackPane(sceneIcon);
                sceneBox.setPrefWidth(160);
                sceneBox.setMinWidth(160);

                HBox card = new HBox(20, robotCircle, textBox, sceneBox);
                card.setAlignment(Pos.CENTER_LEFT);
                card.setPadding(new Insets(24));
                card.setStyle("-fx-background-color: " + LIGHT_GREEN + "; -fx-background-radius: 14; "
                                + "-fx-border-color: " + ACCENT_GREEN + "; -fx-border-radius: 14; -fx-border-width: 1;");
                return card;
        }

        // =================================================================
        // "TRY ASKING ME SOMETHING" SUGGESTION CHIPS
        // =================================================================
        private VBox buildSuggestionsSection() {
                Label title = new Label("Try asking me something");
                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                // TODO: replace with AssistantService.getSuggestedPrompts(userId)
                HBox row1 = new HBox(14,
                                suggestionChip("\uD83D\uDCC4", LIGHT_GREEN, PRIMARY,
                                                "What government schemes are available for farmers?"),
                                suggestionChip("\uD83D\uDC77", LIGHT_PURPLE, PURPLE,
                                                "Show me the status of village road project"),
                                suggestionChip("\uD83D\uDCCB", LIGHT_YELLOW, WARNING,
                                                "How much budget is allocated for water supply project?"));

                HBox row2 = new HBox(14,
                                suggestionChip("\uD83D\uDCE1", LIGHT_BLUE, SECONDARY,
                                                "How can I get a caste certificate?"),
                                suggestionChip("\uD83D\uDCB0", LIGHT_RED, ERROR,
                                                "How can I pay my electricity bill?"),
                                suggestionChip("\uD83D\uDC65", LIGHT_GREEN, PRIMARY,
                                                "When is the next Gram Sabha meeting?"));

                row1.setFillHeight(true);
                row2.setFillHeight(true);

                return new VBox(14, title, row1, row2);
        }

        /** One tappable suggestion chip: icon chip + question text. */
        private HBox suggestionChip(String icon, String iconBg, String iconColor, String question) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-text-fill: " + iconColor + "; -fx-font-size: 15px;");
                StackPane iconCircle = new StackPane(iconLabel);
                iconCircle.setPrefSize(36, 36);
                iconCircle.setMaxSize(36, 36);
                iconCircle.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 10;");

                Label questionLabel = new Label(question);
                questionLabel.setWrapText(true);
                questionLabel.setStyle(
                                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
                HBox.setHgrow(questionLabel, Priority.ALWAYS);

                HBox chip = new HBox(12, iconCircle, questionLabel);
                chip.setAlignment(Pos.CENTER_LEFT);
                chip.setPadding(new Insets(14));
                chip.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-cursor: hand; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                HBox.setHgrow(chip, Priority.ALWAYS);
                // TODO: wire onMouseClicked to send `question` into the chat below

                return chip;
        }

        // =================================================================
        // QUICK INFORMATION (4 stat cards)
        // =================================================================
        private VBox buildQuickInfoSection() {
                Label title = new Label("Quick Information");
                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                // TODO: replace with DashboardService.getQuickInfo(villageId)
                HBox row = new HBox(14,
                                quickInfoCard("\uD83D\uDCC4", LIGHT_GREEN, PRIMARY, "12", "Active Projects",
                                                "View Details"),
                                quickInfoCard("\uD83D\uDC65", LIGHT_BLUE, SECONDARY, "8", "Schemes Available",
                                                "View Schemes"),
                                quickInfoCard("\uD83D\uDCC5", LIGHT_YELLOW, WARNING, "15 Jun 2024", "Next Gram Sabha",
                                                "View Details"),
                                quickInfoCard("\uD83D\uDCDD", LIGHT_PURPLE, PURPLE, "5", "Documents Available",
                                                "View Documents"));

                return new VBox(14, title, row);
        }

        /** One quick-info stat card: icon chip, big value, label, and a "View ..." link. */
        private VBox quickInfoCard(String icon, String iconBg, String iconColor, String value, String label,
                        String linkText) {
                Label iconLabel = new Label(icon);
                iconLabel.setStyle("-fx-text-fill: " + iconColor + "; -fx-font-size: 14px;");
                StackPane iconCircle = new StackPane(iconLabel);
                iconCircle.setPrefSize(38, 38);
                iconCircle.setMaxSize(38, 38);
                iconCircle.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 9;");

                Label valueLabel = new Label(value);
                valueLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label labelLabel = new Label(label);
                labelLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                Label linkLabel = new Label(linkText + "  \u2192");
                linkLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY
                                + "; -fx-cursor: hand;");
                // TODO: wire onMouseClicked to navigate to the relevant page

                VBox card = new VBox(8, iconCircle, valueLabel, labelLabel, linkLabel);
                card.setPadding(new Insets(16));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
                HBox.setHgrow(card, Priority.ALWAYS);
                return card;
        }

        // =================================================================
        // "ASK ME ANYTHING..." CHAT PANEL
        // =================================================================
        private VBox buildChatSection() {
                Label title = new Label("Ask me anything...");
                title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

                // ---- chat history ----
                // TODO: replace with AssistantService.getConversationHistory(userId)
                VBox messages = new VBox(12,
                                assistantMessageBubble(
                                                "You can ask me about government schemes, project updates, certificates, bills, payments, Gram Sabha and any other village related information. I'm here to help!",
                                                "10:30 AM"));

                VBox messagesBox = new VBox(messages);
                messagesBox.setPadding(new Insets(18));
                messagesBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");

                // ---- input row ----
                TextField input = new TextField();
                input.setPromptText("Type your question here...");
                input.setPrefHeight(46);
                input.setStyle(
                                "-fx-background-color: white;"
                                                + "-fx-font-family: " + FONT_FAMILY + ";"
                                                + "-fx-font-size: 12px;"
                                                + "-fx-text-fill: " + TEXT_PRIMARY + ";"
                                                + "-fx-background-radius: 10;"
                                                + "-fx-border-color: " + BORDER + ";"
                                                + "-fx-border-radius: 10;"
                                                + "-fx-border-width: 1;"
                                                + "-fx-padding: 0 14 0 14;");
                HBox.setHgrow(input, Priority.ALWAYS);

                Label micIcon = new Label("\uD83C\uDFA4");
                micIcon.setStyle("-fx-font-size: 15px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                StackPane micButton = new StackPane(micIcon);
                micButton.setPrefSize(46, 46);
                micButton.setMaxSize(46, 46);
                micButton.setStyle("-fx-background-color: white; -fx-background-radius: 10; "
                                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 10; -fx-border-width: 1; "
                                + "-fx-cursor: hand;");
                // TODO: wire mic button to a real speech-to-text input flow

                Label sendIcon = new Label("\u27A4");
                sendIcon.setStyle("-fx-font-size: 15px; -fx-text-fill: white;");
                StackPane sendButton = new StackPane(sendIcon);
                sendButton.setPrefSize(46, 46);
                sendButton.setMaxSize(46, 46);
                sendButton.setStyle("-fx-background-color: " + PRIMARY + "; -fx-background-radius: 10; "
                                + "-fx-cursor: hand;");
                // TODO: wire onMouseClicked to AssistantService.sendMessage(input.getText())

                HBox inputRow = new HBox(10, input, micButton, sendButton);
                inputRow.setAlignment(Pos.CENTER_LEFT);

                Label disclaimer = new Label(
                                "AI responses may not always be 100% accurate. Please verify important information.");
                disclaimer.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");
                HBox disclaimerRow = new HBox(disclaimer);
                disclaimerRow.setAlignment(Pos.CENTER);
                disclaimerRow.setPadding(new Insets(4, 0, 0, 0));

                return new VBox(14, title, messagesBox, inputRow, disclaimerRow);
        }

        /** One assistant chat bubble: robot avatar + message text + timestamp. */
        private HBox assistantMessageBubble(String text, String time) {
                Label robotIcon = new Label("\uD83E\uDD16");
                robotIcon.setStyle("-fx-font-size: 16px;");
                StackPane avatar = new StackPane(robotIcon);
                avatar.setPrefSize(34, 34);
                avatar.setMaxSize(34, 34);
                avatar.setStyle("-fx-background-color: " + TEXT_PRIMARY + "; -fx-background-radius: 17;");

                Label textLabel = new Label(text);
                textLabel.setWrapText(true);
                textLabel.setMaxWidth(600);
                textLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_PRIMARY + ";");

                Label timeLabel = new Label(time);
                timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");

                VBox bubbleContent = new VBox(6, textLabel, timeLabel);
                bubbleContent.setPadding(new Insets(14));
                bubbleContent.setStyle("-fx-background-color: " + LIGHT_GREEN + "; -fx-background-radius: 12;");
                HBox.setHgrow(bubbleContent, Priority.ALWAYS);

                HBox row = new HBox(10, avatar, bubbleContent);
                row.setAlignment(Pos.TOP_LEFT);
                return row;
        }
}
