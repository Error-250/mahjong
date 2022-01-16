package com.wxp.game.mahjong.server.ui.controller;

import com.wxp.game.mahjong.server.MainServer;
import com.wxp.game.mahjong.server.net.NetServerManager;
import com.wxp.game.mahjong.server.net.NetServerUtil;
import com.wxp.game.mahjong.server.ui.ServerUIEnum;
import com.wxp.game.mahjong.server.util.LogWorker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ServerDefaultUiController {
    @FXML
    Button startService;

    public void startServiceClicked(MouseEvent mouseEvent) {
        LogWorker.appendLog("start service");
        try {
            NetServerUtil.initNet();
            MainServer.showUi(ServerUIEnum.SERVER_ON_SERVICE);
        } catch (IOException e) {
            LogWorker.appendLog(e.toString());
            e.printStackTrace();
        }
    }
}
