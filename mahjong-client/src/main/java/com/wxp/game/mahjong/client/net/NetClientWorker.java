package com.wxp.game.mahjong.client.net;

import com.wxp.game.mahjong.common.domain.NetMessage;
import com.wxp.game.mahjong.common.net.NetWorker;

import java.io.IOException;
import java.net.Socket;

public class NetClientWorker extends NetWorker {
    public NetClientWorker(Socket socket) throws IOException {
        super(socket);
    }

    public void dealWithReceiveData(NetMessage netMessage) {
        switch (netMessage.getType()) {
            case LOGIN_REPONSE:
                confirmMessageSendSuccess(netMessage.getMessageId());
                break;
        }
    }

    @Override
    public void testConnect() {
        NetMessage netMessage = NetClientUtil.buildTestMessage();
        boolean isSuccess = sendMessage(NetClientUtil.buildNetMessage(netMessage));
        if (!isSuccess) {
            registerStatus = false;
        } else {
            this.lastAliveCheckPoint = System.currentTimeMillis();
        }
    }

    public void confirmMessageSendSuccess(String messageId) {
//        NetClientManager.confirmMessage(messageId);
        this.registerStatus = true;
    }

    public void sendLoginMessage(String clientId) {
        NetMessage netMessage = NetClientUtil.buildLoginMessage(clientId);
        String message = NetClientUtil.buildNetMessage(netMessage);
        sendMessage(message);
    }
}
