package com.company.design;

import com.company.design.adapter.*;
import com.company.design.singleton.AClazz;
import com.company.design.singleton.BClazz;
import com.company.design.singleton.SocketClient;
import org.w3c.dom.ls.LSOutput;

public class Main {

    public static void main(String[] args) {
        /*
        // 싱글톤 패턴 테스트
        AClazz aClazz = new AClazz();
        BClazz bClazz = new BClazz();

        SocketClient aClient = aClazz.getSocketClient();
        SocketClient bClient = bClazz.getSocketClient();

        System.out.println("A와 B의 소켓클라이언트 객체가 동일한가?");
        System.out.println(aClient.equals(bClient));
        */

        // 어댑터 패턴 테스트
        // 헤어드리이기는 110볼트라서 바로 110볼트짜리 콘센트에 연결 쌉가능
        /*
        HairDryer hairDryer = new HairDryer();
        connect(hairDryer);

        // 청소기는 220볼트라서 110볼트짜리 콘센트에 연결 못함...중간에 어뎁터를 만들어서 연결 하는 시나리오
        Cleaner cleaner = new Cleaner();
        Eletronic110V adapter = new SocketAdapter(cleaner);
        connect(adapter);

        AirConditioner airConditioner = new AirConditioner();
        Eletronic110V airAdapter = new SocketAdapter(airConditioner);
        connect(airAdapter);
        */

        // 프록시 패턴 테스트
    }

    /*
    public static void connect(Eletronic110V eletronic110V){
        eletronic110V.powerOn();
    }
    */

}
