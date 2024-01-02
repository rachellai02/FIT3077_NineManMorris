package com.example.ninemanmorris_team10;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Sets up the user interface for the game. The beginning of everything.
 */
public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("main-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Welcome to Nine Men Morris Game!");
        URL iconUrl = GameApplication.class.getResource("9mm_icon.jpg");
        String iconPath = iconUrl.toExternalForm();

        stage.getIcons().add(new Image(iconPath));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}