package com.navigation.View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomePage extends Application {

    public static Stage myStage;
    private Scene homePageScene;

    @Override
    public void start(Stage stage) throws Exception {
        
        Text t1 = new Text("Welcome to the Food App");
        t1.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: black");

        Button pizzaBtn = new Button("Pizza");
        pizzaBtn.setStyle("-fx-font-size: 25px; -fx-background-color: #f9763d; -fx-text-fill: white;-fx-background-radius:20px;-fx-border-radius:20px");

        pizzaBtn.setOnAction(e -> {
            System.out.println("Pizza button clicked");
            PizzaDetails pizzaDetails = new PizzaDetails();
            Runnable backToHomeAction = () -> {
                back();
            };

            myStage.setScene(pizzaDetails.getPizzaDetailsScene(backToHomeAction));
        });

        VBox vB = new VBox(50,t1, pizzaBtn);
        vB.setStyle("-fx-background-color: #eecb86ae;-fx-alignment: center");


        homePageScene = new Scene(vB,1500,750);
        myStage = stage;
        myStage.setScene(homePageScene);
    
        myStage.show();
    }
    
    public void back(){
        myStage.setScene(homePageScene);
    }
    
}
