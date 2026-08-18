package com.tech_fusion.view.login;


    

import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreen extends Application {

    public static Stage HomepageStage;
    private Scene HomepageScene;

    @Override
    public void start(Stage stage) {

        HomepageStage = stage;

        // =========================================================
        // SPLASH IMAGE
        // =========================================================

        Image image = new Image(
            getClass().getResource(
                "/assets/images/WhatsApp Image 2026-08-12 at 10.45.49 AM.jpeg"
            ).toExternalForm()
        );

        ImageView imageView = new ImageView(image);

        // Image automatically adjusts to screen size
        imageView.setPreserveRatio(false);

        // =========================================================
        // PROGRESS BAR
        // =========================================================

        // Screen size
Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

double screenWidth = screenSize.getWidth();
double screenHeight = screenSize.getHeight();


// ===============================
// PROGRESS BAR
// ===============================

ProgressBar progressBar = new ProgressBar(0);

// Width according to screen
progressBar.setPrefWidth(screenWidth * 0.29);

progressBar.setStyle(
        "-fx-accent: #0d6a02;"
);


// ===============================
// LOADER POSITION
// ===============================

VBox loader = new VBox();

loader.setAlignment(Pos.CENTER);

// Position according to screen height
loader.setTranslateY(screenHeight * 0.42);

loader.getChildren().add(progressBar);
        // =========================================================
        // LOADER CONTAINER
        // =========================================================

        /*VBox loader = new VBox(10);

        loader.setAlignment(
            Pos.CENTER
        );

        loader.getChildren().add(
            progressBar
        );

        // Position loader according to screen height
        loader.translateYProperty().bind(
            stage.heightProperty().multiply(-0.06)
        );*/

        // =========================================================
        // ROOT
        // =========================================================

        StackPane root = new StackPane();

        root.getChildren().addAll(
            imageView,
            loader
        );

        // =========================================================
        // IMAGE RESPONSIVE BINDING
        // =========================================================

        imageView.fitWidthProperty().bind(
            root.widthProperty()
        );

        imageView.fitHeightProperty().bind(
            root.heightProperty()
        );

        // =========================================================
        // SCENE
        // =========================================================

        HomepageScene = new Scene(
            root,
            1200,
            750
        );

        HomepageStage.setScene(
            HomepageScene
        );

        HomepageStage.setTitle(
            "GramConnect"
        );

        // If you want the application to occupy
        // the complete available screen
        HomepageStage.setMaximized(true);

        HomepageStage.show();

        // =========================================================
        // LOADER ANIMATION
        // 0% → 100% IN 3 SECONDS
        // =========================================================

        Timeline timeline = new Timeline(

            new KeyFrame(
                Duration.ZERO,
                new javafx.animation.KeyValue(
                    progressBar.progressProperty(),
                    0
                )
            ),

            new KeyFrame(
                Duration.seconds(3),
                new javafx.animation.KeyValue(
                    progressBar.progressProperty(),
                    1
                )
            )
        );

        timeline.play();

        // =========================================================
        // AFTER 3 SECONDS → LOGIN PAGE
        // =========================================================

        PauseTransition delay =
            new PauseTransition(
                Duration.seconds(3)
            );

        delay.setOnFinished(event -> {

            Loginpage loginpage =
                new Loginpage();

            try {

                HomepageStage.setScene(
                    loginpage.getLoginPageScene()
                );

            } catch (Exception e) {

                e.printStackTrace();
            }
        });

        delay.play();
    }
}