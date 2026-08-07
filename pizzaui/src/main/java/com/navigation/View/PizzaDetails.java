package com.navigation.View;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PizzaDetails {
    private Scene pizzaDetailsScene;

    public Scene getPizzaDetailsScene(Runnable backToHomeAction) {
        
        Text t2 = new Text("Pizza Details");
        t2.setStyle("-fx-font-size: 35px; -fx-font-weight: bold; -fx-text-fill: black");

        Text t3 = new Text("Name : Margherita Pizza");
        t3.setStyle("-fx-font-size: 25px; -fx-text-fill: black");

        Text t4 = new Text("Price : ₹ 150");
        t4.setStyle("-fx-font-size: 25px; -fx-text-fill: black");

        Button homeBackBtn = new Button("Back to Home");
        homeBackBtn.setStyle("-fx-font-size: 23px; -fx-background-color: #640cb6; -fx-text-fill: white;-fx-background-radius:20px;-fx-border-radius:20px");

        Button toppingsBtn = new Button("View Toppings");
        toppingsBtn.setStyle("-fx-font-size: 23px; -fx-background-color: #f9763d; -fx-text-fill: white;-fx-background-radius:20px;-fx-border-radius:20px");

        toppingsBtn.setOnAction(e -> {
            System.out.println("View Toppings button clicked");
            ToppingsDetails toppingsDetails = new ToppingsDetails();
            HomePage.myStage.setScene(toppingsDetails.getToppingsDetailsScene(backToHomeAction));
        });

        homeBackBtn.setOnAction(e -> {
            System.out.println("Back to Home button clicked");
            backToHomeAction.run();
        });   
        
        VBox vB2 = new VBox(50,t2,t3,t4,toppingsBtn,homeBackBtn);
        vB2.setStyle("-fx-background-color: #eecb86ae;-fx-alignment: center");

        VBox.setMargin(t3,new Insets(0,0,-20,0));
        
        pizzaDetailsScene = new Scene(vB2, 1500, 750);
        return pizzaDetailsScene;
    }
}
