package com.navigation.View;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ToppingsDetails {
    private Scene toppingsDetailsScene;

    public Scene getToppingsDetailsScene(Runnable backToHomeAction) {
        Text t5 = new Text("Pizza Toppings");
        t5.setStyle("-fx-font-size: 35px; -fx-font-weight: bold; -fx-text-fill: black");

        Text t6 = new Text("1. Extra Cheese");
        t6.setStyle("-fx-font-size: 25px; -fx-text-fill: black");

        Text t7 = new Text("2. Mushrooms");
        t7.setStyle("-fx-font-size: 25px; -fx-text-fill: black");

        Text t8 = new Text("3. Paneer");
        t8.setStyle("-fx-font-size: 25px; -fx-text-fill: black");

        Text t9 = new Text("4. Onions");
        t9.setStyle("-fx-font-size: 25px; -fx-text-fill: black");

        Text t10 = new Text("5. Capsicum");
        t10.setStyle("-fx-font-size: 25px; -fx-text-fill: black");

        Text t11 = new Text("6. Corn");
        t11.setStyle("-fx-font-size: 25px; -fx-text-fill: black");

        Button backBtn = new Button("Back to Pizza Details");
        backBtn.setStyle("-fx-font-size: 23px; -fx-background-color: #f9763d; -fx-text-fill: white;-fx-background-radius:20px;-fx-border-radius:20px");

        backBtn.setOnAction(e -> {
            System.out.println("Back to Pizza Details button clicked");
            HomePage.myStage.setScene(new PizzaDetails().getPizzaDetailsScene(backToHomeAction));
        });

        VBox vB3 = new VBox(50,t5,t6,t7,t8,t9,t10,t11,backBtn);
        vB3.setStyle("-fx-background-color: #eecb86ae;-fx-alignment: center");
        VBox.setMargin(t6,new Insets(0, 0, -20, 0));
        VBox.setMargin(t7,new Insets(0, 0, -20, 0));
        VBox.setMargin(t8,new Insets(0, 0, -20, 0));
        VBox.setMargin(t9,new Insets(0, 0, -20, 0));
        VBox.setMargin(t10,new Insets(0, 0, -20, 0));

        toppingsDetailsScene = new Scene(vB3, 1500, 750);
        return toppingsDetailsScene;
    }
    
}
