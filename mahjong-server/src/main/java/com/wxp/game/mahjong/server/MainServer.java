package com.wxp.game.mahjong.server;

import com.wxp.game.mahjong.server.net.NetServerUtil;
import com.wxp.game.mahjong.server.ui.ServerUIEnum;
import com.wxp.game.mahjong.server.util.LogWorker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainServer extends Application {
    public static Stage mainStage;
    private static Map<String, Scene> uiMap = new HashMap<>();

    public static void main(String[] args) {
        LogWorker.init();
        launch(args);
    }

    private void loadUi() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/ui/ServerDefaultUi.fxml"));
        uiMap.put(ServerUIEnum.DEFAULT.getName(), new Scene(parent));
        parent = FXMLLoader.load(getClass().getResource("/ui/ServerOnServiceUi.fxml"));
        uiMap.put(ServerUIEnum.SERVER_ON_SERVICE.getName(), new Scene(parent));
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        mainStage = primaryStage;
        loadUi();

        primaryStage.setTitle("麻将");
        primaryStage.setScene(uiMap.get(ServerUIEnum.DEFAULT.getName()));
        primaryStage.setOnCloseRequest(e -> {
            LogWorker.shutdown();
            try {
                NetServerUtil.stopNet();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        primaryStage.show();
    }

    public static void showUi(ServerUIEnum serverUIEnum) {
        mainStage.setScene(uiMap.get(serverUIEnum.getName()));
    }
}
