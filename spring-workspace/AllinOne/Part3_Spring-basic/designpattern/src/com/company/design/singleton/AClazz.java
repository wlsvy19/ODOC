package com.company.design.singleton;

public class AClazz {
    private SocketClient socketClient;


    public AClazz() {
        // 객체 생성 방지하기 위해 기본생성자를 private으로 막음
        //this.socketClient = new SocketClient();

        this.socketClient = SocketClient.getInstance();
    }

/*
    public AClazz() {
        // 소켓클라이언트 객체 새로 생성
        this.socketClient = new SocketClient();
    }
*/

    public SocketClient getSocketClient() {
        return this.socketClient;
    }
}
