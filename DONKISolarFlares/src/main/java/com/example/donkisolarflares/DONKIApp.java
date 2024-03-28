package com.example.donkisolarflares;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DONKIApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DONKIApp.class.getResource("DONKIv2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 746, 450);
        stage.setTitle("Solar Flare Dates");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}