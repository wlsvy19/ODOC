package com.company.design.singleton;

public class BClazz {
    private SocketClient socketClient;

    public BClazz() {
        this.socketClient = SocketClient.getInstance();
    }

/*
    public BClazz() {
        // 소켓클라이언트 객체 새로 생성
        this.socketClient = new SocketClient();
    }
*/

    public SocketClient getSocketClient() {
        return this.socketClient;
    }
}
