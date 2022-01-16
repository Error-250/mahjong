package com.wxp.game.mahjong.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainClient extends Application {
    public static Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        mainStage = primaryStage;

        Parent parent = FXMLLoader.load(getClass().getResource("/ui/ClientDefaultUi.fxml"));

        primaryStage.setTitle("麻将");
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }
}
