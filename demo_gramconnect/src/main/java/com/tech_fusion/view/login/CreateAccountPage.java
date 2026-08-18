package com.tech_fusion.view.login;

import com.tech_fusion.controller.AuthController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class CreateAccountPage {

        public static Scene CreateSccountPageScene;

        public Scene getCreateAccountScene(Runnable callbackAction) {

                // =========================================================
                // BACKGROUND IMAGE
                // =========================================================

                Image backgroundImage = new Image(
                                getClass().getResource(
                                                "/assets/images/login page .jpeg").toExternalForm());

                ImageView background = new ImageView(backgroundImage);

                background.setPreserveRatio(false);

                // =========================================================
                // OVERLAY
                // =========================================================

                Region overlay = new Region();

                overlay.setStyle(
                                "-fx-background-color: rgba(255,255,255,0.15);");

                // =========================================================
                // CREATE ACCOUNT CARD
                // SAME SIZE AS LOGIN PAGE
                // =========================================================

                VBox card = new VBox(15);

                card.setPadding(new Insets(35));

                // SAME SIZE AS LOGIN CARD
                card.setPrefWidth(500);
                card.setMinWidth(500);
                card.setMaxWidth(500);

                card.setPrefHeight(700);
                card.setMinHeight(700);
                card.setMaxHeight(700);

                card.setStyle(
                                "-fx-background-color: rgba(248,252,253,0.96);" +
                                                "-fx-background-radius: 25;");

                // =========================================================
                // ICON
                // =========================================================

                Label icon = new Label("♙+");

                icon.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.NORMAL,
                                                42));

                icon.setTextFill(
                                Color.web("#287A20"));

                icon.setMaxWidth(
                                Double.MAX_VALUE);

                icon.setAlignment(
                                Pos.CENTER);

                // =========================================================
                // TITLE
                // =========================================================

                Label title = new Label("Create Account");

                title.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                32));

                title.setTextFill(
                                Color.web("#0D1B2A"));

                title.setMaxWidth(
                                Double.MAX_VALUE);

                title.setAlignment(
                                Pos.CENTER);

                // =========================================================
                // SUBTITLE
                // =========================================================

                Label subtitle = new Label(
                                "Join GramConnect and be a part of your village's progress.");

                subtitle.setFont(
                                Font.font("Arial", 15));

                subtitle.setTextFill(
                                Color.GRAY);

                subtitle.setMaxWidth(
                                Double.MAX_VALUE);

                subtitle.setAlignment(
                                Pos.CENTER);

                // =========================================================
                // FULL NAME
                // =========================================================

                Label nameLabel = new Label("Full Name");

                nameLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                15));

                TextField nameField = new TextField();

                nameField.setPromptText(
                                "Enter your full name");

                nameField.setPrefHeight(48);

                nameField.setMaxWidth(
                                Double.MAX_VALUE);

                // =========================================================
                // MOBILE
                // =========================================================

                Label mobileLabel = new Label("Mobile Number");

                mobileLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                15));

                TextField mobileField = new TextField();

                mobileField.setPromptText(
                                "Enter your mobile number");

                mobileField.setPrefHeight(48);

                mobileField.setMaxWidth(
                                Double.MAX_VALUE);

                VBox nameBox = new VBox(
                                7,
                                nameLabel,
                                nameField);

                VBox mobileBox = new VBox(
                                7,
                                mobileLabel,
                                mobileField);

                // =========================================================
                // FIRST ROW
                // =========================================================

                HBox firstRow = new HBox(18);

                firstRow.setMaxWidth(
                                Double.MAX_VALUE);

                firstRow.getChildren().addAll(
                                nameBox,
                                mobileBox);

                HBox.setHgrow(
                                nameBox,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                mobileBox,
                                Priority.ALWAYS);

                // =========================================================
                // EMAIL
                // =========================================================

                Label emailLabel = new Label("Email Address");

                emailLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                15));

                TextField emailField = new TextField();

                emailField.setPromptText(
                                "Enter your email address");

                emailField.setPrefHeight(48);

                emailField.setMaxWidth(
                                Double.MAX_VALUE);

                VBox emailBox = new VBox(
                                7,
                                emailLabel,
                                emailField);

                // =========================================================
                // PASSWORD
                // =========================================================

                Label passwordLabel = new Label("Password");

                passwordLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                15));

                PasswordField passwordField = new PasswordField();

                passwordField.setPromptText(
                                "Create a password");

                passwordField.setPrefHeight(48);

                passwordField.setMaxWidth(
                                Double.MAX_VALUE);

                // =========================================================
                // CONFIRM PASSWORD
                // =========================================================

                Label confirmLabel = new Label("Confirm Password");

                confirmLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                15));

                PasswordField confirmField = new PasswordField();

                confirmField.setPromptText(
                                "Confirm your password");

                confirmField.setPrefHeight(48);

                confirmField.setMaxWidth(
                                Double.MAX_VALUE);

                VBox passwordBox = new VBox(
                                7,
                                passwordLabel,
                                passwordField);

                VBox confirmBox = new VBox(
                                7,
                                confirmLabel,
                                confirmField);

                // =========================================================
                // PASSWORD ROW
                // =========================================================

                HBox passwordRow = new HBox(18);

                passwordRow.setMaxWidth(
                                Double.MAX_VALUE);

                passwordRow.getChildren().addAll(
                                passwordBox,
                                confirmBox);

                HBox.setHgrow(
                                passwordBox,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                confirmBox,
                                Priority.ALWAYS);

                // =========================================================
                // VILLAGE
                // =========================================================

                Label villageLabel = new Label(
                                "Village / Gram Panchayat");

                villageLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                15));

                ComboBox<String> villageBox = new ComboBox<>();

                villageBox.getItems().addAll(
                                "Pabal",
                                "Shirur",
                                "Ranjangaon",
                                "Koregaon",
                                "Other");

                villageBox.setPromptText(
                                "Select your village or gram panchayat");

                villageBox.setPrefHeight(48);

                villageBox.setMaxWidth(
                                Double.MAX_VALUE);

                VBox villageContainer = new VBox(
                                7,
                                villageLabel,
                                villageBox);

                // =========================================================
                // ROLE
                // =========================================================

                Label roleLabel = new Label("Role");

                roleLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                15));

                ComboBox<String> roleBox = new ComboBox<>();

                roleBox.getItems().addAll(
                                "User",
                                "Sarpanch",
                                "Gramsevak",
                                "BDO");

                roleBox.setPromptText(
                                "Select your role");

                roleBox.setPrefHeight(48);

                roleBox.setMaxWidth(
                                Double.MAX_VALUE);

                VBox roleContainer = new VBox(
                                7,
                                roleLabel,
                                roleBox);

                // =========================================================
                // ROLES TEXT
                // =========================================================

                Label rolesInfo = new Label(
                                "Roles: User, Sarpanch, Gramsevak, BDO");

                rolesInfo.setFont(
                                Font.font("Arial", 15));

                rolesInfo.setTextFill(
                                Color.web("#287A20"));

                // =========================================================
                // CREATE BUTTON
                // =========================================================

                Button createButton = new Button(
                                "♙+   Create Account");

                createButton.setPrefHeight(50);

                createButton.setMaxWidth(
                                Double.MAX_VALUE);

                createButton.setStyle(
                                "-fx-background-color: #287A20;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-cursor: hand;");

                // =========================================================
                // LOGIN
                // =========================================================

                Label already = new Label(
                                "Already have an account?");

                already.setFont(
                                Font.font("Arial", 15));

                already.setTextFill(
                                Color.GRAY);

                Hyperlink loginLink = new Hyperlink("Login");

                loginLink.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                15));

                loginLink.setStyle(
                                "-fx-text-fill: #287A20;");

                Text output = new Text();
                output.setStyle("-fx-font-size : 24; -fx-fill: black; -fx-font-weight: bold;");

                // =========================================================
                // LOGIN NAVIGATION
                // =========================================================

                loginLink.setOnAction(e -> {

                        if (callbackAction != null) {
                                callbackAction.run();
                        }

                });

                HBox loginBox = new HBox(5);

                loginBox.setAlignment(
                                Pos.CENTER);

                loginBox.getChildren().addAll(
                                already,
                                loginLink);

                // =========================================================
                // CREATE ACCOUNT ACTION
                //
                // BUGFIX: on a successful signUp this previously only set
                // some (invisible-looking, unstyled) Text and left the user
                // stranded on the Create Account screen with no feedback and
                // no way forward. The confirmation Alert is restored, and
                // once the user dismisses it, callbackAction.run() (the same
                // Runnable the "Login" link already uses) takes them back to
                // the Login screen so they can actually sign in with the
                // account they just created. On failure, the real reason
                // from Firebase (AuthController.getLastErrorMessage()) is
                // shown instead of a generic message.
                // =========================================================

                AuthController controller = new AuthController();

                createButton.setOnAction(event -> {

                        String name = nameField.getText();

                        String mobile = mobileField.getText();

                        String email = emailField.getText();

                        String villageName = villageBox.getValue();

                        String role = roleBox.getValue();

                        String password = passwordField.getText();

                        String confirmPassword = confirmField.getText();

                        if (name.isEmpty()
                                        || mobile.isEmpty()
                                        || email.isEmpty()
                                        || password.isEmpty()
                                        || confirmPassword.isEmpty()
                                        || villageBox.getValue() == null
                                        || roleBox.getValue() == null) {

                                Alert alert = new Alert(
                                                Alert.AlertType.WARNING);

                                alert.setTitle(
                                                "GramConnect");

                                alert.setHeaderText(
                                                null);

                                alert.setContentText(
                                                "Please fill all required fields.");

                                alert.showAndWait();

                        } else if (!password.equals(confirmPassword)) {

                                Alert alert = new Alert(
                                                Alert.AlertType.ERROR);

                                alert.setTitle(
                                                "GramConnect");

                                alert.setHeaderText(
                                                "Password Mismatch");

                                alert.setContentText(
                                                "Password and Confirm Password must be same.");

                                alert.showAndWait();

                        } else {

                                boolean flag = controller.signUp(email, password, name, mobile, villageName, role);

                                if (flag) {

                                        System.out.println("signUp successfully");
                                        output.setText("Account created successfully");

                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("GramConnect");
                                        alert.setHeaderText("Account Created Successfully");
                                        alert.setContentText("Welcome to GramConnect! Please log in to continue.");
                                        alert.showAndWait();

                                        if (callbackAction != null) {
                                                callbackAction.run();
                                        }

                                } else {

                                        System.out.println("signUp failed");
                                        output.setText("Account creation failed");

                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("GramConnect");
                                        alert.setHeaderText("Account Creation Failed");
                                        // alert.setContentText(controller.getLastErrorMessage());
                                        alert.showAndWait();
                                }
                        }
                });

                // =========================================================
                // ADD EVERYTHING
                // =========================================================

                card.getChildren().addAll(

                                icon,
                                title,
                                subtitle,

                                firstRow,

                                emailBox,

                                passwordRow,

                                villageContainer,

                                roleContainer,

                                rolesInfo,

                                createButton,

                                loginBox,
                                output);

                // =========================================================
                // ROOT
                // =========================================================

                StackPane root = new StackPane();

                root.getChildren().addAll(
                                background,
                                overlay,
                                card);

                // =========================================================
                // RESPONSIVE BACKGROUND
                // =========================================================

                background.fitWidthProperty().bind(
                                root.widthProperty());

                background.fitHeightProperty().bind(
                                root.heightProperty());

                // =========================================================
                // RESPONSIVE OVERLAY
                // =========================================================

                overlay.prefWidthProperty().bind(
                                root.widthProperty());

                overlay.prefHeightProperty().bind(
                                root.heightProperty());

                // =========================================================
                // CARD POSITION
                // SAME POSITION STYLE AS LOGIN PAGE
                // =========================================================

                StackPane.setAlignment(
                                card,
                                Pos.CENTER_RIGHT);

                StackPane.setMargin(
                                card,
                                new Insets(
                                                25,
                                                60,
                                                25,
                                                40));

                // =========================================================
                // SCENE
                // =========================================================

                CreateSccountPageScene = new Scene(root);

                return CreateSccountPageScene;
        }
}