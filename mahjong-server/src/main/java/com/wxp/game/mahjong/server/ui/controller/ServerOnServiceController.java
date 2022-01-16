package com.wxp.game.mahjong.server.ui.controller;

import com.wxp.game.mahjong.server.MainServer;
import com.wxp.game.mahjong.server.net.NetServerManager;
import com.wxp.game.mahjong.server.net.NetServerUtil;
import com.wxp.game.mahjong.server.ui.ServerUIEnum;
import com.wxp.game.mahjong.server.util.LogWorker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerOnServiceController implements Initializable {
    @FXML
    Button closeServerBtn;
    @FXML
    TextArea logTextArea;

    public void initialize(URL location, ResourceBundle resources) {
        logTextArea.textProperty().bind(LogWorker.getLogProperty());
        logTextArea.scrollTopProperty().add(logTextArea.textProperty().length());
    }

    public void onCloseServerBtnClicked(MouseEvent mouseEvent) {
        LogWorker.appendLog("close server.");
        try {
            NetServerUtil.stopNet();
            MainServer.showUi(ServerUIEnum.DEFAULT);
        } catch (IOException e) {
            LogWorker.appendLog(e.toString());
            e.printStackTrace();
        }
    }
}
