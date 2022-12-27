package com.fastcampus.ch3.diCopy2;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

class Car {
}

class SportsCar extends Car {
}

class Truck extends Car {
}

class SUV extends Car {
}

class Engine {
}

class AppContext {
    Map map; // 객체 저장소

    AppContext() {
        // map = new HashMap(); // 생성자에서 map 생성
        // map.put("car", new SportsCar()); // key, value
        // map.put("engine", new Engine()); // key,  value

        try {
            Properties p = new Properties();
            p.load(new FileReader("config2.txt"));

            // Properties에 저장된 내용을 Map에 저장
            map = new HashMap(p);

            // 반복문으로 클래스 이름얻은 후 객체를 얻고 Map에 저장
            for (Object key : map.keySet()) {
                // 클래스 정보 얻어오기
                Class clazz = Class.forName((String) map.get(key));
                // 객체를 만들어 Map에 저장
                map.put(key, clazz.newInstance());
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Object getBean(String key) {
        return map.get(key); // key에 해당하는 객체 반환
    }
}

// 객체 컨테이너(저장소) 만들기
public class Main2 {
    public static void main(String[] args) throws Exception {
        AppContext ac = new AppContext();
        Car car = (Car) ac.getBean("car");
        Engine engine = (Engine) ac.getBean("engine");
        System.out.println("car = " + car);
        System.out.println("engine = " + engine);
    }

}
