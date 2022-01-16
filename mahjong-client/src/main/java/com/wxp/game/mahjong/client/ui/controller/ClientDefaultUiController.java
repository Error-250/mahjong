package com.wxp.game.mahjong.client.ui.controller;

import com.wxp.game.mahjong.client.net.NetClientManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ClientDefaultUiController {
    @FXML
    Button startGame;

    public void onStartGameClicked(MouseEvent mouseEvent) {
        try {
            NetClientManager.initClientNet();
            NetClientManager.doLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
