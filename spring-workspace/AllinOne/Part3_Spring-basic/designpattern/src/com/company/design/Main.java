package com.company.design;

import com.company.design.singleton.AClazz;
import com.company.design.singleton.BClazz;
import com.company.design.singleton.SocketClient;
import org.w3c.dom.ls.LSOutput;

public class Main {

    public static void main(String[] args) {
        // 싱글톤 패턴 테스트
        AClazz aClazz = new AClazz();
        BClazz bClazz = new BClazz();

        SocketClient aClient = aClazz.getSocketClient();
        SocketClient bClient = bClazz.getSocketClient();

        System.out.println("A와 B의 소켓클라이언트 객체가 동일한가?");
        System.out.println(aClient.equals(bClient));
        
        // 어댑터 패턴 테스트
    }

}
