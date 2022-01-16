package com.wxp.game.mahjong.common.net;

import com.wxp.game.mahjong.common.domain.NetMessage;
import lombok.Getter;

import java.io.*;
import java.net.Socket;

/**
 * 维护一个socket, 可以时服务端的, 也可以是客户端的
 * 支持socket的读写操作以及后续动作。
 */
public abstract class NetWorker extends Thread {
    private Socket socket;
    // 线程是否还在运行
    private boolean isRunning;
    // 客户端注册状态
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;

    @Getter
    protected boolean registerStatus = false;
    protected long lastAliveCheckPoint = 0;

    public NetWorker(Socket socket) throws IOException {
        this.socket = socket;
        isRunning = true;
        inputStream = new BufferedInputStream(socket.getInputStream());
        outputStream = new BufferedOutputStream(socket.getOutputStream());
    }

    public abstract void dealWithReceiveData(NetMessage netMessage);

    public abstract void testConnect();

    public void shutdown() throws IOException {
        isRunning = false;
        socket.close();
    }

    public boolean sendMessage(String message) {
        try {
            outputStream.write(message.getBytes());
            outputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void run() {
        int dataSize = 10;
        byte[] data = new byte[dataSize];
        while (isRunning) {
            // 接受数据
            try {
                if (inputStream.available() > 0) {
                    int allDataCount = inputStream.available();
                    StringBuilder stringBuilder = new StringBuilder();
                    int readDataCount = 0;
                    while (readDataCount < allDataCount) {
                        int currentCount = inputStream.read(data);
                        stringBuilder.append(new String(data, 0, currentCount));
                        if (currentCount < 0) {
                            break;
                        }
                        readDataCount += currentCount;
                    }
                    String originData = stringBuilder.toString();
                    NetMessage netMessage = NetUtil.readNetMessage(originData);
                    dealWithReceiveData(netMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("" + registerStatus + lastAliveCheckPoint);
            if (registerStatus && System.currentTimeMillis() - lastAliveCheckPoint > 1000) {
                // 检查连接
                testConnect();
            }
        }
    }
}
