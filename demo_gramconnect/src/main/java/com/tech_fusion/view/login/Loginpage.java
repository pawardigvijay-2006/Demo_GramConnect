package com.tech_fusion.view.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class Loginpage  {

        public static Scene LoginPageScene;
   public Scene getLoginPageScene(){

        //BACKGROUND IMAGE 
        Image backgroundImage = new Image(
                getClass().getResource("/assets/images/login page .jpeg").toExternalForm()
        );

        ImageView background = new ImageView(backgroundImage);

        background.setPreserveRatio(false);
        background.setFitWidth(1200);
        background.setFitHeight(750);

        
        // Slight white transparent layer
        Region overlay = new Region();
        overlay.setStyle(
                "-fx-background-color: rgba(255,255,255,0.18);"
        );

        // LOGIN CARD 
        VBox loginCard = new VBox(20);
        loginCard.setPadding(new Insets(30));
        loginCard.setPrefWidth(600);
        loginCard.setMaxWidth(400);
        loginCard.setMaxHeight(700);
        loginCard.setTranslateX(35);

        loginCard.setStyle(
                "-fx-background-color: #fdfefe(255,255,255,0.96);" +
                "-fx-background-radius: 25;"
        );

        // TITLE
        Label welcome = new Label("Welcome Back!");

        welcome.setFont(
                Font.font("Arial", FontWeight.BOLD, 34)
        );
        welcome.setTextFill(
                Color.web("#123B59")
        );

        welcome.setMaxWidth(Double.MAX_VALUE);
        welcome.setAlignment(Pos.CENTER);

        // Green line
        Label greenLine = new Label("━━━━━━");

        greenLine.setTextFill(
                Color.web("#185905")
        );

        greenLine.setFont(
                Font.font(18)
        );

        greenLine.setMaxWidth(Double.MAX_VALUE);
        greenLine.setAlignment(Pos.CENTER);

        // Subtitle
        Label subtitle = new Label(
                "Login to continue to GramConnect"
        );

        subtitle.setFont(
                Font.font("Arial", 16)
        );

        subtitle.setTextFill(Color.GRAY);

        subtitle.setMaxWidth(Double.MAX_VALUE);
        subtitle.setAlignment(Pos.CENTER);


        //  USERNAME 
        Label usernameLabel =
                new Label("Username / Mobile Number *");

        usernameLabel.setFont(
                Font.font("Arial", 16)
        );

        TextField username = new TextField();

        username.setPromptText(
                "Enter mobile number or username"
        );

        username.setPrefHeight(50);

        username.setMaxWidth(Double.MAX_VALUE);

        // PASSWORD 

        Label passwordLabel =
                new Label("Password *");

        passwordLabel.setFont(
                Font.font("Arial", 16)
        );

        PasswordField password = new PasswordField();

        password.setPromptText(
                "Enter your password"
        );

        password.setPrefHeight(50);

        password.setMaxWidth(Double.MAX_VALUE);

        //  FORGOT PASSWORD 
        Hyperlink forgotPassword =
                new Hyperlink("Forgot Password?");

        forgotPassword.setStyle(
                "-fx-text-fill: #1768B3;" +
                "-fx-font-size: 14px;"
        );

        HBox forgotBox = new HBox();

        forgotBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        forgotBox.getChildren().add(
                forgotPassword
        );

        // LOGIN BUTTON 
        Button loginButton =
                new Button("↪   LOGIN");

        loginButton.setPrefHeight(55);

        loginButton.setMaxWidth(
                Double.MAX_VALUE
        );
        
        loginButton.setStyle(
                "-fx-background-color: #247c0c;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        //divider
        HBox divider = new HBox(10);

        divider.setAlignment(Pos.CENTER);

        Region line1 = new Region();
        Region line2 = new Region();

        line1.setPrefHeight(1);
        line2.setPrefHeight(1);

        line1.setStyle(
                "-fx-background-color: #DDDDDD;"
        );

        line2.setStyle(
                "-fx-background-color: #DDDDDD;"
        );

        HBox.setHgrow(
                line1,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                line2,
                Priority.ALWAYS
        );

        Label orText =
                new Label("or continue with");

        orText.setTextFill(Color.GRAY);

        orText.setFont(
                Font.font(15)
        );

        divider.getChildren().addAll(
                line1,
                orText,
                line2
        );

        //  CREATE ACCOUNT 
        Label accountText =
                new Label("Don't have an account?");

        accountText.setFont(
                Font.font(15)
        );

        accountText.setTextFill(Color.GRAY);

        Hyperlink createAccount =
                new Hyperlink("Create Account");
         
         createAccount.setOnAction(e->{
            CreateAccountPage createAccountPage = new CreateAccountPage();
          try {
                SplashScreen.HomepageStage.setScene(createAccountPage.getCreateAccountScene());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
              
        createAccount.setStyle(
                "-fx-text-fill: #1768B3;" +
                "-fx-font-size: 15px;"
        );

        HBox createBox =
                new HBox(5);

        createBox.setAlignment(
                Pos.CENTER
        );

        createBox.getChildren().addAll(
                accountText,
                createAccount
        );

        //  BUTTON ACTION 
        loginButton.setOnAction(event -> {

            
            String user = username.getText();
            String pass = password.getText();

            if (
                    user.isEmpty() ||
                    pass.isEmpty()) {

                Alert alert = new Alert(
                        Alert.AlertType.WARNING
                );

                alert.setTitle("GramConnect");
                alert.setHeaderText(null);
                alert.setContentText(
                        "Please fill all required fields."
                );

                alert.showAndWait();

            } else {

                Alert alert = new Alert(
                        Alert.AlertType.INFORMATION
                );

                alert.setTitle("GramConnect");
                alert.setHeaderText("Login Successful");
                alert.setContentText(
                        "Welcome to GramConnect!"
                );

                alert.showAndWait();
            }
        });

        //  ADD EVERYTHING 

        loginCard.getChildren().addAll(

                welcome,
                greenLine,
                subtitle,

                new Region(),

                usernameLabel,
                username,

                passwordLabel,
                password,

                forgotBox,

                loginButton,

                divider,

                createBox
        );

        //  ROOT 

        StackPane root = new StackPane();

        root.getChildren().addAll(
                background,
                overlay,
                loginCard
        );

        //
        StackPane.setAlignment(
                loginCard,
                Pos.CENTER_RIGHT
        );

        StackPane.setMargin(
                loginCard,
                new Insets(40, 60, 40, 40)
        );

        //  SCENE 

        Scene scene =
                new Scene(root, 1200, 750);
        return scene;
}
    }
   

    

