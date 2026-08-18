package com.tech_fusion.view.login;

import javax.smartcardio.Card;

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
import javafx.stage.Screen;





public class CreateAccountPage {

    public static Scene CreateSccountPageScene;
    
    Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

double screenWidth = screenSize.getWidth();
double screenHeight = screenSize.getHeight();

    public Scene getCreateAccountScene(Runnable callbackAction) {
        

        // =========================================================
        // BACKGROUND IMAGE
        // =========================================================

        Image backgroundImage = new Image(
                getClass().getResource(
                        "/assets/images/login page .jpeg"
                ).toExternalForm()
        );

        ImageView background = new ImageView(backgroundImage);

        background.setPreserveRatio(false);


        // =========================================================
        // OVERLAY
        // =========================================================

        Region overlay = new Region();

        overlay.setStyle(
                "-fx-background-color: rgba(255,255,255,0.15);"
        );


        // =========================================================
        // CREATE ACCOUNT CARD
        // SAME SIZE AS LOGIN PAGE
        // =========================================================

       /*  VBox card = new VBox(15);

        card.setPadding(new Insets(35));

        // SAME SIZE AS LOGIN CARD
        card.setPrefWidth(550);
        card.setMinWidth(550);
        card.setMaxWidth(550);

        card.setPrefHeight(700);
        card.setMinHeight(700);
        card.setMaxHeight(700);*/

        VBox Card = new VBox(15);

        Card.setPadding(
                new Insets(
                        screenHeight * 0.035
                )
        );

        // Card width = approximately 33% of screen width
        Card.setPrefWidth(
                screenWidth * 0.33
        );

        Card.setMaxWidth(
                screenWidth * 0.33
        );

        // Card height = approximately 88% of screen height
        Card.setPrefHeight(
                screenHeight * 0.88
        );

        Card.setMaxHeight(
                screenHeight * 0.88
        );

        Card.setTranslateX(
                screenWidth * 0.022
        );
       

        Card.setStyle(
                "-fx-background-color: rgba(248,252,253,0.96);" +
                "-fx-background-radius: 25;"
        );


        // =========================================================
        // ICON
        // =========================================================

        Label icon = new Label("♙+");

        icon.setFont(
                Font.font(
                        "Arial",
                        FontWeight.NORMAL,
                        42
                )
        );

        icon.setTextFill(
                Color.web("#287A20")
        );

        icon.setMaxWidth(
                Double.MAX_VALUE
        );

        icon.setAlignment(
                Pos.CENTER
        );


        // =========================================================
        // TITLE
        // =========================================================

        Label title =
                new Label("Create Account");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        32
                )
        );

        title.setTextFill(
                Color.web("#0D1B2A")
        );

        title.setMaxWidth(
                Double.MAX_VALUE
        );

        title.setAlignment(
                Pos.CENTER
        );


        // =========================================================
        // SUBTITLE
        // =========================================================

        Label subtitle =
                new Label(
                        "Join GramConnect and be a part of your village's progress."
                );

        subtitle.setFont(
                Font.font("Arial", 15)
        );

        subtitle.setTextFill(
                Color.GRAY
        );

        subtitle.setMaxWidth(
                Double.MAX_VALUE
        );

        subtitle.setAlignment(
                Pos.CENTER
        );


        // =========================================================
        // FULL NAME
        // =========================================================

        Label nameLabel =
                new Label("Full Name");

        nameLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Enter your full name"
        );

        nameField.setPrefHeight(48);

        nameField.setMaxWidth(
                Double.MAX_VALUE
        );


        // =========================================================
        // MOBILE
        // =========================================================

        Label mobileLabel =
                new Label("Mobile Number");

        mobileLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        TextField mobileField =
                new TextField();

        mobileField.setPromptText(
                "Enter your mobile number"
        );

        mobileField.setPrefHeight(48);

        mobileField.setMaxWidth(
                Double.MAX_VALUE
        );


        VBox nameBox =
                new VBox(
                        7,
                        nameLabel,
                        nameField
                );

        VBox mobileBox =
                new VBox(
                        7,
                        mobileLabel,
                        mobileField
                );


        // =========================================================
        // FIRST ROW
        // =========================================================

        HBox firstRow =
                new HBox(18);

        firstRow.setMaxWidth(
                Double.MAX_VALUE
        );

        firstRow.getChildren().addAll(
                nameBox,
                mobileBox
        );

        HBox.setHgrow(
                nameBox,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                mobileBox,
                Priority.ALWAYS
        );


        // =========================================================
        // EMAIL
        // =========================================================

        Label emailLabel =
                new Label("Email Address");

        emailLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        TextField emailField =
                new TextField();

        emailField.setPromptText(
                "Enter your email address"
        );

        emailField.setPrefHeight(48);

        emailField.setMaxWidth(
                Double.MAX_VALUE
        );


        VBox emailBox =
                new VBox(
                        7,
                        emailLabel,
                        emailField
                );


        // =========================================================
        // PASSWORD
        // =========================================================

        Label passwordLabel =
                new Label("Password");

        passwordLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        PasswordField passwordField =
                new PasswordField();

        passwordField.setPromptText(
                "Create a password"
        );

        passwordField.setPrefHeight(48);

        passwordField.setMaxWidth(
                Double.MAX_VALUE
        );


        // =========================================================
        // CONFIRM PASSWORD
        // =========================================================

        Label confirmLabel =
                new Label("Confirm Password");

        confirmLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        PasswordField confirmField =
                new PasswordField();

        confirmField.setPromptText(
                "Confirm your password"
        );

        confirmField.setPrefHeight(48);

        confirmField.setMaxWidth(
                Double.MAX_VALUE
        );


        VBox passwordBox =
                new VBox(
                        7,
                        passwordLabel,
                        passwordField
                );

        VBox confirmBox =
                new VBox(
                        7,
                        confirmLabel,
                        confirmField
                );


        // =========================================================
        // PASSWORD ROW
        // =========================================================

        HBox passwordRow =
                new HBox(18);

        passwordRow.setMaxWidth(
                Double.MAX_VALUE
        );

        passwordRow.getChildren().addAll(
                passwordBox,
                confirmBox
        );

        HBox.setHgrow(
                passwordBox,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                confirmBox,
                Priority.ALWAYS
        );


        // =========================================================
        // VILLAGE
        // =========================================================

        Label villageLabel =
                new Label(
                        "Village / Gram Panchayat"
                );

        villageLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        ComboBox<String> villageBox =
                new ComboBox<>();

        villageBox.getItems().addAll(
                "Pabal",
                "Shirur",
                "Ranjangaon",
                "Koregaon",
                "Other"
        );

        villageBox.setPromptText(
                "Select your village or gram panchayat"
        );

        villageBox.setPrefHeight(48);

        villageBox.setMaxWidth(
                Double.MAX_VALUE
        );


        VBox villageContainer =
                new VBox(
                        7,
                        villageLabel,
                        villageBox
                );


        // =========================================================
        // ROLE
        // =========================================================

        Label roleLabel =
                new Label("Role");

        roleLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        ComboBox<String> roleBox =
                new ComboBox<>();

        roleBox.getItems().addAll(
                "User",
                "Sarpanch",
                "Gramsevak",
                "BDO"
        );

        roleBox.setPromptText(
                "Select your role"
        );

        roleBox.setPrefHeight(48);

        roleBox.setMaxWidth(
                Double.MAX_VALUE
        );


        VBox roleContainer =
                new VBox(
                        7,
                        roleLabel,
                        roleBox
                );


        // =========================================================
        // ROLES TEXT
        // =========================================================

        Label rolesInfo =
                new Label(
                        "Roles: User, Sarpanch, Gramsevak, BDO"
                );

        rolesInfo.setFont(
                Font.font("Arial", 15)
        );

        rolesInfo.setTextFill(
                Color.web("#287A20")
        );


        // =========================================================
        // CREATE BUTTON
        // =========================================================

        Button createButton =
                new Button(
                        "♙+   Create Account"
                );

        createButton.setPrefHeight(50);

        createButton.setMaxWidth(
                Double.MAX_VALUE
        );

        createButton.setStyle(
                "-fx-background-color: #287A20;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 7;" +
                "-fx-cursor: hand;"
        );


        // =========================================================
        // LOGIN
        // =========================================================

        Label already =
                new Label(
                        "Already have an account?"
                );

        already.setFont(
                Font.font("Arial", 15)
        );

        already.setTextFill(
                Color.GRAY
        );


        Hyperlink loginLink =
                new Hyperlink("Login");

        loginLink.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        loginLink.setStyle(
                "-fx-text-fill: #287A20;"
        );


        // =========================================================
        // LOGIN NAVIGATION
        // =========================================================

        loginLink.setOnAction(e -> {

            if (callbackAction != null) {
                callbackAction.run();
            }

        });


        HBox loginBox =
                new HBox(5);

        loginBox.setAlignment(
                Pos.CENTER
        );

        loginBox.getChildren().addAll(
                already,
                loginLink
        );


        // =========================================================
        // CREATE ACCOUNT ACTION
        // =========================================================

        createButton.setOnAction(event -> {

            String name =
                    nameField.getText();

            String mobile =
                    mobileField.getText();

            String email =
                    emailField.getText();

            String password =
                    passwordField.getText();

            String confirmPassword =
                    confirmField.getText();


            if (
                    name.isEmpty()
                    || mobile.isEmpty()
                    || email.isEmpty()
                    || password.isEmpty()
                    || confirmPassword.isEmpty()
                    || villageBox.getValue() == null
                    || roleBox.getValue() == null
            ) {

                Alert alert =
                        new Alert(
                                Alert.AlertType.WARNING
                        );

                alert.setTitle(
                        "GramConnect"
                );

                alert.setHeaderText(
                        null
                );

                alert.setContentText(
                        "Please fill all required fields."
                );

                alert.showAndWait();


            } else if (
                    !password.equals(confirmPassword)
            ) {

                Alert alert =
                        new Alert(
                                Alert.AlertType.ERROR
                        );

                alert.setTitle(
                        "GramConnect"
                );

                alert.setHeaderText(
                        "Password Mismatch"
                );

                alert.setContentText(
                        "Password and Confirm Password must be same."
                );

                alert.showAndWait();


            } else {

                Alert alert =
                        new Alert(
                                Alert.AlertType.INFORMATION
                        );

                alert.setTitle(
                        "GramConnect"
                );

                alert.setHeaderText(
                        "Account Created Successfully"
                );

                alert.setContentText(
                        "Welcome to GramConnect!"
                );

                alert.showAndWait();
            }
        });


        // =========================================================
        // ADD EVERYTHING
        // =========================================================

        Card.getChildren().addAll(

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

                loginBox
        );


        // =========================================================
        // ROOT
        // =========================================================

        StackPane root =
                new StackPane();

        root.getChildren().addAll(
                background,
                overlay,
                Card
        );

        
        // =========================================================
        // SCENE
        // =========================================================

        CreateSccountPageScene =
                new Scene(root,screenSize.getWidth(),
                screenSize.getHeight());



        // =========================================================
        // RESPONSIVE BACKGROUND
        // =========================================================

        background.fitWidthProperty().bind(
                root.widthProperty()
        );

        background.fitHeightProperty().bind(
                root.heightProperty()
        );


        // =========================================================
        // RESPONSIVE OVERLAY
        // =========================================================

        overlay.prefWidthProperty().bind(
                root.widthProperty()
        );

        overlay.prefHeightProperty().bind(
                root.heightProperty()
        );


        // =========================================================
        // CARD POSITION
        // SAME POSITION STYLE AS LOGIN PAGE
        // =========================================================

        StackPane.setAlignment(
                Card,
                Pos.CENTER_RIGHT
        );

        StackPane.setMargin(
                Card,
                new Insets(
                        25,
                        60,
                        25,
                        40
                )
        );


        return CreateSccountPageScene;
    }
}