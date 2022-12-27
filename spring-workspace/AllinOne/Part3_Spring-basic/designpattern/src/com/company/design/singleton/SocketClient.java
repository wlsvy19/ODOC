package com.company.design.singleton;

/**
 * 싱글톤: 객체를 최초 한번만 생성하여 다른 곳 에서도 공유하며 사용
 */
public class SocketClient {

    private static SocketClient socketClient = null;

    // private: 기본 생성자를 으로 외부접근 막음
    private SocketClient() {
    }

    // public: 외부접근 허용
/*
    public SocketClient() {
    }
*/

    // static이기 때문에 다른 클래스에서SocketClient.getInstance()으로 접근 가능
    public static SocketClient getInstance() {
        if (socketClient == null) {
            socketClient = new SocketClient();
        }
        return socketClient;
    }

    public void connect() {
        System.out.println("connect!!");
    }
}
