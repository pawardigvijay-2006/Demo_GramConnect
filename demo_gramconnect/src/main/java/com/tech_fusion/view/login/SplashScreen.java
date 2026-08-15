package com.tech_fusion.view.login;


    

import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreen extends Application {
    public static Stage HomepageStage ;
    private Scene HomepageScene;

    @Override
    public void start(Stage stage) {
        HomepageStage = stage;

        // Splash image
        Image image = new Image(
            getClass().getResource("/assets/images/WhatsApp Image 2026-08-12 at 10.45.49 AM.jpeg").toExternalForm()
        );

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1200);
        imageView.setFitHeight(750);
        imageView.setPreserveRatio(false);

        // Loader
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(350);
        progressBar.setStyle("-fx-accent: #0d6a02;");
        progressBar.setTranslateY(-42);

        VBox loader = new VBox(10);
        loader.setAlignment(Pos.BOTTOM_CENTER);
        loader.getChildren().add(progressBar);
        
        StackPane root = new StackPane();
        root.getChildren().addAll(imageView, loader);

        HomepageScene = new Scene(root, 1200, 750);

        HomepageStage.setScene(HomepageScene);
        HomepageStage.setTitle("GramConnect");
        HomepageStage.show();

        // Loader fills from 0% to 100% in exactly 3 seconds
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                new javafx.animation.KeyValue(
                    progressBar.progressProperty(), 0
                )
            ),
            new KeyFrame(Duration.seconds(3),
                new javafx.animation.KeyValue(
                    progressBar.progressProperty(), 1
                )
            )
        );

        timeline.play();

        // After exactly 3 seconds → open next screen
        PauseTransition delay = new PauseTransition(Duration.seconds(3));

        delay.setOnFinished(event -> {
            Loginpage loginpage = new Loginpage();
            try {
                HomepageStage.setScene(loginpage.getLoginPageScene());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        delay.play();
    }


}
    

