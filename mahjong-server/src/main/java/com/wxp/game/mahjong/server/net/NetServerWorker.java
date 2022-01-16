package com.wxp.game.mahjong.server.net;

import com.wxp.game.mahjong.common.domain.LoginMessage;
import com.wxp.game.mahjong.common.domain.MessageType;
import com.wxp.game.mahjong.common.domain.NetMessage;
import com.wxp.game.mahjong.common.net.NetWorker;
import com.wxp.game.mahjong.server.util.LogWorker;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.Socket;

/**
 * 维护一个socket, 可以时服务端的, 也可以是客户端的
 * 支持socket的读写操作以及后续动作。
 */
public class NetServerWorker extends NetWorker {
    private String clientId;
    @Getter
    private String ip;

    public NetServerWorker(Socket socket) throws IOException {
        super(socket);
        this.ip = socket.getInetAddress().toString();
    }

    public void dealWithReceiveData(NetMessage netMessage) {
        LogWorker.appendLog(String.format("receive data: %s", NetServerUtil.buildNetMessage(netMessage)));
        switch (netMessage.getType()) {
            case LOGIN:
                dealWithLoginMessage(netMessage);
                break;
        }

        this.lastAliveCheckPoint = System.currentTimeMillis();
        if (StringUtils.isNotEmpty(clientId)) {
            this.registerStatus = true;
        }
    }

    @Override
    public void testConnect() {
        if (System.currentTimeMillis() - this.lastAliveCheckPoint > 3000) {
            this.registerStatus = false;
            try {
                LogWorker.appendLog(String.format("try shutdown client %s .", ip));
                shutdown();
            } catch (IOException e) {
                LogWorker.appendLog("shutdown exception " + e.toString());
                e.printStackTrace();
            }
        }
    }

    private void dealWithLoginMessage(NetMessage netMessage) {
        LoginMessage loginMessage = NetServerUtil.parseLoginMessage(netMessage);
        this.clientId = loginMessage.getClientId();
        // 注册
        NetServerUtil.registerClient(clientId, this);
        this.registerStatus = true;
        // 回复响应
        String message = NetServerUtil.buildServerResp(netMessage.getMessageId(), MessageType.LOGIN_REPONSE);
        LogWorker.appendLog(String.format("response: %s", message));
        sendMessage(message);
    }
}
